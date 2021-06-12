package tests.gitlab.ckpt1;

import cse332.interfaces.worklists.WorkList;
import datastructures.worklists.ArrayStack;

public class ArrayStackTests extends WorklistGradingTests {

    public static void main(String[] args) {
        new ArrayStackTests().run();
    }

    public static void init() {
        STUDENT_STR = new ArrayStack<>();
        STUDENT_DOUBLE = new ArrayStack<>();
        STUDENT_INT = new ArrayStack<>();
    }

    @Override
    protected void run() {
        super.run();
        finish();
    }

    public static int checkStructure() {
        WorkList<Integer> stack = new ArrayStack<>();

        // Fill
        for (int i = 0; i < 1000; i++) {
            stack.add(i);
            boolean peekWorks = (stack.peek() == i);
            boolean hasWork = stack.hasWork();
            boolean sizeIsCorrect = (stack.size() == (i + 1));
            if (!peekWorks || !hasWork || !sizeIsCorrect) {
                return 0;
            }
        }

        // Empty
        for (int i = 999; i >= 0; i--) {
            boolean hasWorks = stack.hasWork();
            boolean peekWorks = (stack.peek() == i);
            boolean nextWorks = (stack.next() == i);
            boolean sizeIsCorrect = (stack.size() == i);
            if (!hasWorks || !peekWorks || !nextWorks || !sizeIsCorrect) {
                return 0;
            }
        }

        return 1;
    }
}
