package tests.gitlab.ckpt1;

import cse332.interfaces.worklists.WorkList;
import tests.TestsUtility;

import java.util.NoSuchElementException;

public abstract class WorklistGradingTests extends TestsUtility {
    protected static WorkList<String> STUDENT_STR;
    protected static WorkList<Double> STUDENT_DOUBLE;
    protected static WorkList<Integer> STUDENT_INT;

    // DEFAULT LIST OF TESTS

    @Override
    protected void run() {
        SHOW_TESTS = true;
	    PRINT_TESTERR = true;
	    DEBUG = true;

        test("testHasWork");
        test("testHasWorkAfterAdd");
        test("testHasWorkAfterAddRemove");
        test("testPeekHasException");
        test("testNextHasException");
        test("testClear");
        test("checkStructure");
    }

    // DEFAULT TESTS

    public static int testHasWork() {
        return STUDENT_INT.hasWork() ? 0 : 1;
    }

    public static int testHasWorkAfterAdd() {
        STUDENT_INT.add(1);
        return STUDENT_INT.hasWork() ? 1 : 0;
    }

    public static int testHasWorkAfterAddRemove() {
        for (int i = 0; i < 1000; i++) {
            STUDENT_DOUBLE.add(Math.random());
        }
        for (int i = 0; i < 1000; i++) {
            STUDENT_DOUBLE.next();
        }
        return STUDENT_DOUBLE.hasWork() ? 0 : 1;
    }

    public static int testPeekHasException() {
        boolean initiallyThrowsException = doesPeekThrowException(STUDENT_INT);

        addAndRemove(STUDENT_INT, 42, 10);
        boolean throwsExceptionAfterUsage = doesPeekThrowException(STUDENT_INT);

        return (initiallyThrowsException && throwsExceptionAfterUsage) ? 1 : 0;
    }

    public static int testNextHasException() {
        boolean initiallyThrowsException = doesNextThrowException(STUDENT_INT);

        addAndRemove(STUDENT_INT, 42, 10);
        boolean throwsExceptionAfterUsage = doesNextThrowException(STUDENT_INT);

        return (initiallyThrowsException && throwsExceptionAfterUsage) ? 1 : 0;
    }

    public static int testClear() {
        addAll(STUDENT_STR, new String[]{"Beware", "the", "Jabberwock", "my", "son!"});

        if (!STUDENT_STR.hasWork() || (STUDENT_STR.size() != 5)) {
            return 0;
        }

        STUDENT_STR.clear();
        boolean result = !STUDENT_STR.hasWork()
                && STUDENT_STR.size() == 0
                && doesPeekThrowException(STUDENT_STR)
                && doesNextThrowException(STUDENT_STR);

        return result ? 1 : 0;
    }

    // UTILITY METHODS

    protected static <E> void addAll(WorkList<E> worklist, E[] values) {
        for (E value : values) {
            worklist.add(value);
        }
    }

    protected static <E> void addAndRemove(WorkList<E> worklist, E value, int amount) {
        for (int i = 0; i < amount; i++) {
            worklist.add(value);
        }
        for (int i = 0; i < amount; i++) {
            worklist.next();
        }
    }

    protected static <E> boolean doesPeekThrowException(WorkList<E> worklist) {
        try {
            worklist.peek();
        } catch (NoSuchElementException e) {
            return true;
        }
        return false;
    }

    protected static <E> boolean doesNextThrowException(WorkList<E> worklist) {
        try {
            worklist.next();
        } catch (NoSuchElementException e) {
            return true;
        }
        return false;
    }
}
