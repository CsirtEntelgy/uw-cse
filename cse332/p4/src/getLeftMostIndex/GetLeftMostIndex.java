package getLeftMostIndex;

public class GetLeftMostIndex {
    public static int getLeftMostIndex(char[] needle, char[] haystack, int sequentialCutoff) {
    	Exercise10 temp = new Exercise10();
    	return temp.getLeftMostIndex(needle, haystack, sequentialCutoff);
    }

    private static void usage() {
        System.err.println("USAGE: GetLeftMostIndex <needle> <haystack> <sequential cutoff>");
        System.exit(2);
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            usage();
        }

        char[] needle = args[0].toCharArray();
        char[] haystack = args[1].toCharArray();
        try {
            System.out.println(getLeftMostIndex(needle, haystack, Integer.parseInt(args[2])));
        } catch (NumberFormatException e) {
            usage();
        }
    }
}
