@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@doc{Tests the potential clashes among value constructors of different adts, plus, the identified clash with: bool eq(value, value);}
module lang::rascal::\syntax::tests::ImplodeTests

import lang::rascal::\syntax::tests::ImplodeTestGrammar;
import ParseTree;
import Exception;

import IO;


public data Num = \int(str n);
public data Exp = id(str name) | eq(Exp e1, Exp e2) | number(Num n);
public Exp number(Num::\int("0")) = Exp::number(Num::\int("01"));

public anno loc Num@location;
public anno loc Exp@location;
public anno map[int,list[str]] Num@comments;
public anno map[int,list[str]] Exp@comments;

public data Number = \int(str n);
public data Expr = id(str name) | eq(Expr e1, Expr e2) | number(Number n);
public Expr number(Number::\int("0")) = Expr::number(Number::\int("02"));

public anno loc Number@location;
public anno loc Expr@location;
public anno map[int,list[str]] Number@comments;
public anno map[int,list[str]] Expr@comments;

public Exp implodeExp(str s) = implode(#Exp, parseExp(s));
public Exp implodeExpLit1() = implode(#Exp, expLit1());
public Exp implodeExpLit2() = implode(#Exp, expLit2());

public Expr implodeExpr(str s) = implode(#Expr, parseExp(s));
public Expr implodeExprLit1() = implode(#Expr, exprLit1());
public Expr implodeExprLit2() = implode(#Expr, exprLit2());

public test bool test1() {
	try {
		bool tst = true;
		tst = tst && Exp::id(_) := implodeExp("a");
		tst = tst && Exp::number(Num::\int("01")) := implodeExp("0");
		tst = tst && Exp::eq(Exp::id(_),Exp::id(_)) := implodeExp("a == b");
		tst = tst && Exp::eq(Exp::number(Num::\int("01")), Exp::number(Num::\int("1"))) := implodeExp("0 == 1");
		tst = tst && Expr::id(_) := implodeExpr("a");
		tst = tst && Expr::number(Number::\int("02")) := implodeExpr("0");
		tst = tst && Expr::eq(Expr::id(_),Expr::id(_)) := implodeExpr("a == b");
		tst = tst && Expr::eq(Expr::number(Number::\int("02")), Expr::number(Number::\int("1"))) := implodeExpr("0 == 1");
		return tst;
	} catch ImplodeError(_): return false;
}

public test bool test2() {
	try {
		bool tst = true;
		tst = tst && Exp::eq(Exp::id("a"),Exp::id("b")) := implodeExpLit1();
		tst = tst && Exp::eq(Exp::id("a"),Exp::number(Num::\int("11"))) := implodeExpLit2();
		tst = tst && Expr::eq(Expr::id("a"),Expr::id("b")) := implodeExprLit1();
		tst = tst && Expr::eq(Expr::id("a"),Expr::number(Number::\int("11"))) := implodeExprLit2();
		return tst;
	} catch ImplodeError(_): return false;
}