//package com.cjp.app.exafs.pso;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import com.rbi.ai.AIData;
//import com.rbi.ai.mo.ObjectiveVector;
//import com.rbi.ai.pso.PSOConfiguration;
//import com.rbi.ai.pso.PSOEngine;
//
//public class EXAFSPSOEngine extends PSOEngine<EXAFSPSOParticle, ObjectiveVector<Double>, Double> {
//
//	public EXAFSPSOEngine(PSOConfiguration config) {
//		super(config);
//	}
//	
////	public EXAFSPSOEngine(PSOConfiguration config, AIData data) {
////		
////		super(config,data);
////	}
//	
//	protected List<EXAFSPSOParticle> generate(PSOConfiguration config) {
//
//		int populationSize = config.getInt(PSOConfiguration.POP_SIZE_KEY);
//
//		List<EXAFSPSOParticle> ret = new ArrayList<EXAFSPSOParticle>(populationSize);
//		int count = populationSize;
//		while (count-- > 0) {
//			ret.add(new EXAFSPSOParticle(config));
//		}
//
//		evaluatePopulation(ret, data);
//		return ret;
//	}
//}
