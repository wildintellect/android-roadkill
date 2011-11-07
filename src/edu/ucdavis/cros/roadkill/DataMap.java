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
	private MapView mapView=null;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.datamap_view);
        
        String slat=getIntent().getStringExtra(EXTRA_LATITUDE);
        String slon=getIntent().getStringExtra(EXTRA_LONGITUDE);
        double dlat = Double.parseDouble(slat);
        double dlon = Double.parseDouble(slon);
        
        this.mapView = (MapView) findViewById(R.id.datamap);
        
        GeoPoint status=new GeoPoint((int)(dlat*1E6),(int)(dlon*1E6));
        mapView.getController().setZoom(10);
        mapView.getController().setCenter(status);
        mapView.setBuiltInZoomControls(true);
        
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
			Toast.LENGTH_LONG).show();
			return(true);
		}
		@Override
		public int size() {
			return(1);
			}
		}
}



