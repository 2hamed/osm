package com.hmomeni.osm.tools;

import android.os.Environment;

/**
 * Created by hamed on 1/21/17.in osm
 */

public class Constants {
	public static final String BASE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/osm";
	public static final String KML_PATH = BASE_PATH + "/kml";
	public static final String MAP_PATH = BASE_PATH + "/map";
}
