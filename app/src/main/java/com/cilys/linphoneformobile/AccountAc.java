package com.cilys.linphoneformobile;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;


import com.cilys.linphoneformobile.base.BaseAc;
import com.cilys.linphoneformobile.event.Event;
import com.cilys.linphoneformobile.event.EventImpl;
import com.cilys.linphoneformobile.event.LinPhoneBean;
import com.cilys.linphoneformobile.service.LinphoneService;
import com.cilys.linphoneformobile.utils.Sp;
import com.cilys.linphoneformobile.view.SingleClickListener;

import org.linphone.core.AVPFMode;
import org.linphone.core.AccountCreator;
import org.linphone.core.ProxyConfig;
import org.linphone.core.RegistrationState;
import org.linphone.core.TransportType;

public class AccountAc extends BaseAc {
    private boolean clickConfigButton = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_account);

        initUI();
    }

    private void initUI(){
        final EditText ed_userName = findView(R.id.ed_userName);
        final EditText ed_pwd = findView(R.id.ed_pwd);
        final EditText ed_domain = findView(R.id.ed_domain);
        final EditText ed_proxy = findView(R.id.ed_proxy);
        final RadioGroup rg = findView(R.id.rg);


        String u = Sp.getStr(this, "SP_SIP_USER", null);
        String p = Sp.getStr(this, "SP_SIP_PWD", null);
        String d = Sp.getStr(this, "SP_SIP_DOMAIN", null);
        String x = Sp.getStr(this, "SP_SIP_PROXY_SERVER", null);
        ed_userName.setText(u == null ? "8002" : u);
        ed_pwd.setText(p == null ? "4b435aa3" : p);
        ed_domain.setText(d == null ? "Training" : d);
        ed_proxy.setText(x == null ? "core1-hk.netustay.com" : x);

//        ed_userName.setText("8001");
//        ed_pwd.setText("f0f7d0da");

        findView(R.id.btn_connect).setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                String userName = ed_userName.getText().toString().trim();
                String pwd = ed_pwd.getText().toString().trim();
                String domain = ed_domain.getText().toString().trim();
                String proxy = ed_proxy.getText().toString().trim();

                if (userName == null || userName.length() < 1
                        || (pwd == null || pwd.length() < 1)
                        || (domain == null || domain.length() < 1)
                ){
                    showToast("请输入必填项");
                    return;
                }
                TransportType type = TransportType.Udp;

                if (rg.getCheckedRadioButtonId() == R.id.rbt_udp) {
                    type = TransportType.Udp;
                } else if (rg.getCheckedRadioButtonId() == R.id.rbt_tcp) {
                    type = TransportType.Tcp;
                } else if (rg.getCheckedRadioButtonId() == R.id.rbt_tls) {
                    type = TransportType.Tls;
                }

                Sp.putStr(AccountAc.this, "SP_SIP_USER", userName);
                Sp.putStr(AccountAc.this, "SP_SIP_PWD", pwd);
                Sp.putStr(AccountAc.this, "SP_SIP_DOMAIN", domain);
                Sp.putStr(AccountAc.this, "SP_SIP_PROXY_SERVER", proxy);

                proxy = "<sip:" + proxy + ";transport=udp>";

                initAccountCreator();
                if (mAccountCreator == null) {
                    return;
                }

                clickConfigButton = true;
                config(userName, pwd, domain, proxy, type);
            }
        });
        initAccountCreator();
    }

    private void initAccountCreator(){
        if (mAccountCreator == null) {
            if (LinphoneService.getCore() != null) {
                mAccountCreator = LinphoneService.getCore().createAccountCreator(null);
            }
        }
    }

    @Override
    protected void onEvent(Event e) {
        super.onEvent(e);

        if (e.what == EventImpl.REGISTION_STATE_CHANGED) {
            if (e.obj instanceof LinPhoneBean) {
                RegistrationState state = ((LinPhoneBean) e.obj).getRegistrationState();
                if (state == RegistrationState.Ok) {
                    if (clickConfigButton) {
                        clickConfigButton = false;

                        showToast("配置成功");

                        ProxyConfig cfg = ((LinPhoneBean) e.obj).getProxyConfig();
                        cfg.edit();
                        cfg.setAvpfMode(AVPFMode.Disabled);
                        cfg.done();

                        finish();
                    }
                } else if (state == RegistrationState.Failed) {
                    if (clickConfigButton) {
                        clickConfigButton = false;

                        showToast("Failure: " + ((LinPhoneBean) e.obj).getMessage());
                    }
                }
            }
        }
    }

    private AccountCreator mAccountCreator;
    private void config(String useName, String pwd, String domain, String proxy, TransportType type){
        mAccountCreator.setUsername(useName);
        mAccountCreator.setPassword(pwd);
        mAccountCreator.setDomain(domain);
        mAccountCreator.setTransport(type);

        ProxyConfig cfg = mAccountCreator.createProxyConfig();
        cfg.setServerAddr(proxy);
        LinphoneService.getCore().setDefaultProxyConfig(cfg);
    }
}