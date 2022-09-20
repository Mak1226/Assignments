(* Question 1 *)
fun foldl f x [] = x
| foldl f x (y::ys) = foldl f ( f x y) ys

fun foldr f x []  = x
| foldr f x (y::ys) = f(y , foldr f x ys)

(* Question 2 *)
fun add x y = x + y
val sum  = foldl add 0
