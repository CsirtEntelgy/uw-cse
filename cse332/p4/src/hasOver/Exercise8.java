package hasOver;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class Exercise8 extends RecursiveTask<Boolean> {

	private static int[] arr;
	private static int sequentialCutOff;
	private static int val;
	private static int low;
	private static int high;
	
	public Exercise8(int val, int[] arr, int sequentialCutOff, int low, int high) {
		Exercise8.arr = arr;
		Exercise8.sequentialCutOff = sequentialCutOff;
		Exercise8.val = val;
		Exercise8.low = low;
		Exercise8.high = high;
	}

	@Override
	protected Boolean compute() {
		if(high - low <= sequentialCutOff) {
			for(int i = low; i < high; i++) {
				if(arr[i] > val)
					return true;
			}
			return false;
		} else {
			int nLow = low;
			int mid = low + (high - low)/2;
			int nHigh = high;
			Exercise8 left = new Exercise8(val, arr, sequentialCutOff, nLow, mid);
			Exercise8 right = new Exercise8(val, arr, sequentialCutOff, mid, nHigh);
			left.fork();
			boolean rightAns = right.compute();
			boolean leftAns = left.join();
			return leftAns && rightAns;
		}
	}
	
	public static boolean hasOver(int val, int[] arr, int sequentialCutOff) {
		return ForkJoinPool.commonPool().invoke(new Exercise8(val, arr, sequentialCutOff, 0, arr.length));
	}
}