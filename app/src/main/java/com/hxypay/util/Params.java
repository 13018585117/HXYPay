package com.hxypay.util;

import java.util.HashMap;
import java.util.Map;

public class Params {

    public static Map<String, String> loginParams(String login, String password, String version, String os) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("login", login);
        params.put("password", password);
        params.put("version", version);
        params.put("os", os);
        return params;
    }

    public static Map<String, String> smsCodeParams(String phoneNum) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("phoneNum", phoneNum);
        return params;
    }

    public static Map<String, String> exitParams() {
        Map<String, String> params = new HashMap<String, String>();
        return params;
    }

    public static Map<String, String> updatePhoneParams(String type, String code, String phoneNum) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("type", type);
        params.put("code", code);
        params.put("phoneNum", phoneNum);
        return params;
    }

    public static Map<String, String> updatePayPwdParams( String code, String phoneNum,String password) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("code", code);
        params.put("phoneNum", phoneNum);
        params.put("password", password);
        return params;
    }

    public static Map<String, String> exitLogin() {
        Map<String, String> params = new HashMap<String, String>();
        return params;
    }

    public static Map<String, String> homePlain() {
        Map<String, String> params = new HashMap<String, String>();
        return params;
    }


    public static Map<String, String> popNew() {
        Map<String, String> params = new HashMap<String, String>();
        return params;
    }

    public static Map<String, String> memberPopNew() {
        Map<String, String> params = new HashMap<String, String>();
        return params;
    }

    public static Map<String, String> regParams(String phoneNum, String password, String sendCode,
                                                String referee, String uuid, String version, String os) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("phoneNum", phoneNum);
        params.put("password", password);
        params.put("sendCode", sendCode);
        params.put("referee", referee);
        params.put("uuid", uuid);
        params.put("version", version);
        params.put("os", os);
        return params;
    }


    public static Map<String, String> verUpdateParams() {
        Map<String, String> params = new HashMap<String, String>();
        return params;
    }

    public static Map<String, String> uploadImg(String file) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("file", file);
        return params;
    }

    public static Map<String, String> forgetPwdParams(String phone, String password, String check_password, String code) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("phone", phone);
        params.put("password", password);
        params.put("check_password", check_password);
        params.put("code", code);
        return params;
    }

    public static Map<String, String> updatePwdParams(String loginPwd, String newLoginPwd, String confirmLoginPwd) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("loginPwd", loginPwd);
        params.put("newLoginPwd", newLoginPwd);
        params.put("confirmLoginPwd", confirmLoginPwd);
        return params;
    }

    public static Map<String, String> banner() {
        Map<String, String> params = new HashMap<String, String>();
        return params;
    }

    public static Map<String, String> cardTestMoneyAndOrderId(String type) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("type", type);
        return params;
    }

    public static Map<String, String> cardTestResultQuery(String orderId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("orderId", orderId);
        return params;
    }

    public static Map<String, String> cardTest(String name,String mobile,String idcard,String bankcard,String code,String order_id) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("name", name);
        params.put("mobile", mobile);
        params.put("idcard", idcard);
        params.put("bankcard", bankcard);
        params.put("code", code);
        params.put("order_id", order_id);
        return params;
    }

    public static Map<String, String> merchantInfoParams() {
        Map<String, String> params = new HashMap<String, String>();
        return params;
    }

    public static Map<String, String> AppVerParams(String type) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("type", type);
        return params;
    }

    public static Map<String, String> myBDParams() {
        Map<String, String> params = new HashMap<String, String>();
        return params;
    }

    public static Map<String, String> myMerchantParams() {
        Map<String, String> params = new HashMap<String, String>();
        return params;
    }

    public static Map<String, String> newsOrNotice() {
        Map<String, String> params = new HashMap<String, String>();
        return params;
    }

    public static Map<String, String> cardMgrParams() {
        Map<String, String> params = new HashMap<String, String>();
        return params;
    }
    public static Map<String, String> RouletteParams(String grade,String money,String numDay,String frequency) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("grade",grade);
        params.put("money",money);
        params.put("numDay",numDay);
        params.put("frequency",frequency);
        return params;
    }

    public static Map<String, String> merchantRankInfo() {
        Map<String, String> params = new HashMap<String, String>();
        return params;
    }

    public static Map<String, String> withdraw(String money, String pay_password) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("money", money);
        params.put("pay_password", pay_password);
        return params;
    }

    public static Map<String, String> hkBill(String limit) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("limit", limit);
        return params;
    }

    public static Map<String, String> hkBill1(String cardId, String limit) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("cardId", cardId);
        params.put("limit", limit);
        return params;
    }

    public static Map<String, String> hkBillDetail(String mid, String cardId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("mid", mid);
        params.put("cardId", cardId);
        return params;
    }

    public static Map<String, String> chargeTxBill(String cardID, String limit) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("cardID", cardID);
        params.put("limit", limit);
        return params;
    }

    public static Map<String, String> addCardParams(String bankCard, String name, String cvn2, String expDate, String phoneNum,
                                                    String billDay, String repaymentDay, String code) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("bankCard", bankCard);
        params.put("name", name);
        params.put("cvn2", cvn2);
        params.put("expDate", expDate);
        params.put("phoneNum", phoneNum);
        params.put("billDay", billDay);
        params.put("repaymentDay", repaymentDay);
        params.put("code", code);
        return params;
    }

    public static Map<String, String> updateCardParams(String cardID, String billDay, String repaymentDay) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("cardID", cardID);
        params.put("billDay", billDay);
        params.put("repaymentDay", repaymentDay);
        return params;
    }


    public static Map<String, String> delCardParams(String cardID) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("cardID", cardID);
        return params;
    }

    public static Map<String, String> cancelPlainParams(String cardId,String mid) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("cardId", cardId);
        params.put("mid", mid);
        return params;
    }

    public static Map<String, String> sj1(String type) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("type", type);
        return params;
    }

    public static Map<String, String> sj2(String pay, String type_id) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("pay", pay);
        params.put("type_id", type_id);
        return params;
    }

    public static Map<String, String> sj3(String orderId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("orderId", orderId);
        return params;
    }

    public static Map<String, String> merRealNameAuthParams(String phoneNum, String sendcode, String username, String idCard, String bankCard, String card_front,
                                                            String idcard_front, String idcard_back, String pay_password, String type) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("phoneNum", phoneNum);
        params.put("sendcode", sendcode);
        params.put("username", username);
        params.put("idCard", idCard);
        params.put("bankCard", bankCard);
        params.put("card_front", card_front);
        params.put("idcard_front", idcard_front);
        params.put("idcard_back", idcard_back);
        params.put("pay_password", pay_password);
        params.put("type", type);
        return params;
    }

    public static Map<String, String> updateMcc(String formNo, String mcc) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("formNo", formNo);
        params.put("mcc", mcc);
        return params;
    }

    public static Map<String, String> shareUrl() {
        Map<String, String> params = new HashMap<String, String>();
        return params;
    }

    public static Map<String, String> previewPlanDataParams(String type, String cardId, String repayAmount, String day1,
                                                            String day2, String num, String provincecity, String mcc) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("type", type);
        params.put("cardId", cardId);
        params.put("repayAmount", repayAmount);
        params.put("day1", day1);
        params.put("day2", day2);
        params.put("num", num);
        params.put("provincecity", provincecity);
        params.put("mcc", mcc);
        return params;
    }

    public static Map<String, String> previewPlanDataParams1(String type, String cardId, String repayAmount, String day, String num, String provincecity, String mcc) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("type", type);
        params.put("cardId", cardId);
        params.put("repayAmount", repayAmount);
        params.put("day", day);
        params.put("num", num);
        params.put("provincecity", provincecity);
        params.put("mcc", mcc);
        return params;
    }

    public static Map<String, String> createPlanParams(String type, String cardId, String repayment, String parentOrder, String provincecity, String allpayfee,String pay_password) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("type", type);
        params.put("cardId", cardId);
        params.put("repayment", repayment);
        params.put("parentOrder", parentOrder);
        params.put("provincecity", provincecity);
        params.put("allpayfee", allpayfee);
        params.put("pay_password", pay_password);
        return params;
    }

    public static Map<String, String> payPlanParams(String batchNo, String cardNo, String payAmount, String isNeedSafeInput, String cvn2, String expDate, String duplicatePayTimeStamp) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("batchNo", batchNo);
        params.put("cardNo", cardNo);
        params.put("payAmount", payAmount);
        params.put("isNeedSafeInput", isNeedSafeInput);
        params.put("cvn2", cvn2);
        params.put("expDate", expDate);
        params.put("duplicatePayTimeStamp", duplicatePayTimeStamp);
        return params;
    }

    public static Map<String, String> payComPlanParams(String batchNo, String cardNo, String repayAmount, String cvn2, String expDate, String service, String serviceOrderNo, String serviceValue) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("batchNo", batchNo);
        params.put("cardNo", cardNo);
        params.put("payAmount", repayAmount);
        params.put("cvn2", cvn2);
        params.put("expDate", expDate);
        params.put("service", service);
        params.put("serviceOrderNo", serviceOrderNo);
        params.put("serviceValue", serviceValue);
        return params;
    }

    public static Map<String, String> endPlanParams(String batchNo) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("batchNo", batchNo);
        return params;
    }
    public static Map<String, String> stopPlanParams(String cardId,String mid) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("cardId", cardId);
        params.put("mid",mid);
        return params;
    }

    public static Map<String, String> planListParams(String pageNum, String pageSize, String startTime, String endTime, String status) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("pageNo", pageNum);
        params.put("pageSize", pageSize);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("status", status);
        return params;
    }

    public static Map<String, String> planDetailParams(String batchNo, String status) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("batchNo", batchNo);
        params.put("status", status);
        return params;
    }

    public static Map<String, String> profitSummaryParams() {
        Map<String, String> params = new HashMap<String, String>();
        return params;
    }

    public static Map<String, String> profitParams(String limit) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("limit", limit);
        return params;
    }

    public static Map<String, String> profitDayParams(String days, String limit) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("days", days);
        params.put("limit", limit);
        return params;
    }

    public static Map<String, String> profitDetailParams(String pageNum, String pageSize, String startTime, String endTime) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("pageNo", pageNum);
        params.put("pageSize", pageSize);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        return params;
    }

    public static Map<String, String> fastPayParams(String card_id, String money, String pay_password,String channel,String provincecity,String uid) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("card_id", card_id);
        params.put("money", money);
        params.put("pay_password", pay_password);

        params.put("channel",channel);
        params.put("provincecity",provincecity);
        params.put("uid",uid);
        return params;
    }
    public static Map<String, String> FeedBackParams( String username,String phonenum,String content) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        params.put("phonenum", phonenum);
        params.put("content",content);
        return params;
    }


    public static Map<String, String> request_poundageParams( String money) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("money", money);
        return params;
    }

    public static Map<String, String> withdrawRecordParams(String pageNum, String pageSize, String startTime, String endTime) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("pageNo", pageNum);
        params.put("pageSize", pageSize);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        return params;
    }

    public static Map<String, String> fastPayInfo(String merchantNo, String bankAccountNo, String payAmount, String cvn2, String expDate, String province, String city, String acqCode) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("merchantNo", merchantNo);
        params.put("bankAccountNo", bankAccountNo);
        params.put("payAmount", payAmount);
        params.put("cvn2", cvn2);
        params.put("expDate", expDate);
        params.put("province", province);
        params.put("city", city);
        params.put("acqCode", acqCode);
        return params;
    }

    public static Map<String, String> fastPay(String serviceOrderNo, String smsCode, String merchantNo, String cardNo, String acqCode) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("serviceOrderNo", serviceOrderNo);
        params.put("smsCode", smsCode);
        params.put("merchantNo", merchantNo);
        params.put("cardNo", cardNo);
        params.put("acqCode", acqCode);
        return params;
    }

    public static Map<String, String> fastPaySms(String merchantNo, String cvn2, String expDate, String bankAccountNo) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("merchantNo", merchantNo);
        params.put("cvn2", cvn2);
        params.put("expired", expDate);
        params.put("bankAccountNo", bankAccountNo);
        return params;
    }

    public static Map<String, String> fastPayBindCard(String merchantNo, String cvn2, String expDate, String smsCode, String serviceOrderNo, String cardNo, String bankAccountNo, String payAmount, String acqCode) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("merchantNo", merchantNo);
        params.put("cvn2", cvn2);
        params.put("expired", expDate);
        params.put("smsCode", smsCode);
        params.put("serviceOrderNo", serviceOrderNo);
        params.put("cardNo", cardNo);
        params.put("bankAccountNo", bankAccountNo);
        params.put("payAmount", payAmount);
        params.put("acqCode", acqCode);
        return params;
    }

    public static Map<String, String> channelParams() {
        Map<String, String> params = new HashMap<String, String>();
        return params;
    }

    public static Map<String, String> mcc(String type) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("type", type);
        return params;
    }

    public static Map<String, String> rq(String cardId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("cardId", cardId);
        return params;
    }

    public static Map<String, String> fqChannelParams() {
        Map<String, String> params = new HashMap<String, String>();
        return params;
    }


    public static Map<String, String> server() {
        Map<String, String> params = new HashMap<String, String>();
        return params;
    }

    public static Map<String, String> dkParams() {
        Map<String, String> params = new HashMap<String, String>();
        return params;
    }

    public static Map<String, String> dkClickStatisticsParams(String smallLoanCode, String smallLoanClientNum) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("smallLoanCode", smallLoanCode);
        params.put("smallLoanClientNum", smallLoanClientNum);
        return params;
    }

    public static Map<String, String> appStartStatisticsParams(String exposureRate) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("exposureRate", exposureRate);
        return params;
    }

    public static Map<String, String> fq(String bankAccountNo, String payAmount, String acqCode, String stageType) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("bankAccountNo", bankAccountNo);
        params.put("payAmount", payAmount);
        params.put("acqCode", acqCode);
        params.put("stageType", stageType);
        return params;
    }

    public static Map<String, String> fqDetail(String payAmount, String acqCode) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("payAmount", payAmount);
        params.put("acqCode", acqCode);
        return params;
    }
}
