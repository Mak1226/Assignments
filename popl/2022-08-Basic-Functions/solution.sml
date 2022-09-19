(*Question 1*)
fun curry f x y z = f(x,y,z)

(*Question 2*)
fun fst (a, b) = a
fun snd (a, b) = b

(*Question 3*)
fun length [] = 0
| length (x :: xs) = 1 + length xs

(*Question 4*)
fun reverse (x :: xs) [] = reverse xs [x]
| reverse [] ys = ys
| reverse (x :: xs) ys = reverse xs (x :: ys)

fun rev x = reverse x []
(*Question 5*)
fun fib 1 x y v = v
| fib n x y v = fib (n-1) y v (y + v)

fun fibonacci n = fib n 0 1 1
