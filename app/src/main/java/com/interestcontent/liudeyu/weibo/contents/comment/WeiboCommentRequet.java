package com.interestcontent.liudeyu.weibo.contents.comment;

import com.google.gson.annotations.SerializedName;
import com.interestcontent.liudeyu.weibo.data.bean.WeiboCommontBean;

import java.util.List;

/**
 * Created by liudeyu on 2018/1/31.
 */

public class WeiboCommentRequet {
    @SerializedName(value = "total_number")
    private int totalNumber;
    @SerializedName(value = "comments")
    private List<WeiboCommontBean>mWeiboCommontBeans;

    public int getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(int totalNumber) {
        this.totalNumber = totalNumber;
    }

    public List<WeiboCommontBean> getWeiboCommontBeans() {
        return mWeiboCommontBeans;
    }

    public void setWeiboCommontBeans(List<WeiboCommontBean> weiboCommontBeans) {
        mWeiboCommontBeans = weiboCommontBeans;
    }
}
