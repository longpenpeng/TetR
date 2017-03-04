package temp;
import java.io.BufferedReader;
 
import java.io.FileReader;
 
import java.io.PrintWriter;
 

public class Mv {
	public static void main(String[] args) {
		String logPath = args[0];
		String out =args[1];
		try {
			String acc = "";
			String line = "";
			PrintWriter writer = new PrintWriter(out);
			BufferedReader in= new BufferedReader(new FileReader(logPath));
			while((line = in.readLine())!=null){
				String[] spt = line.split(" +");
				if(spt.length ==3){
					 acc = spt[2].trim().substring(0,spt[2].trim().indexOf("#"));
					 writer.write(acc+ "\n");
				}
			}
			in.close();
			writer.flush();
			writer.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
