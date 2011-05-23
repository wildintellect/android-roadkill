package edu.ucdavis.cros.roadkill;

public class Tools {
	 public Float convertToDegree(String stringDMS){
    	 Float result = null;
    	 String[] DMS = stringDMS.split(",", 3);

    	 String[] stringD = DMS[0].split("/", 2);
    	    Double D0 = new Double(stringD[0]);
    	    Double D1 = new Double(stringD[1]);
    	    Double FloatD = D0/D1;

    	 String[] stringM = DMS[1].split("/", 2);
    	 Double M0 = new Double(stringM[0]);
    	 Double M1 = new Double(stringM[1]);
    	 Double FloatM = M0/M1;
    	  
    	 String[] stringS = DMS[2].split("/", 2);
    	 Double S0 = new Double(stringS[0]);
    	 Double S1 = new Double(stringS[1]);
    	 Double FloatS = S0/S1;
    	  
    	 result = new Float(FloatD + (FloatM/60) + (FloatS/3600));
    	  
    	 return result;


    	};
}
