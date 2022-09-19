(* Question 1 *)
fun map f [] = []
| map f (x :: xs) = f x :: map f xs

(* Question 2 *)
datatype 'a tree = Null | node of 'a tree * 'a * 'a tree;

(* Question 3 *)
datatype 'a tree = null
| node of ('a tree * 'a * 'a tree)

fun treemap f null = null
| treemap f (node (l, x, r)) = node (treemap f l, f x, treemap f r)

(* Question 4 *)
datatype 'a tree = niltree
|   node of ('a tree * 'a * 'a tree)

fun inorder niltree = nil
| inorder (node(tl, x, tr)) = inorder tl @ (x :: inorder tr);


fun preorder  niltree = nil
| preorder  (node(tl, x, tr)) = [x] @ preorder tl @ preorder tr ;


fun postorder niltree = nil
| postorder (node(tl, x, tr)) = postorder tl @ postorder tr @ [x];

fun tr y = inorder (node (niltree, y, niltree));

(* Question 5 *)
datatype 'a tree = nil
|   node of ('a tree * 'a * 'a tree)

fun rotCW (node (node(t1,b,t2),a, t3)) = (node(t1, b, node (t2, a, t3)))
| rotCW (node (nil, a, t3)) = node(nil, a, t3)