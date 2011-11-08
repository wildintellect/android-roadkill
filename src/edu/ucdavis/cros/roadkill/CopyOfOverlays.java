package edu.ucdavis.cros.roadkill;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class CopyOfOverlays extends ItemizedOverlay<OverlayItem> {

	// list of overlay items we wish to add to map
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Context myContext;

	private Drawable marker = null;
	private OverlayItem inDrag = null;
	private ImageView dragImage = null;
	private int xDragImageOffset = 0;
	private int yDragImageOffset = 0;
	private int xDragTouchOffset = 0;
	private int yDragTouchOffset = 0;

	public CopyOfOverlays(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		myContext = context;

		this.marker = defaultMarker;

		dragImage = (ImageView) ((MapChoose) myContext).findViewById(R.id.drag);
		xDragImageOffset = dragImage.getDrawable().getIntrinsicWidth() / 2;
		yDragImageOffset = dragImage.getDrawable().getIntrinsicHeight();
		
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

	@Override
	public boolean onTouchEvent(MotionEvent event, MapView mapView) {
		final int action = event.getAction();
		final int x = (int) event.getX();
		final int y = (int) event.getY();
		boolean result = false;

		if (action == MotionEvent.ACTION_DOWN) {
			for (OverlayItem item : mOverlays) {
				Point p = new Point(0, 0);

				((MapData) myContext).mapView.getProjection().toPixels(
						item.getPoint(), p);

				if (hitTest(item, marker, x - p.x, y - p.y)) {
					result = true;
					inDrag = item;
					mOverlays.remove(inDrag);
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

			GeoPoint pt = ((MapData) myContext).mapView.getProjection()
					.fromPixels(x - xDragTouchOffset, y - yDragTouchOffset);
			OverlayItem toDrop = new OverlayItem(pt, inDrag.getTitle(),
					inDrag.getSnippet());

			mOverlays.add(toDrop);
			populate();

			inDrag = null;
			result = true;
		}

		return (result || super.onTouchEvent(event, mapView));
	}

	private void setDragImagePosition(int x, int y) {
		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) dragImage
				.getLayoutParams();

		lp.setMargins(x - xDragImageOffset - xDragTouchOffset, y
				- yDragImageOffset - yDragTouchOffset, 0, 0);
		dragImage.setLayoutParams(lp);
	}
}
/*
 * // gets coordinates of location touched on map
 * 
 * @Override public boolean onTouchEvent(MotionEvent event, MapView mapview) {
 * 
 * if (event.getAction() == MotionEvent.ACTION_UP) { GeoPoint p =
 * mapview.getProjection().fromPixels((int) event.getX(), (int) event.getY());
 * Toast.makeText(myContext, p.getLatitudeE6() / 1E6 + "," + p.getLongitudeE6()
 * / 1E6, Toast.LENGTH_SHORT).show(); MyMap mMap = (MyMap) myContext;
 * mMap.finish(); }
 * 
 * return false;
 * 
 * }
 */

