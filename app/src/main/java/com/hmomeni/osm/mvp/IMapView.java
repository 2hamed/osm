package com.hmomeni.osm.mvp;

import com.hmomeni.osm.tools.MapsForgeTileProvider;

import org.osmdroid.util.BoundingBox;

/**
 * Created by hamed on 1/21/17.in osm
 */

public interface IMapView extends BaseView {

	void onMapTileLoaded(BoundingBox boundingBox, MapsForgeTileProvider mapsForgeTileProvider);
}
