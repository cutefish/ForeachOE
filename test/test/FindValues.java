package test;

import java.io.File;
import java.util.Arrays;

import ors.pdcl.sac.SacHeader;
import ors.pdcl.sac.SacTimeSeries;

public class FindValues {
	public static void main(String[] args) throws Exception {
		int directoryLength = 0;
		float[] sumArray;
		float MADValue = 0.0f;
		String tempDirectory, contDirectory;
		String tpath, cpath;
		String[] tList, cList;
        System.out.println("FindValues: \n"); 
        String currentDir = System.getProperty("user.dir");
        tempDirectory = currentDir + "\\data\\Template\\10628349\\";
        contDirectory = currentDir + "\\data\\Continuous\\2010083\\";
        System.out.println(tempDirectory + " " + contDirectory);
        final File tempFolder = new File(tempDirectory);
        final File contFolder = new File(contDirectory);
        tList = listFilesInLocalFolder(tempFolder);
        cList = listFilesInLocalFolder(contFolder);
        for (int i = 0; i < tList.length; i++) {
        	if (tList[i] != null || cList[i] != null && tList[i] == cList[i]) {
        		directoryLength++;
        		System.out.println(tList[i] + " " + cList[i]);
        	}
        }
        float[][] shiftedArrays = new float[directoryLength][];
        for (int i = 0; i < directoryLength; i++) {
        	if (tList[i] != null || cList[i] != null && tList[i].equals(cList[i])) {
        		tpath = tempDirectory + tList[i];
                cpath = contDirectory + cList[i];
                shiftedArrays[i] = getShiftedCorrArray(tpath, cpath);
        		System.out.println(tList[i] + " " + cList[i] + "\n");
        	}
        }
        sumArray = sumShiftedCorrArray(shiftedArrays, directoryLength);
        MADValue = getMADValue(sumArray);
        printOutstandingVals(sumArray,MADValue);
	}
	
	public static void printOutstandingVals(float[] sumArray, float MADValue) {
		float tempVal = 0.0f;
		for (int i = 0; i < sumArray.length; i++) {
			tempVal = ((float) i) / 100.0f;
			if (sumArray[i] > 12 * MADValue) {
				System.out.println("Time: " + tempVal + " seconds. Value: " + sumArray[i]);
			}
		}
	}
	
	public static float getMADValue(float[] sumArray) {		// this method calculates the MAD value of the summed correlation arrays
		float[] localArray = sumArray;
		float[] tempMADArray = localArray;
		float arrayMedian = 0.0f;
		float actualMADValue;
		Arrays.sort(localArray);
		arrayMedian = localArray[localArray.length/2];
		for (int i = 0; i < localArray.length; i++) {
			tempMADArray[i] = Math.abs(tempMADArray[i] - arrayMedian);
        	if (i % 200000 == 0) {
            	System.out.println(localArray[i] + " ");
        	}
        }
		Arrays.sort(tempMADArray);
		actualMADValue = tempMADArray[tempMADArray.length/2];
		System.out.println("The MAD Value is: " + actualMADValue);
		return actualMADValue;        
	}
	
	public static float[] sumShiftedCorrArray(float[][] shiftedArrays, int directoryLength) { // this method does a element-wise sum of the 2d array
		float[] sumArray = new float[shiftedArrays[0].length];
		for (int i = 0; i < directoryLength; i++) {
			for (int j = 0; j < shiftedArrays[0].length; j++) {
				sumArray[j] = sumArray[j] + shiftedArrays[i][j];
			}
		}
		return sumArray;
	}
	
	public static float[] getShiftedCorrArray(String templatePath, String continuousPath) throws Exception {
		int[] info1 = getFileInfo(templatePath);
        int[] info2 = getFileInfo(continuousPath);
		float[] tarray = getArray(templatePath);
		float[] carray = getArray(continuousPath);
		float[] corrArray = calcCorrTempCont(tarray, carray, info1);
		corrArray = shiftCorrArray(corrArray, info1[0]);
		return corrArray;
	}
	
