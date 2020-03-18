package com.hxypay.util;

import android.text.TextUtils;

public class VerifyRuler {
    private static VerifyRuler iVerifyRuler;

    public static VerifyRuler getIVerifyRuler() {
        if (iVerifyRuler == null) {
            iVerifyRuler = new VerifyRuler();
        }
        return iVerifyRuler;
    }

    public boolean pwdRuler(String pwd) {
        String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";
        return pwd.matches(regex);
    }
    public static boolean pwdAstrict(String pwd) {
        String regex = "^[A-Za-z0-9]+$";
        return pwd.matches(regex);
    }

    public static String teleShow(String tele) {
        String reTele = "";
        if (!TextUtils.isEmpty(tele)) {
            reTele = tele.substring(0, 3) + " **** "
                    + tele.substring(tele.length() - 4, tele.length());
        }
        return reTele;
    }

    public static String nameShow(String name) {
        String reName = "";
        if (!TextUtils.isEmpty(name)) {
            if (name.length() == 2) {
                reName = name.substring(0, 1) + "*";
            }
            if (name.length() > 2) {
                int tempStarLen = name.length() - 2;
                StringBuffer starStr = new StringBuffer();
                for (int i = 0; i < tempStarLen; i++) {
                    starStr.append("*");
                }
                reName = name.substring(0, 1) + starStr
                        + name.substring(name.length() - 1, name.length());
            }
        }
        return reName;
    }

    //保留前10位；
    public static String timeShow(String time){
        int length = time.length();
        if (length>=10){
            time =time.substring(0,10);
            return time;
        }else {
            return time;
        }
    }


    public static boolean isLegalName(String name) {
        if (name.matches("^[\\u4e00-\\u9fa5]{2,16}$")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isId(String id) {
        if (id.matches("^(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)")) {
            return true;
        } else {
            return false;
        }
    }

    public static String idCardShow(String idCard) {
        String reId = "";
        if (!TextUtils.isEmpty(idCard)) {
            reId = idCard.substring(0, 4) + " **** " + idCard.substring(idCard.length() - 4, idCard.length());
        }
        return reId;
    }

    public static String cardShow(String card) {
        String reCard = "";
        if (!TextUtils.isEmpty(card)) {
            reCard = " **** **** **** " + card.substring(card.length() - 4, card.length());
        } else {
            reCard = " **** **** **** **** ";
        }
        return reCard;
    }

    public static String num(String s) {
        String data = "";
        if (TextUtils.isEmpty(s)) {
            return data;
        }
        if (s.contains(".")) {
            int dotIndex = s.indexOf(".");
            if (dotIndex == 1) {
                data = s;
            } else {
                if (dotIndex >= 9) {
                    data = s.substring(0, dotIndex);
                } else {
                    if (s.length() < 10) {
                        data = s.substring(0, s.length());
                    } else {
                        data = s.substring(0, 10);
                    }
                }
            }
        } else {
            data = s;
        }
        return data;
    }
}
