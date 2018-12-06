package com.company.ice.sitegrabber.Presenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.company.ice.sitegrabber.Interfaces.IContentControl;
import com.company.ice.sitegrabber.Interfaces.MainPresentFunction;
import com.company.ice.sitegrabber.Model.DataAboutShortArticle;
import com.company.ice.sitegrabber.Model.HTMLDownloader;
import com.company.ice.sitegrabber.Model.HTMLParser;
import com.company.ice.sitegrabber.Service.NewArticleService;
import com.company.ice.sitegrabber.View.SettingsActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Ice on 13.09.2017.
 */

public class MainPresenter implements MainPresentFunction, SharedPreferences.OnSharedPreferenceChangeListener{
    public final String TAG = "Main";

    private IContentControl view;

    private List<DataAboutShortArticle> mDataList = new ArrayList<>();
    private Context context;
    private SharedPreferences sharedPreferences;

    String siteUrl = "https://dou.ua/lenta/page/";
    private int currentPage;

    HTMLParser htmlParser;

    public MainPresenter(Context context) {
        this.context = context;
        currentPage = 1;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void startFillRecyclerView()
    {
        HTMLDownloader.ObservableDownloadHtml(siteUrl + String.valueOf(currentPage))
                .flatMap(string -> HTMLParser.getObservableHTMLParser(string))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> showArticles(result), error -> Log.e("Main", "Error: " + error.toString() + " || " + Thread.currentThread().getName()));

//        HTMLDownloader htmlDownloader = new HTMLDownloader(this, siteUrl + String.valueOf(currentPage));
//        htmlDownloader.execute();
    }

    private void showArticles(List<DataAboutShortArticle> result){
        if (result != null) {
//            mDataList.clear();
            mDataList.addAll(result);
            if (view != null)
                view.showArticles(mDataList);
            else Log.e("Main", "View is null!");
        }
    }



    @Override
    public void attachView(Object view) {
        this.view = (IContentControl)view;
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void detachView(){
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
        view = null;
        Log.d("Fragment", "Detach view!");
        //cancelDownloadThread();
    }

    private void startService(){
        Intent intent = new Intent(NewArticleService.SERVICE_TAG);
        intent.setPackage(context.getPackageName());
        context.startService(intent);
    }

    private void stopService(){
        Intent intent = new Intent(NewArticleService.SERVICE_TAG);
        intent.setPackage(context.getPackageName());
        context.stopService(intent);
    }

    public void viewIsReady(){
        if (isFirstRun()) startService();


        startFillRecyclerView();
        //startDownloadHTML();
    }
    private boolean isFirstRun(){
        if (sharedPreferences.getBoolean(SettingsActivity.FIRST_RUN, true)){
            sharedPreferences.edit().putBoolean(SettingsActivity.FIRST_RUN, false).commit();
            Toast.makeText(context, "First Application RUN!", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return false;
        }
    }

    public void loadMore(){
        currentPage++;
        startFillRecyclerView();
    }

    public void clickItem(int position){
        view.loadArticle(position, mDataList.get(position).getUrlToFullArticle().toString());
    }

    public void refreshList(){
        currentPage = 1;
        view.deleteAll();
        mDataList.clear();
        startFillRecyclerView();
    }


    void printHtml(String html){
        int maxLogSize = 1000;

        for(int i = 0; i <= html.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i+1) * maxLogSize;
            end = end > html.length() ? html.length() : end;
            Log.d("Main", html.substring(start, end));
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//        Toast.makeText(this, "Change!", Toast.LENGTH_SHORT).show();
        if (key.equals(SettingsActivity.NOTIF_ENABLE)){
            if (sharedPreferences.getBoolean(SettingsActivity.NOTIF_ENABLE, true)){
                startService();
            } else {
                stopService();
            }
        }
    }
}
