package com.hmomeni.osm.interfaces;

import com.hmomeni.osm.objects.KmlObject;

/**
 * Created by hamed on 1/21/17.
 */

public interface KmlFragmentInterface {
	void addKmlObject(KmlObject kmlObject);

	void removeKmlObject(KmlObject kmlObject);
}
