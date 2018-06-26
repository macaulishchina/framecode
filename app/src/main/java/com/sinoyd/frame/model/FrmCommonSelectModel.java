package com.sinoyd.frame.model;

/**
 * 作者： 王一凡
 * 创建时间： 2017/11/7
 * 版权： 江苏远大信息股份有限公司
 * 描述： com.sinoyd.baoshuiqu.model
 */
public class FrmCommonSelectModel {
    public String item_id = "";
    public String item_value = "";
    public String item_remark = "";

    public FrmCommonSelectModel(String id, String name) {
        this.item_id = id;
        this.item_value = name;
    }
}
