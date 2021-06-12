package getLeftMostIndex;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class Exercise10 extends RecursiveTask<Integer> {

	private char[] needle;
	private char[] haystack;
	private int sequentialCutOff;
	private int low;
	private int high;
	private int contStart = 0;
	private int contCount = 0;
	
	public Exercise10() {
		
	}
	
	public Exercise10(char[] needle, char[] haystack, int sequentialCutOff, int low, int high) {
		this.needle = needle;
		this.haystack = haystack;
		this.sequentialCutOff = sequentialCutOff;
		this.low = low;
		this.high = high;
	}
	
	private boolean isCont(int count, int begin) {
		for(int i = count; i < needle.length; i++) {
			if(needle[i] != haystack[begin])
				return false;
			begin++;
		}
		return true;
	}

	@Override
	protected Integer compute() {
		int[] found = new int[high - low];
		if(high - low <= sequentialCutOff) {
			int curr = 0;
			for(int i = low; i < high; i++) {
				if(curr == needle.length)
					return found[0];
				if(haystack[i] == needle[curr]) {
					found[curr] = i;
					curr++;
				}
				else
					curr = 0;
				if(found[found.length - 1] == high - 1) {
					this.contStart = found[0];
					this.contCount = high - found[0];
				}
			}
			return -1;
		} else {
			int mid = low + (high - low)/2;
			Exercise10 left = new Exercise10(needle, haystack, sequentialCutOff, low, mid);
			Exercise10 right = new Exercise10(needle, haystack, sequentialCutOff, mid, high);
			left.fork();
			int rightAns = right.compute();
			int leftAns = left.join();
			if(left.contCount != 0) {
				if(left.isCont(left.contCount, left.contStart + left.contCount))
					return left.contStart;
			}
			if(right.contCount != 0) {
				if(right.isCont(right.contCount, right.contStart + right.contCount))
					return right.contStart;
			}
			if((rightAns == -1 && leftAns != -1) ||
					(leftAns == -1 && rightAns != -1))
				return Math.max(rightAns, leftAns);
			else
				return Math.min(leftAns, rightAns);
		}
	}
	
	public int getLeftMostIndex(char[] needle, char[] haystack, int sequentialCutOff) {
		return ForkJoinPool.commonPool().invoke(new Exercise10(needle, haystack, 
				sequentialCutOff, 0, needle.length));
	}
}