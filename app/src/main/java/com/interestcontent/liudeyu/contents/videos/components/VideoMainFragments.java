package com.interestcontent.liudeyu.contents.videos.components;

import com.interestcontent.liudeyu.base.baseComponent.AbsTopTabFragment;
import com.interestcontent.liudeyu.base.tabs.ItemTab;
import com.interestcontent.liudeyu.contents.videos.VideoPlayManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liudeyu on 2018/2/12.
 */

public class VideoMainFragments extends AbsTopTabFragment {
    @Override
    protected List<ItemTab> provideItemTabs() {
        int[] itemTabKeys = new int[]{
                ItemTab.VIDEO_RECOMEND_TAB, ItemTab.VIDEO_HOT_TAB
        };
        String[] names = new String[]{
                "推荐", "热门"
        };
        List<ItemTab> itemTabs = new ArrayList<>();
        for (int i = 0; i < itemTabKeys.length; i++) {
            itemTabs.add(new ItemTab(itemTabKeys[i], names[i]));
        }
        return itemTabs;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            VideoPlayManager.getInstance().onDestoryVideoPlayView();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        VideoPlayManager.getInstance().onDestoryVideoPlayView();
    }
}
