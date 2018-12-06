package com.company.ice.sitegrabber.View;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.company.ice.sitegrabber.Interfaces.BaseSwipeToDismissActivity;
import com.company.ice.sitegrabber.R;

public class ArticleFullActivity extends BaseSwipeToDismissActivity {

    ArticleFullContentFragment articleFullContentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_full);

        FragmentManager supportFragmentManager  = getSupportFragmentManager();
        articleFullContentFragment = (ArticleFullContentFragment) supportFragmentManager.findFragmentById(R.id.fragment_article_full_content);
        if (articleFullContentFragment == null){
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            articleFullContentFragment = new ArticleFullContentFragment();
            articleFullContentFragment.setArguments(getIntent().getExtras());

            fragmentTransaction.replace(R.id.fragment_article_full_content, articleFullContentFragment);
            fragmentTransaction.setTransition(android.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragmentTransaction.commit();
        }

        articleFullContentFragment.setArticleURL(getArticleURL());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return true;
    }

    @Override
    public boolean isActivityDraggable() {
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }



    @Override
    public int getLayoutId() {
        return R.layout.activity_article_full;
    }

    public String getArticleURL(){
        return getIntent().getExtras().getString("article_reference");
    }
}
