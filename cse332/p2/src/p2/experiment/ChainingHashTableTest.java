package p2.experiment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Arrays;

import cse332.datastructures.containers.Item;
import cse332.datastructures.trees.BinarySearchTree;
import cse332.interfaces.misc.Dictionary;
import datastructures.dictionaries.AVLTree;
import datastructures.dictionaries.ChainingHashTable;
import datastructures.dictionaries.MoveToFrontList;

public class ChainingHashTableTest {
    
    public static void main(String[] args) throws FileNotFoundException {    
        File outfile = new File("src\\p2\\writeup\\ChainingHashTableTest.txt");
        FileOutputStream out = new FileOutputStream(outfile);
        PrintStream output = new PrintStream(out);   
        // initialize different ChainingHashTables
        ChainingHashTable<Integer, Integer> CHT1 = 
                new ChainingHashTable<Integer, Integer>(() -> new MoveToFrontList<Integer, Integer>());
        ChainingHashTable<Integer, Integer> CHT2 = 
                new ChainingHashTable<Integer, Integer>(() -> new BinarySearchTree<Integer, Integer>());
        ChainingHashTable<Integer, Integer> CHT3 = 
                new ChainingHashTable<Integer, Integer>(() -> new AVLTree<Integer, Integer>());
        System.out.println("Start!");
        System.out.println("Begining ChainingHashTable with MTF");
        output.println("MTF Insert");
        output.println("====================================================");
        output.println("Input Number \t\t\t Time");
        for(int i = 1; i < 1000000 ; i = i*2) {
            testInsert(i, CHT1, output);
            CHT1 = new ChainingHashTable<Integer, Integer>(() -> new MoveToFrontList<Integer, Integer>());
            System.out.print(".");
        }   
        output.println();
        output.println();
        output.println();
        output.println("MTF Iterator");
        output.println("====================================================");
        output.println("Input Number \t\t\t Time");
        for(int i = 1; i < 1000000 ; i = i*2) {
            testIterator(i, CHT1, output);
            CHT1 = new ChainingHashTable<Integer, Integer>(() -> new MoveToFrontList<Integer, Integer>());
            System.out.print(".");
        }   
        System.out.println("finished MTF");
        System.out.println("Begining ChainingHashTable with BST");
        output.println();
        output.println();
        output.println();
        output.println("BSTTree Insert");
        output.println("====================================================");
        output.println("Input Number \t\t\t Time");
        for(int i = 1; i < 1000000 ; i = i*2) {
            testInsert(i, CHT2, output);
            CHT2 = new ChainingHashTable<Integer, Integer>(() -> new BinarySearchTree<Integer, Integer>());
            System.out.print(".");
        }
        output.println();
        output.println();
        output.println();
        output.println("BSTTree Iterator");
        output.println("====================================================");
        output.println("Input Number \t\t\t Time");
        for(int i = 1; i < 1000000 ; i = i*2) {
            testIterator(i, CHT2, output);
            CHT2 = new ChainingHashTable<Integer, Integer>(() -> new BinarySearchTree<Integer, Integer>());
            System.out.print(".");
        }   
        System.out.println("finisehd BST");
        System.out.println("Begining ChainingHashTable with AVL");
        output.println();
        output.println();
        output.println();
        output.println("AVLTree Insert");
        output.println("====================================================");
        output.println("Input Number \t\t\t Time");
        for(int i = 1; i < 1000000 ; i = i*2) {
            testInsert(i, CHT3, output);
            CHT3 = new ChainingHashTable<Integer, Integer>(() -> new AVLTree<Integer, Integer>());
            System.out.print(".");
        }
        output.println();
        output.println();
        output.println();
        output.println("AVLTree Iterator");
        output.println("====================================================");
        output.println("Input Number \t\t\t Time");
        for(int i = 1; i < 1000000 ; i = i*2) {
            testIterator(i, CHT3, output);
            CHT3 = new ChainingHashTable<Integer, Integer>(() -> new AVLTree<Integer, Integer>());
            System.out.print(".");
        }   
        System.out.println("finished AVL");
        System.out.println(outfile.getAbsolutePath());
    }
    
    public static void testInsert(int numNode, Dictionary<Integer, Integer> testDic, PrintStream output) {
        long[] result = new long[5];
        for(int j = 0; j < 5 ;j++) {
            long startTime = System.nanoTime();
            for (int i = 0; i < numNode; i++)
                testDic.insert(i, i);
            long estimatedTime = System.nanoTime() - startTime;
            result[j] = estimatedTime;
        }
        record(numNode, result, output);        
    }
    
    public static void testIterator(int numNode, Dictionary<Integer, Integer> testDic, PrintStream output) {
        long[] result = new long[5];
        for(int j = 0; j < 5 ;j++) {
            long startTime = System.nanoTime();
            for(Item<Integer, Integer> d : testDic)
                d.hashCode();
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