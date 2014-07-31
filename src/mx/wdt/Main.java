package mx.wdt;

import mx.wdt.utils.OCRConverter;

public class Main{
	public static void main(String[] args){
		System.out.println("Reading from: "+args[0]);
		System.out.println("Reading from: "+args[1]);
		OCRFileProcessor fr = new OCRFileProcessor(args[0], args[1]);
		//OCRConverter.getAlternatives(" _ |_| _ ");
	}
}