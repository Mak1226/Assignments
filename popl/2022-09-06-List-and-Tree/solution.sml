(* Question 1 *)
(* map : ('a -> 'b) -> 'a list -> 'b list *)
fun map f [] = []
| map f (x :: xs) = f x :: map f xs;

(* Question 2 *)
(* datatype 'a tree = node of 'a tree * 'a * 'a tree | null *)
datatype 'a tree = null 
| node of 'a tree * 'a * 'a tree;

(* Question 3 *)
(* treemap = fn : ('a -> 'b) -> 'a tree -> 'b tree *)
fun treemap f null = null
| treemap f (node (l, x, r)) = node (treemap f l, f x, treemap f r);

(* Question 4 *)
(* preorder = fn : 'a tree -> 'a list *)
fun preorder  null = nil
| preorder  (node(tleft, x, tright)) = [x] @ preorder tleft @ preorder tright;
(* inorder = fn : 'a tree -> 'a list *)
fun inorder null = nil
| inorder (node(tleft, x, tright)) = inorder tleft @ (x :: inorder tright);
(* postorder = fn : 'a tree -> 'a list *)
fun postorder null = nil
| postorder (node(tleft, x, tright)) = postorder tleft @ postorder tright @ [x];
(* traversal = fn : 'a -> 'a list *)
fun traversal y = inorder (node (null, y, null));

(* Question 5 *)
(* rotate = fn : 'a tree -> 'a tree *)
fun rotate (node (node(t1,b,t2),a, t3)) = (node(t1, b, node (t2, a, t3)))
| rotate noleft = noleft
