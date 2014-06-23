//package com.cjp.app.exafs.pso;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//import com.cjp.app.exafs.ifeffit.IFEFFITHelper;
//import com.cjp.app.exafs.pdb.PDBHelper;
//import com.rbi.ai.mo.ObjectiveVector;
//import com.rbi.ai.pso.PSOConfiguration;
//import com.rbi.ai.pso.Particle;
//import com.rbi.ai.pso.simple.DoubleVectorParticle;
//import com.rbi.io.MapParser;
//
//public class EXAFSPSOParticle extends DoubleVectorParticle<ObjectiveVector<Double>> {
//
//	static PDBHelper pdbHelper;
//	static IFEFFITHelper ifeffitHelper;
//	static List<List<Double>> initializers;
//	
//	public EXAFSPSOParticle(){
//		
//	}
//	
//	public EXAFSPSOParticle(PSOConfiguration config){
//		
//		this.position = ifeffitRandomVector(config);
//		this.velocity = randomVector(this.position.size(),config);
//	}
//	
//	
//	public EXAFSPSOParticle(EXAFSPSOParticle copy) {
//		super(copy);
//		this.fitness = copy.getStoredFitness();
//	}
//
//	@Override
//	public void evaluateAndStoreFitness(Object dataSet, PSOConfiguration config) {
//		
//		pdbHelper.setEXAFSAtomsFromList(position);
//		ifeffitHelper.evaluate(pdbHelper.getEXAFSAtoms());
//		fitness = new ObjectiveVector<Double>(ifeffitHelper.getRMSD());
//		fitness.rank = ifeffitHelper.getRMSD();
//		
//		System.out.println(fitness);
//	}
//	
//	@Override
//	public Particle<ObjectiveVector<Double>, Double> cloneParticle() {
//		return new EXAFSPSOParticle(this);
//	}
//	
//	private List<Double> ifeffitRandomVector(PSOConfiguration config) {
//		
//		Random rand = config.getRandom();
//		List<Double> startingAtoms = pdbHelper.getEXAFSAtomsAsList();
//		List<Double> modifiedAtoms = new ArrayList<Double>();
//		
//		for (int i = 0; i < startingAtoms.size(); i++) {
//			modifiedAtoms.add(startingAtoms.get(i) + (rand.nextDouble()));
//		}
//		
//		return modifiedAtoms;
//	}
//	
//	static {
//		MapParser config = new MapParser("config/ifeffit.cfg");
//		
//		pdbHelper = new PDBHelper(config.getString("ifeffit-dir")+"/"+config.getString("amber"), config.getString("ifeffit-dir")+"/"+config.getString("pdb-file"));
//		ifeffitHelper = new IFEFFITHelper(config, pdbHelper.getEXAFSAtoms());
//	}
//
//	
//	
//}
