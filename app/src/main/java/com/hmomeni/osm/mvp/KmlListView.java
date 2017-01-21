package com.hmomeni.osm.mvp;

import com.hmomeni.osm.objects.KmlObject;

import java.util.List;

/**
 * Created by hamed on 1/21/17.in osm
 */

public interface KmlListView extends BaseView {

	void onKmlFilesLoaded(List<KmlObject> kmlObjects);

	void onNoFileFound();
}
