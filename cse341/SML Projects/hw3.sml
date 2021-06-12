(* Dan Grossman, CSE341, HW3 Provided Code *)

exception NoAnswer

datatype pattern = WildcardP
                 | VariableP of string
                 | UnitP
                 | ConstantP of int
                 | ConstructorP of string * pattern
                 | TupleP of pattern list

datatype valu = Constant of int
              | Unit
              | Constructor of string * valu
              | Tuple of valu list

fun g f1 f2 p =
    let 
        val r = g f1 f2 
    in
        case p of
            WildcardP         => f1 ()
          | VariableP x       => f2 x
          | ConstructorP(_,p) => r p
          | TupleP ps         => List.foldl (fn (p,i) => (r p) + i) 0 ps
          | _                 => 0
    end

(**** for the challenge problem only ****)

datatype typ = AnythingT
             | UnitT
             | IntT
             | TupleT of typ list
             | DatatypeT of string

(**** you can put all your code here ****)

(*Takes string list and returns a list with strings that start with lower case*)
fun only_lowercase x =
    List.filter(fn y => Char.isLower(String.sub(y, 0))) x

(*Takes string list and returns the longest string closest to front in list.*)
fun longest_string1 x =
    List.foldl(fn(x,acc) => if String.size x > String.size acc
			    then x else acc) "" x
	      
(*Same as longest_string1 except returns one that is closes to end.*)
fun longest_string2 x =
    List.foldl(fn(x,acc) => if String.size x >= String.size acc
			    then x else acc) "" x

(*Takes a function x that is to be applied as boolean in comparing strings.*)
fun longest_string_helper x =
    List.foldl(fn(z,acc) =>
		  if x(String.size z, String.size acc)
		  then z else acc) ""
	      
(*Same as longest_string1, but with val bindings.*)
val longest_string3 = fn x =>
			 longest_string_helper(fn(a,b) => a>b) x
					      
(*Same as longest_string2, but with val bindings*)
val longest_string4 = fn x =>
			 longest_string_helper(fn(a,b) => a>=b) x

(*Takes a string list and returns the longest string beginning with lowercase.*)
val longest_lowercase =
    fn x => (longest_string1 o only_lowercase) x

(*Takes a string and removes all "x" and "X" and capitalizes all characters.*)
val caps_no_X_string =
    fn x => String.map Char.toUpper
		       (String.implode(List.filter
					   (fn z => Char.toUpper z <> #"X")
					       (String.explode x)))

(*Returns the object in list y that first corresponds as true 
  with given function x. Throws exception when no y corresponds to true*)
fun first_answer x y =
    case y of
	[] => raise NoAnswer
     |  y'::ys' => case x y' of
		       SOME v => v
		    |  NONE => first_answer x ys'

(*Applies x to all elements of list y. Returns NONE if any x(y) is NONE.
  Returns SOME list of SOME x(y).*)
fun all_answers x y =
    let
	fun aux (orgList, acc) =
	    case orgList of 
		[] => SOME acc
	     |  y'::ys' => case x y' of
			       SOME v => aux(ys', acc @ v)
			    |  NONE => NONE
    in
	aux(y, [])
    end

(* 9.(a) Returns some int for cases of given valu (depending on given function)
         Takes some pattern as paramater. If not pattern, returns 0.*)

(*Returns the number of WildcardP in given pattern.*)
fun count_wildcards x =
    g (fn y => if y = () then 1 else 0) (fn z => 0) x

(*Returns the sum of the number of WildcardP and the length of strings.*)
fun count_wild_and_variable_lengths x =
    g (fn y => if y = () then 1 else 0) (fn z => String.size z) x

(*Returns the number of times given string x appears as variable in pattern y.*)
fun count_a_var (x, y) =
    g (fn y => 0) (fn z => if x = z then 1 else 0) y

(*Returns true only if all variables in pattern x is unique from each other.*)
fun check_pat x =
    let
	fun aux1 x =
	    case x of
		VariableP p => [p]
	      | ConstructorP(_,p) => aux1 p
	      | TupleP p => (case p of
				 [] => []
			      |  p'::ps' => (aux1 p') @ aux1(TupleP ps'))
	      | _ => []
	fun aux2 x =
	    case x of
	        [] => true
	     |  x'::[] => true
	     |  x'::xs' => case List.exists (fn i => i = x') xs' of
			       true => false
			     | false => aux2 xs'
    in
	(aux2 o aux1) x
    end		     

(*Matches a valu and a pattern, if valu and pattern matches, returns the
  pair of string and valu. Sorts these into SOME list. Returns NONE if any
  pattern and valu doesn't match.*)
fun match (x, y) =
    case y of
	WildcardP => SOME[]
     |  VariableP p => SOME[(p,x)]
     |  UnitP => (case x of
		      Unit => SOME[]
		    | _ => NONE)
     |  ConstantP p => (case x of
			    Constant x' => if x' = p then SOME[] else NONE
			  | _  => NONE)
     |  ConstructorP (s1,p) => (case x of
				    Constructor(s2,v) => (if s1 = s2
							  then match(v,p)
							  else NONE)
				  | _ => NONE) 
     |  TupleP p => (case x of
			 Tuple vs => all_answers (fn z => match z)
						 (ListPair.zip (vs, p))
		       | _ => NONE)

(*Returns the first pattern in list y that matches valu x in the form stated
  above in match.*)
fun first_match (x, y) =
    SOME(first_answer (fn z => match (x, z)) y)
    handle NoAnswer => NONE
