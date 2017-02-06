package com.hmomeni.osm.mvp.views;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.hmomeni.osm.R;
import com.hmomeni.osm.interfaces.KmlFragmentInterface;
import com.hmomeni.osm.interfaces.KmlListCallback;
import com.hmomeni.osm.mvp.KmlListView;
import com.hmomeni.osm.mvp.presenters.KmlListPresenter;
import com.hmomeni.osm.objects.KmlObject;
import com.hmomeni.osm.tools.Constants;
import com.hmomeni.osm.tools.SimpleRecyclerAdapter;
import com.hmomeni.osm.viewholders.KmlHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hamed on 1/21/17.in osm
 */

public class KmlListFragment extends Fragment implements KmlListView, KmlListCallback {
	private RecyclerView recyclerView;
	KmlListPresenter mPresenter;
	SimpleRecyclerAdapter<KmlObject, KmlListCallback> mAdapter;
	List<KmlObject> kmlObjects = new ArrayList<>();
	KmlFragmentInterface kmlFragmentInterface;


	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater,
	                         @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_kml_list, container, false);
		initView(rootView);

		mPresenter = new KmlListPresenter();

		mAdapter = new SimpleRecyclerAdapter<>(getContext(), kmlObjects, KmlHolder.class, R.layout.rcl_it_kml, this);

		recyclerView.setAdapter(mAdapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

		return rootView;
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);

		kmlFragmentInterface = (KmlFragmentInterface) context;
	}

	@Override
	public void onStart() {
		super.onStart();
		mPresenter.attach(this);
		mPresenter.loadKmlFiles();
	}

	@Override
	public void onStop() {
		super.onStop();
		mPresenter.detach();
	}

	@Override
	public Context getViewContext() {
		return getContext();
	}

	private void initView(View rootView) {
		recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
	}

	@Override
	public void onKmlFilesLoaded(List<KmlObject> kmlObjects) {
		this.kmlObjects.addAll(kmlObjects);
		mAdapter.notifyItemRangeInserted(0, kmlObjects.size());
	}

	@Override
	public void onNoFileFound() {
		Toast.makeText(getContext(), getString(R.string.no_kml_file_found, Constants.KML_PATH), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onItemClicked(int position) {
		boolean newState = !kmlObjects.get(position).isSelected();
		kmlObjects.get(position).setSelected(newState);
		mAdapter.notifyItemChanged(position);

		// we detect if we are going to add or remove this kmlObject and inform MapActivity
		// to do accordingly

		if (newState) {
			kmlFragmentInterface.addKmlObject(kmlObjects.get(position));
		} else {
			kmlFragmentInterface.removeKmlObject(kmlObjects.get(position));
		}
	}

	@Override
	public void onColorClicked(final int position) {
		ColorPickerDialogBuilder
				.with(getContext())
				.initialColor(kmlObjects.get(position).getLayerColor())
				.setPositiveButton(R.string.pick_color, new ColorPickerClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i, Integer[] integers) {
						KmlObject kmlObject = kmlObjects.get(position);
						kmlObject.setLayerColor(i);
						mAdapter.notifyItemChanged(position);
						kmlFragmentInterface.removeKmlObject(kmlObject);
						kmlFragmentInterface.addKmlObject(kmlObject);
					}
				})
				.wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
				.build().show();
	}
}
