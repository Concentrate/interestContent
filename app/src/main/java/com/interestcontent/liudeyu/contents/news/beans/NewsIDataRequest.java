package com.interestcontent.liudeyu.contents.news.beans;

import com.interestcontent.liudeyu.base.baseBeans.BaseRequest;

import java.util.List;

/**
 * Created by liudeyu on 2018/2/4.
 */

public class NewsIDataRequest extends BaseRequest {

    /**
     * data:[]
     * dataType : news
     * hasNext : true
     * retcode : 000000
     * pageToken : 2
     * appCode : kr36
     */

    private String dataType;
    private boolean hasNext;
    private String retcode;
    private String pageToken;
    private String appCode;
    private List<NewsIDataApiBean> data;

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public String getRetcode() {
        return retcode;
    }

    public void setRetcode(String retcode) {
        this.retcode = retcode;
    }

    public String getPageToken() {
        return pageToken;
    }

    public void setPageToken(String pageToken) {
        this.pageToken = pageToken;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public List<NewsIDataApiBean> getData() {
        return data;
    }

    public void setData(List<NewsIDataApiBean> data) {
        this.data = data;
    }

}