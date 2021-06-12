(*Variable for date_to_string*)
val months = ["January", "February", "March", "April", "May", "June", "July",
	      "August", "September", "October", "November","December"]

(*Variable for what_month*)
val whatMonth = [31, 28, 31, 30, 31, 30, 30, 31, 30, 31, 30, 31]

(*Helper function for number_in_months/ dates_in_months*)
fun recurse (x : int*int*int, y : int list)=
    if null y
    then false
    else (if #2(x) = hd(y)
	  then true
	  else recurse(x, tl(y)))		    
		    
fun is_older (x : int*int*int, y : int*int*int)=
    #3 x < #3 y orelse
    (if #3 x = #3 y
     then (#2 x < #2 y orelse
	   (if #2 x = #2 y then (#1 x < #1 y orelse false) else false))
     else false)
	
fun number_in_month (x : (int*int*int)list, y : int)=
    if null x
    then 0
    else (if #2(hd(x)) = y then 1 + number_in_month(tl(x), y)
	  else number_in_month(tl(x), y))
	     
fun number_in_months (x : (int*int*int)list, y : int list)=
    if null x
    then 0
    else (if recurse(hd(x), y)
	  then 1 + number_in_months(tl(x), y)
	  else number_in_months(tl(x), y))

fun dates_in_month (x : (int*int*int)list, y : int)=
    if null x
    then []
    else (if #2(hd(x)) = y
	  then hd(x) :: dates_in_month(tl(x), y)
	  else dates_in_month(tl(x), y))

fun dates_in_months (x : (int*int*int)list, y : int list)=
    if null x
    then []
    else (if recurse(hd(x), y)
	  then hd(x) :: dates_in_months(tl(x), y)
	  else dates_in_months(tl(x), y))

fun get_nth (x : string list, n : int)=
    if n = 1
    then hd(x)
    else get_nth(tl(x), n-1)
	
fun date_to_string (x : int*int*int)=
    (get_nth(months, #2x))^"-"^(Int.toString(#1x))^"-"^(Int.toString(#3x))	

fun number_before_reaching_sum (sum : int, x : int list)=
    if hd(x) >= sum
    then 0
    else 1 + number_before_reaching_sum(sum, (hd(x) + hd(tl(x))) :: tl(tl((x))))

fun what_month (x : int)=
    number_before_reaching_sum(x, whatMonth) + 1

fun month_range (day1 : int*int*int, day2 : int*int*int)=
    if #2 day1 > #2 day2
    then []
    else #2 day1 :: month_range((#1day1, #2day1 + 1, #3day1), day2)

fun oldest (x : (int*int*int)list)=
    if null x
    then NONE
    else (if tl(x) = []
	  then SOME(hd(x))
	  else (if is_older(hd(x), hd(tl(x)))
		then oldest(hd(x) :: tl(tl(x)))
		else oldest(tl(x))))

fun cumulative_sum (x : int list)=
    hd(x) :: cumulative_helper(tl(x), hd(x))

and cumulative_helper (x : int list, y : int)=
    if x = []
    then []
    else (y + hd(x)) :: cumulative_helper(tl(x), (y + hd(x)))
