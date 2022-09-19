(* Question 1 *)
fun map f [] = []
| map f (x :: xs) = f x :: map f xs;

(* Question 2 *)
datatype 'a tree = Null 
| node of 'a tree * 'a * 'a tree;

(* Question 3 *)
datatype 'a tree = null
| node of ('a tree * 'a * 'a tree);

fun treemap f null = null
| treemap f (node (l, x, r)) = node (treemap f l, f x, treemap f r);

(* Question 4 *)
datatype 'a tree = emptytree
|   node of ('a tree * 'a * 'a tree);

fun preorder  emptytree = nil
| preorder  (node(tleft, x, tright)) = [x] @ preorder tleft @ preorder tright;

fun inorder emptytree = nil
| inorder (node(tleft, x, tright)) = inorder tleft @ (x :: inorder tright);

fun postorder emptytree = nil
| postorder (node(tleft, x, tright)) = postorder tleft @ postorder tright @ [x];

fun traversal y = inorder (node (emptytree, y, emptytree));

(* Question 5 *)
datatype 'a tree = nil
|  node of ('a tree * 'a * 'a tree);

fun rotate (node (node(t1,b,t2),a, t3)) = (node(t1, b, node (t2, a, t3)))
| rotate (node (nil, a, t3)) = node(nil, a, t3);