import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileReader1432 {

	private static BufferedInputStream br;

	FileReader1432(String fileName){
		try {
			br = new BufferedInputStream(new FileInputStream(fileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	int getEdgeSize() throws IOException{
		String temp="";
		int i;
		while((i=br.read())!=10) {
			temp += ((char)i)+"";
		}
		//System.out.println(temp);
		return Integer.parseInt(temp);
	}

	int getVerticeSize() throws NumberFormatException, IOException{
		String temp="";
		int i;
		while((i=br.read())!=32) {
			temp += ((char)i)+"";
		}
		return Integer.parseInt( temp );
	}

	int getNextValue() throws IOException{
		int i;
		String temp="";
		while((i = br.read()) != -1) {
			//System.out.println(i);
			if(i != 32 && i != 10) {	//32 is space, 10 is new line
				temp += ((char)i)+"";
				//System.out.println("if>"+temp);
			}else {
				//System.out.println("else>"+temp);
				if(temp.length() == 0) return -1;
				return Integer.parseInt(temp);
			}
		}
		return -1;
	}


	void close(){
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
