/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.io.Serializable;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質情報管理システム<br>
 * <br>
 * 変更日	2021/08/21<br>
 * 計画書No	MB2008-DK001<br>
 * 変更者	SRC K.Ijuin<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101D001(B･Cﾗﾝｸ発行)
 *
 * @author SRC K.Ijuin
 * @since 2021/08/21
 */
@SessionScoped
@Named("beanGXHDO101D001")
public class GXHDO101D001 implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(GXHDO101D001.class.getName());

    /**
     * コンストラクタ
     */
    public GXHDO101D001() {

    }

    /**
     * 初期化処理
     */
    public void init() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
    }
}
