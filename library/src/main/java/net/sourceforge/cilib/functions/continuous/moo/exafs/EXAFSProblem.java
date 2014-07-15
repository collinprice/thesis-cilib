package net.sourceforge.cilib.functions.continuous.moo.exafs;

import net.sourceforge.cilib.problem.MOOptimisationProblem;

public abstract class EXAFSProblem extends MOOptimisationProblem {
	
	protected int s;
	
	EXAFSProblem() {
		System.out.println("exafasProboem!!");
		this.s = 3807;
       initialize();
    }
    
	EXAFSProblem(EXAFSProblem copy) {
        super(copy);
        System.out.println("exafasproglem");
        this.s = copy.s;
        initialize();
    }
    
    protected abstract void initialize();
    
    public void setS(int s) {
    	System.out.println("set s");
        this.s = s;
        initialize();
    }
    
    public int getS() {
    	System.out.println("get s");
        return this.s;
    }
    
}
