package edu.ucdavis.cros.roadkill;

import java.io.File;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class TakePhoto {
	
	protected String _path;
	protected boolean _taken;
	ExifInterface exif;
    File picFile;
    String strLat;
    Float fltLatC;
    String strLatC;
    String strLatRef;
    String strLong;
    Float fltLongC;
    String strLongC;
    String strLongRef;
    String strDateTime;
    
    protected static final String PHOTO_TAKEN	= "photo_taken";
    
	protected void startCameraActivity()
    {
    	Log.i("MakeMachine", "startCameraActivity()" );
    	File file = new File( _path );
    	Uri outputFileUri = Uri.fromFile( file );
    	
    	// Start intent that will open the camera
    	Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE );
    	intent.putExtra( MediaStore.EXTRA_OUTPUT, outputFileUri );
    	
    	// Tells camera to return to App with a result when done
    	startActivityForResult( intent, 0 );
    }


	protected void onPhotoTaken()
    {
    	Log.i( "MakeMachine", "onPhotoTaken" );
    	
    	_taken = true;
    	
    	// Downsample the image size (inSampleSize > 1) for viewing in App
    	BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
    	
    	Bitmap bitmap = BitmapFactory.decodeFile( _path, options );
    	
    	_image.setImageBitmap(bitmap);
    	

    	
    	/// I don't know where this goes yet!!!
    	try {
    		File picFile = new File( _path );
    		exif = new ExifInterface(picFile.getCanonicalPath());
    		
    	} catch (Exception e) {
    	// TODO: handle exception
    	}
    	
    	strLat = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
    	strLatRef = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
    	strLong = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
    	strLongRef = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
    	strDateTime = exif.getAttribute(ExifInterface.TAG_DATETIME);
    	
 
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
    	datetimeT = (TextView)findViewById(R.id.dateVal);
    	
    	fltLatC = fltLatC*1000000;
    	fltLongC = fltLongC*1000000;
    	
    	strLatC = fltLatC.toString();
    	strLongC = fltLongC.toString();
    	
    	latT.setText(strLatC);
    	longT.setText(strLongC);
    	datetimeT.setText(strDateTime);
    	
    	_field.setVisibility( View.GONE );

    	
    }

}