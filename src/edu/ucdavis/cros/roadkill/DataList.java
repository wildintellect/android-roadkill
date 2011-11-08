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
package edu.ucdavis.cros.roadkill;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;

public class DataList extends ListActivity{
	private DbAdapter myDbHelper;
	private Cursor cursor;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.datalist);
		this.getListView().setDividerHeight(2);
		myDbHelper = new DbAdapter(this);
		myDbHelper.open();
		fillData();
		registerForContextMenu(getListView());
	}

	private void fillData() {
		cursor = myDbHelper.allrecords();
		startManagingCursor(cursor);

		String[] from = new String[] { DbAdapter.Record_Species,DbAdapter.Record_Time };
		int[] to = new int[] { R.id.label,R.id.timestamp };

		// Now create an array adapter and set it to display using our row
		SimpleCursorAdapter data = new SimpleCursorAdapter(this,
				R.layout.data_row, cursor, from, to);
		setListAdapter(data);
	}

}
