package test;

import ors.pdcl.sac.SacHeader;
import ors.pdcl.sac.SacTimeSeries;

public class TestSAC {

    public static void main(String args[]) throws Exception {
        if (args.length != 1){
            System.out.println("TestSAC <path>");
            System.exit(-1);
        }
        String path = args[0];
        System.out.println(path);
        SacTimeSeries series = new SacTimeSeries(path);
        SacHeader header = series.getHeader();
        System.out.println("npts:" + header.getNpts());
        System.out.println("b:" + header.getB());
        System.out.println("e:" + header.getE());
        System.out.println("KINST:" + header.getKinst());
        System.out.println("KSTNM:" + header.getKstnm());
        System.out.println("KCMPNM:" + header.getKcmpnm());
        System.out.println("KNETWK:" + header.getKnetwk());
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
    }
}
