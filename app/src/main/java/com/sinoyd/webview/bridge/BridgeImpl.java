package com.sinoyd.webview.bridge;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.WebView;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.google.gson.JsonObject;
import com.sinoyd.R;
import com.sinoyd.frame.actys.FrmAttachUploadActivity;
import com.sinoyd.frame.actys.SinoBaseActivity;
import com.sinoyd.frame.db.FrmDBService;
import com.sinoyd.frame.task.BaseTask;
import com.sinoyd.frame.task.FrmUpdateTask;
import com.sinoyd.frame.util.AppUtil;
import com.sinoyd.frame.util.DateUtil;
import com.sinoyd.frame.util.DownloadUtil;
import com.sinoyd.frame.util.IOService;
import com.sinoyd.frame.util.JsonHelp;
import com.sinoyd.frame.util.PhoneUtil;
import com.sinoyd.frame.util.ToastUtil;
import com.sinoyd.frame.views.BaseNavigationBar;
import com.sinoyd.frame.views.ConfirmDialog;
import com.sinoyd.frame.views.FrmActionSheet;
import com.sinoyd.webview.FrmMojsFragment;
import com.sinoyd.webview.FrmWebLoader;
import com.sinoyd.webview.FrmWebViewActivity;
import com.sinoyd.webview.action.WebloaderAction;
import com.sinoyd.webview.config.WebConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Iterator;

/**
 * 作者： 王一凡
 * 创建时间： 2018/3/21
 * 版权： 江苏远大信息股份有限公司
 * 描述： com.sinoyd.webview.action
 */
