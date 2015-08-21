package com.HIPER.DiabetesInControlMobileApp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.util.Log;

import java.io.Serializable;
import java.net.URL;
import java.text.AttributedString;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by connorwatts on 7/29/15.
 */
public class DICArticle implements Parcelable {

    // article data
    String title;
    String link;
    String category;
    String descr;
    String descrWithoutHTML;
    String content;
    Bitmap image;

    public DICArticle(String title, String link, String category, String descr, String content) {
        this.title = title;
        this.link = link;
        this.category = category;
        this.descr = descr;
        this.descrWithoutHTML = Html.fromHtml(descr).toString().replace('\n', (char) 32)
                .replace((char) 160, (char) 32).replace((char) 65532, (char) 32).trim();
        this.content = content;

        // get image
        Pattern p = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
        Matcher m = p.matcher(descr);
        if (m.find()) {
            String src = m.group(1);
            try {
                URL url = new URL(src);
                image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (Exception e) {
                Log.e("app", "error", e);
            }
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(title);
        out.writeString(link);
        out.writeString(category);
        out.writeString(descr);
        out.writeString(descrWithoutHTML);
        out.writeString(content);
        //out.writeParcelable(image, 0);
    }

    public static final Parcelable.Creator<DICArticle> CREATOR
            = new Parcelable.Creator<DICArticle>() {
        public DICArticle createFromParcel(Parcel in) {
            return new DICArticle(in);
        }

        public DICArticle[] newArray(int size) {
            return new DICArticle[size];
        }
    };

    private DICArticle(Parcel in) {
        title = in.readString();
        link = in.readString();
        category = in.readString();
        descr = in.readString();
        descrWithoutHTML = in.readString();
        content = in.readString();
        //image
    }
}
