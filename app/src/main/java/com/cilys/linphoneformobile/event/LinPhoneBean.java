package com.cilys.linphoneformobile.event;

import org.linphone.core.Call;
import org.linphone.core.Core;
import org.linphone.core.ProxyConfig;
import org.linphone.core.RegistrationState;

import java.io.Serializable;

public class LinPhoneBean implements Serializable {
    private Core core;
    private Call call;
    private Call.State state;
    private String message;

    private ProxyConfig proxyConfig;
    private RegistrationState registrationState;

    public LinPhoneBean setCallState(Call.State state) {
        this.state = state;
        return this;
    }

    public LinPhoneBean setCore(Core core){
        this.core = core;
        return this;
    }

    public LinPhoneBean setCall(Call call) {
        this.call = call;
        return this;
    }

    public LinPhoneBean setMessage (String message) {
        this.message = message;
        return this;
    }

    public LinPhoneBean setProxyConfig(ProxyConfig proxyConfig) {
        this.proxyConfig = proxyConfig;
        return this;
    }

    public LinPhoneBean setRegistrationState(RegistrationState state) {
        this.registrationState = state;
        return this;
    }

    public Core getCore() {
        return core;
    }

    public Call getCall() {
        return call;
    }

    public Call.State getCallState() {
        return state;
    }

    public String getMessage() {
        return message;
    }

    public ProxyConfig getProxyConfig() {
        return proxyConfig;
    }

    public RegistrationState getRegistrationState() {
        return registrationState;
    }
}
