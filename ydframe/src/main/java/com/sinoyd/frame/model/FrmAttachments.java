package com.sinoyd.frame.model;

import java.io.Serializable;

/**
 * 作者： 王一凡
 * 创建时间： 2018/3/30
 * 版权： 江苏远大信息股份有限公司
 * 描述： com.sinoyd.frame.model
 */
public class FrmAttachments implements Serializable {
    private static final long serialVersionUID = 1L;
    //============本地图片=============
    public String AttachPath;// 附件路径
    public String AttContent;
    public String AttFileName;
    public String FileDate = "";// 文件时间
    public String DateTime = "";

    //============网络图片=============
    public String id;
    public String size;
}
