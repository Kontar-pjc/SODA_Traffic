package com.NJUPT.Algorithm;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;   
import java.util.List;
  
public class floydWarshall {   
	boolean debug=true;

    private static double INF=Integer.MAX_VALUE;  
    //设定最大值，A到不了B，认为A-B之间为INF  最好用边数*权的上限（路的条数*所有距离中最大的）
    
    private  double[][] dist;   
    //顶点i 到 j的最短路径长度，初值是i到j的边的权重    
    
    private double[][] path;     
    private List< Integer> result=new ArrayList< Integer>();   
       
    public static void main(String[] args) throws IOException {   
    	Integer siteCount=Integer.parseInt(args[2]); //站点个数
    	floydWarshall graph=new floydWarshall(siteCount);   

        double[][] arr=new double[siteCount][siteCount]; //数组初始化 
        for(int n=0;n<siteCount;n++){
        	for(int m=0;m<siteCount;m++){
        		if(m==n)
        			arr[n][m]=0;
        		else
        			arr[n][m] = INF;
        	}
        }
        
        
        int row = 0,col=0;    
        BufferedReader readerA = null;
        try{
        File file = new File(args[0]); 
        //存放数组数据的文件
        readerA = new BufferedReader(new FileReader(file));  
        String line;  
        while((line = readerA.readLine()) != null){
        	
		         String[] temp = line.trim().split("\t");  
		         String sitesAndTime[]=temp[1].split("-");
		         
		         String siteA=sitesAndTime[0];
		         String siteB=sitesAndTime[1];
		         String time=sitesAndTime[2];
		     
		         row=Integer.parseInt(siteA) ;
		         col=Integer.parseInt(siteB);
		        if(row<=5000 && col <=5000) 
			         {
			            arr[row][col]=Double.parseDouble(time);
			            //System.out.println(arr[row][col]); 
			         }
		        }
            }catch (IOException e) {
            e.printStackTrace();
       } finally {            
            try {
                if (readerA!= null)readerA.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        		  }

        
        
   	 File f = new File(args[1]) ;

	   OutputStream out = null ;
	   out = new FileOutputStream(f) ;
	   
      if(graph.debug)
	   System.out.println("数组赋值完成！");
    	  
	   
	   
//设定起始点 结束点
        int begin=0;   
        int end=0;   
        
        List< Integer> list ;
        int rows=arr.length;
        int cols=arr[0].length;
        if(rows<cols)
        	rows=cols; //找最大的即可
//输出任意两地之间最短距离        
        for(begin=0;begin<rows;begin++){
        	for(end=0;end<rows;end++){
        		graph.findCheapestPath(begin,end,arr); 
                list=graph.result;  
        		if(begin!=end && graph.dist[begin][end]<INF) 
        		{
                    System.out.println(begin+" to "+end+",The shortest path is:");   
                    System.out.println(list.toString());   
                    System.out.println(graph.dist[begin][end]); 
                    String line=begin+"|"+end+"@"+list.toString()+"|"+graph.dist[begin][end];
                    line+="\r\n";
                    out.write(line.getBytes());
        		}
        		list.clear();
        	}
        }
        out.close();
    }   
  
    public  void findCheapestPath(int begin,int end,double[][] arr){   
        floyd(arr);   
        result.add(begin);   
        findPath(begin,end);   
        result.add(end);   
    }   
       
    public void findPath(int i,int j){   
        int k=(int)path[i][j];   
        if(k==-1)return;   
        findPath(i,k);   //递归
        result.add(k);   
        findPath(k,j);   
    }   
  
    public void floyd(double[][] matrix){   
        int size=matrix.length;   
        
        //初始化   
        for(int i=0;i< size;i++){   
            for(int j=0;j< size;j++){   
                path[i][j]=-1;   
                dist[i][j]=matrix[i][j];   
            }   
        } 
        
        
        
        for(int k=0;k< size;k++){   
            for(int i=0;i< size;i++){   
                for(int j=0;j< size;j++){   
                    if(
                    	dist[i][k]!=INF&&   
                        dist[k][j]!=INF&&   
                        dist[i][k]+dist[k][j]< dist[i][j]
                       )
                    {
                        dist[i][j]=dist[i][k]+dist[k][j];   
                        path[i][j]=k;   
                    }   
                }   
            }   
        }   
           
    }   
       
    public floydWarshall(int size){   
        this.path=new double[size][size];   
        this.dist=new double[size][size];   
    }   
}    