public class BridgeImpl implements IBridge {
    /**
     * 隐藏导航栏
     */
    public static void hideNavigation(final FrmMojsFragment webLoader, WebView wv, JSONObject param, final Callback callback) {
        wv.post(new Runnable() {
            public void run() {
                webLoader.getNbBar().hide();
                try {
                    callback.apply(JSBridge.getSuccessJSONObject());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 显示导航栏
     */
    public static void showNavigation(final FrmMojsFragment webLoader, WebView wv, JSONObject param, final Callback callback) {
        wv.post(new Runnable() {
            public void run() {
                webLoader.getNbBar().show();
                try {
                    callback.apply(JSBridge.getSuccessJSONObject());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * JS设置标题接口
     * <p/>
     * pageTitle:   标题
     */
    public static void setNaigationTitle(final FrmMojsFragment webLoader, WebView wv, JSONObject param, final Callback callback) {
        final String title = param.optString(WebloaderAction.PAGE_TITLE);
        wv.post(new Runnable() {
            public void run() {
                webLoader.getNbBar().setNBTitle(title);
                if (null != callback) {
                    try {
                        callback.apply(JSBridge.getSuccessJSONObject());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 设置导航栏右上角文字按钮
     * 参数：
     * nbRightText：按钮文字,若为空则隐藏
     */
    public static void setNBRightText(final FrmMojsFragment webLoader, WebView wv, JSONObject param, final Callback callback) {
        final String nbRightText = param.optString(WebConfig.NBRIGHT_TEXT);
        wv.post(new Runnable() {
                    public void run() {
                        BaseNavigationBar nbBar = webLoader.getNbBar();
                        //清除动画效果,否则无法隐藏
                        nbBar.nbRight.clearAnimation();
                        nbBar.nbRight.invalidate();
                        nbBar.nbRight.setVisibility(View.INVISIBLE);
                        nbBar.nbRightText.setVisibility(View.VISIBLE);
                        nbBar.nbRightText.setText(nbRightText);
                        try {
                            callback.apply(JSBridge.getSuccessJSONObject());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }

    /**
     * 隐藏导航栏右侧文字按钮
     */
    public static void hideNBRightText(final FrmMojsFragment webLoader, WebView wv, JSONObject param, final Callback callback) {
        wv.post(new Runnable() {
                    public void run() {
                        BaseNavigationBar nbBar = webLoader.getNbBar();;
                        nbBar.nbRightText.setVisibility(View.GONE);
                        try {
                            callback.apply(JSBridge.getSuccessJSONObject());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }

    /**
     * JS打开H5页面接口,并且可返回数据
     * <p/>
     * finishAfterOpen: 打开下一个页面后是否关闭当前页面 1:是 其他：否
     * requestCode：    下一个页面回传数据的请求值，若为空则不接受回传数据；finishAfterOpen为1时无法接收数据
     * data:            传递给下一个页面的参数
     */
    public static void openPage(final FrmMojsFragment webLoader, WebView wv, JSONObject param, final Callback callback) {
        final boolean finishAfterOpen = "1".equals(param.optString(WebConfig.FINISH_AFTER_OPEN));
        final String requestCode = param.optString(WebConfig.REQUEST_CODE);
        final String jsonStr = param.optString(WebConfig.INIT_DATA);
        wv.post(new Runnable() {
            public void run() {
                int code = 1;
                String msg = "";
                Intent intent = new Intent(webLoader.getActivity(), FrmWebLoader.class);
                if (!TextUtils.isEmpty(jsonStr)) {
                    try {
                        JSONObject jsonObj = new JSONObject(jsonStr);
                        Iterator<String> it = jsonObj.keys();
                        while (it.hasNext()) {
                            String key = it.next();
                            Object valueObj = jsonObj.get(key);
                            if (valueObj instanceof Boolean) {
                                intent.putExtra(key, (Boolean) valueObj);
                            } else if (valueObj instanceof String) {
                                intent.putExtra(key, valueObj.toString());
                            } else if (valueObj instanceof Integer) {
                                intent.putExtra(key, (Integer) valueObj);
                            } else if (valueObj instanceof Double) {
                                intent.putExtra(key, (Double) valueObj);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        code = 0;
                        msg = "data参数解析错误";
                    }
                }
                if (TextUtils.isEmpty(requestCode)) {
                    webLoader.startActivity(intent);
                } else {
                    try {
                        webLoader.startActivityForResult(intent, Integer.parseInt(requestCode));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        msg = "requestCode参数解析错误";
                    }
                }
                if (finishAfterOpen) {
                    webLoader.getActivity().finish();
                }
                if (null != callback) {
                    try {
                        callback.apply(getJSONObject(code, msg, null));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * JS打开原生页面接口,并且可返回数据
     * <p/>
     * localPageClassName:     原生页面activity类名
     * finishAfterOpen:  打开下一个页面后是否关闭当前页面 1:是 其他：否
     * requestCode：     下一个页面回传数据的请求值，若为空则不接受回传数据；finishAfterOpen为1时无法接收数据
     * data:             传递下一页面的数据
     */
    public static void openLocal(final FrmMojsFragment webLoader, WebView wv, JSONObject param, final Callback callback) {
        final String activityName = param.optString(WebConfig.LOCALPAGE_CLASSNAME);
        final boolean finishAfterOpen = "1".equals(param.optString(WebConfig.FINISH_AFTER_OPEN));
        final String requestCode = param.optString(WebConfig.REQUEST_CODE);
        final String jsonStr = param.optString(WebConfig.INIT_DATA);
        wv.post(new Runnable() {
            public void run() {
                int code = 1;
                String msg = "";
                try {
                    Class clz = Class.forName(activityName);
                    Intent intent = new Intent(webLoader.getActivity(), clz);

                    if (!TextUtils.isEmpty(jsonStr)) {
                        JSONObject jsonObj = new JSONObject(jsonStr);
                        Iterator<String> it = jsonObj.keys();
                        while (it.hasNext()) {
                            String key = it.next();
                            Object valueObj = jsonObj.get(key);
                            if (valueObj instanceof Boolean) {
                                intent.putExtra(key, (Boolean) valueObj);
                            } else if (valueObj instanceof String) {
                                intent.putExtra(key, valueObj.toString());
                            } else if (valueObj instanceof Integer) {
                                intent.putExtra(key, (Integer) valueObj);
                            } else if (valueObj instanceof Double) {
                                intent.putExtra(key, (Double) valueObj);
                            }
                        }
                    }
                    if (TextUtils.isEmpty(requestCode)) {
                        webLoader.startActivity(intent);
                    } else {
                        webLoader.startActivityForResult(intent, Integer.parseInt(requestCode));
                    }
                    if (finishAfterOpen) {
                        webLoader.getActivity().finish();
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    code = 0;
                    msg = "requestCode参数解析错误";
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    code = 0;
                    msg = activityName + "找不到该类";
                } catch (JSONException e) {
                    e.printStackTrace();
                    code = 0;
                    msg = "data参数解析错误";
                }

                if (null != callback) {
                    try {
                        callback.apply(getJSONObject(code, msg, null));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * JS关闭当前Activity接口
     * RESULT_DATA:    回传上个页面的值,如果为空则不回传
     */
    public static void closePage(final FrmMojsFragment webLoader, WebView wv, JSONObject param, final Callback callback) {
        final String jsonStr = param.optString(WebloaderAction.RESULT_DATA);
        wv.post(new Runnable() {
            public void run() {
                try {
                    callback.apply(getJSONObject(1));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (TextUtils.isEmpty(jsonStr)) {
                    webLoader.getActivity().finish();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra(WebloaderAction.RESULT_DATA, jsonStr);
                    webLoader.getActivity().setResult(Activity.RESULT_OK, intent);
                    webLoader.getActivity().finish();
                }
            }
        });
    }

    /**
     * 弹出原生对话框
     */
    public static void showDialog(final FrmMojsFragment webLoader, WebView wv, JSONObject param, final Callback callback) {
        final String title = param.optString(WebConfig.DIALOG_TITLE);
        final String content = param.optString(WebConfig.DIALOG_CONTENT);
        wv.post(new Runnable() {
            public void run() {
                new ConfirmDialog(webLoader.getActivity(), R.style.frm_dialog, title, content, new ConfirmDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if(confirm){
                            try {
                            JSONObject object = new JSONObject();
                            object.put("index", 1);
                            callback.apply(JSBridge.getSuccessJSONObject(object));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).show();
            }
        });
    }

    /**
     * 获取Frame_Resc数据库值
     */
    public static void getConfigValue(final FrmMojsFragment webLoader, WebView wv, JSONObject param, final Callback callback) {
        final String key = param.optString("config_key");
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject callBackObject;
                JSONObject object = new JSONObject();
                try {
                    object.put("value", FrmDBService.getConfigValue(key));
                    callBackObject = JSBridge.getSuccessJSONObject(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                    callBackObject = JSBridge.getFailJSONObject();
                }
                try {
                    callback.apply(callBackObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 获取Frame_Resc数据库值
     */
    public static void setConfigValue(final FrmMojsFragment webLoader, WebView wv, JSONObject param, final Callback callback) {
        final String key = param.optString("config_key");
        final String value = param.optString("config_value");
        new Thread(new Runnable() {
            @Override
            public void run() {
                FrmDBService.setConfigValue(key, value);
                try {
                    callback.apply(JSBridge.getSuccessJSONObject());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    /**
     * 弹出日期选择对话框
     * 参数：
     * title 标题
     * datetime 指定日期 yyyy-MM-dd
     */
    public static void showDatePickDialog(final FrmMojsFragment webLoader, WebView wv, JSONObject param, final Callback callback) {
        final String title = param.optString("title");
        final String date = param.optString("datetime");
        final Calendar calendar = Calendar.getInstance();
        if (!TextUtils.isEmpty(date)) {
            calendar.setTime(DateUtil.convertString2Date(date, "yyyy-MM-dd"));
        }
        wv.post(new Runnable() {
            public void run() {
                DatePickerDialog dialog = new DatePickerDialog(webLoader.getContext(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        try {
                            String y = year + "-";
                            monthOfYear = monthOfYear + 1;
                            String m = (monthOfYear > 9 ? monthOfYear : ("0" + monthOfYear)) + "-";
                            String d = dayOfMonth + "";
                            JSONObject object = new JSONObject();
                            object.put("datetime", y + m + d);
                            callback.apply(JSBridge.getSuccessJSONObject(object));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.setTitle("选择日期");
                dialog.show();

            }
        });
    }

    /**
     * 弹出日期及时间选择对话框
     * 参数：
     * title 标题
     * datetime 指定日期 yyyy-MM-dd HH:mm
     */
    public static void showDateTimePickDialog(final FrmMojsFragment webLoader, WebView wv, JSONObject param, final Callback callback) {
        final String title = param.optString("title");
        final String date = param.optString("datetime");
        final Calendar calendar = Calendar.getInstance();
        if (!TextUtils.isEmpty(date)) {
            calendar.setTime(DateUtil.convertString2Date(date, "yyyy-MM-dd HH:mm"));
        }
        wv.post(new Runnable() {
            public void run() {
                DatePickerDialog dialog = new DatePickerDialog(webLoader.getContext(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        TimePickerDialog timePick = new TimePickerDialog(webLoader.getContext(), new TimePickerDialog.OnTimeSetListener() {
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);
                                String chooseDate = DateUtil.convertDate(calendar.getTime(), "yyyy-MM-dd HH:mm");
                                try {
                                    JSONObject object = new JSONObject();
                                    object.put("datetime", chooseDate);
                                    callback.apply(JSBridge.getSuccessJSONObject(object));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                        timePick.setTitle("选择时间");
                        timePick.show();
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.setTitle("选择日期");
                dialog.show();

            }
        });
    }

    /**
     * JS消息提示接口
     * message： 需要提示的消息内容
     */
    public static void toast(final FrmMojsFragment webLoader, WebView wv, JSONObject param, final Callback callback) {
        final String message = param.optString("message");
        wv.post(new Runnable() {
            public void run() {
                ToastUtil.showShort(webLoader.getActivity(), message);
                try {
                    callback.apply(getJSONObject(1));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * JS消息提示接口
     * message： 需要提示的消息内容
     */
    public static void toastWarn(final FrmMojsFragment webLoader, WebView wv, JSONObject param, final Callback callback) {
        final String message = param.optString("message");
        wv.post(new Runnable() {
            public void run() {
                ToastUtil.showWorning(webLoader.getActivity(), message);
                try {
                    callback.apply(getJSONObject(1));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 弹出消息对话框
     * 参数：
     * title：标题
     * message：消息
     */
    public static void showMsgDialog(final FrmMojsFragment webLoader, WebView wv, JSONObject param, final Callback callback) {
        final String message = param.optString("message");
        wv.post(new Runnable() {
            public void run() {
                ToastUtil.showShort(webLoader.getActivity(), message);
                try {
                    callback.apply(getJSONObject(1));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * JS隐藏导航栏返回按钮接口
     */
    public static void hideBackButton(final FrmMojsFragment webLoader, WebView wv, JSONObject param, final Callback callback) {
        wv.post(new Runnable() {
            public void run() {
                webLoader.getNbBar().nbBack.setVisibility(View.GONE);
                try {
                    callback.apply(getJSONObject(1));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * JS设置容器重新显示时页面是否重载接口
     */
    public static void setResumeReload(final FrmMojsFragment webLoader, WebView wv, JSONObject param, final Callback callback) {
        wv.post(new Runnable() {
            public void run() {
                webLoader.setIsResumeRefreshPage(true);
                try {
                    callback.apply(getJSONObject(1));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * JS重载页面接口
     */
    public static void reloadPage(final FrmMojsFragment webLoader, final WebView wv, JSONObject param, final Callback callback) {
        wv.post(new Runnable() {
            public void run() {
                wv.reload();
                try {
                    callback.apply(getJSONObject(1));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * JS隐藏loading图标接口
     */
    public static void hideProgress(final FrmMojsFragment webLoader, WebView wv, JSONObject param, final Callback callback) {
        wv.post(new Runnable() {
            public void run() {
                webLoader.hideProgress();
                try {
                    callback.apply(getJSONObject(1));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * JS显示loading图标接口
     */
    public static void showProgress(final FrmMojsFragment webLoader, WebView wv, JSONObject param, final Callback callback) {
        wv.post(new Runnable() {
            public void run() {
                webLoader.showProgress();
                try {
                    callback.apply(getJSONObject(1));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 弹出底部选项按钮
     * 参数：
     * items：多个选项用,隔开
     * cancelable: 是否可取消
     */
    public static void uploadAttach(final FrmMojsFragment webLoader, WebView wv, JSONObject param, final Callback callback) {
        final String[] items = param.optString("items").split(",");
        wv.post(new Runnable() {
                    public void run() {
                        Intent intent = new Intent(webLoader.getActivity(), FrmAttachUploadActivity.class);
                        webLoader.startActivity(intent);
                    }
                }
        );
    }

    /**
     * 弹出底部选项按钮
     * 参数：
     * items：多个选项用,隔开
     * cancelable: 是否可取消
     */
    public static void showActionsheet(final FrmMojsFragment webLoader, WebView wv, JSONObject param, final Callback callback) {
        final String[] items = param.optString("items").split(",");
        final boolean cancelable = !"0".equals(param.optString("cancelable"));
        wv.post(new Runnable() {
                    public void run() {
                        final FrmActionSheet menuView = new FrmActionSheet(webLoader.getActivity());
                        menuView.setCancelButtonTitle("取消");
                        menuView.addItems(items);
                        menuView.setItemClickListener(new FrmActionSheet.MenuItemClickListener() {
                            @Override
                            public void onItemClick(int index) {
                                try {
                                    JSONObject object = new JSONObject();
                                    object.put("which", index);
                                    callback.apply(JSBridge.getSuccessJSONObject(object));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        menuView.setCancelableOnTouchMenuOutside(cancelable);
                        menuView.showMenu();
                    }
                }
        );
    }

    /**
     * 获取设备唯一编码
     */
    public static void getDeviceid(final FrmMojsFragment webLoader, WebView wv, JSONObject param, final Callback callback) {
        wv.post(new Runnable() {
                    public void run() {
                        try {
                            JSONObject object = new JSONObject();
                            object.put("deviceId", PhoneUtil.getDeviceId(webLoader.getActivity()));
                            callback.apply(JSBridge.getSuccessJSONObject(object));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }

    /**
     * 打电话
     * phoneNum：电话号码
     */
    public static void call(final FrmMojsFragment webLoader, WebView wv, JSONObject param, final Callback callback) {
        final String phoneNum = param.optString("phoneNum");
        wv.post(new Runnable() {
                    public void run() {
                        try {
                            IOService.Call(webLoader.getActivity(), phoneNum);
                            callback.apply(JSBridge.getSuccessJSONObject());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }

    /**
     * 获取设备基础信息
     */
    public static void getUAinfo(final FrmMojsFragment webLoader, WebView wv, JSONObject param, final Callback callback) {
        wv.post(new Runnable() {
                    public void run() {
                        try {
                            JSONObject object = new JSONObject();
                            //设备厂商以及型号
                            String type = android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL;
                            object.put("UAinfo", "android " + type);
                            //设备分辨率
                            DisplayMetrics dm = new DisplayMetrics();
                            webLoader.getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
                            String pixel = PhoneUtil.getPhoneHeight(webLoader.getActivity()) + "*" + PhoneUtil.getPhoneWidth(webLoader.getActivity());
                            object.put("pixel", pixel);
                            //唯一表示(机器码或者MAC地址)
                            object.put("deviceId", PhoneUtil.getDeviceId(webLoader.getActivity()));
                            //网络状态 -1:无网络1：wifi 0：移动网络
                            int mNetWorkType = -1;
                            ConnectivityManager manager = (ConnectivityManager) webLoader.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
                            if (networkInfo != null && networkInfo.isConnected()) {
                                int netType = networkInfo.getType();
                                if (netType == ConnectivityManager.TYPE_WIFI) {
                                    mNetWorkType = 1;
                                } else if (netType == ConnectivityManager.TYPE_MOBILE) {
                                    mNetWorkType = 0;
                                }
                            } else {
                                mNetWorkType = -1;
                            }
                            object.put("NetWorkType", mNetWorkType);
                            callback.apply(JSBridge.getSuccessJSONObject(object));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }

    /**
     * 更新
     */
    public static void updateApp(final FrmMojsFragment webLoader, WebView wv, JSONObject param, final Callback callback) {
        wv.post(new Runnable() {
                    public void run() {
                        FrmUpdateTask task = new FrmUpdateTask(1, new BaseTask.BaseTaskCallBack() {
                            @Override
                            public void refresh(int var1, Object obj) {
                                if (JsonHelp.checkResult(obj)) {
                                    JsonObject jsonObj = (JsonObject) obj;
                                    JsonObject body = (JsonObject) jsonObj.get("body");
                                    final String url = body.get("url").getAsString();
                                    final String version = body.get("version").getAsString();
                                    String localversion = AppUtil.getAppVersion();
                                    if (!localversion.equals(version)) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(webLoader.getContext());
                                        builder.setMessage("发现app有新版本");
                                        builder.setTitle("版本号v" + version);
                                        builder.setPositiveButton("下载", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                new DownloadUtil(webLoader.getContext(), url, version).start();
                                            }
                                        });
                                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        });
                                        builder.create().show();
                                    }
                                }
                                try {
                                    callback.apply(JSBridge.getSuccessJSONObject());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        task.start();

                    }
                }
        );
    }

    /**
     * 获取设备分辨率
     */
    public static void getPixel(final FrmMojsFragment webLoader, WebView wv, JSONObject param, final Callback callback) {
        wv.post(new Runnable() {
                    public void run() {
                        try {
                            DisplayMetrics dm = new DisplayMetrics();
                            webLoader.getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
                            String pixel = PhoneUtil.getPhoneWidth(webLoader.getActivity()) + "*" + PhoneUtil.getPhoneHeight(webLoader.getActivity());
                            JSONObject object = new JSONObject();
                            object.put("pixel", pixel);
                            callback.apply(JSBridge.getSuccessJSONObject(object));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }

    /**
     * 获取版本号
     */
    public static void getVersionName(final FrmMojsFragment webLoader, final WebView wv, JSONObject param, final Callback callback) {
        wv.post(new Runnable() {
            public void run() {
                try {
                    JSONObject object = new JSONObject();
                    object.put("VersionName", PhoneUtil.getVersionName(webLoader.getActivity()));
                    callback.apply(JSBridge.getSuccessJSONObject(object));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 下载文件，如果已下载直接打开
     * url:下载地址
     * fileName:文件名
     * type:下载分类
     * reDownloaded:是否重新下载,默认0
     * isBackground:是否后台下载,默认0
     */
    public static void downloadFile(final FrmMojsFragment webLoader, WebView wv, JSONObject param, final Callback callback) {
        final String url = param.optString("url");
        final String fileName = param.optString("fileName");
        final String type = param.optString("type");
        final boolean isBackground = "1".equals(param.optString("isBackground"));
        final boolean reDownloaded = "1".equals(param.optString("reDownloaded"));
        wv.post(new Runnable() {
                    public void run() {
                        String urlEncode = null;
                        try {
                            urlEncode = URLEncoder.encode(url, "utf-8").replaceAll("\\+", "%20");
                            urlEncode = urlEncode.replaceAll("%3A", ":").replaceAll("%2F", "/");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

//                        MOADownLoadAction downLoadAction = new MOADownLoadAction(webLoader.getActivity());
//                        if (isBackground) {
//                            downLoadAction.downloadInBackground(urlEncode, fileName, type, reDownloaded);
//                        } else {
//                            downLoadAction.download(urlEncode, fileName, type, reDownloaded);
//                        }

                        new DownloadUtil(webLoader.getActivity(), url).start();

                        try {
                            callback.apply(JSBridge.getSuccessJSONObject());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }

    /**
     * 获取callback返回数据json对象
     *
     * @param code   1：成功 0：失败
     * @param msg    描述
     * @param result
     * @return
     */
    public static JSONObject getJSONObject(int code, String msg, JSONObject result) throws JSONException {
        JSONObject object = new JSONObject();
        object.put("code", code);
        object.put("msg", msg);
        object.putOpt("result", result == null ? "" : result);
        return object;
    }

    /**
     * 获取callback返回数据json对象
     *
     * @param code 1：成功 0：失败
     * @return
     */
    public static JSONObject getJSONObject(int code) throws JSONException {
        return getJSONObject(code, "", null);
    }
}
