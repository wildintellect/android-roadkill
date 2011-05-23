package edu.ucdavis.cros.roadkill;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;


public class roadkill extends Activity {
	//Declare Variables
	private ImageButton photoButton;
	private Button locationButton;
	private Button dateButton;
	private AutoCompleteTextView Species;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alt);

        this.photoButton = (ImageButton)findViewById(R.id.photoButton);
        this.locationButton = (Button)findViewById(R.id.locationButton);
        this.dateButton = (Button)findViewById(R.id.dateButton);
        //this.Species = (TextView) findViewById(R.id.speciesTextView);
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String> (this,android.R.layout.simple_dropdown_item_1line, dbAdapter.animalList);
        this.Species = (AutoCompleteTextView) findViewById(R.id.speciesTextView);
        Species.setAdapter(adapter);
        
        //Set up listeners for each button
        this.photoButton.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View v) {
        		//Call the code to take the picture
        		Log.i("RoadKill", "photoButton.onClick()" );
        		
        		//return the photo to the button as a thumbnail
        		//return path to the photo for storage in the db
        		//return the exif data in the photo for date, time and location
        	}
        });
    	this.locationButton.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View v) {
        		Log.i("RoadKill", "locationButton.onClick()" );
        		//open a selection window, 
        		//ask the user if they want the GPS fix
        		//or adjust manually on a map
        	}
    	});
        this.dateButton.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View v) {
        		//open a popup with a date/time selector widget
        		Log.i("RoadKill", "dateButton.onClick()" );
        	}
    	});
        this.Species.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View v) {
        		Log.i("RoadKill", "Species.onClick()" );
        		//clear the Species label once the start typing
        		//auto match based on the pre-seeded data
        		//if (Species.getText() == "Species"){
        		//		Species.setText("");
        		
        	}
    	});
    
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
        
    
}