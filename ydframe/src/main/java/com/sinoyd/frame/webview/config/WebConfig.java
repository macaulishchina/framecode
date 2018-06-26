package com.sinoyd.frame.webview.config;

/**
 * 作者： 王一凡
 * 创建时间： 2018/3/21
 * 版权： 江苏远大信息股份有限公司
 * 描述： com.sinoyd.webview.config
 */
public class WebConfig {
    /**
     * 自定义api路径，值String类型 默认空
     */
    public static final String CUSTOM_APIPATH = "customAPIPath";

    /**
     * 打开下一个页面后是否关闭当前页面,值String类型 默认为空 1:是 其他：否
     */
    public static final String FINISH_AFTER_OPEN = "finishAfterOpen";

    /**
     * 下一个页面回传数据的请求值，若为空则不接受回传数据；finishAfterOpen为1时无法接收数据
     */
    public static final String REQUEST_CODE = "requestCode";

    /**
     * 打开新页面时传递的二级参数，值json类型 默认为空
     */
    public static final String INIT_DATA = "data";

    /**
     * 打开新的原生页面的activity名字，值String类型 非空
     */
    public static final String LOCALPAGE_CLASSNAME = "localPageClassName";

    /**
     * 是否隐藏导航栏，值boolean类型 默认为true
     */
    public static final String SHOW_NAVIGATION = "showNavigation";

    /**
     * 是否显示导航栏左边返回按钮，值boolean类型 默认为true
     */
    public static final String SHOW_BACK_BUTTON = "showBackButton";

    /**
     * 是否开启右滑关闭页面，值boolean类型 默认为true
     */
    public static final String SLIDE_FINISH = "slideFinish";

    /**
     * 导航栏右边按钮文字，值String类型 默认为空
     * 不为空时需要实现onNBRight()方法
     */
    public static final String NBRIGHT_TEXT = "nbRightText";

    /**
     * 对话框标题
     */
    public static final String DIALOG_TITLE = "dialogTitle";

    /**
     * 对话框内容
     */
    public static final String DIALOG_CONTENT = "dialogContent";
}
