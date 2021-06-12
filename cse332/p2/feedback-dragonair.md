# Project 2 Feedback #
## CSE 332 Summer 2018 ##

**Team:** Dragonair <br>
**Graded By:** Caitlin Schaefer (ceschae@uw.edu), Alon Milchgrub (alonmil@cs.washington.edu)

## Unit Tests ##

**AVLTree**  `(6/6)`
> ✓ Passed *initialize* <br>
> ✓ Passed *insert* <br>
> ✓ Passed *getters* <br>
> ✓ Passed *sorted_duplicate_input* <br>
> ✓ Passed *unsorted_duplicate_input* <br>
> ✓ Passed *structure* <br>

**MoveToFrontList**  `(6/6)`
> ✓ Passed *initialize* <br>
> ✓ Passed *insert* <br>
> ✓ Passed *getters* <br>
> ✓ Passed *sorted_duplicate_input* <br>
> ✓ Passed *unsorted_duplicate_input* <br>
> ✓ Passed *structure* <br>

**HashTable**  `(6/6)`
> ✓ Passed *initialize* <br>
> ✓ Passed *insert* <br>
> ✓ Passed *getters* <br>
> ✓ Passed *sorted_duplicate_input* <br>
> ✓ Passed *unsorted_duplicate_input* <br>
> ✓ Passed *comparator* <br>
> ✓ Passed *negative_hash_codes* <br>

**HeapSort**  `(5/5)`
> ✓ Passed *integer_inorder* <br>
> ✓ Passed *integer_reverseorder* <br>
> ✓ Passed *integer_interleaved* <br>
> ✓ Passed *integer_random* <br>
> ✓ Passed *string* <br>
> ✓ Passed *dataCount_string* <br>

**QuickSort**  `(5/5)`
> ✓ Passed *integer_inorder* <br>
> ✓ Passed *integer_reverseorder* <br>
> ✓ Passed *integer_interleaved* <br>
> ✓ Passed *integer_random* <br>
> ✓ Passed *string* <br>
> ✓ Passed *dataCount_string* <br>

**TopKSort**  `(6/6)`
> ✓ Passed *integer_random_100* <br>
> ✓ Passed *string_20* <br>
> ✓ Passed *dataCount_ngram_counts_inorder* <br>
> ✓ Passed *dataCount_ngram_counts_reverseorder* <br>
> ✓ Passed *dataCount_ngram_counts_interleaved* <br>
> ✓ Passed *dataCount_ngram_counts_random* <br>

**CircularArrayHashCode**  `(0/2)`
> ✓ Passed *generate_hash_codes* <br>
> `✘ Failed` *hash_overlap* <br>
> `✘ Failed` *high_variance* <br>
> `✘ Failed` *with_null_chars* <br>

**CircularArrayComparator**  `(2/2)`
> ✓ Passed *vary_length* <br>
> ✓ Passed *vary_order* <br>

**NGramToNextChoicesMap**  `(2/2)`
> ✓ Passed *poem* <br>
> ✓ Passed *large* <br>

## Miscellaneous ##
`(-2/0)` Your hash table should not have a hard limit on its size. You should have a backup plan for when you run out of primes to resize by

`(-2/0)` Using `toString()` to write `compareTo()` was explicitly forbidden in the spec

`(-0/0)` Timed experiments must be done very carefully to
get accurate results. You need to account for things like
JVM warmup and the possibility of random outliers (e.g. if
your testing machine is running a background process).

`(-0/0)` Your results are difficult to understand. You
should always have provided at least one graph per experiment.

`(-0/0)` `AVLTree` does not need a field to manage the previous value when `insert()` is called

`(-0/0)` `MoveToFrontList.find()` and `insert()` have some redundant sections (`insert()` can rely on `find()` putting the thing at the front of the list)

`(-0/0)` `MoveToFrontList.Iterator.hasNext()` has bad boolean zen

`(-0/0)` `MinFourHeap` you shouldn't have public methods/constructors that aren't in the interface 

`(-0/0)` `ChainingHashTable.Iterator` doesn't follow the Java iterator design patterns. Be sure to come talk to us about proper iterator design
    
## Write-Up ##

**Project Enjoyment**
`(3/3)`<br>
Thank you for your feedback about the debugging process! We've heard this from several groups so we'll be sure to make that process a little more manageable in the future. 

**BST vs. AVLTree**
`(4/4)`<br>
Nice experiment! And absolutely great work doing multiple trials for one input size, this makes your data very reliable. 

**ChainingHashTable**
`(3/3)`<br>
`(-0)` Good experiment, but your chart wasn't linked in the `WriteUp.md` file, the chart still said "Chart Title" on it, and units were missing everywhere. Please be thorough in your presentation next time

**Hash Functions**
`(3/3)`<br>
`(-0)` Why did you choose sorted `CircularArrayFIFOQueue` input?  It would have been better to use randomized data to get a better idea of the average case. Also, need to more thoroughly describe your created hash function in your description next time!

**General Purpose Dictionary**
`(3/3)`<br>
`(-0)` Again, you really needed to back up this experiment with data. I can see your files, so you didn't lose points this time, but EVERY experiment needs data (in a table and/or graph) provided _in_ the writeup to get full credit in the future. 

**Above & Beyond**
`(EX: none)`
