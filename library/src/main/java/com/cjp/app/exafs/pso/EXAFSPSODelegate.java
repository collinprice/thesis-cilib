//package com.cjp.app.exafs.pso;
//
//import com.rbi.ai.AIConfiguration;
//import com.rbi.ai.AIEvolutionDelegate;
//import com.rbi.ai.mo.ObjectiveVector;
//import com.rbi.ai.testharness.AIHarnessConfiguration;
//import com.rbi.ai.testharness.AITest;
//import com.rbi.ai.testharness.AITestHarnessDelegate;
//import com.rbi.ai.testharness.AITestSet;
//import com.rbi.io.GeneralFileParser;
//
//public class EXAFSPSODelegate 
//implements 
//	AIEvolutionDelegate<EXAFSPSOEngine,EXAFSPSOParticle,ObjectiveVector<Double>>,
//	AITestHarnessDelegate<EXAFSPSOEngine, ObjectiveVector<Double>>{
//
//	@Override
//	public void evolutionDidBegin(EXAFSPSOEngine engine, AIConfiguration config) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void evolutionDidEnd(EXAFSPSOEngine engine, AIConfiguration config,
//			boolean earlyTermination) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void newGenerationEvolved(EXAFSPSOEngine engine,
//			AIConfiguration config) {
//		// TODO Auto-generated method stub
//		
//		EXAFSPSOParticle pBest = engine.getBestIndivual();
//		
//		config.getString(currentDir);
//	}
//	
//	//--------------------------------------------------------+
//
//	@Override
//	public void aiRunDidFinish(AIHarnessConfiguration harnessConfig,
//			EXAFSPSOEngine engine, AIConfiguration engineConfig, int testIndex,
//			int runIndex) {
//		
//		
//	}
//
//	@Override
//	public void aiTestdidFinish(AIHarnessConfiguration harnessConfig,
//			AITest<EXAFSPSOEngine, ObjectiveVector<Double>> test, int testIndex) {
//		
//		
//	}
//
//	@Override
//	public void allTestsFinished(AIHarnessConfiguration harnessConfig,
//			AITestSet<EXAFSPSOEngine, ObjectiveVector<Double>> allTests) {
//		
//		
//	}
//
//	@Override
//	public void aiRunDidStart(AIHarnessConfiguration harnessConfig,
//			EXAFSPSOEngine engine, AIConfiguration engineConfig, int testIndex,
//			int runIndex) {
//		
//		//add path
//		
//		String dataName = null;
//		try {
//			dataName = GeneralFileParser
//					.fileNameWithoutExtensionFromPath(engineConfig
//							.getString(AIConfiguration.DATA_PATH_KEY));
//		} catch (Exception e) {
//			dataName = "NODATA";
//		}
//		
//		
//
//		String outputPath = "results/"
//				+ harnessConfig.getString(AIHarnessConfiguration.TEST_SET_NAME_KEY)
//				+ "/" + harnessConfig.getString(AIHarnessConfiguration.SEED_KEY) + "/"
//				+ dataName + // add option in harness configuration
//				"/test" + testIndex + "/config.txt";
//	}
//	
//	//--------------------------------------------------------+
//}
