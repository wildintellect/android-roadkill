package edu.ucdavis.cros.roadkill;

import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class CopyOfMapChoose extends MapActivity {

	public static final String EXTRA_LATITUDE = "edu.ucdavis.cros.roadkill.EXTRA_LATITUDE";
	public static final String EXTRA_LONGITUDE = "edu.ucdavis.cros.roadkill.EXTRA_LONGITUDE";
	public static final String EXTRA_NAME = "edu.ucdavis.cros.roadkill.EXTRA_NAME";

	LinearLayout linearLayout;
	public static MapView mapView;
	List<Overlay> mapOverlays;
	Drawable drawable;
	CopyOfOverlays overlays;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapchoose);

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

		mapOverlays = mapView.getOverlays();
		drawable = this.getResources().getDrawable(android.R.drawable.star_on);
		overlays = new CopyOfOverlays(drawable, this);

		// creates overlayitem
		GeoPoint point = new GeoPoint((int) (38.6 * 1000000.0),
				(int) (-121.1 * 1000000.0));
		OverlayItem overlayitem = new OverlayItem(point, "City", "Davis");

		// creates another item
		GeoPoint point2 = new GeoPoint(35410000, 139460000);
		OverlayItem overlayitem2 = new OverlayItem(point2, "", "");

		// adds the overlay item to our list
		overlays.addOverlay(overlayitem);
		overlays.addOverlay(overlayitem2);
		mapOverlays.add(overlays);
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}


	private GeoPoint getPoint(double lat, double lon) {
		return (new GeoPoint((int) (lat * 1000000.0), (int) (lon * 1000000.0)));
	}
	


}
