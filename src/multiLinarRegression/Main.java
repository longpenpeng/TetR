package multiLinarRegression;

import java.io.PrintWriter;
import java.text.Format;
import java.util.List;

import io.PubFunc;
import seqAlign.NuclMatrix;
import seqAlign.SeqAlign;
import svm.DnaPortPair;

public class Main {
	 
	public static void main(String[] args) {
		
		String pairs = "/home/longpengpeng/refine_TETR/test/test_multiRE/merge/0";
		String out ="/home/longpengpeng/refine_TETR/test/test_multiRE/0";
		code(pairs, out);
	}
	public static void code(String pairs,String out) {
		String prot1;
		String prot2;
		String nucl1;
		String nucl2;
		String acc1;
		String acc2;
		DnaPortPair dpp1;
		DnaPortPair dpp2;
		List<DnaPortPair> dpps = DnaPortPair.getDnaPortPairsFromFile(pairs);
		NuclMatrix nm = new NuclMatrix();
		try {
			PrintWriter writer = new PrintWriter(out);
			double ident;
			for(int i = 0; i<dpps.size(); i++) {
				dpp1 = dpps.get(i);
				prot1 = dpp1.getProtSeq();
				nucl1 = dpp1.getNuclList().get(0);
				acc1 = dpp1.getAcc();
						
				for(int j = i+1; j<dpps.size(); j++) {
					dpp2 = dpps.get(j);
					prot2 = dpp2.getProtSeq();
					nucl2 = dpp2.getNuclList().get(0);
					acc2 = dpp2.getAcc();
					System.out.println(nucl1 + "  " + nucl2);
					ProtVar pv = new ProtVar(prot1, prot2, acc1, acc2);
					ident = SeqAlign.localIdentity(nucl1, nucl2, nm);
					writer.write(pv.toString() + String.format("%.2f",ident) + PubFunc.lineSeparator); 
					writer.flush(); 
				} 
			}
			writer.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}

}
