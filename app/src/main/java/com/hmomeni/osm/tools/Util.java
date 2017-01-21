package com.hmomeni.osm.tools;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

/**
 * Created by hamed on 1/13/17.
 */

public class Util {

	public static void copyAssets(Context context) {
		AssetManager assetManager = context.getAssets();
		File outDir = new File(Environment.getExternalStorageDirectory(), "osm");
		if (!outDir.exists()) {
			outDir.mkdirs();
		}
		String[] files = null;
		try {
			files = assetManager.list("");
		} catch (IOException e) {
			Log.e("tag", "Failed to get asset file list.", e);
		}
		if (files != null) {
			for (String filename : files) {
				InputStream in = null;
				OutputStream out = null;
				try {
					in = assetManager.open(filename);
					File outFile = new File(outDir, filename);
					out = new FileOutputStream(outFile);
					copyFile(in, out);
				} catch (IOException e) {
					Log.e("tag", "Failed to copy asset file: " + filename, e);
				} finally {
					if (in != null) {
						try {
							in.close();
						} catch (IOException e) {
							// NOOP
						}
					}
					if (out != null) {
						try {
							out.close();
						} catch (IOException e) {
							// NOOP
						}
					}
				}
			}
		}
	}

	private static void copyFile(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
	}

	static int[] colors = new int[]{
			Color.parseColor("#ff1744"),
			Color.parseColor("#d50000"),
			Color.parseColor("#f06292"),
			Color.parseColor("#c2185b"),
			Color.parseColor("#c51162"),
			Color.parseColor("#ba68c8"),
			Color.parseColor("#ff5722"),
			Color.parseColor("#ffc107"),
			Color.parseColor("#69f0ae"),

	};

	public static int generateRandomColor() {
		return colors[randInt(0, colors.length - 1)];
	}

	static Random rand = new Random();

	public static int randInt(int min, int max) {
		return rand.nextInt((max - min) + 1) + min;
	}
}
