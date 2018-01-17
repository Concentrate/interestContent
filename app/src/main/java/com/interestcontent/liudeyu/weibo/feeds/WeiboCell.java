package com.interestcontent.liudeyu.weibo.feeds;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.bumptech.glide.Glide;
import com.interestcontent.liudeyu.R;
import com.interestcontent.liudeyu.base.baseUiKit.aboutRecycleView.SpaceItemDecoration;
import com.interestcontent.liudeyu.base.constants.Constants;
import com.interestcontent.liudeyu.base.constants.SpConstants;
import com.interestcontent.liudeyu.base.specificComponent.BrowseActivity;
import com.interestcontent.liudeyu.base.utils.Logger;
import com.interestcontent.liudeyu.base.utils.SharePreferenceUtil;
import com.interestcontent.liudeyu.weibo.data.bean.WeiboRequest;
import com.interestcontent.liudeyu.weibo.data.bean.WeiboUserBean;
import com.luseen.autolinklibrary.AutoLinkMode;
import com.luseen.autolinklibrary.AutoLinkOnClickListener;
import com.luseen.autolinklibrary.AutoLinkTextView;
import com.zhouwei.rvadapterlib.base.RVBaseCell;
import com.zhouwei.rvadapterlib.base.RVBaseViewHolder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liudeyu on 2018/1/2.
 */

public class WeiboCell extends RVBaseCell<List<WeiboRequest.StatusesBean>> implements View.OnClickListener, OnRecycleViewItemClickListener {
    public static final String MIDDLE = "bmiddle";
    public static final String ORIGIN = "large";
    public static final String SMALL = "thumbnail";

    private static final String TAG = WeiboCell.class.getSimpleName();

    private Context mContext;

    public WeiboCell(List<WeiboRequest.StatusesBean> data) {
        super(data);
    }

