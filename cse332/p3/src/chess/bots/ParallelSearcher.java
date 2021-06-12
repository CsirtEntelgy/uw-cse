package chess.bots;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import cse332.chess.interfaces.AbstractSearcher;
import cse332.chess.interfaces.Board;
import cse332.chess.interfaces.Evaluator;
import cse332.chess.interfaces.Move;

/**
 * @Description: Divides all possible moves via ForkJoin, continues splitting until a certain level
 * (DEPTH) is reached. When DEPTH is reached, division is complete and resulting boards
 * are sent into SimpleSearcher.
 */
public class ParallelSearcher<M extends Move<M>, B extends Board<M, B>> extends AbstractSearcher<M, B>{
	private static final int DIVIDE_CUT_OFF = 3;

	public ParallelSearcher() {
		super();
	}

	public M getBestMove(B board, int myTime, int opTime) {
		List<M> moves = board.generateMoves();
		BestMove<M> best = ForkJoinPool.commonPool().invoke(new ParallelSearcherTask<M,B>(board, 
				evaluator, moves, 0, moves.size(), ply, cutoff, null));
		return best.move;
	}

	@SuppressWarnings("serial")
	private static class ParallelSearcherTask<M extends Move<M>, B extends Board<M, B>> extends RecursiveTask<BestMove<M>>{

		private B board;
		private M move;
		private List<M> moves;
		private Evaluator<B> evaluator;
		private int depth, lo, hi, cutoff;

		private ParallelSearcherTask(B board, Evaluator<B> evaluator, List<M> moves, int lo, 
				int hi, int depth, int cutoff, M move) {
			this.board = board;
			this.moves = moves;
			this.move = move;
			this.depth = depth;
			this.lo = lo;
			this.hi = hi;
			this.evaluator = evaluator;
			this.cutoff = cutoff;
		}

		@Override
		protected BestMove<M> compute() {
			// Case0: BaseCase. If an array of moves is constructed instead of with a move
			// (thus is called for another level of division), divide this board one
			// more depth.
			if (move != null) {
				this.board = board.copy();
				this.board.applyMove(move);
				this.moves = this.board.generateMoves();
				this.hi = this.moves.size();
				this.lo = 0;
				this.depth = depth - 1;
			} 
			// Case1: BaseCase. If depth is reached, thus no more division should be done, send the
			// board into SimpleSearcher.
			if(depth <= cutoff) {
				BestMove<M> a = SimpleSearcher.minimax(this.evaluator, this.board, this.depth);
				return a;
			}
			// Case2: BaseCase. If the board is a leaf, return mate or stale mate 
			// depending on player turns.
			if (moves.isEmpty()) {
				if (board.inCheck())
					return new BestMove<M>(null, -evaluator.mate() - depth);
				else
					return new BestMove<M>(null, -evaluator.stalemate());
			}
			// Case3: If the divided array is bigger than the wanted cutoff, divide the array
			// and send it to ForkJoin. Return the better move out of two divided ParallelSearcherTask.
			if(hi - lo > DIVIDE_CUT_OFF) {
				int mid = lo + (hi - lo)/2;
				ParallelSearcherTask<M,B> left = new ParallelSearcherTask<M,B>(board, evaluator, moves, 
						lo, mid, depth, cutoff, null);
				ParallelSearcherTask<M,B> right = new ParallelSearcherTask<M,B>(board, evaluator, moves,
						mid, hi, depth, cutoff, null);
				left.fork();
				BestMove<M> rightAns = right.compute();
				BestMove<M> leftAns = left.join();
				return SimpleSearcher.getBetterMove(rightAns, leftAns);
			// Case4: If the division is complete but depth is not reached, fork and join every
			// possible board with each move, in the divided array, applied.
			} else {
				List<ParallelSearcherTask<M,B>> sequences = new ArrayList<>();
				BestMove<M> bestMove = new BestMove<M>(evaluator.infty()).negate();
				for (int i = lo; i < hi; i++) {
					ParallelSearcherTask<M,B> temp = new ParallelSearcherTask<M,B>(board, evaluator, 
							moves, 0,-1, depth, cutoff, moves.get(i));
					if (i == hi - 1) {
						bestMove = temp.compute().negate();
						bestMove.move = moves.get(i);
					} else {
						temp.fork();
						sequences.add(temp);
					}
				}
				for (int i = 0; i < sequences.size(); i++) {
					BestMove<M> temp = sequences.get(i).join().negate();
					if (temp.value > bestMove.value) {
						bestMove = temp;
						bestMove.move = moves.get(i + lo);
					}
				}
				return bestMove;
			}
		}
	}
}