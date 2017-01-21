package com.hmomeni.osm.mvp.presenters;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;

import com.hmomeni.osm.mvp.BasePresenter;
import com.hmomeni.osm.mvp.IMapView;
import com.hmomeni.osm.tools.MapsForgeTileProvider;
import com.hmomeni.osm.tools.MapsForgeTileSource;

import org.osmdroid.tileprovider.IRegisterReceiver;
import org.osmdroid.tileprovider.MapTile;
import org.osmdroid.tileprovider.modules.IFilesystemCache;
import org.osmdroid.tileprovider.tilesource.ITileSource;

import java.io.File;
import java.io.InputStream;

/**
 * Created by hamed on 1/21/17.in osm
 */

public class MapPresenter extends BasePresenter<IMapView> implements IRegisterReceiver,
                                                                     IFilesystemCache {
	public void loadMapFile(File mapFile) {
		MapsForgeTileSource.createInstance((Application) getView().getViewContext().getApplicationContext());
		MapsForgeTileSource mapsForgeTileSource = MapsForgeTileSource.createFromFiles(new File[]{mapFile});
		MapsForgeTileProvider mapsForgeTileProvider = new MapsForgeTileProvider(this, mapsForgeTileSource, this);
		if (isAttached()) {
			getView().onMapTileLoaded(mapsForgeTileSource.getBoundsOsmdroid(), mapsForgeTileProvider);
		}

	}

	@Override
	public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
		return null;
	}

	@Override
	public void unregisterReceiver(BroadcastReceiver receiver) {

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
