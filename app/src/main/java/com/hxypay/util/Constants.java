package com.hxypay.util;

import android.os.Environment;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 各种初始化信息
 */
public final class Constants {

    /**
     * JH sdcard中的根目录
     */
    public static final String DIR_JH_ROOT = Environment.getExternalStorageDirectory() + File.separator + "ZBPay" + File.separator;


    //测试
//    public static final String BASE_URL = "http://tp.freejian.top";
    //生产
    public static final String BASE_URL = "http://aliyunhxygj.jiameiit.com";
    //获取验证码
    public static final String SMS_CODE = BASE_URL + "/app/account/sms";
    //更改手机号
    public static final String UPDATE_PHONE = BASE_URL + "/app/account/updateUser";
    //注册
    public static final String REGIEST = BASE_URL + "/app/account/register";
    //找回密码
    public static final String FIND_PWD = BASE_URL + "/app/account/forget_pwd";
    //登录
    public static final String LOGIN = BASE_URL + "/app/account/login";
    //商户信息
    public static final String MERCHANT_INFO = BASE_URL + "/app/buyPos/info";
    //图片上传
    public static final String UPLOAD_IMG = BASE_URL + "/app/customer/uploadimg";
    //我的商户
    public static final String MY_MERCHANT_LIST = BASE_URL + "/app/buyPos/friend";
    //卡管理-信用卡
    public static final String CREDIT_CARD_LIST = BASE_URL + "/app/payment/credit_card";
    //周转金计算；
    public static final String ROULETTE = BASE_URL +"/app/buyPos/roulette";
    //卡管理-储蓄卡
    public static final String DEPOSIT_CARD_LIST = BASE_URL + "/app/payment/deposit_card";
    //删除卡片
    public static final String DEL_CARD = BASE_URL + "/app/payment/delete_card";
    //资讯
    public static final String NEWS = BASE_URL + "/app/buyPos/classroom";
    //公告
    public static final String NOTICE = BASE_URL + "/app/buyPos/sys_news";
    //首页广告
    public static final String BANNER = BASE_URL + "/app/buyPos/find";
    //首页更多广告
    public static final String MORE_BANNER = BASE_URL + "/app/buyPos/more_find";
    //启动页广告
    public static final String WEL_BG = BASE_URL + "/app/buyPos/startup_find";
    //弹窗消息
    public static final String POP_NEW = BASE_URL + "/app/buyPos/popup";
    //弹窗升级会员消息
    public static final String MEMBER_POP_NEW = BASE_URL + "/app/buyPos/member_news";
    //公告大全
    public static final String ANNOUNCEMENT = BASE_URL + "/app/buyPos/sys_news";
    //每日收益
    public static final String MERCHANT_PROFIT = BASE_URL + "/app/deposit/day_detail";
    //每日收益
    public static final String MERCHANT_PROFIT_DAY = BASE_URL + "/app/deposit/detail";
    //实名认证
    public static final String REAL_NAME_AUTH = BASE_URL + "/app/customer/customer_autonym";
    //添加卡片
    public static final String ADD_CARD = BASE_URL + "/app/payment/add_card";
    //修改卡片
    public static final String UPDATE_CARD = BASE_URL + "/app/payment/updateCard";
    //还款账单
    public static final String HK_BILLS = BASE_URL + "/app/plan_grounda/plan_list";
    //充值提现账单
    public static final String CHARGE_TX_BILLS = BASE_URL + "/app/payment/detail";
    //还款明细记录
    public static final String HK_TRADE_BILLS = BASE_URL + "/app/plan_grounda/history_plan_ground";
    //还款明细记录-详情
    public static final String HK_TRADE_BILLS_DETAIL = BASE_URL + "/app/plan_grounda/planDetail_ground";
    //生成计划
    public static final String CREATE_REPAY_PLAN = BASE_URL + "/app/plan_grounda/makePlan";
    //终止计划；
    public static final String STOP_PLAN = BASE_URL+"/app/plan_grounda/close_ground";
    //版本更新
    public static final String APP_VER = BASE_URL + "/app/buyPos/version";
    //预览计划根据还款时间
    public static final String PREVIEW_REPAY_PLAN1 = BASE_URL + "/app/plan_grounda/previewPlanDay";
    //    public static final String PREVIEW_REPAY_PLAN1 = BASE_URL + "/app/plan_grounda/previewPlanDayCs"; //测试
    //终止计划
    public static final String CANCEL_PLAIN = BASE_URL + "/app/plan_grounda/close_ground";
    //首页还款记录
    public static final String HOME_PLAIN = BASE_URL + "/app/buyPos/plan";
    //上传头像
    public static final String SEND_HEAD_PIC = BASE_URL + "/app/customer/upload_headPic";
    //修改支付密码
    public static final String UPDTATE_PAY_PWD = BASE_URL + "/app/account/updatePayPassword";
    //我的保单
    public static final String MY_BD = BASE_URL + "/app/buyPos/insurance";
    //我的团队
    public static final String MY_TD = BASE_URL +"/app/buyPos/team";
    //还款日期
    public static final String REPAY_DAY = BASE_URL + "/app/plan_grounda/days";
    //提现
    public static final String WITHDRAW = BASE_URL + "/app/deposit/deposit_money";
    //分享url
    public static final String SHARE_URL = BASE_URL + "/app/buyPos/extension_pics";
    //快捷消费
//    public static final String FASTPAY = BASE_URL + "/app/payment/proceeds";
    public static final String FASTPAY = BASE_URL + "/app/api/proceeds";
    //渠道；
    public static final String FASTPAY2 = BASE_URL + "/app/api/passageway";
    //手续费
    public static final String POUNDAGE = BASE_URL + "/app/payment/user_fee";
    //通道列表 换成 FASTPAY2了；
//    public static final String CHANNEL_LIST = BASE_URL + "/app/plan_grounda/passageway";
    //MCC
    public static final String MCC = BASE_URL + "/app/plan_grounda/mcc";
    //更新MCC
    public static final String UPDATE_MCC = BASE_URL + "/app/plan_grounda/upate_mcc";
    //升级获取当前是否能升的等级和当前金额
    public static final String SJ1 = BASE_URL + "/app/upgrade/upgrade_info";
    //升级获取当前是否能升的等级和当前金额;
//    public static final String SJ_IMG = BASE_URL + "/app/buyPos/get_upgrade_img";

