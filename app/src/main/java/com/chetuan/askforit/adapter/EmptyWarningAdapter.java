package com.chetuan.askforit.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * Created by YT on 2015/11/12.
 */
public class EmptyWarningAdapter extends ArrayAdapter{

    private String warning;

    public EmptyWarningAdapter(Context context, String warning) {
        super(context, 0);
        this.warning = warning;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return convertView;
    }
}
