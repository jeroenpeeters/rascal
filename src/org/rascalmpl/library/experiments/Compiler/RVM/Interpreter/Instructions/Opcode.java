package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import java.io.PrintWriter;
import java.util.TreeMap;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CompilerError;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalPrimitive;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.MuPrimitive;



public enum Opcode {
	/*
	 * Instructions for the RVM. Each instruction has 
	 * 	- a unique opcode
	 * 	- a pc increment, i.e., the number code elements for this instruction
	 * 
	 * OPCODENAME(	opcode,	pc increment)
	 */
	LOADCON			    (0, 	1), //2),
	LOADVAR 			(1, 	1), //3),
	LOADLOC 			(2,		1), //2),
	STOREVAR 			(3, 	1), //3),
	STORELOC 			(4, 	1), //2),
	CALL 				(5, 	1), //3),
	CALLPRIM	 		(6, 	2), //3),
	RETURN1 			(7, 	1), //2),
	JMP 				(8, 	1), //2),
	JMPTRUE 			(9, 	1), //2),
	JMPFALSE 			(10, 	1), //2),
	LABEL 				(11, 	0),
	HALT 				(12, 	1),
	POP 				(13, 	1),
	CALLDYN				(14,	1), //2),
	LOADFUN				(15,	1), //2), // TODO: to be renamed to LOAD_ROOT_FUN
	NEXT0				(16,	1),
	NEXT1				(17,	1),
	YIELD0				(18,	1),
	YIELD1				(19,	1), //2),
	CREATE				(20,	1), //3),
	CREATEDYN           (21,    1), //2),
	PRINTLN				(22,	1), //2),
	RETURN0				(23,	1),
	LOADLOCREF			(24,	1), //2),
	LOADVARREF			(25,	1), //3),
	LOADLOCDEREF		(26,	1), //2),
	LOADVARDEREF		(27,	1), //3),
	STORELOCDEREF		(28,	1), //2),
	STOREVARDEREF		(29,	1), //3),
	LOADCONSTR			(30,	1), //2),
	CALLCONSTR			(31,	1/*2*/), //3), // TODO: plus number of formal parameters
	LOAD_NESTED_FUN		(32, 	1), //3),
	LOADTYPE			(33,	1), //2),
	CALLMUPRIM			(34,	1), //3),
	LOADBOOL			(35,	1), //2),
	LOADINT				(36,	1), //2),
	FAILRETURN			(37, 	1),
	LOADOFUN        	(38,    1), //2),
	OCALL           	(39,    2), //3),
	OCALLDYN	    	(40,	2), //3),
	CALLJAVA        	(41,    5),
	THROW           	(42,    1),
	TYPESWITCH			(43,	1), //2),
	UNWRAPTHROWNLOC     (44,    1), //2),
	FILTERRETURN		(45, 	1),
	EXHAUST             (46,    1),
	GUARD               (47,    1),
	SUBSCRIPTARRAY		(48,    1),
	SUBSCRIPTLIST		(49,    1),
	LESSINT				(50,	1),
	GREATEREQUALINT		(51,	1),
	ADDINT				(52,	1),
	SUBTRACTINT			(53,	1),
	ANDBOOL				(54,	1),
	TYPEOF				(55,	1),
	SUBTYPE				(56,	1),
	CHECKARGTYPE		(57,	1),
	LOADLOC0			(58, 	1),
	LOADLOC1			(59, 	1),
	LOADLOC2			(60, 	1),
	LOADLOC3			(61, 	1),
	LOADLOC4			(62, 	1),
	LOADLOC5			(63, 	1),
	LOADLOC6			(64, 	1),
	LOADLOC7			(65, 	1),
	LOADLOC8			(66, 	1),
	LOADLOC9			(67, 	1),
	JMPINDEXED			(68, 	1),
	LOADLOCKWP          (69,    1), // 2
	LOADVARKWP          (70,    1), // 3
	STORELOCKWP         (71,    1), // 2
	STOREVARKWP         (72,    1), // 3
	UNWRAPTHROWNVAR     (73,    1), // 3
	APPLY               (74,    1), // 3
	APPLYDYN            (75,    1), // 2
	LOADCONT            (76,    1), // 2
	RESET               (77,    1), // 2
	SHIFT               (78,    1)  // 2
	;
	
	
	private final int op;
	private final int pc_incr;
	
	public final static Opcode[] values = Opcode.values();
	
