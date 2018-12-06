package com.company.ice.sitegrabber.View;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.company.ice.sitegrabber.Interfaces.BaseSwipeToDismissActivity;
import com.company.ice.sitegrabber.Presenter.MainPresenter;
import com.company.ice.sitegrabber.R;
import com.company.ice.sitegrabber.Service.NewArticleService;
import com.orhanobut.logger.AndroidLogAdapter;

import java.util.logging.Logger;

public class MainActivity extends BaseSwipeToDismissActivity {

//    @JavascriptInterface
//    @SuppressWarnings("unused")
//    @Override
//    public void processHTML(String html) {
//        Log.d("HTML", html);
//    }

    ArticlesListFragment articlesListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_main);
//        final WebView webview = new WebView(this);
//        setContentView(webview);
//        webview.getSettings().setJavaScriptEnabled(true);
//        webview.addJavascriptInterface(this, "HTMLOUT");
//        webview.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                view.loadUrl(request.getUrl().toString());
//                return true;
//            }
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                /* This call inject JavaScript into the page which just finished loading. */
//                view.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
//            }
//
//        });
//        webview.loadUrl("https://ebanoe.it/");

        FragmentManager supportFragmentManager  = getSupportFragmentManager();
        articlesListFragment = (ArticlesListFragment) supportFragmentManager.findFragmentById(R.id.fragment_articles_list);
        if (articlesListFragment == null){
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            articlesListFragment = new ArticlesListFragment();
            fragmentTransaction.replace(R.id.fragment_articles_list, articlesListFragment);
            fragmentTransaction.setTransition(android.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragmentTransaction.commit();
        }
    }

    @Override
    public boolean isActivityDraggable() {
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }

}




