package com.cjp.app.exafs.pdb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
			writer = new PrintWriter(tempDirectory + "/" + RUN_SCRIPT, "UTF-8");
			
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
			
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public double calculateEnergy() {
		
		try {
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
		}
	}
	
}
