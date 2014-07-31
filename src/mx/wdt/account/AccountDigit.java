package mx.wdt.account;

public class AccountDigit{
	private String number;
	private String ocrDigit;
	private String[] alternatives;
	public enum DigitStatus{
		OK,
		MISSING
	}
	private DigitStatus status;
	
	public AccountDigit(String ocrDigit, String number){
		init(ocrDigit, number, DigitStatus.OK);
	}
	
	public AccountDigit(String ocrDigit, String number, DigitStatus status){
		init(ocrDigit, number, status);
	}
	
	private void init(String ocrDigit, String number, DigitStatus status){
		this.ocrDigit = ocrDigit;
		this.number = number;
		this.status = status;
	}
	
	public void setAlternatives(String[] alternatives){
		this.alternatives = alternatives;
	}
	
	public String[] getAlternatives(){
		return alternatives;
	}
	
	public String getOCRDigit(){
		return ocrDigit;
	}
	
	public String getNumber(){
		return number;
	}
	
	public DigitStatus status(){
		return status;
	}
	
	@Override
	public String toString(){
		return number;
	}
}