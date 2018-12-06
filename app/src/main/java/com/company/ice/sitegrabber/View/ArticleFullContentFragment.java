package com.company.ice.sitegrabber.View;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.company.ice.sitegrabber.Model.HTMLDownloader;
import com.company.ice.sitegrabber.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArticleFullContentFragment extends Fragment{

    WebView webView;
    private String articleURL;

    private TextView tvLoading;

    public ArticleFullContentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_article_full_content, container, false);
        tvLoading = (TextView)view.findViewById(R.id.tvLoading);

        webView = view.findViewById(R.id.web_view_content);
        webView.getSettings().setJavaScriptEnabled(true);

//        onlineObservable
        HTMLDownloader.ObservableDownloadHtml(articleURL)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(string -> showArticle(string), error -> Log.e("Main", "Error: " + error.toString() + " || " + Thread.currentThread().getName()));
//        HTMLDownloader htmlDownloader = new HTMLDownloader(this, articleURL);
//        htmlDownloader.execute();

        return view;
    }
    public void setArticleURL(String t) {
        articleURL = t;
    }

    private void showArticle(String html){
        if (html != null) {
            //Log.d("Main", "finishDownloadHTML: " + html);
            final String mimeType = "text/html";
            final String encoding = "UTF-8";
            html = filtringHTML(html);
            webView.loadDataWithBaseURL("", html, mimeType, encoding, "");
//            webView.loadData(html, mimeType, encoding);
        } else
            Log.e("Main", "HTML == NULL");
    }

    private String filtringHTML(String html){
        Document document = Jsoup.parse(html);
        document.select("header.b-head").remove();
        document.select("div.max-header-brand").remove();
        document.select("footer.b-footer").remove();
        document.select("div.cell.g-right-shadowed.mobtab-maincol").select("div.b-articles.b-articles__also").remove();
//        Log.d("Main", document.html());
        return document.html();
    }
}
