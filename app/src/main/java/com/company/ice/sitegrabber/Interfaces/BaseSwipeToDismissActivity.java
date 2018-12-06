package com.company.ice.sitegrabber.Interfaces;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.company.ice.sitegrabber.R;
import com.r0adkll.slidr.Slidr;

/**
 * Created by Ice on 28.09.2017.
 */

public abstract class BaseSwipeToDismissActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (isActivityDraggable())
                Slidr.attach(this);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
        if (isActivityDraggable())
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
//
    }


    public void startActivity(Intent intent){
        super.startActivity(intent);
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }






    public abstract int getLayoutId();
    public abstract boolean isActivityDraggable();
}
