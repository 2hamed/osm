package com.hmomeni.osm;

import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
	private static final String TAG = "MainActivity";
	MapView map;
	NestedScrollView bottomSheet;
	LinearLayout layerWrapper;
	BottomSheetBehavior<NestedScrollView> bottomSheetBehavior;
	Button openLayers;
	public static final int baseId = 6161;
	List<KmlDocument> kmlDocuments = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
		setContentView(R.layout.activity_main);
		bottomSheet = (NestedScrollView) findViewById(R.id.bottomSheet);
		layerWrapper = (LinearLayout) findViewById(R.id.layerWrapper);
		bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
		openLayers = (Button) findViewById(R.id.openLayers);
		openLayers.setOnClickListener(this);

		Util.copyAssets(this);

		map = (MapView) findViewById(R.id.map);
		map.setTileSource(TileSourceFactory.MAPNIK);

//		map.setBuiltInZoomControls(true);
		map.setMultiTouchControls(true);
		loadKmlFiles();

	}

	private void loadKmlFiles() {
		File kmlDir = new File(Environment.getExternalStorageDirectory(), "osm");
		if (!kmlDir.exists()) return;

		File[] kmlFiles = kmlDir.listFiles();
		int i = 0;
		for (File kml :
				kmlFiles) {
			KmlDocument kmlDocument = new KmlDocument();
			kmlDocument.parseKMLFile(kml);
			kmlDocuments.add(kmlDocument);

			FolderOverlay kmlOverlay = (FolderOverlay) kmlDocument.mKmlRoot.buildOverlay(map, null, null, kmlDocument);

			map.getOverlayManager().add(kmlOverlay);

			Button button = new Button(this);
			button.setText(kml.getName());
			button.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			button.setTag(i);
			button.setId(baseId);
			button.setOnClickListener(this);

			layerWrapper.addView(button);
			i++;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.openLayers:
				bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
				break;
			case baseId: {
				int index = (int) v.getTag();
				KmlDocument kmlDocument = kmlDocuments.get(index);
				BoundingBox bb = kmlDocument.mKmlRoot.getBoundingBox();
				map.getController().setZoom(9);
				map.zoomToBoundingBox(bb, true);
				bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
			}
		}
	}
}
