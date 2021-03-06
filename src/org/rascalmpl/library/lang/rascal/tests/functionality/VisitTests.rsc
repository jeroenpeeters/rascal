module lang::rascal::tests::functionality::VisitTests

/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Bert Lisser - Bert.Lisser@cwi.nl - CWI
*******************************************************************************/

data NODE1 = f(value V) | f(value V1, value V2) | f(value V1, value V2, value V3) | g(value V1, value V2) | h(value V1, value V2)|h(value V1, value V2, value V3);

data T = knot(int i, T l, T r) | tip(int i);

data NODE10 = f1(int I) | g1(list[NODE10] L) | h1(NODE10 N1, NODE10 N2);
		
		int cnt(NODE1 t) {
		     int C = 0;
		     visit(t) {
		        case int N: C = C + 1;
		        }
		     return C;
		     }
		     
		NODE1 walk(NODE1 t) {
		     return visit(t) {
		        case int N=>x when x:=N*2, x>=1
		        };
		     }
		     
		NODE1 drepl(NODE1 T) {
				return bottom-up-break visit (T) {
				     case g(value T1, value T2) =>  h(T1, T2)
				     };
			   }
			   
		NODE1 frepa(NODE1 T) {
				return visit (T) {
				    case g(value T1, value T2):
				          insert h(T1, T2);
				   };
				}
				
		NODE1 frepb(NODE1 T) { 
				return visit (T) {
				     case g(value T1, value T2) => h(T1, T2)
			          };
				}
				
		NODE1 frepG2H3a(NODE1 T) {
				return visit (T) {
				    case g(value T1, value T2):
				         insert h(T1, T2, 0);
				   };
				}
				
		NODE1 frepG2H3b(NODE1 T) {
				return visit (T) {
				   case g(value T1, value T2) => h(T1, T2, 0)
				   };
				}
				
		NODE1 inc(NODE1 T) {
				return visit(T) {
				     case int N: insert N + 1;
				   }
				}
				
		
				
		tuple[int, NODE1] inc_and_count(NODE1 T, int D) {
				int C = 0;
				T = visit (T) {
				        case int N: { C = C + 1;
				                      insert N + D;
				                    }
				       };
				return <C, T>;
			}
		
		NODE1 srepl(NODE1 T) {
				return top-down-break visit (T) {
				     case g(value T1, value T2) =>  h(T1, T2)
			     };
			  }	
	  
		list[int] order(NODE10 T) {
				res = [];
				visit (T) {
				   case int N:  res += N;
				};
				return res;
				}	
     
//  Cnt()

test bool Cnt1()= cnt(f(3)) == 1;
test bool Cnt2()= cnt(f(1,2,3)) == 3;
test bool Cnt3()= cnt(f(1,g(2,3))) == 3;
test bool Cnt4()= cnt(f(1,g(2,[3,4,5]))) == 5;
test bool Cnt5()= cnt(f(1,g(2,{3,4,5}))) == 5;
test bool Cnt6()= cnt(f(1,g(2,<3,4,5>))) == 5;
test bool Cnt7()= cnt(f(1,g(2,{<1,10>,<2,20>}))) == 6;
test bool Cnt8()= cnt(f(1,g(2,(1:10,2:20)))) == 6;
	
//	When

test bool When1()= walk(f(3)) == f(6);
test bool When2()= walk(f(1,2,3)) == f(2,4,6);
test bool When3()= walk(f(1,g(2,3))) == f(2, g(4, 6));
test bool When4()= walk(f(1,g(2,[3,4,5]))) == f(2, g(4, [6, 8, 10]));

//	NewTreeVisibleBottomUp

	test bool NewTreeVisibleBottomUp() =
		visit(knot(0,tip(0),tip(0))) { case tip(int i) => tip(i+1) case knot(int i, T l, T r) => knot(i + l.i + r.i, l, r) } == knot(2,tip(1),tip(1));
	
//	Drepl

test bool Drepl1()= drepl(f(3)) == f(3);
test bool Drepl2()= drepl(g(1,2)) == h(1,2);
test bool Drepl3()= drepl(g(1,f(g(2,3)))) == g(1,f(h(2,3)));
		// The following test used to work, but now that we are using more and more static types it fails.
		// Explanation: [g(2,3),4,5] has as type list[value] and the elements have static type value as well.
		// In particular g(2,3) has type value.
		// As a result the node pattern g(value T1, value T2) in the case does not match.
		// test bool Drepl()= + drepl + "drepl(g(1,f([g(2,3),4,5]))) == g(1,f([h(2,3),4,5]));}"));


//	FrepA	

