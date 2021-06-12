package p2.experiment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.function.Supplier;
import cse332.datastructures.trees.BinarySearchTree;
import cse332.interfaces.misc.BString;
import cse332.interfaces.misc.Dictionary;
import cse332.types.AlphabeticString;
import cse332.types.NGram;
import datastructures.dictionaries.AVLTree;
import datastructures.dictionaries.ChainingHashTable;
import datastructures.dictionaries.HashTrieMap;
import datastructures.dictionaries.MoveToFrontList;
import p2.clients.NGramTester;
import p2.wordsuggestor.WordSuggestor;

public class DictionaryTest {
    
    public static <A extends Comparable<A>, K extends BString<A>, V> Supplier<Dictionary<K, V>> trieConstructor(Class<K> clz) {
        return () -> new HashTrieMap<A, K, V>(clz);
    }

    public static <K, V> Supplier<Dictionary<K, V>> hashtableConstructor(
            Supplier<Dictionary<K, V>> constructor) {
        return () -> new ChainingHashTable<K, V>(constructor);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static <K, V> Supplier<Dictionary<K, V>> binarySearchTreeConstructor() {
        return () -> new BinarySearchTree();
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static <K, V> Supplier<Dictionary<K, V>> avlTreeConstructor() {
        return () -> new AVLTree();
    }
    
    public static void main(String[] args) throws FileNotFoundException {    
        File outfile = new File("src\\p2\\writeup\\DictionaryTest.txt");
        FileOutputStream out = new FileOutputStream(outfile);
        PrintStream output = new PrintStream(out);   
        // initialize different WordSuggestors
        try {
            long startTime;
            long estimatedTime;
            WordSuggestor ws1 = new WordSuggestor("src\\p2\\writeup\\alice.txt", 2, -1,
                    NGramTester.binarySearchTreeConstructor(),
                    NGramTester.binarySearchTreeConstructor());
            startTime = System.nanoTime();
            ws1.toString();
            estimatedTime = System.nanoTime() - startTime;
            output.println("WordSuggestor with BST took " + estimatedTime + " long.");
            WordSuggestor ws2 = new WordSuggestor("src\\p2\\writeup\\alice.txt", 2, -1,
                    NGramTester.avlTreeConstructor(),
                    NGramTester.avlTreeConstructor());
            startTime = System.nanoTime();
            ws2.toString();
            estimatedTime = System.nanoTime() - startTime;
            output.println("WordSuggestor with AVLTree took " + estimatedTime + " long.");
            WordSuggestor ws3 = new WordSuggestor("src\\p2\\writeup\\alice.txt", 2, -1,
                    NGramTester.hashtableConstructor(() -> new MoveToFrontList<>()),
                    NGramTester.hashtableConstructor(() -> new MoveToFrontList<>()));
            startTime = System.nanoTime();
            ws3.toString();
            estimatedTime = System.nanoTime() - startTime;
            output.println("WordSuggestor with HashTable took " + estimatedTime + " long.");
            WordSuggestor ws4 = new WordSuggestor("src\\p2\\writeup\\alice.txt", 2, -1,
                    NGramTester.trieConstructor(NGram.class),
                    NGramTester.trieConstructor(AlphabeticString.class));
            startTime = System.nanoTime();
            ws4.toString();
            estimatedTime = System.nanoTime() - startTime;
            output.print("WordSuggestor with TrieMap took " + estimatedTime + " long.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("finished Dictionary Tests");
        System.out.println(outfile.getAbsolutePath());
        output.close();
    }
}