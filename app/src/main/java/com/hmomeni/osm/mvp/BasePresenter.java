package com.hmomeni.osm.mvp;

import android.support.annotation.CallSuper;

import java.lang.ref.WeakReference;

/**
 * Created by hamed on 1/21/17.in osm
 */

public abstract class BasePresenter<IView extends BaseView> {
	private WeakReference<IView> mView;

	@CallSuper
	public void attach(IView IView) {
		mView = new WeakReference<>(IView);
	}

	@CallSuper
	public void detach() {
		mView.clear();
		mView = null;
	}

	protected boolean isAttached() {
		return mView != null && mView.get() != null;
	}

	protected IView getView() {
		return mView.get();
	}


}