    //升级获取对应得订单号
    public static final String SJ2 = BASE_URL + "/app/upgrade/upgrade_self";
    //支付宝app支付
    public static final String ALIPAY_PAY = BASE_URL + "/app/upgrade/alipay_pay";
    //微信app支付
    public static final String WECHAT_PAY = BASE_URL + "/app/upgrade/wx_pay";
    //卡测评获取金额和订单
    public static final String CARD_TEST_MONEY_AND_ORDERID = BASE_URL + "/app/upgrade/card_pay";
    //卡测评支付结果查询
    public static final String CARD_TEST_RESULT_QUERY = BASE_URL + "/app/upgrade/query";
    //卡测评
    public static final String CARD_START_TEST = BASE_URL + "/app/upgrade/checkCard";
    //客服
    public static final String SERVER = BASE_URL + "/app/buyPos/service";
    //统一上传身份证正面照
    public static final String ID_CARD_FONT_IMG = BASE_URL + "/app/images/idcard_front";
    //统一上传身份证反面照
    public static final String ID_CARD_BACK_IMG = BASE_URL + "/app/images/idcard_back";
    //统一上传银行卡正面
    public static final String BANK_CARD_IMG = BASE_URL + "/app/images/card_front";
    //统一上传手持身份证；
    public static final String HAND_CARD_IMG = BASE_URL +"/app/images/Handheld_Idcard";
    //退出
    public static final String EXIT = BASE_URL + "/app/account/logout";
    //新手教程
    public static final String NEW_HAND = BASE_URL + "/app/buyPos/guide";
    //关于我们；
    public static final String ABOUT_US = BASE_URL + "/app/buyPos/about_us";
    //推广素材；
    public static final String POPULARIZE_MATERIAL = BASE_URL + "/app/buyPos/PromotionMaterial";
    //意见反馈；
    public static final String FEEDBACK = BASE_URL + "/app/buyPos/feedback";
    //好友交易数据；
    public static final String TRANSACTIONDATA = BASE_URL + "/app/buyPos/transactionData";
    //好友交易数据详情；
    public static final String TRANSACTIONDETAILS = BASE_URL + "/app/buyPos/transactionDetails";
    //key
    public static final String KEY = "83CBAD0F4321F733A3F5C0E2210CE158E21222";

