package com.interestcontent.liudeyu.base.constants;

import com.interestcontent.liudeyu.weibo.data.WeiboLoginManager;

/**
 * Created by liudeyu on 2017/12/30.
 */

public class Constants {

//    weibo相关
    /**
     * 当前 DEMO 应用的 APP_KEY，第三方应用应该使用自己的 APP_KEY 替换该 APP_KEY
     */
    public static final String APP_KEY = "547766077";

    /**
     * 当前 DEMO 应用的回调页，第三方应用可以使用自己的回调页。
     * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
     */
    public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";

    /**
     * WeiboSDKDemo 应用对应的权限，第三方开发者一般不需要这么多，可直接设置成空即可。
     * 详情请查看 Demo 中对应的注释。
     */
    public static final String SCOPE =
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";

    public static final String WEIBO_DOMAIN = "https://api.weibo.com/2";
    public static final String HOME_WEIBO_FOLLOW = WEIBO_DOMAIN + "/statuses/home_timeline.json";
    public static final String WEIBO_GO_WEB_ORIGIN = "http://api.weibo.com/2/statuses/go";
    public static final String WEIBO_GOOD_ATTITUDE = "https://api.weibo.com/2/attitudes/create.json"; // 点赞 api post
    public static final String WEIBO_NEGETIVE_ATTITUDE = "https://api.weibo.com/2/attitudes/destroy.json"; // 取消点赞 post
    public static final String WEIBO_SINGLE_CONTENT = WEIBO_DOMAIN + "/statuses/show.json";
    public static final String WEIBO_PERSONNAL_PROFILE = "https://www.weibo.com/";
    public static final String WEIBO_MY_PERSON_PAGE = "http://m.weibo.cn/u/" + WeiboLoginManager.getInstance().getUid()+"?";
    public static final String WEIBO_MY_PROFILE_SETTING_PAGE = "http://m.weibo.cn/index/router?";
    public static final String WEIBO_COMMON_API="https://api.weibo.com/2/statuses/public_timeline.json";

    public static class WB_REQUEST_PARAMETER {
        public static final String ACCESS_TOKEN = "access_token";
        public static final String SINGLE_PAGE_COUNT = "count";
        public static final String PAGE = "page";
        public static final String FEATURE = "feature"; //过滤类型ID，0：全部、1：原创、2：图片、3：视频、4：音乐，默认为0。
        public static final String TRIM_USER = "trim_user"; //返回值中user字段开关，0：返回完整user字段、1：user字段仅返回user_id，默认为0。
        public static final String UID = "uid";
        public static final String ID = "id";
        public static final String ATTITUDE = "attitude"; // 点赞相关参数
    }
}

