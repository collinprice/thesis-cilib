package com.cjp.app.exafs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.cjp.app.exafs.ifeffit.IFEFFITHelper;
import com.cjp.app.exafs.pdb.ForceFieldHelper;
import com.cjp.app.exafs.pdb.PDBHelper;
import com.cjp.app.exafs.pdb.Point3D;
import com.cjp.app.exafs.pdb.XYZReader;
import com.cjp.io.MapParser;

public class EXAFSEvaluator {
	
	private IFEFFITHelper ifeffitHelper;
	private ForceFieldHelper forceFieldHelper;
	private PDBHelper pdbHelper;
	private List<List<Point3D>> xyzFiles;
	private int xyzIndexCounter;
	private int problemDimension;
	
	private static EXAFSEvaluator exafsEvaluator;
	
	private EXAFSEvaluator(String configFile) {
		
		MapParser config = new MapParser(configFile);
//		System.out.println("pdb");
		this.pdbHelper = new PDBHelper(config.getString("ifeffit-dir")+"/"+config.getString("amber"), 
										config.getString("ifeffit-dir")+"/"+config.getString("pdb-file"), 
										config.getList_String("atom-filter"));
		
//		System.out.println("ifeffit");
		this.ifeffitHelper = new IFEFFITHelper(config, pdbHelper.getEXAFSAtoms());
//		System.out.println("ff");
		this.forceFieldHelper = new ForceFieldHelper(config);
//		
//		System.out.println("before xyz");
		this.xyzFiles = XYZReader.readDirectory(config.getString("xyz-dir"));
//		System.out.println("after xyz");
		this.xyzIndexCounter = 0;
		this.problemDimension = this.xyzFiles.get(0).size() * 3;
	}
	
	/**
	 * Retrieve the coordinates from the xyz file in a linear list. Files are converted from 3-dimensional points to a list of points.
	 * @return
	 */
	public List<Double> getNextAtomList() {
		
		// Randomize list of files if all of them have been read.
		if (xyzIndexCounter == xyzFiles.size()) {
			Collections.shuffle(xyzFiles);
			xyzIndexCounter = 0;
		}
		
		List<Point3D> xyzFile = xyzFiles.get(xyzIndexCounter);
		xyzIndexCounter++;
		
		List<Double> pointList = new ArrayList<Double>();
		for (Point3D point : xyzFile) {
			
			pointList.add(point.x);
			pointList.add(point.y);
			pointList.add(point.z);
		}
		
		return pointList;
	}
	
	public double evaluateIFEFFIT(List<Double> input) {
		
		pdbHelper.setEXAFSAtomsFromList(input);
		
//		System.out.println(pdbHelper.getEXAFSAtoms().size());
		ifeffitHelper.evaluate(pdbHelper.getEXAFSAtoms());
		
		return ifeffitHelper.getRMSD();
	}
	
	public double evaluateForceFields(List<Double> input) {
		
		pdbHelper.setAllAtomsFromList(input);
		
//		System.out.println(pdbHelper.getAtoms().size());
//		System.out.println("um..");
		forceFieldHelper.evaluate(pdbHelper.getAtoms());
		
		return forceFieldHelper.getEnergy();
	}
	
	public int getDimension() {
		return this.problemDimension;
	}
	
	public List<Double> getEXAFSAtoms() {
		return pdbHelper.getEXAFSAtomsAsList();
	}
	
	public double evaluateAtoms(List<Double> input) {
		pdbHelper.setEXAFSAtomsFromList(input);
		ifeffitHelper.evaluate(pdbHelper.getEXAFSAtoms());
		
		return ifeffitHelper.getRMSD();
	}
	
	public static EXAFSEvaluator sharedInstance() {
		
		if (exafsEvaluator == null) {
			exafsEvaluator = new EXAFSEvaluator("config/ifeffit.cfg");
		}
		
		return exafsEvaluator;
	}
}
