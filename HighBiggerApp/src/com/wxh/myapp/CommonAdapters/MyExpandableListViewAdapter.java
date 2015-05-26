package com.wxh.myapp.CommonAdapters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wxh.myapp.R;

public class MyExpandableListViewAdapter extends BaseExpandableListAdapter {

	Context mContext;
	List<Map<String, Object>> groupItemList;
	List<List<Map<String, Object>>> childItemList;

	public MyExpandableListViewAdapter(Context context,
			ArrayList<Map<String, Object>> groupList,
			List<List<Map<String, Object>>> childList) {
		this.mContext = context;
		this.groupItemList = groupList;
		this.childItemList = childList;
	}

	public void refresh(ArrayList<List<Map<String, Object>>> list) {
		this.childItemList = list;
		notifyDataSetChanged();
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return childItemList.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.expandableview_child, null);
		}
		TextView tv_child = (TextView) view.findViewById(R.id.tv_childName);
		String[] childItems = (String[]) getItems((Map<String, Object>) getChild(
				groupPosition, childPosition));
		tv_child.setText(childItems[0]);

		return view;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return childItemList.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return groupItemList.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return groupItemList.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.expandableview_group, null);
		}
		TextView tv_group = (TextView) view.findViewById(R.id.tv_groupName);
		ImageView iv_arrow = (ImageView) view.findViewById(R.id.iv_arrow);

		String[] groupItems = (String[]) getItems((Map<String, Object>) getGroup(groupPosition));
		tv_group.setText(groupItems[0]);
		if (isExpanded) {
			iv_arrow.setBackgroundResource(R.drawable.btn_browser2);
		} else {
			iv_arrow.setBackgroundResource(R.drawable.btn_browser);
		}

		return view;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	private Object[] getItems(Map<String, Object> groupMap) {
		int index = 0;
		String[] groupItems = new String[] {};

		for (Entry<String, Object> entry : groupMap.entrySet()) {
			String value = (String) entry.getValue();
			groupItems[index] = value;
			index++;
		}

		return groupItems;
	}

}
