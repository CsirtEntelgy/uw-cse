package p2.experiment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import cse332.datastructures.trees.BinarySearchTree;
import datastructures.dictionaries.AVLTree;

public class BSTvsAVL {
       
    public static void main(String[] args) throws FileNotFoundException {
        File outfile = new File("src\\p2\\writeup\\BSTvsAVL.txt");
        FileOutputStream out = new FileOutputStream(outfile);
        PrintStream output = new PrintStream(out);   
        // initialize two data structure
        BinarySearchTree<Integer, Integer> BST = new BinarySearchTree<Integer, Integer>();
        AVLTree<Integer, Integer> AVL = new AVLTree<Integer, Integer>();
        System.out.println("Start!");
        System.out.println("Begining AVL");
        output.println("AVLTree");
        output.println("====================================================");
        output.println("Input Number \t\t\t Time");
        for(int i = 1; i < 1000000 ; i = i*2) {
            testInsert(i, AVL, output);
            AVL = new AVLTree<Integer, Integer>();
            System.out.print(".");
        }   
        System.out.println("finished AVL");
        System.out.println("Begining BST");
        output.println();
        output.println();
        output.println();
        output.println("BSTTree");
        output.println("====================================================");
        output.println("Input Number \t\t\t Time");
        for(int i = 1; i < 1000000 ; i = i*2) {
            testInsert(i, BST, output);
            BST = new BinarySearchTree<Integer, Integer>();
            System.out.print(".");
        }
        System.out.println("finished BST");  
        System.out.println(outfile.getAbsolutePath());
    }
    
    public static void testInsert(int numNode, BinarySearchTree<Integer, Integer> testTree, PrintStream output) {
        long[] result = new long[5];
        for(int j = 0; j < 5 ;j++) {
            long startTime = System.nanoTime();
            for (int i = 0; i < numNode; i++)
                testTree.insert(i, i);
            long estimatedTime = System.nanoTime() - startTime;
            result[j] = estimatedTime;
        }
        record(numNode, result, output);        
    }
    
    public static void record(int numNode, long[] result, PrintStream output) {
        Arrays.sort(result);
        output.println(numNode + "\t\t\t\t" + (result[1]+result[2]+result[3])/3);  
    }
}