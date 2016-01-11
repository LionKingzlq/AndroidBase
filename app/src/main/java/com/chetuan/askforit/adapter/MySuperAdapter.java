package com.chetuan.askforit.adapter;

import android.content.Context;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chetuan.askforit.util.ImgUtil;
import com.chetuan.askforit.widget.MyTextView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by abraham on 2015/12/22.
 */
public class MySuperAdapter extends BaseAdapter {

    /**
     * Created by abraham on 2015/12/29
     */
    private List<List<Object>> data;
    private int layout;
    private int[] to;
    private Context ctx;

    public MySuperAdapter(Context ctx, List<List<Object>> data, int layout, int[] to) {
        this.ctx = ctx;
        this.data = data;
        this.layout = layout;
        this.to = to;
    }

    @Override
    public int getCount() {
        if (data == null || data.size() == 0) {
            return 0;
        }
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = View.inflate(ctx, layout, null);
        }

        List<Object> rowData = data.get(position);

        for (int i = 0; i < to.length; i++) {
            View view = ViewHolder.get(convertView,to[i]);
            if (view instanceof TextView) {
                if(rowData.get(i) instanceof Spanned)
                    ((TextView)view).setText((Spanned)rowData.get(i));
                else
                    ((TextView)view).setText(rowData.get(i).toString());
            }else if(view instanceof MyTextView){
                if(rowData.get(i) instanceof Spanned)
                    ((MyTextView)view).setText((Spanned)rowData.get(i));
                else
                    ((MyTextView)view).setText(rowData.get(i).toString());
            }
            else if (view instanceof SimpleDraweeView){
                new ImgUtil().load(rowData.get(i).toString(), (SimpleDraweeView) view);
            }
        }
        return convertView;
    }
}
