package com.HIPER.DiabetesInControlMobileApp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by connorwatts on 7/31/15.
 */
public class MenuListViewAdapter extends BaseAdapter {

    String[] loggedOutOptions = {"Back to Articles", "Login or Register", "Settings"};
    String[] loggedInOptions = {"Back to Articles", "Manage Favorites", "My Account", "Settings"};

    static HashMap<String, Integer> imagesForMenuItem = new HashMap<String, Integer>();
    static {
        imagesForMenuItem.put("Back to Articles", R.drawable.menu_arrow);
        imagesForMenuItem.put("Login or Register", R.drawable.menu_pencil);
        imagesForMenuItem.put("Manage Favorites", R.drawable.menu_favorites);
        imagesForMenuItem.put("My Account", R.drawable.menu_pencil);
        imagesForMenuItem.put("Settings", R.drawable.menu_cog);
    }

    LayoutInflater inflater;
    Context context;

    public MenuListViewAdapter(Context context) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.menu_item, null);
        }
        ImageView icon = (ImageView) convertView.findViewById(R.id.menuItemImageView);
        TextView text = (TextView) convertView.findViewById(R.id.menuItemTextView);

        text.setText(loggedOutOptions[position]);
        icon.setImageResource(imagesForMenuItem.get(loggedOutOptions[position]));

        return convertView;
    }

    public String getItem(int pos) {
        return loggedOutOptions[pos];
    }

    public long getItemId(int pos) {
        return pos;
    }

    public int getCount() {
        return loggedOutOptions.length;
    }

}
