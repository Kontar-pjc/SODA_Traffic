package Buildmap;

import java.util.Comparator;


public class Minstationtime implements Comparator<String>
{
    @Override
    public int compare(String x, String y)
    {   
    	String[] a = x.split(",")[1].split(" ");
    	String[] b = y.split(",")[1].split(" ");
    	String[] data_a = a[0].split("-");
    	String[] time_a = a[1].split(":");
    	String[] data_b = b[0].split("-");
    	String[] time_b = b[1].split(":");
    	long formernum=Long.parseLong(data_a[0]+data_a[1]+data_a[2]+time_a[0]+time_a[1]+time_a[2]);
    	long latternum=Long.parseLong(data_b[0]+data_b[1]+data_b[2]+time_b[0]+time_b[1]+time_b[2]);	
        if (formernum<latternum)
        {
            return -1;
        }
        if (formernum>latternum)
        {
            return 1;
        }
        return 0;
    }
}
