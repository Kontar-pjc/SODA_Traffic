package toArray;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.util.GenericOptionsParser;



public class toArray {
		public static  HashMap<String, String> stationindexhashmap = new HashMap<String, String>();
		public static class TokenizerMapper extends Mapper<Object, Text, Text, Text>{  
				public  MultipleOutputs<Text, Text> output;
		
			    protected void setup(Context context
			    	    ) throws IOException, InterruptedException {
			    	        output = new MultipleOutputs<Text, Text>(context);
			    	    }
				
				public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
						String line = value.toString();
						String index = line.split("\t")[0];
						String pairs = line.split("\t")[1];
						String pair[] = pairs.split("\\|");
						String bufferline;
			    	   	File file = new File ("/home/kontar/Direction_hashmap/id->index_each/"+index+".txt");
						FileInputStream in=new FileInputStream(file);   
						InputStreamReader inReader=new InputStreamReader(in);   
						BufferedReader bufReader=new BufferedReader(inReader);
						while((bufferline = bufReader.readLine()) != null){
								String stationid = bufferline.split(",")[0];
								String stationindex = bufferline.split(",")[1];
								stationindexhashmap.put(stationid, stationindex);
						}
						bufReader.close();
					

			    	   for(String each:pair){
				    		   String startstation = each.split("@")[0];
				    		   String endstations[] = each.split("@")[1].split("\\*");
				    		   for(String eachendstation:endstations){
					    			   String currentvalue = stationindexhashmap.get(startstation)+"-"+stationindexhashmap.get(eachendstation.split("-")[0])+"-"+eachendstation.split("-")[1];
					    			   if(stationindexhashmap.containsKey(startstation)&stationindexhashmap.containsKey(eachendstation.split("-")[0])){
						    				   	output.write(new Text(index), new Text(currentvalue), index);
						    					System.out.println(index+currentvalue);
					    			   }
				    		   }
			    	   	}
				}
			    protected void cleanup(Context context) throws IOException, InterruptedException {
			    		output.close();
			    }
		}
		
		public static void main(String[] args) throws Exception {  
			    Configuration conf = new Configuration();  
			    String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();  

				
			    Job job=Job.getInstance();
			    job.setJarByClass(toArray.class);
			    job.setMapperClass(TokenizerMapper.class);
			    job.setOutputKeyClass(Text.class);
			    job.setOutputValueClass(Text.class);
			    FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
			    FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
			    job.waitForCompletion(true);
		}
}