    public WeiboCell(List<WeiboRequest.StatusesBean> data, Context context) {
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

    private void initViewState(View view) {
        AutoLinkTextView autoLinkTextView = (AutoLinkTextView) view.findViewById(R.id.wb_content_tv);
        autoLinkTextView.addAutoLinkMode(
                AutoLinkMode.MODE_HASHTAG,
                AutoLinkMode.MODE_PHONE,
                AutoLinkMode.MODE_URL,
                AutoLinkMode.MODE_MENTION);
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
                        BrowseActivity.start(mContext, matchedText);
                        break;

                }
            }
        });
        RecyclerView recyclerView = view.findViewById(R.id.wb_image_recyle_view);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        int itemDecortWidth = (int) ((ScreenUtils.getScreenWidth() - mContext.getResources().
                getDimension(R.dimen.wb_cell_image_size) * 3) / 6);
        recyclerView.addItemDecoration(new SpaceItemDecoration(itemDecortWidth, SizeUtils.dp2px(10)));
        recyclerView.setAdapter(new WeiboImageRecycleViewAdapter(mContext, new ArrayList<String>()));
    }


    @Override
    public void onBindViewHolder(RVBaseViewHolder holder, int position) {
        if (holder.getItemViewType() == FeedConstants.FEED_NORMAL_WEIBO_TYPE) {
            holder.getView(R.id.root_container).setTag(R.id.root_container, position);
            holder.getView(R.id.root_container).setOnClickListener(this);
            AutoLinkTextView autoLinkTextView = (AutoLinkTextView) holder.getTextView(R.id.wb_content_tv);
            autoLinkTextView.setAutoLinkText(mData.get(position).getText());
            holder.getTextView(R.id.create_time_tv).setText(mData.get(position).getCreated_at());
            WeiboUserBean userBean = mData.get(position).getUser();
            if (userBean != null) {
                holder.getTextView(R.id.author_tv).setText(userBean.getName());
                Glide.with(mContext).load(userBean.getProfile_image_url()).into(holder.getImageView(R.id.avater_iv));
            }
            List<WeiboRequest.StatusesBean.PicUrlsBean> picUrlsBeans = mData.get(position).getPic_urls();
            RecyclerView recyclerView = (RecyclerView) holder.getView(R.id.wb_image_recyle_view);
            String originPicDomen = getOriginPicHost(position);
            if (picUrlsBeans != null && !picUrlsBeans.isEmpty()) {
                int limitPreivewSize = getLimitPreivewSize(picUrlsBeans);
                recyclerView.setVisibility(View.VISIBLE);
                WeiboImageRecycleViewAdapter adapter = (WeiboImageRecycleViewAdapter) recyclerView.getAdapter();
                List<String> urls = getOriginPicUrls(picUrlsBeans, originPicDomen, limitPreivewSize, MIDDLE);
                adapter.setImageUrls(urls);
            } else {
                recyclerView.setVisibility(View.GONE);
            }
        }

    }

    @NonNull
    private String getOriginPicHost(int position) {
        String originPicDomen = "";
        if (!TextUtils.isEmpty(mData.get(position).getOriginal_pic())) {
            try {
                URL url = new URL(mData.get(position).getOriginal_pic());
                originPicDomen = url.getProtocol() + "://" + url.getHost();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return originPicDomen;
    }

    @NonNull
    private List<String> getOriginPicUrls(List<WeiboRequest.StatusesBean.PicUrlsBean> picUrlsBeans, String originPicDomen, int limitPreivewSize
            , String imageScaleTag) {
        List<String> urls = new ArrayList<>();
        for (int i = 0; i < limitPreivewSize; i++) {
            String picRequestUrl = "";
            if (!TextUtils.isEmpty(originPicDomen)) {
                try {
//                            通过观察链接得到的原图链接，官方api没提供
                    URL url = new URL(picUrlsBeans.get(i).getThumbnail_pic());
                    String jpgName = url.getFile().substring(url.getFile().indexOf("thumbnail/") + "thumbnail/".length());
                    picRequestUrl = originPicDomen + "/" + imageScaleTag + "/" + jpgName;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
            Logger.d(TAG, "request origin pic url is " + picRequestUrl);
            urls.add(!TextUtils.isEmpty(picRequestUrl) ? picRequestUrl : mData.get(i).getThumbnail_pic());

        }
        return urls;
    }

    private int getLimitPreivewSize(List<WeiboRequest.StatusesBean.PicUrlsBean> picUrlsBeans) {
        int limitPreivewSize = picUrlsBeans.size();
        return limitPreivewSize > 6 ? 6 : limitPreivewSize;
    }

    @Override
    public void onClick(View view) {


    }


    @Override
    public void onItemClick(View view, int position) {
        switch (view.getId()) {
            case R.id.root_container:
                dealWithGoToSourceWeibo(position);
                break;
        }
    }

    private void dealWithGoToSourceWeibo(int position) {
        if (!TextUtils.isEmpty(mData.get(position).getSource())) {
            Document document = Jsoup.parse(mData.get(position).getSource());
            Elements elements = document.getElementsByAttribute("a");
            if (elements != null && !elements.isEmpty()) {
                String originUrl = elements.get(0).attr("href");
                Logger.d(TAG, "origin url is " + originUrl);
                String requestUrl = originUrl + "?" + Constants.WB_REQUEST_PARAMETER.ACCESS_TOKEN + SharePreferenceUtil.getStringPreference(mContext,
                        SpConstants.WEIBO_AUTHEN_TOKEN) + "&" + Constants.WB_REQUEST_PARAMETER.UID + mData.get(position).getUser().getIdstr()
                        + "&" + Constants.WB_REQUEST_PARAMETER.ID + mData.get(position).getIdstr();
                Logger.d(TAG, "rquest url is " + requestUrl);
                BrowseActivity.start(mContext, requestUrl);
            }
        }
    }

    @Override
    public void onItemLongClick(View view, int postion) {

    }
}
