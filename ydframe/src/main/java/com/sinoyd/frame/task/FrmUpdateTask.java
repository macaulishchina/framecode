package com.sinoyd.frame.task;

import com.sinoyd.frame.action.FrmCommonAction;

/**
 * 作者： 王一凡
 * 创建时间： 2018/3/30
 * 版权： 江苏远大信息股份有限公司
 * 描述： com.sinoyd.frame.task
 */
public class FrmUpdateTask extends BaseTask{
    public FrmUpdateTask(int taskId, BaseTaskCallBack callBack) {
        super(taskId, callBack);
    }

    @Override
    public Object execute() {
        String method = "version.json";
        Object obj = FrmCommonAction.request(null, method, "http://lims-openfile.oss-cn-shanghai.aliyuncs.com/om-apk",FrmCommonAction.GET);
        return obj;
    }
}
