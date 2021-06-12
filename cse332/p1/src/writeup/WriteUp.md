# Project 1 (Zip) Write-Up #
--------

#### How Was Your Partnership? ####
-   Did both partners do an equal amount of work?  If not, why not?
    If so, what did each person do? What happened?<pre>
    
	We did. We decided one person should do 1 ~ 4 and other do 5 	~ 6. Both of us helped debugging each others code and discuss what could be done better.
</pre><br>

-----

#### Project Enjoyment ####
-   What was your favorite part of the project?  What was your least
    favorite part of the project?<pre>
	
	Honestly, the WriteUp part is the best part,
	it didn't cost much time and there is no debugging :).
	On a more serious node, our favorite part was figuring out little details that will
	improve our runtime, such as having a pointer that points to the end of the linked list
	in ListFIFOQueue.
	Our least favorite part was indeed debugging. There were a lot of edge cases in HashTrieMap
	implementation that we did not figure at our first attempt.
</pre><br>

-   Did you enjoy the project?  Why or why not?<pre>

	We generally enjoyed the project.
	Although some parts were tricky we believe it gave us good insight of 
	how some of the data types might be implemented under the hood.  
	

</pre><br>

-----

#### WorkLists, Tries, and Zip ####
-   The ADT for a WorkList explicitly forbids access to the middle elements.  However, the FixedSizeFIFOWorkList has a peek(i) method
    which allows you to do exactly that.  Why is this an acceptable addition to the WorkList ADT in this particular case but not in general?  
    In other words, what about fixed size FIFO worklists makes peek(i) make sense? Why does peek(i) NOT make sense in other worklist implementations?<pre>

	Since it is fixed size and first-in-first-out, the search for the i-th element is simply the i-th element of the array implementation. This is not possible when the WorkList is not fixed size because it might return a "0" or Null depending on the extensive size of the array implementation. Also this could be troublesome when the list is not first-in-first-out since the elements have to be traced backwards.
</pre><br>

-   As we've described it, a `TrieMap` seems like a general-purpose replacement for `HashMap` or `TreeMap`.  Why might we still want to use one of these other data structures instead?<pre>
	
	Although TrieMap might seem it could replace HashMap or TreeMap, TrieMap has a restriction that the key
	value has to be iterable. That said it can not replace HashMap and TreeMap that has a key that can not be 
	interated such as int or Dictionary<E>.
</pre><br>

-   One of the applications of Tries is in solving Word Searches.  A "word search" is an n x m rectangle of letters.  The goal is to find all
    of the possible words (horizontal, vertical, diagonal, etc.).  In Boggle, a similar game, any consecutive chain of letters
    are allowed.  Explain (in very high-level pseudo-code) how you might solve this problem with a TrieSet or a TrieMap.  Make sure to detail
    how a similar solution that uses a HashSet/HashMap instead would be different and why using a Trie might make the solution better.<pre>

	We could have a TrieMap that maps alphabets and the meaning of the words in the dictionary. After that,
	we can iterate through all the possible words in the Word Search n x m rectangle and uses those words as
	a key and run find on our TrieMap. If the result of find is not null, then that is one of the answers.
	If we use HashSet/HashMap it will take longer to find the key because it has to iterate over the keyset
	to find the key. So using Trie will give us a better runtime and therefore is a better solution. 	
	
</pre><br>

-   One of the classes in the main package is called Zip.  This class uses your PriorityQueue to do Huffman coding, your FIFOQueue as a buffer,
    your stack to calculate the keyset of a trie (using recursive backtracking), and your SuffixTrie to do LZ77Compression.  Find some text file
    (a free book from https://www.gutenberg.org/ or even the HTML of some website) and use Zip.java to zip it into a zip file.  Then, use a 
    standard zip utility on your machine (Finder on OS X, zip on Linux, WinZip or the like on Windows) to UNZIP your file.  Check that you got back
    the original.  Congratulations!  Your program correctly implements the same compression algorithm you have been using for years!  Discuss in a
    sentence or two how good the compression was and why you think it was good or bad.<pre>

	The compression was working. It worked great on .txt files, but this could be simply due to the nature of 
	unicode. It also worked on some very simple .exe files, but even the most famous zipping programs are 
	known for breaking complex files in the process of compression, our zip method is far to be tested. It is 
	very very very slow!!!

</pre><br>

-   Now that you've played with Zip, we want you to do an **experiment** with Zip.  Notice that there is a constant called `BUFFER_LENGTH` in `Zip.java`.
    Higher values of this constant makes the compression algorithm that Zip uses use more memory and consequently more time.  The "compression ratio"
    of a file is the uncompressed size divided by the compressed size.  Compare time, type of input file, and compression ratio by running
    your code on various inputs.  We would like an in-depth analysis.  You should try at least one "book-like" file, at least one "website-like" file,
    and some other input of your choice.  We expect you to draw meaningful conclusions and possibly have graphs that convince us of your conclusions. 
    Say something about WHY you think you may have gotten the results you did.
    This single question is worth almost as much as the implementation of `ArrayStack`; so, please take it seriously.  If you spend less than 20 minutes
    on this question, there is no conceivable way that you answered this question in the way we were intending.<pre>

	So we tested with three different file. 
	1. Book Like file 684kb
	2. Website Like file 229kb
	3. Number file 682kb (random numbers)

	We tried 9 different inputs, 1 10 50 100 150 200 250 300 350
	and record the time using System.currentTimeMillis().
	The graph can be founded in the following link
	https://www.desmos.com/calculator/z6tjhws1lk
	Red being BookLike, Blue being WebsiteLike, and Green being Numbers
	The result were the following
	    1       10      50      100     150     200      250      300      350
	1   14.205  15.973  23.546  38.260  65.893  174.908  217.947  266.905  323.356
	2   4.080   3.932   5.660   10.441  15.596  59.434   75.421   103.743  122.086
	3   12.283  12.668  27.44   36.325  71.268  88.305   118.403  132.36   146.401
	The graph tells us few things. 
	First, the bigger the file the longer it takes. This is some what obvious because the bigger the file,
	the more it has to compress. But this was very apperent in the graph.
	Second, the input matters. For example, although the size of Number file 
	and the Book Like file was almost the same, as the BUFFER_LENGTH got bigger Number File was able to zip 
	faster. I believe this is because number file had less variety of input that it had less branches in
	tries which allowed it to zip faster.
	Third, the Book Like file has the highest increase rate as the BUFFER_LENGTH increased. I believe this 
	might be because BookLike file is probably the file with most randomized input because Website Like is 
	file of code that probably has some pattern inside it. 
	Forth, The graph is close to liner as the number gets bigger. The part of graph for BUFFER_LENGTH bigger 
	than 200 is close to liner. I am not sure why this is but I would guess after compressing many times,
	the data format gets restricted which makes the time increase somewhat linear.
	
	
</pre><br>

#### Above and Beyond ####
-   Did you do any Above and Beyond?  Describe exactly what you
    implemented.<pre>
**TODO**: Answer this question
</pre><br>