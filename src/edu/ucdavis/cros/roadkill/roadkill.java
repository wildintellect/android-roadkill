package edu.ucdavis.cros.roadkill;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;


public class roadkill extends Activity {
	//Declare Variables
	public ImageButton photoButton;
	public static String _path;
//	private String strLatLong;
//	private Date d;
//	private SimpleDateFormat dateParser;
//	private SimpleDateFormat dateConverter;
	private Button locationButton;
	private Button dateButton;
	private AutoCompleteTextView Species;
	private Button saveButton;
	public static final int PHOTO_BMP = 1;
	//Declare Database
	//private dbAdapter mDbHelper;
	private DataBaseHelper myDbHelper;

    LocationManager lm = null;
    LocationListener ll = null;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alt);

        //mDbHelper = new dbAdapter(this);
        //mDbHelper.open();
        
        //DataBaseHelper myDbHelper = new DataBaseHelper();
        myDbHelper = new DataBaseHelper(this);
 
        try { myDbHelper.createDataBase();
        } catch (IOException ioe) {
 		throw new Error("Unable to create database"); 
        }

        
        this.photoButton = (ImageButton)findViewById(R.id.photoButton);
        this.locationButton = (Button)findViewById(R.id.locationButton);
        this.dateButton = (Button)findViewById(R.id.dateButton);
        splist();
        
        //Turn on GPS at application start/resume for better/faster fix
        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
        	Intent gpsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        	startActivity(gpsIntent);
        }
        
        this.saveButton = (Button)findViewById(R.id.saveButton);
        
        //Set up listeners for each button
        this.photoButton.setOnClickListener(new OnClickListener(){
//        	@Override
        	public void onClick(View v) {
        		//Call the code to take the picture
        		Log.i("RoadKill", "photoButton.onClick()" );
        		
        		// set path for photo to be saved in db
        		_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "CROSPic.jpg";
        		
        		//Start TakePhoto Activity
        		Intent photoIntent = new Intent(roadkill.this,TakePhoto.class);
        		startActivityForResult(photoIntent,PHOTO_BMP);
        		
        	}
        });
    	this.locationButton.setOnClickListener(new OnClickListener(){
//        	@Override
        	public void onClick(View v) {
        		Log.i("RoadKill", "locationButton.onClick()" );
        		//open a selection window, 
        		//ask the user if they want the GPS fix
        		//or adjust manually on a map
        	}
    	});
        this.dateButton.setOnClickListener(new OnClickListener(){
//        	@Override
        	public void onClick(View v) {
        		//open a popup with a date/time selector widget
        		Log.i("RoadKill", "dateButton.onClick()" );
        	}
    	});
        this.Species.setOnClickListener(new OnClickListener(){
//        	@Override
        	public void onClick(View v) {
        		Log.i("RoadKill", "Species.onClick()" );
        		//clear the Species label once the start typing
        		//auto match based on the pre-seeded data
        		//if (Species.getText() == "Species"){
        		//		Species.setText("");
        		
        	}
    	});
    
        this.saveButton.setOnClickListener(new OnClickListener(){
//        	@Override
        	public void onClick(View v) {
        		//save data to record, if existing record update information
        		Log.i("RoadKill", "saveButton.onClick()" );
        		
        	}
    	});
    
    }
    
    private void splist() {
    	//Set the species list from a database query
    	//Cursor sppCursor = myDbHelper.spplist();
        //startManagingCursor(sppCursor);
        //String[] from = new String[]{"list"};
        //int[] to = new int[]{R.id.speciesTextView};
        //SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_dropdown_item_1line, sppCursor, from, to);
        
    	//Method uses a straight string array
    	ArrayAdapter<String> adapter = new ArrayAdapter<String> (this,android.R.layout.simple_dropdown_item_1line, dbAdapter.animalList);
        
        this.Species = (AutoCompleteTextView) findViewById(R.id.speciesTextView);
        Species.setAdapter(adapter);
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	//Triggered from the device menu button
    	//Allows user to reach other views and configure the application
    	// TODO : implement opening views based on button picked, with onClick property in menu.xml
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.view_menu, menu);
        return true;
    }   
        


	   @Override
	   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		   super.onActivityResult(requestCode, resultCode, extras);
		   //return the photo to the button as a thumbnail

		   if (requestCode == PHOTO_BMP) {
		   if (resultCode == RESULT_OK) {
			   
			   // Downsample the image size (inSampleSize > 1) for viewing in App
			   BitmapFactory.Options options = new BitmapFactory.Options();
			   options.inSampleSize = 8;

			   // Put bitmap image onto photoButton
			   Bitmap bitmap = BitmapFactory.decodeFile( _path, options );
			   photoButton.setImageBitmap(bitmap);
			   
			   //return the exif data in the photo for date, time and location
			   String strLatLong = new String (TakePhoto.strLatC + "\n" + TakePhoto.strLongC);
			   locationButton.setText(strLatLong);
//			   
//			   SimpleDateFormat dateParser = new SimpleDateFormat("yyy:MM:dd HH:mm:ss");
//			   SimpleDateFormat dateConverter = new SimpleDateFormat ("yyy-MM-dd'T'HH:mm:ss");
//			   Date d = dateParser.parse(TakePhoto.strDateTime);
//			   String DateString = dateConverter.format(d);

		   }
		   


			   
			   
		   }

	

	   }
};