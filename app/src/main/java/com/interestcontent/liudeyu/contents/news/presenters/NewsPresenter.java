package com.interestcontent.liudeyu.contents.news.presenters;

import com.example.commonlib.utils.Logger;
import com.interestcontent.liudeyu.base.constants.FeedConstants;
import com.interestcontent.liudeyu.base.constants.LoggerConstants;
import com.interestcontent.liudeyu.base.dataManager.FeedDataManager;
import com.interestcontent.liudeyu.base.mvp.IMvpView;
import com.interestcontent.liudeyu.base.mvp.MvpPresenter;
import com.interestcontent.liudeyu.contents.news.beans.NewsApiBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liudeyu on 2018/2/4.
 */

public class NewsPresenter extends MvpPresenter<List<NewsApiBean>, IMvpView<List<NewsApiBean>>> {

    @Override
    public List<NewsApiBean> doWork(Object... params) throws Exception {
        if (params == null || params.length < 3) {
            throw new IllegalArgumentException("wrong argument num");
        }
        String url = (String) params[0];
        int itemTab = (int) params[1];
        FeedConstants.FEED_REQUEST_EMUM type = (FeedConstants.FEED_REQUEST_EMUM) params[2];
        Logger.d(LoggerConstants.RELEASE_DATA_PROBLEM,"url is :"+url);
        switch (type) {
            case FIRST_FLUSH:
                return FeedDataManager.getInstance().getNewsTechListAtFirst(itemTab, url);
            case REFLASH:
                return FeedDataManager.getInstance().getNewsTechListByNet(itemTab, url, true);
            case NORMAL_BY_NET:
                return FeedDataManager.getInstance().getNewsTechListByNet(itemTab, url, false);
            default:
                return new ArrayList<NewsApiBean>();
        }

    }
}
