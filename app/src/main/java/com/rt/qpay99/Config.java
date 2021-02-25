package com.rt.qpay99;

import com.rt.qpay99.util.SharedPreferenceUtil;

public class Config {
    public static boolean isDebug = false; // default: false
    public static boolean showLog = false; // default: false
    public static boolean showTrace = false;// default: false
    public static boolean isForceGST = false;// default: false
    public static String Custotmer_care = "";
    public static String Custotmer_website = "";
    public static String DEBUG_TAG = "";
    public static String WORKING_HOUR = "9AM - 6PM (Monday - Friday)";
    public static int Decimal_Point = 2;

    public static boolean isLocationAvaiable = false;
    public static double Latitude=0;
    public static double Longitude=0;

    public static String AZUREMAIN_URL = "https://onlinereload.azurewebsites.net/";
    public static String AGENT_URL = "api/Agent";
    public static String LoginHistory_URL = "api/LoginHistory";

    public static String VersionCheck_Status = "ACTIVE";
    public static String FirebaseStorageID = "kimwei.chew@gmail.com";
    public static String FirebaseStoragePassword = "1qazxsw23";

    public static String AZURE_DOMAIN = "https://srsclientapiv2.azurewebsites.net/";
    public static String URL_UpdatePrinterMacAddress = AZURE_DOMAIN + "api/printer/InsertPrinter?";

    public  static String IphoneProductURL = AZURE_DOMAIN + "api/productlist";
    public  static String ItemPriceURL = AZURE_DOMAIN + "api/itemprice/";

    public static String productCategoryURL = AZURE_DOMAIN + "api/ProductCategory";
    public static String PrinterListURLItemListURL = AZURE_DOMAIN +  "api/productlist/GetByCategory/";
    public static String VersionCheckURL = AZURE_DOMAIN + "api/appctrl/VersionCheck/";
    public static String ErrorLog_URL = "http://reload.dyndns.org:8020/rtweb/api/ErrorLog?";


    //QPoints
    public static String AgentQPointsURL = AZURE_DOMAIN +  "api/QPointTx/getQPoint/";
    public static String QPoint_REDEEMPRODUCT = AZURE_DOMAIN +  "api/ProductList/ProductRedeem/";
    public static String QPoint_REDEMPTIONLOG = AZURE_DOMAIN +  "api/ProductRedemption/Log/";
    public static String QPoint_ProductRedemption = AZURE_DOMAIN +  "api/ProductRedemption/";
    public static String QPoint_LuckyDraw = AZURE_DOMAIN +  "api/LuckyDraw/";
    public static String QPoint_LuckyDrawCount = AZURE_DOMAIN +  "api/LuckyDraw/";

    public static String QPointTx = AZURE_DOMAIN +  "api/QPointTx/";

    //60109685582sss
    public static String GatewayNumber = "+60109687854";

    public static String WS_URL = "http://ezeepay.ddns.net:8222/ezeeapi/connect.asmx"; //QPAY99

//    public static String WS_URL = "http://onlinereload.dyndns.org:9999/srsclientapi/connect.asmx"; //QPAY99
//        public static String WS_URL = "http://reload.dyndns.org:8020/srsclientAPI/connect.asmx"; // QPAY99



    public static String WS_ASTROQUERY_URL = "http://121.121.33.37/ASTROQUERY/service.asmx?wsdl";
    //public static String WS_URL ="http://onlinereload.dyndns.org:9999/newapi/connect.asmx"; // QPAY99

    public static String WS_NAME_SPACE = "http://tempuri.org/";
    public static String WS_SUBMIT_SUCCESS = "SUBMIT_SUCCESS";
    public static final int INACTIVE_TIMES = 1;
    public static String strUUID = "";

    public final static String UTF8 = "UTF-8";
    public static String printer_CompanyName = "QPay99";

    private static String setting_Share = "This is my text to send.";

    public static int MAX_MOBILE_LENGHT = 13;
    public static String OS_PLATFORM = "ANDROID";

    public final static String GCM_SENDER_ID = "911303413814";

    public static String GCM_RegId = "";

    public static String EMAIL_ADD1 = "qpay99sb@gmail.com";
    public static String EMAIL_ADD2 = "qpaytrading@gmail.com";

    public static String EMAIL_SUBJECT = "QPay99 Registration";
    public static String EMAIL_INFO = "QPay99 Registration";

    public static String printerId = "";
    public static String printerName = "";

    public static int sClientID = 0;
    public static String sMasterID = "0";

    // public static String MobileMoney_MobileNo = "0162282008";
    public static String MobileMoney_MobileNo = "0166572577";

    public static String GST_RATE = "0.00";

    public static String GST_ID_MAXIS = "000311951360";
    public static String GST_ID_DIGI = "001211957248";
    public static String GST_ID_CELCOM = "001490792448";
    public static String GST_ID_UMOBILE = "001877737472";
    public static String GST_ID_TUNETALK = "00073744936";
    public static String GST_ID_XOX = "000397234175";
    public static String GST_ID_CLIXSTER = "";
    public static String GST_ID_MOL = "";
    public static String GST_ID_MERCHANTRADE = "";


