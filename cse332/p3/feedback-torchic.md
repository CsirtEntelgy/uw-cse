# Project 3 (Chess) Feedback #
## CSE 332 Winter 2018 ##

**Team:** Torchic <br />
**Graded By:** Caitlin Schaefer (ceschae@uw.edu), Alon Milchgrub (alonmil@cs.washington.edu) <br>

## Unit Tests ##

**Minimax**  `(4/4)`
> ✓ Passed *depth2* <br>
> ✓ Passed *depth3* <br>

**ParallelMinimax**  `(15/15)`
> ✓ Passed *depth2* <br>
> ✓ Passed *depth3* <br>
> ✓ Passed *depth4* <br>

**AlphaBeta**  `(9/9)`
> ✓ Passed *depth2* <br>
> ✓ Passed *depth3* <br>
> ✓ Passed *depth4* <br>

## Miscellaneous ##

`(-2/0)` Some parts of your code are missing comments, be sure to include them everywhere you're describing complex algorithms.

## Write-Up ##

**Project Enjoyment**
`(3/3)`<br>
It was great having you two in class this quarter! I'm really glad you overall enjoyed the project and found a good partnership. Thanks for all the fun, and good luck in your future coding endeavors :) 

### Experiments ###

**Chess Game**
`(6/6)`<br>
Great conclusions!

**Sequential Cut-Offs**
`(3/7)`<br>
It's unclear what your inputs and/or data are. I couldn't find a corresponding graph in your repo, and no table is provided here. 

**Comparing the Algorithms**
`(7/7)`<br>
Great investigation here!

### Traffic ###

**Beating Traffic**
`(3/4)`<br>
You provided a reasonable difference, but this can be fixed with changing how edge weights are represented, not necessarily with algorithm usage. Consider what happens as minimax executes (this is actually what Google does too): Once you get to your next intersection, the "game board" updates with new traffic information, so minimax can update in real time while Dijkstra's can't.