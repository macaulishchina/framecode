package com.sinoyd.frame.util;

/**
 * 作者： 王一凡
 * 创建时间： 2017/9/5
 * 版权： 江苏远大信息股份有限公司
 * 描述：资源文件（反射工具类）
 */
public class ResManager {
    public static final String style = "style";
    public static final String string = "string";
    public static final String id = "id";
    public static final String layout = "layout";
    public static final String drawable = "drawable";
    public static final String attr = "attr";
    public static final String anim = "anim";
    public static final String raw = "raw";
    public static final String color = "color";
    public static final String animator = "animator";

    public ResManager() {
    }

    public static int getResourseIdByName(String className, String name) {
        int id = 0;
        Class cls = null;

        try {
            cls = Class.forName("com.sinoyd.R$" + className);
            id = cls.getField(name).getInt(className);
        } catch (ClassNotFoundException var7) {
            var7.printStackTrace();

            try {
                cls = Class.forName(AppUtil.getApplicationContext().getPackageName() + ".R$" + className);
                id = cls.getField(name).getInt(className);
            } catch (Exception var6) {
                var6.printStackTrace();
            }
        } catch (Exception var8) {
            var8.printStackTrace();
        }

        return id;
    }

    public static int getLayoutInt(String layoutname) {
        return getResourseIdByName("layout", layoutname);
    }

    public static int getStringInt(String stringname) {
        return getResourseIdByName("string", stringname);
    }

    public static int getIdInt(String idname) {
        return getResourseIdByName("id", idname);
    }

    public static int getDrawableInt(String drawablename) {
        return getResourseIdByName("drawable", drawablename);
    }

    public static int getAttrInt(String attrname) {
        return getResourseIdByName("attr", attrname);
    }

    public static int getAnimInt(String animname) {
        return getResourseIdByName("anim", animname);
    }

    public static int getRawInt(String rawname) {
        return getResourseIdByName("raw", rawname);
    }

    public static int getColorInt(String colorname) {
        return getResourseIdByName("color", colorname);
    }

    public static int getStyleInt(String stylename) {
        return getResourseIdByName("style", stylename);
    }

    public static int getAnimatorInt(String animatorname) {
        return getResourseIdByName("animator", animatorname);
    }

    public static int getDimenInt(String stylename) {
        return getResourseIdByName("dimen", stylename);
    }
}
