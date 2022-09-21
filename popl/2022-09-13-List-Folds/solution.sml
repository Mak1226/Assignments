(*Question 1*)
fun foldl f x [] = x
| foldl f x (y::ys) = foldl f ( f (y,x) )ys

fun foldr f x [] = x
| foldr f x (y::ys) = f(y , foldr f x ys)


(* Question 2  *)
(* fold = fn : ('a -> 'b -> 'a) -> 'a -> 'b list -> 'a
add = fn : int -> int -> int
sum = fn : int list -> int *)


fun fold _ x [] = x
| fold f x (y::ys) = fold f (f x y) ys

fun add x y = x + y
val sum  = fold add 0 


(* Question 3(a) *)
(* partition : ( 'a -> bool ) -> 'a list = 'a list * 'a list *)

fun partition pre list = let fun sfun ( x , (u,v) ) = if pre x then ( x::u , v ) else ( u, x::v )
in
    foldr sfun ([],[]) list
end;


(* Question 3(b) *)
(* ( 'a -> 'b ) -> 'b list -> 'a list *)

fun map f list = let fun sfun ( a , b ) = f a :: b
in
    foldr sfun [] list
end;


(* Question 3(c) *)
(* 'a list -> 'a list *)

fun reverse list = let fun sfun ( a , b ) = a::b
in
    foldl sfun [] list
end;


(* Question 3(d) *)
(* 0 base indexing *)
(* datatype 'a Find = Found of 'a | Looking for int *)

datatype 'a Find = LookingFor of int
| Found of 'a;

(* 'a list * int -> 'a Find *)

fun nthAux ( list , index ) = let fun sfun ( a , LookingFor(n) ) = if n = 0 then Found(a) else LookingFor(n-1)
| sfun   ( a, Found(n) )   = Found(n)
in
    foldl sfun (LookingFor( index ))  list
end;

(* dataype 'a option = Node | valueIs of 'a *)

datatype 'a option = None | ValueIs of 'a;

(* 'a list * int -> 'a option *)

fun nth ( list, index ) = case nthAux( list, index ) of LookingFor(x) => None
| Found(x) => ValueIs(x);
