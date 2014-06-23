package com.cjp.app.exafs.ifeffit;

import static java.nio.file.StandardCopyOption.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;

import com.cjp.app.exafs.pdb.Atom;
import com.cjp.io.MapParser;

public class IFEFFITHelper {
	
	private final MapParser _config;
	
	private double _lastRMSD;
	private List<Tuple> _lastEXAFSData;
	private final List<Tuple> _targetEXAFSData;
	
	private Map<String, Integer> _atomicSymboleIndexes;
	List<Integer> _targetAtomIndexes;
	private String _feffHeader;
	
	private static final String IFEFFIT_DIRECTORY = "ifeffit-dir";
	private static final String TARGET_ATOM = "target-atom";
	private static final String EXAFS_FILE = "exafs-file";
	private static final String CONTAINER_DIRECTORY = "exafs-container-";
	private static final String FEFF_INPUT_FILE = "feff.inp";
	private static final String PROCESS_INPUT_FILE = "process.iff";
	private static final String FEFF_SCRIPT = "feffer.sh";
	private static final String IFEFFIT_SCRIPT = "ifeffiter.sh";
	private static final String FEFF_COMMAND = "feff";
	private static final String IFEFFIT_COMMAND = "ifeffit";
	private static final String IFEFFIT_OUTPUT = "my_chi.chi3";
	private static final String MIN_X = "min-x";
	private static final String MAX_X = "max-x";
	
	private String temp_CONTAINER_DIRECTORY;
	
	public IFEFFITHelper(MapParser config, List<Atom> initialAtoms) {
		
		_config = config;
		
		// FEFF File Config
		_atomicSymboleIndexes = new HashMap<String, Integer>();
		_targetAtomIndexes = new ArrayList<Integer>();
		String targetAtom = config.getString(TARGET_ATOM);
		
		final String FEFF_HEADER = "TITLE My Molecule\nPOTENTIALS\n*	ipot	z	tag";
		final String FEFF_MID = "CONTROL 1 1 1 1 1 1\nNLEG 8\nPRINT 0 0 0 3\nEXCHANGE 0 0.0 0.0\nCRITERIA 4.0 2.5\nATOMS\n*	x	y	z	ipot	tag";
		
		StringBuilder headerBuilder = new StringBuilder();
		
		headerBuilder.append(FEFF_HEADER);
		headerBuilder.append(String.format("\n\t0\t%d\t%s", new Atom(null, targetAtom, false).getAtomicNumber(), targetAtom));
		
		// Get unique atomic symbol indexes.
		int currentAtomIndex = 1;
		for (int i = 0; i < initialAtoms.size(); i++) {
			
			Atom atom = initialAtoms.get(i);
			if (!_atomicSymboleIndexes.containsKey(atom.getAtomicSymbol())) {
					_atomicSymboleIndexes.put(atom.getAtomicSymbol(), currentAtomIndex);
					headerBuilder.append(String.format("\n\t%d\t%d\t%s", currentAtomIndex, atom.getAtomicNumber(), atom.getAtomicSymbol()));
					currentAtomIndex++;
			}
			
			if (atom.getAtomicSymbol().equalsIgnoreCase(targetAtom)) {
				_targetAtomIndexes.add(i);
			}
		}
		
		headerBuilder.append("\n");
		headerBuilder.append(FEFF_MID);
		_feffHeader = headerBuilder.toString();
		
		File file = new File(config.getString(IFEFFIT_DIRECTORY));
		
		if (!file.exists()) {
			throw new RuntimeException("IFEFFIT Directory is incorrect or missing.");
		}
		
		// Create container directory.
		File containerFile;
		do {
			temp_CONTAINER_DIRECTORY = CONTAINER_DIRECTORY + ((int)(Math.random()*10000));
			containerFile = new File(file, temp_CONTAINER_DIRECTORY);
		} while (containerFile.exists());
		
		containerFile.mkdir();
		
		// Create feff script.
		PrintWriter feffWriter = null;
		PrintWriter ifeffitWriter = null;
		try {
			feffWriter = new PrintWriter(new File(containerFile, FEFF_SCRIPT));
			ifeffitWriter = new PrintWriter(new File(containerFile, IFEFFIT_SCRIPT));
		} catch (FileNotFoundException e) { e.printStackTrace(); }
		
		// Create folder for each target atom index.
		for (int i = 0; i < _targetAtomIndexes.size(); i++) {
			
			File newDirectory = new File(containerFile, ""+i);
			newDirectory.mkdir();
			
			File exafsFile = new File(file, config.getString(EXAFS_FILE));
			File movedExafsFile = new File(newDirectory, config.getString(EXAFS_FILE));
			
			try {
				Files.copy(Paths.get(exafsFile.toURI()), Paths.get(movedExafsFile.toURI()), REPLACE_EXISTING);
			} catch (IOException e) {
				System.err.println("Could not copy exafs file.");
				e.printStackTrace();
				System.exit(0);
			}
			
			feffWriter.println("cd " + newDirectory.getAbsolutePath());
			feffWriter.println(config.getString(FEFF_COMMAND));
			
			ifeffitWriter.println("cd " + newDirectory.getAbsolutePath());
			ifeffitWriter.println(config.getString(IFEFFIT_COMMAND) + " " + PROCESS_INPUT_FILE);
		}
		
		feffWriter.close();
		ifeffitWriter.close();
		
		// Read in target EXAFS data.
		File exafsFile = new File(file, config.getString(EXAFS_FILE));
		_targetEXAFSData = readTupleFile(exafsFile);
	}
	