	public static String[] listFilesInLocalFolder(final File folder) {	// method to list all the file names in current directory
		String[] list = new String[(int) folder.length()];
		int counter = 0;
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) { 
				listFilesInLocalFolder(fileEntry);
			} else if (fileEntry.isFile()) {
				// System.out.println(fileEntry.getName());
				list[counter] = fileEntry.getName();
				counter++;
			}
		}
		return list;
	}
	
	public static float[] shiftCorrArray(float[] inputArray, int shiftValue) {	// shift the calculated correlation array by t1, then pad zeros
		float[] corrArray = inputArray;
		for (int i = 0; i < corrArray.length; i++) {
        	if (i < (corrArray.length - (shiftValue * 100))) {
        		corrArray[i] = corrArray[i + (shiftValue * 100)]; 	// shift corrArray values by t1 to the left
        	} else {
        		corrArray[i] = 0;									// pad zeros till the end of corrArray
        	}
        	if (i % 100000 == 0) {
        		System.out.println("This is iteration #: " + i + ", the current corrArray value is: " + corrArray[i]);
        	}
        }
		return corrArray;
	}

	public static float[] calcCorrTempCont(float[] array1, float[] array2, int[] tempInfo) {	// calcCorr between template and continuous
		float[] corrValues = new float[array2.length-400];
		float[] tempArray400 = getTemplateVals(array1, tempInfo[0]);
		for (int i = 0; i < (array2.length-400); i++) {
			corrValues[i] = Correlation.calcCorrelation(tempArray400, array2, i);
			if (i % 100000 == 0) {
				System.out.println("This is iteration #: " + i + ", correlation value is: " + corrValues[i]);
			}
		}
		return corrValues;
	}
	
	public static float[] getTemplateVals(float[] array, int startIndex) {	// get the 400 template values used in the correlation calculation
		float[] returnarray = new float[400];
		int counter = 0;
		for (int i = startIndex; i < (startIndex+400); i++) {
			returnarray[counter] = array[i];
			counter++;
		}
		return returnarray;
	}
	
	public static float[] getArray(String tpath) throws Exception {	// get the array from the file using the getY() method
        SacTimeSeries tseries = new SacTimeSeries(tpath);
        float[] y = tseries.getY();
        return y;
	}
	
	public static int[] getFileInfo(String tpath) throws Exception {	// outputs all sorts of file info and an length 4 int array with useful values
		int[] output = new int[4];
		String orientation;
        float t1, t2, tw1, tw2, startIndex, endIndex;
        File tfile = new File(tpath);
        System.out.println(tpath);
        SacTimeSeries tseries = new SacTimeSeries(tpath);
        SacHeader theader = tseries.getHeader();
        t1 = theader.getT1();
        t2 = theader.getT2();
        System.out.println("npts:" + theader.getNpts());
        System.out.println("t1:" + theader.getT1());
        System.out.println("t2:" + theader.getT2());
        System.out.println("zero time: " + theader.getO());
        System.out.println("b:" + theader.getB());
        System.out.println("e:" + theader.getE());
        System.out.println("Delta:" + theader.getDelta() + "\n");
        
        String filename = tfile.getName();
        String[] parts = filename.split("\\.");
        String[] pathtokens = tpath.split("\\\\");
       
        if (pathtokens[6].matches("Template")) {	// if the pathtoken equals template, then print template array information
        	if (parts[2].equals("EH2") || parts[2].equals("EH3")) {
            	orientation = "horizontal";
            	startIndex = ((theader.getT2() - 1 - theader.getB()) / theader.getDelta());
            	endIndex = startIndex + 400.0f;
            	startIndex = Math.round(startIndex);
            	endIndex = Math.round(endIndex);
            	System.out.println("Start index: " + startIndex);
            	System.out.println("End index: " + endIndex);
            	tw1 = t2 - 1.0f;
            	tw2 = t2 + 3.0f;
            } else {								// this means pathtoken is continuous, print information for continuous file
            	orientation = "vertical";
            	startIndex = ((theader.getT1() - 1 - theader.getB()) / theader.getDelta());
            	endIndex = startIndex + 400.0f;
            	startIndex = Math.round(startIndex);
            	endIndex = Math.round(endIndex);
            	System.out.println("Start index: " + startIndex);
            	System.out.println("End index: " + endIndex);
            	tw1 = t1 - 1.0f;
            	tw2 = t1 + 3.0f;
            }
        	output[0] = (int) startIndex;
            output[1] = (int) endIndex;
            output[2] = (int) tw1;
            output[3] = (int) tw2;
        	System.out.println("The template file is of type: " + orientation + ", t1: " + tw1 + ", t2: " + tw2 + ".\n\n");
        } else if (pathtokens[6].matches("Continuous")){
        	if (parts[2].equals("EH2") || parts[2].equals("EH3")) {
            	orientation = "horizontal";
            } else {
            	orientation = "vertical";
            }
        	System.out.println("The continuous file is of type: " + orientation + ".\n\n");
        }
        return output;
	}
}