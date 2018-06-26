package com.sinoyd.frame.task;

import android.os.Handler;

/**
 * 作者： 王一凡
 * 创建时间： 2017/9/5
 * 版权： 江苏远大信息股份有限公司
 * 描述：网络请求 异步类
 */
public abstract class BaseTask implements Runnable{
    int taskId;
    BaseTaskCallBack callBack;
    Handler handler = new Handler();

    public BaseTask(int taskId, BaseTaskCallBack callBack) {
        this.taskId = taskId;
        this.callBack = callBack;
    }

    public void start() {
        (new Thread(this)).start();
    }

    public abstract Object execute();

    public void run() {
        final Object response = this.execute();
        this.handler.post(new Runnable() {
            public void run() {
                BaseTask.this.callBack.refresh(BaseTask.this.taskId, response);
            }
        });
    }

    public interface BaseTaskCallBack {
        void refresh(int var1, Object var2);
    }
}
