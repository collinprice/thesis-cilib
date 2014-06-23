package net.sourceforge.cilib.functions.continuous.unconstrained;

import java.util.ArrayList;
import java.util.List;

import com.cjp.app.exafs.EXAFSEvaluator;
import com.google.common.base.Preconditions;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

public class IFEFFIT extends ContinuousFunction {

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
}