test bool FrepA1()= frepa(f(3)) == f(3);
test bool FrepA2()= frepa(f(1,2,3)) == f(1,2,3);
test bool FrepA3()= frepa(f(1,g(2,3))) == f(1,h(2,3));
test bool FrepA4()= frepa(f(1,g(2,[3,4,5]))) == f(1,h(2,[3,4,5]));
test bool FrepA5()= frepa(f(1,g(2,{3,4,5}))) == f(1,h(2,{3,4,5}));
test bool FrepA6()= frepa(f(1,g(2,<3,4,5>))) == f(1,h(2,<3,4,5>));
test bool FrepA7()= frepa(f(1,g(2,{<1,10>,<2,20>}))) == f(1,h(2,{<1,10>,<2,20>}));
test bool FrepA8()= frepa(f(1,g(2,(1:10,2:20)))) == f(1,h(2,(1:10,2:20)));
	
//	FrepB

test bool FrepB1()= frepb(f(3)) == f(3);
test bool FrepB2()= frepb(f(1,2,3)) == f(1,2,3);
test bool FrepB3()= frepb(f(1,g(2,3))) == f(1,h(2,3));
test bool FrepB4()= frepb(f(1,g(2,[3,4,5]))) == f(1,h(2,[3,4,5]));
test bool FrepB5()= frepb(f(1,g(2,{3,4,5}))) == f(1,h(2,{3,4,5}));
test bool FrepB6()= frepb(f(1,g(2,<3,4,5>))) == f(1,h(2,<3,4,5>));
test bool FrepB7()= frepb(f(1,g(2,{<1,10>,<2,20>}))) == f(1,h(2,{<1,10>,<2,20>}));
test bool FrepB8()= frepb(f(1,g(2,(1:10,2:20)))) == f(1,h(2,(1:10,2:20)));

//	FrepG2H3a

test bool FrepG2H3a1()= frepG2H3a(f(3)) == f(3);
test bool FrepG2H3a2()= frepG2H3a(f(1,2,3)) == f(1,2,3);
test bool FrepG2H3a3()= frepG2H3a(f(1,g(2,3))) == f(1,h(2,3,0));
test bool FrepG2H3a4()= frepG2H3a(f(1,g(2,[3,4,5]))) == f(1,h(2,[3,4,5],0));
test bool FrepG2H3a5()= frepG2H3a(f(1,g(2,{3,4,5}))) == f(1,h(2,{3,4,5},0));
test bool FrepG2H3a6()= frepG2H3a(f(1,g(2,<3,4,5>))) == f(1,h(2,<3,4,5>,0));
test bool FrepG2H3a7()= frepG2H3a(f(1,g(2,{<1,10>,<2,20>}))) == f(1,h(2,{<1,10>,<2,20>},0));
test bool FrepG2H3a8()= frepG2H3a(f(1,g(2,(1:10,2:20)))) == f(1,h(2,(1:10,2:20), 0));
		
//	FrepG2H3b
		
test bool FrepG2H3b1()= frepG2H3b(f(3)) == f(3);
test bool FrepG2H3b2()= frepG2H3b(f(1,2,3)) == f(1,2,3);
test bool FrepG2H3b3()= frepG2H3b(f(1,g(2,3))) == f(1,h(2,3,0));
test bool FrepG2H3b4()= frepG2H3b(f(1,g(2,[3,4,5]))) == f(1,h(2,[3,4,5],0));
test bool FrepG2H3b5()= frepG2H3b(f(1,g(2,{3,4,5}))) == f(1,h(2,{3,4,5},0));
test bool FrepG2H3b6()= frepG2H3b(f(1,g(2,<3,4,5>))) == f(1,h(2,<3,4,5>,0));
test bool FrepG2H3b7()= frepG2H3b(f(1,g(2,{<1,10>,<2,20>}))) == f(1,h(2,{<1,10>,<2,20>},0));
test bool FrepG2H3b8()= frepG2H3b(f(1,g(2,(1:10,2:20)))) == f(1,h(2,(1:10,2:20), 0));

//	Inc

test bool Inc1()= inc(f(3)) == f(4);
test bool Inc2()= inc(f(1,2,3)) == f(2,3,4);
test bool Inc3()= inc(f(1,g(2,3))) == f(2,g(3,4));
test bool Inc4()= inc(f(1,g(2,[3,4,5]))) == f(2,g(3,[4,5,6]));
test bool Inc5()= inc(f(1,g(2,{3,4,5}))) == f(2,g(3,{4,5,6}));
test bool Inc6()= inc(f(1,g(2,<3,4,5>))) == f(2,g(3,<4,5,6>));
test bool Inc7()= inc(f(1,g(2,{<1,10>,<2,20>}))) == f(2,g(3,{<2,11>,<3,21>}));
test bool Inc8()= inc(f(1,g(2,(1:10,2:20)))) == f(2,g(3,(2:11,3:21)));
	
//	IncAndCount

