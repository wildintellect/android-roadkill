/* This class manages the use of the GPS */ 

package edu.ucdavis.cros.roadkill;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.widget.Toast;

public class GPSHandler extends Activity {

	private LocationManager lm;
	private Context rkContext;

	public GPSHandler(LocationManager locationmanager, Context roadkill) {
		lm = locationmanager;
		rkContext = roadkill;
	}

	// returns true if GPS is enabled and false if disabled
	public boolean isEnabled() {
		if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
			return true;
		else
			return false;
	}

	//creates an alert that prompts user to enable GPS
	public void promptEnable() {
		// if GPS is not enabled, prompt user to enable GPS
		AlertDialog.Builder alert = new AlertDialog.Builder(rkContext);
		alert.setTitle("GPS not enabled:");
		alert.setMessage("Would you like to enable GPS?");

		// is user chooses yes, open up GPS settings
		alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Intent i = new Intent(Settings.ACTION_SECURITY_SETTINGS);
				Roadkill rk = (Roadkill) rkContext;
				rk.startActivityForResult(i, Roadkill.GPS_BMP);
			}
		});

		// if user chooses no, show a warning message
		alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Toast.makeText(rkContext,
						"Cannot retrieve location because GPS is not enabled.",
						Toast.LENGTH_LONG).show();
			}
		});
		alert.show();
	}

	//gets the longitude and latitude from GPS
	public void getLocation() {
		try {
			Location location = lm
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			Roadkill.LATITUDE = String.format("%f", location.getLatitude());
			Roadkill.LONGITUDE = String.format("%f", location.getLongitude());
			Toast.makeText(
					rkContext,
					"Location set to " + Roadkill.LATITUDE + ", "
							+ Roadkill.LONGITUDE, Toast.LENGTH_LONG).show();
		} catch (Exception except) {
		}
	}

	//sets the coordinates to location button
	public void setLocation() {
		StringBuffer loc = new StringBuffer();
		loc.append(Roadkill.LATITUDE);
		loc.append(",");
		loc.append(Roadkill.LONGITUDE);
		(Roadkill.locationButton).setText(loc.toString());
	}
}
