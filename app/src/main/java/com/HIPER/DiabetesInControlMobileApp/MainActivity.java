package com.HIPER.DiabetesInControlMobileApp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements DICClientDelegate, TopicScrollViewDelegate, ArticleViewAdapterDelegate, View.OnClickListener {

    boolean hasLoaded = false;

    // use this to dispatch events to main thread
    Handler mainHandler;

    DrawerLayout drawer;
    ListView menuList;
    MenuListViewAdapter menuListViewAdapter;

    ImageButton favoritesButton;
    ImageButton menuButton;

    TopicScrollView topicScrollView;

    GridView articleGridView;
    ArticleViewAdapter articleViewAdapter;

    boolean loadingArticles = false;
    boolean moreArticles = true;

    String category = "Home";
    int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainHandler = new Handler(Looper.getMainLooper());

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        menuList = (ListView) drawer.findViewById(R.id.menuListView);
        menuListViewAdapter = new MenuListViewAdapter(this);
        menuList.setAdapter(menuListViewAdapter);

        topicScrollView = new TopicScrollView(this, this);

        articleGridView = (GridView) findViewById(R.id.gridView);
        articleGridView.setVerticalSpacing(DICConstants.Tile.vertMargin);
        articleGridView.setHorizontalSpacing(DICConstants.Tile.horizMargin);
        articleViewAdapter = new ArticleViewAdapter(this, this);
        articleGridView.setAdapter(articleViewAdapter);
        articleGridView.setOnItemClickListener(articleViewAdapter);

        menuButton = (ImageButton) findViewById(R.id.menuBarsButton);
        favoritesButton = (ImageButton) findViewById(R.id.menuFavoriteButton);
        menuButton.setOnClickListener(this);
        favoritesButton.setOnClickListener(this);

        // set correct number of cols
        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE) { // tablet
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                articleGridView.setNumColumns(2);
            } else {
                articleGridView.setNumColumns(3);
            }
        } else { // phone
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                articleGridView.setNumColumns(1);
            } else {
                articleGridView.setNumColumns(2);
            }
        }
    }

    void loadArticles(String category, int page) {
        loadingArticles = true;

        this.category = category;
        this.page = page;
        DICClient.getInstance().getArticleFeed(category, page, this);
    }

    //DICClientDelegate
    //region
    public void articlesDidFinishLoading(DICArticle[] articles) {
        if (page == 1) {
            articleViewAdapter.articles = articles;
        } else { // add articles to current articles
            int aLen = articleViewAdapter.articles.length;
            int bLen = articles.length;
            DICArticle[] concated = new DICArticle[aLen + bLen];
            System.arraycopy(articleViewAdapter.articles, 0, concated, 0, aLen);
            System.arraycopy(articles, 0, concated, aLen, bLen);
            articleViewAdapter.articles = concated;
        }
        loadingArticles = false;
        moreArticles = !(articles.length < DICConstants.articles_per_page);

        // reload gridview on main thread
        Runnable reload = new Runnable() {
            @Override
            public void run() {
                articleViewAdapter.notifyDataSetChanged();
            }
        };
        mainHandler.post(reload);
    }
    //endregion
    //TopicScrollViewDelegate
    //region
    public void topicButtonPressed(String name) {
        articleViewAdapter.articles = new DICArticle[0];
        articleViewAdapter.notifyDataSetChanged();

        moreArticles = true;
        this.category = name;

        if (category.equals(DICConstants.URLConvenience.categories[0])) { // favorites
            return;
        }

        loadArticles(name, 1);
    }
    //endregion
    //ArticleViewAdapterDelegate
    //region
    public void articleCellClicked(DICArticle forArticle) {
        Intent startArticleViewActivity = new Intent(this, ArticleViewActivity.class);
        startArticleViewActivity.putExtra("ARTICLE", forArticle);
        startActivityForResult(startArticleViewActivity, 1);
    }

    // returns true iff there are more articles to load
    public Status requestMoreArticles() {
        if (category.equals(DICConstants.URLConvenience.categories[0])) { // favorites
            return Status.NOT_REGISTERED;
        }
        if (!moreArticles) {
            return Status.NO_MORE_ARTICLES;
        }
        if (!loadingArticles) {
            loadArticles(category, page + 1);
        }
        return Status.OK;
    }
    //endregion
    //OnCLickListener

    //region
    public void onClick(View v) {
        if (v == favoritesButton) {
            Toast.makeText(this, R.string.not_registered, Toast.LENGTH_SHORT).show();
        } else if (v == menuButton) {
            drawer.openDrawer(Gravity.LEFT);
        }
    }
    //endregion

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        articleViewAdapter.notifyDataSetChanged();
        super.onActivityResult(requestCode, resultCode, data);
    }
}
