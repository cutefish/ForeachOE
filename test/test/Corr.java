package test;

//import java.io.*;

public class Corr {
	float temp_mean = 0;
	float[] xdata;
	float[] ydata;
	int size;
	float xmean, ymean;
	
	float getTemp(float[] data, int size, float temp_mean) {
		temp_mean = 0;
		for (int i = 0; i < size; ++i) {
			temp_mean += data[i];
		}
		temp_mean /= size;	// temp_mean now equals the average of the data entries
		return temp_mean;
	}
	
	float calcCorr(float[] xdata, float[] ydata, int size, int temp_mean) {
		float correlation = 0;
		float var_numerator = 0;
		float var_denominator = 0;
		float xvar = 0;
		float yvar = 0;
		
		System.out.println("Hello from calcCorr");
		
		xmean = getTemp(xdata, size, temp_mean);	// mean of x data values
		ymean = getTemp(ydata, size, temp_mean);	// mean of y data values
		
		for (int i = 0; i < size; ++i) {
			var_numerator += (xdata[i] - xmean) * (ydata[i] - ymean);
		}
		
		for (int i = 0; i < size; ++i) {
			xvar += (xdata[i] - xmean) * (xdata[i] - xmean); // xvar squared
			yvar += (ydata[i] - ymean) * (ydata[i] - ymean); // yvar squared
		}
		
		var_denominator = (float)(Math.sqrt((double)xvar*yvar));
		correlation = var_numerator / var_denominator;
		if (correlation != 0) {
			System.out.println("Correlation calculated!");
		}
		return correlation;
	}
	

}