    //用户信息储存
    public static final int AUTH_CODE_S = 90;

    //用户信息储存
    public static final String PREFS_NAMES = "userInfo";
    //session
    public static final String SESSION = "session";
    //token
    public static final String TOKEN = "token";
    //用户名
    public static final String ACCOUNT_NAME = "accName";
    //用户名UID
    public static final String UID = "uid";
    //信用卡url
    public static final String XYK_URL = "xykUrl";
    //贷款url
    public static final String DK_URL = "dkUrl";

    //密码
    public static final String ACCOUNT_PWD = "accPwd";
    //商户号
    public static final String MERCHANT_NO = "merchantNo";
    //实名认证标志
    public static final String REAL_NAME_CE = "certificationStatus";
    //身份证
    public static final String ID_CARD = "idCardNo";
    //是否记住密码标识
    public static final String IS_REMEMBER_FLAG = "isRememberPwdFlag";
    //客服电话
    public static final String TELE = "tele";

    public static final String GOURL = "http://www.shandongclub.com/sdRepay/shareMgr/succesUrl";
    //信用卡链接
    public static final String creditUrl = "https://interacts.hq.vidata.com.cn/h5/card-platform/index.html?source=18698&userid=hxy201909051454";

    //代理商登录链接
    public static final String agentUrl = "http://www.shandongclub.com:9090/repayBoss";


    //积分巴士的首页银行列表；
    public static final String busCardList = BASE_URL +"/app/integral/integralBank";
    //积分巴士商品列表；
    public static final String busCommodityList = BASE_URL +"/app/integral/integralGoodsList";
    //积分商品详情；
    public static final String busCommodityDetails = BASE_URL +"/app/integral/integralGoodsDetails";
    //积分订单提交；
    public static final String busOrderSubmission = BASE_URL +"/app/integral/integraPost";
    //积分巴士上传图片；
    public static final String busUploadImg = BASE_URL + "/app/integral/uploadFile";
    //积分巴士订单；
    public static final String busOrderForm = BASE_URL + "/app/integral/integraOrderList";

