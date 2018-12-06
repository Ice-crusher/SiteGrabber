package com.company.ice.sitegrabber.Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

/**
 * Created by Ice on 13.09.2017.
 */

public class HTMLParser{

    public static Single<List<DataAboutShortArticle>> getObservableHTMLParser(String html){
        Single<List<DataAboutShortArticle>> onlineObservable = Single.create(sub ->
                sub.onSuccess(parseHtml(html)));
        return onlineObservable;
    }

    private static List<DataAboutShortArticle> parseHtml(String html){
        List<DataAboutShortArticle> listData = new ArrayList<>();

        try {
            Document doc = Jsoup.parse(html);
            // Get topics
            Elements topicList = doc.select("div.b-lenta").first().children();
            Log.d("Main", "Amount articles: " + topicList.size());
            for (Element topic : topicList) {

                String articleTitle = topic.select("a").first().text();
                String shortDescription = topic.getElementsByClass("b-typo").first().text();
                String articleTag = topic.getElementsByClass("more").first().text();
                articleTag = articleTag.replace("Ссылки · ", "");
                String urlImageToDownload = topic.getElementsByClass("title").first().select("img").first().attr("src");
//                int amountLike = Integer.valueOf(topic.getElementsByClass("vortex-container-like").first().text());
//                int amountDislike = Integer.valueOf(topic.getElementsByClass("vortex-container-dislike").first().text());
                int amountViews = Integer.valueOf(topic.getElementsByClass("b-info").first().getElementsByClass("pageviews").text().replaceAll("\\s",""));
                String urlToFullArticle = topic.select("a").first().attr("href");
                String author = topic.getElementsByClass("b-info").first().getElementsByClass("author").text();
                String time  = topic.getElementsByClass("b-info").first().getElementsByClass("date").text();
//                Log.d("Main", String.valueOf(time));
//                Bitmap articleImageBitmap = getBitmapFromURL(urlImageToDownload);//, 500, 300);
                Bitmap articleImageBitmap = null;
//                //Thread.sleep(1000);
//                Log.d("Main", " Finish to DOWNLOAD: " + urlImageToDownload);
//                Log.d("Main", articleImageBitmap.toString());

//                Log.d("Main", author + time);
//                Log.d("Main", "Article Title: " + articleTitle);
//                Log.d("Main", "Short Short Description: " + shortDescription);
//                Log.d("Main", "Url To Full Article: " + urlToFullArticle);
                listData.add(new DataAboutShortArticle(articleTitle, shortDescription,
                        articleTag, normalizeURLProtocol(urlImageToDownload), amountViews, normalizeURLProtocol(urlToFullArticle), author, time, articleImageBitmap));
            }
        } catch (Throwable t) {
            if (t.getClass().equals(HttpStatusException.class)){
                Log.d("Main", "BLOCKED!!1 THREAD = " + Thread.currentThread());
            }
            Log.e("Main", "ParseURL_ERROR: " + t);
            return null;
        }

        return listData;
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public Bitmap getBitmapFromURL(String url1){//, int reqWidth, int reqHeight) {
        try {
            //url1 = "http://www.naturalnews.com/wp-content/uploads/sites/91/2017/04/Gardening-Tools-2.jpg";
            Log.d("Main", "Start, URL to DOWNLOAD: " + url1);
            URL url = new URL(url1);//"http://image10.bizrate-images.com/resize?sq=60&uid=2216744464");
            //Bitmap myBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//            java.net.URL url = new java.net.URL(url1);
//            HttpURLConnection connection = (HttpURLConnection) url
//                    .openConnection();
//            connection.setDoInput(true);
//            connection.connect();
//            InputStream input = connection.getInputStream();
//
//            final BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inJustDecodeBounds = true;
//            BitmapFactory.decodeStream(input, null, options);
//
//            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
//            options.inJustDecodeBounds = false;
//
//            Bitmap myBitmap = BitmapFactory.decodeStream(input, null, options);
            //Log.d("Main", "Bitmap byte is: " + String.valueOf(myBitmap.getRowBytes() * myBitmap.getHeight()));
            return null;
        } catch (IOException e) {
            Log.e("Main", "Unknown url: " + e);
            return null;
        }
    }

    private static URL normalizeURLProtocol(String s) throws MalformedURLException{
        if (s.contains("https")){
            s.replace("https", "http");
        }

        try {
            URL nUrl = new URL(s);
            return nUrl;
        } catch (MalformedURLException e){
            return null;
        }

    }

}
