package edu.ucdavis.cros.roadkill;
//This module handles all the database interaction
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
    //private static final String DATABASE_TABLE = "records";
    private static String DB_PATH = "/data/data/edu.ucdavis.cros.roadkill/databases/";
    private static final int DATABASE_VERSION = 2;
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
        mDbHelper.createDataBase();
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }
    
    //various database queries as functions
    public Cursor spplist(){
		Cursor mCursor = mDb.rawQuery("SELECT _id,common FROM species ORDER BY common", null);
    	//Cursor mCursor = myDataBase.query("allspp",new String[] {"list"}, null, null, null, null, null);
		//return mDb.query("allspp",new String[] {"list"}, null, null, null, null, null);
		Log.i(TAG,"Query of species done.");
		int count = mCursor.getCount();
		Log.i(TAG,String.valueOf(count));
		return mCursor;
	}
}