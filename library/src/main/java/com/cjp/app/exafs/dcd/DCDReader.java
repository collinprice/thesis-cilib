package com.cjp.app.exafs.dcd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

public class DCDReader {
	
	private int charmm;
	private int charmm_extrablock;
	private int charmm_4dims;
	private int NSet;
	private int ISTART;
	private int NSAVC;
	private int NAMNF;
	private int N;
	private int FREEINDEXES;
	private int endoffile;
	private float DELTA;
	
	public DCDReader(File file) {
		
		if (!file.exists()) {
			System.err.println("DCD file not found.");
			System.exit(0);
		}
		
		endoffile = -1;
		byte[] s = new byte[4];
		
		try {
			RandomAccessFile fp = new RandomAccessFile(file, "r");
			
			fp.skipBytes(s.length);
			
			fp.read(s);
			System.out.println(new String(s));
			
			fp.read(s);
			ByteBuffer buffer = ByteBuffer.wrap(s);
			System.out.println(buffer.getInt());
			
//			System.out.println(fp.readShort());
//			System.out.println(fp.readInt());
//			System.out.println(fp.readInt());
//			fp.read(s);
//			System.out.println(new String(s));
//			fp.read(s);
//			System.out.println(new String(s));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
