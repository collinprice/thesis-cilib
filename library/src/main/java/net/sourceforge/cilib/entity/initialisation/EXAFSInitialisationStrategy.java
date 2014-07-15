package net.sourceforge.cilib.entity.initialisation;

import java.util.List;

import com.cjp.app.exafs.EXAFSEvaluator;
import com.google.common.base.Preconditions;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Property;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFunction;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

public class EXAFSInitialisationStrategy<E extends Entity> implements
InitialisationStrategy<E> {
	
	private static final long serialVersionUID = 143545623423L;
	private ProbabilityDistributionFunction random;
	
	public EXAFSInitialisationStrategy() {
		this.random = new UniformDistribution();
    }
	
	public EXAFSInitialisationStrategy(EXAFSInitialisationStrategy<E> copy) {
		this.random = copy.random;
    }
	
	@Override
	public InitialisationStrategy<E> getClone() {
		return new EXAFSInitialisationStrategy<E>(this);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initialise(Property key, E entity) {
		
//		System.out.println("Initializing Population.");
		Type type = entity.get(key);
		Vector vector = (Vector) type;
        
//		System.out.println("Before");
        List<Double> atomPositionList = EXAFSEvaluator.sharedInstance().getNextAtomList();
//        System.out.println("After");
        
//        Preconditions.checkArgument(atomPositionList.size() == vector.size(), "EXAFS Dimension needs to be " + atomPositionList.size());
        
        for (int i = 0; i < vector.size(); i++) {
        	
//        	vector.setReal(i, atomPositionList.get(i) + random.getRandomNumber());
        	vector.setReal(i, atomPositionList.get(i)); // Don't need to randomize due to getNextAtomList() ^^
        }

	}

}
