(* CSE 341, HW2 Provided Code *)

(* main datatype definition we will use throughout the assignment *)
datatype json =
         Num of real (* real is what SML calls floating point numbers *)
       | String of string
       | False
       | True
       | Null
       | Array of json list
       | Object of (string * json) list

(* some examples of values of type json *)
val json_pi    = Num 3.14159
val json_hello = String "hello"
val json_false = False
val json_array = Array [Num 1.0, String "world", Null]
val json_obj   = Object [("foo", json_pi), ("bar", json_array), ("ok", True)]

(* some provided one-liners that use the standard library and/or some features
   we have not learned yet. (Only) the challenge problem will need more
   standard-library functions. *)

(* dedup : string list -> string list -- it removes duplicates *)
fun dedup xs = ListMergeSort.uniqueSort String.compare xs

(* strcmp : string * string -> order compares strings alphabetically
   where datatype order = LESS | EQUAL | GREATER *)
val strcmp = String.compare

(* convert an int to a real *)
val int_to_real = Real.fromInt

(* absolute value of a real *)
val real_abs = Real.abs

(* convert a real to a string *)
val real_to_string = Real.toString

(* return true if a real is negative : real -> bool *)
val real_is_negative = Real.signBit

(* We now load 3 files with police data represented as values of type json.
   Each file binds one variable: small_incident_reports (10 reports),
   medium_incident_reports (100 reports), and large_incident_reports
   (1000 reports) respectively.

   However, the large file is commented out for now because it will take
   about 15 seconds to load, which is too long while you are debugging
   earlier problems.  In string format, we have ~10000 records -- if you
   do the challenge problem, you will be able to read in all 10000 quickly --
   it's the "trick" of giving you large SML values that is slow.
*)

(* Make SML print a little less while we load a bunch of data. *)
       ; (* this semicolon is important -- it ends the previous binding *)
Control.Print.printDepth := 3;
Control.Print.printLength := 3;

use "parsed_small_police.sml";
use "parsed_medium_police.sml";
use "parsed_large_police.sml";

val large_incident_reports_list =
    case large_incident_reports of
        Array js => js
      | _ => raise (Fail "expected large_incident_reports to be an array")

