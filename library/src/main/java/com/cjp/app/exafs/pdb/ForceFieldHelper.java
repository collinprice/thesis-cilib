package com.cjp.app.exafs.pdb;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import com.cjp.io.MapParser;

public class ForceFieldHelper {
	
	private static final String IFEFFIT_DIRECTORY = "ifeffit-dir";
	private static final String CONTAINER_DIRECTORY = "energy-container-";
	private static final String TEMP_XYZ = "temp.xyz";
	private static final String TEMP_PDB = "temp.pdb";
	
	private final MapParser _config;
	private XYZConverter _xyzConverter;
	private EnergyCalculator _energyCalculator;
	private double _lastEnergy;
	private File temp_CONTAINER_DIRECTORY;

	public ForceFieldHelper(MapParser config) {
		
		_config = config;
		_xyzConverter = new XYZConverter(config.getString("xyz-pdb"), 
										config.getString(IFEFFIT_DIRECTORY) + "/" + config.getString("amber"), 
										config.getString(IFEFFIT_DIRECTORY) + "/" + config.getString("pdb-file"));
		
		// Create temporary folder for energy calculations.
		File file = new File(config.getString(IFEFFIT_DIRECTORY));
		
		if (!file.exists()) {
			throw new RuntimeException("IFEFFIT Directory is incorrect or missing.");
		}
		
		// Create container directory.
		String temp_name;
		do {
			temp_name = CONTAINER_DIRECTORY + ((int)(Math.random()*10000));
			temp_CONTAINER_DIRECTORY = new File(file, temp_name);
		} while (temp_CONTAINER_DIRECTORY.exists());
		
		temp_CONTAINER_DIRECTORY.mkdir();
		
		_energyCalculator = new EnergyCalculator(temp_CONTAINER_DIRECTORY.getAbsolutePath(),
												new File(temp_CONTAINER_DIRECTORY, TEMP_PDB).getAbsolutePath(),
												new File(config.getString(IFEFFIT_DIRECTORY),config.getString("amber")).getAbsolutePath(),
												config.getString("namd2"), 
												config.getString("vmd"));
	}

	public void evaluate(List<Atom> atoms) {
//		System.out.println("eval");
		
		if (this.createXYZFile(atoms, new File(temp_CONTAINER_DIRECTORY, TEMP_XYZ).getAbsolutePath())) {
//			System.out.println("inside");
			if (_xyzConverter.convert(new File(temp_CONTAINER_DIRECTORY, TEMP_XYZ).getAbsolutePath(), 
										new File(temp_CONTAINER_DIRECTORY, TEMP_PDB).getAbsolutePath())) {
				_lastEnergy = _energyCalculator.calculateEnergy();
			}
		}
	}
	
	public double getEnergy() {
		return _lastEnergy;
	}
	
	private boolean createXYZFile(List<Atom> atoms, String outputFile) {
		
		PrintWriter writer;
		
		try {
			writer = new PrintWriter(outputFile);
			
			// Write number of atoms.
			writer.println(atoms.size());
			
			// Write comment line;
			writer.println();
			
			for (Atom atom : atoms) {
				
				writer.printf("%s %.13f %.13f %.13f%n", atom.getAtomicSymbol(), 
														atom.getPoint().x, 
														atom.getPoint().y, 
														atom.getPoint().z);
			}
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
}
