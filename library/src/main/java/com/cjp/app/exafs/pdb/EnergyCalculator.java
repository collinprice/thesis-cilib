package com.cjp.app.exafs.pdb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.StringTokenizer;

public class EnergyCalculator {
	
	public static final String RUN_SCRIPT = "vmd.sh";
	public static final String VMD_SCRIPT = "energy_script.vmd";
	public static final String VMD_OUTPUT = "pdb_energy";
	
	private String directory;

	public EnergyCalculator(String tempDirectory, String pdbFile, String amberFile, String namd2Path, String vmdPath) {
		
		this.directory = tempDirectory;
		
		PrintWriter writer;
		try {
			writer = new PrintWriter(tempDirectory + "/" + RUN_SCRIPT);
			
			writer.println("cd " + tempDirectory);
			writer.println(vmdPath + " -dispdev text -e " + VMD_SCRIPT + " > /dev/null");
			writer.close();
			
			writer = new PrintWriter(tempDirectory + "/" + VMD_SCRIPT);
			writer.println("package require namdenergy");
			writer.println("mol new " + amberFile + " type parm7");
			writer.println("mol addfile " + pdbFile);
			writer.println("set sel [atomselect top \"occupancy 1.0\"]");
			writer.println("namdenergy -all -sel $sel -exe " + namd2Path + " -ofile " + VMD_OUTPUT);
			writer.println("quit");
			writer.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public double calculateEnergy() {
		
		String command = "bash " + this.directory + "/" + RUN_SCRIPT;

		try {
			Process pr = Runtime.getRuntime().exec(command);
			
			InputStreamReader reader = new InputStreamReader(pr.getInputStream());
			while(reader.read() != -1){}
			
			pr.waitFor();
			
			BufferedReader br = new BufferedReader(new FileReader(this.directory + "/" + VMD_OUTPUT));
			br.readLine();
			
			StringTokenizer tokenizer = new StringTokenizer(br.readLine());
			String energy = null;
			for (int i = 0; i < 11; i++) {
				energy = tokenizer.nextToken();
			}
			
			File file = new File(this.directory + "/" + VMD_OUTPUT);
			file.delete();
			
			br.close();
		
			return Double.parseDouble(energy);
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
}
