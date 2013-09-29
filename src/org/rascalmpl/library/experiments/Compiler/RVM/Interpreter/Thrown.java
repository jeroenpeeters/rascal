package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.PrintWriter;
import java.util.List;

import org.eclipse.imp.pdb.facts.IValue;

public class Thrown extends RuntimeException {
	
	private static final long serialVersionUID = 5789848344801944419L;
	
	private static Thrown instance = new Thrown();
	
	IValue value;
	List<Frame> stacktrace;
	
	private Thrown() {
		this.value = null;
		this.stacktrace = null;
	}
	
	public static Thrown getInstance(IValue value, List<Frame> stacktrace) {
		instance.value = value;
		instance.stacktrace = stacktrace;
		return instance;
	}

	public String toString() {
		return value.toString();
	}
	
	public void printStackTrace(PrintWriter stdout) {
		stdout.println("EXCEPTION: throw " + this.toString());
		for(Frame cf : stacktrace) {
			for(Frame f = cf; f != null; f = cf.previousCallFrame) {
				stdout.println("at " + f.function.name);
			}
		}
	}
	
}