test bool IncAndCount1()= inc_and_count(f(3),10)                       == <1,f(13)>;
test bool IncAndCount2()= inc_and_count(f(1,2,3), 10)                  == <3,f(11,12,13)>;
test bool IncAndCount3()= inc_and_count(f(1,g(2,3)), 10)               == <3, f(11,g(12,13))>;
test bool IncAndCount4()= inc_and_count(f(1,g(2,[3,4,5])), 10)         == <5,f(11,g(12,[13,14,15]))>;
test bool IncAndCount5()= inc_and_count(f(1,g(2,{3,4,5})), 10)         == <5,f(11,g(12,{13,14,15}))>;
test bool IncAndCount6()= inc_and_count(f(1,g(2,<3,4,5>)), 10)         == <5,f(11,g(12,<13,14,15>))>;
test bool IncAndCount7()= inc_and_count(f(1,g(2,{<1,10>,<2,20>})), 10) == <6,f(11,g(12,{<11,20>,<12,30>}))>;
test bool IncAndCount8()= inc_and_count(f(1,g(2,(1:10,2:20))),10)      == <6, f(11,g(12,(11:20,12:30)))>;
	
//	Srepl

test bool srepl1()= srepl(f(3)) == f(3);
test bool srepl2()= srepl(g(1,2)) == h(1,2);
test bool srepl3()= srepl(g(1,f(g(2,3)))) == h(1,f(g(2,3)));
test bool srepl4()= srepl(g(1,f([g(2,3),4,5]))) == h(1,f([g(2,3),4,5]));

//	Order

test bool order1()= order(f1(3)) == [3];
test bool order2()= order(g1([f1(1),f1(2)])) == [1,2];
test bool order3()= order(h1(f1(1),h1(f1(2),f1(3)))) == [1,2,3];
test bool order4()= order(h1(f1(1),g1([h1(f1(2),f1(3)),f1(4),f1(5)]))) == [1,2,3,4,5];

// StringVisit1a

test bool StringVisit1a1()=visit(""){ case /b/: insert "B";} == "";
test bool StringVisit1a2()=visit("a"){ case /b/: insert "B";} == "a";
test bool StringVisit1a3()=visit("b"){ case /b/: insert "B";} == "B";
test bool StringVisit1a4()=visit("abc"){ case /b/: insert "B";} == "aBc";
test bool StringVisit1a5()=visit("abcabc"){ case /b/: insert "B";} == "aBcaBc";
	
//	StringVisit1b

test bool StringVisit1b1()=visit(""){ case /b/ => "B"} == "";
test bool StringVisit1b2()=visit("a"){ case /b/ => "B"} == "a";
test bool StringVisit1b3()=visit("b"){ case /b/ => "B"} == "B";
test bool StringVisit1b4()=visit("abc"){ case /b/ => "B"} == "aBc";
test bool StringVisit1b5()=visit("abcabc"){ case /b/ =>"B"} == "aBcaBc";
	
//	StringVisit2
		
test bool StringVisit2a1()=visit(""){ case /b/: insert "BB";} == "";
test bool StringVisit2a2()=visit("a"){ case /b/: insert "BB";} == "a";
test bool StringVisit2a3()=visit("b"){ case /b/: insert "BB";} == "BB";
test bool StringVisit2a4()=visit("abc"){ case /b/: insert "B";} == "aBc";
test bool StringVisit2a5()=visit("abcabc"){ case /b/: insert "BB";} == "aBBcaBBc";
	
//	StringVisit3
		
test bool StringVisit3a1()=visit(""){ case /^a/: insert "AA"; case /^b/: insert "BB";} == "";
test bool StringVisit3a2()=visit("a"){ case /^a/: insert "AA"; case /^b/: insert "BB";} == "AA";
test bool StringVisit3a3()=visit("b"){ case /^a/: insert "AA"; case /^b/: insert "BB";} == "BB";
test bool StringVisit3a4()=visit("abcabc"){ case /^a/: insert "AA"; case /^b/: insert "BB";} == "AABBcAABBc";
test bool StringVisit3a5()=visit("abcabca"){ case /^a/: insert "AA"; case /^b/: insert "BB";} == "AABBcAABBcAA";
	
// StringVisit4
		
test bool StringVisit4a1()=visit(""){ case "a": insert "AA"; case /b/: insert "BB";} == "";
test bool StringVisit4a2()=visit("a"){ case "a": insert "AA"; case /b/: insert "BB";} == "AA";
test bool StringVisit4a3()=visit("b"){ case "a": insert "AA"; case /b/: insert "BB";} == "BB";
test bool StringVisit4a4()=visit("abcabc"){ case "a": insert "AA"; case /b/: insert "BB";} == "aBBcaBBc";
test bool StringVisit4a5()=visit("abcabca"){ case "a": insert "AA"; case /b/: insert "BB";} == "aBBcaBBcAA";
		


