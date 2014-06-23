package com.cjp.app.exafs.pdb;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import com.cjp.io.MapParser;

public class ForceFieldHelper {
	
	private final MapParser _config;
	private XYZConverter _xyzConverter;
	private EnergyCalculator _energyCalculator;
	private double _lastEnergy;

	public ForceFieldHelper(MapParser config) {
		
		_config = config;
		_xyzConverter = new XYZConverter(config.getString("xyz-pdb"), 
										config.getString("amber"), 
										config.getString("pdb"));
		_energyCalculator = new EnergyCalculator(config.getString("energy-dir"),
												config.getString("energy-dir") + "/" + config.getString("pdb"), 
												config.getString("amber"), 
												config.getString("namd2"), 
												config.getString("vmd"));
	}

	public void evaluate(List<Atom> atoms) {
		
		if (this.createXYZFile(atoms, _config.getString("energy-dir") + "/" + _config.getString("temp-xyz"))) {
			if (_xyzConverter.convert(_config.getString("energy-dir") + "/" + _config.getString("temp-xyz"), 
										_config.getString("energy-dir") + "/" + _config.getString("temp-pdb"))) {
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
