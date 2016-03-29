package Buildmap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;



public class Buildmap {
	public static void main(String[] args) throws Exception{
		Path tabel1 = new Path("table1");
		Path tabel2 = new Path("table2");
		
		//----------------------------------------------------job1----------------------------------------------------	
				Configuration conf=new Configuration();
				Job job=Job.getInstance(conf);
			    job.setJarByClass(Buildmap.class);
			    job.setMapperClass(Mapper_1.class);
			    job.setReducerClass(Reduce_1.class);
			    job.setOutputKeyClass(Text.class);
			    job.setOutputValueClass(Text.class);
			    FileInputFormat.addInputPath(job, new Path(args[0]));
			    FileOutputFormat.setOutputPath(job, tabel1);
			    System.out.println("job1.......");
			    job.waitForCompletion(true);
		
			    
			    
				//----------------------------------------------------job2----------------------------------------------------	
				Job job2=Job.getInstance(conf);
			    job2.setJarByClass(Buildmap.class);
			    job2.setMapperClass(Mapper_2.class);
			    job2.setCombinerClass(SubStationTimeCombiner.class);
			    job2.setReducerClass(Reduce_2.class);
			    job2.setOutputKeyClass(Text.class);
			    job2.setOutputValueClass(Text.class);
			    FileInputFormat.addInputPath(job2, tabel1);
			    FileOutputFormat.setOutputPath(job2, tabel2);
			    System.out.println("job2.......");
			    job2.waitForCompletion(true);
		

			    
			    Job job4=Job.getInstance(conf);
			    job4.setJarByClass(Buildmap.class);
			    job4.setMapperClass(Mapper_4.class);
			    //job4.setCombinerClass(Combiner.class);
			    job4.setReducerClass(Reduce_4.class);
			    job4.setOutputKeyClass(Text.class);
			    job4.setOutputValueClass(Text.class);
			    job4.setNumReduceTasks(1);
			    FileInputFormat.addInputPath(job4, tabel2);
			    FileOutputFormat.setOutputPath(job4, new Path(args[1]));
			    job4.waitForCompletion(true);
			    
			    //------------------------------------------Write Hashmaps to Local---------------------------------
//			    Hashmaps_Writer writer =new Hashmaps_Writer();
//			    String [] url = args[0].split("/");
//			    String hdfsurl = url[0]+"//"+url[2];
//			    writer.HashmapWrite(args[1],hdfsurl);
			  
			    
	}			
}
