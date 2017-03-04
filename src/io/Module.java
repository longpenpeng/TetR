package io;

import java.io.BufferedReader;


import java.io.File; 
import java.io.FileReader; 
import java.util.ArrayList; 
 

public class Module {
    protected	ArrayList<String> lines;
    protected	int lineNum;
	public  Module() 
	{
		this.lines = new ArrayList<String>();
		this.lineNum = 0;
	}
	public void add(String line)
	{
		this.lines.add(line);
		this.lineNum++;
	}
	public String readLine(int i)
	{
		return this.lines.get(i);
	}
	public int getNum()
	{
		return this.lineNum;
	}
	public static ArrayList<Module> getModules(String path,String regex) {
		 File file =new File(path);
		
		 String line="";
		 ArrayList<Module> modules =new ArrayList<Module>();
		 Module module=null ;
		 boolean bo=false;
		 try{
			 BufferedReader in =new BufferedReader(new FileReader(file));
			 while((line=in.readLine())!=null){
				 if(line.startsWith(regex)){
					 module=new Module();
					 modules.add(module);
					 bo=true;
				 }
				 if(bo==true){
					 module.add(line);
				 } 
			 } 
			 in.close();
		 }catch (Exception e) {
			 System.err.println("some exception happened when get modules from file"
					 + e.getMessage());
		 }
	
		 return modules;
	}
	public static ArrayList<Module> getModules(Module m,String regex) {
		 //File file =new File(path);
		
		 String line="";
		 ArrayList<Module> modules =new ArrayList<Module>();
		 Module module=null ;
		 boolean bo=false;
		 //try{
			// BufferedReader in =new BufferedReader(new FileReader(file));
		 for(int i = 0; i< m.getNum(); i++){
			 line = m.readLine(i);
			 if(line.startsWith(regex)){
				 module=new Module();
				 modules.add(module);
				 bo=true;
			 }
			 if(bo==true){
				 module.add(line);
			 }   
		 }  
		 return modules;
	}
	public String toString()
	{
		String line="";
		for(int i=0;i<this.getNum();i++)
		{
			line+=this.readLine(i) + PubFunc.lineSeparator;
		}
		return line;
	}

}
