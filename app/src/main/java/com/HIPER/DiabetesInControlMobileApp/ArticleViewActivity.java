package com.HIPER.DiabetesInControlMobileApp;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


public class ArticleViewActivity extends ActionBarActivity implements DICClientDelegate {

    DICArticle article;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_view);

        Bundle extras = getIntent().getExtras();
        article = (DICArticle) extras.getParcelable("ARTICLE");

        webView = (WebView) findViewById(R.id.articleWebView);
        webView.loadData(article.content, "text/html", "utf-8");
        webView.setHorizontalScrollBarEnabled(false);

        // intercept links that go to other DIC articles
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith(DICConstants.URLConvenience.articlesUrl)) {
                    DICClient.getInstance().getArticleFromURLString(url, ArticleViewActivity.this);
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        setTitle(article.title);
    }

    //DICClientDelegate
    //region
    public void articlesDidFinishLoading(DICArticle[] articles) {
        final Intent startArticleViewActivity = new Intent(this, ArticleViewActivity.class);
        startArticleViewActivity.putExtra("ARTICLE", articles[0]);

        // start new article activity on main thread
        Runnable startActivity = new Runnable() {
            @Override
            public void run() {
                startActivity(startArticleViewActivity);
            }
        };
        new Handler(Looper.getMainLooper()).post(startActivity);
    }
    //endregion

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_article_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.menu_favorite) {
            Toast.makeText(this, R.string.not_registered, Toast.LENGTH_SHORT).show();
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
