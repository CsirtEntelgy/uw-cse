package chess.bots;

import java.util.List;
import cse332.chess.interfaces.AbstractSearcher;
import cse332.chess.interfaces.Board;
import cse332.chess.interfaces.Evaluator;
import cse332.chess.interfaces.Move;

/**
 * @Description: Searches the tree for given number (ply) of levels ahead and 
 * returns the best move for the current player to take.
 */
public class SimpleSearcher<M extends Move<M>, B extends Board<M, B>> extends AbstractSearcher<M, B> {

	public SimpleSearcher() {
		super();
	}

	public M getBestMove(B board, int myTime, int opTime) {
		BestMove<M> best = minimax(this.evaluator, board, ply);
		return best.move;
	}

	static <M extends Move<M>, B extends Board<M, B>> BestMove<M> minimax(Evaluator<B> evaluator, B board, int ply) {
		// ply due
		if(ply == 0)
			return new BestMove<M>(null, evaluator.eval(board));
		// fetch moves
		List<M> moves = board.generateMoves();
		// base case
		if(moves.isEmpty()) {
			if(board.inCheck())
				return new BestMove<M>(null, evaluator.mate() + ply).negate();
			else
				return new BestMove<M>(null, evaluator.stalemate()).negate();
		}
		// initialize empty best value and empty best move
		BestMove<M> bestMove = new BestMove<M>(null, evaluator.infty()).negate();
		// test each and every possible move
		for(M m : moves) {
			board.applyMove(m);
			// has to be negative since the next player is to make the worst possible
			// move in terms of the current player (assume best possible behavior)
			BestMove<M> value = new BestMove<M>(m, -minimax(evaluator, board, ply-1).value);
			board.undoMove();
			bestMove = getBetterMove(bestMove, value);
		}
		return bestMove;
	}

	/**
	 * @Description: A static method (to be used in other codes as well) for
	 * comparing two BestMove<M> objects and returns one with higher value field.
	 */
	static <M extends Move<M>> BestMove<M> getBetterMove(BestMove<M> m1, BestMove<M> m2){
		if(m1.value > m2.value)
			return m1;
		return m2;
	}
}