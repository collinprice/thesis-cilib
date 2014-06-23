package com.cjp.app.exafs.pdb;

import java.io.IOException;
import java.io.InputStreamReader;

public class XYZConverter {

	private String executableName;
	private String amberFile;
	private String pdbFile;
	
	public XYZConverter(String executableName, String amberFile, String pdbFile) {
		
		this.executableName = executableName;
		this.amberFile = amberFile;
		this.pdbFile = pdbFile;
	}
	
	public boolean convert(String input, String output) {
		
		String command = String.format("%s -a %s -p %s -x %s -o %s", this.executableName,
																		this.amberFile,
																		this.pdbFile,
																		input,
																		output);
		
		try {
			Process pr = Runtime.getRuntime().exec(command);
			
			InputStreamReader reader = new InputStreamReader(pr.getInputStream());
			while(reader.read() != -1){}
			
			pr.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
}
