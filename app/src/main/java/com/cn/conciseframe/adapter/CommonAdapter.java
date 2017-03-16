package com.cn.conciseframe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class CommonAdapter<T> extends BaseAdapter
{
	protected LayoutInflater mInflater;
	protected Context mContext;
	protected List<T> mDatas;
	protected final int mItemLayoutId;

	/**
	 * 继承自 BaseAdapter 的一个通用 Adapter
	 * @param context 用于显示所在位置的上下文对象
	 * @param mDatas 用于填充的数据集合
	 * @param itemLayoutId 条目的布局文件
	 */
	public CommonAdapter(Context context, List<T> mDatas, int itemLayoutId)
	{
		this.mContext = context;
		this.mInflater = LayoutInflater.from(mContext);
		this.mDatas = mDatas;
		this.mItemLayoutId = itemLayoutId;
	}

	@Override
	public int getCount()
	{
		return mDatas.size();
	}

	@Override
	public T getItem(int position)
	{
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		final ViewHolder viewHolder = getViewHolder(position, convertView,parent);
		convert(viewHolder, getItem(position));
		View myConvertView = viewHolder.getConvertView();
		return myConvertView;

	}

	/**
	 * 将数据集合中的数据填入到布局中。
	 * @param helper 持有 ConvertView 和每个控件对象的 ViewHolder 对象
	 * @param item 每个条目索要填充的数据对象
	 */
	public abstract void convert(ViewHolder helper, T item);

	private ViewHolder getViewHolder(int position, View convertView,
			ViewGroup parent)
	{
		return ViewHolder.get(mContext, convertView, parent, mItemLayoutId,
				position);
	}

}
