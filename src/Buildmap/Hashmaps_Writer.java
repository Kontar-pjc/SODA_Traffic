package Buildmap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;

public class Hashmaps_Writer {
	
	public void HashmapWrite(String args,String hdfsurl) throws IOException {
		Configuration conf = new Configuration();
		conf.set("fs.default.name", hdfsurl);
												//由于配置文件问题，索性直接设定fs.default.name，以免报错Wrong FS
        FileSystem fs = FileSystem.get(conf);				//用FS获取configuration，使文件获取路径为HDFS
        Path inputDir = new Path(args);
        String line = null;
        FileStatus[] status = fs.listStatus(inputDir);
        Path[] listedPaths = FileUtil.stat2Paths(status);				//将输入的目录下所有文件的status转为path
        for (Path p : listedPaths) {
        		if (p.toString().contains("_SUCCESS")|p.toString().contains("part-r")){}
        		else{
		        		Map<String,String> adjacencymap = new HashMap<String,String>();
		        		String filename = new String();
		            	BufferedReader br=new BufferedReader(new InputStreamReader(fs.open(p)));
		            	System.out.println("Reading " + p.toString());
		            	while ((line = br.readLine()) != null) {
		            			filename = line.split("\t")[0];
		            			String[] HashPairsArray = line.split("\t")[1].split("\\|");
		            			for (String  hashpairs : HashPairsArray){
		            					String hashkey = hashpairs.split("@")[0];
		            					String hashvalue = hashpairs.split("@")[1];
		            					adjacencymap.put(hashkey, hashvalue);
		            			}
		            	}
		            	writer(filename,adjacencymap);
		            	br.close();
        		}
        }
 
}

		
		public static void writer(String filename,Map<String, String> adjacencymap){
					File stuInfo = new File("hashmaps/"+filename);
					ObjectOutputStream oos = null;
					try {
							oos = new ObjectOutputStream( new FileOutputStream(stuInfo));
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
