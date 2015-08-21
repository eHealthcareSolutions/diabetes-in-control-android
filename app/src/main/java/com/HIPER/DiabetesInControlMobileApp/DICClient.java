package com.HIPER.DiabetesInControlMobileApp;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

/**
 * Created by connorwatts on 7/29/15.
 */
public class DICClient {

    DICClientDelegate delegate;
    DownloadParseXMLTask currentTask;

    public void getArticleFeed(String category, int page, DICClientDelegate delegate) {
        String urlString = DICConstants.URLConvenience.baseUrl +
                DICConstants.URLConvenience.categoryUrls[Arrays.asList(DICConstants.URLConvenience.categories).indexOf(category)] +
                DICConstants.URLConvenience.feed + DICConstants.URLConvenience.paged + page;
        this.delegate = delegate;

        if (currentTask != null) {
            currentTask.cancel(true);
        }

        currentTask = new DownloadParseXMLTask();
        currentTask.execute(urlString);
    }

    public void getArticleFromURLString(String named, DICClientDelegate delegate) {
        String urlString = named + DICConstants.URLConvenience.feedSingleArticle;
        this.delegate = delegate;

        if (currentTask != null) {
            currentTask.cancel(true);
        }
        
        currentTask = new DownloadParseXMLTask();
        currentTask.execute(urlString);
    }

    private class DownloadParseXMLTask extends AsyncTask<String, Void, Void> {
        protected Void doInBackground(String... urlStrings) {
            URL url;
            URLConnection urlConnection;
            InputStream in;

            try {
                url = new URL(urlStrings[0]);
                urlConnection = url.openConnection();
                in = new BufferedInputStream(urlConnection.getInputStream());

                DICXMLParser parser = new DICXMLParser();
                DICArticle[] articles = parser.parse(in);
                if (!isCancelled() && delegate != null) {
                    delegate.articlesDidFinishLoading(articles);
                }
            } catch (IOException ioe) {
                Log.e("app", "IOException - downloading cancelled");
            } catch (Exception e) {
                Log.e("app", "error", e);
            }
            return null;
        }

    }

    // singleton
    private static DICClient instance = null;
    public static DICClient getInstance() {
        if (instance == null) {
            instance = new DICClient();
        }

        return instance;
    }

}

interface DICClientDelegate {
    void articlesDidFinishLoading(DICArticle[] articles);
}