package com.cjp.app.exafs;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.cjp.app.exafs.pdb.XYZConverter;
import com.cjp.app.exafs.pdb.XYZReader;

//import java.io.File;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.List;

//import com.cjp.app.exafs.dcd.DCDReader;
//import com.cjp.app.exafs.ifeffit.IFEFFITHelper;
//import com.cjp.app.exafs.pdb.PDBHelper;
//import com.cjp.app.exafs.pso.EXAFSPSOEngine;
//import com.cjp.app.exafs.pso.EXAFSPSOParticle;
//import com.rbi.ai.AIConfiguration;
//import com.rbi.ai.mo.ObjectiveVector;
//import com.rbi.ai.problems.clustering.ClusterParticle;
//import com.rbi.ai.problems.trading.TradingGroup;
//import com.rbi.ai.problems.trading.TradingNeuralNetwork;
//import com.rbi.ai.pso.PSOConfiguration;
//import com.rbi.ai.pso.PSOEngine;
//import com.rbi.ai.testharness.AIHarnessConfiguration;
//import com.rbi.ai.testharness.AITest;
//import com.rbi.ai.testharness.AITestHarness;
//import com.rbi.ai.testharness.AITestHarnessDelegate;
//import com.rbi.ai.testharness.AITestSet;
//import com.rbi.io.GeneralFileParser;
//import com.rbi.io.MapParser;
//import com.rbi.structures.MW;

public class Main {

	public static void main(String[] args) {
		
//		Path currentRelativePath = Paths.get("");
//		String s = currentRelativePath.toAbsolutePath().toString();
//		System.out.println("Current relative path is: " + s);
//		
//		XYZReader.readDirectory("results");
		
		XYZConverter xyzConverter = new XYZConverter("xyz-to-pdb", "prmtop-sphere", "relaxed-H.pdb");
		xyzConverter.convert("results/1.xyz", "out.pdb");
		
//		DCDReader dcdReader = new DCDReader(new File(config.getString("ifeffit-dir"), config.getString("dcd")));
		
		/*
		MapParser config = new MapParser("config/ifeffit.cfg");
		PDBHelper pdbHelper = new PDBHelper(config.getString("ifeffit-dir")+"/"+config.getString("amber"), config.getString("ifeffit-dir")+"/"+config.getString("pdb-file"));
		
		IFEFFITHelper ifeffitHelper = new IFEFFITHelper(config, pdbHelper.getEXAFSAtoms());
		ifeffitHelper.evaluate(pdbHelper.getEXAFSAtoms());
		
		System.out.println("RMSD = " + ifeffitHelper.getRMSD());
		*/
		
		
//		PSOConfiguration config = new PSOConfiguration("config/ifeffit.cfg");
//		EXAFSPSOEngine engine = new EXAFSPSOEngine(config);
//		engine.initializePopulation();
//		engine.evolve();
		
//		AIHarnessConfiguration config = new AIHarnessConfiguration("config/ifeffit.cfg");
//		AITestHarness harness = new AITestHarness(config);
//		
//		harness.delegate = new AITestHarnessDelegate<
//				EXAFSPSOEngine, 
//				ObjectiveVector<Double>>() {
//
//					@Override
//					public void aiRunDidFinish(
//							AIHarnessConfiguration harnessConfig,
//							EXAFSPSOEngine engine,
//							AIConfiguration engineConfig, int testIndex,
//							int runIndex) {
//						// TODO Auto-generated method stub
//						
//					}
//
//					@Override
//					public void aiTestdidFinish(
//							AIHarnessConfiguration harnessConfig,
//							AITest<EXAFSPSOEngine, ObjectiveVector<Double>> test,
//							int testIndex) {
//						// TODO Auto-generated method stub
//						
//					}
//
//					@Override
//					public void allTestsFinished(
//							AIHarnessConfiguration harnessConfig,
//							AITestSet<EXAFSPSOEngine, ObjectiveVector<Double>> allTests) {
//						// TODO Auto-generated method stub
//						
//					}
//
//		};
//		
//		harness.startTesting();
	}

}

