package com.chetuan.askforit.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YT on 2015/9/6.
 */
public class NormalPageAdapter<T> extends PagerAdapter implements ViewPager.OnPageChangeListener {

    protected Context context;

    protected List<T> datas;

    private List<View> viewList;

    private ViewPager viewPager;

    public boolean showPageIndex = true;

    public NormalPageAdapter(ViewPager viewPager, List<T> picUrls)
    {
        this.context = viewPager.getContext();
        this.viewPager = viewPager;
        this.datas = picUrls;
        this.viewList = new ArrayList<View>();
        int size = datas.size();
        for (int i = 0; i < size; i ++)
        {
            viewList.add(getView(i));
        }
        viewPager.setOnPageChangeListener(this);
        viewPager.setAdapter(this);
        viewPager.setCurrentItem(0);
    }

    public NormalPageAdapter(ViewPager viewPager, List<T> picUrls, int radius, int divide, int marginBottom, int fontColor, int backColor)
    {
        this.context = viewPager.getContext();
        this.viewPager = viewPager;
        this.datas = picUrls;
        this.viewList = new ArrayList<View>();
        int size = datas.size();
        for (int i = 0; i < size; i ++)
        {
            viewList.add(getView(i));
        }
        viewPager.setOnPageChangeListener(this);
        viewPager.setAdapter(this);
        viewPager.setCurrentItem(0);
    }

    @Override
    public void notifyDataSetChanged() {
        viewPager.removeAllViews();
        viewList.clear();
        int size = datas.size();
        for (int i = 0; i < size; i ++)
        {
            viewList.add(getView(i));
        }
        super.notifyDataSetChanged();
        viewPager.setCurrentItem(viewList.size() > 1?1: 0);
    }

    protected View getView(int position)
    {
        return new View(context);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public int getCount() {
        return viewList.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position,
                            Object object) {
        container.removeView(viewList.get(position));
    }

    @Override
    public int getItemPosition(Object object) {
        if (((View)object).getParent() == null) {
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if(viewList.get(position).getParent() == null)
        {
            container.addView(viewList.get(position));
        }
        return viewList.get(position);
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
