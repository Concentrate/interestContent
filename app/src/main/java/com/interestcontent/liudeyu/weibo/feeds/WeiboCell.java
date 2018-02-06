package com.interestcontent.liudeyu.weibo.feeds;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.bumptech.glide.Glide;
import com.interestcontent.liudeyu.R;
import com.interestcontent.liudeyu.base.baseUiKit.aboutRecycleView.BaseRecyclerView;
import com.interestcontent.liudeyu.base.baseUiKit.aboutRecycleView.GridManagerSpaceItemDecoration;
import com.interestcontent.liudeyu.base.constants.WebConstants;
import com.interestcontent.liudeyu.base.specificComponent.BrowseActivity;
import com.interestcontent.liudeyu.weibo.contents.WebContentBrowseFragment;
import com.interestcontent.liudeyu.weibo.contents.WeiboContentActivity;
import com.interestcontent.liudeyu.weibo.data.WeiboLoginManager;
import com.interestcontent.liudeyu.weibo.data.bean.WeiboBean;
import com.interestcontent.liudeyu.weibo.data.bean.WeiboUserBean;
import com.interestcontent.liudeyu.weibo.util.MyWeiboPageUtils;
import com.interestcontent.liudeyu.weibo.util.WeiboUrlsUtils;
import com.luseen.autolinklibrary.AutoLinkMode;
import com.luseen.autolinklibrary.AutoLinkOnClickListener;
import com.luseen.autolinklibrary.AutoLinkTextView;
import com.zhouwei.rvadapterlib.base.RVBaseCell;
import com.zhouwei.rvadapterlib.base.RVBaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import static com.interestcontent.liudeyu.weibo.util.WeiboUrlsUtils.MIDDLE;

/**
 * Created by liudeyu on 2018/1/2.
 */

public class WeiboCell extends RVBaseCell<List<WeiboBean>> implements View.OnClickListener, OnRecycleViewItemClickListener, BaseRecyclerView.BlankListener {

    private int mCurrentPosition;

    private static final String TAG = WeiboCell.class.getSimpleName();

    private Context mContext;
    private android.support.v4.app.Fragment mFragment;

    public WeiboCell(List<WeiboBean> data) {
        super(data);
    }

    public WeiboCell(List<WeiboBean> data, Context context) {
        super(data);
        mContext = context;
    }


    @Override
    public int getItemType() {
        return FeedConstants.FEED_NORMAL_WEIBO_TYPE;
    }

