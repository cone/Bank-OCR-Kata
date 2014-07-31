package mx.wdt;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.util.ArrayList;

public class OCRFileProcessor{
	private int lineCount = 0;
	private ArrayList<String> ans = new ArrayList<>();
	private BufferedWriter  out;
	
	public OCRFileProcessor(String inputFile, String outputFile){
		try{
			out = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(outputFile), "UTF-8"));
				
			Files.lines(new File(inputFile).toPath()).forEach((line)->{
				getAccountNumber(line);
			});
			
			out.close();
		}
		catch(IOException ex){
			System.err.println(ex);
		}
	}
	
	private void getAccountNumber(String line){
		if(lineCount < 3){
			ans.add(line);
			lineCount++;
		}
		else{
			lineCount = 0;
			AccountNumber ac = new AccountNumber(ans);
			writeToOutputFile(ac.getAccountNumberWithFullInfo());
			ans.clear();
		}
	}
	
	private void writeToOutputFile(String line){
		try {
			out.write(line);
			out.newLine();
		}
		catch(IOException ex) {
			System.err.println(ex);
		}
	}
}