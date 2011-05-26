package edu.ucdavis.cros.roadkill;

/* TODO: Add license information, properly cite the Android dev code under Apache 2.0 license
*
*Code inspiration
*http://developer.android.com/resources/tutorials/views/hello-timepicker.html
*http://developer.android.com/resources/tutorials/views/hello-datepicker.html
*
*/
import java.io.IOException;
import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;
import android.widget.TimePicker;


public class roadkill extends Activity {
	//Declare Variables
	private ImageButton photoButton;
	private Button locationButton;
	private Button dateButton;
	private Button timeButton;
	private AutoCompleteTextView Species;
	private Button saveButton;
	//Declare Database
	//private dbAdapter mDbHelper;
	private DataBaseHelper myDbHelper;
	private int mYear;
    private int mMonth;
    private int mDay;
	static final int DATE_DIALOG_ID = 1;
	private int mHour;
    private int mMinute;
    static final int TIME_DIALOG_ID = 2;

	
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
        this.timeButton = (Button)findViewById(R.id.timeButton);
        splist();
        
        //TODO :Turn on GPS at application start/resume for better/faster fix
        this.saveButton = (Button)findViewById(R.id.saveButton);
        
        
        //Set up listeners for each button
        this.photoButton.setOnClickListener(new OnClickListener(){
//        	@Override
        	public void onClick(View v) {
        		//Call the code to take the picture
        		Log.i("RoadKill", "photoButton.onClick()" );
        		
        		//return the photo to the button as a thumbnail
        		//return path to the photo for storage in the db
        		//return the exif data in the photo for date, time and location
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
        		showDialog(DATE_DIALOG_ID);
        	}
    	});
        //Setup the current date/time for the dialog
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        //Enable the next line only if you want set the data immediately
        //updateDisplay();
        this.timeButton.setOnClickListener(new OnClickListener(){
//        	@Override
        	public void onClick(View v) {
        		//open a popup with a date/time selector widget
        		Log.i("RoadKill", "dateButton.onClick()" );
        		showDialog(TIME_DIALOG_ID);
        	}
    	});
        // get the current time
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        
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
    protected Dialog onCreateDialog(int id) {
            switch (id) {

            case DATE_DIALOG_ID:
                    return new DatePickerDialog(this,
                            mDateSetListener,
                            mYear, mMonth, mDay);
            
            case TIME_DIALOG_ID:
		        return new TimePickerDialog(this,
		                mTimeSetListener, mHour, mMinute, false);
		    
            }

            return null;
    }
    protected void onPrepareDialog(int id, Dialog dialog) {
            switch (id) {

            case DATE_DIALOG_ID:
                    ((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
                    break;
            }
    }    
    
    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear,
                            int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    DateTime.DupdateDisplay(mYear,mMonth,mDay,dateButton);
            }
    };
    // the callback received when the user "sets" the time in the dialog
    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
        new TimePickerDialog.OnTimeSetListener() {
    		@Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mHour = hourOfDay;
                mMinute = minute;
                DateTime.TupdateDisplay(mHour,mMinute,timeButton);
            }

			
        };
    
}