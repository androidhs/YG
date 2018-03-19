package android.eq366pt.zxtnetwork.com.yg;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReadUtils {

	public static void read(String file){
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			String line;
			while((line = br.readLine()) != null) {
				System.out.println(line);
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
