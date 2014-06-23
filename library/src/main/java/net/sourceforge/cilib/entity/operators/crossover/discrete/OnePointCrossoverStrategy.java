/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.operators.crossover.discrete;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.operators.crossover.DiscreteCrossoverStrategy;
import net.sourceforge.cilib.math.random.generator.Rand;
import net.sourceforge.cilib.type.types.container.Vector;

public class OnePointCrossoverStrategy implements DiscreteCrossoverStrategy {

    private static final long serialVersionUID = 7313531386910938748L;

    private List<Integer> crossoverPoints;

    public OnePointCrossoverStrategy() {
        this.crossoverPoints = new ArrayList<>();
    }

    public OnePointCrossoverStrategy(OnePointCrossoverStrategy copy) {
        this.crossoverPoints = new ArrayList<>(copy.crossoverPoints);
    }

    @Override
    public OnePointCrossoverStrategy getClone() {
        return new OnePointCrossoverStrategy(this);
    }

    @Override
    public <E extends Entity> List<E> crossover(List<E> parentCollection) {
        Preconditions.checkArgument(parentCollection.size() == 2, "OnePointCrossoverStrategy requires 2 parents.");

        // Select the pivot point where crossover will occur
        int maxLength = Math.min(parentCollection.get(0).getDimension(), parentCollection.get(1).getDimension());
        crossoverPoints = Arrays.asList(Rand.nextInt(maxLength));

        return crossover(parentCollection, crossoverPoints);
    }

    @Override
    public <E extends Entity> List<E> crossover(List<E> parentCollection, List<Integer> crossoverPoints) {
        Preconditions.checkArgument(parentCollection.size() == 2, "OnePointCrossoverStrategy requires 2 parents.");

        E offspring1 = (E) parentCollection.get(0).getClone();
        E offspring2 = (E) parentCollection.get(1).getClone();

        int crossoverPoint = crossoverPoints.get(0);

        Vector offspringVector1 = (Vector) offspring1.getPosition();
        Vector offspringVector2 = (Vector) offspring2.getPosition();

        Vector.Builder offspringVector1Builder = Vector.newBuilder();
        Vector.Builder offspringVector2Builder = Vector.newBuilder();

        offspringVector1Builder.copyOf(offspringVector1.copyOfRange(0, crossoverPoint));
        offspringVector2Builder.copyOf(offspringVector2.copyOfRange(0, crossoverPoint));
        offspringVector1Builder.copyOf(offspringVector2.copyOfRange(crossoverPoint, offspringVector2.size()));
        offspringVector2Builder.copyOf(offspringVector1.copyOfRange(crossoverPoint, offspringVector1.size()));

        offspring1.setPosition(offspringVector1Builder.build());
        offspring2.setPosition(offspringVector2Builder.build());

        return Arrays.asList(offspring1, offspring2);
    }

    @Override
    public int getNumberOfParents() {
        return 2;
    }

    @Override
    public void setCrossoverPointProbability(ControlParameter crossoverPointProbability) {
        throw new UnsupportedOperationException("Not applicable");
    }

    @Override
    public ControlParameter getCrossoverPointProbability() {
        throw new UnsupportedOperationException("Not applicable");
    }

    @Override
    public List<Integer> getCrossoverPoints() {
        return crossoverPoints;
    }
}