    @Override
    public RVBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case FeedConstants.FEED_NORMAL_WEIBO_TYPE:
                view = LayoutInflater.from(mContext).inflate(R.layout.weibo_feed_cell_layout, parent, false);
                initViewState(view);
                break;
        }
        RVBaseViewHolder viewHolder = new RVBaseViewHolder(view);
        return viewHolder;
    }

    private void initViewState(final View view) {
        AutoLinkTextView autoLinkTextView = (AutoLinkTextView) view.findViewById(R.id.wb_content_tv);
        autoLinkTextView.addAutoLinkMode(
                AutoLinkMode.MODE_HASHTAG,
                AutoLinkMode.MODE_PHONE,
                AutoLinkMode.MODE_URL,
                AutoLinkMode.MODE_MENTION);
        autoLinkTextView.setWeiboEmojiAdd(true);
        autoLinkTextView.setHashtagModeColor(mContext.getResources().getColor(R.color.md_pink_100));
        autoLinkTextView.setPhoneModeColor(mContext.getResources().getColor(R.color.md_green_100));
        autoLinkTextView.setUrlModeColor(mContext.getResources().getColor(R.color.md_light_blue_400));
        autoLinkTextView.setMentionModeColor(mContext.getResources().getColor(R.color.md_deep_purple_100));
        autoLinkTextView.setEmailModeColor(ContextCompat.getColor(mContext, R.color.md_deep_orange_800));
        autoLinkTextView.setAutoLinkOnClickListener(new AutoLinkOnClickListener() {
            @Override
            public void onAutoLinkTextClick(AutoLinkMode autoLinkMode, String matchedText) {
                switch (autoLinkMode) {
                    case MODE_URL:
                        Intent intent = BrowseActivity.getIntent(matchedText, true);
                        intent.putExtra(WebContentBrowseFragment.DEPRECATED_WEB_Class_FUN, WebConstants.DEPRECATED_WEIBO_AD_CLASS);
                        intent.setClass(mContext, BrowseActivity.class);
                        mContext.startActivity(intent);
                        break;

                }
            }
        });
        final BaseRecyclerView recyclerView = view.findViewById(R.id.wb_image_recyle_view);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false));
        view.post(new Runnable() {
            @Override
            public void run() {
                int itemDecortWidth = (int) ((view.getWidth() - mContext.getResources().
                        getDimension(R.dimen.wb_cell_image_size) * 3) / 2);
                recyclerView.addItemDecoration(new GridManagerSpaceItemDecoration(itemDecortWidth, SizeUtils.dp2px(10)));
            }
        });
        WeiboImageRecycleViewAdapter adapter = new WeiboImageRecycleViewAdapter(mContext, new ArrayList<String>());
        adapter.setFragment(mFragment);
        recyclerView.setAdapter(adapter);
        recyclerView.setBlankListener(this);
        LinearLayout goodFinger = view.findViewById(R.id.good_fingger_layout);
        LinearLayout resendLayout = view.findViewById(R.id.resend_layout);
        LinearLayout commentLayout = view.findViewById(R.id.comment_layout);
        OnWeiboOperationBottomClickListener mBottomClickListener = new OnWeiboOperationBottomClickListener(mContext);
        goodFinger.setOnClickListener(mBottomClickListener);
        resendLayout.setOnClickListener(mBottomClickListener);
        commentLayout.setOnClickListener(mBottomClickListener);
        ImageView avaterIv = view.findViewById(R.id.avater_iv);
        TextView authorTv = view.findViewById(R.id.author_tv);

        // TODO: 2018/1/20 这里是需要监听点击的，同时设置下tag,好分辨position
        view.setOnClickListener(this);
        recyclerView.setOnClickListener(this);
        avaterIv.setOnClickListener(this);
        authorTv.setOnClickListener(this);
        autoLinkTextView.setOnClickListener(this);

    }


    public void setFragment(android.support.v4.app.Fragment fragment) {
        mFragment = fragment;
    }

    @Override
    public void onBindViewHolder(RVBaseViewHolder holder, int position) {
        if (position >= mData.size()) {
            return;
        }
        if (holder.getItemViewType() == FeedConstants.FEED_NORMAL_WEIBO_TYPE) {
//            设置下tag,为相同点击处理
            holder.getView(R.id.root_container).setTag(R.layout.weibo_feed_cell_layout, position);
            holder.getView(R.id.wb_image_recyle_view).setTag(R.layout.weibo_feed_cell_layout, position);
            holder.getView(R.id.good_fingger_layout).setTag(R.layout.weibo_feed_cell_layout, mData.get(position).getIdstr());
            holder.getView(R.id.comment_layout).setTag(R.layout.weibo_feed_cell_layout, mData.get(position).getIdstr());
            holder.getView(R.id.resend_layout).setTag(R.layout.weibo_feed_cell_layout, mData.get(position).getIdstr());
            holder.getImageView(R.id.avater_iv).setTag(R.layout.weibo_feed_cell_layout, position);
            holder.getTextView(R.id.author_tv).setTag(R.layout.weibo_feed_cell_layout, position);
            holder.getTextView(R.id.wb_content_tv).setTag(R.layout.weibo_feed_cell_layout, position);

/*todo  以上setTag地方要改，这样容易遗忘，出问题，写法不好，为了快*/

            holder.getTextView(R.id.resend_count_tv).setText(mData.get(position).getReposts_count() + "");
            holder.getTextView(R.id.comment_count_tv).setText(mData.get(position).getComments_count() + "");
            holder.getTextView(R.id.good_fingger_count_tv).setText(mData.get(position).getAttitudes_count() + "");

            AutoLinkTextView autoLinkTextView = (AutoLinkTextView) holder.getTextView(R.id.wb_content_tv);
            autoLinkTextView.setAutoLinkText(mData.get(position).getText());
            holder.getTextView(R.id.create_time_tv).setText(mData.get(position).getCreated_at());
            WeiboUserBean userBean = mData.get(position).getUser();
            if (userBean != null) {
                holder.getTextView(R.id.author_tv).setText(userBean.getName());
                Glide.with(mFragment).load(userBean.getProfile_image_url()).into(holder.getImageView(R.id.avater_iv));
            }
            List<WeiboBean.PicUrlsBean> picUrlsBeans = mData.get(position).getPic_urls();
            RecyclerView recyclerView = (RecyclerView) holder.getView(R.id.wb_image_recyle_view);
            String originPicDomen = WeiboUrlsUtils.getOriginPicHost(mData.get(position).getOriginal_pic());
            if (picUrlsBeans != null && !picUrlsBeans.isEmpty()) {
                int limitPreivewSize = WeiboUrlsUtils.getLimitPreivewSize(picUrlsBeans);
                recyclerView.setVisibility(View.VISIBLE);
                WeiboImageRecycleViewAdapter adapter = (WeiboImageRecycleViewAdapter) recyclerView.getAdapter();
                List<String> urls = WeiboUrlsUtils.getOriginPicUrls(picUrlsBeans, originPicDomen, limitPreivewSize, MIDDLE);
                adapter.setImageUrls(urls);
            } else {
                recyclerView.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void onClick(View view) {
        onItemClick(view, (Integer) view.getTag(R.layout.weibo_feed_cell_layout));
    }


    @Override
    public void onItemClick(View view, int position) {
        switch (view.getId()) {
            case R.id.root_container:
            case R.id.wb_content_tv:
                dealWithGoToSourceWeibo(position);
                break;
            case R.id.avater_iv:
            case R.id.author_tv:
                dealWithGoToAuthorPage(position);
                break;

        }
    }

    private void dealWithGoToAuthorPage(int position) {
        if (mData.get(position).getUser() == null) {
            return;
        }
        String profile = mData.get(position).getUser().getIdstr();
        if (!TextUtils.isEmpty(profile)) {
            MyWeiboPageUtils.getInstance(mContext, WeiboLoginManager.getInstance().getAuthInfo())
                    .startOtherPage(WeiboUrlsUtils.getPersonalProfileUrl(profile));
        }
    }

    private void dealWithGoToSourceWeibo(int position) {
//        MyWeiboPageUtils.getInstance(mContext, WeiboLoginManager.getInstance()
//                .getAuthInfo()).startWeiboDetailPage(mData.get(position).getMid(),
//                mData.get(position).getUser().getIdstr(), true);
        Intent intent = new Intent(mContext, WeiboContentActivity.class);
        intent.putExtra(WeiboContentActivity.WEIBO_ITEM, mData.get(position));
        mContext.startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, int postion) {

    }

    //    搞定了点击ImageRecycle画廊浏览时候，不知道上一级RecycleView Item 位置的问题，牛批!
    @Override
    public void onBlankClick(RecyclerView recyclerView) {
        if (recyclerView.getTag(R.layout.weibo_feed_cell_layout) != null) {
            dealWithGoToSourceWeibo((Integer) recyclerView.getTag(R.layout.weibo_feed_cell_layout));
        }
    }


}
