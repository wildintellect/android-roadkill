package edu.ucdavis.cros.roadkill;
//This module handles all the database interaction
import android.content.Context;
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
				"Rabbit/Hare",
				"Other",
				"Mammal/small",
				"Mammal/medium",
				"Mammal/large",
				
		};
	
	private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    private static final String DATABASE_NAME = "roadkill";
    private static final String DATABASE_TABLE = "records";
    private static final int DATABASE_VERSION = 2;
    
    //Defines a blank database creation
    private static final String DATABASE_CREATE =
        "create table records (_id integer primary key autoincrement, "
        + "species text not null, location text not null);";
    
    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS records");
            onCreate(db);
        }
    }
}