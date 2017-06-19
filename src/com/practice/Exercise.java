package com.practice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Exercise implements Serializable {
	private static final long serialVersionUID = -1622536020144679558L;

	class Answers implements Serializable {
		private static final long serialVersionUID = -7833709422448085208L;
		String content;
		boolean correct;

		public Answers() {
			content = "";
			correct = false;
		}

		public Answers(String ans, boolean cr) {
			content = ans;
			correct = cr;
		}
	}

	private ArrayList<Operation> operationList = new ArrayList<Operation>();
	private List<Answers> answers = new ArrayList<>();
	private int current = 0;
	private Mode currentType;

	public Mode getCurrentType() {
		return currentType;
	}

	public void setAnswer(int index, String ans) {
		Operation op;
		op = operationList.get(index);
		String result = String.valueOf(op.getResult());
		String tans = ans.trim();
		answers.set(index, new Answers(tans, result.equals(tans)));
	}

	public String getAnswer(int index) {
		return answers.get(index).content;
	}

	public void clearAnswers() {
		for (int i = 0; i < answers.size(); i++)
			answers.set(i, new Answers("", false));
	}

	public int Correct() {
		int count = 0;
		for (int i = 0; i < answers.size(); i++) {
			if (answers.get(i).correct)
				count++;
		}
		return count;
	}

	private void setCurrentType(Mode type) {
		this.currentType = type;
	}

	public boolean getJudgement(int index) {
		return answers.get(index).correct;
	}

	private Operation generateOperation() {
		Random random = new Random();
		int opValue = random.nextInt(2);
		if (opValue == 1) {
			return new Addition();
		}
		return new Substract();
	}

	public void saveObject(String filename) throws Exception { // 串行化存储对象
		try {
			FileOutputStream fos = new FileOutputStream(filename);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(this);
			oos.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("存储对象失败");
		}
	}

	public static Exercise loadObject(String filename) throws Exception { // 串行化载入对象
		Exercise exercise = null;
		try {
			FileInputStream fis = new FileInputStream(filename);
			ObjectInputStream ois = new ObjectInputStream(fis);
			exercise = (Exercise) ois.readObject();
			ois.close();
			fis.close();
		} catch (Exception e) {
			throw new Exception("载入对象失败");
		}
		return exercise;
	}

	public void generateWithFormerType(int operationCount) {
		switch (currentType) {
		case BINARY:
			this.generateBinaryExercise(operationCount);
			break;
		case ADDITION:
			this.generateAdditionExercise(operationCount);
			break;
		case SUBTRACT:
			this.generateSubstractExercise(operationCount);
			break;
		}
	}

	public void generateAdditionExercise(int operationCount) {
		Operation anOperation;
		setCurrentType(Mode.ADDITION);
		operationList.clear();
		answers.clear();
		while (operationCount > 0) {
			do {
				anOperation = new Addition();
			} while (operationList.contains(anOperation));
			operationList.add(anOperation);
			answers.add(new Answers("", false));
			operationCount--;
		}
	}

	public void generateBinaryExercise(int operationCount) {
		Operation anOperation;
		setCurrentType(Mode.BINARY);
		operationList.clear();
		answers.clear();
		while (operationCount > 0) {
			do {
				anOperation = generateOperation();
			} while (operationList.contains(anOperation));
			operationList.add(anOperation);
			answers.add(new Answers("", false));
			operationCount--;
		}
	}

	public void generateSubstractExercise(int operationCount) {
		Operation anOperation;
		setCurrentType(Mode.SUBTRACT);
		operationList.clear();
		answers.clear();
		while (operationCount > 0) {
			do {
				anOperation = new Substract();
			} while (operationList.contains(anOperation));
			operationList.add(anOperation);
			answers.add(new Answers("", false));
			operationCount--;
		}
	}

	public void add(Operation anOperation) {
		operationList.add(anOperation);
	}

	public boolean contains(Operation anOperation) {
		return operationList.contains(anOperation);
	}

	public int length() {
		return operationList.size();
	}

	public void writeExercise() {
		File wfile = new File("eq2.txt");
		try {
			Writer out = new FileWriter(wfile, true);
			for (Operation op : operationList) {
				out.write(op.toString() + ",");
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			System.out.println("ERROR: " + e);
		}
	}

	public void writeCSVExercise(File aFile) {
		try {
			Writer out = new FileWriter(aFile, true);
			for (Operation op : operationList) {
				out.write(op.toString() + ",");
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			System.out.println("ERROR: " + e);
		}
	}

	public Exercise readCSVExercise() {
		Exercise exercise = new Exercise();
		String eqString;
		Operation op;
		Scanner sc = null;
		File rfile = new File("eq2.txt");
		try {
			sc = new Scanner(rfile);
			sc.useDelimiter(",\\n");

			while (sc.hasNext()) {
				eqString = sc.next();
				op = new Addition();
				op.unsafeConstructor(eqString);
				exercise.add(op);
			}
		} catch (IOException e) {
			System.out.println("ERROR: " + e);
		}

		return exercise;
	}

	public Exercise readCSVExercise(File aFile) {
		Exercise exercise = new Exercise();
		String eqString;
		Operation op;
		try {
			Scanner sc = new Scanner(aFile);
			sc.useDelimiter(",");

			while (sc.hasNext()) {
				eqString = sc.next().replaceAll("\\s", "");
				op = new Addition();
				op.unsafeConstructor(eqString);
				exercise.add(op);
			}
		} catch (IOException e) {
			System.out.println("ERROR: " + e);
		}

		return exercise;
	}

	public boolean hasNext() {
		return current <= operationList.size() - 1;
	}

	public Operation next() {
		return operationList.get(current++);
	}

	public void printCurrent() {
		System.out.println("current=" + current);
	}

	public Operation getOperation(int index) {
		if (index < operationList.size())
			return operationList.get(index);
		else
			return null;
	}

	public void all() {
		for (Operation op : operationList) {
			System.out.println(op.asString());
		}
	}

	public void writeResults(File aFile) {
		try {
			Writer out = new FileWriter(aFile, true);
			for (Operation op : operationList) {
				out.write(op.getResult() + ",");
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			System.out.println("ERROR: " + e);
		}
	}

	public Iterator<Operation> iterator() {
		return operationList.iterator();
	}
}
