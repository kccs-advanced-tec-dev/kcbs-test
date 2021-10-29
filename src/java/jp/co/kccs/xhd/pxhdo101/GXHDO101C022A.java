/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import jp.co.kccs.xhd.db.model.FXHDD12;
import jp.co.kccs.xhd.model.GXHDO101C022Model;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質情報管理システム<br>
 * <br>
 * 変更日	2021/10/08<br>
 * 計画書No	MB2109-DK002<br>
 * 変更者	SRC T.Ushiyama<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101C022A(ﾃｰﾋﾟﾝｸﾞ号機選択画面)
 *
 * @author SRC T.Ushiyama
 * @since 2021/10/08
 */
@SessionScoped
@Named("beanGXHDO101C022")
public class GXHDO101C022A implements Serializable {

    /**
     * 登録者
     */
    private FXHDD12 torokusha;
    /**
     * 登録日
     */
    private FXHDD12 toroku_date;
    /**
     * 更新者
     */
    private FXHDD12 koshinsha;
    /**
     * 更新日
     */
    private FXHDD12 koshin_date;
    /**
     * REV
     */
    private FXHDD12 rev;
    /**
     * 工場ｺｰﾄﾞ
     */
    private FXHDD12 kojyo;
    /**
     * ﾛｯﾄNo
     */
    private FXHDD12 lotno;
    /**
     * 枝番
     */
    private FXHDD12 edaban;
    /**
     * 実績No
     */
    private FXHDD12 jissekino;
    /**
     * 番号
     */
    private FXHDD12 bango;
    /**
     * 号機ｺｰﾄﾞ
     */
    private FXHDD12 goki;
    /**
     * 状態ﾌﾗｸﾞ
     */
    private FXHDD12 jotai_flg;

    /**
     * 登録No表示用データ
     */
    private GXHDO101C022Model gxhdO101c022Model;

    /**
     * 登録No表示用データ(表示制御用)
     */
    private GXHDO101C022Model gxhdO101c022ModelView;

    /**
     * 表示render有無
     */
    private boolean tableRender;
    
    /**
     * コンストラクタ
     */
    public GXHDO101C022A() {
    }

    /**
     * 登録者
     *
     * @return the torokusha
     */
    public FXHDD12 getTorokusha() {
        return torokusha;
    }

    /**
     * 登録者
     *
     * @param torokusha the torokusha to set
     */
    public void setTorokusha(FXHDD12 torokusha) {
        this.torokusha = torokusha;
    }

    /**
     * 登録日
     *
     * @return the toroku_date
     */
    public FXHDD12 getToroku_date() {
        return toroku_date;
    }

    /**
     * 登録日
     *
     * @param toroku_date the toroku_date to set
     */
    public void setToroku_date(FXHDD12 toroku_date) {
        this.toroku_date = toroku_date;
    }

    /**
     * 更新者
     *
     * @return the koshinsha
     */
    public FXHDD12 getKoshinsha() {
        return koshinsha;
    }

    /**
     * 更新者
     *
     * @param koshinsha the koshinsha to set
     */
    public void setKoshinsha(FXHDD12 koshinsha) {
        this.koshinsha = koshinsha;
    }

    /**
     * 更新日
     *
     * @return the koshin_date
     */
    public FXHDD12 getKoshin_date() {
        return koshin_date;
    }

    /**
     * 更新日
     *
     * @param koshin_date the koshin_date to set
     */
    public void setKoshin_date(FXHDD12 koshin_date) {
        this.koshin_date = koshin_date;
    }

    /**
     * REV
     *
     * @return the rev
     */
    public FXHDD12 getRev() {
        return rev;
    }

    /**
     * REV
     *
     * @param rev the rev to set
     */
    public void setRev(FXHDD12 rev) {
        this.rev = rev;
    }

    /**
     * 工場ｺｰﾄﾞ
     *
     * @return the kojyo
     */
    public FXHDD12 getKojyo() {
        return kojyo;
    }

