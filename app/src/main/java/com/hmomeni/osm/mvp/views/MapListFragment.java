package com.hmomeni.osm.mvp.views;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hmomeni.osm.R;
import com.hmomeni.osm.interfaces.MapListCallback;
import com.hmomeni.osm.interfaces.MapListFragmentInterface;
import com.hmomeni.osm.mvp.IMapListView;
import com.hmomeni.osm.mvp.presenters.MapListPresenter;
import com.hmomeni.osm.tools.Constants;
import com.hmomeni.osm.tools.SimpleRecyclerAdapter;
import com.hmomeni.osm.viewholders.MapItemHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapListFragment extends Fragment implements IMapListView, MapListCallback {
	RecyclerView recyclerView;
	MapListFragmentInterface mapListFragmentInterface;

	MapListPresenter mPresenter;
	List<File> files = new ArrayList<>();
	SimpleRecyclerAdapter<File, MapListCallback> mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_kml_list, container, false);
		recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
		mPresenter = new MapListPresenter();

		mAdapter = new SimpleRecyclerAdapter<>(getContext(), files, MapItemHolder.class, R.layout.rcl_it_map, this);
		recyclerView.setAdapter(mAdapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		return rootView;
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		mapListFragmentInterface = (MapListFragmentInterface) context;
	}

	@Override
	public void onStart() {
		super.onStart();
		mPresenter.attach(this);
		mPresenter.loadMapFiles();
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

	@Override
	public void onNoFileFound() {
		Toast.makeText(getContext(), getString(R.string.no_map_file_found, Constants.MAP_PATH), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onMapFilesLoaded(List<File> files) {
		this.files.addAll(files);
		mAdapter.notifyItemRangeInserted(0, files.size());
	}

	@Override
	public void onItemClicked(int position) {
		mapListFragmentInterface.onMapFileClicked(files.get(position));
	}
}
