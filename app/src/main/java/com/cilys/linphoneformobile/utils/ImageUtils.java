package com.cilys.linphoneformobile.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

public class ImageUtils {

    public static void load(Activity ac, @DrawableRes final int resourceId, final View targetView) {
        Glide.with(ac).load(resourceId).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                Drawable drawable = new BitmapDrawable(resource);
                targetView.setBackground(drawable);
            }
        });
    }

    public static void load(Fragment fg, @DrawableRes final int resourceId, ImageView img) {
        Glide.with(fg).load(resourceId).into(img);
    }
    public static void load(Activity ac, @DrawableRes final int resourceId, ImageView img) {
        Glide.with(ac).load(resourceId).into(img);
    }
}
