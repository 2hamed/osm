package com.hmomeni.osm.objects;

import java.io.File;

/**
 * Created by hamed on 1/21/17.in osm
 */

public class KmlObject {
	private File kmlFile;
	private int layerColor;

	public KmlObject(File kmlFile, int layerColor) {
		this.kmlFile = kmlFile;
		this.layerColor = layerColor;
	}

	public File getKmlFile() {
		return kmlFile;
	}

	public void setKmlFile(File kmlFile) {
		this.kmlFile = kmlFile;
	}

	public int getLayerColor() {
		return layerColor;
	}

	public void setLayerColor(int layerColor) {
		this.layerColor = layerColor;
	}
}
