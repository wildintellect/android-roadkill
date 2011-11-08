/*This Map View allows the user to choose coordinates by dragging a pin to the desired location*/

package edu.ucdavis.cros.roadkill;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MapChoose extends MapActivity {

	public static final String EXTRA_LATITUDE = "edu.ucdavis.cros.roadkill.EXTRA_LATITUDE";
	public static final String EXTRA_LONGITUDE = "edu.ucdavis.cros.roadkill.EXTRA_LONGITUDE";
	public static final String EXTRA_NAME = "edu.ucdavis.cros.roadkill.EXTRA_NAME";

	private LinearLayout linearLayout;
	private MapView mapView;
	private List<Overlay> mapOverlays;
	private Drawable drawable;
	private Overlays overlays;
	private static String LAT;
	private static String LON;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapchoose);

		double latitude = Double.parseDouble(getIntent().getStringExtra(
				EXTRA_LATITUDE));
		double longitude = Double.parseDouble(getIntent().getStringExtra(
				EXTRA_LONGITUDE));

		Toast.makeText(MapChoose.this, "Drag pin to desired location.",
				Toast.LENGTH_SHORT).show();

		// set the starting center of map
		GeoPoint start = getPoint(latitude, longitude);

		// sets up mapview
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true); // sets up zoom control
		mapView.getController().setZoom(10); // sets default zoom
		mapView.getController().setCenter(start); // sets default map location

		// sets up overlay items
		mapOverlays = mapView.getOverlays();
		drawable = this.getResources().getDrawable(R.drawable.pin);
		overlays = new Overlays(drawable);

		// creates an overlay item at our starting center
		GeoPoint point = getPoint(latitude, longitude);
		OverlayItem pin = new OverlayItem(point, "California", "Davis");

		// adds the overlay item to our list
		overlays.addOverlay(pin);
		mapOverlays.add(overlays);
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	private GeoPoint getPoint(double lat, double lon) {
		return (new GeoPoint((int) (lat * 1000000.0), (int) (lon * 1000000.0)));
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.select:
			overlays.promptCoordinates();
			break;
		}
	}

	// this class deals with overlay items
	public class Overlays extends ItemizedOverlay<OverlayItem> {

		// list of overlay items we wish to add to map
		private ArrayList<OverlayItem> myOverlays = new ArrayList<OverlayItem>();

		private Drawable marker = null;
		private OverlayItem inDrag = null;
		private ImageView dragImage = null;
		private int xDragImageOffset = 0;
		private int yDragImageOffset = 0;
		private int xDragTouchOffset = 0;
		private int yDragTouchOffset = 0;

		public Overlays(Drawable defaultMarker) {
			super(boundCenterBottom(defaultMarker));
			this.marker = defaultMarker;

			dragImage = (ImageView) findViewById(R.id.drag);
			xDragImageOffset = dragImage.getDrawable().getIntrinsicWidth() / 2;
			yDragImageOffset = dragImage.getDrawable().getIntrinsicHeight();
		}

		// adds the overlay items
		public void addOverlay(OverlayItem overlay) {
			myOverlays.add(overlay);
			populate();
		}

		// populate() calls this method to read from our overlay list
		@Override
		protected OverlayItem createItem(int i) {
			return myOverlays.get(i);
		}

		@Override
		public int size() {
			return myOverlays.size();
		}

		// method that controls the movable overlay item
		@Override
		public boolean onTouchEvent(MotionEvent event, MapView mapview) {
			final int action = event.getAction();
			final int x = (int) event.getX();
			final int y = (int) event.getY();
			boolean result = false;

			if (action == MotionEvent.ACTION_DOWN) {
				for (OverlayItem item : myOverlays) {
					Point p = new Point(0, 0);

					mapView.getProjection().toPixels(item.getPoint(), p);

					if (hitTest(item, marker, x - p.x, y - p.y)) {
						result = true;
						inDrag = item;
						myOverlays.remove(inDrag);
						populate();

						xDragTouchOffset = 0;
						yDragTouchOffset = 0;

						setDragImagePosition(p.x, p.y);
						dragImage.setVisibility(View.VISIBLE);

						xDragTouchOffset = x - p.x;
						yDragTouchOffset = y - p.y;

						break;
					}
				}
			} else if (action == MotionEvent.ACTION_MOVE && inDrag != null) {
				setDragImagePosition(x, y);
				result = true;
			} else if (action == MotionEvent.ACTION_UP && inDrag != null) {
				dragImage.setVisibility(View.GONE);

				GeoPoint pt = mapView.getProjection().fromPixels(
						x - xDragTouchOffset, y - yDragTouchOffset);
				OverlayItem toDrop = new OverlayItem(pt, inDrag.getTitle(),
						inDrag.getSnippet());

				myOverlays.add(toDrop);
				populate();

				// show toast of coordinates
				GeoPoint p = mapview.getProjection().fromPixels(
						(int) event.getX(), (int) event.getY());
				LAT = Double.toString(p.getLatitudeE6() / 1E6);
				LON = Double.toString(p.getLongitudeE6() / 1E6);
				Toast.makeText(MapChoose.this, LAT + ", " + LON,
						Toast.LENGTH_SHORT).show();

				inDrag = null;
				result = true;
			}

			return (result || super.onTouchEvent(event, mapview));
		}

		// sets image position
		private void setDragImagePosition(int x, int y) {
			RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) dragImage
					.getLayoutParams();

			lp.setMargins(x - xDragImageOffset - xDragTouchOffset, y
					- yDragImageOffset - yDragTouchOffset, 0, 0);
			dragImage.setLayoutParams(lp);
		}

		// show alert that prompts user to confirm coordinates
		private void promptCoordinates() {
			AlertDialog.Builder alert = new AlertDialog.Builder(MapChoose.this);
			alert.setTitle("Choose this coordinate?");
			alert.setMessage(LAT + ", " + LON);

			// is user chooses yes, return to main activity
			alert.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							Roadkill.LATITUDE = LAT;
							Roadkill.LONGITUDE = LON;
							setResult(MapChoose.RESULT_OK);
							finish();
						}
					});
			// if user chooses no, let them choose again
			alert.setNegativeButton("No",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							Toast.makeText(MapChoose.this,
									"Drag pin to desired location.",
									Toast.LENGTH_SHORT).show();
						}
					});
			alert.show();
		}

	}

}
