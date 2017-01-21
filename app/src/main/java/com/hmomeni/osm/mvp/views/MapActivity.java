package com.hmomeni.osm.mvp.views;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.hmomeni.osm.R;
import com.hmomeni.osm.mvp.IMapView;
import com.hmomeni.osm.mvp.presenters.MapPresenter;
import com.hmomeni.osm.tools.MapsForgeTileProvider;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.views.MapView;

import java.io.File;

public class MapActivity extends AppCompatActivity implements IMapView {
	MapPresenter mPresenter;
	private DrawerLayout activityMap;
	private FrameLayout mapWrapper;

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
		MapView mapView = new MapView(this, mapsForgeTileProvider);
		mapView.setMultiTouchControls(true);
		mapWrapper.addView(mapView);

		mapView.getController().setCenter(boundingBox.getCenter());
		mapView.getController().setZoom(5);
	}

	private void initView() {
		activityMap = (DrawerLayout) findViewById(R.id.activity_map);
		mapWrapper = (FrameLayout) findViewById(R.id.mapWrapper);
	}
}
