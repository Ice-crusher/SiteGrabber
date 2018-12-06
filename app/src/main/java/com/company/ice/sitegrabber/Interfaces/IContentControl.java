package com.company.ice.sitegrabber.Interfaces;

import com.company.ice.sitegrabber.Model.DataAboutShortArticle;

import java.util.List;

/**
 * Created by Ice on 14.09.2017.
 */

public interface IContentControl {
    void showArticles(List<DataAboutShortArticle> dataList);
    void updateArticles();
    void deleteAll();
    void loadArticle(int position, String articleReference);
}