	private List<Tuple> readTupleFile(File file) {
		
		List<Tuple> list = new ArrayList<Tuple>();
		try {
			BufferedReader exafsFileReader = new BufferedReader(new FileReader(file));
			while(exafsFileReader.ready()) {
				StringTokenizer tokenizer = new StringTokenizer(exafsFileReader.readLine());
				if (tokenizer.countTokens() != 2) continue;
				
				try {
					list.add(new Tuple(Double.parseDouble(tokenizer.nextToken()),Double.parseDouble(tokenizer.nextToken())));					
				} catch (Exception e) {
					exafsFileReader.close();
					return null;
				}
				
			}
			exafsFileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public void evaluate(List<Atom> atoms) {
		
		generateFEFFFiles(atoms);
//		System.out.println("run feff");
		runFEFFCommand();
//		System.out.println("done feff");
		if (generateProcessFiles()) {
			runIFEFFITCommand();
			calculateRMSD();
		}
		
		cleanDirectories();
		
	}
	
	private void cleanDirectories() {
		
		for (int i = 0; i < _targetAtomIndexes.size(); i++) {
			cleanDirectory(i);
		}
	}

	private void cleanDirectory(int index) {
		
		File IFEFFITFolder = new File(new File(_config.getString(IFEFFIT_DIRECTORY), temp_CONTAINER_DIRECTORY), ""+index);
		for (File file : IFEFFITFolder.listFiles()) {
			if (!file.getName().contains(_config.getString(EXAFS_FILE)) && !file.getName().contains(IFEFFIT_OUTPUT)) {
				file.delete();
			}
		}
	}

	private void calculateRMSD() {
		
		List< List<Tuple> > ifeffitOutput = new ArrayList< List<Tuple> >();
		for (int i = 0; i < _targetAtomIndexes.size(); i++) {
			File IFEFFITFolder = new File(new File(_config.getString(IFEFFIT_DIRECTORY), temp_CONTAINER_DIRECTORY), ""+i);
			File ifeffitResultFile = new File(IFEFFITFolder, IFEFFIT_OUTPUT);
			
			if (!ifeffitResultFile.exists()) {
				System.err.println("IFEFFIT Error. Skipping");
				generateFailedAttempt();
				return;
			}
			List<Tuple> tupleList = readTupleFile(ifeffitResultFile);
			if (tupleList == null) {
				System.err.println("IFEFFIT Error. Skipping");
				generateFailedAttempt();
				return;
			}
			
			
			ifeffitOutput.add(tupleList);
		}
		
		// Get average
		List<Tuple> averageIfeffitOutput = new ArrayList<Tuple>();
		double[] averageOutput = new double[ifeffitOutput.get(0).size()];
		for (int i = 0; i < ifeffitOutput.size(); i++) {
			for (int j = 0; j < ifeffitOutput.get(0).size(); j++) {
				averageOutput[j] += ifeffitOutput.get(i).get(j).y;
			}
		}
		
		for (int i = 0; i < averageOutput.length; i++) {
			averageOutput[i] /= ifeffitOutput.size();
			averageIfeffitOutput.add(new Tuple(ifeffitOutput.get(0).get(i).x,averageOutput[i]));
		}
		
		
		
		// Compare to target
		
		double rmsd = 0;
		int targetEXAFSIndex = 0;
		int averagedEXAFSIndex = 0;
		
		while (targetEXAFSIndex < _targetEXAFSData.size() && averagedEXAFSIndex < averageIfeffitOutput.size()) {
			
			if (_targetEXAFSData.get(targetEXAFSIndex).x == averageIfeffitOutput.get(averagedEXAFSIndex).x) {
				
				if (averageIfeffitOutput.get(averagedEXAFSIndex).x > _config.getDouble(MIN_X) && averageIfeffitOutput.get(averagedEXAFSIndex).x < _config.getDouble(MAX_X)) {
					rmsd += Math.pow(averageIfeffitOutput.get(averagedEXAFSIndex).y - _targetEXAFSData.get(targetEXAFSIndex).y , 2);
				}
				targetEXAFSIndex++;
				averagedEXAFSIndex++;
			} else if (_targetEXAFSData.get(targetEXAFSIndex).x < averageIfeffitOutput.get(averagedEXAFSIndex).x) {
				targetEXAFSIndex++;
			} else {
				averagedEXAFSIndex++;
			}
		}
		
		_lastRMSD = rmsd;
	}

	private void runIFEFFITCommand() {
		
		File file = new File(_config.getString(IFEFFIT_DIRECTORY));
		File containerFile = new File(file, temp_CONTAINER_DIRECTORY);
		File ifeffitFile = new File(containerFile, IFEFFIT_SCRIPT);
		
		try {
			Process pr = Runtime.getRuntime().exec("bash " + ifeffitFile.getAbsolutePath());
			pr.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private boolean generateProcessFiles() {
		
		for (int i = 0; i < _targetAtomIndexes.size(); i++) {
			if (!generateProcessFile(i)) {
				System.err.println("generateProcessFile Error.");
				generateFailedAttempt();
				return false;
			}
		}
		
		return true;
	}

	private boolean generateProcessFile(int index) {
		
		String processEXAFSFile = "read_data(file=" + _config.getString(EXAFS_FILE) + ",type=chi,group=data)";
		final String PROCESS_HEADER = "my.k = data.k\nmy.chi = data.chi/(data.k**3)\nnewdata.k = range(0,10.0,0.05)\nnewdata.chi = qinterp(my.k, my.chi, newdata.k)\nguess e = 0.0\nset s = 1.0\nguess s1 = 0.0025";
		final String PROCESS_FOOTER = "set (kmin = 1.0, kmax =10.0)\nset (kweight=3,dk = 1, kwindow='hanning')\nset (rmin = 0, rmax = 6)\nff2chi(1-100,group = init)\nfeffit(1-100,chi = newdata.chi, group=fit )\nfit3.chi = fit.chi*fit.k**3\nwrite_data(file=my_chi.chi3,fit.k,fit3.chi)\nexit";
		
		File IFEFFITFolder = new File(new File(_config.getString(IFEFFIT_DIRECTORY), temp_CONTAINER_DIRECTORY), ""+index);
		List<String> feffFiles = new ArrayList<String>();
		for (File file : IFEFFITFolder.listFiles()) {
			if (file.getName().contains("feff00")) {
				feffFiles.add(file.getName());
			}
		}
		
		if (feffFiles.size() == 0) {
			return false;
		}
		
		Collections.sort(feffFiles);
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new File(IFEFFITFolder, PROCESS_INPUT_FILE));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		writer.println(processEXAFSFile);
		writer.println(PROCESS_HEADER);
		
		for (int i = 0; i < feffFiles.size(); i++) {
			writer.println("path(" + (i+1) + ", file=" + feffFiles.get(i) + ", e0 = e, s02 = s, sigma2 = s1)");
		}
		
		writer.println(PROCESS_FOOTER);
		writer.close();
		
		return true;
	}

	private void generateFEFFFiles(List<Atom> atoms) {
		
		for (int i = 0; i < _targetAtomIndexes.size(); i++) {
			generateFEFFFile(atoms, i);
		}
	}
	
	private void generateFEFFFile(List<Atom> atoms, int index) {
		
		PrintWriter writer;
		try {
			File IFEFFITFolder = new File(new File(_config.getString(IFEFFIT_DIRECTORY), temp_CONTAINER_DIRECTORY), ""+index);
			writer = new PrintWriter(new File(IFEFFITFolder, FEFF_INPUT_FILE));
			
			writer.println(_feffHeader);
			
			for (int i = 0; i < atoms.size(); i++) {
				
				writer.print(String.format("\t%.15f\t%.15f\t%.15f\t", atoms.get(i).getPoint().x, atoms.get(i).getPoint().y, atoms.get(i).getPoint().z));
				
				if (_targetAtomIndexes.get(index) == i) {
					writer.print(0);
				} else {
					writer.print(_atomicSymboleIndexes.get(atoms.get(i).getAtomicSymbol()));
				}
				
				writer.println(String.format("\t%s", atoms.get(i).getAtomicSymbol()));
			}
			
			writer.println("END");
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	private void runFEFFCommand() {
		
		File file = new File(_config.getString(IFEFFIT_DIRECTORY));
		File containerFile = new File(file, temp_CONTAINER_DIRECTORY);
		File feffFile = new File(containerFile, FEFF_SCRIPT);
		
		try {
			Process pr = Runtime.getRuntime().exec("bash " + feffFile.getAbsolutePath());
			
			InputStreamReader reader = new InputStreamReader(pr.getInputStream());
			while(reader.read() != -1){}
			
			pr.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public double getRMSD() {
		return _lastRMSD;
	}
	
	public List<Tuple> getLastEXAFSData() {
		return _lastEXAFSData;
	}
	
	public void cleanUp() {
		
		File containerDirectory = new File(_config.getString(IFEFFIT_DIRECTORY), temp_CONTAINER_DIRECTORY);
		if (containerDirectory.exists()) {
			deleteDirectory(containerDirectory);
		}
	}
	
	private void deleteDirectory(File directory) {
		
		if (directory.exists()) {
			for (File file : directory.listFiles()) {
				if (file.isDirectory()) {
					deleteDirectory(file);
				} else {
					file.delete();
				}
			}
			directory.delete();
		}
	}
	
	private void generateFailedAttempt() {
		_lastRMSD = 99999;
		_lastEXAFSData = null;
	}
}
