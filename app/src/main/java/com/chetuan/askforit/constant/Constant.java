package com.chetuan.askforit.constant;

/**
 * Created by yfding on 2015/11/11.
 */
public class Constant {

    public static final int SENDSUCCESS = 200; //登录成功返回标志

    public static final String REQUEST_SUCCESS = "0000";

    //result code
    public static final int RESULT_CODE_SUCCESS = 1;

    public static final int RESULT_CODE_FAIL = 0;

    public static final String[] ORDER_STATUS = {"已失效", "已报备", "已申请签单", "已签单", "已提车"};

    public static final String[] MSG_TYPES = {"入账消息", "提醒消息", "提现消息", "经验学堂", "订单消息"};

    public static final int REQUEST_CODE_GETBANK = 1;
    public static final int REQUEST_CODE_UPDATEACCOUNT = 1;
    public static final int RESULT_CODE_GETBANK = 2;
    public static final int RESULT_CODE_UPDATEACCOUNT = 2;

    public static final int RESULT_CODE_UPDATEBANK =2;

    public static final int REQUEST_MSG_REFRESH =3;

    public static final String ORDER_INVALID = "0";//已失效

    public static final String ORDER_REPORTED = "1";//已报备

    public static final String ORDER_SIGNING = "2";//已申请签单

    public static final String ORDER_SIGNED = "3";//已签单

    public static final String ORDER_PICKED = "4";//已提车
}
