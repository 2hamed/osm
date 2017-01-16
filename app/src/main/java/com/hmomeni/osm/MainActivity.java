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
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.hmomeni.osm.tools.MapsForgeTileProvider;
import com.hmomeni.osm.tools.MapsForgeTileSource;

import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.IRegisterReceiver;
import org.osmdroid.tileprovider.MapTile;
import org.osmdroid.tileprovider.modules.IFilesystemCache;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
                                                               IRegisterReceiver, IFilesystemCache {
	private static final String TAG = "MainActivity";
	FrameLayout mapWrapper;
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
		mapWrapper = (FrameLayout) findViewById(R.id.mapWrapper);
		bottomSheet = (NestedScrollView) findViewById(R.id.bottomSheet);
		layerWrapper = (LinearLayout) findViewById(R.id.layerWrapper);
		bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
		openLayers = (Button) findViewById(R.id.openLayers);
		openLayers.setOnClickListener(this);

		Util.copyAssets(this);
		MapsForgeTileSource.createInstance(getApplication());
		MapsForgeTileSource mapsForgeTileSource = MapsForgeTileSource.createFromFiles(new File[]{new File(Environment.getExternalStorageDirectory() + "/iran.map")});
		MapsForgeTileProvider mapsForgeTileProvider = new MapsForgeTileProvider(this, mapsForgeTileSource, this);
		map = new MapView(this, mapsForgeTileProvider);

		mapWrapper.addView(map);
		map.setTileSource(TileSourceFactory.MAPNIK);

//		map.setBuiltInZoomControls(true);
		map.setMultiTouchControls(true);
		loadKmlFiles();


	}

	private void loadKmlFiles() {
		File kmlDir = new File(Environment.getExternalStorageDirectory(), "osm");
		if (!kmlDir.exists()) {
			return;
		}

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
			//noinspection ResourceType
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

	@Override
	public void destroy() {

	}

	@Override
	public boolean saveFile(ITileSource pTileSourceInfo, MapTile pTile, InputStream pStream) {
		return false;
	}

	@Override
	public boolean exists(ITileSource pTileSourceInfo, MapTile pTile) {
		return false;
	}

	@Override
	public void onDetach() {

	}

	@Override
	public boolean remove(ITileSource tileSource, MapTile tile) {
		return false;
	}
}
