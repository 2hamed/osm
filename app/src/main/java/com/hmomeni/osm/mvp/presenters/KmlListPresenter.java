package com.hmomeni.osm.mvp.presenters;

import android.os.Handler;

import com.hmomeni.osm.mvp.BasePresenter;
import com.hmomeni.osm.mvp.KmlListView;
import com.hmomeni.osm.objects.KmlObject;
import com.hmomeni.osm.tools.Constants;
import com.hmomeni.osm.tools.Util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hamed on 1/21/17.in osm
 */

public class KmlListPresenter extends BasePresenter<KmlListView> {
	private Handler handler = new Handler();

	public void loadKmlFiles() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				File kmlPath = new File(Constants.KML_PATH);
				if (!kmlPath.exists()) {
					//noinspection ResultOfMethodCallIgnored
					kmlPath.mkdirs();

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


				final List<KmlObject> kmlObjects = new ArrayList<>();
				for (File file :
						kmlPath.listFiles(getFilenameFilter())) {
					kmlObjects.add(new KmlObject(file, Util.generateRandomColor()));
				}

				if (isAttached()) {
					handler.post(new Runnable() {
						@Override
						public void run() {
							getView().onKmlFilesLoaded(kmlObjects);
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
				return name.endsWith(".kml");
			}
		};
	}
}
