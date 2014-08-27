package test;

import java.io.File;

import ors.pdcl.sac.SacHeader;
import ors.pdcl.sac.SacTimeSeries;

public class TestSAC {

    public static void main(String args[]) throws Exception {
        if (args.length != 1){
            System.out.println("TestSAC <path>");
            System.exit(-1);
        }
        String path = args[0];
        char orientation;
        float t1, t2, tw1, tw2;
        File f = new File(path);
        System.out.println(path);
        SacTimeSeries series = new SacTimeSeries(path);
        SacHeader header = series.getHeader();
        t1 = header.getT1();
        t2 = header.getT2();
        
        System.out.println("npts:" + header.getNpts());
        System.out.println("t1:" + header.getT1());
        System.out.println("t2:" + header.getT2());
        System.out.println("b:" + header.getB());
        System.out.println("e:" + header.getE());
        System.out.println("KINST:" + header.getKinst());
        System.out.println("KSTNM:" + header.getKstnm());
        System.out.println("KCMPNM:" + header.getKcmpnm());
        System.out.println("KNETWK:" + header.getKnetwk());
        System.out.println("Delta:" + header.getDelta());
        System.out.println("data:");
        float[] x = series.getX();
        float[] y = series.getY();
        for (int i = 0; i < 10; ++i) {
            String record = "";
            if (x == null) {
                record += "null";
            }
            else {
                record += Float.toString(x[i]);
            }
            record += " ";
            if (y == null) {
                record += "null";
            }
            else {
                record += Float.toString(y[i]);
            }
            System.out.println(record);
        }
        
        for (int i = 0; i < 10; i++) {
        	System.out.println(y[i]);
        }
        
        String filename = f.getName();
        System.out.println(filename);
        String[] parts = filename.split("\\.");
        
        if (parts[2].equals("EH2") || parts[2].equals("EH3")) {
        	orientation = 'h';
        	tw1 = t2 - 1.0f;
        	tw2 = t2 + 3.0f;
        } else {
        	orientation = 'v';
        	tw1 = t1 - 1.0f;
        	tw2 = t1 + 3.0f;
        }
        
        System.out.println("The template file is of type: " + orientation + ", t1: " + tw1 + ", t2: " + tw2);
    }
}
