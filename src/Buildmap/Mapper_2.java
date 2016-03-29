package Buildmap;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;



public class Mapper_2 extends Mapper<LongWritable,Text,Text,Text>{
    String [] weeklist = {"","Sun","Mon","Tue","Wed","Thu","Fri","Sat"};
    String Index;
	@Override
	public void map(LongWritable key, Text values,Context context) throws IOException, InterruptedException {
		// 059L0828    7F870000,2015-01-01 09:59:52|7B7C0008,2015-01-01 14:06:16|......
		String str = values.toString();
		String row = str.split("\t")[1];
		String station_time[] = row.split("\\|");
		int strsize = station_time.length;
		
		for(int i = 0;i<strsize-2;i++){
			String value_a = station_time[i];
			String value_b = station_time[i+1];
			String station_a = value_a.split(",")[0];
			String station_b = value_b.split(",")[0];
			
			String[] a = value_a.split(",")[1].split(" ");

			String data_a = value_a.split(",")[1];
	    	String data_b = value_b.split(",")[1];
	    	
			String IndexTime = a[0] + " " + a[1];
			Index = Judge(IndexTime);
			
	    	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    	long diff = 0;
	    	try{
	    		java.util.Date d1 = df.parse(data_a);
	    		java.util.Date d2 = df.parse(data_b);
	    		diff = d2.getTime() - d1.getTime();
	    	}catch (Exception e){

	    	}
	    	if(diff<=1200000){
	    		String currentkey = Index;
		    	String currentvalue = station_a + "-" +station_b + "=" + diff/1000;
//		    	if(currentvalue.equals("657C0000-657B0000=77")){
//		    		System.out.println("bug");
//		    	}
//		    	System.out.println("Mapper2"+currentvalue);
		    	context.write(new Text(currentkey), new Text(currentvalue));
		    	//Thu0902 7F870000-7B7C0008=888
	    	}
		}
	}
	
	public String Judge(String keytime) {
		//2015-01-01 09:59:52
		String[] key_1 = keytime.split(" ")[0].split("-");
		String[] key_2 = keytime.split(" ")[1].split(":");
		String week = new String();
		Calendar calendar = Calendar.getInstance();//获得一个日历
	    calendar.set(Integer.parseInt(key_1[0]), Integer.parseInt(key_1[1])-1, Integer.parseInt(key_1[2]));//设置当前时间,月份是从0月开始计算
	    int number = calendar.get(Calendar.DAY_OF_WEEK);//星期表示1-7，是从星期日开始
	    week=weeklist[number].toString();
		if( Integer.parseInt(key_2[1])-30>0){
			return week + key_2[0]+"02";
		}else {
			return week + key_2[0]+"01";
		}
	}
}
