package edu.ucdavis.cros.roadkill;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class DataMap extends MapActivity {

	public static final String EXTRA_LATITUDE="edu.ucdavis.cros.roadkill.EXTRA_LATITUDE";
	public static final String EXTRA_LONGITUDE="edu.ucdavis.cros.roadkill.EXTRA_LONGITUDE";
	public static final String EXTRA_NAME="edu.ucdavis.cros.roadkill.EXTRA_NAME";

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        double lat=getIntent().getDoubleExtra(EXTRA_LATITUDE, 0);
        double lon=getIntent().getDoubleExtra(EXTRA_LONGITUDE, 0);
        
        MapView mapView = (MapView) findViewById(R.id.datamap);
        mapView.setBuiltInZoomControls(true);
        
        GeoPoint status=new GeoPoint((int)(lat*1000000.0),(int)(lon*1000000.0));
        mapView.getController().setCenter(status);

        Drawable marker=getResources().getDrawable(android.R.drawable.star_on);
        marker.setBounds(0, 0, marker.getIntrinsicWidth(),
        marker.getIntrinsicHeight());
        mapView
        .getOverlays()
        .add(new DataOverlay(marker, status,getIntent().getStringExtra(EXTRA_NAME)));
        
//        Intent myIntent = new Intent(android.content.Intent.ACTION_VIEW,
//        		Uri.parse("geo:0,0?q="));
        		//Uri.parse("geo:0,0?q="+Uri.fromFile(kmlFile)));
        		//Uri.parse("geo:0,0?q=http://gis.its.ucdavis.edu/hydrogen/feeds/dynamic/stations.kml"));
//        		startActivity(myIntent);
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	
	private class DataOverlay extends ItemizedOverlay<OverlayItem> {
		private OverlayItem item=null;
		public DataOverlay(Drawable marker, GeoPoint point,	String name) {
			super(marker);
			boundCenterBottom(marker);
			item=new OverlayItem(point, name, name);
			populate();
		}
		
		@Override
		protected OverlayItem createItem(int i) {
			return(item);
		}
		@Override
		protected boolean onTap(int i) {
			Toast.makeText(DataMap.this,
			item.getSnippet(),
			Toast.LENGTH_SHORT).show();
			return(true);
		}
		@Override
		public int size() {
			return(1);
			}
		}
}



