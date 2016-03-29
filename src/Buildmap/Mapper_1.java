package Buildmap;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;


public class Mapper_1 extends  Mapper<LongWritable,Text,Text,Text>{
	public void map(LongWritable key, Text values,Context context) throws IOException {
		//059L0828,10065,7F870000,42,出站,上行,2015-01-01 09:59:52

		String str = values.toString();
		if(!isChinese(str.charAt(1))){
			String[] row1 = str.split(",");
			String plant = row1[0];
			String station = row1[2];
			String timString = row1[6];
			String current = station+","+timString;
			try {
//				System.out.println("Mapper1"+str);
				context.write(new Text(plant),new Text(current));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	private static boolean isChinese(char c) {
		
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }
	
}
