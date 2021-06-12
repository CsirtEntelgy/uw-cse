package traffic;

import java.util.List;

import chess.bots.AlphaBetaSearcher;
import chess.bots.BestMove;
import cse332.chess.interfaces.AbstractSearcher;
import cse332.chess.interfaces.Board;
import cse332.chess.interfaces.Evaluator;
import cse332.chess.interfaces.Move;
import cse332.exceptions.NotYetImplementedException;

public class TrafficSearcher<M extends Move<M>, B extends Board<M, B>> extends AbstractSearcher<M, B> {

	public M getBestMove(B board, int myTime, int opTime) {
		/* Calculate the best move */
		return newAlphaBeta( board,this.evaluator,-evaluator.infty(), evaluator.infty(), ply).move;
	}

	public static <M extends Move<M>, B extends Board<M, B>> BestMove<M> newAlphaBeta(B board, Evaluator<B> evaluator, int alpha, int beta, int depth) {

		// base case

		if(depth == 0) {
			return new BestMove<M>(null, evaluator.eval(board));
		}

		List<M> moves = board.generateMoves();

		// changed is empty
		if(moves.isEmpty()) { 
			return new BestMove<M>(null, evaluator.eval(board));
		}

		
		BestMove<M> bestValue = new BestMove<M>(null, alpha);

		
		// apply move and fine best path
		for (M move : moves) {
			board.applyMove(move);
			int maybeVal = -newAlphaBeta(board, evaluator, -beta, -alpha,  depth - 1).value;
			board.undoMove();


			if (maybeVal > alpha) {
				alpha = maybeVal;
				bestValue.value = alpha;
				bestValue.move = move;
			}
			if (alpha >= beta) {
				return bestValue;
			}
			
			
		}
		return bestValue;

	}
}