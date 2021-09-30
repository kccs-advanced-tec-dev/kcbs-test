/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo102;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import jp.co.kccs.xhd.GetModel;
import jp.co.kccs.xhd.common.InitMessage;
import jp.co.kccs.xhd.db.model.FXHDM01;
import jp.co.kccs.xhd.util.CommonUtil;
import jp.co.kccs.xhd.util.DBUtil;
import jp.co.kccs.xhd.util.ErrUtil;
import jp.co.kccs.xhd.util.MessageUtil;
import jp.co.kccs.xhd.util.StringUtil;
import jp.co.kccs.xhd.util.SubFormUtil;
import jp.co.kccs.xhd.util.ValidateUtil;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.primefaces.context.RequestContext;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2021/08/04<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHD102A(品質DB原料入力機能) 入力画面選択
 *
 * @author KCSS K.Jo
 * @since  2021/08/04
 */
@Named
@ViewScoped
public class GXHDO102A implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(GXHDO102A.class.getName());
    private static final String CHARSET = "MS932";
    private static final int LOTNO_BYTE = 14;
    private static final int TANTOSHA_CODE_BYTE = 6;

    /**
     * DataSource(wip)
     */
    @Resource(mappedName = "jdbc/wip")
    private transient DataSource dataSourceWip;
    /**
     * DataSource(DocumentServer)
     */
    @Resource(mappedName = "jdbc/DocumentServer")
    private transient DataSource dataSourceDocServer;
    /**
     * DataSource(QCDB)
     */
    @Resource(mappedName = "jdbc/qcdb")
    private transient DataSource dataSourceXHD;

    /**
     * メニュー項目(権限絞り込みあり)
     */
    private List<FXHDM01> menuListGXHDO102;
    /**
     * メニュー項目(権限絞り込みなし)
     */
    private List<FXHDM01> menuListGXHDO102Nofiltering;
    /**
     * ロットNo
     */
    private String lotNo;
    /**
     * 画面ID
     */
    private String gamenID;
    /**
     * 表示render有無
     */
    private boolean menuTableRender;
    /**
     * 情報メッセージ
     */
    private String infoMessage;
    /**
     * 警告メッセージ
     */
    private String warnMessage;
    /**
     * エラーメッセージ
     */
    private String errorMessage;
    /**
     * 担当者ｺｰﾄﾞ
     */
    private String tantoshaCd;

    /**
     * ロットNo(検索値)
     */
    private String searchLotNo;

    /**
     * 担当者ｺｰﾄﾞ(検索値)
     */
    private String searchTantoshaCd;

    /**
     * リンクページ
     */
    private String linkPage;

    /**
     * 印刷工程画面ID
     */
    private String insatsuKoteiGamenID;

    /**
     * 参照元ﾃﾞｰﾀ
     */
    private FXHDM01 sanshouMotoInfo = null;
    
    /**
     * コンストラクタ
     */
    public GXHDO102A() {
    }

    /**
     * 起動時処理
     */
    public void init() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        String sLotNo = StringUtil.nullToBlank(session.getAttribute("lotNo"));
        String sTantoshaCd = StringUtil.nullToBlank(session.getAttribute("tantoshaCd"));
        this.tantoshaCd = sTantoshaCd;

        session.setAttribute("lotNo", "");
        session.setAttribute("tantoshaCd", "");
        if (!StringUtil.isEmpty(sLotNo)) {
            this.lotNo = sLotNo;
            getMenuListGXHDO102(this.lotNo, this.tantoshaCd, false);
        }
    }

    /**
     * 一覧取得処理
     *
     * @param strGamenLotNo 画面ﾛｯﾄNo
     * @param gamenTantoshaCd 画面担当者
     * @param onInitCheck 初期チェック有無
     * @return 一覧データ
     */
    public List<FXHDM01> getMenuListGXHDO102(String strGamenLotNo, String gamenTantoshaCd, boolean onInitCheck) {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        List<String> userGrpList = (List<String>) session.getAttribute("login_user_group");

        menuListGXHDO102 = new ArrayList<>();
        FacesContext facesContext = FacesContext.getCurrentInstance();

        // user-agent
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String uAgent = request.getHeader("user-agent");
        // model
        GetModel getModel = new GetModel(uAgent);
        String model = getModel.getModel();

        // userAgentでPC or タブレットを判定
        boolean isPC = false;
        if (model.equals("ie11")) {
            isPC = true;
        }

        // ユーザーグループでメニューマスタを検索
        try {

            String strSyurui;

            // 担当者ｺｰﾄﾞ桁数チェック
            if (TANTOSHA_CODE_BYTE != StringUtil.getByte(this.tantoshaCd, CHARSET, LOGGER)) {
                setErrorMessage(MessageUtil.getMessage("XHD-000004", "担当者ｺｰﾄﾞ", TANTOSHA_CODE_BYTE));
                setMenuTableRender(false);
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, getErrorMessage(), null);
                facesContext.addMessage(null, message);
                return null;
            }

            // ロットNo桁数チェック
            if (LOTNO_BYTE != StringUtil.getByte(strGamenLotNo, CHARSET, LOGGER)) {
                setErrorMessage(MessageUtil.getMessage("XHD-000004", "ロットNo", LOTNO_BYTE));
                setMenuTableRender(false);
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, getErrorMessage(), null);
                facesContext.addMessage(null, message);
                return null;
            }

            // ﾛｯﾄNoのﾁｪｯｸﾃﾞｼﾞｯﾄﾁｪｯｸ
            ValidateUtil validateUtil = new ValidateUtil();
            String retMsg = validateUtil.checkValueE001(strGamenLotNo);

            if (!StringUtil.isEmpty(retMsg)) {
                setMenuTableRender(false);
                setErrorMessage(retMsg);
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, getErrorMessage(), null);
                facesContext.addMessage(null, message);
                return null;
            }

            // ﾛｯﾄNo仕掛存在ﾁｪｯｸ
            QueryRunner queryRunnerWip = new QueryRunner(dataSourceWip);

            Map shikakariData = loadShikakariData(queryRunnerWip, lotNo);
            if (shikakariData == null || shikakariData.isEmpty()) {
                setMenuTableRender(false);
                setErrorMessage(MessageUtil.getMessage("XHD-000039"));
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, getErrorMessage(), null);
                facesContext.addMessage(null, message);
                return null;
            }

            // 担当者ﾏｽﾀﾁｪｯｸ
            retMsg = validateUtil.checkT002("担当者ｺｰﾄﾞ", this.tantoshaCd, queryRunnerWip);
            if (!StringUtil.isEmpty(retMsg)) {
                setMenuTableRender(false);
                setErrorMessage(retMsg);
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, getErrorMessage(), null);
                facesContext.addMessage(null, message);
                return null;
            }

            // 設計情報の取得
            String strKojyo = strGamenLotNo.substring(0, 3);
            String strLotNo = strGamenLotNo.substring(3, 11);
            String strEdaban = strGamenLotNo.substring(11, 14);

            QueryRunner queryRunnerXHD = new QueryRunner(dataSourceXHD);

            Map processResult = CommonUtil.getMkSekkeiInfo(queryRunnerXHD, queryRunnerWip, strKojyo, strLotNo, "001");

            if (null == processResult || processResult.isEmpty()) {
                setMenuTableRender(false);
                setInfoMessage(MessageUtil.getMessage("XHD-000031"));
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, getInfoMessage(), null);
                facesContext.addMessage(null, message);
                return null;
            }else {
                strSyurui = StringUtil.nullToBlank(processResult.get("syurui"));
            }
            
            QueryRunner queryRunnerDoc = new QueryRunner(dataSourceDocServer);
            String sqlsearchGamenID = "SELECT gamen_id, kotei_jun FROM fxhdm03 WHERE kotei_process_kubun = ? order by kotei_jun ";
            List<Object> params2 = new ArrayList<>();
            params2.add(strSyurui);

            List sqlsearchResult = (List) queryRunnerDoc.query(sqlsearchGamenID, new MapListHandler(), params2.toArray());

            List<Object> listGamenID = new ArrayList<>();
            for (Iterator i = sqlsearchResult.iterator(); i.hasNext();) {
                HashMap m = (HashMap) i.next();
                listGamenID.add(m.get("gamen_id").toString());
            }

            if (sqlsearchResult.isEmpty()) {
                setMenuTableRender(false);
                setInfoMessage(MessageUtil.getMessage("XHD-000031"));
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, getInfoMessage(), null);
                facesContext.addMessage(null, message);
                return null;
            }

            // 権限で絞り込んだメニュー情報の取得
            this.menuListGXHDO102 = getMenuList(queryRunnerDoc, listGamenID, userGrpList, isPC);

            // 権限で絞り込んでいないメニュー情報の取得
            this.menuListGXHDO102Nofiltering = getMenuListNoAuthorityFiltering(queryRunnerDoc, listGamenID);

            // メニューデータが無い場合はエラー
            if (menuListGXHDO102.isEmpty() || this.menuListGXHDO102Nofiltering.isEmpty()) {
                setMenuTableRender(false);
                setInfoMessage(MessageUtil.getMessage("XHD-000031"));
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, getInfoMessage(), null);
                facesContext.addMessage(null, message);
            }

            // メッセージリスト
            List<String> messageListMain = new ArrayList<>();  // 権限絞り込みなしメニューの処理で使用するリスト(メイン)

            //画面ID(動的メニュー)追加
            addRecurringMenu(this.menuListGXHDO102, queryRunnerDoc, strKojyo, strLotNo, strEdaban);
            addRecurringMenu(this.menuListGXHDO102Nofiltering, queryRunnerDoc, strKojyo, strLotNo, strEdaban);

            // 実績状態を表示(権限絞り込みありメニューのみ)
            List<String[]> fxhdd11List = loadFxhdd11LotNoTaniInfoList(queryRunnerDoc, strGamenLotNo,listGamenID);
            setJissekiJotai(this.menuListGXHDO102, fxhdd11List);
            setJissekiJotai(this.menuListGXHDO102Nofiltering, fxhdd11List);

            // チェックメッセージの表示
            if (onInitCheck && !messageListMain.isEmpty()) {

                messageListMain.add("　");
                messageListMain.add("　技術に連絡してください");
                // メッセージを画面に渡す
                InitMessage beanInitMessage = (InitMessage) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_INIT_MESSAGE);
                beanInitMessage.setInitMessageList(messageListMain);

                RequestContext context = RequestContext.getCurrentInstance();
                context.addCallbackParam("firstParam", "initMessage");
            }

            setMenuTableRender(true);
            // 検索した時点のﾛｯﾄNoと担当者ｺｰﾄﾞを保持する。
            this.searchLotNo = strGamenLotNo;
            this.searchTantoshaCd = gamenTantoshaCd;

        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("メニュー項目未登録", ex, LOGGER);
            menuListGXHDO102 = new ArrayList<>();
        } catch (CloneNotSupportedException ex) {
            ErrUtil.outputErrorLog("実行時エラー", ex, LOGGER);
            menuListGXHDO102 = new ArrayList<>();
        }

        return menuListGXHDO102;
    }

    /**
     * 参照元ﾃﾞｰﾀ
     *
     * @return the sanshouMotoInfo
     */
    public FXHDM01 getSanshouMotoInfo() {
        return sanshouMotoInfo;
    }

    /**
     * 参照元ﾃﾞｰﾀ
     *
     * @param sanshouMotoInfo the sanshouMotoInfo to set
     */
    public void setSanshouMotoInfo(FXHDM01 sanshouMotoInfo) {
        this.sanshouMotoInfo = sanshouMotoInfo;
    }

    /**
     * 品質DBメンテナンス画面open
     *
     * @param rowData 選択行のデータ
     * @return 遷移先画面
     */
    public String openXhdForm(FXHDM01 rowData) {
        try {

            // 権限の絞り込み無しのメニューをもとに前工程自身の一つ前のメニュー情報を取得する。
            FXHDM01 maeKoteiMenuInfo = null; // 前工程情報
            for (FXHDM01 fxhdm01 : this.menuListGXHDO102Nofiltering) {
                if (fxhdm01.getFormId().equals(rowData.getFormId())) {
                    break;
                }
                // 一つ前の情報を前工程情報として保持
                maeKoteiMenuInfo = fxhdm01;
            }

            // 前工程のメニュー情報を取得している場合、前工程情報を取得
            Map maekoteiInfo = null;
            if (maeKoteiMenuInfo != null) {
                maekoteiInfo = getMaeKoteiInfo(maeKoteiMenuInfo.getFormId(), maeKoteiMenuInfo.getJissekiNo());
            }

            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpSession session = (HttpSession) externalContext.getSession(false);

            // 画面遷移パラメータをセッション変数に保持
            session.setAttribute("formId", rowData.getFormId());
            session.setAttribute("callerFormId", "GXHDO102A");
            session.setAttribute("formTitle", rowData.getFormTitle());
            session.setAttribute("menuName", rowData.getMenuName());
            session.setAttribute("menuComment", rowData.getMenuComment());
            session.setAttribute("titleSetting", rowData.getTitleSetting());
            session.setAttribute("formClassName", rowData.getFormClassName());
            session.setAttribute("hyojiKensu", rowData.getHyojiKensu());
            session.setAttribute("lotNo", this.searchLotNo);
            session.setAttribute("tantoshaCd", this.searchTantoshaCd);
            session.setAttribute("jissekino", rowData.getJissekiNo());
            // ﾛｯﾄ参照情報初期化
            session.setAttribute("maeGamenID", "");
            session.setAttribute("sanshouMotoLotNo", "");

            // 自身の枝番でない(親ﾛｯﾄの枝番)場合、前工程情報は引き渡さない
            if (StringUtil.nullToBlank(getMapData(maekoteiInfo, "edaban")).equals(this.searchLotNo.substring(11, 14))) {
                session.setAttribute("maekoteiInfo", maekoteiInfo);
            } else {
                session.setAttribute("maekoteiInfo", null);
            }
            if (maeKoteiMenuInfo != null) {
                session.setAttribute("maekoteiFormId", maeKoteiMenuInfo.getFormId());
            } else {
                session.setAttribute("maekoteiFormId", null);
            }

            // 前工程が存在するかつ前工程のデータが取得できなかった場合
            if (maeKoteiMenuInfo != null && maekoteiInfo == null) {
                this.warnMessage = MessageUtil.getMessage("XHD-000035", maeKoteiMenuInfo.getMenuName());
                this.linkPage = rowData.getLinkChar() + "?faces-redirect=true";
                RequestContext context = RequestContext.getCurrentInstance();
                context.addCallbackParam("firstParam", "warning");
                return "";
            }

            return rowData.getLinkChar() + "?faces-redirect=true";
            
        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("メニュー項目未登録", ex, LOGGER);
        }
        return "";

    }

    /**
     * 品質DB画面データ検索
     */
    public void searchHinshitsuData() {
        getMenuListGXHDO102(this.lotNo, this.tantoshaCd, true);
    }

    /**
     * 品質DB画面データ再検索
     *
     * @param isError エラー有無(サブ画面からの戻り)
     */
    public void reSearchHinshitsuData(boolean isError) {
        if (isError) {
            return;
        }
        menuListGXHDO102 = new ArrayList<>();
        getMenuListGXHDO102(this.searchLotNo, this.searchTantoshaCd, false);
    }

    /**
     * 一覧表示データを返却します。
     *
     * @return 一覧データ
     */
    public List<FXHDM01> getListData() {
        return this.menuListGXHDO102;
    }

    /**
     * 前画面に戻ります。
     *
     * @return ログイン用URL文字列
     */
    public String btnReturn() {
        return "/pxhdo012/gxhdo012a.xhtml?faces-redirect=true";
    }

    /**
     * 画面ID
     *
     * @return the GamenID
     */
    public String getGamenID() {
        return gamenID;
    }

    /**
     * 画面ID
     *
     * @param gamenID
     */
    public void setGamenID(String gamenID) {
        this.gamenID = gamenID;
    }

    /**
     * 印刷工程画面ID
     *
     * @return the insatsuKoteiGamenID
     */
    public String getInsatsuKoteiGamenID() {
        return insatsuKoteiGamenID;
    }

    /**
     * 印刷工程画面ID
     *
     * @param insatsuKoteiGamenID
     */
    public void setInsatsuKoteiGamenID(String insatsuKoteiGamenID) {
        this.insatsuKoteiGamenID = insatsuKoteiGamenID;
    }

    /**
     * ロットNo
     *
     * @return the lotNo
     */
    public String getLotNo() {
        return lotNo;
    }

    /**
     * ロットNo
     *
     * @param lotNo
     */
    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    /**
     * 表示render有無
     *
     * @param menuTableRender
     */
    public void setMenuTableRender(boolean menuTableRender) {
        this.menuTableRender = menuTableRender;
    }

    /**
     * 表示render有無
     *
     * @return menuTableRender
     */
    public boolean getMenuTableRender() {
        return menuTableRender;
    }

    /**
     * エラーメッセージ
     *
     * @return the errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * エラーメッセージ
     *
     * @param errorMessage the errorMessage to set
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * 情報メッセージ
     *
     * @return the infoMessage
     */
    public String getInfoMessage() {
        return infoMessage;
    }

    /**
     * 情報メッセージ
     *
     * @param infoMessage the infoMessage to set
     */
    public void setInfoMessage(String infoMessage) {
        this.infoMessage = infoMessage;
    }

    /**
     * 警告メッセージ
     *
     * @return the warnMessage
     */
    public String getWarnMessage() {
        return warnMessage;
    }

    /**
     * 警告メッセージ
     *
     * @param warnMessage the warnMessage to set
     */
    public void setWarnMessage(String warnMessage) {
        this.warnMessage = warnMessage;
    }

    /**
     * リンクページ
     *
     * @return the linkPage
     */
    public String getLinkPage() {
        return linkPage;
    }

    /**
     * リンクページ
     *
     * @param linkPage the linkPage to set
     */
    public void setLinkPage(String linkPage) {
        this.linkPage = linkPage;
    }

    /**
     * 担当者ｺｰﾄﾞ
     *
     * @return the tantoshaCd
     */
    public String getTantoshaCd() {
        return tantoshaCd;
    }

    /**
     * 担当者ｺｰﾄﾞ
     *
     * @param tantoshaCd the tantoshaCd to set
     */
    public void setTantoshaCd(String tantoshaCd) {
        this.tantoshaCd = tantoshaCd;
    }

    /**
     * [品質DB登録実績]から、ﾘﾋﾞｼﾞｮﾝ,状態ﾌﾗｸﾞを取得
     *
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param formId 画面ID(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadFxhdd11RevInfo(QueryRunner queryRunnerDoc, String kojyo, String lotNo,
            String edaban, String formId) throws SQLException {

        // 品質DB登録実績情報の取得
        String sql = " SELECT rev, jotai_flg, edaban "
                + " FROM fxhdd11 "
                + " WHERE kojyo = ? AND lotno = ? "
                + " AND edaban = ? AND gamen_id = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(formId);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerDoc.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * Mapから値を取得する(マップがNULLまたは空の場合はNULLを返却)
     *
     * @param map マップ
     * @param mapId ID
     * @return マップから取得した値
     */
    private Object getMapData(Map map, String mapId) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        return map.get(mapId);
    }

    /**
     * 前工程情報の取得
     *
     * @param maeKoteiGamenId 前工程画面ID
     * @param maeKoteiJissekiNo 前工程実績No
     * @return 前工程情報
     * @throws SQLException 例外エラー
     */
    private Map getMaeKoteiInfo(String maeKoteiGamenId, int maeKoteiJissekiNo) throws SQLException {
        String strKojyo = this.searchLotNo.substring(0, 3);
        String strLotNo = this.searchLotNo.substring(3, 11);
        String strEdaban = this.searchLotNo.substring(11, 14);
        QueryRunner queryRunnerDoc = new QueryRunner(dataSourceDocServer);
        QueryRunner queryRunnerWip = new QueryRunner(dataSourceWip);
        QueryRunner queryRunnerQcdb = new QueryRunner(dataSourceXHD);

        // 前工程の実績情報を取得
        Map fxhdd11Info = getJissekiInfo(queryRunnerDoc, queryRunnerWip, strKojyo, strLotNo, strEdaban, maeKoteiGamenId);
        String jotaiFlg = StringUtil.nullToBlank(getMapData(fxhdd11Info, "jotai_flg")); //状態フラグ

        // 登録済み実績情報があった場合
        if ("1".equals(jotaiFlg)) {
            return CommonUtil.getMaeKoteiData(queryRunnerQcdb, maeKoteiGamenId, strKojyo, strLotNo,
                    StringUtil.nullToBlank(getMapData(fxhdd11Info, "edaban")), StringUtil.nullToBlank(getMapData(fxhdd11Info, "rev")), maeKoteiJissekiNo);
        }
        return null;
    }

    /**
     * 実績情報の取得
     *
     * @param queryRunnerDoc queryRunnerオブジェクト(DocServer)
     * @param queryRunnerWip queryRunnerオブジェクト(Wip)
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param gamenId 画面ID
     * @param jissekino 実績No
     * @return 実績情報
     * @throws SQLException 例外エラー
     */
    private Map getJissekiInfo(QueryRunner queryRunnerDoc, QueryRunner queryRunnerWip, String kojyo, String lotNo, String edaban, String gamenId) throws SQLException {

        // 実績を検索
        Map fxhdd11RevInfo = loadFxhdd11RevInfo(queryRunnerDoc, kojyo, lotNo, edaban, gamenId);
        // 状態フラグが空(実績がない)の場合、自身の親の実績を探しに行く
        if (StringUtil.isEmpty(StringUtil.nullToBlank(getMapData(fxhdd11RevInfo, "jotai_flg")))) {

            // 親ﾛｯﾄの枝番
            String oyalotEdaban = getOyaLotEdaban(queryRunnerWip, kojyo, lotNo, edaban);
            if (StringUtil.isEmpty(oyalotEdaban)) {
                // 該当データがない場合はリターン
                return null;
            }

            // 親ﾛｯﾄの実績を検索
            return getJissekiInfo(queryRunnerDoc, queryRunnerWip, kojyo, lotNo, oyalotEdaban, gamenId);
        }

        return fxhdd11RevInfo;
    }

    /**
     * 親ﾛｯﾄ枝番の取得
     *
     * @param queryRunnerWip QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private String getOyaLotEdaban(QueryRunner queryRunnerWip, String kojyo, String lotNo, String edaban) throws SQLException {

        // 仕掛情報データの取得
        String sql = "SELECT oyalotedaban "
                + " FROM sikakari WHERE kojyo = ? AND lotno = ? AND edaban = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        Map sikakariInfo = queryRunnerWip.query(sql, new MapHandler(), params.toArray());
        return StringUtil.nullToBlank(getMapData(sikakariInfo, "oyalotedaban"));
    }

    /**
     * 実績状態の設定を行う。
     *
     * @param menuListGXHDO102 メニューリスト
     * @param fxhdd11List 実績リスト
     * @throws SQLException 例外エラー
     */
    private void setJissekiJotai(List<FXHDM01> menuListGXHDO102, List<String[]> fxhdd11List) throws SQLException {

        boolean existJiseki;
        for (FXHDM01 fxhdm01 : menuListGXHDO102) {
            existJiseki = false;
            for (String[] fxhdd11Info : fxhdd11List) {
                if (fxhdm01.getFormId().equals(fxhdd11Info[0]) && StringUtil.nullToBlank(fxhdm01.getJissekiNo()).equals(fxhdd11Info[1])) {
                    fxhdm01.setJotaiFlg(fxhdd11Info[2]);
                    switch (fxhdd11Info[2]) {
                        case "0":
                            fxhdm01.setMenuComment("仮登録");
                            break;
                        case "1":
                            fxhdm01.setMenuComment("登録済");
                            break;
                        default:
                            fxhdm01.setMenuComment("未登録");
                            break;
                    }
                    // 実績が存在した場合
                    existJiseki = true;
                    break;
                }
            }
            // 実績が無かった場合
            if (!existJiseki) {
                fxhdm01.setJotaiFlg("");
                fxhdm01.setMenuComment("未登録");
            }
        }
    }

    /**
     * [原料品質DB登録実績]から、ﾛｯﾄNo単位で状態ﾌﾗｸﾞ,実績No,ﾘﾋﾞｼﾞｮﾝ,を取得
     *
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<String[]> loadFxhdd11LotNoTaniInfoList(QueryRunner queryRunnerDoc,String gamenLotNo, List<Object> listGamenID) throws SQLException {

        // 品質DB登録実績情報の取得
        String sql = " SELECT "
                + "     QJ.gamen_id "
                + "    ,QJ.jissekino "
                + "    ,QJ.jotai_flg "
                + "    ,QJ.kojyo "
                + "    ,QJ.lotno "
                + "    ,QJ.edaban "
                + "  FROM fxhdd11 QJ "
                + "  JOIN( "
                + "    SELECT "
                + "      MAX(CONCAT( kojyo,lotno,edaban)) AS queryLotno "
                + "      ,gamen_id AS queryGamenID "
                + "     FROM fxhdd11 "
                + "    WHERE CONCAT( kojyo,lotno,edaban) IN (?) ";
                if (!listGamenID.isEmpty()) {
                    sql += " AND ";
                    sql += DBUtil.getInConditionPreparedStatement("gamen_id", listGamenID.size());
                }
            sql += "    AND jotai_flg <> '9' "
                + " GROUP BY gamen_id "
                + "  ) DD11 "
                + "  ON  CONCAT( QJ.kojyo,QJ.lotno,QJ.edaban ) = DD11.queryLotno "
                + "  AND QJ.gamen_id = DD11.queryGamenID ";

        List<Object> params = new ArrayList<>();
        params.add(gamenLotNo);
        params.addAll(listGamenID);
        
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        List processResult = (List) queryRunnerDoc.query(sql, new MapListHandler() ,params.toArray());

        List<String[]> fxhdd11InfoList = new ArrayList<>();
        for (Iterator i = processResult.iterator(); i.hasNext();) {
            HashMap m = (HashMap) i.next();
            fxhdd11InfoList.add(new String[]{m.get("gamen_id").toString(), m.get("jissekino").toString(), m.get("jotai_flg").toString()});
        }

        return fxhdd11InfoList;
    }

    /**
     * メニューリスト取得(権限絞りこみあり)
     *
     * @param queryRunnerDoc データオブジェクト
     * @param listGamenID 画面IDリスト
     * @param userGrpList ユーザーグループリスト
     * @param isPC PC判定
     * @return メニューリスト(権限絞り込みなし)
     * @throws SQLException 例外エラー
     */
    private List<FXHDM01> getMenuList(QueryRunner queryRunnerDoc, List<Object> listGamenID, List<String> userGrpList, boolean isPC) throws SQLException {
        String sql = " SELECT gamen_id,gamen_title,title_setting,menu_no,menu_name,link_char,menu_parameter,menu_comment,gamen_classname,hyouji_kensu "
                + " FROM fxhdm01 WHERE menu_group_id = 'QCDB' ";

        if (!listGamenID.isEmpty()) {
            sql += " AND ";
            sql += DBUtil.getInConditionPreparedStatement("gamen_id", listGamenID.size());
        }
        sql += " AND " + DBUtil.getInConditionPreparedStatement("user_role", userGrpList.size());

        if (!isPC) {
            sql += " AND pc_flg = '0' ";
        }

        sql += " ORDER BY gamen_id ASC, menu_no ASC ";

        Map<String, String> mapping = new HashMap<>();
        mapping.put("gamen_id", "formId");
        mapping.put("gamen_title", "formTitle");
        mapping.put("title_setting", "titleSetting");
        mapping.put("menu_no", "menuNo");
        mapping.put("menu_name", "menuName");
        mapping.put("link_char", "linkChar");
        mapping.put("menu_parameter", "menuParam");
        mapping.put("menu_comment", "menuComment");
        mapping.put("gamen_classname", "formClassName");
        mapping.put("hyouji_kensu", "hyojiKensu");

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<FXHDM01>> beanHandler = new BeanListHandler<>(FXHDM01.class, rowProcessor);

        List<Object> params = new ArrayList<>();
        params.addAll(listGamenID);
        params.addAll(userGrpList);

        if (listGamenID.isEmpty()) {
            DBUtil.outputSQLLog(sql, userGrpList.toArray(), LOGGER);
            return queryRunnerDoc.query(sql, beanHandler, userGrpList.toArray());
        } else {
            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            return queryRunnerDoc.query(sql, beanHandler, params.toArray());
        }
    }

    /**
     * メニューリスト取得(権限絞り込みなし)
     *
     * @param queryRunnerDoc データオブジェクト
     * @param listGamenID 画面IDリスト
     * @return メニューリスト(権限絞り込みなし)
     * @throws SQLException 例外エラー
     */
    private List<FXHDM01> getMenuListNoAuthorityFiltering(QueryRunner queryRunnerDoc, List<Object> listGamenID) throws SQLException {
        String sql = " SELECT gamen_id,menu_name "
                + " FROM fxhdm01 WHERE menu_group_id = 'QCDB' ";

        if (!listGamenID.isEmpty()) {
            sql += " AND ";
            sql += DBUtil.getInConditionPreparedStatement("gamen_id", listGamenID.size());
        }

        sql += " ORDER BY menu_no ASC ";

        Map<String, String> mapping = new HashMap<>();
        mapping.put("gamen_id", "formId");
        mapping.put("menu_name", "menuName");

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<FXHDM01>> beanHandler = new BeanListHandler<>(FXHDM01.class, rowProcessor);

        List<Object> params = new ArrayList<>();
        params.addAll(listGamenID);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerDoc.query(sql, beanHandler, params.toArray());

    }

    /**
     * 仕掛データ検索
     *
     * @param queryRunnerWip QueryRunnerオブジェクト
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadShikakariData(QueryRunner queryRunnerWip, String lotNo) throws SQLException {
        String lotNo1 = lotNo.substring(0, 3);
        String lotNo2 = lotNo.substring(3, 11);
        String lotNo3 = lotNo.substring(11, 14);

        // 仕掛情報データの取得
        String sql = " SELECT kcpno, oyalotedaban, suuryo, torikosuu, lotkubuncode, ownercode, tokuisaki "
                + " FROM sikakari WHERE kojyo = ? AND lotno = ? AND edaban = ? ";

        List<Object> params = new ArrayList<>();
        params.add(lotNo1);
        params.add(lotNo2);
        params.add(lotNo3);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerWip.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * 繰り返しメニュー追加処理
     *
     * @param menuListGXHDO102 メニューリスト
     * @param queryRunnerDoc queryRunnerオブジェクト(DocumentServer)
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotno ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     * @throws CloneNotSupportedException 例外エラー
     */
    private void addRecurringMenu(List<FXHDM01> menuListGXHDO102, QueryRunner queryRunnerDoc, String kojyo, String lotno, String edaban) throws SQLException, CloneNotSupportedException {

        // 工程別のメニュー名を保持
        Map<String, String> mapMenuName = new HashMap<>();
        for (FXHDM01 fxhdm01 : menuListGXHDO102) {
            mapMenuName.put(fxhdm01.getFormId(), fxhdm01.getMenuName());
        }

        List<Map<String, Object>> fxhdd08Info = loadFxhdd08InfoList(queryRunnerDoc, kojyo, lotno, edaban);

        // 実績NoのMAX値を保持するMAP
        Map mapMaxJissekiNo = new HashMap();
        int idx = 0;
        while (true) {

            if (menuListGXHDO102.size() <= idx) {
                break;
            }

            // 画面制御情報をもとにメニュー追加
            FXHDM01 fxhdm01 = menuListGXHDO102.get(idx);
            if (addInsertMenu(menuListGXHDO102, idx, fxhdd08Info, mapMaxJissekiNo)) {
                continue;
            }

            // その他メニューの実績No割り当て処理
            if (fxhdm01.getJissekiNo() == 0) {
                // 番号の割り当て
                fxhdm01.setJissekiNo(getJissekino(mapMaxJissekiNo, fxhdm01.getFormId(), fxhdd08Info));
                continue;
            }

            idx++;
        }

    }
    
    /**
     * [画面制御]から情報を取得
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<Map<String, Object>> loadFxhdd08InfoList(QueryRunner queryRunnerDoc, String kojyo, String lotNo,
            String edaban) throws SQLException {

        // 品質DB登録実績情報の取得
        String sql = "SELECT gamen_id,jissekino,maekoutei_gamen_id"
                + ",maekoutei_jissekino,deleteflag,koshin_date, 0 AS compflag "
                + "FROM fxhdd08 "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? "
                + "ORDER BY gamen_id, jissekino";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerDoc.query(sql, new MapListHandler(), params.toArray());

    }

    /**
     * 画面ID別の最大実績No取得
     *
     * @param mapJissekiNo 実績Noマップ
     * @param gamenId 画面ID
     * @param fxhdd08Info 画面制御情報
     * @return 実績No
     */
    private int getJissekino(Map mapJissekiNo, String gamenId, List<Map<String, Object>> fxhdd08Info) {
        // 最大の実績Noを取得
        int jissekiNo = getMaxJissekiNo(mapJissekiNo, gamenId, fxhdd08Info);

        // 最大実績No更新
        if (!mapJissekiNo.containsKey(gamenId) || (int) mapJissekiNo.get(gamenId) < jissekiNo) {
            mapJissekiNo.put(gamenId, jissekiNo);
        }

        return jissekiNo;
    }

    /**
     * 最大実績Noの取得
     *
     * @param mapJissekiNo 実績NoMap
     * @param formId 画面ID
     * @param fxhdd08Info 画面制御情報
     * @return 最大実績NO
     */
    private int getMaxJissekiNo(Map mapJissekiNo, String formId, List<Map<String, Object>> fxhdd08Info) {
        int jissekiNo = 1;
        if (mapJissekiNo.containsKey(formId)) {
            jissekiNo = (int) mapJissekiNo.get(formId) + 1;
        }

        // 画面制御情報から実績Noのリストを取得
        List<Object> jissekiNoList = fxhdd08Info.stream().filter(n -> StringUtil.nullToBlank(n.get("gamen_id")).equals(formId)).map(f -> f.get("jissekino")).collect(Collectors.toList());

        // 画面制御情報に無い番号まで採番
        while (jissekiNoList.contains(jissekiNo)) {
            jissekiNo++;
        }

        return jissekiNo;
    }

    /**
     * 画面制御情報をもとにしたメニュー追加
     *
     * @param menuListGXHDO102 メニューリスト
     * @param fxhdd08InfoList 画面制御情報リスト
     * @param mapMaxJissekiNo 最大実績NoMap
     * @return true：追加あり、false:追加なし
     * @throws CloneNotSupportedException 例外エラー
     * @throws SQLException 例外エラー
     */
    private boolean addInsertMenu(List<FXHDM01> menuListGXHDO102, int menuIdx, List<Map<String, Object>> fxhdd08InfoList,
            Map mapMaxJissekiNo) throws CloneNotSupportedException, SQLException {

        // 対象のメニューを取得
        FXHDM01 fxhdm01 = menuListGXHDO102.get(menuIdx);
        Map<String, Object> fxhdd08Info = fxhdd08InfoList.stream()
                .filter(n -> "0".equals(StringUtil.nullToBlank(n.get("deleteflag"))))
                .filter(n -> "0".equals(StringUtil.nullToBlank(n.get("compflag"))))
                .filter(n -> StringUtil.nullToBlank(n.get("maekoutei_gamen_id")).equals(fxhdm01.getFormId()))
                .filter(n -> (int) n.get("maekoutei_jissekino") == fxhdm01.getJissekiNo()).findFirst().orElse(null);

        if (fxhdd08Info == null) {
            // 画面制御情報が無い場合はfalseをリターン
            return false;
        }

        // 処理済みとしてフラグを立てる
        fxhdd08Info.put("compflag", 1);

        String gamenId = StringUtil.nullToBlank(fxhdd08Info.get("gamen_id"));
        int jissekino = (int) fxhdd08Info.get("jissekino");

        // 最大の実績Noを保持
        if (!mapMaxJissekiNo.containsKey(gamenId) || (int) mapMaxJissekiNo.get(gamenId) < jissekino) {
            mapMaxJissekiNo.put(gamenId, jissekino);
        }

        return true;
    }    
}