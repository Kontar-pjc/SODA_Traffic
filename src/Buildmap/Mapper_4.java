package Buildmap;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class Mapper_4 extends  Mapper<LongWritable,Text,Text,Text>{
	public void map(LongWritable keys, Text values, Context context) throws IOException {
		String Index = values.toString().split("\t")[0];
		String value = values.toString().split("\t")[1];
		try {
			context.write(new Text(Index), new Text(value));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
