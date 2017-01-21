package com.hmomeni.osm.mvp.presenters;

import android.os.Handler;

import com.hmomeni.osm.mvp.BasePresenter;
import com.hmomeni.osm.mvp.IMapListView;
import com.hmomeni.osm.tools.Constants;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hamed on 1/21/17.
 */

public class MapListPresenter extends BasePresenter<IMapListView> {
	private Handler handler = new Handler();

	public void loadMapFiles() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				File mapPath = new File(Constants.MAP_PATH);
				if (!mapPath.exists()) {
					mapPath.mkdirs();

					if (isAttached()) {
						handler.post(new Runnable() {
							@Override
							public void run() {
								getView().onNoFileFound();
							}
						});
					}
					return;
				}

				final List<File> files = Arrays.asList(mapPath.listFiles(getFilenameFilter()));
				if (isAttached()) {
					handler.post(new Runnable() {
						@Override
						public void run() {
							getView().onMapFilesLoaded(files);
						}
					});
				}
			}
		}).start();
	}

	private FilenameFilter getFilenameFilter() {
		return new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".map");
			}
		};
	}
}
