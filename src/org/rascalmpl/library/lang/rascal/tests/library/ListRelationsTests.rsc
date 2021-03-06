module lang::rascal::tests::library::ListRelationsTests

import List;

// Tests related to the correctness of the dynamic types of list relations produced by the library functions;
// incorrect dynamic types make pattern matching fail;


test bool dynamicTypes1(){ lrel[value, value] lr = [<"1","1">,<2,2>,<3,3>]; return lrel[int, int] _ := slice(lr, 1, 2);}

test bool dynamicTypes2(){ lrel[value, value] lr = [<"1","1">,<2,2>,<3,3>];return lrel[int, int] _ := lr - <"1","1">; }

test bool dynamicTypes3(){ lrel[value a, value b] lr = [<"1","1">,<2,2>,<3,3>]; return lrel[int, int] _ := lr - [<"1","1">] && (lr - [<"1","1">]).a == [2,3] && (lr - [<"1","1">]).b == [2,3]; }

test bool dynamicTypes4(){ lrel[value, value] lr = [<"1","1">,<2,2>,<3,3>]; return lrel[int, int] _ := delete(lr, 0); }

test bool dynamicTypes5(){ lrel[value, value] lr = [<"1","1">,<2,2>,<3,3>]; return lrel[int, int] _ := drop(1, lr); }

test bool dynamicTypes6(){ lrel[value, value] lr = [<1,1>,<2,2>,<"3","3">]; return lrel[int, int] _ := head(lr, 2); }

test bool dynamicTypes7(){ lrel[value, value] lr = [<1,1>,<2,2>,<"3","3">]; return lrel[int, int] _ := prefix(lr); }

test bool dynamicTypes8(){ lrel[value, value] lr = [<"1","1">,<2,2>,<3,3>]; return lrel[int, int] _ := tail(lr); }

test bool dynamicTypes9(){ lrel[value, value] lr = [<1,1>,<2,2>,<"3","3">]; return lrel[int, int] _ := take(2, lr); }	

test bool dynamicTypes10(){ return [tuple[str,str] _, *tuple[int,int] _] := [<"1","1">,<2,2>,<3,3>]; }

test bool dynamicTypes11(){ 
    lrel[value a, value b] lr1 = [<"1","1">,<2,2>,<3,3>]; lrel[value a, value b] lr2 = [<2,2>,<3,3>]; 
    return lrel[int, int] _ := lr1 & lr2 && (lr1 & lr2).a == [2,3] && (lr2 & lr1).b == [2,3]; 
}
       
test bool dynamicTypes12(){ 
    lrel[value, value] lr = [<"1","1">,<2,2>,<3,3>]; 
    return lrel[int, int] _ := delete(lr, 0); 
}