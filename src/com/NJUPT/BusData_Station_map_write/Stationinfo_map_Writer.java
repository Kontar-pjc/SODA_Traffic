package com.NJUPT.BusData_Station_map_write;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class Stationinfo_map_Writer {
	
	public static void main(String[] args) throws IOException {
        File inputfile = new File(new String("/home/kontar/SODA_files/官方数据stationInfo.csv"));
        String line = null;
        Integer index = 0;
        FileInputStream in=new FileInputStream(inputfile); 
        		Map<String,String> stationlocationhashmap = new HashMap<String,String>();
        		Map<String,String> stationnamehashmap = new HashMap<String,String>();
        		Map<String, String> stationindexhashmap = new HashMap<String, String>();
            	BufferedReader br=new BufferedReader(new InputStreamReader(in));
            	while ((line = br.readLine()) != null) {
//            		System.out.println(line);
            			String[] info = line.split(",");
            			if(info.length<6|info[4].equals("0")|info[5].equals("0")){}
            			else{
                				String stationid = info[2];
                				String stationname = info[3];
	            				String longitude = info[4];
	            				String latitude =info[5];
	            				String location =latitude +","+ longitude;
	            				stationlocationhashmap.put(stationid,location);
	            				if(!stationindexhashmap.containsKey(stationid)){		            				
	            					stationindexhashmap.put(stationid, index.toString());
	            					stationnamehashmap.put(stationid, stationname);
	            					System.out.println(stationid+","+index.toString());
	            					index++;
	            				}
            			}
            	}
            	writer("id->location.hashmap",stationlocationhashmap);
            	writer("station_name.hashmap", stationnamehashmap);
            	br.close();
//            	System.out.println(stationindex);
            	System.out.println(index);
        }
 


		
		public static void writer(String filename,Map<String, String> adjacencymap){
					File stationinfo = new File("/home/kontar/SODA_files/Direction_hashmap/"+filename);
					ObjectOutputStream oos = null;
					try {
							oos = new ObjectOutputStream( new FileOutputStream(stationinfo));
							oos.writeObject(adjacencymap);
					} catch (FileNotFoundException e) {
							e.printStackTrace();
					} catch (IOException e) {
							e.printStackTrace();
					}finally{
							try {
									oos.flush();
									oos.close();
							} catch (IOException e) {
									e.printStackTrace();
							}
				}
		}

}
