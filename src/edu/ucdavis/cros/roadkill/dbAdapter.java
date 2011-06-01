package edu.ucdavis.cros.roadkill;
//This module handles all the database interaction
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class dbAdapter {
	//Log filter TAG
	private static final String TAG = "RoadKillDbAdapter";
	
	//Temporary list until database is hooked up
	public static String[] animalList = 
		{
				"Deer",
				"Bear",
				"Mountain Lion",
				"Raccoon",
				"Squirrel",
				"Rodent",
				"Skunk",
				"Bird",
				"Lizard",
				"Cat",
				"Dog",
				"Rabbit / Hare",
				"Other",
				"Mammal small",
				"Mammal medium",
				"Mammal large",
				
		};
	
	private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    private static final String DATABASE_NAME = "roadkill.db";
    private static String DB_PATH = "/data/data/edu.ucdavis.cros.roadkill/databases/";
    private static final int DATABASE_VERSION = 2;
    protected static final String LOOKUP = "common";

    //List of Database fields to be commonly used
    public static final String DATABASE_TABLE = "records";
    public static final String Key_ID = "_id";
    public static final String Record_User= "username";
    public static final String Record_Species = "species";
    public static final String Record_Lat = "lat";
    public static final String Record_Lon = "lon";
    public static final String Record_Time = "time";
    public static final String Record_Upload = "uploaded";
    public static final String Record_Photo = "photo";
    public static final String Record_Rating = "rating";
    
    private final Context myContext;
    //Defines a blank database creation
    //private static final String DATABASE_CREATE =
    //    "create table records (_id integer primary key autoincrement, "
    //    + "species text not null, location text not null);";
    
    private static class DatabaseHelper extends SQLiteOpenHelper {
    	
    	private final Context _context;
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this._context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //db.execSQL(DATABASE_CREATE);
        	//If database exists do nothing, else copy from assets
        }

        public void createDataBase() {
        	this.getReadableDatabase();
        	copyDb();
        }
        
        private void copyDb(){
        	InputStream assetsDB = null;
            try {
                assetsDB = this._context.getAssets().open(DATABASE_NAME);
                OutputStream dbOut = new FileOutputStream(DB_PATH + DATABASE_NAME);
     
                byte[] buffer = new byte[1024];
                int length;
                while ((length = assetsDB.read(buffer)) > 0) {
                    dbOut.write(buffer, 0, length);
                }
     
                dbOut.flush();
                dbOut.close();
                assetsDB.close();
                Log.i(TAG, "New database created...");
            } catch (IOException e) {
                Log.e(TAG, "Could not create new database...");
                e.printStackTrace();
            }
        }
        
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            //db.execSQL("DROP TABLE IF EXISTS records");
            //onCreate(db);
        }
        
    }
    
    public dbAdapter(Context ctx) {
        this.myContext = ctx;
    }
    public dbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(myContext);
        //Check if the database file exists, if not copy from asset
        //if you don't do this you'll overwrite on every run of the app
        File f = new File(DB_PATH + DATABASE_NAME); 
        if (f.exists() == false){
        	Log.i(TAG,"Database does not exist, creating...");
        	mDbHelper.createDataBase();
        }
        else {Log.i(TAG,"Using existing database");
        };
        
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }
    
    /* Various database queries as functions
     * Includes inserts for new records
     */
    public ArrayList<String> spplist(){
		Cursor results = mDb.rawQuery("SELECT _id,common FROM species ORDER BY common", null);
    	//Cursor mCursor = myDataBase.query("allspp",new String[] {"list"}, null, null, null, null, null);
		//return mDb.query("allspp",new String[] {LOOKUP}, null, null, null, null, null);
		Log.i(TAG,"Query of species done.");
		int count = results.getCount();
		Log.i(TAG,String.valueOf(count));
		//Convert results to a list array
		ArrayList<String> list = new ArrayList<String>();
	    while(results.moveToNext())
	    	list.add(results.getString(results.getColumnIndex("common")));
	    //Close the cursor to avoid errors later
	    results.close();
		return list;
		//return mCursor;
	}
    
    public Cursor allrecords() {
		return mDb.query(DATABASE_TABLE, new String[] { Key_ID,
				Record_User, Record_Species, Record_Lat, Record_Lon,Record_Time,Record_Upload,Record_Photo,Record_Rating }, null, null, null,
				null, null);
	}

    
    public long save(String species,String lat,String lon,String time,String photopath,float rating) {
    	//Should this be a boolean so it will return true if it works?
    	ContentValues initialValues = new ContentValues();
    	initialValues.put( Record_User ,"me");
    	initialValues.put( Record_Species ,species);
    	initialValues.put( Record_Lat , lat);
    	initialValues.put( Record_Lon , lon);
    	initialValues.put( Record_Time ,time);
    	initialValues.put( Record_Upload ,"0");
    	initialValues.put( Record_Photo , photopath);
    	initialValues.put( Record_Rating, rating);

        
        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }
}