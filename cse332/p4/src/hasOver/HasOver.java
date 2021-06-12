package hasOver;

public class HasOver {
    public static boolean hasOver(int val, int[] arr, int sequentialCutoff) {
        return Exercise8.hasOver(val, arr, sequentialCutoff);
    }

    private static void usage() {
        System.err.println("USAGE: HasOver <number> <array> <sequential cutoff>");
        System.exit(2);
    }

    public static void main(String[] args) {
        if (args.length != 3)
            usage();
        int val = 0;
        int[] arr = null;
        try {
            val = Integer.parseInt(args[0]); 
            String[] stringArr = args[1].replaceAll("\\s*",  "").split(",");
            arr = new int[stringArr.length];
            for (int i = 0; i < stringArr.length; i++)
                arr[i] = Integer.parseInt(stringArr[i]);
            System.out.println(hasOver(val, arr, Integer.parseInt(args[2])));
        } catch (NumberFormatException e) {
            usage();
        }
    }
}