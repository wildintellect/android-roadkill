package edu.ucdavis.cros.roadkill;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class Overlays extends ItemizedOverlay<OverlayItem> {

	// list of overlay items we wish to add to map
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Context myContext;

	public Overlays(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		myContext = context;
	}

	// adds the overlay items
	public void addOverlay(OverlayItem overlay) {
		mOverlays.add(overlay);
		populate();
	}

	// populate() calls this method to read from our overlay list
	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}

	// displays information of overlay item when tapped
	@Override
	protected boolean onTap(int index) {
		OverlayItem item = mOverlays.get(index);
		Toast.makeText(myContext, item.getSnippet(), Toast.LENGTH_LONG).show();
		return true;
	}

	// gets coordinates of location touched on map
	@Override
	public boolean onTouchEvent(MotionEvent event, MapView mapview) {

		if (event.getAction() == MotionEvent.ACTION_UP) {
			GeoPoint p = mapview.getProjection().fromPixels((int) event.getX(),
					(int) event.getY());
			Toast.makeText(myContext,
					p.getLatitudeE6() / 1E6 + "," + p.getLongitudeE6() / 1E6,
					Toast.LENGTH_SHORT).show();
			MyMap mMap = (MyMap) myContext;
			mMap.finish();
		}

		return false;
	}

}
