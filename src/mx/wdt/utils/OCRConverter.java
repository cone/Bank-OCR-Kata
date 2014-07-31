package mx.wdt.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.ArrayList;
import mx.wdt.account.AccountDigit;
import static mx.wdt.account.AccountDigit.DigitStatus;

public class OCRConverter{
	static HashMap<String, String> ocrToNumberMappings = new HashMap<>();
	static String aux;
	
	public final static String ZERO = " _ | ||_|";
	public final static String ONE = "     |  |";
	public final static String TWO = " _  _||_ ";
	public final static String THREE = " _  _| _|";
	public final static String FOUR = "   |_|  |";
	public final static String FIVE = " _ |_  _|";
	public final static String SIX = " _ |_ |_|";
	public final static String SEVEN = " _   |  |";
	public final static String EIGHT = " _ |_||_|";
	public final static String NINE = " _ |_| _|";
	public final static String MISSING_CHAR = "?";
	
	static{
		ocrToNumberMappings.put(ZERO, "0");
		ocrToNumberMappings.put(ONE, "1");
		ocrToNumberMappings.put(TWO, "2");
		ocrToNumberMappings.put(THREE, "3");
		ocrToNumberMappings.put(FOUR, "4");
		ocrToNumberMappings.put(FIVE, "5");
		ocrToNumberMappings.put(SIX, "6");
		ocrToNumberMappings.put(SEVEN, "7");
		ocrToNumberMappings.put(EIGHT, "8");
		ocrToNumberMappings.put(NINE, "9");
	}
	
	public static AccountDigit toNumber(String ocrString){
		aux = ocrToNumberMappings.get(ocrString);
		AccountDigit digit;
		if(aux != null){
			digit = new AccountDigit(ocrString, aux, DigitStatus.OK);
		}
		else{
			digit = new AccountDigit(ocrString, MISSING_CHAR, DigitStatus.MISSING);
		}
		return digit;
	}
	
	public static String[] getAlternatives(String ocr){
		Iterator it = ocrToNumberMappings.entrySet().iterator();
		String compateTo = "";
		String[] opts;
		ArrayList<String> aux = new ArrayList<>();
		int mismatches;
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry)it.next();
			compateTo = (String)pairs.getKey();
			mismatches = 0;
			for(int i=0; i<ocr.length(); i++){
				if(mismatches > 1){
					break;
				}
				if(!ocr.substring(i,i+1).equals(compateTo.substring(i,i+1))){
					mismatches++;
				}
			}
			if(mismatches == 1){
				aux.add((String)pairs.getValue());
			}
		}
		opts = new String[aux.size()];
		opts = aux.toArray(opts);
		return opts;
	}
}

