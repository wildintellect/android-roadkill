package edu.ucdavis.cros.roadkill;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MapData extends MapActivity {

	public static final String EXTRA_LATITUDE = "edu.ucdavis.cros.roadkill.EXTRA_LATITUDE";
	public static final String EXTRA_LONGITUDE = "edu.ucdavis.cros.roadkill.EXTRA_LONGITUDE";
	public static final String EXTRA_NAME = "edu.ucdavis.cros.roadkill.EXTRA_NAME";

	private LinearLayout linearLayout;
	public static MapView mapView;
	private List<Overlay> mapOverlays;
	private Drawable drawable;
	private Overlays overlays;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapdata);

		double latitude = Double.parseDouble(getIntent().getStringExtra(
				EXTRA_LATITUDE));
		double longitude = Double.parseDouble(getIntent().getStringExtra(
				EXTRA_LONGITUDE));

		// set the starting location
		GeoPoint start = getPoint(latitude, longitude);

		// sets up mapview
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true); // sets up zoom control
		mapView.getController().setZoom(10); // sets default zoom
		mapView.getController().setCenter(start); // sets default map location

		// sets up overlay
		mapOverlays = mapView.getOverlays();
		drawable = this.getResources().getDrawable(android.R.drawable.star_on);
		overlays = new Overlays(drawable, this);

		// creates overlay items from records in database
		OverlayItem overlayitem;
		GeoPoint point;
		myDbAdapter myDbHelper = new myDbAdapter(this);
		myDbHelper.open();
		Cursor cursor = myDbHelper.allrecords();
		cursor.moveToFirst();

		while (cursor.isAfterLast() == false) {
			point = new GeoPoint(
					(int) (Double.valueOf(cursor.getString(3)) * 1000000.0),
					(int) (Double.valueOf(cursor.getString(4)) * 1000000.0));

			overlayitem = new OverlayItem(point, cursor.getString(2),
					cursor.getString(2));
			overlays.addOverlay(overlayitem);
			cursor.moveToNext();

		}
		myDbHelper.close();
		cursor.close();

		// adds the overlay item to our list
		mapOverlays.add(overlays);
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	private GeoPoint getPoint(double lat, double lon) {
		return (new GeoPoint((int) (lat * 1000000.0), (int) (lon * 1000000.0)));
	}

	public class Overlays extends ItemizedOverlay<OverlayItem> {

		// list of overlay items we wish to add to map
		private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();

		public Overlays(Drawable defaultMarker, Context context) {
			super(boundCenterBottom(defaultMarker));
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
			Toast.makeText(MapData.this, item.getSnippet(), Toast.LENGTH_LONG)
					.show();
			return true;
		}

	}

	/*
	 * // gets coordinates of location touched on map
	 * 
	 * @Override public boolean onTouchEvent(MotionEvent event, MapView mapview)
	 * {
	 * 
	 * if (event.getAction() == MotionEvent.ACTION_UP) { GeoPoint p =
	 * mapview.getProjection().fromPixels((int) event.getX(), (int)
	 * event.getY()); Toast.makeText(myContext, p.getLatitudeE6() / 1E6 + "," +
	 * p.getLongitudeE6() / 1E6, Toast.LENGTH_SHORT).show(); MyMap mMap =
	 * (MyMap) myContext; mMap.finish(); }
	 * 
	 * return false;
	 * 
	 * }
	 */

}
