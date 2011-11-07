/* Alex Mandel and Paul Haverkamp 
 * Copyright 2011
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Code inspiration
 * http://www.anddev.org/code-snippets-for-android-f33/autocompletetextview-cursoradapter-t12430.html
 */

package edu.ucdavis.cros.roadkill;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.format.Time;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Bundle;

public class TakePhoto extends Activity {

	protected boolean _taken;
	protected ImageView _image;
	public ImageButton phButton;
	ExifInterface exif;
    File picFile;
    String strLat;
    Float fltLatC;
    static String strLatC;
    String strLatRef;
    String strLong;
    Float fltLongC;
    static String strLongC;
    String strLongRef;
    static String strDateTime;
    static String strDateTP;
    static String strTimeTP;
    static String strPicOr;
    static String strPicL;
    static String strPicW;
    static double picRatio;
    static String cal_str;
    Boolean tz_daylight;
    long ds_offset;
    long st_offset;
    long datemillis;
    long localmillis;
    static String localDateStr;
    Date localDate;
    Date localDF;
    static String localDFreg;
    TextView longT;
    TextView latT;
    TextView datetimeT;
    
    protected static final String PHOTO_TAKEN	= "photo_taken";
    
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	
//    	_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "CROSPic.jpg";
    	Log.i("TakePhoto", "startCameraActivity()" );
    	File file = new File( Roadkill._path );
    	Uri outputFileUri = Uri.fromFile( file );
    	
    	// Start intent that will open the camera
    	Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE );
    	intent.putExtra( MediaStore.EXTRA_OUTPUT, outputFileUri );
    	
    	// Tells camera to return to App with a result when done
    	startActivityForResult( intent, 0 );
    	
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) 
    {	
    	// Log whether they took a photo or not
    	Log.i( "TakePhoto", "resultCode: " + resultCode );
    	switch( resultCode )
    	{
    		case 0:
    			Log.i( "TakePhoto", "User cancelled" );
    			break;
    			
    		case -1:
			try {
				onPhotoTaken();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    			Intent returnIntent = new Intent();
    			setResult(RESULT_OK,returnIntent);
    			finish();
    			
    	}
    }
    
	public boolean onPhotoTaken() throws ParseException
    {
    	Log.i( "RoadKillPhoto", "onPhotoTaken" );
    	
    	_taken = true;
    	
    	
    	try {
    		File picFile = new File( Roadkill._path );
    		exif = new ExifInterface(picFile.getCanonicalPath());
    		
    	} catch (Exception e) {
    	// TODO: handle exception
    	}
    	
    	strLat = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
    	strLatRef = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
    	strLong = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
    	strLongRef = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
    	strDateTime = exif.getAttribute(ExifInterface.TAG_DATETIME);
    	strPicOr = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
    	strPicL = exif.getAttribute(ExifInterface.TAG_IMAGE_LENGTH);
    	strPicW = exif.getAttribute(ExifInterface.TAG_IMAGE_WIDTH);
    	
    	picRatio = Double.parseDouble(strPicW)/Double.parseDouble(strPicL);
    	
    		
    	SimpleDateFormat df = new SimpleDateFormat ("yyyy:MM:dd HH:mm:ss");
    	Date picDate = df.parse(strDateTime);
    	
    	// Get local Timezone
    	TimeZone local_tz = TimeZone.getDefault();
  	
    	// Check whether picDate is in Daylight savings
    	tz_daylight = local_tz.inDaylightTime(picDate);
    	// Daylight savings local offset
    	ds_offset = local_tz.getDSTSavings();
    	// Standard offset time
    	st_offset = local_tz.getRawOffset();
    	
    	// Convert string time to milliseconds
    	datemillis = picDate.getTime();
    	
    	// Check to see whether it is DST, and make offset accordingly
    	if (tz_daylight) {
    		localmillis = datemillis + st_offset + ds_offset;	
    	} else {
    		localmillis = datemillis + st_offset;
    	}
    	
//    	Date localDate = new Date(localmillis);
    	Date localDate = new Date(datemillis);
    	localDateStr = localDate.toLocaleString();
    	SimpleDateFormat milliDF = new SimpleDateFormat ("MMM d, yyyy hh:mm:ss a");
    	localDF = milliDF.parse(localDateStr);
    	localDFreg = df.format(localDF);
    	
    	
		   
		   
    	//TODO : Comparison appears to fail if the GPS doesn't have a fix
		//Set the Location from the Photo exif it it has a GPS fix
    	if(strLatRef != null){
			if(strLatRef.equals("N")){
				fltLatC = convertToDegree(strLat);
			}
			else{
				fltLatC = 0 - convertToDegree(strLat);
			}
			 
			if(strLongRef.equals("E")){
				fltLongC = convertToDegree(strLong);
			}
			else{
				fltLongC = 0 - convertToDegree(strLong);
			}

			latT = (TextView)findViewById(R.id.latVal);
	    	longT = (TextView)findViewById(R.id.longVal);
	    	strLatC = fltLatC.toString();
	    	strLongC = fltLongC.toString();
	    	

		}
    	
    	return _taken;
    }
	
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