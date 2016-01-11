package com.chetuan.askforit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.rl01.lib.utils.logger;

import java.util.List;

/**
 * Created by abraham on 2015/8/18.
 */
public class NormalExpandableListAdapter extends BaseExpandableListAdapter {

    protected Context context;

    protected LayoutInflater inflater;

    protected int groupHeaderResource;

    public NormalExpandableListAdapter(Context context, List<Object> datas, int groupHeaderResource){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.groupHeaderResource = groupHeaderResource;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = inflater.inflate(groupHeaderResource, null);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        return convertView;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        try{
        }catch(Exception e){
            logger.e(e);
        }
        return null;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public int getGroupCount() {
        return 0;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}