    //银行
    public static final String[] bank = {
            "中国工商银行", "中国农业银行", "中国银行", "中国建设银行", "中国邮政储蓄银行",
            "交通银行", "招商银行", "上海浦东发展银行", "兴业银行", "华夏银行", "广东发展银行",
            "中国民生银行", "中信银行", "中国光大银行", "恒丰银行", "浙商银行", "渤海银行", "国家开发银行",
            "平安银行", "上海农村商业银行", "玉溪市商业银行", "尧都农商行", "北京银行", "上海银行",
            "江苏银行", "杭州银行", "南京银行", "宁波银行", "徽商银行", "长沙银行",
            "成都银行", "重庆银行", "大连银行", "南昌银行", "福建海峡银行", "汉口银行",
            "温州银行", "青岛银行", "台州银行", "嘉兴银行", "常熟农村商业银行", "南海农村信用联社",
            "常州农村信用联社", "内蒙古银行", "绍兴银行", "顺德农商银行", "吴江农商银行", "齐商银行",
            "贵阳市商业银行", "遵义市商业银行", "湖州市商业银行", "龙江银行", "晋城银行JCBANK", "浙江泰隆商业银行",
            "广东省农村信用社联合社", "东莞农村商业银行", "浙江民泰商业银行", "广州银行", "辽阳市商业银行", "江苏省农村信用联合社",
            "廊坊银行", "浙江稠州商业银行", "德阳商业银行", "晋中市商业银行", "苏州银行", "桂林银行",
            "乌鲁木齐市商业银行", "成都农商银行", "张家港农村商业银行", "东莞银行", "莱商银行", "北京农村商业银行",
            "天津农商银行", "上饶银行", "富滇银行", "重庆农村商业银行", "鞍山银行", "宁夏银行",
            "河北银行", "华融湘江银行", "自贡市商业银行", "云南省农村信用社", "吉林银行", "东营市商业银行",
            "昆仑银行", "鄂尔多斯银行", "邢台银行", "晋商银行", "天津银行", "营口银行",
            "吉林农信", "山东农信", "西安银行", "河北省农村信用社", "宁夏黄河农村商业银行", "贵州省农村信用社",
            "阜新银行", "湖北银行黄石分行", "浙江省农村信用社联合社", "新乡银行", "湖北银行宜昌分行", "乐山市商业银行",
            "江苏太仓农村商业银行", "驻马店银行", "赣州银行", "无锡农村商业银行", "广西北部湾银行", "广州农商银行",
            "江苏江阴农村商业银行", "平顶山银行", "泰安市商业银行", "南充市商业银行", "重庆三峡银行", "中山小榄村镇银行",
            "邯郸银行", "库尔勒市商业银行", "锦州银行", "齐鲁银行", "青海银行", "阳泉银行",
            "盛京银行", "抚顺银行", "郑州银行", "深圳农村商业银行", "潍坊银行", "九江银行",
            "江西省农村信用", "河南省农村信用", "甘肃省农村信用", "四川省农村信用", "广西省农村信用", "陕西信合",
            "武汉农村商业银行", "宜宾市商业银行", "昆山农村商业银行", "石嘴山银行", "衡水银行", "信阳银行",
            "鄞州银行", "张家口市商业银行", "许昌银行", "济宁银行", "开封市商业银行", "威海市商业银行",
            "湖北银行", "承德银行", "丹东银行", "金华银行", "朝阳银行", "临商银行",
            "包商银行", "兰州银行", "周口银行", "德州银行", "三门峡银行", "安阳银行",
            "安徽省农村信用社", "湖北省农村信用社", "湖南省农村信用社", "广东南粤银行", "洛阳银行", "农信银清算中心",
            "城市商业银行资金清算中心"
    };

    public static Map<String, String> bankCode = new HashMap<String, String>();

    static {
        bankCode.put("102100099996", "中国工商银行");
        bankCode.put("103100000026", "中国农业银行");
        bankCode.put("104100000004", "中国银行");
        bankCode.put("105100000017", "中国建设银行");
        bankCode.put("308584000013", "招商银行");
        bankCode.put("306581000003", "广东发展银行");
        bankCode.put("302100011000", "中信银行");
        bankCode.put("305100000013", "中国民生银行");
        bankCode.put("303100000006", "中国光大银行");
        bankCode.put("307584007998", "平安银行");
        bankCode.put("310290000013", "浦东发展银行");
        bankCode.put("403100000004", "中国邮政储蓄银行");
        bankCode.put("304100040000", "华夏银行");
        bankCode.put("309391000011", "兴业银行");
        bankCode.put("313100000013", "北京银行");
        bankCode.put("301290000007", "交通银行");
        bankCode.put("313290000017", "上海银行");
        bankCode.put("313332082914", "宁波银行");
        bankCode.put("313331000014", "杭州银行");
        bankCode.put("318110000014", "渤海银行");
        bankCode.put("316331000018", "浙商银行");
        bankCode.put("313301008887", "南京银行");
        bankCode.put("402100000018", "北京农村商业银行");
        bankCode.put("313345010019", "浙江泰隆商业银行");
    }

}
