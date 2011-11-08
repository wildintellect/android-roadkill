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
 * Code inspiration
 * http://www.anddev.org/code-snippets-for-android-f33/autocompletetextview-cursoradapter-t12430.html
 */

package edu.ucdavis.cros.roadkill;
/*

 */

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class SpCurAdapter extends CursorAdapter {
        public SpCurAdapter(Context context, Cursor c)
        {
            super(context, c);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor)
        {
            int columnIndex = cursor.getColumnIndexOrThrow(DbAdapter.LOOKUP);
            ((TextView) view).setText(cursor.getString(columnIndex));
        }

        @Override
        public String convertToString(Cursor cursor)
        {
            int columnIndex = cursor.getColumnIndexOrThrow(DbAdapter.LOOKUP);
            return cursor.getString(columnIndex);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent)
        {
            final LayoutInflater inflater = LayoutInflater.from(context);
            final TextView view = (TextView) inflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
            int columnIndex = cursor.getColumnIndexOrThrow(DbAdapter.LOOKUP);
            view.setText(cursor.getString(columnIndex));
            return view;
        }

//        @Override
//        public Cursor runQueryOnBackgroundThread(CharSequence constraint)
//        {
//        	final dbAdapter db = new dbAdapter(Distandroid.this); 
        	
//        	if (constraint == null)
//            {
                //db.open();    
        		//Cursor cursorBackground = db.getSuggestions("%");
        		//db.close();
        		
//        		return cursorBackground;
//            }   
            
//        	db.open();    
//    		Cursor cursorBackground = db.getSuggestions(capitalizeFirstLetter(constraint.toString()));
//    		db.close();
    		
//            return cursorBackground;
//        }
     
}
