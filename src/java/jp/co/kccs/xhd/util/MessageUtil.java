/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.util;

import java.text.MessageFormat;
import java.util.List;
import java.util.ResourceBundle;
import javax.faces.context.FacesContext;
import jp.co.kccs.xhd.db.model.FXHDD01;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2018/05/06<br>
 * 計画書No	K1803-DS001<br>
 * 変更者	KCCS D.Yanagida<br>
 * 変更理由	新規作成<br>
 * <br>
 * 変更日	2018/11/13<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	ﾛｯﾄｶｰﾄﾞ電子化対応<br>
 * <br>
 * ===============================================================================<br>
 */

/**
 * ﾒｯｾｰｼﾞ処理関連ﾕｰﾃｨﾘﾃｨｸﾗｽ
 */
public class MessageUtil {
    /**
     * ｺﾝｽﾄﾗｸﾀ
     */
    private MessageUtil() {
    }

    /**
     * ﾒｯｾｰｼﾞ取得処理
     *
     * @param id ﾒｯｾｰｼﾞID
     * @param params ﾊﾟﾗﾒｰﾀ
     * @return ﾒｯｾｰｼﾞ
     */
    public static String getMessage(String id, Object... params) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle messages = context.getApplication().getResourceBundle(context, "myMsg");

        String message = messages.getString(id);
        MessageFormat mf = new MessageFormat(message);

        return mf.format(params);
    }
    
    /**
     * ﾒｯｾｰｼﾞ取得処理
     *
     * @param id ﾒｯｾｰｼﾞID
     * @param params ﾊﾟﾗﾒｰﾀ
     * @param itemList ｱｲﾃﾑﾘｽﾄ
     * @return ﾒｯｾｰｼﾞ
     */
    public static String getMessageWithSetBackColor(String id, List<FXHDD01> itemList, Object... params) {
        if (itemList != null && !itemList.isEmpty()) {
            for (FXHDD01 fxhdd01 : itemList) {
                if (fxhdd01 == null) {
                    continue;
                }
                // 受け取ったItemにエラー時のカラーをセットする。
                fxhdd01.setBackColorInput(ErrUtil.ERR_BACK_COLOR);
            }
        }

        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle messages = context.getApplication().getResourceBundle(context, "myMsg");

        String message = messages.getString(id);
        MessageFormat mf = new MessageFormat(message);

        return mf.format(params);
    }
}
