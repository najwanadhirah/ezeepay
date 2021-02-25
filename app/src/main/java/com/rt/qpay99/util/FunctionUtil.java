package com.rt.qpay99.util;

import android.content.Context;
import android.provider.Settings;
import android.text.InputFilter;
import android.text.InputType;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.widget.EditText;

import com.rt.qpay99.Config;
import com.rt.qpay99.R;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FunctionUtil {

    static InputFilter[] filterArray = new InputFilter[1];
    private static SimpleDateFormat df;
    private static String TAG = "FunctionUtil";

    public static boolean isPDA(String printerName) {
        try {
            if ((printerName.equalsIgnoreCase("IposPrinter:"))
                    || (printerName.equalsIgnoreCase("InnerPrinter:"))
                    ) {

                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }



    public static int calculateNoOfColumns(Context context, int imageWidth) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / imageWidth);
        return noOfColumns;
    }

    public static String getPhoneBrand(String model) {

        String brandName = "";
        String phoneModel;
        try {
            phoneModel = model.toLowerCase();

            brandName = model;

            if (phoneModel.indexOf("oneplus") > -01) {
                brandName = "ONEPLUS";
                return brandName;
            }


            if (phoneModel.indexOf("vivo") > -01) {
                brandName = "VIVO";
                return brandName;
            }


            if (phoneModel.indexOf("xiaomi") > -01) {
                brandName = "XIAOMI";
                return brandName;
            }


            if (phoneModel.indexOf("oppo") > -01) {
                brandName = "OPPO";
                return brandName;
            }


            if (phoneModel.indexOf("sony") > -01) {
                brandName = "SONY";
                return brandName;
            }


            if (phoneModel.indexOf("samsung") > -01) {
                brandName = "SAMSUNG";
                return brandName;
            }


            if (phoneModel.indexOf("iphone") > -01) {
                brandName = "IPHONE";
                return brandName;
            }


            if (phoneModel.indexOf("srs mobile printer") > -01) {
                brandName = "SRSPRINTER";
                return brandName;
            }


            if (phoneModel.indexOf("printer") > -01) {
                brandName = "SRSPRINTER";
                return brandName;
            }


            if (phoneModel.indexOf("paper roll") > -01) {
                brandName = "PAPERROLL";
                return brandName;
            }


            if (phoneModel.indexOf("paper roll") > -01) {
                brandName = "PAPERROLL";
                return brandName;
            }

            brandName = phoneModel.replace(" ", "").toUpperCase();


        } catch (Exception e) {

        }

        return brandName;
    }

    public static boolean isSet(String s) {
        return !(s == null || s.equals(""));
    }

    public static String[] splitToStringArray(String Subject, String Delimiters) {

        String[] parts = Subject.split(",");

        return parts;
    }

    public static List<String> splitToStringList(String Subject,
                                                 String Delimiters) {

        String[] parts = Subject.split(",");

        return Arrays.asList(parts);

    }

    public static String convertString2Date(String date, String dateFormat) {
        Date myDate = new Date();
        String mdy = "";
        String dmy = "";
        try {

            df = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
            myDate = df.parse(date);
            SimpleDateFormat mdyFormat = new SimpleDateFormat("MM-dd-yyyy");
            SimpleDateFormat dmyFormat = new SimpleDateFormat(dateFormat);
            mdy = mdyFormat.format(myDate);
            dmy = dmyFormat.format(myDate);

            // Results...
            System.out.println(mdy);
            System.out.println(dmy);
            // Parse the Strings back to dates
            // Note, the formats don't "stick" with the Date value
            System.out.println(mdyFormat.parse(mdy));
            System.out.println(dmyFormat.parse(dmy));

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            dmy = date;
            e.printStackTrace();
        }
        return dmy;
    }

    public static Date getConvertdate(String date) {

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "MM/dd/yyyy hh:mm:ss aa");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(convertedDate);
        return convertedDate;
    }

    public static String getConvertdate(String date, String timeFormat) {

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "MM/dd/yyyy hh:mm:ss aa");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(date);
            return CalenderUtil.getStringAddDate(convertedDate,
                    "yyyy-MM-dd HH:mm:ss", 0);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return new Date().toString();
    }

    public static String getStringDateTimeSec() {
        // SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        Date today = Calendar.getInstance().getTime();
        String reportDate = df.format(today);
        long millis = today.getTime();
        DLog.e(TAG, "millis" + millis);

        // Print what date is today!
        System.out.println("Report Date: " + reportDate);
        DLog.e(TAG, "reportDate" + reportDate);

        return reportDate;
    }

    public static String getStrCurrentDateTime() {
        Calendar c = Calendar.getInstance();
        df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DLog.e(TAG, "reportDate" + df.format(c.getTime()));
        return df.format(c.getTime());
    }

    public static EditText getLengthFilterInput(EditText input) {
        filterArray[0] = new InputFilter.LengthFilter(10);
        input.setInputType(InputType.TYPE_CLASS_PHONE);
        input.setFilters(filterArray);
        return input;
    }

    public static String getDeviceIdOrAndroidId(Context context) {
//		final String deviceId = ((TelephonyManager) context
//				.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
//		if (deviceId != null) {
//			return deviceId;
//		} else {
//			return android.os.Build.SERIAL;
//		}
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static int setImages(Context mContext, String name) {
        String uri = "drawable/ic_" + name.toString().toLowerCase() + "_icon";
        int imgRes = mContext.getResources().getIdentifier(uri, null,
                mContext.getPackageName());
        if (imgRes == 0)
            return R.drawable.ic_launcher;
        return imgRes;

    }

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        @SuppressWarnings("deprecation")
                        String ip = Formatter.formatIpAddress(inetAddress
                                .hashCode());
                        DLog.i(TAG, "***** IP=" + ip);
                        return ip;
                    }
                }
            }
        } catch (SocketException ex) {
            DLog.e(TAG, ex.toString());
        }
        return null;
    }

    public static String getIPAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception ex) {
            DLog.e("IP Address", ex.toString());
        }
        return null;
    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static String countSpacing(String name, String total) {
        String space = "";
        if (total == null)
            total = "";

        int totalSpacing = 41;
        int cLen = name.length() + total.length();
        int newLen = totalSpacing - cLen;

        for (int i = 0; i <= newLen; i++) {
            space = space + " ";
        }

        return space;
    }

    public static String countSpacingForShare(String name, String total) {
        String space = "";
        if (total == null)
            total = "";

        int totalSpacing = 61;
        int cLen = name.length() + total.length();
        int newLen = totalSpacing - cLen;

        for (int i = 0; i <= newLen; i++) {
            space = space + " ";
        }

        return space;
    }

    public static String getPriceWithGST(String mPrice, double GST_Tax) {

        String strPlayValue = "";
        Double dPlayValue = 0.00;
        try {
            dPlayValue = Double.parseDouble(mPrice);
            if (GST_Tax != 0)
                dPlayValue = dPlayValue + (dPlayValue * GST_Tax / 100);
            strPlayValue = "" + String.format("%.2f", dPlayValue);
            DLog.e(TAG, "=======================================" + strPlayValue);
        } catch (Exception e) {
            //strPlayValue = mPrice + " 6% GST";
            DLog.e(TAG, "" + e.getMessage());
        }

        return strPlayValue;

    }

    public static String PriceRoundUp(double sValue) {
        String sTemp = String.format("%.2f", sValue);
        String[] aTemp;
        if (sTemp.contains(".")) {
            aTemp = sTemp.toString().split(Pattern.quote("."));
            if (aTemp.length >= 1) {
                String dlo1 = aTemp[1];
                String lastDigit = dlo1.substring(dlo1.length() - 1);
                if (lastDigit.equalsIgnoreCase("1")) {
                    sValue = sValue - 0.01;
                } else if (lastDigit.equalsIgnoreCase("2")) {
                    sValue = sValue - 0.02;
                } else if (lastDigit.equalsIgnoreCase("3")) {
                    sValue = sValue + 0.02;
                } else if (lastDigit.equalsIgnoreCase("4")) {
                    sValue = sValue + 0.01;
                } else if (lastDigit.equalsIgnoreCase("6")) {
                    sValue = sValue - 0.01;
                } else if (lastDigit.equalsIgnoreCase("7")) {
                    sValue = sValue - 0.02;
                } else if (lastDigit.equalsIgnoreCase("8")) {
                    sValue = sValue + 0.02;
                } else if (lastDigit.equalsIgnoreCase("9")) {
                    sValue = sValue + 0.01;
                } else {
                    sValue = sValue;
                }
            }
        }
        return String.format("%.2f", sValue);
    }

    public static double Round2Decimal(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static boolean IsOdd(int val) {
        return (val & 0x01) != 0;

    }

    public static String getsDNReceivedID() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String sDNReceivedID = sdf.format(new Date());
        return sDNReceivedID;
    }

    public static String getsEncK(String key) {
        DLog.e(TAG, "md5 " + key);
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
            byte[] array = m.digest(key.getBytes());
            byte[] digest = m.digest(key.getBytes());
            //byte[] digest= md5.getBytes("UTF-8");
            String hash = new BigInteger(1, digest).toString(16);
            System.out.println(hash);
            return hash;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getPINFormat(String input) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            if (i % 4 == 0 && i != 0) {
                result.append(" ");
            }

            result.append(input.charAt(i));
        }
        DLog.e(TAG, "=============" + result.toString());
        return result.toString();
    }

    public static String getsEncK2(String key) {
        DLog.e(TAG, "md5 " + key);
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
            byte[] array = m.digest(key.getBytes());
            byte[] digest = m.digest(key.getBytes());
            //byte[] digest= md5.getBytes("UTF-8");
            String hash = new BigInteger(1, digest).toString(16);
            System.out.println(hash);

            while (hash.length() < 32) {
                hash = "0" + hash;
            }
            return hash;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getBarcode(String mProduct, String mValue) {
        DLog.e(TAG, "mProduct " + mProduct);
        DLog.e(TAG, "mValue " + mValue);
        String barCode = "";

        if (mProduct.equalsIgnoreCase("DIGIPIN")
                || mProduct.equalsIgnoreCase("DIGI")) {
            if (mValue.equalsIgnoreCase("5"))
                return Config.DIGI_RM5;

            if (mValue.equalsIgnoreCase("10"))
                return Config.DIGI_RM10;

            if (mValue.equalsIgnoreCase("30"))
                return Config.DIGI_RM30;

            if (mValue.equalsIgnoreCase("50"))
                return Config.DIGI_RM50;

            if (mValue.equalsIgnoreCase("100"))
                return Config.DIGI_RM100;
        }

        if (mProduct.equalsIgnoreCase("MAXISPIN")
                || mProduct.equalsIgnoreCase("MAXISPIN")) {
            if (mValue.equalsIgnoreCase("5"))
                return Config.MAXIS_RM5;

            if (mValue.equalsIgnoreCase("10"))
                return Config.MAXIS_RM10;

            if (mValue.equalsIgnoreCase("30"))
                return Config.MAXIS_RM30;

            if (mValue.equalsIgnoreCase("50"))
                return Config.MAXIS_RM50;

            if (mValue.equalsIgnoreCase("60"))
                return Config.MAXIS_RM60;

            if (mValue.equalsIgnoreCase("100"))
                return Config.MAXIS_RM100;
        }

        if (mProduct.equalsIgnoreCase("CELCOMPIN")
                || mProduct.equalsIgnoreCase("CELCOM")) {
            if (mValue.equalsIgnoreCase("5"))
                return Config.CELCOM_RM5;

            if (mValue.equalsIgnoreCase("10"))
                return Config.CELCOM_RM10;

            if (mValue.equalsIgnoreCase("30"))
                return Config.CELCOM_RM30;

            if (mValue.equalsIgnoreCase("50"))
                return Config.CELCOM_RM50;

            if (mValue.equalsIgnoreCase("100"))
                return Config.CELCOM_RM100;
        }

        if (mProduct.equalsIgnoreCase("UMOBILEPIN")
                || mProduct.equalsIgnoreCase("UMOBILE")) {

            if (mValue.equalsIgnoreCase("10"))
                return Config.UMOBILE_RM10;

            if (mValue.equalsIgnoreCase("30"))
                return Config.UMOBILE_RM30;

            if (mValue.equalsIgnoreCase("50"))
                return Config.UMOBILE_RM50;

            if (mValue.equalsIgnoreCase("100"))
                return Config.UMOBILE_RM100;

        }

        if (mProduct.equalsIgnoreCase("TUNETALKPIN")
                || mProduct.equalsIgnoreCase("TUNETALK")) {

            if (mValue.equalsIgnoreCase("5"))
                return Config.TUNETALK_RM5;

            if (mValue.equalsIgnoreCase("10"))
                return Config.TUNETALK_RM10;

            if (mValue.equalsIgnoreCase("30"))
                return Config.TUNETALK_RM30;

            if (mValue.equalsIgnoreCase("50"))
                return Config.TUNETALK_RM50;

            if (mValue.equalsIgnoreCase("100"))
                return Config.TUNETALK_RM100;

        }

        if (mProduct.equalsIgnoreCase("MECHANTRADEPIN")
                || mProduct.equalsIgnoreCase("MECHANTRADE")) {

            if (mValue.equalsIgnoreCase("5"))
                return Config.MERCHANTRADE_RM5;

            if (mValue.equalsIgnoreCase("10"))
                return Config.MERCHANTRADE_RM10;

            if (mValue.equalsIgnoreCase("30"))
                return Config.MERCHANTRADE_RM30;

        }

        if (mProduct.equalsIgnoreCase("CLIXSTERPIN")
                || mProduct.equalsIgnoreCase("CLIXSTERP")) {

            if (mValue.equalsIgnoreCase("10"))
                return Config.CLIXSTER_RM10;

            if (mValue.equalsIgnoreCase("30"))
                return Config.CLIXSTER_RM30;

        }

        if (mProduct.equalsIgnoreCase("XOXPIN")
                || mProduct.equalsIgnoreCase("XOX")) {

            if (mValue.equalsIgnoreCase("5"))
                return Config.XOX_RM5;

            if (mValue.equalsIgnoreCase("10"))
                return Config.XOX_RM10;

            if (mValue.equalsIgnoreCase("30"))
                return Config.XOX_RM30;

            if (mValue.equalsIgnoreCase("50"))
                return Config.XOX_RM50;

        }

        if (mProduct.equalsIgnoreCase("MOLPIN")
                || mProduct.equalsIgnoreCase("MOL")) {

            if (mValue.equalsIgnoreCase("10"))
                return Config.MOL_RM10;

            if (mValue.equalsIgnoreCase("20"))
                return Config.MOL_RM20;

            if (mValue.equalsIgnoreCase("30"))
                return Config.MOL_RM30;

            if (mValue.equalsIgnoreCase("50"))
                return Config.MOL_RM50;

            if (mValue.equalsIgnoreCase("100"))
                return Config.MOL_RM100;

        }

        if (mProduct.equalsIgnoreCase("TMGOPIN")
                || mProduct.equalsIgnoreCase("TMGO")) {

            if (mValue.equalsIgnoreCase("10"))
                return Config.TMGO10;

            if (mValue.equalsIgnoreCase("20"))
                return Config.TMGO20;

            if (mValue.equalsIgnoreCase("30"))
                return Config.TMGO30;

            if (mValue.equalsIgnoreCase("50"))
                return Config.TMGO50;

        }

        if (mProduct.equalsIgnoreCase("NJOIPIN")
                || mProduct.equalsIgnoreCase("NJOI")) {

            if (mValue.equalsIgnoreCase("10"))
                return Config.NJOI10;

            if (mValue.equalsIgnoreCase("20"))
                return Config.NJOI20;

            if (mValue.equalsIgnoreCase("30"))
                return Config.NJOI30;

            if (mValue.equalsIgnoreCase("50"))
                return Config.NJOI50;

        }

        if (mProduct.equalsIgnoreCase("ITALKPIN")
                || mProduct.equalsIgnoreCase("ITALK")) {

            if (mValue.equalsIgnoreCase("10"))
                return Config.ITALK10;

            if (mValue.equalsIgnoreCase("20"))
                return Config.ITALK20;

            if (mValue.equalsIgnoreCase("30"))
                return Config.ITALK30;

        }


        if (mProduct.equalsIgnoreCase("GRABPIN")
                || mProduct.equalsIgnoreCase("GRAB")) {

            if (mValue.equalsIgnoreCase("10"))
                return Config.GRAB10;

            if (mValue.equalsIgnoreCase("20"))
                return Config.GRAB20;

            if (mValue.equalsIgnoreCase("30"))
                return Config.GRAB50;

        }

        if (mProduct.equalsIgnoreCase("LEBARAPIN")
                || mProduct.equalsIgnoreCase("LEBARA")) {

            if (mValue.equalsIgnoreCase("10"))
                return Config.LEBARA_RM10;

            if (mValue.equalsIgnoreCase("15"))
                return Config.LEBARA_RM15;
        }


        if (mProduct.equalsIgnoreCase("ONEMYPIN")
                || mProduct.equalsIgnoreCase("ONEMY")) {
            return Config.ONEMYPIN;

        }

        return "";

    }

    public static String countSpacing2(String name, String total) {
        String space = "";
        if (total == null)
            total = "";

        int totalSpacing = 31;
        int cLen = name.length() + total.length();
        int newLen = totalSpacing - cLen;

        for (int i = 0; i <= newLen; i++) {
            space = space + " ";
        }

        return space;
    }

    public String TransLongToTime(long time) {
        long daytime;
        daytime = time % 86400000;
        long hour = (daytime / 3600000);
        long minute = (daytime - hour * 3600000) / 60000;
        long second = ((daytime - hour * 3600000) % 60000) / 1000;
        long misecond = ((daytime - hour * 3600000) % 60000) % 1000;

        return String.format("%02d:%02d:%02d.%d", hour + 8, minute, second,
                misecond);

    }
}
