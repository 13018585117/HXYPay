package com.daoqidata.okhttputils.request;

/**
 * Description:
 * Author     : Xiccc.
 * Date       : 2019/1/17 14:32
 */

public class PayParamBean {
    /**
     * 支付宝
     */
    private String aliPayStr;

    /**
     * 微信
     */
    private String appid; //应用ID
    private String partnerid;//商户号
    private String prepayid; //预支付交易会话id
    private String noncestr; //随机字符串
    private String timestamp; //时间戳
    private String sign;   //签名

    /**
     * 订单号 根据这个查询自己服务器最终结果
     */
    private String out_trade_no;

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getAliPayStr() {
        return aliPayStr;
    }

    public void setAliPayStr(String aliPayStr) {
        this.aliPayStr = aliPayStr;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }


    @Override
    public String toString() {
        return "PayParamBean{" +
                "aliPayStr='" + aliPayStr + '\'' +
                ", appid='" + appid + '\'' +
                ", partnerid='" + partnerid + '\'' +
                ", prepayid='" + prepayid + '\'' +
                ", noncestr='" + noncestr + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", sign='" + sign + '\'' +
                ", out_trade_no='" + out_trade_no + '\'' +
                '}';
    }
}
