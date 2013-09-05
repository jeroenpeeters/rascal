module lang::java::m3::TypeSymbol

extend analysis::m3::TypeSymbol;

data Bound 
  = \super(TypeSymbol bound)
  | \extends(TypeSymbol bound)
  | \unbounded()
  ;
  
data TypeSymbol 
  = \class(loc decl, list[TypeSymbol] typeParameters)
  | \interface(loc decl, list[TypeSymbol] typeParameters)
  | \enum(loc decl)
  | \method(loc decl, list[TypeSymbol] typeParameters, Type returnType, list[TypeSymbol] parameters)
  | \constructor(loc decl, list[TypeSymbol] parameters)
  | \typeParameter(loc decl, TypeSymbol upperbound) 
  | \wildcard(Bound bound)
  | \intersection(list[TypeSymbol] types)
  | \union(list[TypeSymbol] types)
  | \object()
  | \int()
  | \float()
  | \double()
  | \boolean()
  | \char()
  | \byte()
  | \long()
  | \void()
  | \null()
  | \array(list[TypeSymbol] dimensions)
  ;
  
  
  