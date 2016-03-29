package Buildmap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;


public class Reduce_4 extends  Reducer<Text,Text,Text,Text>{
	
	private MultipleOutputs<Text, Text> output;
	
    protected void setup(Context context
    ) throws IOException, InterruptedException {
        output = new MultipleOutputs<Text, Text>(context);
    }
    
	public void reduce(Text keys, Iterable<Text> values, Context context) throws IOException {
		//key :Index Value :X,Y@X,Y-ST-N|X,Y@X,Y-ST-N*X,Y-ST-N|X,Y@X,Y-ST-N...
		
		
		String CoordinatePairCurrent = new String();
		//------------------------------------------------------------------------------------------//
		String CoordinateVal = new String();
		HashMap<String, String> key_valCoordinateHmap = new HashMap<String, String>();
		ArrayList<Object> KeyCoorList = new ArrayList<Object>();
		String CoorCurrent = new String();
		
		while(values.iterator().hasNext()){
			CoordinateVal = CoordinateVal + values.iterator().next().toString() + "|";
		}
		
		String[] CoordinatePair = CoordinateVal.split("\\|");
		for(int i = 0;i<CoordinatePair.length;i++){
			String keyCoorPair = CoordinatePair[i].split("@")[0];
			String valCoorCurrent = CoordinatePair[i].split("@")[1];
			if(key_valCoordinateHmap.containsKey(keyCoorPair)){
				String hashVal = key_valCoordinateHmap.get(keyCoorPair);
				String addVal = hashVal + "*" + valCoorCurrent;
				key_valCoordinateHmap.put(keyCoorPair, addVal);
			}else {
				key_valCoordinateHmap.put(keyCoorPair, valCoorCurrent);
				KeyCoorList.add(keyCoorPair);
			}	
		}
		for(int j = 0;j<KeyCoorList.size();j++){
			String key = KeyCoorList.get(j).toString();
			CoorCurrent = CoorCurrent + key + "@" + key_valCoordinateHmap.get(key) + "|";
		}
		
		//------------------------------------------------------------------------------------------//
		
		
		
		String[] CoordinatePair1 = CoorCurrent.split("\\|");
		for(int i = 0;i<CoordinatePair1.length;i++){
			String keyCoorPair = CoordinatePair1[i].split("@")[0];
			String valCurrent = CoordinatePair1[i].split("@")[1];
			
			HashMap<String, String> Coordinate_TimeHmap = new HashMap<String, String>();
			
			if(valCurrent.indexOf("*")!=-1){
				String[] value = valCurrent.split("\\*");
				for(int j = 0;j<value.length;j++){
					String valCoorPair = value[j].split("-")[0];
					if(Coordinate_TimeHmap.containsKey(valCoorPair)){
						String hmapVal = Coordinate_TimeHmap.get(valCoorPair);
						int SubTime = Integer.parseInt(value[j].split("-")[1]) + Integer.parseInt(hmapVal.split("-")[0]);
						int N = Integer.parseInt(value[j].split("-")[2]) + Integer.parseInt(hmapVal.split("-")[1]);
						String addVal = SubTime + "-" + N;
						Coordinate_TimeHmap.put(valCoorPair, addVal);
					}else {
						int SubTime = Integer.parseInt(value[j].split("-")[1]);
						int N = Integer.parseInt(value[j].split("-")[2]);
						String addVal = SubTime + "-" + N;
						Coordinate_TimeHmap.put(valCoorPair, addVal);
					}
				}
				
			}else {
				String valCoorPair = valCurrent.split("-")[0];
				String addVal = valCurrent.split("-")[1] + "-" + valCurrent.split("-")[2];
				Coordinate_TimeHmap.put(valCoorPair, addVal);
			}
		
			Iterator iter = Coordinate_TimeHmap.entrySet().iterator();
			String Current = new String();
			while(iter.hasNext()){
				Map.Entry entry = (Map.Entry) iter.next();
				String key =  entry.getKey().toString();
				String value = entry.getValue().toString();
				int averageTime = Integer.parseInt(value.split("-")[0])/Integer.parseInt(value.split("-")[1]);
				String Pair = key + "-" + averageTime;
				Current = Current + Pair + "*";
			}
			CoordinatePairCurrent = CoordinatePairCurrent + keyCoorPair + "@" + Current + "|";
		}
		
		try {
			CoordinatePairCurrent=CoordinatePairCurrent.substring(0, CoordinatePairCurrent.length()-1);
			output.write(new Text(keys), new Text(CoordinatePairCurrent),keys.toString());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	 @Override
	    protected void cleanup(Context context
	    ) throws IOException, InterruptedException {
	        output.close();
	    }
}
