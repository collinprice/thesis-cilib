package com.cjp.app.exafs.pdb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.text.AbstractDocument.BranchElement;

public class XYZReader {
	
	public static List<List<Point3D>> readDirectory(String directory_path, final String extension) {
		
		File directory = new File(directory_path);
		
		if (!directory.isDirectory()) {
			return null;
		}
		
		List<List<Point3D>> pointLists = new ArrayList<List<Point3D>>();
		
		// Retrieve the files in the directory with the given extension.
		File[] xyzFiles = directory.listFiles(new FilenameFilter() {
		    public boolean accept(File directory, String fileName) {
		        return fileName.endsWith("." + extension);
		    }
		});
		
		for (File filename : xyzFiles) {
			pointLists.add(XYZReader.readFile(filename));
		}
		
		System.out.println(pointLists.size());
		
		return pointLists;
	}
	
	public static List<List<Point3D>> readDirectory(String directory_path) {
		return XYZReader.readDirectory(directory_path, "xyz");
	}

	public static List<Point3D> readFile(File file) {
		
		List<Point3D> list = new ArrayList<Point3D>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			
			// First line contains atom count.
			int atomCount = Integer.parseInt(br.readLine());
			
			// Second line is comment line. Used for comments.
			br.readLine();
			
			for (int i = 0; i < atomCount; i++) {
				
				StringTokenizer tokenizer = new StringTokenizer(br.readLine());
				
				// Atomic symbol
				tokenizer.nextToken();
				
				double x = Double.parseDouble(tokenizer.nextToken());
				double y = Double.parseDouble(tokenizer.nextToken());
				double z = Double.parseDouble(tokenizer.nextToken());
				
				list.add(new Point3D(x, y, z));
			}
			
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		return list;
	}
	
}
