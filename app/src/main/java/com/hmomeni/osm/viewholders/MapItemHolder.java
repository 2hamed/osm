package com.hmomeni.osm.viewholders;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.hmomeni.osm.R;
import com.hmomeni.osm.interfaces.MapListCallback;
import com.hmomeni.osm.tools.SimpleRecyclerHolder;

import java.io.File;

/**
 * Created by hamed on 1/21/17.
 */

public class MapItemHolder extends SimpleRecyclerHolder<File, MapListCallback> implements View.OnClickListener {
	private TextView title;

	public MapItemHolder(View itemView) {
		super(itemView);
		title = (TextView) itemView.findViewById(R.id.title);
		itemView.setOnClickListener(this);
	}

	@Override
	public void bindView(Context context, File file) {
		title.setText(file.getName());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			default:
			case R.id.title: {
				callback.onItemClicked(getAdapterPosition());
				break;
			}
		}
	}
}
