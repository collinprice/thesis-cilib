package com.cjp.app.exafs.pdb;

public class Atom {
	
	private Point3D point;
	private String atomicSymbol;
	private boolean isEXAFS;
	
	public Atom(Point3D point, String atomicSymbol, boolean isEXAFS) {
		
		this.point = point;
		this.atomicSymbol = atomicSymbol;
		this.isEXAFS = isEXAFS;
	}
	
	public Atom(Atom copy) {
		this.atomicSymbol = copy.atomicSymbol;
		this.point = copy.point.clonePoint3D();
		this.isEXAFS = copy.isEXAFS;
	}
	
	public boolean isEXAFS() {
		return this.isEXAFS;
	}
	
	public Point3D getPoint() {
		return this.point;
	}
	
	public void setPoint(Point3D point) {
		this.point = point;
	}
	
	public String getAtomicSymbol() {
		return this.atomicSymbol;
	}
	
	public String toString() {
		return this.atomicSymbol + ": " + point.x + " " + point.y + " " + point.z + " " + (this.isEXAFS ? "1" : "");
	}
	
	public Atom cloneAtom() {
		return new Atom(this);
	}
	
	public int getAtomicNumber() {
		
		String[] elements = new String[] {
			"H", "He","Li","Be","B", "C", "N", "O", "F", "Ne",
			"Na","Mg","Al","Si","P", "S", "Cl","Ar","K", "Ca",
			"Sc","Ti","V", "Cr","Mn","Fe","Co","Ni","Cu","Zn",
			"Ga","Ge","As","Se","Br","Kr","Rb","Sr","Y", "Zr",
			"Nb","Mo","Tc","Ru","Rh","Pd","Ag","Cd","In","Sn",
			"Sb","Te","I", "Xe","Cs","Ba","La","Ce","Pr","Nd",
			"Pm","Sm","Eu","Gd","Tb","Dy","Ho","Er","Tm","Yb",
			"Lu","Hf","Ta","W", "Re","Os","Ir","Pt","Au","Hg",
			"Tl","Pb","Bi","Po","At","Rn","Fr","Ra","Ac","Th",
			"Pa","U", "Np","Pu","Am","Cm","Bk","Cf","Es","Fm",
			"Md","No","Lr"
		};
		
		for (int i = 0; i < elements.length; i++) {
			if (elements[i].equalsIgnoreCase(atomicSymbol)) return (i+1);
		}
		
		return -1;
	}
}