(* Now make SML print more again so that we can see what we're working with. *)
; Control.Print.printDepth := 20;
Control.Print.printLength := 20;

(**** PUT PROBLEMS 1-8 HERE ****)

(*Takes an int i and returns a json list containing i entries*)
fun make_silly_json i =
    let
	fun aux i =
	    case i of
		0 => []
	     |  x => Object [("n", Num(int_to_real x)), ("b", True)]
		     :: aux(x -1)
    in
	Array(aux i)
    end

(*Returns an option version of the second entry of the pair where the
  first entry matches k*)
fun assoc (k, xs) =
    let
	fun aux (x, xs) =
	    case xs of
		[] => NONE
	     |  x'::xs' => case x' of
			       (a, b) => if x = a
					 then SOME b
					 else aux(a, xs')
    in
	aux(k, xs)
    end

(*Takes a json and returns SOME object when a match with given string is found *)
fun dot (j, f) =
    case j of
	Object i => assoc(f, i)
     |  json  => NONE
	
(*Takes a json and returns a list holding all the field names*)
fun one_fileds xs =
    let
	fun aux xs =
	    case xs of
		Object i => (case i of
				 [] => []
			       | x'::xs' => case x' of
						(a, b) => a::aux(Object(xs')))
	      | json => []
    in
	aux xs
    end

(*Checks if a list of strings hold any duplicates*)
fun no_repeats xs =
    length xs = length (dedup xs)

(*checks if no fields of an object is same*)
fun recursive_no_field_repeats xs =
    let
	fun aux x =
	    case x of
		Array i => (let fun aux2 y =
				    case y of
					[] => true
				     |  y'::ys' =>
					((recursive_no_field_repeats y')andalso
					(recursive_no_field_repeats(Array ys')))
			    in aux2 i
			    end)
	     |  json => no_repeats(one_fileds x)
    in
	aux xs
    end			       

(*checks if list is in order and returns a list of the pairs of string and its occurensces*)
fun count_occurrences (qs, ys) =
    let
	fun aux xs =
	    case xs of
		x::y::xs' => (case strcmp(x,y) of
				  GREATER => raise ys
			        | i => aux(y::xs'))
	      | [] => let fun aux2 ys =
				 case ys of
				     [] => []
				   | y'::ys' => let fun aux3 (count, z, zs)=
							case zs of
							    z'::zs' => (if z=z'
								        then aux3(count + 1, z, zs')
								        else (z, count)::(aux2 zs'))
							  | [] => []
						in
						    aux3(1, y', ys')
						end
			 in
			     aux2 qs
			 end
    in
	aux qs
    end

(*returns the string values for fields*)
fun string_values_for_field (xs, ys) =
    case ys of
	[] => []
     |  y'::ys' => case dot(y',xs) of
		       SOME(String i) => i::string_values_for_field(xs,ys')
		    |  r => string_values_for_field(xs,ys')

(* histogram and historgram_for_field are provided, but they use your
   count_occurrences and string_values_for_field, so uncomment them
   after doing earlier problems *)
					       
(* histogram_for_field takes a field name f and a list of objects js and
   returns counts for how often a string is the contents of f in js. *)

exception SortIsBroken

fun histogram (xs : string list) : (string * int) list =
  let
    fun compare_strings (s1 : string, s2 : string) : bool = s1 > s2

    val sorted_xs = ListMergeSort.sort compare_strings xs
    val counts = count_occurrences (sorted_xs,SortIsBroken)

    fun compare_counts ((s1 : string, n1 : int), (s2 : string, n2 : int)) :
	bool = n1 < n2 orelse (n1 = n2 andalso s1 < s2)
  in
    ListMergeSort.sort compare_counts counts
  end

fun histogram_for_field (f,js) =
  histogram (string_values_for_field (f, js))


(**** PUT PROBLEMS 9-11 HERE ****)

(*See if the option has given field and given content*)
fun filter_field_value (xs, ys, zs) =
    case zs of
	[] => []
     |  z'::zs' => case dot(z',xs) of
		       SOME(String i) => (if i = ys
					  then String i::filter_field_value
							     (xs,ys,zs')
					  else filter_field_value(xs,ys,zs'))
		    |  r  => filter_field_value(xs,ys,zs')

(*Sorts data*)
val large_event_clearance_description_histogram =
    histogram_for_field("event_clearance_description",
			large_incident_reports_list)

(*Sorts data*)
val large_hundred_block_location_histogram =
    histogram_for_field("hundred_block_location", large_incident_reports_list)
    
;Control.Print.printDepth := 3;
Control.Print.printLength := 3;

(**** PUT PROBLEMS 12-15 HERE ****)

(*Sorts data*)
val forty_third_and_the_ave_reports =
    filter_field_value("hundred_block_location",
		       "43XX BLOCK OF UNIVERSITY WAY NE",
		       large_incident_reports_list)

(*Sorts data*)
val forty_third_and_the_ave_event_clearance_description_histogram =
    histogram_for_field("event_clearance_description",
			forty_third_and_the_ave_reports)

(*Sorts data*)
val nineteenth_and_forty_fifth_reports =
    filter_field_value("hundred_block_location", "45XX BLOCK OF 19TH AVE NE",
		       large_incident_reports_list)
		       
(*Sorts data*)
val nineteenth_and_forty_fifth_event_clearance_description_histogram =
    histogram_for_field("event_clearance_description",
			nineteenth_and_forty_fifth_reports)

;Control.Print.printDepth := 20;
Control.Print.printLength := 20;

(**** PUT PROBLEMS 16-19 HERE ****)

(*concatnate a list of strings with given concatnater in between*)
fun concat_with (xs,ys) =
    case ys of
	y::[] => y
     |  y'::ys' => y'^xs^concat_with(xs,ys')
     | [] => ""

(*surrounds a string with quotes*)
fun quote_string xs =
    "'"^xs^"'"

(*changes a real to string*)
fun real_to_string_for_json xs =
    if real_is_negative xs then "-"^real_to_string xs else real_to_string xs

(*Parses a json*)
fun json_to_string xs =
    let
	val temp = []
	fun aux xs =
	    case xs of
		Num i => real_to_string_for_json i
	     |  String i => i
	     |  False => "false"
	     |  True => "true"
	     |  Null => "null"
	     |  Array i => (let fun aux2 y =
				    case y of
					[] => []
				     |  y'::ys' => (json_to_string y')::
						   (aux2 ys')
			    in
				"["^(concat_with (", ",aux2 i))^"]"
			    end)
	     |  Object i => (let fun aux3 y =
				     case y of
					 [] => []
				      |  y'::ys' => case y' of
							(a,b) =>
							((quote_string a)^" : "^
							(json_to_string b))::aux3 ys'
			     in
				 "{"^(concat_with (", ",aux3 i))^"}"
			     end)
    in
	aux xs
    end

(* For CHALLENGE PROBLEMS, see hw2challenge.sml *)
