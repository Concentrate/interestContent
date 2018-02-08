package com.interestcontent.liudeyu.contents.zhihu.cells;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.interestcontent.liudeyu.R;
import com.interestcontent.liudeyu.base.baseUiKit.aboutGlide.GlideRoundTransform;
import com.interestcontent.liudeyu.contents.weibo.feeds.FeedConstants;
import com.interestcontent.liudeyu.contents.zhihu.bean.ZhihuJournayListRequest;
import com.zhouwei.rvadapterlib.base.RVBaseCell;
import com.zhouwei.rvadapterlib.base.RVBaseViewHolder;

import java.util.List;

/**
 * Created by liudeyu on 2018/2/8.
 */

public class ZhihuItemCell extends RVBaseCell<List<ZhihuJournayListRequest.StoriesBean>> {
    private Activity mActivity;
    private android.support.v4.app.Fragment mFragment;

    public ZhihuItemCell(List<ZhihuJournayListRequest.StoriesBean> storiesBeans, android.support.v4.app.Fragment fragment) {
        super(storiesBeans);
        mFragment = fragment;
        mActivity = mFragment.getActivity();
    }

    @Override
    public int getItemType() {
        return FeedConstants.OPINION_ZHIHU_CELL_TYPE;
    }

    @Override
    public RVBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.zhihu_feed_cell, parent, false);
        return new RVBaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RVBaseViewHolder holder, int position) {
        if (position >= mData.size()) {
            return;
        }
        if (mData.get(position).getImages().isEmpty()) {
            holder.getImageView(R.id.image_iv).setVisibility(View.GONE);
        }
        Glide.with(mFragment).load(mData.get(position).getImages().get(0)).transform(
                new GlideRoundTransform(mActivity, 5)
        ).into(holder.getImageView(R.id.image_iv));
        holder.getTextView(R.id.title).setText(mData.get(position).getTitle());
    }

}
