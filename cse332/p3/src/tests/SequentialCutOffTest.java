package tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import chess.board.ArrayBoard;
import chess.bots.AlphaBetaSearcher;
import chess.bots.ParallelSearcher;
import chess.bots.SimpleSearcher;
import chess.game.SimpleEvaluator;
import cse332.chess.interfaces.Move;

public class SequentialCutOffTest {
	
	private static final String FILE_NAME = "src//tests//SeqTest.txt";
	private static final int PLY = 5;
	
	public static void main(String[] args) throws FileNotFoundException {
		// load fens
        List<String> fenList = loadFens(FILE_NAME);
        // testing for each cutoff (comment out for it to not run)
        // (integer parameter for biggest possible cutoff)
    	findOptimalCutOff(fenList, 5);
	}
	
	private static void findOptimalCutOff(List<String> fenList, int maxCutOff) {
		// testing for each cutoff
    	for(int i = 0; i <= maxCutOff; i++) {
    		// set bot
    		ParallelSearcher searcher = new ParallelSearcher();
    		System.out.println("Testing on ply " + PLY + " with cutoff " + i);
    		System.out.println();
    		// setting bot fields
        	searcher.setDepth(PLY);
    	    searcher.setCutoff(i);
    	    searcher.setEvaluator(new SimpleEvaluator());
    	    // record time
    	    long bigTime = (long) 0;
    	    long startTime = (long) 0;
    	    long endTime = (long) 0;
        	for(int x = 0; x < fenList.size(); x++) {
    	    	startTime = System.nanoTime();
    		   	searcher.getBestMove(ArrayBoard.FACTORY.create().init(fenList.get(x)), 
    		   			0 , 0);
    		   	endTime = System.nanoTime() - startTime;
    	    	bigTime += endTime;
    	    	if(x == 0)
        			System.out.println("For early game, it took about " + bigTime + " to complete.");
        		if(x == fenList.size() / 2)
        			System.out.println("For mid game, it took about " + bigTime + " to complete.");
        		if(x == fenList.size() - 1)
        			System.out.println("For late game, it took about " + bigTime + " to complete.");
    	    }
    		System.out.println("Total time for cutoff " + i + ": " + bigTime);	
        	System.out.println();
	   	}
	}

    private static List<String> loadFens(String fileName) throws FileNotFoundException {
    	List<String> result = new ArrayList<String>();
    	Scanner sc = new Scanner(new File(fileName));
    	while(sc.hasNextLine()) {
    		String s = sc.nextLine();
    		result.add(s);
    	}
    	sc.close();
		return result;
    }
}