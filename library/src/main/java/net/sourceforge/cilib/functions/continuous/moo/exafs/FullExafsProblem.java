package net.sourceforge.cilib.functions.continuous.moo.exafs;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.problem.FunctionOptimisationProblem;
import net.sourceforge.cilib.type.types.container.Vector;

import com.cjp.app.exafs.EXAFSEvaluator;
import com.google.common.base.Preconditions;

public class FullExafsProblem extends EXAFSProblem {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public FullExafsProblem() {}

	@Override
    protected void initialize() {
		System.out.println(this.s);
        clear();
        ContinuousFunction ifeffitFunction = new ContinuousFunction() {

            @Override
            public Double f(Vector input) {
            	EXAFSEvaluator exafsEvaluator = EXAFSEvaluator.sharedInstance();
        		Preconditions.checkArgument(exafsEvaluator.getDimension() == input.size(), "EXAFS Dimension needs to be " + exafsEvaluator.getDimension());
        		
        		List<Double> inputCoords = new ArrayList<Double>();
        		for (int i = 0; i < input.size(); i++) {
        			inputCoords.add(input.doubleValueOf(i));
        		}

        		double fitness = exafsEvaluator.evaluateIFEFFIT(inputCoords);
        		System.out.println("EXAFS: " + fitness);
        		
                return fitness;
            }
        };
        FunctionOptimisationProblem ifeffitProblem = new FunctionOptimisationProblem();
        ifeffitProblem.setFunction(ifeffitFunction);
        ifeffitProblem.setDomain("R(-1000:1000)^" + this.s);
        add(ifeffitProblem);
        
        ContinuousFunction forceFieldFunction = new ContinuousFunction() {

            @Override
            public Double f(Vector input) {
            	EXAFSEvaluator exafsEvaluator = EXAFSEvaluator.sharedInstance();
        		Preconditions.checkArgument(exafsEvaluator.getDimension() == input.size(), "EXAFS Dimension needs to be " + exafsEvaluator.getDimension());
        		
        		List<Double> inputCoords = new ArrayList<Double>();
        		for (int i = 0; i < input.size(); i++) {
        			inputCoords.add(input.doubleValueOf(i));
        		}
        		
        		double fitness = exafsEvaluator.evaluateForceFields(inputCoords);
        		System.out.println("Force Fields: " + fitness);
        		
                return fitness;
            }
        };
        FunctionOptimisationProblem forceFieldProblem = new FunctionOptimisationProblem();
        forceFieldProblem.setFunction(forceFieldFunction);
        forceFieldProblem.setDomain("R(-1000:1000)^" + this.s);
        add(forceFieldProblem);
        
    }

    public FullExafsProblem(FullExafsProblem copy) {
        super(copy);
    }

    @Override
    public FullExafsProblem getClone() {
        return new FullExafsProblem(this);
    }

}
