package com.rt.qpay99.object;

/**
 * Created by kimwei on 1/19/2016.
 */
public class RTResponse {
    private boolean ResultBoolean;
    private String sResponseMessage;

    public boolean isResultBoolean() {
        return ResultBoolean;
    }

    public void setResultBoolean(boolean resultBoolean) {
        ResultBoolean = resultBoolean;
    }

    public String getsResponseMessage() {
        return sResponseMessage;
    }

    public void setsResponseMessage(String sResponseMessage) {
        this.sResponseMessage = sResponseMessage;
    }
}
