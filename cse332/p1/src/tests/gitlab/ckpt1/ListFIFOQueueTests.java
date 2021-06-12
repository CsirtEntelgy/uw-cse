package tests.gitlab.ckpt1;

import cse332.interfaces.worklists.WorkList;
import datastructures.worklists.ListFIFOQueue;

public class ListFIFOQueueTests extends WorklistGradingTests {
    public static void main(String[] args) {
        new ListFIFOQueueTests().run();
    }

    public static void init() {
        STUDENT_STR = new ListFIFOQueue<>();
        STUDENT_DOUBLE = new ListFIFOQueue<>();
        STUDENT_INT = new ListFIFOQueue<>();
    }

    @Override
    protected void run() {
        super.run();
        finish();
    }

    public static int checkStructure() {
        WorkList<Integer> queue = new ListFIFOQueue<>();

        // Fill
        for (int i = 0; i < 1000; i++) {
            queue.add(i);
            boolean peekWorks = (queue.peek() == 0);
            boolean hasWork = queue.hasWork();
            boolean sizeIsCorrect = (queue.size() == (i + 1));
            if (!peekWorks || !hasWork || !sizeIsCorrect) {
                return 0;
            }
        }

        // Empty
        for (int i = 0; i < 999; i++) {
            boolean hasWorks = queue.hasWork();
            boolean peekWorks = (queue.peek() == i);
            boolean nextWorks = (queue.next() == i);
            boolean sizeIsCorrect = (queue.size() == 999 - i);
            if (!hasWorks || !peekWorks || !nextWorks || !sizeIsCorrect) {
                return 0;
            }
        }

        return 1;
    }
}
