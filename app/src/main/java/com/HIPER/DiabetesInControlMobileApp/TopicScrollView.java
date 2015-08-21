package com.HIPER.DiabetesInControlMobileApp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import java.util.Arrays;

/**
 * Created by connorwatts on 7/29/15.
 */
public class TopicScrollView implements View.OnClickListener {

    Context context;
    Activity activity;
    TopicScrollViewDelegate delegate;

    LinearLayout layout;
    HorizontalScrollView scrollView;

    Button[] buttons = new Button[DICConstants.URLConvenience.categories.length];
    Button currentlySelectedButton;

    ImageButton leftArrow, rightArrow;

    public TopicScrollView(Activity activity, TopicScrollViewDelegate delegate) {
        context = activity;
        this.activity = activity;
        this.delegate = delegate;

        layout = (LinearLayout) activity.findViewById(R.id.topicScrollView);
        scrollView = (HorizontalScrollView) activity.findViewById(R.id.horizontalScrollView);

        addButtonsToTopicScrollView();

        // add actionlistener to right/left arrows
        leftArrow = (ImageButton) activity.findViewById(R.id.left_arrow);
        rightArrow = (ImageButton) activity.findViewById(R.id.right_arrow);
        leftArrow.setOnClickListener(this);
        rightArrow.setOnClickListener(this);

        // select home button
        currentlySelectedButton = buttons[1];
        selectButton(currentlySelectedButton);
    }

    void addButtonsToTopicScrollView() {

        Typeface buttonFont = Typeface.createFromAsset(activity.getAssets(), DICConstants.fontName);
        scrollView.setHorizontalScrollBarEnabled(false);

        for (int i = 0; i < DICConstants.URLConvenience.categories.length; i++) {
            String category = DICConstants.URLConvenience.categories[i];

            Button btn = new Button(activity);
            btn.setTextColor(Color.parseColor(DICConstants.ScrollView.unselectedColor));
            btn.setBackgroundColor(0xFFFFFF);
            btn.setText(category);
            btn.setTypeface(buttonFont);
            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
            params.setMargins(0, -20, 0, -20);
            btn.setLayoutParams(params);

            buttons[i] = btn;
            layout.addView(btn);

            btn.setOnClickListener(this);
        }
    }

    void selectButton(Button btn) {
        currentlySelectedButton.setTextColor(Color.parseColor(DICConstants.ScrollView.unselectedColor));
        btn.setTextColor(Color.parseColor(DICConstants.ScrollView.selectedColor));

        // scroll to selected button
        scrollView.smoothScrollTo((int) btn.getX() - (scrollView.getWidth() / 2) + (btn.getWidth() / 2), 0);

        currentlySelectedButton = btn;

        leftArrow.setVisibility(View.VISIBLE);
        rightArrow.setVisibility(View.VISIBLE);
        if (currentlySelectedButton == buttons[0]) {
            leftArrow.setVisibility(View.INVISIBLE);
        } else if (currentlySelectedButton == buttons[buttons.length-1]) {
            rightArrow.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        Button btn;

        if (v == rightArrow) {
            btn = buttons[Math.min(Arrays.asList(buttons).indexOf(currentlySelectedButton) + 1, buttons.length - 1)];
        } else if (v == leftArrow) {
            btn = buttons[Math.max(Arrays.asList(buttons).indexOf(currentlySelectedButton) - 1, 0)];
        } else { // topic button
            btn = (Button) v;
        }

        delegate.topicButtonPressed(btn.getText().toString());
        selectButton(btn);
    }

}

interface TopicScrollViewDelegate {
    void topicButtonPressed(String name);
}
