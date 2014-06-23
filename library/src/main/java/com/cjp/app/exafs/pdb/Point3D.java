package com.cjp.app.exafs.pdb;

public class Point3D {
	
	public double x;
	public double y;
	public double z;
	
	public Point3D() {};
	
	public Point3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Point3D clonePoint3D() {
		return new Point3D(x,y,z);
	}
}
