package com.rt.qpay99.object;

/**
 * Created by User on 1/15/2018.
 */

public class ColorCodeObj {
    private String ColorCode;
    private String ColorName;

    public ColorCodeObj(String colorCode , String colorName){
        this.setColorCode(colorCode);
        this.setColorName(colorName);
    }

    public String getColorCode() {
        return ColorCode;
    }

    public void setColorCode(String colorCode) {
        ColorCode = colorCode;
    }

    public String getColorName() {
        return ColorName;
    }

    public void setColorName(String colorName) {
        ColorName = colorName;
    }
}
