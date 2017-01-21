package com.hmomeni.osm.mvp;

import java.io.File;
import java.util.List;

/**
 * Created by hamed on 1/21/17.
 */

public interface IMapListView extends BaseView {
	void onNoFileFound();

	void onMapFilesLoaded(List<File> files);
}