    /**
     * 工場ｺｰﾄﾞ
     *
     * @param kojyo the kojyo to set
     */
    public void setKojyo(FXHDD12 kojyo) {
        this.kojyo = kojyo;
    }

    /**
     * ﾛｯﾄNo
     *
     * @return the lotno
     */
    public FXHDD12 getLotno() {
        return lotno;
    }

    /**
     * ﾛｯﾄNo
     *
     * @param lotno the lotno to set
     */
    public void setLotno(FXHDD12 lotno) {
        this.lotno = lotno;
    }

    /**
     * 枝番
     *
     * @return the edaban
     */
    public FXHDD12 getEdaban() {
        return edaban;
    }

    /**
     * 枝番
     *
     * @param edaban the edaban to set
     */
    public void setEdaban(FXHDD12 edaban) {
        this.edaban = edaban;
    }

    /**
     * 実績No
     *
     * @return the jissekino
     */
    public FXHDD12 getJissekino() {
        return jissekino;
    }

    /**
     * 実績No
     *
     * @param jissekino the jissekino to set
     */
    public void setJissekino(FXHDD12 jissekino) {
        this.jissekino = jissekino;
    }

    /**
     * 番号
     *
     * @return the bango
     */
    public FXHDD12 getBango() {
        return bango;
    }

    /**
     * 番号
     *
     * @param bango the bango to set
     */
    public void setBango(FXHDD12 bango) {
        this.bango = bango;
    }

    /**
     * 号機ｺｰﾄﾞ
     *
     * @return the goki
     */
    public FXHDD12 getGoki() {
        return goki;
    }

    /**
     * 号機ｺｰﾄﾞ
     *
     * @param goki the goki to set
     */
    public void setGoki(FXHDD12 goki) {
        this.goki = goki;
    }

    /**
     * 状態ﾌﾗｸﾞ
     *
     * @return the jotai_flg
     */
    public FXHDD12 getJotai_flg() {
        return jotai_flg;
    }

    /**
     * 状態ﾌﾗｸﾞ
     *
     * @param jotai_flg the jotai_flg to set
     */
    public void setJotai_flg(FXHDD12 jotai_flg) {
        this.jotai_flg = jotai_flg;
    }

    /**
     * @return the gxhdO101c022Model
     */
    public GXHDO101C022Model getGxhdO101c022Model() {
        return gxhdO101c022Model;
    }

    /**
     * @param gxhdO101c022Model the gxhdO101c022Model to set
     */
    public void setGxhdO101c022Model(GXHDO101C022Model gxhdO101c022Model) {
        this.gxhdO101c022Model = gxhdO101c022Model;
    }

    /**
     * @return the gxhdO101c022ModelView
     */
    public GXHDO101C022Model getGxhdO101c022ModelView() {
        return gxhdO101c022ModelView;
    }

    /**
     * @param gxhdO101c022ModelView the gxhdO101c022ModelView to set
     */
    public void setGxhdO101c022ModelView(GXHDO101C022Model gxhdO101c022ModelView) {
        this.gxhdO101c022ModelView = gxhdO101c022ModelView;
    }

    /**
     * @return the tableRender
     */
    public boolean isTableRender() {
        return tableRender;
    }

    /**
     * @param tableRender the tableRender to set
     */
    public void setTableRender(boolean tableRender) {
        this.tableRender = tableRender;
    }

    /**
     * 品質確認連絡書の画面を表示する
     *
     * @param bango
     * @return
     */
    public String openXhdFormGXHDO101B048(int bango) {

        // セッション情報
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);

        session.setAttribute("bango", bango);
        session.setAttribute("formClassName", "jp.co.kccs.xhd.pxhdo101.GXHDO101B048");
        session.setAttribute("formId", "GXHDO101B048");
        session.setAttribute("callerFormId", "GXHDO101C022");
        session.setAttribute("formTitle", "ﾃｰﾋﾟﾝｸﾞ・作業");
        session.setAttribute("titleSetting", "LABEL_TITLE");
        
        return "/secure/pxhdo901/gxhdo901a.xhtml?faces-redirect=true";
    }


}
