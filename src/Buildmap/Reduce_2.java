package Buildmap;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;


public class Reduce_2 extends  Reducer<Text,Text,Text,Text>{
	//Thu0001	78820003-78820002=257,1
	//	Thu0001	81940001-82940001=160,2
	private MultipleOutputs<Text, Text> output;
	
    protected void setup(Context context
    ) throws IOException, InterruptedException {
        output = new MultipleOutputs<Text, Text>(context);
    }
	@Override
	public void reduce(Text key, Iterable<Text> values,Context context) throws IOException, InterruptedException {
		HashMap<String, String> adjacencymap = new HashMap<String, String>();
		while (values.iterator().hasNext()) {
			String hashpair = values.iterator().next().toString();
			String hashkey = hashpair.split("-")[0];
			String hashvalue = hashpair.split("-")[1].split("=")[0]+"-"+hashpair.split("-|=|,")[2]+"-"+hashpair.split("-|=|,")[3];
			if(!adjacencymap.containsKey(hashkey)){
				adjacencymap.put(hashkey, hashvalue);
			}else{
				String currenthashvalue = adjacencymap.get(hashkey)+"*"+hashvalue;
				adjacencymap.put(hashkey, currenthashvalue);
			}
		}
		Iterator<?> iter = adjacencymap.entrySet().iterator();
		String value = "";
		while(iter.hasNext()){
			Map.Entry entry = (Map.Entry) iter.next();
			String hashkey =  entry.getKey().toString();
			String hashvalue = entry.getValue().toString();
//			value = value + hashkey+"@"+hashvalue + "|";
			output.write(key, new Text(hashvalue),key.toString());
//			Thu0001	7F91000D@80910000-106-3|80940004@8095000E-250-3|8095000E@80980001-486-3*80950000-145-2|80980001@81980001-121-4|
		}
//		output.write(key, new Text(value),key.toString());
	}
	
    protected void cleanup(Context context
    ) throws IOException, InterruptedException {
        output.close();
    }
}

