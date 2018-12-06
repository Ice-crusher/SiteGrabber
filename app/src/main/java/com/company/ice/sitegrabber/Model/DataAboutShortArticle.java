package com.company.ice.sitegrabber.Model;

import android.graphics.Bitmap;

import java.net.URL;

/**
 * Created by Ice on 13.09.2017.
 */

public class DataAboutShortArticle {

    private String title;
    private String shortDescription;
    private String articleTag;
    private URL urlImageToDownload;
//    private int amountLike;
//    private int amountDislike;
    private int amountViews;
    private URL urlToFullArticle;
    private String author;
    private String time;
    private Bitmap articleImageBitmap;

    public DataAboutShortArticle(String title, String shortDescription, String articleTag, URL urlImageToDownload, int amountViews,
                                 URL urlToFullArticle, String author, String time, Bitmap articleImageBitmap) {
        this.title = title;
        this.shortDescription = shortDescription;
        this.articleTag = articleTag;
        this.urlImageToDownload = urlImageToDownload;
//        this.amountLike = amountLike;
//        this.amountDislike = amountDislike;
        this.amountViews = amountViews;
        this.urlToFullArticle = urlToFullArticle;
        this.author = author;
        this.time = time;
        this.articleImageBitmap = articleImageBitmap;
    }



    public String getTitle() {
        return title;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getArticleTag() {
        return articleTag;
    }

//    public int getAmountLike() {
//        return amountLike;
//    }
//
//    public int getAmountDislike() {
//        return amountDislike;
//    }

    public int getAmountViews() {
        return amountViews;
    }

    public URL getUrlImageToDownload() {
        return urlImageToDownload;
    }

    public URL getUrlToFullArticle() {
        return urlToFullArticle;
    }

    public String getAuthor() {
        return author;
    }

    public String getTime() {
        return time;
    }

    public Bitmap getArticleImageBitmap() {
        return articleImageBitmap;
    }
}
