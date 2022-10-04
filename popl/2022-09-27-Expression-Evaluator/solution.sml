(*  Expression evaluator *)

open Option
open AtomMap
datatype Expr = constant of real
			  | variable   of Atom.atom
			  | Plus  of Expr * Expr
			  | Mul   of Expr * Expr

datatype Stmt = assign of Atom.atom * Expr
			  | Print  of Expr

val env = empty
fun move f a [] 	 = a
| 	move f a (x::xs) = move f (f x a) xs

fun foo (x) = isSome (x)
fun doo (x) = valOf (x)

fun eval env (constant(c)) 		= SOME c
| eval env (variable(v)) 		= find(env,v)
| eval env (Plus(expr1,expr2))  = 	let
										val calc = (foo (eval env expr1) andalso foo (eval env expr2))
									in
										if calc 
										then  SOME (doo (eval env expr2) + doo (eval env expr1))
										else NONE
									end
								
| eval env (Mul(expr1,expr2))  	= 	let
                                       val calc = (foo (eval env expr1) andalso foo (eval env expr2)) 
                                   	in
                                        if calc 
										then SOME (doo (eval env expr2) * doo (eval env expr1)) 
                                        else NONE
                                   	end
								
fun helper a c = a

fun execute (assign ((c,v))) env 	= insert (env,c, doo (eval env v))
|	execute (Print expr1) env    	= helper env (print(Real.toString(doo (eval env expr1))))

fun interpret v = move execute env v
