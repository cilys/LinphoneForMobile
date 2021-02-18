package com.cilys.linphoneformobile.ui.call;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.cilys.linphoneformobile.AccountAc;
import com.cilys.linphoneformobile.R;
import com.cilys.linphoneformobile.base.BaseLinphoneAc;
import com.cilys.linphoneformobile.service.LinphoneService;
import com.cilys.linphoneformobile.view.SingleClickListener;


/**
 * 拨号界面
 */
public class CallNumberAc extends BaseLinphoneAc {
    private final int TIME_INVAL = 10;
    private TextView tv_show_number;

    @Override
    protected int getLayout() {
        return R.layout.ac_call_number;
    }

    @Override
    protected void initUI(){
        super.initUI();

        tv_show_number = findView(R.id.tv_show_number);

        findView(R.id.ll_number_1).setOnClickListener(new SingleClickListener(TIME_INVAL) {
            @Override
            public void onSingleClick(View v) {
                tv_show_number.append("1");
            }
        });
        findView(R.id.ll_number_2).setOnClickListener(new SingleClickListener(TIME_INVAL) {
            @Override
            public void onSingleClick(View v) {
                tv_show_number.append("2");
            }
        });
        findView(R.id.ll_number_3).setOnClickListener(new SingleClickListener(TIME_INVAL) {
            @Override
            public void onSingleClick(View v) {
                tv_show_number.append("3");
            }
        });
        findView(R.id.ll_number_4).setOnClickListener(new SingleClickListener(TIME_INVAL) {
            @Override
            public void onSingleClick(View v) {
                tv_show_number.append("4");
            }
        });
        findView(R.id.ll_number_5).setOnClickListener(new SingleClickListener(TIME_INVAL) {
            @Override
            public void onSingleClick(View v) {
                tv_show_number.append("5");
            }
        });
        findView(R.id.ll_number_6).setOnClickListener(new SingleClickListener(TIME_INVAL) {
            @Override
            public void onSingleClick(View v) {
                tv_show_number.append("6");
            }
        });
        findView(R.id.ll_number_7).setOnClickListener(new SingleClickListener(TIME_INVAL) {
            @Override
            public void onSingleClick(View v) {
                tv_show_number.append("7");
            }
        });
        findView(R.id.ll_number_8).setOnClickListener(new SingleClickListener(TIME_INVAL) {
            @Override
            public void onSingleClick(View v) {
                tv_show_number.append("8");
            }
        });
        findView(R.id.ll_number_9).setOnClickListener(new SingleClickListener(TIME_INVAL) {
            @Override
            public void onSingleClick(View v) {
                tv_show_number.append("9");
            }
        });
        findView(R.id.ll_number_0).setOnClickListener(new SingleClickListener(TIME_INVAL) {
            @Override
            public void onSingleClick(View v) {
                tv_show_number.append("0");
            }
        });

        findView(R.id.ll_number_star).setOnClickListener(new SingleClickListener(TIME_INVAL) {
            @Override
            public void onSingleClick(View v) {
                tv_show_number.append("*");
            }
        });

        findView(R.id.ll_number_shape).setOnClickListener(new SingleClickListener(TIME_INVAL) {
            @Override
            public void onSingleClick(View v) {
                tv_show_number.append("#");
            }
        });

        findView(R.id.ll_number_add).setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                tv_show_number.append("+");
            }
        });

        findView(R.id.ll_del).setOnClickListener(new SingleClickListener(TIME_INVAL) {
            @Override
            public void onSingleClick(View v) {
                String s = tv_show_number.getText().toString();
                if (s != null && s.length() > 0) {
                    s = s.substring(0, s.length() - 1);
                    tv_show_number.setText(s);
                }
            }
        });
        findView(R.id.img_call).setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                String phone = tv_show_number.getText().toString().trim();

                toCall(phone);
            }
        });

        findView(R.id.service_center_button).setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                setTextToView(tv_show_number, "0");
                toCall("0");
            }
        });


        setBackgroundById(R.id.root, R.mipmap.ic_call_bg);
        setBackgroundById(R.id.ll_number_dialog, R.mipmap.ic_call_dialog_bg);
    }

    private void toCall(String phone) {
        if (phone == null || phone.length() < 1) {
            showToast("请输入号码");
            return;
        }

        if (getLinphoneConfig() == null) {
            showToast("请配置账户信息");
            startActivity(new Intent(CallNumberAc.this, AccountAc.class));

            return;
        }

        Intent i = new Intent(CallNumberAc.this, PhoneAc.class);
        i.putExtra(INTENT_CALL_NUMBER, phone);
        i.putExtra("FROM_TYPE", PhoneAc.FROM_TYPE_CALL_NUMBER);

        i.putExtra("SHOW_TYPE", PhoneAc.SHOW_TYPE_OUT);
        startActivity(i);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (tv_show_number != null) {
            outState.putString("SHOW_CALL_NUMBER", tv_show_number.getText().toString());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            String show_call_number = savedInstanceState.getString("SHOW_CALL_NUMBER", "");
            setTextToView(tv_show_number, show_call_number);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        requestCameraPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 11) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestRecordPermission();
            } else {
                showToast("相机权限被拒绝，暂无法使用app");

                finish();
            }
        } else if (requestCode == 12) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestSipPermission();
            } else {
                showToast("录音权限被拒绝，暂无法使用app");

                finish();
            }
        } else if (requestCode == 13) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                start();
            } else {
                showToast("电话被拒绝，暂无法使用app");

                finish();
            }
        }
    }

    private void requestCameraPermission(){
        String p1 = Manifest.permission.CAMERA;

        if (ContextCompat.checkSelfPermission(this, p1)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{p1}, 11);
        } else {
            //已授权
            requestRecordPermission();
        }
    }

    private void requestRecordPermission(){
        String p2 = Manifest.permission.RECORD_AUDIO;

        if (ContextCompat.checkSelfPermission(this, p2)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{p2}, 12);
        } else {
            //已授权
            requestSipPermission();
        }
    }

    private void requestSipPermission(){
        String p3 = Manifest.permission.USE_SIP;


        if (ContextCompat.checkSelfPermission(this, p3)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{p3}, 13);
        } else {
            //已授权
            start();
        }
    }

    private void start(){
        if (LinphoneService.isReady()) {
            onServiceReady();
        } else {
            startService(new Intent(this, LinphoneService.class));

            final Handler mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!LinphoneService.isReady()) {
                        mHandler.postDelayed(this, 30);
                    } else {
                        onServiceReady();
                    }
                }
            }, 30);
        }
    }

    private void onServiceReady(){

    }
}