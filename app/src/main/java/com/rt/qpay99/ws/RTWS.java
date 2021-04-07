package com.rt.qpay99.ws;

import com.rt.qpay99.Config;
import com.rt.qpay99.Helper.HttpHandlerHelper;
import com.rt.qpay99.object.AgentProductDiscount;
import com.rt.qpay99.object.AgentProductRebate;
import com.rt.qpay99.object.AgentSales;
import com.rt.qpay99.object.AgentSalesByMSISDN;
import com.rt.qpay99.object.BankIn;
import com.rt.qpay99.object.CheckBalanceResponse;
import com.rt.qpay99.object.CustomerInputApproveResponseObj;
import com.rt.qpay99.object.CustomerInputList;
import com.rt.qpay99.object.CustomerTopupTx;
import com.rt.qpay99.object.CustomerTxStatusInfo;
import com.rt.qpay99.object.DeviceLoginResponse;
import com.rt.qpay99.object.LoginInfo;
import com.rt.qpay99.object.MMExChangeRateResult;
import com.rt.qpay99.object.ProductInfo;
import com.rt.qpay99.object.RTResponse;
import com.rt.qpay99.object.RequestInput;
import com.rt.qpay99.object.RequestInputResponse;
import com.rt.qpay99.object.RequestReloadPinObject;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.FunctionUtil;
import com.rt.qpay99.util.SharedPreferenceUtil;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class RTWS {

    private String TAG = this.getClass().getName();

    public RTResponse ForgotPasswordUpdatePassword(String sClientUserName,
                                                   String sCustomerMobile, String sNewPassword, String sCustomerID,String sTS, String sEncKey, String sResponseMsg) {
        RTCallWS cs = new RTCallWS();
        RTResponse result = new RTResponse();
        DLog.e(TAG, "sClientUserName " + sClientUserName);
        DLog.e(TAG, "sCustomerMobile " + sCustomerMobile);
        DLog.e(TAG, "sNewPassword " + sNewPassword);
        DLog.e(TAG, "sCustomerID " + sCustomerID);
        DLog.e(TAG, "sTS " + sTS);
        DLog.e(TAG, "sEncKey " + sEncKey);
        DLog.e(TAG, "sNewPassword " + sResponseMsg);

        try {
            RTSoap response = cs.ForgotPasswordUpdatePassword(sClientUserName, sCustomerMobile,
                    sNewPassword,sCustomerID,sTS,sEncKey,sResponseMsg);
            if (response != null) {
                if (response.getProperties() != null) {
                    for (RTSoapProperty element : response.getProperties()) {
                        if ("ForgotPasswordUpdatePasswordResult".equalsIgnoreCase(element
                                .getName()))
                            result.setResultBoolean(element.getBooleanValue());
                        if ("sResponseMsg".equalsIgnoreCase(element
                                .getName()))
                            result.setsResponseMessage(element.getValue());
                    }
                }
            }
        } catch (Exception e) {
            DLog.e(TAG, "" + e.getMessage());
        }

        return result;

    }

    public RTResponse ForgotPasswordSendTac(String sClientUserName,String sCustomerMobile , String sTS, String sEncKey, String sClientID,String sDeviceID) {

        RTCallWS cs = new RTCallWS();
        RTResponse result = new RTResponse();

        DLog.e(TAG, "sClientUserName " + sClientUserName);
        DLog.e(TAG, "sCustomerMobile " + sCustomerMobile );
        DLog.e(TAG, "sClientID " + sClientID);
        DLog.e(TAG, "sTS " + sTS);
        DLog.e(TAG, "sEncKey " + sEncKey);
        DLog.e(TAG, "sDeviceID " + sDeviceID);


        try {
            RTSoap response = cs.ForgotPasswordSendTac(sClientUserName, sCustomerMobile ,sTS,sEncKey,sClientID,sDeviceID);
            if (response != null) {
                if (response.getProperties() != null) {
                    for (RTSoapProperty element : response.getProperties()) {
                        if ("ForgotPasswordSendTacResult".equalsIgnoreCase(element
                                .getName()))
                            result.setResultBoolean(element.getBooleanValue());
                        if ("sClientID".equalsIgnoreCase(element
                                .getName()))
                            result.setsResponseMessage(element.getValue());
                    }
                }
            }
        } catch (Exception e) {
            DLog.e(TAG, "" + e.getMessage());
        }

        return result;

    }

//    public RTResponse ForgotPasswordSendTac(String sClientUserName,String sCustomerMobile , String sTS, String sEncKey, String sClientID,String sDeviceID) {
//
//        RTCallWS cs = new RTCallWS();
//        RTResponse result = new RTResponse();
//
//        DLog.e(TAG, "sClientUserName " + sClientUserName);
//        DLog.e(TAG, "sCustomerMobile " + sCustomerMobile );
//        DLog.e(TAG, "sClientID " + sClientID);
//        DLog.e(TAG, "sTS " + sTS);
//        DLog.e(TAG, "sEncKey " + sEncKey);
//        DLog.e(TAG, "sDeviceID " + sDeviceID);
//
//
//        try {
//            RTSoap response = cs.ForgotPasswordSendTac(sClientUserName, sCustomerMobile ,sTS,sEncKey,sClientID,sDeviceID);
//            if (response != null) {
//                if (response.getProperties() != null) {
//                    for (RTSoapProperty element : response.getProperties()) {
//                        if ("ForgotPasswordSendTacResult".equalsIgnoreCase(element
//                                .getName()))
//                            result.setResultBoolean(element.getBooleanValue());
//                        if ("sClientID".equalsIgnoreCase(element
//                                .getName()))
//                            result.setsResponseMessage(element.getValue());
//                    }
//                }
//            }
//        } catch (Exception e) {
//            DLog.e(TAG, "" + e.getMessage());
//        }
//
//        return result;
//
//    }

    public RequestInputResponse RequestBuy(String sCustomerAccountNumber,
                                           String sCustomerMobileNumber, String dProductPrice,
                                           String sProductName, String sRemark, String sClientTxID,
                                           String sOtherParameter,String sTS, String sEncKey, String sResponseID, String sResponseStatus) {
        RequestInputResponse result = new RequestInputResponse();
        RTCallWS cs = new RTCallWS();

        RequestInput r = new RequestInput();
        r.setdProductPrice(dProductPrice);
        r.setsProductName(sProductName);
        r.setsClientPassword(SharedPreferenceUtil.getsClientPassword());
        r.setsClientUserName(SharedPreferenceUtil.getsClientUserName());
        r.setsClientTxID(sClientTxID);
        r.setsCustomerAccountNumber(sCustomerAccountNumber);
        r.setsCustomerMobileNumber(SharedPreferenceUtil.getsClientUserName() );//Change from sCustomerAccountNumber to SharedPreferenceUtil.getsClientUserName()
        r.setsDealerMobileNumber(SharedPreferenceUtil.getsClientUserName());
        r.setsRemark(sRemark);
        r.setsTS(sTS);
        r.setsEnckey(sEncKey);
        r.setsOtherParameter(sOtherParameter);
        r.setsResponseID(sResponseID);
        r.setsResponseStatus(sResponseStatus);

        try {
            RTSoap response = cs.RequestBuy(r);
            if (response != null) {
                if (response.getProperties() != null) {
                    for (RTSoapProperty element : response.getProperties()) {
                        if (element != null) {
                            if ("RequestInputResult".equalsIgnoreCase(element
                                    .getName()))
                                result.setRequestInputResult(element
                                        .getBooleanValue());
                            if ("sResponseID".equalsIgnoreCase(element
                                    .getName()))
                                result.setsResponseID(element.getValue());
                            if ("sResponseStatus".equalsIgnoreCase(element
                                    .getName()))
                                result.setsResponseStatus(element.getValue());
                        }

                    }
                }
            }
        } catch (Exception e) {
            DLog.e(TAG, "" + e.getMessage());
            return null;
        }

        return result;
    }

    public RTResponse ShareCredit(String sClientUserName,String sClientPassword, String sCustomerMobile, String sCustomerAmount,String sTS, String sEncKey) {

        RTCallWS cs = new RTCallWS();
        RTResponse result = new RTResponse();

        DLog.e(TAG, "sClientUserName " + sClientUserName);
        DLog.e(TAG, "sClientPassword " + sClientPassword);
        DLog.e(TAG, "sCustomerMobile " + sCustomerMobile);
        DLog.e(TAG, "sCustomerAmount " + sCustomerAmount);
        DLog.e(TAG, "sTS " + sTS);
        DLog.e(TAG, "sEncKey " + sEncKey);


        try {
            RTSoap response = cs.ShareCredit(sClientUserName, sClientPassword,sCustomerMobile, sCustomerAmount,sTS,sEncKey);
            if (response != null) {
                if (response.getProperties() != null) {
                    for (RTSoapProperty element : response.getProperties()) {
                        if ("ShareCreditResult".equalsIgnoreCase(element
                                .getName()))
                            result.setResultBoolean(element.getBooleanValue());
                        if ("sResponseStatus".equalsIgnoreCase(element
                                .getName()))
                            result.setsResponseMessage(element.getValue());
                    }
                }
            }
        } catch (Exception e) {
            DLog.e(TAG, "" + e.getMessage());
        }

        return result;

    }

    public List<AgentSalesByMSISDN> GetAgentSalesByMSISDN(String sClientUserName,
                                                          String sClientPassword, String sSelectedMSISDN, String sSDate, String sTimeStart, String sTimeEnd) {
        RTCallWS cs = new RTCallWS();
        List<AgentSalesByMSISDN> result = new ArrayList<AgentSalesByMSISDN>();
        DLog.e(TAG, "sClientUserName " + sClientUserName);
        DLog.e(TAG, "sClientPassword " + sClientPassword);
        DLog.e(TAG, "sCreditType " + sSelectedMSISDN);
        DLog.e(TAG, "sSDate " + sSDate);

        try {
            RTSoap response = cs.GetAgentSalesByMSISDN(sClientUserName, sClientPassword, sSelectedMSISDN, sSDate, sTimeStart, sTimeEnd);
            if (response != null) {
                if (response.getProperties() != null) {
                    for (RTSoapProperty tag : response.getProperties()) {
                        if (tag != null)
                            if (tag.getName().equalsIgnoreCase(
                                    "GetAgentSalesByMSISDNResult"))
                                result = WSBinder.bindAgentSalesByMSISDN(tag);
                    }
                }
            }
        } catch (Exception e) {
            DLog.e(TAG, "" + e.getMessage());
            return null;
        }

        return result;

    }

    public String QueryAccount(String accountNumber) {
        DLog.e(TAG, "sClientUserName " + accountNumber);
        try {
            RTCallWS cs = new RTCallWS();
            RTSoap response = cs.QueryAccount(accountNumber);

            if (response != null) {
                if (response.getProperties() != null) {
                    for (RTSoapProperty element : response.getProperties()) {
                        if ("QueryAccountResult".equalsIgnoreCase(element
                                .getName()))
                            return element.getValue();
                    }
                }
            }
        } catch (Exception e) {
            DLog.e(TAG, "" + e.getMessage());
            return "";
        }
        return "";
    }

    public List<CustomerTopupTx> CheckUserTopupTxByType(String sClientUserName,
                                                        String sClientPassword, String sCreditType, String sSDate, String sEDate) {
        RTCallWS cs = new RTCallWS();
        List<CustomerTopupTx> result = new ArrayList<CustomerTopupTx>();
        DLog.e(TAG, "sClientUserName " + sClientUserName);
        DLog.e(TAG, "sClientPassword " + sClientPassword);
        DLog.e(TAG, "sCreditType " + sCreditType);
        DLog.e(TAG, "sSDate " + sSDate);
        DLog.e(TAG, "sEDate " + sEDate);

        try {
            RTSoap response = cs.CheckUserTopupTxByType(sClientUserName, sClientPassword,
                    sCreditType, sSDate, sEDate);

            if (response != null) {
                if (response.getProperties() != null) {
                    for (RTSoapProperty tag : response.getProperties()) {
                        if (tag != null)
                            if (tag.getName().equalsIgnoreCase(
                                    "CheckUserTopupTxByTypeResult"))
                                result = WSBinder.bindCheckUserTopupTxByType(tag);
                    }
                }
            }
        } catch (Exception e) {
            DLog.e(TAG, "" + e.getMessage());
            return null;
        }

        return result;

    }

    public List<BankIn> GetBankInList(String sClientUserName,
                                      String sClientPassword, String sTS, String sEncKey) {
        RTCallWS cs = new RTCallWS();
        List<BankIn> result = new ArrayList<BankIn>();
        DLog.e(TAG, "sClientUserName " + sClientUserName);
        DLog.e(TAG, "sClientPassword " + sClientPassword);
        DLog.e(TAG, "sTS " + sTS);
        DLog.e(TAG, "sEncKey " + sEncKey);

        try {
            RTSoap response = cs.GetBankInList(sClientUserName, sClientPassword,
                    sTS, sEncKey);

            if (response != null) {
                if (response.getProperties() != null) {
                    for (RTSoapProperty tag : response.getProperties()) {
                        if (tag != null)
                            if (tag.getName().equalsIgnoreCase(
                                    "GetBankInListResult"))
                                result = WSBinder.bindBankIns(tag);
                    }
                }
            }
        } catch (Exception e) {
            DLog.e(TAG, "" + e.getMessage());
            return null;
        }

        return result;

    }


    public RTResponse UpdatePrintCount(String sClientUserName,
                                       String sLocalMOID, String sTS, String sEncKey) {
        RTCallWS cs = new RTCallWS();
        RTResponse result = new RTResponse();

        try {
            if (!FunctionUtil.isSet(sClientUserName))
                return null;

            DLog.e(TAG, "sClientUserName " + sClientUserName);
            DLog.e(TAG, "sLocalMOID " + sLocalMOID);
            DLog.e(TAG, "sTS " + sTS);
            DLog.e(TAG, "sEncKey " + sEncKey);

            RTSoap response = cs.UpdatePrintCount(sClientUserName,
                    sLocalMOID, sTS, sEncKey);
            if (response != null) {
                if (response.getProperties() != null) {
                    for (RTSoapProperty element : response.getProperties()) {
                        if ("RegisterDealerResult".equalsIgnoreCase(element
                                .getName()))
                            result.setResultBoolean(element.getBooleanValue());
                        if ("sResponseMessage".equalsIgnoreCase(element
                                .getName()))
                            result.setsResponseMessage(element.getValue());
                    }
                }
            }
        } catch (Exception e) {
            DLog.e(TAG, "" + e.getMessage());
            return null;
        }
        return result;
    }


    public RTResponse FBUpdateFBID(String sFBID,
                                   String sAgentID, String sMobileNumber, String sPassword, String sTS, String sEncKey) {
        RTCallWS cs = new RTCallWS();
        RTResponse result = new RTResponse();

        try {
            if (!FunctionUtil.isSet(sFBID))
                return null;


            DLog.e(TAG, "sFBID " + sFBID);
            DLog.e(TAG, "sAgentID " + sAgentID);
            DLog.e(TAG, "sMobileNumber " + sMobileNumber);
            DLog.e(TAG, "sPassword " + sPassword);
            DLog.e(TAG, "sTS " + sTS);
            DLog.e(TAG, "sEncKey " + sEncKey);

            RTSoap response = cs.FBUpdateFBID(sFBID,
                    sAgentID, sMobileNumber, sPassword, sTS, sEncKey);
            if (response != null) {
                if (response.getProperties() != null) {
                    for (RTSoapProperty element : response.getProperties()) {
                        if ("RegisterDealerResult".equalsIgnoreCase(element
                                .getName()))
                            result.setResultBoolean(element.getBooleanValue());
                        if ("sResponseMessage".equalsIgnoreCase(element
                                .getName()))
                            result.setsResponseMessage(element.getValue());
                    }
                }
            }
        } catch (Exception e) {
            DLog.e(TAG, "" + e.getMessage());
            return null;
        }
        return result;
    }

    public RTResponse RegisterDealer(String sDealerMobile, String sPassword,String sName,
                                     String sMobileNumber, String sLocation, String sEmail,String sReferrer,String sTS, String sEncKey) {
        RTCallWS cs = new RTCallWS();
        RTResponse result = new RTResponse();

        try {
            if (!FunctionUtil.isSet(sName))
                return null;
            if (!FunctionUtil.isSet(sMobileNumber))
                return null;

            DLog.e(TAG, "sName " + sName);
            DLog.e(TAG, "sMobileNumber " + sMobileNumber);
            DLog.e(TAG, "sLocation " + sLocation);
            DLog.e(TAG, "sEmail " + sEmail);
            DLog.e(TAG, "sReferrer " + sReferrer);
            DLog.e(TAG, "sTS " + sTS);
            DLog.e(TAG, "sEncKey " + sEncKey);

            RTSoap response = cs.RegisterDealer(sDealerMobile,sPassword,sName,
                    sMobileNumber, sLocation, sEmail, sReferrer, sTS, sEncKey);
            if (response != null) {
                if (response.getProperties() != null) {
                    for (RTSoapProperty element : response.getProperties()) {
                        if ("RegisterDealerResult".equalsIgnoreCase(element
                                .getName()))
                            result.setResultBoolean(element.getBooleanValue());
                        if ("sResponseMessage".equalsIgnoreCase(element
                                .getName()))
                            result.setsResponseMessage(element.getValue());
                    }
                }
            }
        } catch (Exception e) {
            DLog.e(TAG, "" + e.getMessage());
            return null;
        }
        return result;
    }

    public String GetMessageInfo(String sClientUserName,
                                 String sClientPassword, String sMessageType) {
        RTCallWS cs = new RTCallWS();
        String result = "";

        try {
            if (!FunctionUtil.isSet(sClientUserName)) {
                return null;
            }

            RTSoap response = cs.GetMessageInfo(sClientUserName,
                    sClientPassword, sMessageType);
            if (response != null) {
                if (response.getProperties() != null) {
                    for (RTSoapProperty element : response.getProperties()) {

                        if ("sResponseMessage".equalsIgnoreCase(element
                                .getName()))
                            result = element.getValue();
                    }
                }
            }
        } catch (Exception e) {
            DLog.e(TAG, "" + e.getMessage());
            return null;
        }

        return result;

    }

    public CustomerTxStatusInfo GetPinTxByDN(String sClientUserName,
                                             String sClientPassword, String sCustomerAccount, String sDNId) {
        RTCallWS cs = new RTCallWS();
        CustomerTxStatusInfo result = new CustomerTxStatusInfo();
        DLog.e(TAG, "sClientUserName " + sClientUserName);
        DLog.e(TAG, "sClientPassword " + sClientPassword);
        DLog.e(TAG, "sCustomerAccount " + sCustomerAccount);
        DLog.e(TAG, "sDNId " + sDNId);

        try {
            RTSoap response = cs.GetPinTxByDN(sClientUserName, sClientPassword,
                    sCustomerAccount, sDNId);
            if (response != null) {
                if (response.getProperties() != null) {
                    for (RTSoapProperty e : response.getProperties()) {
                        for (RTSoapProperty element : e.getProperties()) {
                            // result = WSBinder.bindCustomerTxStatusInfos(tag);
                            if ("sReloadMSISDN".equalsIgnoreCase(element
                                    .getName()))
                                result.setsReloadMSISDN(element.getValue());

                            if ("LocalMOID".equalsIgnoreCase(element.getName()))
                                result.setLocalMOID(element.getValue());

                            if ("Product".equalsIgnoreCase(element.getName()))
                                result.setProduct(element.getValue());

                            if ("Amount".equalsIgnoreCase(element.getName()))
                                result.setAmount(element.getValue());

                            if ("Status".equalsIgnoreCase(element.getName()))
                                result.setStatus(element.getValue());

                            if ("DN".equalsIgnoreCase(element.getName()))
                                result.setDN(element.getValue());

                            if ("Code".equalsIgnoreCase(element.getName()))
                                result.setCode(element.getValue());

                            if ("DateTime".equalsIgnoreCase(element.getName()))
                                result.setDateTime(element.getValue());
                        }
                    }
                }
            }
        } catch (Exception e) {
            DLog.e(TAG, "" + e.getMessage());
            return null;
        }

        return result;

    }

    public MMExChangeRateResult GetExChangeRateById(String sClientUserName,
                                                    String sClientPassword, int mId) {
        RTCallWS cs = new RTCallWS();
        MMExChangeRateResult result = new MMExChangeRateResult();
        DLog.e(TAG, "sClientUserName " + sClientUserName);
        DLog.e(TAG, "sClientPassword " + sClientPassword);
        DLog.e(TAG, "mId " + mId);

        RTSoap response = cs.GetExChangeRateById(sClientUserName,
                sClientPassword, mId);
        try {
            if (response != null) {
                if (response.getProperties() != null) {
                    for (RTSoapProperty e : response.getProperties()) {
                        for (RTSoapProperty element : e.getProperties()) {

                            if ("mID".equalsIgnoreCase(element.getName()))
                                result.setmID(element.getIntValue());

                            if ("LocalCurrency".equalsIgnoreCase(element
                                    .getName()))
                                result.setLocalCurrency(element.getValue());

                            if ("LocalAmount".equalsIgnoreCase(element
                                    .getName()))
                                result.setLocalAmount(element.getValue());

                            if ("ForeignCountry".equalsIgnoreCase(element
                                    .getName()))
                                result.setForeignCountry(element.getValue());

                            if ("ForeignCurrency".equalsIgnoreCase(element
                                    .getName()))
                                result.setForeignCurrency(element.getValue());

                            if ("ForeignAmount".equalsIgnoreCase(element
                                    .getName()))
                                result.setForeignAmount(element.getValue());

                            if ("Status".equalsIgnoreCase(element.getName()))
                                result.setStatus(element.getValue());

                            if ("LastUpdated".equalsIgnoreCase(element
                                    .getName()))
                                result.setLastUpdated(element.getValue());

                        }
                    }
                }
            }
        } catch (Exception e) {
            DLog.e(TAG, "" + e.getMessage());
            return null;
        }
        return result;

    }

    public boolean InsertMobileMobilemessage(String sClientUserName,
                                             String sClientPassword, String MessageStatus, String sMessage,
                                             String dateTime, String AgentId, String MSISDN) {
        RTCallWS cs = new RTCallWS();
        DLog.e(TAG, "sClientUserName " + sClientUserName);
        DLog.e(TAG, "sClientPassword " + sClientPassword);
        DLog.e(TAG, "MessageStatus " + MessageStatus);
        DLog.e(TAG, "sMessage " + sMessage);
        DLog.e(TAG, "dateTime " + dateTime);
        DLog.e(TAG, "AgentId " + AgentId);
        DLog.e(TAG, "MSISDN " + MSISDN);

        try {
            RTSoap response = cs.InsertMobileMobilemessage(sClientUserName,
                    sClientPassword, MessageStatus, sMessage, dateTime,
                    AgentId, MSISDN);
            if (response != null) {
                if (response.getProperties() != null) {
                    for (RTSoapProperty element : response.getProperties()) {
                        DLog.e(TAG, "" + element.getBooleanValue());
                        return element.getBooleanValue();
                    }
                }
            }
        } catch (Exception e) {
            DLog.e(TAG, "" + e.getMessage());
            return false;
        }

        return false;

    }

    public boolean DeviceVerifyTAC(String sClientUserName,
                                   String sClientPassword, String sDeviceID, int sClientID, String sTAC, String sTS, String sEncKey) {
        RTCallWS cs = new RTCallWS();
        DLog.e(TAG, "sClientUserName " + sClientUserName);
        DLog.e(TAG, "sClientPassword " + sClientPassword);
        DLog.e(TAG, "sDeviceID " + sDeviceID);
        DLog.e(TAG, "sClientID " + sClientID);
        DLog.e(TAG, "sTAC " + sTAC);
        DLog.e(TAG, "sTAC " + sTAC);
        try {
            RTSoap response = cs.DeviceVerifyTAC(sClientUserName,
                    sClientPassword, sClientID, sDeviceID, sTAC,sTS,sEncKey);
            if (response != null) {
                if (response.getProperties() != null) {
                    for (RTSoapProperty element : response.getProperties()) {
                        DLog.e(TAG, "" + element.getBooleanValue());
                        return element.getBooleanValue();
                    }
                }
            }
        } catch (Exception e) {
            DLog.e(TAG, "" + e.getMessage());
            return false;
        }

        return false;

    }

//    public boolean DeviceVerifyTAC(String sClientUserName,
//                                   String sClientPassword, String sDeviceID, int sClientID, String sTAC) {
//        RTCallWS cs = new RTCallWS();
//        DLog.e(TAG, "sClientUserName " + sClientUserName);
//        DLog.e(TAG, "sClientPassword " + sClientPassword);
//        DLog.e(TAG, "sDeviceID " + sDeviceID);
//        DLog.e(TAG, "sClientID " + sClientID);
//        DLog.e(TAG, "sTAC " + sTAC);
//        try {
//            RTSoap response = cs.DeviceVerifyTAC(sClientUserName,
//                    sClientPassword, sClientID, sDeviceID, sTAC);
//            if (response != null) {
//                if (response.getProperties() != null) {
//                    for (RTSoapProperty element : response.getProperties()) {
//                        DLog.e(TAG, "" + element.getBooleanValue());
//                        return element.getBooleanValue();
//                    }
//                }
//            }
//        } catch (Exception e) {
//            DLog.e(TAG, "" + e.getMessage());
//            return false;
//        }
//
//        return false;
//
//    }

    public boolean UpdatePushNotificationID(String sClientUserName,
                                            String sClientPassword, String sPNID) {
        RTCallWS cs = new RTCallWS();

        DLog.e(TAG, "sClientUserName " + sClientUserName);
        DLog.e(TAG, "sClientPassword " + sClientPassword);
        DLog.e(TAG, "sPNID " + sPNID);

        RTSoap response = cs.UpdatePushNotificationID(sClientUserName,
                sClientPassword, sPNID);
        try {
            if (response != null) {
                if (response.getProperties() != null) {
                    for (RTSoapProperty element : response.getProperties()) {
                        DLog.e(TAG, "" + element.getBooleanValue());
                        return element.getBooleanValue();
                    }
                }
            }
        } catch (Exception e) {
            DLog.e(TAG, "" + e.getMessage());
            return false;
        }
        return true;

    }

    public boolean changePassword(String sClientUserName,
                                  String sClientPassword, String sNewPassword) {
        RTCallWS cs = new RTCallWS();

        DLog.e(TAG, "sClientUserName " + sClientUserName);
        DLog.e(TAG, "sClientPassword " + sClientPassword);
        DLog.e(TAG, "sNewPassword " + sNewPassword);

        RTSoap response = cs.ChangePassword(sClientUserName, sClientPassword,
                sNewPassword);
        try {
            if (response != null) {
                if (response.getProperties() != null) {
                    for (RTSoapProperty element : response.getProperties()) {
                        DLog.e(TAG, "" + element.getBooleanValue());
                        return element.getBooleanValue();
                    }
                }
            }
        } catch (Exception e) {
            DLog.e(TAG, "" + e.getMessage());
            return false;
        }
        return true;

    }

    public boolean getLogLogin(String sClientUserName, String sClientPassword,
                               String IPAddress, String LoginStatus, String LoginChannel,
                               String sGPSLocation) {
        RTCallWS cs = new RTCallWS();

        DLog.e(TAG, "sClientUserName " + sClientUserName);
        DLog.e(TAG, "sClientPassword " + sClientPassword);
        DLog.e(TAG, "IPAddress " + IPAddress);
        DLog.e(TAG, "LoginStatus " + LoginStatus);
        DLog.e(TAG, "LoginChannel " + LoginChannel);
        DLog.e(TAG, "sGPSLocation " + sGPSLocation);

        RTSoap response = cs.getLogLogin(sClientUserName, sClientPassword,
                IPAddress, LoginStatus, LoginChannel, sGPSLocation);
        try {
            if (response != null) {
                if (response.getProperties() != null) {
                    for (RTSoapProperty element : response.getProperties()) {
                        DLog.e(TAG, "" + element.getBooleanValue());
                        return element.getBooleanValue();
                    }
                }
            }
        } catch (Exception e) {
            DLog.e(TAG, "" + e.getMessage());
            return false;
        }
        return true;

    }

    public RequestReloadPinObject GetReloadPINImmediate(String sClientUserName,
                                                        String sClientPassword, String sLocalMOID) {
        RTCallWS cs = new RTCallWS();
        RequestReloadPinObject result = new RequestReloadPinObject();
        DLog.e(TAG, "sClientUserName " + sClientUserName);
        DLog.e(TAG, "sClientPassword " + sClientPassword);
        DLog.e(TAG, "sLocalMOID " + sLocalMOID);

        RTSoap response = cs.GetReloadPINImmediate(sClientUserName,
                sClientPassword, sLocalMOID);
        try {
            if (response != null) {
                if (response.getProperties() != null) {
                    for (RTSoapProperty element : response.getProperties()) {

                        if ("GetReloadPINImmediateResult"
                                .equalsIgnoreCase(element.getName()))
                            result.setGetReloadPINResult(element
                                    .getBooleanValue());
                        if ("sSerialNumber".equalsIgnoreCase(element.getName()))
                            result.setsSerialNumber(element.getValue());
                        if ("sReloadPin".equalsIgnoreCase(element.getName()))
                            result.setsReloadPin(element.getValue());
                        if ("sExpiryDate".equalsIgnoreCase(element.getName()))
                            result.setsExpiryDate(element.getValue());

                        if ("sReloadTelco".equalsIgnoreCase(element.getName()))
                            result.setsReloadTelco(element.getValue());

                        if ("sAmount".equalsIgnoreCase(element.getName()))
                            result.setsAmount(element.getValue());

                        if ("sInstruction".equalsIgnoreCase(element.getName()))
                            result.setsInstruction(element.getValue());

                        if ("sDescription".equalsIgnoreCase(element.getName()))
                            result.setsDescription(element.getValue());

                        if ("sDNReceivedID".equalsIgnoreCase(element.getName())) {
                            result.setsDNReceivedID(element.getValue());
                        }

                        if ("sBatchID".equalsIgnoreCase(element.getName())) {
                            result.setsBatchID(element.getValue());
                        }

                    }
                }
            }
        } catch (Exception e) {
            DLog.e(TAG, "" + e.getMessage());
            return null;
        }
        return result;

    }

    public RequestReloadPinObject getReloadPIN(String sClientUserName,
                                               String sClientPassword, String sLocalMOID) {
        RTCallWS cs = new RTCallWS();
        RequestReloadPinObject result = new RequestReloadPinObject();
        DLog.e(TAG, "sClientUserName " + sClientUserName);
        DLog.e(TAG, "sClientPassword " + sClientPassword);
        DLog.e(TAG, "sLocalMOID " + sLocalMOID);

        RTSoap response = cs.getReloadPIN(sClientUserName, sClientPassword,
                sLocalMOID);
        try {
            if (response != null) {
                if (response.getProperties() != null) {
                    for (RTSoapProperty element : response.getProperties()) {

                        if ("GetReloadPINResult".equalsIgnoreCase(element
                                .getName()))
                            result.setGetReloadPINResult(element
                                    .getBooleanValue());
                        if ("sSerialNumber".equalsIgnoreCase(element.getName()))
                            result.setsSerialNumber(element.getValue());
                        if ("sReloadPin".equalsIgnoreCase(element.getName()))
                            result.setsReloadPin(element.getValue());
                        if ("sExpiryDate".equalsIgnoreCase(element.getName()))
                            result.setsExpiryDate(element.getValue());

                        // if (FunctionUtil.isSet(element.getName()))
                        // if ("sExpiryDate".equalsIgnoreCase(element
                        // .getName()))
                        // result.setsExpiryDate(element.getValue());

                        if ("sReloadTelco".equalsIgnoreCase(element.getName()))
                            result.setsReloadTelco(element.getValue());

                        if ("sAmount".equalsIgnoreCase(element.getName()))
                            result.setsAmount(element.getValue());

                        if ("sInstruction".equalsIgnoreCase(element.getName()))
                            result.setsInstruction(element.getValue());

                        if ("sDescription".equalsIgnoreCase(element.getName()))
                            result.setsDescription(element.getValue());

                        if ("sPurchaseTS".equalsIgnoreCase(element.getName()))
                            result.setsPurchaseTS(element.getValue());

                        if ("sRetailPrice".equalsIgnoreCase(element.getName()))
                            result.setsRetailPrice(element.getValue());

                        if ("sPINID".equalsIgnoreCase(element.getName())) {
                            if (FunctionUtil.isSet(element.getValue()))
                                result.setsDNReceivedID(element.getValue());
                            else
                                result.setsDNReceivedID("");
                        }

                    }
                }
            }
        } catch (Exception e) {
            DLog.e(TAG, "" + e.getMessage());
            return null;
        }
        return result;

    }

    public CustomerInputApproveResponseObj getCustomerInputApprove(
            String sClientUserName, String sClientPassword, String sTxID,
            String sResponseID) {
        RTCallWS cs = new RTCallWS();
        CustomerInputApproveResponseObj result = new CustomerInputApproveResponseObj();
        DLog.e(TAG, "sClientUserName " + sClientUserName);
        DLog.e(TAG, "sClientPassword " + sClientPassword);
        DLog.e(TAG, "sTxID " + sTxID);
        DLog.e(TAG, "sResponseID " + sResponseID);
        RTSoap response = cs.getCustomerInputApprove(sClientUserName,
                sClientPassword, sTxID, sResponseID);
        try {
            if (response != null) {
                if (response.getProperties() != null) {
                    for (RTSoapProperty element : response.getProperties()) {
                        if ("CustomerInputApproveResult"
                                .equalsIgnoreCase(element.getName()))
                            result.setCustomerInputApproveResult(element
                                    .getBooleanValue());
                        if ("sTxID".equalsIgnoreCase(element.getName()))
                            result.setsTxID(element.getValue());
                        if ("sResponseID".equalsIgnoreCase(element.getName()))
                            result.setsResponseID(element.getValue());
                        if ("sResponseStatus".equalsIgnoreCase(element
                                .getName()))
                            result.setsResponseStatus(element.getValue());
                    }
                }
            }
        } catch (Exception e) {
            DLog.e(TAG, "" + e.getMessage());
            return null;
        }
        return result;

    }

    public List<CustomerInputList> getCustomerInputList(String sClientUserName,
                                                        String sClientPassword) {
        RTCallWS cs = new RTCallWS();
        List<CustomerInputList> result = new ArrayList<CustomerInputList>();
        DLog.e(TAG, "sClientUserName " + sClientUserName);
        DLog.e(TAG, "sClientPassword " + sClientPassword);

        try {
            RTSoap response = cs.getCustomerInputList(sClientUserName,
                    sClientPassword);
            if (response != null) {
                if (response.getProperties() != null) {
                    for (RTSoapProperty tag : response.getProperties()) {
                        if (tag != null)
                            if (tag.getName().equalsIgnoreCase(
                                    "CustomerInputListResult"))
                                result = WSBinder.bindCustomerInputLists(tag);
                    }
                }
            }
        } catch (Exception e) {
            DLog.e(TAG, "" + e.getMessage());
            return null;
        }
        return result;

    }

    public RequestInputResponse CustomerInput(String sCustomerAccountNumber,
                                              String sCustomerMobileNumber, String dProductPrice,
                                              String sProductID, String sRemark, String sClientTxID,
                                              String sOtherParameter, String sResponseID, String sResponseStatus) {
        RequestInputResponse result = new RequestInputResponse();
        RTCallWS cs = new RTCallWS();
        try {
            RequestInput r = new RequestInput();
            r.setsProductID(sProductID);
            r.setdProductPrice(dProductPrice);
            r.setsClientPassword(SharedPreferenceUtil.getsClientPassword());
            r.setsClientUserName(SharedPreferenceUtil.getsClientUserName());
            // r.setsClientTxID(sClientTxID);
            r.setsCustomerAccountNumber(sCustomerAccountNumber);
            r.setsCustomerMobileNumber(sCustomerMobileNumber);
            r.setsDealerMobileNumber(SharedPreferenceUtil.getsClientUserName());
            r.setsRemark(sRemark);
            r.setsOtherParameter(sOtherParameter);
            r.setsResponseID(sResponseID);
            r.setsResponseStatus(sResponseStatus);

            RTSoap response = cs.CustomerInput(r);
            if (response != null) {
                if (response.getProperties() != null) {
                    for (RTSoapProperty element : response.getProperties()) {
                        if ("RequestInputResult".equalsIgnoreCase(element
                                .getName()))
                            result.setRequestInputResult(element
                                    .getBooleanValue());
                        if ("sResponseID".equalsIgnoreCase(element.getName()))
                            result.setsResponseID(element.getValue());
                        if ("sResponseStatus".equalsIgnoreCase(element
                                .getName()))
                            result.setsResponseStatus(element.getValue());
                    }
                }
            }
        } catch (Exception e) {
            DLog.e(TAG, "" + e.getMessage());
            return null;
        }

        return result;
    }

    public String GetAgentSalesProfit(String sClientUserName,
                                      String sClientPassword, String sSDate, String sEDate) {
        RTCallWS cs = new RTCallWS();
        AgentProductRebate result = new AgentProductRebate();
        DLog.e(TAG, "sClientUserName " + sClientUserName);
        DLog.e(TAG, "sClientPassword " + sClientPassword);
        DLog.e(TAG, "sSDate " + sSDate);
        DLog.e(TAG, "sEDate " + sEDate);

        try {
            RTSoap response = cs.GetAgentSalesProfit(sClientUserName,
                    sClientPassword, sSDate, sEDate);
            if (response != null) {
                if (response.getProperties() != null) {
                    for (RTSoapProperty element : response.getProperties()) {
                        if ("GetAgentSalesProfitResult"
                                .equalsIgnoreCase(element.getName()))
                            return element.getValue();

                    }
                }
            }
        } catch (Exception e) {
            DLog.e(TAG, "" + e.getMessage());
            return null;
        }

        return "";

    }

    public List<AgentSales> GetAgentSales(String sClientUserName,
                                          String sClientPassword, String sSDate, String sEDate) {
        RTCallWS cs = new RTCallWS();
        List<AgentSales> result = new ArrayList<AgentSales>();
        DLog.e(TAG, "sClientUserName " + sClientUserName);
        DLog.e(TAG, "sClientPassword " + sClientPassword);
        DLog.e(TAG, "sSDate " + sSDate);
        DLog.e(TAG, "sEDate " + sEDate);

        try {
            RTSoap response = cs.GetAgentSales(sClientUserName,
                    sClientPassword, sSDate, sEDate);
            if (response != null) {
                if (response.getProperties() != null) {
                    for (RTSoapProperty tag : response.getProperties()) {
                        if (tag != null)
                            result = WSBinder.bindAgentSaleInfos(tag);
                    }
                }
            }
        } catch (Exception e) {
            DLog.e(TAG, "" + e.getMessage());
            return null;
        }

        return result;

    }

    public List<AgentProductRebate> GetAgentProductRebate(
            String sClientUserName, String sClientPassword, int sProductID) {
        RTCallWS cs = new RTCallWS();
        AgentProductRebate result = new AgentProductRebate();
        DLog.e(TAG, "sClientUserName " + sClientUserName);
        DLog.e(TAG, "sClientPassword " + sClientPassword);
        DLog.e(TAG, "sProductID " + sProductID);

        List<AgentProductRebate> results = new ArrayList<AgentProductRebate>();

        try {
            RTSoap response = cs.GetAgentProductRebate(sClientUserName,
                    sClientPassword, sProductID);
            if (response != null) {
                if (response.getProperties() != null) {
                    for (RTSoapProperty tag : response.getProperties()) {
                        if (tag != null) {
                            results = WSBinder.bindAgentProductRebates(tag);
                        }
                    }
                }
            }
        } catch (Exception e) {
            DLog.e(TAG, "" + e.getMessage());
            return null;
        }

        return results;

    }

    public List<AgentProductDiscount> GetAgentProductDiscount(
            String sClientUserName, String sClientPassword) {
        RTCallWS cs = new RTCallWS();
        List<AgentProductDiscount> result = new ArrayList<AgentProductDiscount>();
        DLog.e(TAG, "sClientUserName " + sClientUserName);
        DLog.e(TAG, "sClientPassword " + sClientPassword);

        try {
            RTSoap response = cs.GetAgentProductDiscount(sClientUserName,
                    sClientPassword);
            if (response != null) {
                if (response.getProperties() != null) {
                    for (RTSoapProperty tag : response.getProperties()) {
                        if (tag != null)
                            result = WSBinder.bindAgentProductDiscounts(tag);
                    }
                }
            }
        } catch (Exception e) {
            DLog.e(TAG, "" + e.getMessage());
            return null;
        }

        return result;

    }

    public Boolean SubmitBankIn(String sClientUserName, String sClientPassword,
                                String sBank, String sAmount, String sDate, String sTime,
                                String sbiCode) {
        RTCallWS cs = new RTCallWS();
        boolean result = false;
        DLog.e(TAG, "sClientUserName " + sClientUserName);
        DLog.e(TAG, "sClientPassword " + sClientPassword);
        DLog.e(TAG, "sBank " + sBank);
        DLog.e(TAG, "sAmount " + sAmount);
        DLog.e(TAG, "sDate " + sDate);
        DLog.e(TAG, "sTime " + sTime);
        DLog.e(TAG, "sbiCode " + sbiCode);
        try {
            RTSoap response = cs.SubmitBankIn(sClientUserName, sClientPassword,
                    sBank, sAmount, sDate, sTime, sbiCode);
            if (response != null) {
                if (response.getProperties() != null) {
                    for (RTSoapProperty element : response.getProperties()) {
                        if ("SubmitBankInBiCodeResult".equalsIgnoreCase(element
                                .getName()))
                            result = element.getBooleanValue();
                    }
                }
            }
        } catch (Exception e) {
            DLog.e(TAG, "" + e.getMessage());
            return null;
        }

        return result;

    }

    public List<CustomerTxStatusInfo> CheckCustomerTxStatus(
            String sClientUserName, String sClientPassword,
            String sCustomerAccount, String sSDate, String sEDate) {
        RTCallWS cs = new RTCallWS();
        List<CustomerTxStatusInfo> result = null;
        DLog.e(TAG, "sClientUserName " + sClientUserName);
        DLog.e(TAG, "sClientPassword " + sClientPassword);
        DLog.e(TAG, "sCustomerAccount " + sCustomerAccount);
        DLog.e(TAG, "sSDate " + sSDate);
        DLog.e(TAG, "sEDate " + sEDate);

        try {
            RTSoap response = cs.CheckCustomerTxStatus(sClientUserName,
                    sClientPassword, sCustomerAccount, sSDate, sEDate);
            if (response != null) {
                if (response.getProperties() != null) {
                    for (RTSoapProperty tag : response.getProperties()) {
                        if (tag != null)
                            result = WSBinder.bindCustomerTxStatusInfos(tag);
                    }
                }
            }
        } catch (Exception e) {
            DLog.e(TAG, "" + e.getMessage());
            return null;
        }

        return result;

    }

    public CheckBalanceResponse CheckBalance(String sClientUserName,
                                             String sClientPassword) {
        RTCallWS cs = new RTCallWS();
        CheckBalanceResponse result = new CheckBalanceResponse();
        DLog.e(TAG, "sClientUserName " + sClientUserName);
        DLog.e(TAG, "sClientPassword " + sClientPassword);

        try {
            RTSoap response = cs.CheckBalance(sClientUserName, sClientPassword);
            if (response != null) {
                if (response.getProperties() != null) {
                    for (RTSoapProperty element : response.getProperties()) {
                        if ("CheckBalanceResult".equalsIgnoreCase(element
                                .getName()))
                            result.setCheckBalanceResult(element
                                    .getBooleanValue());
                        if ("dBalance".equalsIgnoreCase(element.getName()))
                            result.setdBalance(element.getValue());
                        if ("sResponseID".equalsIgnoreCase(element.getName()))
                            result.setsResponseID(element.getIntValue());
                        if ("sResponseStatus".equalsIgnoreCase(element
                                .getName()))
                            result.setsResponseStatus(element.getValue());

                    }
                }
            }
        } catch (Exception e) {
            DLog.e(TAG, "" + e.getMessage());
            return null;
        }

        return result;

    }

    public DeviceLoginResponse DeviceLogin(String sClientUserName,
                                           String sClientPassword, String sDeviceID) {
        RTCallWS cs = new RTCallWS();
        DeviceLoginResponse result = new DeviceLoginResponse();
        LoginInfo login = new LoginInfo();
        login.setsClientPassword(sClientPassword);
        login.setsClientUserName(sClientUserName);
        login.setsDeviceID(sDeviceID);
        try {
            RTSoap response = cs.DeviceLogin(login);
            if (response != null) {


                if (response.getProperties() != null) {
                    for (RTSoapProperty element : response.getProperties()) {
                        if ("bVerifyTac".equalsIgnoreCase(element.getName()))
                            result.setbVerifyTac(element.getBooleanValue());
                        if ("DeviceLoginResult".equalsIgnoreCase(element
                                .getName()))
                            result.setDeviceLoginResult(element
                                    .getBooleanValue());
                        if ("sClientID".equalsIgnoreCase(element.getName()))
                            result.setsClientID(element.getIntValue());

                        if ("sMasterID".equalsIgnoreCase(element.getName()))
                            result.setsMasterID(element.getValue());

                        if ("sMerchantName".equalsIgnoreCase(element.getName()))
                            result.setsMerchantName(element.getValue());

                    }
                    return result;
                }
            }
        } catch (Exception e) {
            DLog.e(TAG, "" + e.getMessage());
            return null;
        }

        return null;

    }

    public List<ProductInfo> getProductInfo() {
        RTCallWS cs = new RTCallWS();
        List<ProductInfo> result = null;
        RTSoap response = cs.GetProductObject(
                SharedPreferenceUtil.getsClientUserName(),
                SharedPreferenceUtil.getsClientPassword());
        try {
            if (response != null) {
                if (response.getProperties() != null) {
                    for (RTSoapProperty element : response.getProperties()) {
                        result = WSBinder.bindProductInfos(element);
                    }
                }
            }
        } catch (Exception e) {
            DLog.e(TAG, "" + e.getMessage());
            return null;
        }

        return result;
    }

    public RequestInputResponse RequestInput(String sCustomerAccountNumber,
                                             String sCustomerMobileNumber, String dProductPrice,
                                             String sProductID, String sRemark, String sClientTxID,
                                             String sOtherParameter, String sDealerMobileNumber, String sResponseID, String sResponseStatus) {
        RequestInputResponse result = new RequestInputResponse();
        RTCallWS cs = new RTCallWS();

        RequestInput r = new RequestInput();
        r.setsProductID(sProductID);
        r.setdProductPrice(dProductPrice);
        r.setsClientPassword(SharedPreferenceUtil.getsClientPassword());
        r.setsClientUserName(SharedPreferenceUtil.getsClientUserName());
        r.setsClientTxID(sClientTxID);
        r.setsCustomerAccountNumber(sCustomerAccountNumber);
        r.setsCustomerMobileNumber(sCustomerMobileNumber);
        if (!FunctionUtil.isSet(sDealerMobileNumber)) {
            sDealerMobileNumber = SharedPreferenceUtil.getsClientUserName();
        }
        r.setsDealerMobileNumber(sDealerMobileNumber);
        r.setsRemark(sRemark);
        r.setsOtherParameter(sOtherParameter);
        r.setsResponseID(sResponseID);
        r.setsResponseStatus(sResponseStatus);

        try {
            RTSoap response = cs.RequestInput(r);
            if (response != null) {
                if (response.getProperties() != null) {
                    for (RTSoapProperty element : response.getProperties()) {
                        if (element != null) {
                            if ("RequestInputResult".equalsIgnoreCase(element
                                    .getName()))
                                result.setRequestInputResult(element
                                        .getBooleanValue());
                            if ("sResponseID".equalsIgnoreCase(element
                                    .getName()))
                                result.setsResponseID(element.getValue());
                            if ("sResponseStatus".equalsIgnoreCase(element
                                    .getName()))
                                result.setsResponseStatus(element.getValue());
                        }

                    }
                }
            }
        } catch (Exception e) {
            DLog.e(TAG, "" + e.getMessage());
//            addLog(r, e);
            return null;
        }

        return result;
    }


    String errCode;
    String Message;
    String ServerKey;
    String MSISDN;
    String Param;
    String Remark;

    void addLog(RequestInput r, Exception e) {
        try {
            DLog.e(TAG, "addLog");
            HttpHandlerHelper sh = new HttpHandlerHelper();
            errCode = "Err_Code=RequestInput";
//            if(e!=null)
//                errCode = "Err_Code=" + e.getMessage();
            Message = "&Message=" + URLEncoder.encode(e.getMessage(), "UTF-8");
            ServerKey = "&ServerKey=qpay99";
            MSISDN = "&MSISDN=" + SharedPreferenceUtil.getsClientUserName();
            Param = "&Param=" + r.getsCustomerMobileNumber() + "," + r.getsProductID() + "," + r.getdProductPrice();
            Remark = "&Remark=BUYPIN";
            String url = errCode + ServerKey + MSISDN + Param + Remark + Message;
            DLog.e(TAG, "Response from url: " + Config.ErrorLog_URL + url);
            String jsonStr = sh.makeServiceCall(Config.ErrorLog_URL + url, "POST");
            DLog.e(TAG, "Response from url: " + jsonStr);
        } catch (Exception e2) {
            DLog.e(TAG, "addLog Err " + e2.getMessage());
        }
    }

    void addLogTest(LoginInfo l) {
        try {
            HttpHandlerHelper sh = new HttpHandlerHelper();
            errCode = "Err_Code=12345";
            ServerKey = "&ServerKey=qpay99";
            MSISDN = "&MSISDN=" + SharedPreferenceUtil.getsClientUserName();
            Param = "&Param=" + l.sClientUserName + l.getsDeviceID();
            Remark = "&Remark=TESTONLY";
            String url = errCode + ServerKey + MSISDN + Param + Remark;
            DLog.e(TAG, "Response from url: " + Config.ErrorLog_URL + url);
            String jsonStr = sh.makeServiceCall(Config.ErrorLog_URL + url, "POST");
            DLog.e(TAG, "Response from jsonStr: " + jsonStr);
        } catch (Exception e) {
            DLog.e(TAG, "Response from Err: " + e.getMessage());
        }
    }

}