	public static Opcode fromInteger(int n){
		return values[n];
	}
	
	// TODO: compiler does not like Opcode.LOADCON.getOpcode() in case expressions
	// Here is a -- hopefully temporary -- hack that introduces explicit constants:
	// Beware! Should be in sync with the above operator codes!
	public static final int OP_LOADCON = 0;
	static public final int OP_LOADVAR = 1;
	static public final int OP_LOADLOC = 2;
	static public final int OP_STOREVAR= 3;
	static public final int OP_STORELOC = 4;
	static public final int OP_CALL = 5;
	static public final int OP_CALLPRIM = 6;
	static public final int OP_RETURN1 = 7;
	static public final int OP_JMP = 8;
	static public final int OP_JMPTRUE = 9;
	static public final int OP_JMPFALSE = 10;
	static public final int OP_LABEL = 11;
	static public final int OP_HALT = 12;
	static public final int OP_POP = 13;
	static public final int OP_CALLDYN = 14;
	static public final int OP_LOADFUN = 15;	
	static public final int OP_NEXT0 = 16;
	static public final int OP_NEXT1 = 17;
	static public final int OP_YIELD0 = 18;
	static public final int OP_YIELD1 = 19;
	static public final int OP_CREATE = 20;
	static public final int OP_CREATEDYN = 21;
	static public final int OP_PRINTLN = 22;
	static public final int OP_RETURN0 = 23;
	static public final int OP_LOADLOCREF = 24;
	static public final int OP_LOADVARREF = 25;
	static public final int OP_LOADLOCDEREF = 26;
	static public final int OP_LOADVARDEREF = 27;
	static public final int OP_STORELOCDEREF = 28;
	static public final int OP_STOREVARDEREF = 29;
	static public final int OP_LOADCONSTR = 30;
	static public final int OP_CALLCONSTR = 31;
	static public final int OP_LOAD_NESTED_FUN = 32;
	static public final int OP_LOADTYPE = 33;
	static public final int OP_CALLMUPRIM = 34;
	static public final int OP_LOADBOOL = 35;
	static public final int OP_LOADINT = 36;
	static public final int OP_FAILRETURN = 37;
	static public final int OP_LOADOFUN = 38;
	static public final int OP_OCALL = 39;
	static public final int OP_OCALLDYN = 40;
	static public final int OP_CALLJAVA = 41;
	static public final int OP_THROW = 42;
	static public final int OP_TYPESWITCH = 43;
	static public final int OP_UNWRAPTHROWNLOC = 44;
	static public final int OP_FILTERRETURN = 45;
	static public final int OP_EXHAUST = 46;
	static public final int OP_GUARD = 47;
	static public final int OP_SUBSCRIPTARRAY = 48;
	static public final int OP_SUBSCRIPTLIST = 49;
	static public final int OP_LESSINT = 50;
	static public final int OP_GREATEREQUALINT = 51;
	static public final int OP_ADDINT = 52;
	static public final int OP_SUBTRACTINT = 53;
	static public final int OP_ANDBOOL = 54;
	static public final int OP_TYPEOF = 55;
	static public final int OP_SUBTYPE = 56;
	static public final int OP_CHECKARGTYPE = 57;
	static public final int OP_LOADLOC0 = 58;
	static public final int OP_LOADLOC1 = 59;
	static public final int OP_LOADLOC2 = 60;
	static public final int OP_LOADLOC3 = 61;
	static public final int OP_LOADLOC4 = 62;
	static public final int OP_LOADLOC5 = 63;
	static public final int OP_LOADLOC6 = 64;
	static public final int OP_LOADLOC7 = 65;
	static public final int OP_LOADLOC8 = 66;
	static public final int OP_LOADLOC9 = 67;
	static public final int OP_JMPINDEXED = 68;
	static public final int OP_LOADLOCKWP = 69;
	static public final int OP_LOADVARKWP = 70;
	static public final int OP_STORELOCKWP = 71;
	static public final int OP_STOREVARKWP = 72;
	static public final int OP_UNWRAPTHROWNVAR = 73;
	static public final int OP_APPLY = 74;
	static public final int OP_APPLYDYN = 75;
	static public final int OP_LOADCONT = 76;
	static public final int OP_RESET = 77;
	static public final int OP_SHIFT = 78;
	
	
	/*
	 * Meta-instructions that are generated dynamically during execution and
	 * will never occur in generated code.
	 */
	static public final int POSTOP_CHECKUNDEF = 100;
	static public final int POSTOP_HANDLEEXCEPTION = 101;
	
