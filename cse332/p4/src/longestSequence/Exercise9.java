package longestSequence;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class Exercise9 extends RecursiveTask<Integer> {
	
	private static int[] arr;
	private static int seqCutoff;
	private static int val;
	private static int low;
	private static int high;

	public Exercise9(int val, int[] arr, int seqCutoff, int low, int high) {
		Exercise9.arr = arr;
		Exercise9.seqCutoff = seqCutoff;
		Exercise9.val = val;
		Exercise9.low = low;
		Exercise9.high = high;
	}
	
	@Override
	protected Integer compute() {
		if(high - low <= seqCutoff) {
			int counter = 0;
			int previousCounter = 0;
			int previousElement = arr[low];
			for(int i = low; i < high; i++) {
				if(arr[i] == val && arr[i] == previousElement)
					counter++;
				else if(arr[i] == val)
					counter = 1;
				else {
					previousCounter = getMax(previousCounter, counter);
					counter = 0;
				}
				previousElement = arr[i];
			}
			previousCounter = getMax(counter, previousCounter);
			return previousCounter;
		} else {
			int nLow = low;
			int mid = low + (high - low)/2;
			int nHigh = high;
			if(arr[mid - 1] == arr[mid]) {
				Exercise9 real = new Exercise9(val, arr, arr.length + 1, low, high);
				return real.compute();
			}
			else {
				Exercise9 left = new Exercise9(val, arr, seqCutoff, nLow, mid);
				Exercise9 right = new Exercise9(val, arr, seqCutoff, mid, nHigh);
				left.fork();
				int rightAns = right.compute();
				int leftAns = left.join();
				return getMax(rightAns, leftAns);
			}
		}
	}
	
	private int getMax(int a, int b) {
		if(a > b)
			return a;
		return b;
	}
	
	public static int getLongestSequence(int val, int[] arr, int seqCutoff) {
		return ForkJoinPool.commonPool().invoke(new Exercise9(val, arr, seqCutoff, 0, arr.length));
	}
}