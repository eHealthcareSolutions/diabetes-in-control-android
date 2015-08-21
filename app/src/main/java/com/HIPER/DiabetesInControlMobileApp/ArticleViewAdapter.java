package com.HIPER.DiabetesInControlMobileApp;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by connorwatts on 7/29/15.
 */
public class ArticleViewAdapter extends BaseAdapter implements AdapterView.OnItemClickListener, AdapterView.OnTouchListener {

    Context context;
    LayoutInflater inflater;

    ArticleViewAdapterDelegate delegate;

    Display display;

    DICArticle[] articles = {};

    public ArticleViewAdapter(Activity activity, ArticleViewAdapterDelegate delegate) {
        context = activity;
        this.delegate = delegate;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        if (position == articles.length) { // show loading bar
            TextView messageTextView;
            switch (delegate.requestMoreArticles()) {
                case NO_MORE_ARTICLES:
                    convertView = inflater.inflate(R.layout.message_cell, null);
                    messageTextView = (TextView) convertView.findViewById(R.id.messageTextView);
                    messageTextView.setText(R.string.no_articles);
                    break;
                case NOT_REGISTERED:
                    convertView = inflater.inflate(R.layout.message_cell, null);
                    messageTextView = (TextView) convertView.findViewById(R.id.messageTextView);
                    messageTextView.setText(R.string.not_registered);
                    break;
                case OK:
                    convertView = inflater.inflate(R.layout.loading_cell, null);
                    ProgressBar progress = (ProgressBar) convertView.findViewById(R.id.progressBar);
                    progress.setIndeterminate(true);
                    break;
            }

            setCorrectSize(convertView);
            return convertView;
        }

        if (articles[position].image == null) {
            convertView = inflater.inflate(R.layout.article_view_cell, null);
        } else {
            convertView = inflater.inflate(R.layout.article_view_cell_with_image, null);
            ((ImageView) convertView.findViewById(R.id.articleCellImageView)).setImageBitmap(articles[position].image);
        }

        TextView titleTextView = (TextView) convertView.findViewById(R.id.titleTextView);
        TextView descrTextView = (TextView) convertView.findViewById(R.id.descrTextView);
        Button readMoreButton = (Button) convertView.findViewById(R.id.readMoreButton);

        titleTextView.setText(articles[position].title);
        descrTextView.setText(articles[position].descrWithoutHTML);
        readMoreButton.setTextColor(Color.parseColor(DICConstants.Tile.readMoreUnselectedColor));

        convertView.setTag(articles[position]);

        convertView.setOnTouchListener(this);

        setCorrectSize(convertView);

        return convertView;
    }

    private void setCorrectSize(View v) {
        Point screenSize = new Point();
        display.getSize(screenSize);
        double heightPercentage = (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) ?
                DICConstants.Tile.heightPercentagePortrait : DICConstants.Tile.heightPercentageLandscape;
        v.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, (int) (screenSize.y * heightPercentage)));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Button readMoreButton = (Button) view.findViewById(R.id.readMoreButton);
        if (readMoreButton != null) { // is an article view cell
            readMoreButton.setTextColor(Color.parseColor(DICConstants.Tile.readMoreSelectedColor));
            delegate.articleCellClicked((DICArticle) view.getTag());
        }
    }


    private boolean overFavoriteButton = false;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();

            overFavoriteButton = v.getWidth() - x <= 200 && y <= 200;
        }
        else if (event.getAction() == MotionEvent.ACTION_UP) {
            float x = event.getX();
            float y = event.getY();

            overFavoriteButton = v.getWidth() - x <= 200 && y <= 200 && overFavoriteButton;
            if (overFavoriteButton) { // favorite article
//                ImageView favBtn = (ImageView) v.findViewById(R.id.gridViewFavoriteButton);
//                favBtn.setImageResource(R.drawable.favorite_selected);
                Toast.makeText(context, R.string.not_registered, Toast.LENGTH_SHORT).show();
            }
        }
        return false;
        //return overFavoriteButton; // only consume if user is pressing favorite button
    }

    @Override
    public DICArticle getItem(int pos) {
        return articles[pos];
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public int getCount() {
        return articles.length+1;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

}

interface ArticleViewAdapterDelegate {
    void articleCellClicked(DICArticle forArticle);
    Status requestMoreArticles();

    enum Status {
        OK, NOT_REGISTERED, NO_MORE_ARTICLES
    }
}