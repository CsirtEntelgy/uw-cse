package filterEmpty;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

import cse332.exceptions.NotYetImplementedException;

public class FilterEmpty {
    static ForkJoinPool POOL = new ForkJoinPool();

    public static int[] filterEmpty(String[] arr) {
        int[] bitset = mapToBitSet(arr);
        //System.out.println(java.util.Arrays.toString(bitset));
        int[] bitsum = ParallelPrefixSum.parallelPrefixSum(bitset);
        //System.out.println(java.util.Arrays.toString(bitsum));
        int[] result = mapToOutput(arr, bitsum);
        return result;
    }

    public static int[] mapToBitSet(String[] arr) {
        throw new NotYetImplementedException();
    }

    
    public static int[] mapToOutput(String[] input, int[] bitsum) {
        throw new NotYetImplementedException();
    }

    private static void usage() {
        System.err.println("USAGE: FilterEmpty <String array>");
        System.exit(1);
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            usage();
        }

        String[] arr = args[0].replaceAll("\\s*", "").split(",");
        System.out.println(Arrays.toString(filterEmpty(arr)));
    }
}