	 Opcode(int op, int pc_incr){
		this.op = op;
		this.pc_incr = pc_incr;
	}
	 
	static long opFrequencies[];
	static boolean profiling = false;
	private static PrintWriter stdout;
	
	public static void init(PrintWriter stdoutWriter, boolean doProfile) {
	  stdout = stdoutWriter;
	  profiling = doProfile;
      opFrequencies = new long[values.length];
	}
	
	public static void use(int instruction){
		opFrequencies[CodeBlock.fetchOp(instruction)]++;
	}
	
	public static void exit(){
		if(profiling)
			printProfile();
	}
	
	private static void printProfile(){
		stdout.println("\nOpcode Frequencies");
		long total = 0;
		TreeMap<Long,String> data = new TreeMap<Long,String>();
		for(int i = 0; i < values.length; i++){
			if(opFrequencies[i] > 0 ){
				data.put(opFrequencies[i], values[i].name());
				total += opFrequencies[i];
			}
		}
		for(long t : data.descendingKeySet()){
			stdout.printf("%30s: %3d%% (%d)\n", data.get(t), t * 100 / total, t);
		}
	}
	
	public int getPcIncrement(){
		return pc_incr;
	}
	
	public int getOpcode(){
		return op;
	}
	
	public static String toString(CodeBlock cb, Opcode opc, int pc){
		int instruction = cb.finalCode[pc];
		Opcode opc1 = Opcode.fromInteger(CodeBlock.fetchOp(instruction));
		int arg1 = CodeBlock.fetchArg1(instruction);
		int arg2 = CodeBlock.fetchArg2(instruction);
		switch(opc1){
		case LOADCON:
			return "LOADCON " + cb.getConstantValue(arg1);
			
		case LOADVAR:
			return "LOADVAR " + arg1 + ", " + arg2;
			
		case LOADLOC:
			return "LOADLOC " + arg1;
			
		case STOREVAR:
			return "STOREVAR " + arg1 + ", " + arg2;	
			
		case STORELOC:
			return "STORELOC " + arg1;
			
		case CALL:
			return "CALL " + cb.getFunctionName(arg1)  + ", " + arg2;
			
		case CALLPRIM:
			return "CALLPRIM " + RascalPrimitive.fromInteger(arg1).name() +  ", " + arg2 + ", " + cb.getConstantValue(cb.finalCode[pc + 1]);
			
		case RETURN1:
			return "RETURN1 " + arg1;
			
		case JMP:
			return "JMP " + arg1;
			
		case JMPTRUE:
			return "JMPTRUE " + arg1;
			
		case JMPFALSE:
			return "JMPFALSE " + arg1;
			
		case LABEL:
			break;
			
		case HALT:
			return "HALT";
			
		case POP: 
			return "POP";	
			
		case CALLDYN:
			return "CALLDYN " + arg1;
			
		case LOADFUN:
			return "LOADFUN " + cb.getFunctionName(arg1) ;
			
		case NEXT0:
			return "NEXT0";
			
		case NEXT1:
			return "NEXT1";
			
		case YIELD0:
			return "YIELD0";
		
		case YIELD1:
			return "YIELD1 " + arg1;
		
		case CREATE:
			return "CREATE " + cb.getFunctionName(arg1) + ", " + arg2;
			
		case CREATEDYN:
			return "CREATEDYN " + arg1;
		
		case PRINTLN:
			return "PRINTLN " + arg1;
		
		case RETURN0:
			return "RETURN0";
		
		case LOADLOCREF:
			return "LOADLOCREF " + arg1;
			
		case LOADVARREF:
			return "LOADVARREF " + arg1 + ", " + arg2;
		
		case LOADLOCDEREF:
			return "LOADLOCDEREF " + arg1;
			
		case LOADVARDEREF:
			return "LOADVARDEREF " + arg1 + ", " + arg2;
			
		case STORELOCDEREF:
			return "STORELOCDEREF " + arg1;
		
		case STOREVARDEREF:
			return "STOREVARDEREF " + arg1 + ", " + arg2;
			
		case LOADCONSTR:
			return "LOADCONSTR " + arg1;
		
		case CALLCONSTR:
			return "CALLCONSTR " + arg1 + ", " + arg2  /*+ ", " + cb.getConstantValue(cb.finalCode[pc + 1])*/ ;
		
		case LOAD_NESTED_FUN:
			return "LOAD_NESTED_FUN " + arg1 + ", " + arg2;
			
		case LOADTYPE:
			return "LOADTYPE " + arg1;
			
		case CALLMUPRIM:
			return "CALLMUPRIM " + MuPrimitive.fromInteger(arg1).name() +  ", " + arg2;
			
		case LOADBOOL:
			return "LOADBOOL " + (arg1 == 1);
			
		case LOADINT:
			return "LOADINT " + arg1;
			
		case FAILRETURN:
			return "FAILRETURN";
			
		case LOADOFUN:
			return "LOADOFUN " + cb.getOverloadedFunctionName(arg1);
			
		case OCALL:
			return "OCALL " +  cb.getOverloadedFunctionName(arg1)  + ", " + arg2 + ", " + cb.getConstantValue(cb.finalCode[pc + 1]);
			
		case OCALLDYN:
			return "OCALLDYN " + cb.getConstantType(arg1) + ", " + arg2 + ", " + cb.getConstantValue(cb.finalCode[pc + 1]);
			
		case CALLJAVA:	
			return "CALLJAVA " + cb.getConstantValue(cb.finalCode[pc + 1]) + ", " + cb.getConstantValue(cb.finalCode[pc + 2]) + ", " + cb.getConstantType(cb.finalCode[pc + 3]) + "," + cb.finalCode[pc + 4] ;
			
		case THROW:
			return "THROW " +  cb.getConstantValue(arg1);
			
		case TYPESWITCH:
			return "TYPESWITCH " + cb.getConstantValue(arg1);
			
		case UNWRAPTHROWNLOC:
			return "UNWRAPTHROWNLOC " + arg1;
			
		case FILTERRETURN:
			return "FILTERRETURN";
			
		case EXHAUST:
			return "TERMINATE";
			
		case GUARD:
			return "GUARD";
			
		case SUBSCRIPTARRAY:
			return "SUBSCRIPTARRAY";
			
		case SUBSCRIPTLIST:
			return "SUBSCRIPTLIST";
			
		case LESSINT:
			return "LESSINT";
			
		case GREATEREQUALINT:
			return "GREATEREQUALINT";
			
		case ADDINT:
			return "ADDINT";
			
		case SUBTRACTINT:
			return "SUBTRACTINT";
			
		case ANDBOOL:
			return "ANDBOOL";
			
		case TYPEOF:
			return "TYPEOF";
			
		case SUBTYPE:
			return "SUBTYPE";
			
		case CHECKARGTYPE:
			return "CHECKARGTYPE";
		case LOADLOC0:
			return "LOADLOC0";
		case LOADLOC1:
			return "LOADLOC1";
		case LOADLOC2:
			return "LOADLOC2";
		case LOADLOC3:
			return "LOADLOC3";
		case LOADLOC4:
			return "LOADLOC4";
		case LOADLOC5:
			return "LOADLOC5";
		case LOADLOC6:
			return "LOADLOC6";
		case LOADLOC7:
			return "LOADLOC7";
		case LOADLOC8:
			return "LOADLOC8";
		case LOADLOC9:
			return "LOADLOC9";
		case JMPINDEXED:
			return "JMPINDEXED " + cb.getConstantValue(arg1);
			
		case LOADLOCKWP:
			return "LOADLOCKWP " + cb.getConstantValue(arg1);		
		case LOADVARKWP:
			return "LOADVARKWP " + cb.getConstantValue(arg1) + ", " + cb.getConstantValue(arg2);
		case STORELOCKWP:
			return "STORELOCKWP " + cb.getConstantValue(arg1);
		case STOREVARKWP:
			return "STOREVARKWP " + cb.getConstantValue(arg1) + ", " + cb.getConstantValue(arg2);
			
		case UNWRAPTHROWNVAR:
			return "UNWRAPTHROWNVAR " + arg1 + arg2;
			
		case APPLY:
			return "APPLY " + cb.getFunctionName(arg1) + ", " + arg2;
			
		case APPLYDYN:
			return "APPLYDYN " + arg1;
			
		case LOADCONT:
			return "LOADCONT " + arg1;
		
		case RESET:
			return "RESET";
			
		case SHIFT:
			return "SHIFT";
		
		default:
			break;
		}	
		
		throw new CompilerError("unrecognized opcode " + opc);
	}
}
