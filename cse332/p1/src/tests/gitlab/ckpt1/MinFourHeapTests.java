package tests.gitlab.ckpt1;

import cse332.interfaces.worklists.PriorityWorkList;
import datastructures.worklists.MinFourHeap;

import java.util.*;

public class MinFourHeapTests extends WorklistGradingTests {
    private static Random RAND;

    public static void main(String[] args) {
        new MinFourHeapTests().run();
    }

    @Override
    protected void run() {
        super.run();
        test("testHeapWith5Items");
        test("testHugeHeap");
        test("testOrderingDoesNotMatter");
        test("testWithCustomComparable");
        finish();
    }

    public static void init() {
        STUDENT_STR = new MinFourHeap<>();
        STUDENT_DOUBLE = new MinFourHeap<>();
        STUDENT_INT = new MinFourHeap<>();
        RAND = new Random(42);
    }

    public static int testHeapWith5Items() {
        PriorityWorkList<String> heap = new MinFourHeap<>();
        String[] tests = { "a", "b", "c", "d", "e" };
        for (int i = 0; i < 5; i++) {
            String str = tests[i] + "a";
            heap.add(str);
        }

        boolean passed = true;
        for (int i = 0; i < 5; i++) {
            String str_heap = heap.next();
            String str = (char) ('a' + i) + "a";
            passed &= str.equals(str_heap);
        }

        return passed ? 1 : 0;
    }

    public static int testOrderingDoesNotMatter() {
        PriorityWorkList<String> ordered = new MinFourHeap<>();
        PriorityWorkList<String> reversed = new MinFourHeap<>();
        PriorityWorkList<String> random = new MinFourHeap<>();

        addAll(ordered, new String[]{"a", "b", "c", "d", "e"});
        addAll(reversed, new String[]{"e", "d", "c", "b", "a"});
        addAll(random, new String[]{"d", "b", "c", "e", "a"});

        if (!isSame("a", ordered.peek(), reversed.peek(), random.peek()) ||
                !isSame("a", ordered.next(), reversed.next(), random.next()) ||
                !isSame("b", ordered.next(), reversed.next(), random.next())) {
            return 0;
        }

        addAll(ordered, new String[] {"a", "a", "b", "c", "z"});
        addAll(reversed, new String[] {"z", "c", "b", "a", "a"});
        addAll(random, new String[] {"c", "z", "a", "b", "a"});

        String[] expected = new String[] {"a", "a", "b", "c", "c", "d", "e", "z"};
        for (String e : expected) {
            if (!isSame(e, ordered.peek(), reversed.peek(), random.peek()) ||
                    !isSame(e, ordered.next(), reversed.next(), random.next())) {
                return 0;
            }
        }

        return 1;
    }

    private static boolean isSame(String... args) {
        String first = args[0];
        for (String arg : args) {
            if (!first.equals(arg)) {
                return false;
            }
        }
        return true;
    }

    public static int testHugeHeap() {
        PriorityWorkList<String> heap = new MinFourHeap<>();
        int n = 10000;

        // Add them
        for (int i = 0; i < n; i++) {
            String str = String.format("%05d", i * 37 % n);
            heap.add(str);
        }

        // Delete them all
        boolean passed = true;
        for (int i = 0; i < n; i++) {
            String s = heap.next();
            passed &= i == Integer.parseInt(s);
        }

        return passed ? 1 : 0;
    }

    public static int testWithCustomComparable() {
        PriorityWorkList<Coordinate> student = new MinFourHeap<>();
        Queue<Coordinate> reference = new PriorityQueue<>();

        for (int i = 0; i < 10000; i++) {
            Coordinate coord = new Coordinate(RAND.nextInt(10000) - 5000, RAND.nextInt(10000) - 5000);
            student.add(coord);
            reference.add(coord);
        }

        if (student.size() != reference.size()) {
            return 0;
        }

        while (!reference.isEmpty()) {
            if (reference.peek() != student.peek() || reference.remove() != student.next()) {
                return 0;
            }
        }

        return 1;
    }

    public static class Coordinate implements Comparable<Coordinate> {
        private int x;
        private int y;

        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }

        // What exactly this comparable method is doing is somewhat arbitrary.
        public int compareTo(Coordinate other) {
            if (this.x != other.x) {
                return this.x - other.x;
            } else {
                return this.y - other.y;
            }
        }
    }

    public static int checkStructure() {
        PriorityWorkList<Integer> heap = new MinFourHeap<>();
        addAll(heap, new Integer[] {10, 10, 15, 1, 17, 16, 100, 101, 102, 103, 105, 106, 107, 108});

        Object[] heapData = getField(heap, "data");
        String heapStr = Arrays.toString(heapData);
        String heapExp = "[1, 10, 15, 10, 17, 16, 100, 101, 102, 103, 105, 106, 107, 108";

        heap.next();
        heap.next();
        heap.next();

        Object[] heapData2 = getField(heap, "data");
        String heapStr2 = Arrays.toString(heapData2);
        String heapExp2 = "[15, 16, 103, 107, 17, 108, 100, 101, 102, 106, 105,";

        return heapStr.contains(heapExp) && heapStr2.contains(heapExp2) ? 1 : 0;
    }
}
