package edu.ucdavis.cros.roadkill;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.view.View;
import android.widget.Button;

public class DateTime {
	public static void DupdateDisplay(int mYear,int mMonth, int mDay,Button dateButton) {
		dateButton.setText(
                new StringBuilder()
                // Month is 0 based so add 1
                .append(mMonth + 1).append("-")
                .append(mDay).append("-")
                .append(mYear).append(" "));
		
	}
	// updates the time we display in the TextView
	public static void TupdateDisplay(int mHour,int mMinute,Button timeButton) {
	    timeButton.setText(
	        new StringBuilder()
	                .append(pad(mHour)).append(":")
	                .append(pad(mMinute)));
	}

	public static String pad(int c) {
	    if (c >= 10)
	        return String.valueOf(c);
	    else
	        return "0" + String.valueOf(c);
	}
}
