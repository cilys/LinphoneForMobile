package com.cilys.linphoneformobile.base;

import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cilys.linphoneformobile.ui.call.PhoneAc;
import com.cilys.linphoneformobile.event.Event;
import com.cilys.linphoneformobile.event.EventBus;
import com.cilys.linphoneformobile.event.EventImpl;
import com.cilys.linphoneformobile.utils.L;
import com.cilys.linphoneformobile.utils.ToastUtils;

public class BaseAc extends AppCompatActivity {
    protected final String TAG = this.getClass().getSimpleName();

    protected <V extends View> V findView(@IdRes int id){
        try{
            return (V)findViewById(id);
        }catch (ClassCastException e){
            L.e(TAG, "Could not cast View to concrete class");
            return null;
        }
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (eventImpl == null) {
            eventImpl = new EventImpl() {
                @Override
                public void onTrigger(Event event) {
                    if (event.what == EventImpl.CLOSE_APP) {
                        finish();
                    } else {
                        onEvent(event);
                    }
                }
            };
        }
        EventBus.getInstance().onSub(eventImpl);
    }
    protected void onEvent(Event e) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (eventImpl != null) {
            EventBus.getInstance().unSub(eventImpl);
        }

        changeSpeakerToNomal();
    }

    private EventImpl eventImpl;

    protected void showToast(String str){
        ToastUtils.show(this, str);
    }

    protected void setTextToView(TextView v, String text) {
        if (v == null) {
            return;
        }
        if (text == null) {
            text = "";
        }
        v.setText(text);
    }

    protected void setTextColor(TextView tv, @ColorRes int colorId){
        if (tv != null) {
            tv.setTextColor(getResources().getColor(colorId));
        }
    }

    protected void setBackgoundResource(View v, @DrawableRes int resourceId){
        if (v != null) {
            v.setBackgroundResource(resourceId);
        }
    }

    protected void setImageResource(ImageView img, @DrawableRes int resourceId){
        if (img != null){
            img.setImageResource(resourceId);
        }
    }

    @Override
    public void onBackPressed() {
        if (this instanceof PhoneAc) {
            //不响应返回按键事件
        } else {
            super.onBackPressed();
        }
    }
    /**
     * 切换到外放
     */
    protected void changeSpeakerToExt(){
        AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            am.setMode(AudioManager.MODE_IN_COMMUNICATION);
        } else {
            am.setMode(AudioManager.MODE_IN_CALL);
//            am.setMode(AudioManager.MODE_NORMAL);
        }

        am.setSpeakerphoneOn(true);
    }



    /**
     * 恢复到默认模式
     */
    protected void changeSpeakerToNomal(){
        AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

        am.setMode(AudioManager.MODE_NORMAL);

        am.setSpeakerphoneOn(false);
    }

    protected boolean getSpeakerMode(){
        AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        return am.isSpeakerphoneOn();
    }
}
