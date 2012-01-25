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
 */

/* TODO: Add license information, properly cite the Android dev code under Apache 2.0 license
 *
 *Code inspiration
 *http://developer.android.com/resources/tutorials/views/hello-timepicker.html
 *http://developer.android.com/resources/tutorials/views/hello-datepicker.html
 *
 */

package edu.ucdavis.cros.roadkill;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class Roadkill extends Activity {
	// Debug log tag id
	private static final String TAG = "RoadKill";
	// Declare Variables
	public ImageButton photoButton;
	public static String _path;
	private Button dateButton;
	private Button timeButton;
	private ImageButton helpButton;
	private AutoCompleteTextView Species;
	private Button saveButton;
	public double phButt_h;
	public double phButt_w;
	public double bitmap_h;
	public double bitmap_w;
	public boolean rotated;
	public int hInt;
	public int wInt;
	public Bitmap resultBmp;
	public Bitmap scaledBmp;

	// private DataBaseHelper myDbHelper;
	private int mYear;
	private int mMonth;
	private int mDay;
	private int mHour;
	private int mMinute;

	static final int DATE_DIALOG_ID = 1;
	static final int TIME_DIALOG_ID = 2;
	static final int LOCATION_DIALOG_ID = 3;
	static final int LOGIN_DIALOG_ID = 4;

	private boolean loginSuccess = false;
	private RatingBar ratingBar;
	// private float rating;
	LocationManager lm = null;
	LocationListener LocL = null;

	public static final int PHOTO_BMP = 1;
	public static final int GPS_BMP = 2;
	public static final int MAP_BMP = 3;
	public static Button locationButton;
	public static String LATITUDE = "38.6"; // default set to Davis, CA
	public static String LONGITUDE = "-121.1";
	private Calendar c;
	private GPSHandler gh; // create GPSHandler
	private myDbAdapter myDb; // create database
	private MySqlHandler msh;
	private String recordDate;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alt);

		c = Calendar.getInstance();
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		gh = new GPSHandler(lm, this);

		myDb = new myDbAdapter(this);
		msh = new MySqlHandler();

		this.photoButton = (ImageButton) findViewById(R.id.photoButton);
		this.locationButton = (Button) findViewById(R.id.locationButton);
		this.dateButton = (Button) findViewById(R.id.dateButton);
		this.timeButton = (Button) findViewById(R.id.timeButton);
		this.helpButton = (ImageButton) findViewById(R.id.buttonHelp);
		this.saveButton = (Button) findViewById(R.id.saveButton);
		this.ratingBar = (RatingBar) findViewById(R.id.ratingBar1);
		splist();

		// update changes to GPS status
		LocL = new LocationListener() {
			public void onLocationChanged(Location location) {
				gh.getLocation();
			}

			public void onProviderDisabled(String arg0) {
				// Toast.makeText(getApplicationContext(),
				// "GPS is currently off.", Toast.LENGTH_SHORT).show();
			}

			public void onProviderEnabled(String arg0) {
				// Toast.makeText(getApplicationContext(), "GPS is now on.",
				// Toast.LENGTH_SHORT).show();
			}

			public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
				// TODO Auto-generated method stub
			}
		};
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, LocL);

		this.Species.setOnClickListener(new OnClickListener() {
			// @Override
			public void onClick(View v) {
				Log.i(TAG, "Species.onClick()");
				// clear the Species label once the start typing
				// auto match based on the pre-seeded data
				// if (Species.getText() == "Species"){
				// Species.setText("");

			}
		});

	}

	private void splist() {
		// Set the species list from a database query
		myDb.open();
		final ArrayList<String> spplist = myDb.spplist();
		// startManagingCursor(sppCursor);
		// String[] from = new String[]{"common"};
		// int[] to = new int[]{R.id.speciesTextView};
		// SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
		// android.R.layout.simple_dropdown_item_1line, sppCursor, from, to);
		// SpCurAdapter adapter = new SpCurAdapter(this,sppCursor);
		// int desiredColumn = 1;
		// adapter.setCursorToStringConverter(null);
		// adapter.setStringConversionColumn(desiredColumn);

		// TODO: fix, Hack to convert arraylist to stringarray?
		final String[] mString = (String[]) spplist.toArray(new String[spplist
				.size()]);
		// Method uses a straight string array
		// ArrayAdapter<String> adapter = new ArrayAdapter<String>
		// (this,android.R.layout.simple_dropdown_item_1line,
		// dbAdapter.animalList);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, mString);
		// adapter.setStringConversionColumn(1);
		// adapter.convertToString(sppCursor);
		this.Species = (AutoCompleteTextView) findViewById(R.id.speciesTextView);
		this.Species.setAdapter(adapter);
		myDb.close();
	}

	// create Options Menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.view_menu, menu);
		return true;
	}

	// Option is selected
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.op_settings: {
			Log.i(TAG, "Settings clicked");
			return true;
		}
		case R.id.op_upload: {
			/*
			 * Uploads all records flagged as not uploaded to the site and sets
			 * the value from "0" to "1"
			 */
			Log.i(TAG, "Upload clicked");
			showDialog(LOGIN_DIALOG_ID);
			return true;
		}
		case R.id.op_map: {
			Log.i(TAG, "Map clicked");
			Intent mapIntent = new Intent(Roadkill.this, MapData.class);
			mapIntent.putExtra(MapData.EXTRA_LATITUDE, LATITUDE);
			mapIntent.putExtra(MapData.EXTRA_LONGITUDE, LONGITUDE);
			startActivity(mapIntent);
			return true;
		}
		case R.id.op_list: {
			Intent dataIntent = new Intent(Roadkill.this, ListData.class);
			startActivity(dataIntent);
			return true;
		}
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// create dialogs
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			mYear = c.get(Calendar.YEAR);
			mMonth = c.get(Calendar.MONTH);
			mDay = c.get(Calendar.DAY_OF_MONTH);
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
		case TIME_DIALOG_ID:
			mHour = c.get(Calendar.HOUR_OF_DAY);
			mMinute = c.get(Calendar.MINUTE);
			return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute,
					false);
		case LOCATION_DIALOG_ID: {
			final CharSequence[] items = { "From Photo Data", "From GPS",
					"From Map" };
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Get Location:");
			builder.setItems(items, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					Log.i(TAG, items[item].toString());
					switch (item) {
					case 0: // From Photo
						LATITUDE = TakePhoto.strLatC;
						LONGITUDE = TakePhoto.strLongC;
						gh.setLocation();
						break;
					case 1: // From GPS
						// Check to see if GPS is enabled
						if (!gh.isEnabled()) {
							gh.promptEnable();
							break;
						} else {
							// GPS is already enabled
							gh.getLocation();
							gh.setLocation();
						}
						break;
					case 2: // from Map
						Intent i = new Intent(Roadkill.this, MapChoose.class);
						i.putExtra(MapChoose.EXTRA_LATITUDE, LATITUDE);
						i.putExtra(MapChoose.EXTRA_LONGITUDE, LONGITUDE);
						startActivityForResult(i, MAP_BMP);
						break;
					}
				}
			});
			AlertDialog gpsAlert = builder.create();
			return gpsAlert;
		}
		case LOGIN_DIALOG_ID: {
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			final View layout = inflater.inflate(R.layout.login,
					(ViewGroup) findViewById(R.id.root));
			final EditText user = (EditText) layout.findViewById(R.id.username);
			final EditText pass = (EditText) layout.findViewById(R.id.password);
			final TextView error = (TextView) layout
					.findViewById(R.id.TextView_PwdProblem);
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Wildlifecrossing.net Login");
			builder.setView(layout);
			builder.setNegativeButton(android.R.string.cancel,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							removeDialog(LOGIN_DIALOG_ID);
						}
					});
			builder.setPositiveButton(android.R.string.ok,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							String username = user.getText().toString();
							String password = pass.getText().toString();
							if (msh.login(username, password)) {
								loginSuccess = true;
								upload();
								Toast.makeText(Roadkill.this,
										"Upload Complete", Toast.LENGTH_LONG)
										.show();
								msh.reset();
								Log.i(TAG, "Login Successful");
							} else {
								loginSuccess = false;
								Log.i(TAG, "Login Failed");
								Toast.makeText(Roadkill.this,
										"Login Failed: Please Try Again",
										Toast.LENGTH_LONG).show();
							}
							removeDialog(LOGIN_DIALOG_ID);
						}
					});
			Log.i(TAG, "Login Done");
			AlertDialog loginDialog = builder.create();
			return loginDialog;
		}

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

	// the callback received when the user "sets" the date in the date dialog
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int month, int day) {
			recordDate = Integer.toString(month + 1) + "/"
					+ Integer.toString(day) + "/" + Integer.toString(year);
			DateTime.DupdateDisplay(year, month, day, dateButton);
		}
	};

	// the callback received when the user "sets" the time in the time dialog
	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hour, int min) {
			DateTime.TupdateDisplay(hour, min, timeButton);
		}
	};

	// actions taken from return from child activities
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// return from GPS settings
		if (requestCode == GPS_BMP) {
			if (resultCode == RESULT_OK) {
				// check to see if GPS has been enabled
				if (!gh.isEnabled()) {
					Toast.makeText(getApplicationContext(),
							"GPS has not been enabled.", Toast.LENGTH_LONG)
							.show();
				} else {
					// GPS is enabled
					gh.getLocation();
					gh.setLocation();
				}
			}
		}
		// return from MapChoose
		if (requestCode == MAP_BMP) {
			if (resultCode == RESULT_OK) {
				gh.setLocation();
			}
		}
		// return the photo to the button as a thumbnail
		if (requestCode == PHOTO_BMP) {
			if (resultCode == RESULT_OK) {

				// TODO: return image that fits the button

				// Put bitmap image onto photoButton
				Bitmap bitmap = BitmapFactory.decodeFile(_path);
				phButt_w = photoButton.getWidth();
				phButt_h = photoButton.getHeight();

				// determine Bitmap orientation
				int picOr = Integer.valueOf(TakePhoto.strPicOr);
				Matrix mat = new Matrix();

				switch (picOr) {

				case 1:
					// taken in Landscape so rotate 0 degrees
					bitmap_w = (phButt_w - 10);
					bitmap_h = (phButt_h - 10) * (TakePhoto.picRatio);
					// double hFl = Math.floor(bitmap_h);
					hInt = (int) bitmap_h;
					wInt = (int) bitmap_w;
					scaledBmp = Bitmap.createScaledBitmap(bitmap, hInt, wInt,
							true);
					bitmap.recycle();
					bitmap = null;

					mat.postRotate(0);
					resultBmp = Bitmap.createBitmap(scaledBmp, 0, 0, hInt,
							wInt, mat, true);

					photoButton.setImageBitmap(resultBmp);
					break;

				case 3:
					// taken upside down Landscape so rotate 180 degrees
					bitmap_w = (phButt_w - 10) * (TakePhoto.picRatio);
					bitmap_h = (phButt_h - 10);
					// double hFl = Math.floor(bitmap_h);
					hInt = (int) bitmap_h;
					wInt = (int) bitmap_w;
					scaledBmp = Bitmap.createScaledBitmap(bitmap, hInt, wInt,
							true);
					bitmap.recycle();
					bitmap = null;

					mat.postRotate(180);
					resultBmp = Bitmap.createBitmap(scaledBmp, 0, 0, hInt,
							wInt, mat, true);

					photoButton.setImageBitmap(resultBmp);
					break;

				case 6:
					// taken in Portrait so rotate 90 degrees
					bitmap_w = (phButt_w - 10) * (1 / TakePhoto.picRatio);
					bitmap_h = (phButt_h - 10);
					// double hFl = Math.floor(bitmap_h);
					hInt = (int) bitmap_h;
					wInt = (int) bitmap_w;
					scaledBmp = Bitmap.createScaledBitmap(bitmap, hInt, wInt,
							true);
					bitmap.recycle();
					bitmap = null;

					mat.postRotate(90);
					resultBmp = Bitmap.createBitmap(scaledBmp, 0, 0, hInt,
							wInt, mat, true);

					photoButton.setImageBitmap(resultBmp);
					break;

				case 8:
					// taken upside down Portrait so rotate 270 degrees
					bitmap_w = (phButt_w - 10) * (1 / TakePhoto.picRatio);
					bitmap_h = (phButt_h - 10);
					// double hFl = Math.floor(bitmap_h);
					hInt = (int) bitmap_h;
					wInt = (int) bitmap_w;
					scaledBmp = Bitmap.createScaledBitmap(bitmap, hInt, wInt,
							true);
					bitmap.recycle();
					bitmap = null;

					mat.postRotate(270);
					resultBmp = Bitmap.createBitmap(scaledBmp, 0, 0, hInt,
							wInt, mat, true);

					photoButton.setImageBitmap(resultBmp);
					// // Release image resources
					// scaledBmp.recycle();
					// scaledBmp = null;
					break;
				}

				// return the exif data in the photo for date, time and
				// location
				if (TakePhoto.strLatC != null) {
					String strLatLong = new String(TakePhoto.strLatC + ","
							+ TakePhoto.strLongC);
					locationButton.setText(strLatLong);
				}
				;

				if (TakePhoto.strDateTime != null) {

					String strDate = new String(TakePhoto.localDFreg.substring(
							0, 10));
					String strTime = new String(
							TakePhoto.localDFreg.substring(11));
					strDate = strDate.replace(":", "-");
					dateButton.setText(strDate);
					timeButton.setText(strTime);

				}
			}
		}

	}

	// action taken from clicking a button
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.buttonHelp:
			Log.i(TAG, "ratingHelp.onClick()");
			showHelp();
			break;
		case R.id.photoButton:
			// Call the code to take the picture
			Log.i(TAG, "photoButton.onClick()");

			// set path for photo to be saved in db
			_path = Environment
					.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
					+ "CROSPic.jpg";

			// Start TakePhoto Activity
			Intent photoIntent = new Intent(Roadkill.this, TakePhoto.class);
			startActivityForResult(photoIntent, PHOTO_BMP);
			break;
		case R.id.dateButton:
			// open a pop-up with a date/time selector widget
			Log.i(TAG, "dateButton.onClick()");
			showDialog(DATE_DIALOG_ID);
			break;
		case R.id.timeButton:
			// open a pop-up with a date/time selector widget
			Log.i(TAG, "dateButton.onClick()");
			showDialog(TIME_DIALOG_ID);
			break;
		case R.id.locationButton:
			Log.i(TAG, "locationButton.onClick()");
			// open a selection window for photo, GPS, or map location
			showDialog(LOCATION_DIALOG_ID);
			break;
		case R.id.saveButton:
			// save data to record, if existing record update information
			Log.i(TAG, "saveButton.onClick()");
			Species.performValidation();
			String photopath = new String("");
			myDb.open();
			myDb.save(Species.getText().toString(), LATITUDE, LONGITUDE,
					dateButton.getText().toString(), timeButton.getText()
							.toString(), _path, ratingBar.getRating());
			myDb.close();
			Log.i(TAG, "Saved Record");
			break;
		}
	}

	// display alert that explains the rating
	public void showHelp() {
		AlertDialog.Builder help = new AlertDialog.Builder(this);
		help.setTitle("Confidence Level");
		help.setMessage("Please indicate your level of confidence in identifying the species:"
				+ "\n"
				+ "1 star - Not Confident"
				+ "\n"
				+ "2 star - Somewhat Confident"
				+ "\n"
				+ "3 star - Very Confident");
		help.setNegativeButton("Close", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		help.show();
	}

	@Override
	public void onStop() { //
		lm.removeUpdates(LocL);
		// myDb.close();
		super.onStop();
	}

	@Override
	protected void onResume() { //
		onRestart();
		// myDb.open();
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, LocL);
		// myDb.open();
	}

	@Override
	protected void onPause() { //
		// myDb.close();
		super.onPause();
	}

	@Override
	protected void onRestart() {
		super.onRestart();

		// myDb.open();
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, LocL);
		// myDb.open();
	}

	void upload() {
		if (loginSuccess) {
			Log.i(TAG, "Uploading Records...");
			myDb.open();
			Cursor cursor = myDb.queryUpload();
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				msh.sendRecord(cursor.getString(2), cursor.getString(8),
						"Android App: To be implemented", cursor.getString(3),
						cursor.getString(4), cursor.getString(5),
						cursor.getString(6), "0", "Android App: To be implemented");
				myDb.setUploaded(cursor.getString(0));
				cursor.moveToNext();
			}
			Toast.makeText(Roadkill.this, "Upload Sucessful",
					Toast.LENGTH_SHORT).show();
			myDb.close();
		}
	}

};
