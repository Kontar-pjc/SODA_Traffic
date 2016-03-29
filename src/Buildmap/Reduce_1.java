package Buildmap;

import java.io.IOException;
import java.util.Comparator;
import java.util.PriorityQueue;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;


public class Reduce_1 extends  Reducer<Text,Text,Text,Text> {

	
	public void reduce(Text key, Iterable<Text> values,Context context) throws IOException, InterruptedException {
		  //key 059L0828 value 7F870000,2015-01-01 09:59:52
		  Comparator<String> comparator =(Comparator<String>) new Minstationtime();
		  PriorityQueue<String> carTimeQueue = new PriorityQueue<String>(1,comparator);
	      String currentlineofPairs = new String();	
	      while (values.iterator().hasNext()){
	    	  String currentPair = values.iterator().next().toString();  
	    	  carTimeQueue.add(currentPair);  
	      }
	      int listsize = carTimeQueue.size();
	      for(int x = 0;x<listsize;x++){
	    	  
	    	  String str = carTimeQueue.remove();
	    	 
	    	  String station = str.split(",")[0];
	    	  int LineLong = currentlineofPairs.split("\\|").length;
    		  String lastStation = currentlineofPairs.split("\\|")[LineLong-1].split(",")[0]; 
    		  if(!station.equals(lastStation)) {
    			  currentlineofPairs = currentlineofPairs  + str + "|";
    		  }
	      }
//		   try {
			context.write(new Text(key.toString()),new Text(currentlineofPairs));
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		}
	}

