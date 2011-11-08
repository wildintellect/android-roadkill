package edu.ucdavis.cros.roadkill;

import java.util.ArrayList;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;

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
	}

	private void fillData() {
		cursor = myDbHelper.allrecords();

		cursor.moveToFirst();
		ArrayList stringList = new ArrayList();
		while (cursor.isAfterLast() == false) {
			stringList.add(cursor.getString(0) + " " + cursor.getString(2));
			cursor.moveToNext();
		}

		this.setListAdapter(new ArrayAdapter<String>(this, R.layout.datarow,
				stringList));
	}

}
