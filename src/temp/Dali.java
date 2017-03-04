package temp;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import io.PubFunc;

public class Dali {
	public  static void  extractPDBId(String path, String out) {
		List<String> list = PubFunc.getListFromFile(path, 3, " +");
		List<String> idList = new ArrayList<String>();
		for(String str : list){
			
			System.out.println(str);
			int index = str.indexOf("-");
			String id = str.substring(0,index);
			if(idList.contains(id)){
				continue;
			}else {
				idList.add(id);
			} 
		}
		try {
			PrintWriter writer = new PrintWriter(out);
			for(String id:idList){
				writer.write(id + "  ");
				writer.flush();
			}
			writer.close();
		} catch (IOException e) {
			// TODO: handle exception
		}
	}
	public static void 	main(String args[]) {
		String path = "/home/longpengpeng/dali.txt";
		String out = "/home/longpengpeng/daliIdOut";
		extractPDBId(path, out);
		
	}
}
