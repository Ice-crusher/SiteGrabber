package com.company.ice.sitegrabber.Service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.company.ice.sitegrabber.Model.HTMLDownloader;
import com.company.ice.sitegrabber.Model.HTMLParser;
import com.company.ice.sitegrabber.Model.DataAboutShortArticle;
import com.company.ice.sitegrabber.R;
import com.company.ice.sitegrabber.View.ArticleFullActivity;
import com.orhanobut.logger.CsvFormatStrategy;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Ice on 23.09.2017.
 */

public class NewArticleService extends Service {

    public static final String SERVICE_TAG = "com.company.ice.sitegrabber.service.NewArticleService";
    public static final String ARTICLES_LINK = "https://dou.ua/lenta/page/1/";

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;

    private List<String> articlesSample = new ArrayList<>();

    private final class ServiceHandler extends Handler {


        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
//            Log.d("Thread", Thread.currentThread().toString());
            int i = 0;
            while (true) {
//                if (i == 1)
                start();
//                i++;
                try {
                    Thread.sleep(msg.arg2 * 1000 * 60);
                } catch (InterruptedException e) {
                    Logger.d("FAILED SLEEP, INTERRUPT!");
                    Thread.currentThread().interrupt();
                }
            }
        }

        private void start() {
            // Normally we would do some work here, like download a file.
            try {
                Logger.d("Start download html...");
                HTMLDownloader.ObservableDownloadHtml(ARTICLES_LINK)
                        .flatMap(string -> HTMLParser.getObservableHTMLParser(string))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(result -> checkNewArticle(result));

//                HTMLDownloader htmlDownloader = new HTMLDownloader(this, ARTICLES_LINK);
//                htmlDownloader.execute();

            } catch (Throwable e) {
                // Restore interrupt status.
                Logger.d("FAILED DOWNLOAD HTML, INTERRUPT");
                Thread.currentThread().interrupt();
            }
            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            //stopSelf(msg.arg1);
        }

//        private void doSome(String html){
//            Log.d("Thread", Thread.currentThread().toString());
//            Logger.d("Download html OK");
//            HTMLParser.getObservableHTMLParser(html)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(result -> doSome1(result));
//
////            HTMLParser htmlParser = new HTMLParser(this, html);
////            htmlParser.execute();
//            Logger.d("Start parse html...");
//        }

        private void checkNewArticle(List<DataAboutShortArticle> result){
            Logger.d("Parse html OK");
            if (result.size() <= 0) {
                Logger.d("Result parsing size = 0, RETURNING");
                return;
            }
            List<String> temp = new ArrayList<>();
            for (DataAboutShortArticle data : result) {
                temp.add(data.getTitle());
            }
            Logger.d("Temp list = " + temp.toString().substring(0, 5));
            if (articlesSample.size() != 0) Logger.d("Article sample list = " + articlesSample.toString().substring(0, 5));
//            showNotification("Front-Еnd дайджест #26: Yarn 1.0, потоки в JS, Atom-IDE, начинаем писать на Reason и WebAssembly",
//                    "https://dou.ua/lenta/digests/frontend-digest-26/");
            // IF first run
            if (articlesSample.size() == 0) {
                Logger.d("Article sample list size = 0");
                articlesSample.addAll(temp);
                Logger.d("Add value to Article Sample list...");
                Toast.makeText(NewArticleService.this, "First Run!", Toast.LENGTH_SHORT).show();
                Logger.d("Article sample list = " + articlesSample.toString().substring(0, 5));
            } else if (checkIsNewArticle(temp)) {
                Logger.d("Found new Article!");
                articlesSample.clear();
                articlesSample.addAll(temp);
                Toast.makeText(NewArticleService.this, "New article kurde!", Toast.LENGTH_SHORT).show();
                showNotification(articlesSample.get(0), result.get(0).getUrlToFullArticle().toString());
            } else {
                Logger.d("No new article, let's sleep...");
            }
        }

        private boolean checkIsNewArticle(List<String> temp) {
            if (!(temp.get(0).equals(articlesSample.get(0)))) {
                return true;
            } else {
                return false;
            }
        }

        private void showNotification(String articleName, String urlToFullArticle) {
            Context context = NewArticleService.this;
            String CHANNEL_ID = "my_channel_01";

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
            mBuilder.setSmallIcon(R.drawable.ic_arrow_upward_black_18dp);
            mBuilder.setContentTitle("The new article!");
            mBuilder.setContentText(articleName);
            mBuilder.setChannel(CHANNEL_ID);
            mBuilder.setAutoCancel(true);
            mBuilder.setVibrate(new long[]{100, 500, 300, 500});


            // Creates an explicit intent for an Activity in your app
            Intent resultIntent = new Intent(context, ArticleFullActivity.class);
            resultIntent.putExtra("article_reference", urlToFullArticle);
            // The stack builder object will contain an artificial back stack for the
            // started Activity.
            // This ensures that navigating backward from the Activity leads out of
            // your app to the Home screen.
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            // Adds the back stack for the Intent (but not the Intent itself)
            stackBuilder.addParentStack(ArticleFullActivity.class);
            // Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            // mNotificationId is a unique integer your app uses to identify the
            // notification. For example, to cancel the notification, you can pass its ID
            // number to NotificationManager.cancel().
            mNotificationManager.notify(0, mBuilder.build());
        }
    }

    @Override
    public void onCreate() {
        FormatStrategy formatStrategy = CsvFormatStrategy.newBuilder()
                .tag("custom")
                .build();

        Logger.addLogAdapter(new DiskLogAdapter(formatStrategy));

        Logger.d("\n\nonCreate");
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
        Log.d("Main", "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        Log.d("Main", "onStartCommand");
        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job

        mServiceHandler.removeMessages(startId);

        Logger.d("OnStartCommand");
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        msg.arg2 = 30;
        mServiceHandler.sendMessage(msg);

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        Logger.d("onDestroy");
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }


}
