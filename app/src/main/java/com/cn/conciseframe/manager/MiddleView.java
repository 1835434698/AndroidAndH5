package com.cn.conciseframe.manager;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


public class MiddleView {

	private Map<String, BaseView> storeCacheView = new HashMap<String, BaseView>();
	private LinkedList<String> storeHistory = new LinkedList<String>();
	
	private LinearLayout linearLayout;
	private BaseView currentView;
	private Map<String, BaseView> cacheView = new HashMap<String, BaseView>();
	private LinkedList<String> history = new LinkedList<String>();
	
	private static MiddleView middleView = new MiddleView();
	private MiddleView() {}

	public static MiddleView getInstance(){
		return middleView;
	}
	
	public void setMiddleContainer(LinearLayout l){
		this.linearLayout = l;
	}
	
	private Context getContext(){
		return this.linearLayout.getContext();
	}
	
	public BaseView getCurrentView(){
		return currentView;
	}

	private void changeView(Class<? extends BaseView> clazz, Bundle bundle){
		if (currentView != null) {
			if (currentView.getClass() == clazz) {
				linearLayout.removeAllViews();
				linearLayout.addView(currentView.getView());
				currentView.onResume();
				currentView.onBundle();
				return ;
			}
			currentView.onPause();
		}
		String key = clazz.getSimpleName();
		BaseView targetView = null;
		if (cacheView.containsKey(key)) {
			history.remove(key);
			history.addFirst(key);
			targetView = cacheView.get(key);
			targetView.setBundle(bundle);
		}else {
			try {
				Constructor<? extends BaseView> constructor = clazz.getConstructor(Context.class, Bundle.class);
				targetView = constructor.newInstance(getContext(), bundle);
				history.addFirst(key);
				cacheView.put(key, targetView);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (targetView == null) {
			return;
		}
		View view = targetView.getView();
		linearLayout.removeAllViews();
		linearLayout.addView(view);
		targetView.onResume();
		currentView = targetView;
		
	}

	/**
	 * 跳转界面，并且清理之前的任务栈
	 * @param clazz
	 * @param bundle
     */
	public void startCleanActivity(Class<? extends BaseView> clazz, Bundle bundle){
		String first;
		BaseView firstBase;
		while (!history.isEmpty()){
			first = history.getFirst();
			firstBase = cacheView.get(first);
			firstBase.onPause();
			currentView.onDestory();
			cacheView.remove(first);
			history.removeFirst();
		}
		currentView = null;
		changeView(clazz, bundle);
	}


	/**
	 * 跳转界面，不清理之前任务栈
	 * @param clazz
	 * @param bundle
     */
	public void startCallbackActivity(Class<? extends BaseView> clazz, Bundle bundle){
		changeView(clazz, bundle);
	}

	/**
	 * 跳转界面，并且清理目标到目前的任务栈
	 * @param clazz 目前
	 * @param bundle
	 * @param clazzStart 目标
     */
	public void startFromAssignedActivity(Class<? extends BaseView> clazz, Bundle bundle, Class<? extends BaseView> clazzStart){
		boolean b = false;
		String first;
		while(!b){
			first = history.getFirst();
			if (cacheView.get(first).getClass().equals(clazzStart)){
				b = true;
				changeView(clazz, bundle);
			}else {
				cacheView.remove(first);
				history.removeFirst();
				currentView.onPause();
				currentView.onDestory();
				currentView = cacheView.get(history.getFirst());
			}
		}
	}

	/**
	 * 返回上一个Page
	 * @return
     */
	public boolean goBack(){

		if (history.size() != 0) {
			if (history.size() == 1) {
				return false;
			}
			
			String first = history.getFirst();
			BaseView firstBase = cacheView.get(first);
			firstBase.onPause();
			cacheView.remove(first);
			history.removeFirst();
			
			String key = history.getFirst();
			
			BaseView targetView = cacheView.get(key);
			View view = targetView.getView();
			if (firstBase != null) {
				firstBase.onDestory();
				// FadeUtil.fadeOut(firstBase.getview(), 800);
			}
			linearLayout.removeAllViews();
			linearLayout.addView(view);
			targetView.onResume();
			// FadeUtil.fadeIn(view, 800, 0);
			currentView = targetView;
			return true;
		}
		return false;
	}

	
}
