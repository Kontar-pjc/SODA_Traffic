package final_route;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class finaljava {
	public static HashMap<String, String> station_index_hashmap = new HashMap<String, String>();
	public static HashMap<String, String> station_location_hashmap = new HashMap<String, String>();
	public static HashMap<String, String> station_name_hashmap = new HashMap<String, String>();
	public static String transformfile = "Wed2301.txt";
    @SuppressWarnings("resource")
	public static void main(String[] args) throws IOException {
    	createhashmap();
    	station_location_hashmap=(HashMap<String, String>)reader("/home/kontar/SODA_files/Direction_hashmap/id->location.hashmap");
    	station_name_hashmap = (HashMap<String, String>)reader("/home/kontar/SODA_files/Direction_hashmap/id->name.hashmap");
    	//0|4@[0, 1, 447, 1455, 1457, 2099, 336, 314, 315, 904, 924, 1638, 1762, 135, 161, 568, 569, 2074, 2139, 2140, 1219, 1216, 166, 1268, 1243, 762, 764, 1353, 1496, 1497, 123, 3, 17, 2108, 2110, 127, 128, 2, 4]|4475.0
    	String line ;
    	File file = new File ("/home/kontar/SODA_files/FloydWarshall_output/"+transformfile);   
    	FileInputStream in=new FileInputStream(file);   
    	InputStreamReader inReader=new InputStreamReader(in);   
    	BufferedReader bufReader=new BufferedReader(inReader);  
    	File outfile = new File ("/home/kontar/SODA_files/In_Database/"+transformfile);
    	OutputStream out = null;
    	out = new FileOutputStream(outfile);
    	while((line = bufReader.readLine()) != null){
    		String indexofstations = line.split("@")[0];
    		String shortesttime = line.split("\\|")[2];
    		String route = line.split("@")[1].split("\\|")[0];
    		String station1_id = station_index_hashmap.get(indexofstations.split("\\|")[0]);
    		String station1_name = station_name_hashmap.get(station1_id);
    		String station2_id = station_index_hashmap.get(indexofstations.split("\\|")[1]);
    		String station2_name = station_name_hashmap.get(station2_id);
    		String spots_on_route[] = route.substring(1, route.length()-1).split(",");
    		String str_locations = "";
    		for(String eachspot:spots_on_route){
    			str_locations=str_locations+"-"+station_location_hashmap.get(station_index_hashmap.get(eachspot.trim()));
    		}
    		str_locations=str_locations.substring(1, str_locations.length());
    		String str_line = station1_name+"|"+station2_name + "@" + str_locations+"@"+shortesttime;
    		str_line += "\r\n";
    		out.write(str_line.getBytes());
    		System.out.println(str_line);
    		
    	}
    
    
    }
	
    
    //----------------------------------------------------读txt创建index哈希表---------------------------------------------------
    	public static void createhashmap() throws IOException{
				String bufferline;
			   	File file = new File ("/home/kontar/SODA_files/Direction_hashmap/id->index_each/"+transformfile);
				FileInputStream in=new FileInputStream(file);   
				InputStreamReader inReader=new InputStreamReader(in);   
				BufferedReader bufReader=new BufferedReader(inReader);
				while((bufferline = bufReader.readLine()) != null){
						String stationid = bufferline.split(",")[0];
						String stationindex = bufferline.split(",")[1];
						station_index_hashmap.put(stationindex, stationid);
				}
				bufReader.close();
		    }
    
	//---------------------------------------读出本地哈希表赋给经纬度哈希表------------------------------------
		    @SuppressWarnings("unchecked")
			public static Map<String, String> reader(String hashfilepath){
				Map<String,String> hashmap= null;
				File hashfile = new File(hashfilepath);
				ObjectInputStream ois = null;
				try {
					ois = new ObjectInputStream(new FileInputStream(hashfile));
					Object o = ois.readObject();
					hashmap = (Map<String,String>)o;
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}finally{
					try {
						ois.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return hashmap;
			}
    
    
}
