package com.hmomeni.osm.mvp.views;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.hmomeni.osm.R;
import com.hmomeni.osm.interfaces.KmlFragmentInterface;
import com.hmomeni.osm.mvp.IMapView;
import com.hmomeni.osm.mvp.presenters.MapPresenter;
import com.hmomeni.osm.objects.KmlObject;
import com.hmomeni.osm.tools.MapsForgeTileProvider;

import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.bonuspack.kml.KmlFeature;
import org.osmdroid.bonuspack.kml.KmlLineString;
import org.osmdroid.bonuspack.kml.KmlPlacemark;
import org.osmdroid.bonuspack.kml.KmlPoint;
import org.osmdroid.bonuspack.kml.KmlPolygon;
import org.osmdroid.bonuspack.kml.KmlTrack;
import org.osmdroid.bonuspack.kml.Style;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.Polyline;

import java.io.File;
import java.util.Map;

public class MapActivity extends AppCompatActivity implements IMapView, KmlFragmentInterface {
	MapPresenter mPresenter;
	private FrameLayout mapWrapper;

	Map<String, Integer> layerMap = new ArrayMap<>();
	private MapView mapView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		mPresenter = new MapPresenter();
		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
	}

	@Override
	protected void onStart() {
		super.onStart();
		mPresenter.attach(this);
		mPresenter.loadMapFile(new File(Environment.getExternalStorageDirectory(), "iran.map"));
	}

	@Override
	protected void onStop() {
		super.onStop();
		mPresenter.detach();
	}

	@Override
	public Context getViewContext() {
		return this;
	}

	@Override
	public void onMapTileLoaded(BoundingBox boundingBox,
	                            MapsForgeTileProvider mapsForgeTileProvider) {
		if (mapWrapper.getChildCount() > 0) {
			mapWrapper.removeAllViews();
		}
		mapView = new MapView(this, mapsForgeTileProvider);
		mapView.setMultiTouchControls(true);
		mapWrapper.addView(mapView);

		mapView.getController().setCenter(boundingBox.getCenter());
		mapView.getController().setZoom(5);
	}

	private void initView() {
		mapWrapper = (FrameLayout) findViewById(R.id.mapWrapper);
	}

	@Override
	public void addKmlObjectAdd(final KmlObject kmlObject) {
		int index = layerMap.size();
		layerMap.put(kmlObject.getKmlFile().getName(), index);
		KmlDocument kmlDocument = new KmlDocument();
		kmlDocument.parseKMLFile(kmlObject.getKmlFile());
		Style style = new Style(null, Color.RED, 500, Color.GREEN);
		FolderOverlay kmlOverlay = (FolderOverlay) kmlDocument.mKmlRoot.buildOverlay(mapView, style, new KmlFeature.Styler() {
			@Override
			public void onFeature(Overlay overlay, KmlFeature kmlFeature) {

			}

			@Override
			public void onPoint(Marker marker, KmlPlacemark kmlPlacemark, KmlPoint kmlPoint) {

			}

			@Override
			public void onLineString(Polyline polyline, KmlPlacemark kmlPlacemark, KmlLineString kmlLineString) {
				polyline.setColor(kmlObject.getLayerColor());
			}

			@Override
			public void onPolygon(Polygon polygon, KmlPlacemark kmlPlacemark, KmlPolygon kmlPolygon) {
				polygon.setStrokeColor(kmlObject.getLayerColor());
			}

			@Override
			public void onTrack(Polyline polyline, KmlPlacemark kmlPlacemark, KmlTrack kmlTrack) {
				polyline.setColor(kmlObject.getLayerColor());
			}
		}, kmlDocument);

		mapView.getOverlayManager().add(index, kmlOverlay);
		mapView.zoomToBoundingBox(kmlDocument.mKmlRoot.getBoundingBox(), false);
		mapView.getController().setZoom(13);

	}

	@Override
	public void removeKmlObject(KmlObject kmlObject) {
		int index = layerMap.get(kmlObject.getKmlFile().getName());
		mapView.getOverlayManager().remove(index);
		layerMap.remove(kmlObject.getKmlFile().getName());
		mapView.invalidate();
	}
}
