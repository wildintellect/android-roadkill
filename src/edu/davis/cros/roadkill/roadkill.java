package edu.davis.cros.roadkill;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class roadkill extends Activity 
{
	protected Button _button;
	protected ImageView _image;
	protected TextView _field;
	protected String _path;
	protected boolean _taken;

	Spinner sp;
	TextView aT;
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
    TextView longT;
    TextView latT;
    TextView datetimeT;

	
	protected static final String PHOTO_TAKEN	= "photo_taken";
		
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);

        
        ArrayAdapter<String> adapter = new ArrayAdapter<String> (this,android.R.layout.simple_spinner_dropdown_item, dbAdapter.animalList);
        //TODO: linked animal list to static list, need to now make it dynamic from sqlitedb
        sp = (Spinner)findViewById(R.id.animalspinner);
        sp.setAdapter(adapter);
        aT = (TextView) findViewById(R.id.animaltext);
        aT.setFocusable(false);
        aT.setFocusableInTouchMode(false);
        
        
        
        sp.setOnItemSelectedListener(new OnItemSelectedListener() {
        
        	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
        		//int item = sp.getSelectedItemPosition();
        		
    			aT.setFocusableInTouchMode(true);
    			
    			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
    			imm.hideSoftInputFromWindow(aT.getWindowToken(), 0);	
   			
        	}
        	
        
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
       
        _image = ( ImageView ) findViewById( R.id.image );
        _field = ( TextView ) findViewById( R.id.field );
        _button = ( Button ) findViewById( R.id.button );
        _button.setFocusable(true);
        _button.setFocusableInTouchMode(true);


        
        _button.setOnClickListener( new ButtonClickHandler() );
        
        _path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "CROSPic.jpg";
        
        
    }

    public class ButtonClickHandler implements View.OnClickListener 
    {
    	// When the button is clicked, start the activity
    	public void onClick( View view ){
    		Log.i("MakeMachine", "ButtonClickHandler.onClick()" );
    		startCameraActivity();
    	}
    }
    
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
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) 
    {	
    	// Log whether they took a photo or not
    	Log.i( "MakeMachine", "resultCode: " + resultCode );
    	switch( resultCode )
    	{
    		case 0:
    			Log.i( "MakeMachine", "User cancelled" );
    			break;
    			
    		case -1:
    			onPhotoTaken();
    			break;
    	}
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

    @Override
    // This is used so the image does not disappear when phone is rotated
    protected void onRestoreInstanceState( Bundle savedInstanceState){
    	Log.i( "MakeMachine", "onRestoreInstanceState()");
    	if( savedInstanceState.getBoolean( roadkill.PHOTO_TAKEN ) ) {
    		onPhotoTaken();
    	}
    }
    
    @Override
    // This is used so the image does not disappear when phone is rotated
    protected void onSaveInstanceState( Bundle outState ) {
    	outState.putBoolean( roadkill.PHOTO_TAKEN, _taken );
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.view_menu, menu);
        return true;
    }
}