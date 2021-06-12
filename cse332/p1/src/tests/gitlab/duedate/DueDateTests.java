package tests.gitlab.duedate;

import tests.GradingUtility;

public class DueDateTests extends GradingUtility {
    public static void main(String[] args) {
        new DueDateTests();
    }

    protected Class<?>[] getTests() {
        return new Class<?>[] {
                HashTrieMapTests.class
        };
    }
}
