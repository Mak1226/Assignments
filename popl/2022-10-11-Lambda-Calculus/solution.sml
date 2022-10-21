(* Question-1 *)

datatype expr = Var of Atom.atom
            |   lam of Atom.atom * expr
            |   app of expr * expr

(* open AtomSet *)

(* Question-2 *)

(* `free : expr -> atom set` *)
fun free (Var a) = AtomSet.singleton a
  | free (lam(a, expr)) = AtomSet.subtract (free expr, a)
  | free (app(expr1, expr2)) = AtomSet.union (free expr1, free expr2)

(* `bound : expr -> atom set` *)
fun bound (Var a) = AtomSet.empty
  | bound (lam(a, expr)) = AtomSet.union (bound expr, AtomSet.singleton a)
  | bound (app(expr1, expr2)) = AtomSet.union (bound expr1, bound expr2)

(* Question-3 *)

(* `subst : expr -> atom -> expr -> expr` *)
fun subst (Var y) x expr = if Atom.sameAtom (x, y) then expr else Var y
  | subst (lam (y, expr)) x expr1 = if Atom.sameAtom (x, y) then lam(y, expr) else lam(y, subst expr x expr1)
  | subst (app(expr1, expr2)) x expr3 = app(subst expr1 x expr3, subst expr2 x expr3)


(* Question-4 *)

(* `diag : string -> string -> string` *)
fun diag x y = if String.isPrefix x y then x^"a" else y^"b"

(* `diagA : string -> atom -> string` *)
fun diagA x y = let val ya = Atom.toString(y)
                  in
                    if String.isPrefix x ya then x^"a" else ya^"b"
                  end

(* `fresh : atom set -> atom` *)
fun fresh a = let fun diago (x, y) = diagA y x in Atom.atom ((AtomSet.foldr) diago "" a) end