package edu.ucdavis.cros.roadkill;

import java.util.ArrayList;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ListData extends ListActivity {
	private myDbAdapter myDbHelper;
	private Cursor cursor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.datalist);
		this.getListView().setDividerHeight(2);
		myDbHelper = new myDbAdapter(this);
		myDbHelper.open();
		fillData();
		registerForContextMenu(getListView());
		myDbHelper.close();
	}

	private void fillData() {
		ArrayList stringList = new ArrayList();
		cursor = myDbHelper.allrecords();

		cursor.moveToFirst();
		if (cursor.isAfterLast() == true) {
			stringList.add("No Records");
		}

		while (cursor.isAfterLast() == false) {
			stringList.add(cursor.getString(0) + ": " + cursor.getString(2)
					+ ", " + cursor.getString(8));
			cursor.moveToNext();
		}

		this.setListAdapter(new ArrayAdapter<String>(this, R.layout.datarow,
				stringList));
		Toast.makeText(this, cursor.getColumnNames().toString(), Toast.LENGTH_LONG).show();
		/*
		Cursor c = db.rawQuery("SELECT * FROM table WHERE 0", null);
		try {
		    String[] columnNames = c.columnNames();
		} finally {
		    c.close();
		}
		*/
	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
		String selection = l.getItemAtPosition(position).toString();
		cursor = myDbHelper.allrecords();

		cursor.moveToFirst();
		//cursor = myDbHelper.select(selection.substring(0, 1));
		//cursor.moveToFirst();
		Toast.makeText(this, cursor.getColumnNames().toString(), Toast.LENGTH_LONG).show();
	}

}
