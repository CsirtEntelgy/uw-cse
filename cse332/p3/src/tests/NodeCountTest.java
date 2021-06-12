package tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import chess.board.ArrayBoard;
import chess.bots.AlphaBetaSearcher;
import chess.bots.ParallelSearcher;
import chess.bots.SimpleSearcher;
import chess.game.SimpleEvaluator;
import cse332.chess.interfaces.Move;

public class NodeCountTest {

	public static void main(String[] args) throws FileNotFoundException {

		String fileName = "src//tests//Boards.txt";


		// load fens
		List<String> fenList = new ArrayList<String>();
		Scanner input = new Scanner(new File(fileName));
		while(input.hasNextLine()) {
			String line = input.nextLine();
			fenList.add(line);
		}
		String[] name = new String[] {"simpleSearcher", "ParallelSearcher", "simplAlphaBetaSearchereSearcher"};

		System.out.println("simpleSearcher ");
		for(int i = 1 ; i <= 5 ; i++) {

			System.out.println("ply : " + i);

			SimpleSearcher s = new SimpleSearcher();

			s.setDepth(i);
			s.setCutoff(i / 2);
			s.setEvaluator(new SimpleEvaluator());


			String fen = fenList.get(30);

			s.getBestMove(ArrayBoard.FACTORY.create().init(fen), 0 , 0);
			//System.out.println("num of node looked at is " + s.nodeCount);
			//System.out.println(


			System.out.println();
		}

		System.out.println("ParallelSearcher ");
		for(int i = 1 ; i <= 5 ; i++) {

			System.out.println("ply : " + i);

			ParallelSearcher ps = new ParallelSearcher();

			ps.setDepth(i);
			ps.setCutoff(i / 2);
			ps.setEvaluator(new SimpleEvaluator());


			String fen = fenList.get(30);

			ps.getBestMove(ArrayBoard.FACTORY.create().init(fen), 0 , 0);
		//	System.out.println("num of node looked at is " + ps.nodeCount);
			//System.out.println(


			System.out.println();
		}

		System.out.println("AlphaBetaSearcher ");
		for(int i = 1 ; i <= 5 ; i++) {

			System.out.println("ply : " + i);

			AlphaBetaSearcher s = new AlphaBetaSearcher();


			s.setDepth(i);
			s.setCutoff(i / 2);
			s.setEvaluator(new SimpleEvaluator());


			String fen = fenList.get(30);

			s.getBestMove(ArrayBoard.FACTORY.create().init(fen), 0 , 0);
		//	System.out.println("num of node looked at is " + s.nodeCount);
			//System.out.println(


			System.out.println();
		}

	}



}

