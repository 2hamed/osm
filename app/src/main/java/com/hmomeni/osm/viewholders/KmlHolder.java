package com.hmomeni.osm.viewholders;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.hmomeni.osm.R;
import com.hmomeni.osm.interfaces.KmlListCallback;
import com.hmomeni.osm.objects.KmlObject;
import com.hmomeni.osm.tools.SimpleRecyclerHolder;

/**
 * Created by hamed on 1/21/17.in osm
 */

public class KmlHolder extends SimpleRecyclerHolder<KmlObject, KmlListCallback> {
	private TextView title;
	private View color;

	public KmlHolder(View itemView) {
		super(itemView);
		title = (TextView) itemView.findViewById(R.id.title);
		color = itemView.findViewById(R.id.color);
		itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				callback.onItemClicked(getAdapterPosition());
			}
		});
	}

	@Override
	public void bindView(Context context, KmlObject kmlObject) {
		title.setText(kmlObject.getKmlFile().getName());
		color.setBackgroundColor(kmlObject.getLayerColor());
	}
}
