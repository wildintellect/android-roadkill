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

import android.widget.Button;

public class DateTime {
	public static void DupdateDisplay(int mYear, int mMonth, int mDay,
			Button dateButton) {
		// Month is 0 based so add 1
		dateButton.setText(new StringBuilder().append(pad(mMonth + 1))
				.append("-").append(pad(mDay)).append("-").append(mYear));

	}

	// updates the time we display in the TextView
	public static void TupdateDisplay(int mHour, int mMinute, Button timeButton) {
		boolean pm = false;
		if (mHour >= 12)
			pm = true;
		if (mHour > 12)
			mHour = mHour - 12;
		if (mHour == 0)
			mHour = 12;
		if (pm == true) {
			timeButton.setText(new StringBuilder().append(pad(mHour))
					.append(":").append(pad(mMinute)).append(" ").append("pm"));
		} else {
			timeButton.setText(new StringBuilder().append(pad(mHour))
					.append(":").append(pad(mMinute)).append(" ").append("am"));
		}
	}

	public static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}
}
