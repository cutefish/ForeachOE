package test;
import java.util.Arrays;

public class Correlation {
	public static void main(String args[]) {
		System.out.println("Hello from Correlation class.");
	}
	
	public static float calcCorrelation(float[] array1, float[] array2, int startVal) {	// array1 is 400 values
		float avg1 = mean(array1);														// array2 is entire cont. array
		float avg2 = mean(Arrays.copyOfRange(array2, startVal, startVal+400));
		float val1, val2;
		val1 = numerator(array1, array2, startVal, avg1, avg2);
		val2 = denominator(array1, array2, startVal, avg1, avg2);
		//System.out.println("The correlation of the two arrays is: " + (val1/val2));
		return val1/val2;
	}
	
	public static float numerator(float[] array1, float[] array2, int startVal, float avg1, float avg2) {	// calculates num. of corr. coefficient
		float value = 0f;
		float sum = 0f;
		for (int i = 0; i < array1.length; i++) {
			value = (array1[i] - avg1) * (array2[i+startVal] - avg2);
			sum += value;
		}
		return sum;
	}
	
	public static float denominator(float[] array1, float[] array2, int startVal, float avg1, float avg2) { // calculates dem. of corr. coefficient
		float value1 = 0f;
		float value2 = 0f;
		float sum1 = 0f;
		float sum2 = 0f;
		double placehold = 0;
		float returnval = 0f;
		for (int i = 0; i < array1.length; i++) {
			value1 = (array1[i] - avg1)*(array1[i] - avg1); 
			value2 = (array2[i+startVal] - avg2)*(array2[i+startVal] - avg2);
			sum1 += value1;
			sum2 += value2;
		}
		placehold = sum1*sum2;
		returnval = (float) Math.sqrt(placehold);
		return returnval;
	}
	
	/* public static float difference(float curval, float[] array) {	// obsolete mthd to calc difference
		float diff = 0f;
		float average = mean(array);
		diff = curval - average;
		return diff;		
	}
	*/
	
	public static float mean(float[] array) {
		float result = 0.0f;
		for (int i = 0; i < array.length; i++) {
			result += array[i];
		}
		result = result / array.length;
		return result;
	}
}
