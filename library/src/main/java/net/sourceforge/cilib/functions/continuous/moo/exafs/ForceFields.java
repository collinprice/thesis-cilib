package net.sourceforge.cilib.functions.continuous.moo.exafs;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.problem.FunctionOptimisationProblem;
import net.sourceforge.cilib.type.types.container.Vector;

import com.cjp.app.exafs.EXAFSEvaluator;
import com.google.common.base.Preconditions;

public class ForceFields extends EXAFSProblem {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void initialize() {
		
        clear();
        ContinuousFunction function = new ContinuousFunction() {

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
        forceFieldProblem.setFunction(function);
        forceFieldProblem.setDomain("R(-1000:1000)^" + this.s);
        add(forceFieldProblem);
    }

    public ForceFields(ForceFields copy) {
        super(copy);
    }

    @Override
    public ForceFields getClone() {
        return new ForceFields(this);
    }

}
