package com.sinoyd.webview.action;

/**
 * Created by Jim on 16/9/30.
 * Action 类
 */
public class WebloaderAction {

    /**
     * H5页面url地址（必传参数）
     */
    public static final String MOJS_SCHEME = "SinoydJSBridge";

    /**
     * H5页面url地址（必传参数）
     */
    public static final String PAGE_URL = "PAGE_URL";

    /**
     * H5页面title（必传参数）
     */
    public static final String PAGE_TITLE = "PAGE_TITLE";

    /**
     * 页面之间数据回调key值
     */
    public static final String RESULT_DATA = "result";

    /**
     * 框架api注册名字
     */
    public static final String BRIDGE_API_NAME = "sinoyd_bridge";

    /**
     * 自定义api注册名字
     */
    public static final String CUSTOMBRIDGE_API_NAME = "custom_sinoyd_bridge";

    public static final String ACCEPT_IMAGE = "image";//相册
    public static final String ACCEPT_IMAGE_TITLE = "相册";

    public static final String ACCEPT_CAMERA = "camera";//拍照
    public static final String ACCEPT_CAMERA_TITLE = "拍照";

    public static final String ACCEPT_FILE = "file";//文件
    public static final String ACCEPT_FILE_TITLE = "文件";

    public static final String ACCEPT_VIDEO = "video";//录像
    public static final String ACCEPT_VIDEO_TITLE = "录像";

}
