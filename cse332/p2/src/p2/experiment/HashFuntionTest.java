package p2.experiment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Random;

import datastructures.dictionaries.ChainingHashTable;
import datastructures.dictionaries.MoveToFrontList;
import datastructures.worklists.CircularArrayFIFOQueue;
import datastructures.dictionaries.HashTrieMap.HashTrieNode;

public class HashFuntionTest {
    public static void main(String[] args) throws FileNotFoundException {
        final int[] inputSize = { 1, 10, 100, 1000, 10000, 100000, 1000000 };

        File outfile = new File("src\\p2\\writeup\\HashFuntionTest.txt");
        FileOutputStream out = new FileOutputStream(outfile);
        PrintStream output = new PrintStream(out);
        Random rand = new Random();

        ChainingHashTable<CircularArrayFIFOQueue<Integer>, Integer> goodHashCHT = new ChainingHashTable<CircularArrayFIFOQueue<Integer>, Integer>(
                () -> new MoveToFrontList<>());

        ChainingHashTable<CircularArrayFIFOQueue2<Integer>, Integer> badHashCHT = new ChainingHashTable<CircularArrayFIFOQueue2<Integer>, Integer>(
                () -> new MoveToFrontList<>());

        System.out.println("Start!");

        for (int i = 0; i < inputSize.length; i++) {
            System.out.println("Test adding " + inputSize[i]
                    + "  CircularArrayFIFOQueue, CircularArrayFIFOQueue2");

            // create list of CircularArrayFIFOQueue
            CircularArrayFIFOQueue<Integer>[] elements = new CircularArrayFIFOQueue[100];
            CircularArrayFIFOQueue2<Integer>[] elements2 = new CircularArrayFIFOQueue2[100];

            for (int j = 0; j < inputSize[i]; j++) {
                Integer[] Qelems = createRadomData(rand);

                // create single unique CircularArrayFIFOQueue
                CircularArrayFIFOQueue<Integer> element = createFIFOQueue(Qelems);
                CircularArrayFIFOQueue2<Integer> element2 = createFIFOQueue2(Qelems);

                // add to list of CircularArrayFIFOQueue
                elements[j] = element;
                elements2[j] = element2;
            }

            System.out.println("Created List of " + inputSize[i]
                    + " CircularArrayFIFOQueue, CircularArrayFIFOQueue2");

            output.println("goodHashCHT \t\t\t\t badHashCHT");

            System.out.println("inserting..");
            output.println(insertAndTime(goodHashCHT, elements, output) + "\t\t\t\t"
                    + insertAndTime(badHashCHT, elements2, output));
            System.out.println("insert finished...");
            System.out.println();
            System.out.println();
            goodHashCHT = new ChainingHashTable<CircularArrayFIFOQueue<Integer>, Integer>(
                    () -> new MoveToFrontList<>());
            badHashCHT = new ChainingHashTable<CircularArrayFIFOQueue2<Integer>, Integer>(
                    () -> new MoveToFrontList<>());

        }
    }

    public static CircularArrayFIFOQueue<Integer> createFIFOQueue(Integer[] Qelems) {
        CircularArrayFIFOQueue<Integer> temp = new CircularArrayFIFOQueue<Integer>(11);
        for (int i = 0; i < Qelems.length; i++) {
            temp.add(Qelems[i]);
        }
        return temp;
    }

    public static CircularArrayFIFOQueue2<Integer> createFIFOQueue2(
            Integer[] Qelems) {
        CircularArrayFIFOQueue2<Integer> temp = new CircularArrayFIFOQueue2<Integer>(11);
        for (int i = 0; i < Qelems.length; i++) {
            temp.add(Qelems[i]);
        }
        return temp;
    }

    public static Integer[] createRadomData(Random rand) {
        int length = rand.nextInt(11);
        Integer[] result = new Integer[length];
        for (int j = 0; j < length; j++) {
            result[j] = rand.nextInt(100);
        }
        return result;
    }

    public static <E> long insertAndTime(ChainingHashTable<E, Integer> CHT,
            E[] element, PrintStream output) {
        long[] result = new long[5];
        for (int k = 0; k < 5; k++) {
            long startTime = System.nanoTime();
            for (int i = 0; i < element.length ; i++) {
                CHT.insert(element[i], i);
            }
            result[k] = System.nanoTime() - startTime;
        }
        Arrays.sort(result);
        return (result[1] + result[2] + result[3]) / 3;
    }
}
