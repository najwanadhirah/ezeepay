package com.rt.qpay99.object;

/**
 * Created by User on 7/13/2017.
 */

public class GlobalObj {

    public String mName;
    public String mInfo;
    public String mDesc;
    public String mType;
    public String mColorCode;
    public boolean isSelected;





    public GlobalObj(String mName, String mInfo, String mDesc, String mType, boolean isSelected){
        this.mName = mName;
        this.mInfo = mInfo;
        this.mDesc = mDesc;
        this.mType = mType;

        this.isSelected = isSelected;
    }

    public GlobalObj(String mName, String mInfo, String mDesc, String mType, boolean isSelected, String mColorCode){
        this.mName = mName;
        this.mInfo = mInfo;
        this.mDesc = mDesc;
        this.mType = mType;
        this.mColorCode = mColorCode;

        this.isSelected = isSelected;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmInfo() {
        return mInfo;
    }

    public void setmInfo(String mInfo) {
        this.mInfo = mInfo;
    }

    public String getmDesc() {
        return mDesc;
    }

    public void setmDesc(String mDesc) {
        this.mDesc = mDesc;
    }

    public String getmType() {
        return mType;
    }

    public void setmType(String mType) {
        this.mType = mType;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


}
