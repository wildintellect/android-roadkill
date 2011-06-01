package edu.ucdavis.cros.roadkill;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;

public class DataList extends ListActivity{
	private dbAdapter myDbHelper;
	private Cursor cursor;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.datalist);
		this.getListView().setDividerHeight(2);
		myDbHelper = new dbAdapter(this);
		myDbHelper.open();
		fillData();
		registerForContextMenu(getListView());
	}

	private void fillData() {
		cursor = myDbHelper.allrecords();
		startManagingCursor(cursor);

		String[] from = new String[] { dbAdapter.Record_Species };
		int[] to = new int[] { R.id.label };

		// Now create an array adapter and set it to display using our row
		SimpleCursorAdapter notes = new SimpleCursorAdapter(this,
				R.layout.data_row, cursor, from, to);
		setListAdapter(notes);
	}

}
