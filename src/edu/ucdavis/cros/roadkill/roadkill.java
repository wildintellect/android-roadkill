package edu.ucdavis.cros.roadkill;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


public class roadkill extends Activity {
	//Declare Variables
	private ImageButton photoButton;
	private Button locationButton;
	private Button dateButton;
	private TextView Species;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alt);

        this.photoButton = (ImageButton)findViewById(R.id.photoButton);
        this.locationButton = (Button)findViewById(R.id.locationButton);
        this.dateButton = (Button)findViewById(R.id.dateButton);
        this.Species = (TextView) findViewById(R.id.speciesTextView);
        
        //Set up listeners for each button
        this.photoButton.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View v) {
        		
       	}
        });
    	this.locationButton.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View v) {
        		
        	}
    	});
        this.dateButton.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View v) {
        		
        	}
    	});
        Species.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View v) {
        		if (Species.getText() == "Species"){
        				Species.setText("");
        		}
        	}
    	});
    
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.view_menu, menu);
        return true;
    }   
        
    
}