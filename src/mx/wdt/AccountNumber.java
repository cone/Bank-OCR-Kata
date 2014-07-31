package mx.wdt;

import java.util.ArrayList;
import mx.wdt.utils.OCRConverter;
import mx.wdt.account.AccountDigit;

public class AccountNumber {

	private static final int DIGIT_WIDTH = 3;
	private static final int ACCOUNT_NUMBER_CHAR_WIDTH = 27;
	private static final int ACCOUNT_NUMBER_DIGIT_WIDTH = 9;
	private static String numericAC;
	private ArrayList<String> lines;
	private boolean valid;
	private AccountDigit[] ocrDigits;
	private ArrayList<String> alternativesInfo;
	
	public enum StatusType{
		OK,
		ERR,
		ILL,
		AMB
	}
	
	private StatusType status;

	public AccountNumber(String ocrAN) {
		lines = getLines(ocrAN);
		init();
	}
	
	public AccountNumber(String line1, String line2, String line3) {
		lines = new ArrayList<>();
		lines.add(line1);
		lines.add(line2);
		lines.add(line3);
		init();
	}
	
	public AccountNumber(ArrayList<String> lines) {
		this.lines = lines;
		init();
	}
	
	private void init(){
		alternativesInfo = new ArrayList<>();
		ocrDigits = new AccountDigit[ACCOUNT_NUMBER_DIGIT_WIDTH];
		getNumericalAN();
		valid = checkSum(numericAC);
		checkStatus();
		if(status != StatusType.OK){
			findAlternatives();
		}
	}
	
	public String getAccountNumber(){
		return numericAC;
	}
	
	public String getAccountNumberWithFullInfo(){
		String info = " ";
		for(String alternative : alternativesInfo){
			info += "["+alternative+"]";
		}
		if(!" ".equals(info)){
			return getAccountNumberWithStatus() + info;
		}
		else{
			return getAccountNumberWithStatus();
		}
	}
	
	public String getAccountNumberWithStatus(){
		if(status != StatusType.OK){
			return numericAC+" "+status.toString();
		}
		return numericAC;
	}
	
	public boolean isValid(){
		return valid;
	}
	
	private void getNumericalAN(){
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < ACCOUNT_NUMBER_DIGIT_WIDTH; i++){
			ocrDigits[i] = OCRConverter.toNumber(getDigit(i));
			sb.append(ocrDigits[i].toString());
		}
		numericAC = sb.toString();
	}
	
	private ArrayList<String> getLines(String ocrAN){
		int start = 0;
		ArrayList<String> lines = new ArrayList<>();
		for(int i = 0; i < 3; i++){
			lines.add(ocrAN.substring(start, ACCOUNT_NUMBER_CHAR_WIDTH + start));
			start += ACCOUNT_NUMBER_CHAR_WIDTH;
		}
		return lines;
	}
	
	private String getDigit(int pos){
		int start = pos * DIGIT_WIDTH;
		int end = start + DIGIT_WIDTH;
		String up = ((String)lines.get(0)).substring(start, end);
		String center = ((String)lines.get(1)).substring(start, end);
		String down = ((String)lines.get(2)).substring(start, end);
		return up + center + down;
	}
	
	public boolean checkSum(String accountNumber) {

		int checkSumCalculation = 0;
		int currentDigit;

		for (int digit = 0; digit < ACCOUNT_NUMBER_DIGIT_WIDTH; digit++) {

			String thisCharacter = accountNumber.substring(digit, digit+1);
			if(!OCRConverter.MISSING_CHAR.equals(thisCharacter)){
				currentDigit = Integer.parseInt(thisCharacter);
			}
			else{
				return false;
			}
			checkSumCalculation = checkSumCalculation + ((ACCOUNT_NUMBER_DIGIT_WIDTH-digit) * currentDigit);
		}

		return ((checkSumCalculation % 11) == 0);
	}
	
	private void checkStatus(){
		if(numericAC.indexOf(OCRConverter.MISSING_CHAR)!=-1){
			status = StatusType.ILL;
		}
		else if(!valid){
			status = StatusType.ERR;
		}
		else{
			status = StatusType.OK;
		}
	}
	
	private void findAlternatives(){
		String[] alternatives;
		StringBuilder newAccountNoBuilder;
		String newAccountNo;
		for (int i = 0; i < ACCOUNT_NUMBER_DIGIT_WIDTH; i++) {
			alternatives = OCRConverter.getAlternatives(ocrDigits[i].getOCRDigit());
			for(int j=0; j<alternatives.length; j++){
				newAccountNoBuilder = new StringBuilder(numericAC);
				newAccountNoBuilder.setCharAt(i, alternatives[j].charAt(0));
				newAccountNo = newAccountNoBuilder.toString();
				if(checkSum(newAccountNo)){
					alternativesInfo.add(newAccountNo);
				}
			}
		}
		if(alternativesInfo.size()>1){
			status = StatusType.AMB;
		}
		else if(alternativesInfo.size()==1){
			status = StatusType.OK;
		}
	}
	
}