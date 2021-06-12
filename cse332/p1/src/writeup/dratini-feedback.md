# Project 1 (Zip) Feedback #
## CSE 332 Summer 2018 ##

**Team Dratini:** Josh Cho, Jong Ho Lee <br />
**Graded By:** Caitlin Schaefer (ceschae@uw.edu), Alon Milchgrub (alonmil@cs.washington.edu) <br />

## Unit Tests ##

**ArrayStack**  `(9/9)`
> ✓ Passed *testHasWork* <br>
> ✓ Passed *testHasWorkAfterAdd* <br>
> ✓ Passed *testHasWorkAfterAddRemove* <br>
> ✓ Passed *testPeekHasException* <br>
> ✓ Passed *testNextHasException* <br>
> ✓ Passed *testClear* <br>
> ✓ Passed *checkStructure* <br>
> ✓ Passed *stressTest* <br>
> ✓ Passed *fuzzyStressTest* <br>

**CircularArrayFIFOQueue** ∞ Timeout `(13/14)`
> ✓ Passed *testHasWork* <br>
> ✓ Passed *testHasWorkAfterAdd* <br>
> ✓ Passed *testHasWorkAfterAddRemove* <br>
> ✓ Passed *testPeekHasException* <br>
> ✓ Passed *testNextHasException* <br>
> ✓ Passed *testClear* <br>
> ✓ Passed *checkStructure* <br>
> ✓ Passed *testPeekAndUpdateEmpty* <br>
> ✓ Passed *testPeekAndUpdateOutOfBounds* <br>
> ✓ Passed *testUpdatingOutOfBounds* <br>
> ✓ Passed *testUpdate* <br>
> ✓ Passed *testCycle* <br>
> `∞ Timeout` *stressTest* <br>
> `` *3000ms* <br>
> ✓ Passed *fuzzyStressTest* <br>

**ListFIFOQueue**  `(9/9)`
> ✓ Passed *testHasWork* <br>
> ✓ Passed *testHasWorkAfterAdd* <br>
> ✓ Passed *testHasWorkAfterAddRemove* <br>
> ✓ Passed *testPeekHasException* <br>
> ✓ Passed *testNextHasException* <br>
> ✓ Passed *testClear* <br>
> ✓ Passed *checkStructure* <br>
> ✓ Passed *stressTest* <br>
> ✓ Passed *fuzzyStressTest* <br>

**MinFourHeap**  `(18/18)`
> ✓ Passed *testHasWork* <br>
> ✓ Passed *testHasWorkAfterAdd* <br>
> ✓ Passed *testHasWorkAfterAddRemove* <br>
> ✓ Passed *testPeekHasException* <br>
> ✓ Passed *testNextHasException* <br>
> ✓ Passed *testClear* <br>
> ✓ Passed *checkStructure* <br>
> ✓ Passed *testHeapWith5Items* <br>
> ✓ Passed *testHugeHeap* <br>
> ✓ Passed *testOrderingDoesNotMatter* <br>
> ✓ Passed *testWithCustomComparable* <br>
> ✓ Passed *testStructureInorderInput* <br>
> ✓ Passed *testStructureReverseOrderInput* <br>
> ✓ Passed *testStructureInterleavedInput* <br>
> ✓ Passed *testStructureRandomInput* <br>
> ✓ Passed *testStructureWithDups* <br>
> ✓ Passed *stressTest* <br>
> ✓ Passed *fuzzyStressTest* <br>

**HashTrieMap**  `(19/19)`
> ✓ Passed *testBasic* <br>
> ✓ Passed *testBasicDelete* <br>
> ✓ Passed *testFindPrefixes* <br>
> ✓ Passed *testFindNonexistentDoesNotCrash* <br>
> ✓ Passed *testFindingNullEntriesCausesError* <br>
> ✓ Passed *testInsertReplacesOldValue* <br>
> ✓ Passed *testInsertingNullEntriesCausesError* <br>
> ✓ Passed *testDeleteAll* <br>
> ✓ Passed *testDeleteNothing* <br>
> ✓ Passed *testDeleteAndInsertSingleChars* <br>
> ✓ Passed *testDeleteWorksWhenTrieHasNoBranches* <br>
> ✓ Passed *testDeletingAtRoot* <br>
> ✓ Passed *testDeletingEmptyString* <br>
> ✓ Passed *testDeletingNullEntriesCausesError* <br>
> ✓ Passed *testClear* <br>
> ✓ Passed *checkUnderlyingStructure* <br>
> ✓ Passed *stressTest* <br>
> ✓ Passed *testSize* <br>
> ✓ Passed *testSizeWorksWithMissing* <br>

**SuffixTrie**  `(10/10)`
> ✓ Passed *testExampleFromSpec* <br>
> ✓ Passed *testExampleFromSpecUsingSmallBufferSize* <br>
> ✓ Passed *testExampleFromSpecUsingDifferentBufferSizes* <br>
> ✓ Passed *testAllUnique* <br>
> ✓ Passed *testAllSame* <br>
> ✓ Passed *testParagraph* <br>
> ✓ Passed *testRepetitive* <br>
> ✓ Passed *testDna* <br>
> ✓ Passed *testFakePaper* <br>
> ✓ Passed *testCourseWebsite* <br>

## Miscellaneous ##

`(-10/0)` Static Analysis / Style <br />
    passed Static Analysis <br>

low-priority style corrections:
    - you should use constants whenever you have hard-coded numbers (DEFAULT_CAPACITY, for example) <br>

medium-priority style corrections: 
    - none

high-priority style corrections:
    - (-4) there's some redundancy between HashTrieMap.findPrefix() and HashTrieMap.iterateKey(). always reduce redundancy! <br>
    - (-6) CircularArrayFIFOQueue.insert() has O(n) time, when it should take O(1) time <br>

--------

## Write-Up ##

**Partnership**
`(1/1)`
    glad to hear your partnership worked out! for p2 and p3, take a stab at pair programming to see how much more you learn! <br>

**How was the project?**
`(1/1)`
    it's so nice to hear that you think the writeup is nice :) <br>

### WorkLists, Tries, and Zip ###

**peek(i)**
`(1/2)`
    responses should discuss the two ADTs involved rather than specific implementations. consider a "RandomizedWorkList" in which the elements are output in a random order. what is the i-th element of a random order? <br>

**TrieMap vs. (HashMap and TreeMap)**
`(2/2)`
    great insight! <br>

**Applications of TrieMap**
`(1/3)`
    your pseudocode works for word search solutions, but not boggle solutions (it's a different game) <br>
    pseudocode format should follow what's outlined by the course materials. your pseudocode should have more java, and be less like english paragraphs. <br>
    for HashMap and HashSet, it's an O(1) operation to find the element, comprable to O(d), where d is the length of the key. instead, one of the major advantages of using the trie is that we can stop if a prefix isn't in the dictionary <br>

**Running Zip**
`(1/1)`
    great answer! <br>

**Zip Experiment**
`(5/6)`
    be sure to include compression ratios next time. this is a really critical comparison point for zip algorithms. <br>
    otherwise, great response with regards to different inputs, cause/effect relationships, and overall analysis. keep up this type of in-depth response for p2 and p3! <br> 

### Above and Beyond ###

**Above and Beyond**
`(EX: 0)`
    no attempt at above & beyond