    public static String MAXIS_RM5 = "9555045100051";
    public static String MAXIS_RM10 = "9555045100105";
    public static String MAXIS_RM30 = "9555045100303";
    public static String MAXIS_RM50 = "9555045100501";
    public static String MAXIS_RM60 = "9555045105001";
    public static String MAXIS_RM100 = "9555045101003";

    public static String DIGI_RM5 = "9555059400321";
    public static String DIGI_RM10 = "9555059400383";
    public static String DIGI_RM30 = "9555059400123";
    public static String DIGI_RM50 = "9555059400024";
    public static String DIGI_RM100 = "9555059400031";

    public static String CELCOM_RM5 = "9555055204602";
    public static String CELCOM_RM10 = "9555055200291";

    public static String CELCOM_RM20 = "9555055200192";
    public static String CELCOM_RM30 = "9555055200277";
    public static String CELCOM_RM50 = "9555055200208";
    public static String CELCOM_RM100 = "9555055200215";

    public static String UMOBILE_RM5 = "9555055204497";
    public static String UMOBILE_RM10 = "9555483900008";
    public static String UMOBILE_RM30 = "9555483900015";
    public static String UMOBILE_RM50 = "9555483900022";
    public static String UMOBILE_RM100 = "9555483900046";

    public static String DIGIWOW_RM15 = "9555000010005";
    public static String LEBARA_RM10 = "9555000010012";
    public static String LEBARA_RM15 = "9555000010029";

    public static String TUNETALK_RM5 = "9555397302202";
    public static String TUNETALK_RM10 = "9555055204862";
    public static String TUNETALK_RM30 = "9555055204879";
    public static String TUNETALK_RM50 = "9555055204886";
    public static String TUNETALK_RM100 = "9555397302349";


    public static String ITALK10 = "9555055202257";
    public static String ITALK20 = "9555397302431";
    public static String ITALK30 = "9555055202264";
    public static String ITALK50 = "9555055202271";

    public static String MERCHANTRADE_RM5 = "9555055202486";
    public static String MERCHANTRADE_RM10 = "9555055202455";
    public static String MERCHANTRADE_RM30 = "9555055202462";
    public static String MERCHANTTRADE_RM50 = "9555055202479";

    public static String OKTEL_5 = "9555055205159";
    public static String OKTEL_10 = "9555055205166";
    public static String OKTEL_30 = "9555055205173";
    public static String OKTEL_50 = "9555055205180";


    public static String HOTTICKET_15 = "9555055205203";


    public static String PULSA_RP15K = "9555055205210";

    public static String PULSA_RP25K = "9555055205227";

    public static String OFFGAMERS_10 = "9555055203704";
    public static String OFFGAMERS_20 = "9555055203711";
    public static String OFFGAMERS_30 = "9555055203728";
    public static String OFFGAMERS_50 = "9555055203735";
    public static String OFFGAMERS_100 = "9555055203742";
    public static String OFFGAMERS_200 = "9555055203759";
    public static String OFFGAMERS_500 = "9555055203773";

    public static String CLIXSTER_RM10 = "9555397302622";
    public static String CLIXSTER_RM30 = "9555397302639";

    public static String MOL_RM10 = "9555397301106";
    public static String MOL_RM20 = "9555397301113";
    public static String MOL_RM30 = "9555397301120";
    public static String MOL_RM50 = "9555397301137";
    public static String MOL_RM100 = "9555397301144";

    public static String XOX_RM5 = "9555055204657";
    public static String XOX_RM10 = "9555055204664";
    public static String XOX_RM30 = "9555055204671";
    public static String XOX_RM50 = "9555055204688";

    public static String TMGO10 = "9555397302349";
    public static String TMGO20 = "9555397302356";
    public static String TMGO30 = "9555397302363";
    public static String TMGO50 = "9555397302370";

    public static String NJOI10 = "9555397302387";
    public static String NJOI20 = "9555397302394";
    public static String NJOI30 = "9555397302400";
    public static String NJOI50 = "9555397302417";

    public static String GRAB10 = "9555397302455";
    public static String GRAB20 = "9555397302462";
    public static String GRAB50 = "9555397302479";

    public static String DIGIIDD_RM15 = "9555059400628";
    public static String ONEMYPIN = "9555397302332";

    public static String MAXISRPLUSPIN10 = "9555399343739";
    public static String MAXISRPLUSPIN15 = "9555399353899";

    public static String SPEAKOUTPIN10 = "9555308647897";
    public static String SPEAKOUTPIN30 = "9555308658145";

    public static String YESPIN_10 = "9896015216113";
    public static String YESPIN_30 = "9896015216120";


    public static String MOVING_TEXT = SharedPreferenceUtil.getMovingText();


}
