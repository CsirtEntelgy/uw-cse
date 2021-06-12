package chess.bots;

import java.util.List;
import cse332.chess.interfaces.AbstractSearcher;
import cse332.chess.interfaces.Board;
import cse332.chess.interfaces.Evaluator;
import cse332.chess.interfaces.Move;

/**
 * @Description: Same mechanism as the SimpleSearcher but uses Alpha-Beta pruning 
 * (see games.pdf for description) in order to not traverse every possible node.
 */
public class AlphaBetaSearcher<M extends Move<M>, B extends Board<M, B>> extends AbstractSearcher<M, B> {
	
	public AlphaBetaSearcher() {
		super();
	}
	
    public M getBestMove(B board, int myTime, int opTime) {
    	BestMove<M> best = alphaBeta(this.evaluator, board, ply, 
    			- evaluator.infty(), evaluator.infty());
        return best.move;
    }
    
    
    public static <M extends Move<M>, B extends Board<M, B>> BestMove<M> alphaBeta(Evaluator<B> evaluator, B board, int ply, 
    		int alpha, int beta) {
    	/*
    	 Due to some nature in the testing algorithm,
    	 we were unable to use the BestMove<M> object as parameters instead of
    	 integer. (like in SimpleSearcher and ParallelSearcher) 
    	 (throws nullpointer exception)
    	*/
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
        // initial best move (which is alpha)
        BestMove<M> bestMove = new BestMove<M>(null, alpha);
        // test each and every possible move
        for(M m : moves) {
        	board.applyMove(m);
        	// same reason for negation as in SimpleSearcher, but the lower and
        	// upper bounds are also reversed and negated since the next player
        	// is to make the best move for themselves, thus this expresses the fact that 
        	// the choice of choosing the maximum or minimum value switches between players
        	int value = -alphaBeta(evaluator, board, ply - 1, - beta, - alpha).value;
        	board.undoMove();
        	// if the new value is better than before, replace the bestMove
        	// (since both alpha and beta are negated and reversed, we can simply test if max)
        	if(value > alpha) {
        		alpha = value;
        		bestMove = new BestMove<M>(m, alpha);
        	}
        	// if lower bound has exceeded the upper bound, the branches is to be pruned
        	if(alpha >= beta)
        		return bestMove;
        }
        return bestMove;
    }
}