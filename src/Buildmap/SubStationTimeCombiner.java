package Buildmap;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;


public class SubStationTimeCombiner extends Reducer<Text,Text,Text,Text>{
	
	public void reduce (Text key,Iterable< Text>  values,Context context) throws IOException, InterruptedException{
    	//Thu0902 7F870000-7B7C0008=888,7F870000-7B7C0008=666,7F870000-7B7C0008=888.....
		HashMap<String, String> adjacencymap = new HashMap<String, String>();
		while(values.iterator().hasNext()){
				String currentpair = values.iterator().next().toString();
				String currentstationpair = currentpair.split("=")[0];
				String currenttimediffrence = currentpair.split("=")[1];
				String currenthashkey = key.toString()+"|"+ currentstationpair;
				if (!adjacencymap.containsKey(currenthashkey)){
				adjacencymap.put(currenthashkey, currenttimediffrence);
				}
				else{
					String oldvalue =adjacencymap.get(currenthashkey);
					String newvalue =currenttimediffrence+","+oldvalue;
					adjacencymap.put(currenthashkey, newvalue);
				}
		}
				Iterator iter = adjacencymap.entrySet().iterator();
				while(iter.hasNext()){
					Map.Entry entry = (Map.Entry) iter.next();
					String hashkey =  entry.getKey().toString();
					String hashvalue = entry.getValue().toString();
					String timelist[] = hashvalue.split(",");
					int sumtime = 0;
					for(String time : timelist){
						sumtime+= Integer.parseInt(time);
					}
					String currentvalue = sumtime+","+ timelist.length;
//					System.out.println("Combiner_2"+hashkey+"|"+currentvalue);
					context.write(new Text(hashkey.split("\\|")[0]), new Text(hashkey.split("\\|")[1]+"="+currentvalue));
				
			}
		}
}


