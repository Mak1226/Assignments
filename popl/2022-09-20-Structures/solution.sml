signature SORT =
sig
  type t
  val sort : t list -> t list
end

signature ORD_KEY =
sig
  type ord_key
  val compare : ord_key * ord_key -> order
end

functor QSORT (O : ORD_KEY) : SORT  =
struct
  type t = O.ord_key
  fun part (pvt,[]) = ([],[])
  | part (pvt,x::xs) = 
  let
    val (l,r) = part (pvt,xs)
  in
    (fn LESS => (x::l,r)
    |_=> (l,x::r))
    (O.compare(x,pvt))
  end

 fun sort [] = []
 | sort (x::xs) = 
 let
   val (l,r) = part(x,xs)
 in
   sort(l) @ [x] @ sort(r)
 end

end
  structure IntOrd : ORD_KEY = 
  struct
    type ord_key = int
    val compare = Int.compare
  end

structure qsort = QSORT(IntOrd)