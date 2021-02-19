package com.cilys.linphoneformobile.ui.call;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cilys.linphoneformobile.R;
import com.cilys.linphoneformobile.base.BaseLinphoneAc;
import com.cilys.linphoneformobile.event.Event;
import com.cilys.linphoneformobile.event.EventImpl;
import com.cilys.linphoneformobile.event.LinPhoneBean;
import com.cilys.linphoneformobile.service.LinphoneService;
import com.cilys.linphoneformobile.utils.LinphoneUtils;
import com.cilys.linphoneformobile.utils.Sp;
import com.cilys.linphoneformobile.utils.TimeUtils;
import com.cilys.linphoneformobile.view.SingleClickListener;

import org.linphone.core.Address;
import org.linphone.core.Call;
import org.linphone.core.CallParams;
import org.linphone.core.Core;
import org.linphone.core.ProxyConfig;

public class PhoneAc extends BaseLinphoneAc {
    public final static int SHOW_TYPE_OUT = 1;      //显示外呼布局
    public final static int SHOW_TYPE_INCOMING = 2; //显示来电布局
    public final static int SHOW_TYPE_CALL = 3;     //显示通话布局

    public final static int FROM_TYPE_CALL_NUMBER = 1;  //从拨号界面来的

    private int showType = SHOW_TYPE_OUT;
    private String outNumber;

    @Override
    protected int getLayout() {
        return R.layout.ac_phone;
    }

    @Override
    protected void initUI() {
        super.initUI();

        showType = getIntent().getIntExtra("SHOW_TYPE", SHOW_TYPE_OUT);
        int fromType = getIntent().getIntExtra("FROM_TYPE", 0);


        initOutView(fromType == FROM_TYPE_CALL_NUMBER);
        initIncomingView();
        initCallView();

        showView(showType);
        changeActionSpeakerBackgound(getSpeakerMode(), showType);
    }

    private void showView(int type) {
        if (type == SHOW_TYPE_INCOMING) {
            getViewFromCache(R.id.ll_out_model).setVisibility(View.GONE);
            getViewFromCache(R.id.ll_incoming_model).setVisibility(View.VISIBLE);
            getViewFromCache(R.id.ll_call_model).setVisibility(View.GONE);
        } else if (type == SHOW_TYPE_CALL) {
            getViewFromCache(R.id.ll_out_model).setVisibility(View.GONE);
            getViewFromCache(R.id.ll_incoming_model).setVisibility(View.GONE);
            getViewFromCache(R.id.ll_call_model).setVisibility(View.VISIBLE);
        } else {
            getViewFromCache(R.id.ll_out_model).setVisibility(View.VISIBLE);
            getViewFromCache(R.id.ll_incoming_model).setVisibility(View.GONE);
            getViewFromCache(R.id.ll_call_model).setVisibility(View.GONE);
        }

        setBackgroundByScreen(getScreenModel());
    }

    private TextView tv_call_time;
    private LinearLayout call_speaker;
    private ImageView call_speaker_img;
    private TextView call_speaker_tv;

