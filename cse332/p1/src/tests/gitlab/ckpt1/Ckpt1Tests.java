package tests.gitlab.ckpt1;

import tests.GradingUtility;

public class Ckpt1Tests extends GradingUtility {
    public static void main(String[] args) {
        new Ckpt1Tests();
    }

    protected Class<?>[] getTests() {
        return new Class<?>[] {
                ArrayStackTests.class,
                ListFIFOQueueTests.class,
                CircularArrayFIFOQueueTests.class,
                MinFourHeapTests.class
        };
    }
}
