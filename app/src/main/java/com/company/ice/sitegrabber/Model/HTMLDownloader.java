package com.company.ice.sitegrabber.Model;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import io.reactivex.Single;

/**
 * Created by Ice on 15.09.2017.
 */

public class HTMLDownloader {

    public static Single<String> ObservableDownloadHtml(String htmlToDownload) {

        Single<String> onlineObservable = Single.create(sub ->
                sub.onSuccess(downloadHtml(htmlToDownload)));
        return onlineObservable;
    }

    public static String downloadHtml(String url) {

        try {
            Log.d("JSwa", "Connecting to [" + url + "]");
            Document doc = Jsoup.connect(url)
                    .followRedirects(true)
                    .userAgent("Mozilla/5.0 (Linux; Android 4.0.4; Galaxy Nexus Build/IMM76B)")
                    .timeout(100000)
                    .get();
            return doc.outerHtml();

        } catch (Throwable t) {
            return null;
        }
    }

}