    private LinearLayout ll_call_mute;
    private ImageView call_mute_img;
    private TextView call_mute_tv;
    private void initCallView(){
        TextView tv_call_room = findView(R.id.tv_call_room);
        setTextToView(tv_call_room, LinphoneUtils.getDisplayName(getCurrentCall()));

        ImageView img_call_avatar = findView(R.id.img_call_avatar);

        TextView tv_call_custom_name = findView(R.id.tv_call_custom_name);

        tv_call_time = findView(R.id.tv_call_time);

        ll_call_mute = findView(R.id.ll_call_mute);
        ll_call_mute.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                toggleMute(showType);
            }
        });
        call_mute_img = findView(R.id.call_mute_img);
        call_mute_tv = findView(R.id.call_mute_tv);


        call_speaker = findView(R.id.call_speaker);
        call_speaker_img = findView(R.id.call_speaker_img);
        call_speaker_tv = findView(R.id.call_speaker_tv);
        call_speaker.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (getSpeakerMode()) {
                    changeSpeakerToNomal();
                    changeActionSpeakerBackgound(false, showType);
                } else {
                    changeSpeakerToExt();
                    changeActionSpeakerBackgound(true, showType);
                }
            }
        });

        ImageView img_call_end_call = findView(R.id.img_call_end_call);
        img_call_end_call.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                endCall();
            }
        });
    }

    private void initIncomingView(){
        final Call call = getCurrentCall();

        TextView tv_incoming_room = findView(R.id.tv_incoming_room);
        if (call != null) {
            setTextToView(tv_incoming_room, LinphoneUtils.getAddressDisplayName(
                    call.getRemoteAddress()));
        }

        TextView tv_incoming_custom_name = findView(R.id.tv_incoming_custom_name);


        ImageView img_incoming_decline = findView(R.id.img_incoming_decline);
        img_incoming_decline.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                terminateCurrentCallOrConferenceOrAll();
            }
        });

        ImageView img_incoming_call_accept = findView(R.id.img_incoming_call_accept);
        img_incoming_call_accept.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (getLinphoneCore() != null && call != null) {
                    CallParams params = getLinphoneCore().createCallParams(call);
                    params.enableVideo(false);

                    call.acceptWithParams(params);
                }
            }
        });
    }

    public void terminateCurrentCallOrConferenceOrAll() {
        Core core = getLinphoneCore();
        if (core == null) {
            return;
        }
        Call call = core.getCurrentCall();
        if (call != null) {
            call.terminate();
        } else if (core.isInConference()) {
            core.terminateConference();
        } else {
            core.terminateAllCalls();
        }

        finish();
    }

    private TextView tv_out_room;
    private ImageView out_speaker_img;
    private TextView out_speaker_tv;
    private LinearLayout out_speaker;
    private LinearLayout ll_out_mute;
    private ImageView out_mute_img;
    private TextView out_mute_tv;
    private void initOutView(boolean show) {
        tv_out_room = findView(R.id.tv_out_room);

        ImageView img_out_avatar = findView(R.id.img_out_avatar);

        TextView tv_out_custom_name = findView(R.id.tv_out_custom_name);


        ll_out_mute = findView(R.id.ll_out_mute);
        ll_out_mute.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                toggleMute(showType);
            }
        });
        out_mute_img = findView(R.id.out_mute_img);
        out_mute_tv = findView(R.id.out_mute_tv);

        out_speaker_img = findView(R.id.out_speaker_img);
        out_speaker_tv = findView(R.id.out_speaker_tv);
        out_speaker = findView(R.id.out_speaker);
        out_speaker.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (getSpeakerMode()) {
                    changeSpeakerToNomal();
                    changeActionSpeakerBackgound(false, showType);

                } else {
                    changeSpeakerToExt();
                    changeActionSpeakerBackgound(true, showType);
                }
            }
        });

        ImageView img_out_end_call = findView(R.id.img_out_end_call);
        img_out_end_call.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                endCall();
                finish();
            }
        });

        if (show) {
            outNumber = getIntent().getStringExtra(INTENT_CALL_NUMBER);
            if (outNumber == null || outNumber.length() < 1) {
                finish();
            }
            setTextToView(tv_out_room, outNumber);



            call(outNumber);
        }
    }

    private void toggleMute(int showType){
        if (getLinphoneCore() == null) {
            return;
        }
        getLinphoneCore().enableMic(!getLinphoneCore().micEnabled());
        changeActionMicBackgound(!getLinphoneCore().micEnabled(), showType);
    }

    private void changeActionMicBackgound(boolean selected, int show) {
        if (show == SHOW_TYPE_INCOMING) {

        } else if (show == SHOW_TYPE_CALL) {
            if (selected) {
                setBackgoundResource(ll_call_mute, R.drawable.shape_round_for_action_white_bg);
                setTextColor(call_mute_tv, R.color.color_main_text_color);
                setImageResource(call_mute_img, R.drawable.icon_mute_black);
            } else {
                setBackgoundResource(ll_call_mute, R.drawable.shape_round_for_action_trans_bg);
                setTextColor(call_mute_tv, R.color.white);
                setImageResource(call_mute_img, R.drawable.icon_mute_white);
            }
        } else {
            if (selected) {
                setBackgoundResource(ll_out_mute, R.drawable.shape_round_for_action_white_bg);
                setTextColor(out_mute_tv, R.color.color_main_text_color);
                setImageResource(out_mute_img, R.drawable.icon_mute_black);
            } else {
                setBackgoundResource(ll_out_mute, R.drawable.shape_round_for_action_trans_bg);
                setTextColor(out_mute_tv, R.color.white);
                setImageResource(out_mute_img, R.drawable.icon_mute_white);
            }
        }
    }

    /**
     * 改变外放按钮的背景颜色
     * @param speaker
     * @param show
     */
    private void changeActionSpeakerBackgound(boolean speaker, int show) {
        if (show == SHOW_TYPE_INCOMING) {

        } else if (show == SHOW_TYPE_CALL) {
            if (speaker) {
                setBackgoundResource(call_speaker, R.drawable.shape_round_for_action_white_bg);
                setTextColor(call_speaker_tv, R.color.color_main_text_color);
                setImageResource(call_speaker_img, R.drawable.icon_voice_black);
            } else {
                setBackgoundResource(call_speaker, R.drawable.shape_round_for_action_trans_bg);
                setTextColor(call_speaker_tv, R.color.white);
                setImageResource(call_speaker_img, R.drawable.icon_voice_white);
            }
        } else {
            if (speaker) {
                setBackgoundResource(out_speaker, R.drawable.shape_round_for_action_white_bg);
                setTextColor(out_speaker_tv, R.color.color_main_text_color);
                setImageResource(out_speaker_img, R.drawable.icon_voice_black);
            } else {
                setBackgoundResource(out_speaker, R.drawable.shape_round_for_action_trans_bg);
                setTextColor(out_speaker_tv, R.color.white);
                setImageResource(out_speaker_img, R.drawable.icon_voice_white);
            }
        }
    }


    /**
     * 外呼
     * @param phone
     */
    private void call(String phone){
        Core core = getLinphoneCore();

        ProxyConfig cfg = getLinphoneConfig();

        if (cfg == null) {
            //提示未登陆
            showToast("未配置账户");
        } else {
            cfg.setRoute(Sp.getStr(this, "SP_SIP_PROXY_SERVER", "core1-hk.netustay.com"));
            Address addressToCall = core.interpretUrl(phone);
            CallParams params = core.createCallParams(null);

            params.enableVideo(false);

            if (addressToCall != null) {
                addressToCall.setDisplayName(phone);
                core.inviteAddressWithParams(addressToCall, params);
            }
        }
    }

    @Override
    protected void onEvent(Event e) {
        super.onEvent(e);
        if (e.what == EventImpl.CALL_STATE_CHANGED) {
            if (e.obj instanceof LinPhoneBean) {
                LinPhoneBean bean = (LinPhoneBean) e.obj;
                if (bean.getCallState() == Call.State.IncomingReceived) {
                    if (showType == SHOW_TYPE_OUT || showType == SHOW_TYPE_CALL) {
                        //如果是呼叫、接听页面，则不处理被叫请求（原因：测试时发现主叫时，
                        // 不知道是什么原因，还会有收到IncomingReceived的状态，导致页面错乱显示成被叫页面。
                        // 为了避免页面错乱，故不处理正在主叫、以及正在通话时收到的被叫请求
                        return;
                    }

                    showType = SHOW_TYPE_INCOMING;
                    showView(SHOW_TYPE_INCOMING);
                } else if (bean.getCallState() == Call.State.Connected) {
                    if (LinphoneService.getInstance() != null) {
                        LinphoneService.getInstance().setNeedSystemTimer(true);
                    }
                    showType = SHOW_TYPE_CALL;
                    timeCount = 0;
                    changeActionSpeakerBackgound(getSpeakerMode(), showType);
                    changeActionMicBackgound(!getLinphoneCore().micEnabled(), showType);
                    showView(SHOW_TYPE_CALL);
                } else if (bean.getCallState() == Call.State.End || bean.getCallState() == Call.State.Released) {
                    if (LinphoneService.getInstance() != null) {
                        LinphoneService.getInstance().setNeedSystemTimer(false);
                    }

                    finish();
                }
            }
        } else if (e.what == EventImpl.SYSTEM_TIMER) {
            timeCount ++;

            setTextToView(tv_call_time, TimeUtils.fomcatTimeToSecond(timeCount));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (getViewFromCache(R.id.root) != null) {
            getViewFromCache(R.id.root).setKeepScreenOn(true);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (getViewFromCache(R.id.root) != null) {
            getViewFromCache(R.id.root).setKeepScreenOn(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private long timeCount = -1;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null) {
            if (outNumber != null) {
                outState.putString("OUT_NUMBER", outNumber);
            }
            if (timeCount > -1) {
                outState.putLong("CALL_TIME", timeCount);
            }
            outState.putInt("SHOW_TYPE", showType);

            outState.putBoolean("SPEAKER_ON", getSpeakerMode());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            String number = savedInstanceState.getString("OUT_NUMBER", null);
            if (number != null) {
                setTextToView(tv_out_room, number);
            }
            long time = savedInstanceState.getLong("CALL_TIME", -1L);
            if (time > -1) {
                setTextToView(tv_call_time, TimeUtils.fomcatTimeToSecond(time));
            }

            int show = savedInstanceState.getInt("SHOW_TYPE", SHOW_TYPE_OUT);
            showView(show);

            boolean speaker = savedInstanceState.getBoolean("SPEAKER_ON", false);
            if (speaker) {
                changeSpeakerToExt();
            } else {
                changeSpeakerToNomal();
            }

            changeActionSpeakerBackgound(speaker, show);
        }
    }

    private void setBackgroundByScreen(int screen) {
        setBackgroundById(R.id.root, R.mipmap.ic_phone_bg);

//        if (showType == SHOW_TYPE_INCOMING) {
//            setBackgroundById(R.id.ll_incoming_model, R.mipmap.ic_call_bg);
//        } else if (showType == SHOW_TYPE_CALL) {
//            setBackgroundById(R.id.ll_call_model, R.mipmap.ic_call_bg);
//        } else {
//            setBackgroundById(R.id.ll_out_model, R.mipmap.ic_call_bg);
//        }
    }
}