/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
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
import jp.co.kccs.xhd.db.model.DaJoken;
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
 * 変更日	2018/11/14<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHD101A(品質DB入力機能) 入力画面選択
 *
 * @author KCSS K.Jo
 * @since 2018/11/14
 */
@Named
@ViewScoped
public class GXHDO101A implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(GXHDO101A.class.getName());
    private static final String CHARSET = "MS932";
    private static final int LOTNO_BYTE = 14;
    private static final int TANTOSHA_CODE_BYTE = 6;

    private static final String FORM_ID_SAISANKA = "GXHDO101B019";
    private static final String FORM_ID_KENMA_BARREL = "GXHDO101B020";
    private static final String FORM_ID_KENMA_KEISU = "GXHDO101B021";
    private static final String FORM_ID_JIKI_QC = "GXHDO101B022";
    private static final String FORM_ID_GAIBUDENKYOKU_TOFU_TANSHI = "GXHDO101B025";
    private static final String FORM_ID_GAIBUDENKYOKU_TOFU = "GXHDO101B026";
    
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
    private List<FXHDM01> menuListGXHDO101;
    /**
     * メニュー項目(権限絞り込みなし)
     */
    private List<FXHDM01> menuListGXHDO101Nofiltering;
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
     * コンストラクタ
     */
    public GXHDO101A() {
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
            getMenuListGXHDO101();
        }
    }

    /**
     * 一覧取得処理
     *
     * @return 一覧データ
     */
    public List<FXHDM01> getMenuListGXHDO101() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        List<String> userGrpList = (List<String>) session.getAttribute("login_user_group");

        menuListGXHDO101 = new ArrayList<>();
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

            // ロットNoを取得する
            String strGamenLotNo = getLotNo();

            String strProcess = "";

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

            QueryRunner queryRunnerWip = new QueryRunner(dataSourceWip);
            retMsg = validateUtil.checkT002("担当者ｺｰﾄﾞ", this.tantoshaCd, queryRunnerWip);
            if (!StringUtil.isEmpty(retMsg)) {
                setMenuTableRender(false);
                setErrorMessage(retMsg);
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, getErrorMessage(), null);
                facesContext.addMessage(null, message);
                return null;
            }

            String strKojyo = strGamenLotNo.substring(0, 3);
            String strLotNo = strGamenLotNo.substring(3, 11);
            String strEdaban = strGamenLotNo.substring(11, 14);
            String strSekkeiNo = "";

            QueryRunner queryRunnerXHD = new QueryRunner(dataSourceXHD);
            String sqlsearchProcess = "SELECT PrintFmt,SEKKEINO FROM da_sekkei WHERE KOJYO = ? AND LOTNO = ? AND EDABAN = ? ";
            List<Object> params = new ArrayList<>();
            params.add(strKojyo);
            params.add(strLotNo);
            params.add("001");
            List processResult = (List) queryRunnerXHD.query(sqlsearchProcess, new MapListHandler(), params.toArray());
            for (Iterator i = processResult.iterator(); i.hasNext();) {
                HashMap m = (HashMap) i.next();
                strProcess = m.get("PrintFmt").toString();
                strSekkeiNo = m.get("SEKKEINO").toString();
            }

            if (processResult.isEmpty()) {
                setMenuTableRender(false);
                setInfoMessage(MessageUtil.getMessage("XHD-000031"));
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, getInfoMessage(), null);
                facesContext.addMessage(null, message);
                return null;
            }

            QueryRunner queryRunnerDoc = new QueryRunner(dataSourceDocServer);
            String sqlsearchGamenID = "SELECT gamen_id, kotei_jun FROM fxhdm03 WHERE kotei_process_kubun = ? order by kotei_jun ";
            List<Object> params2 = new ArrayList<>();
            params2.add(strProcess);

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
            this.menuListGXHDO101 = getMenuList(queryRunnerDoc, listGamenID, userGrpList, isPC);

            // 権限で絞り込んでいないメニュー情報の取得
            this.menuListGXHDO101Nofiltering = getMenuListNoAuthorityFiltering(queryRunnerDoc, listGamenID);

            // 画面IDﾌｨﾙﾀﾘﾝｸﾞ
            List<String> filterGamenIdList = getFilterGamenIdList(queryRunnerDoc, queryRunnerXHD, strSekkeiNo);
            menuListFiltering(this.menuListGXHDO101, filterGamenIdList);// 権限絞り込みありメニュー
            menuListFiltering(this.menuListGXHDO101Nofiltering, filterGamenIdList);// 権限絞り込みなしメニュー

            // メニューデータが無い場合はエラー
            if (menuListGXHDO101.isEmpty() || this.menuListGXHDO101Nofiltering.isEmpty()) {
                setMenuTableRender(false);
                setInfoMessage(MessageUtil.getMessage("XHD-000031"));
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, getInfoMessage(), null);
                facesContext.addMessage(null, message);
            }

            List<String> messageList = new ArrayList<>();
            List<String> messageListNoFiltering = new ArrayList<>(); // 不要だが関数の呼び出しで使用する為、仮で定義

            // 再酸化とﾊﾞﾚﾙは事前にメニューの名称を保持しておく
            String menuNameSaisanka = "";
            String menuNameBarrel = "";
            for (FXHDM01 fxhdm01 : this.menuListGXHDO101Nofiltering) {
                if (FORM_ID_SAISANKA.equals(fxhdm01.getFormId())) {
                    menuNameSaisanka = fxhdm01.getMenuName();
                } else if (FORM_ID_KENMA_BARREL.equals(fxhdm01.getFormId())) {
                    menuNameBarrel = fxhdm01.getMenuName();
                }
            }

            //画面ID(メニュー)追加(再酸化)
            //規格値(再酸化回数)取得 ※再酸化の画面IDが存在する場合のみ使用するが「権限絞り込みあり・なし」で2回呼び出される可能性があるため事前に取得
            String kikakuchi = getSaisankaKikakuchi(queryRunnerXHD, strSekkeiNo);
            addMenuSaisanka(this.menuListGXHDO101, messageList, kikakuchi, menuNameSaisanka);// 権限絞り込みありメニュー
            addMenuSaisanka(this.menuListGXHDO101Nofiltering, messageListNoFiltering, kikakuchi, menuNameSaisanka);// 権限絞り込みメニュー

            //画面ID(メニュー)追加(磁器QC)
            // 実績情報(状態ﾌﾗｸﾞ、実績No、Revを取得)※磁器QCの画面IDが存在する場合のみ使用するが「権限絞り込みあり・なし」で2回呼び出される可能性があるため事前に取得
            List<String[]> fxhdd03InfoList = loadFxhdd03InfoList(queryRunnerDoc, strKojyo, strLotNo, strEdaban, FORM_ID_JIKI_QC);
            addMenuJikiQC(this.menuListGXHDO101, messageList, queryRunnerXHD, strKojyo, strLotNo, strEdaban, fxhdd03InfoList, menuNameSaisanka, menuNameBarrel);// 権限絞り込みありメニュー
            addMenuJikiQC(this.menuListGXHDO101Nofiltering, messageListNoFiltering, queryRunnerXHD, strKojyo, strLotNo, strEdaban, fxhdd03InfoList, menuNameSaisanka, menuNameBarrel);// 権限絞り込みメニュー

            // メニューに実績Noを設定
            setMenuJisekiNo(this.menuListGXHDO101);// 権限絞り込みありメニュー
            setMenuJisekiNo(this.menuListGXHDO101Nofiltering);// 権限絞り込みメニュー

            // 実績状態を表示(権限絞り込みありメニューのみ)
            setJissekiJotai(this.menuListGXHDO101, queryRunnerDoc, strKojyo, strLotNo, strEdaban);

            // 画面IDチェック(権限絞り込みなしのメニューでチェック)
            checkMenuFormId(messageListNoFiltering, this.menuListGXHDO101Nofiltering);

            // チェックメッセージの表示
            if (!messageListNoFiltering.isEmpty()) {

                messageListNoFiltering.add("　");
                messageListNoFiltering.add("　技術に連絡してください");
                // メッセージを画面に渡す
                InitMessage beanInitMessage = (InitMessage) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_INIT_MESSAGE);
                beanInitMessage.setInitMessageList(messageListNoFiltering);

                RequestContext context = RequestContext.getCurrentInstance();
                context.addCallbackParam("firstParam", "initMessage");
            }

            setMenuTableRender(true);
            // 検索した時点のﾛｯﾄNoと担当者ｺｰﾄﾞを保持する。
            this.searchLotNo = this.lotNo;
            this.searchTantoshaCd = this.tantoshaCd;

        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("メニュー項目未登録", ex, LOGGER);
            menuListGXHDO101 = new ArrayList<>();
        } catch (CloneNotSupportedException ex) {
            ErrUtil.outputErrorLog("実行時エラー", ex, LOGGER);
            menuListGXHDO101 = new ArrayList<>();
        }

        return menuListGXHDO101;
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
            for (FXHDM01 fxhdm01 : this.menuListGXHDO101Nofiltering) {
                if (fxhdm01.getFormId().equals(rowData.getFormId()) && fxhdm01.getJissekiNo() == rowData.getJissekiNo()) {
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
            session.setAttribute("callerFormId", "GXHDO101A");
            session.setAttribute("formTitle", rowData.getFormTitle());
            session.setAttribute("menuName", rowData.getMenuName());
            session.setAttribute("menuComment", rowData.getMenuComment());
            session.setAttribute("titleSetting", rowData.getTitleSetting());
            session.setAttribute("formClassName", rowData.getFormClassName());
            session.setAttribute("hyojiKensu", rowData.getHyojiKensu());
            session.setAttribute("lotNo", this.searchLotNo);
            session.setAttribute("tantoshaCd", this.searchTantoshaCd);
            session.setAttribute("jissekino", rowData.getJissekiNo());
            
            
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

            // ﾊﾞﾚﾙ判定文字列
            session.setAttribute("barrelgohantei", "");
            if (FORM_ID_KENMA_KEISU.equals(rowData.getFormId())) {
                if (maeKoteiMenuInfo != null && FORM_ID_KENMA_BARREL.equals(maeKoteiMenuInfo.getFormId())) {
                    session.setAttribute("barrelgohantei", "ﾊﾞﾚﾙ後計数");
                } else {
                    session.setAttribute("barrelgohantei", "計数のみ");
                }
            }
            
            // 外部電極塗布(三端子,4端子)、外部電極塗布の場合は総重量を取得してセットする
            if (FORM_ID_GAIBUDENKYOKU_TOFU_TANSHI.equals(rowData.getFormId()) || FORM_ID_GAIBUDENKYOKU_TOFU.equals(rowData.getFormId())) {
                session.setAttribute("soujuryou", getSoujuryou(this.menuListGXHDO101Nofiltering));
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
        menuListGXHDO101 = getMenuListGXHDO101();
    }

    /**
     * 一覧表示データを返却します。
     *
     * @return 一覧データ
     */
    public List<FXHDM01> getListData() {
        return this.menuListGXHDO101;
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
     * @param jissekino 実績No(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadFxhdd03RevInfo(QueryRunner queryRunnerDoc, String kojyo, String lotNo,
            String edaban, String formId, int jissekino) throws SQLException {

        // 品質DB登録実績情報の取得
        String sql = "SELECT rev, jotai_flg, edaban "
                + "FROM fxhdd03 "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND gamen_id = ? "
                + "AND jissekino = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(formId);
        params.add(jissekino);

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
        Map fxhdd03Info = getJissekiInfo(queryRunnerDoc, queryRunnerWip, strKojyo, strLotNo, strEdaban, maeKoteiGamenId, maeKoteiJissekiNo);
        String jotaiFlg = StringUtil.nullToBlank(getMapData(fxhdd03Info, "jotai_flg")); //状態フラグ

        // 登録済み実績情報があった場合
        if ("1".equals(jotaiFlg)) {
            return CommonUtil.getMaeKoteiData(queryRunnerQcdb, maeKoteiGamenId, strKojyo, strLotNo,
                    StringUtil.nullToBlank(getMapData(fxhdd03Info, "edaban")), StringUtil.nullToBlank(getMapData(fxhdd03Info, "rev")), maeKoteiJissekiNo);
        }
        return null;
    }

    /**
     * 実績情報の取得
     *
     * @param queryRunnerDoc
     * @param queryRunnerWip
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param gamenId 画面ID
     * @param jissekino 実績No
     * @return
     * @throws SQLException
     */
    private Map getJissekiInfo(QueryRunner queryRunnerDoc, QueryRunner queryRunnerWip, String kojyo, String lotNo, String edaban, String gamenId, int jissekino) throws SQLException {

        // 実績を検索
        Map fxhdd03RevInfo = loadFxhdd03RevInfo(queryRunnerDoc, kojyo, lotNo, edaban, gamenId, jissekino);
        // 状態フラグが空(実績がない)の場合、自身の親の実績を探しに行く
        if (StringUtil.isEmpty(StringUtil.nullToBlank(getMapData(fxhdd03RevInfo, "jotai_flg")))) {

            // 親ﾛｯﾄの枝番
            String oyalotEdaban = getOyaLotEdaban(queryRunnerWip, kojyo, lotNo, edaban);
            if (StringUtil.isEmpty(oyalotEdaban)) {
                // 該当データがない場合はリターン
                return null;
            }

            // 親ﾛｯﾄの実績を検索
            return getJissekiInfo(queryRunnerDoc, queryRunnerWip, kojyo, lotNo, oyalotEdaban, gamenId, jissekino);
        }

        return fxhdd03RevInfo;
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
     * ﾌﾟﾛｾｽﾏｽﾀ、条件ﾃｰﾌﾞﾙをもとに表示対象の画面IDを返します。(フィルターなしの場合はNULLをリターン)
     *
     * @param gamenIdList 画面IDリスト
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param queryRunnerXHD QueryRunnerオブジェクト
     * @param sekkeiNo 設計No
     * @return フィルタリングデータ
     * @throws SQLException 例外エラー
     */
    private List<String> getFilterGamenIdList(QueryRunner queryRunnerDoc, QueryRunner queryRunnerXHD, String sekkeiNo) throws SQLException {

        List<String> filterGamenIdList = new ArrayList<>();

        // プロセスマスターを検索する
        String sqlsearchGamenID = "SELECT gamen_id,kouteimei,koumokumei,kanrikoumoku,kikakuchi FROM fxhdm03 WHERE kotei_process_kubun = ? ";
        List<Object> paramsM03 = new ArrayList<>();
        paramsM03.add("joken_connect");

        DBUtil.outputSQLLog(sqlsearchGamenID, paramsM03.toArray(), LOGGER);
        List listFxhdm03 = (List) queryRunnerDoc.query(sqlsearchGamenID, new MapListHandler(), paramsM03.toArray());

        // データが取得できなかった場合、フィルタリングなし
        if (listFxhdm03.isEmpty()) {
            return filterGamenIdList;
        }

        // 条件ﾃｰﾌﾞﾙデータ取得
        List<DaJoken> listDaJoken = getDaJokenList(queryRunnerXHD, sekkeiNo);

        // データが取得できなかった場合、フィルタリングなし
        if (listDaJoken.isEmpty()) {
            return filterGamenIdList;
        }

        boolean existData;
        for (Iterator i = listFxhdm03.iterator(); i.hasNext();) {
            existData = false;
            HashMap m = (HashMap) i.next();
            for (DaJoken dajoken : listDaJoken) {
                //工程名,項目名,管理項目,規格値が一致する画面IDを表示対象としてリストに追加する。
                if (StringUtil.nullToBlank(dajoken.getKouteiMei()).equals(StringUtil.nullToBlank(m.get("kouteimei")))
                        && StringUtil.nullToBlank(dajoken.getKoumokuMei()).equals(StringUtil.nullToBlank(m.get("koumokumei")))
                        && StringUtil.nullToBlank(dajoken.getKanriKoumoku()).equals(StringUtil.nullToBlank(m.get("kanrikoumoku")))
                        && StringUtil.nullToBlank(dajoken.getKikakuChi()).equals(StringUtil.nullToBlank(m.get("kikakuchi")))) {

                    existData = true;
                    break;
                }
            }
            if (!existData) {
                filterGamenIdList.add(StringUtil.nullToBlank(m.get("gamen_id")));
            }
        }

        return filterGamenIdList;
    }

    /**
     * 条件データ取得
     *
     * @param queryRunnerXHD データオブジェクト
     * @param sekkeiNo 設計No
     * @return 条件データリスト
     * @throws SQLException
     */
    private List<DaJoken> getDaJokenList(QueryRunner queryRunnerXHD, String sekkeiNo) throws SQLException {
        // 条件ﾃｰﾌﾞﾙを検索
        String sql = "SELECT KOUTEIMEI,KOUMOKUMEI,KANRIKOUMOKU,KIKAKUCHI "
                + "  FROM da_joken "
                + " WHERE SEKKEINO = ? ";

        List<Object> params = new ArrayList<>();
        params.add(sekkeiNo);

        Map<String, String> mapping = new HashMap<>();
        mapping.put("KOUTEIMEI", "kouteiMei");
        mapping.put("KOUMOKUMEI", "koumokuMei");
        mapping.put("KANRIKOUMOKU", "kanriKoumoku");
        mapping.put("KIKAKUCHI", "kikakuChi");
        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<DaJoken>> beanHandler = new BeanListHandler<>(DaJoken.class, rowProcessor);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerXHD.query(sql, beanHandler, params.toArray());
    }

    /**
     * 再酸化メニュー 追加処理
     *
     * @param menuListGXHDO101 メニューリスト
     * @param messageList メッセージリスト
     * @param queryRunnerXHD データオブジェクト
     * @param kikakuchi 再酸化回数
     * @param menuNameSaisanka メニュー名(再酸化)
     * @throws SQLException 例外エラー
     * @throws CloneNotSupportedException 例外エラー
     */
    private void addMenuSaisanka(List<FXHDM01> menuListGXHDO101, List<String> messageList, String kikakuchi, String menuNameSaisanka) throws SQLException, CloneNotSupportedException {
        boolean existData = false;
        int saisankaIdx = -1;
        // 再酸化の画面IDが存在するかと、メニュー位置を取得
        for (FXHDM01 fxhdm01 : menuListGXHDO101) {
            saisankaIdx++;
            if (FORM_ID_SAISANKA.equals(fxhdm01.getFormId())) {
                existData = true;
                break;
            }
        }
        // 再酸化の画面IDが存在しない場合
        if (!existData) {
            return;
        }

        // 規格値がnullの場合データなしエラー
        if (kikakuchi == null) {
            messageList.add(MessageUtil.getMessage("XHD-000070"));
            return;
        }

        int saisankaKaisu = 0;
        try {
            saisankaKaisu = Integer.parseInt(kikakuchi);
        } catch (NumberFormatException e) {
            //処理なし
        }

        // 再酸化回数が0以下の場合、エラー
        if (saisankaKaisu <= 0) {
            messageList.add(MessageUtil.getMessage("XHD-000062"));
            return;
        }
        
        // 再酸化回数が1の場合処理なし
        if(saisankaKaisu == 1){
            return;
        }

        // メニュに再酸化回数分のメニューを追加
        for (int i = 0; i < saisankaKaisu; i++) {
            if (0 < i) {
                FXHDM01 addData = menuListGXHDO101.get(saisankaIdx).clone();
                menuListGXHDO101.add(i + saisankaIdx, addData);
            }
            // メニューリストの名称と実績Noを設定
            menuListGXHDO101.get(i + saisankaIdx).setMenuName(menuNameSaisanka + "(" + (i + 1) + "回目)");
        }
    }

    /**
     * 磁器QCメニュー 追加処理
     *
     * @param menuListGXHDO101 メニューリスト
     * @param messageList メッセージリスト
     * @param queryRunnerXHD データオブジェクト
     * @param kojyoCd 工場コード
     * @param lotNo ロットNo
     * @param edaban 枝番
     * @param fxhdd03InfoList 実績情報リスト
     * @param menuNameSaisanka メニュー名(再酸化)
     * @param menuNameBarrel メニュー名(バレル)
     * @throws SQLException 例外エラー
     * @throws CloneNotSupportedException 例外エラー
     */
    private void addMenuJikiQC(List<FXHDM01> menuListGXHDO101, List<String> messageList, QueryRunner queryRunnerXHD,
            String kojyoCd, String lotNo, String edaban, List<String[]> fxhdd03InfoList, String menuNameSaisanka, String menuNameBarrel) throws SQLException, CloneNotSupportedException {
        boolean existData = false;
        int addIdx = -1;
        // 磁器QCの画面IDが存在するかまたその位置(メニューの追加位置を保持)を取得
        for (FXHDM01 fxhdm01 : menuListGXHDO101) {
            addIdx++;
            if (FORM_ID_JIKI_QC.equals(fxhdm01.getFormId())) {
                existData = true;
                break;
            }
        }
        // 磁器QCの画面IDが存在しない場合、リターン
        if (!existData) {
            return;
        }

        // 追加時のコピー元となるメニュー情報を取得
        FXHDM01 fxhdm01Saisanka = null; //焼成・再酸化
        FXHDM01 fxhdm01Barrel = null;   //研磨・ﾊﾞﾚﾙ
        FXHDM01 fxhdm01Keisu = null;    //研磨・計数
        FXHDM01 fxhdm01JikiQC = null;   //磁器QC
        for (FXHDM01 fxhdm01 : menuListGXHDO101) {
            switch (fxhdm01.getFormId()) {
                case FORM_ID_SAISANKA:
                    fxhdm01Saisanka = fxhdm01;
                    break;
                case FORM_ID_KENMA_BARREL:
                    fxhdm01Barrel = fxhdm01;
                    break;
                case FORM_ID_KENMA_KEISU:
                    fxhdm01Keisu = fxhdm01;
                    break;
                case FORM_ID_JIKI_QC:
                    fxhdm01JikiQC = fxhdm01;
                    break;
            }

            // 全ての値がセットされたらbreak
            if (fxhdm01Saisanka != null && fxhdm01Barrel != null && fxhdm01Keisu != null && fxhdm01JikiQC != null) {
                break;
            }
        }

        // 各画面IDの追加回数を保持する。
        Map addCountMap = new HashMap();

        for (String[] fxhdd03Info : fxhdd03InfoList) {
            // 状態ﾌﾗｸﾞが"1"(登録済)以外の場合は処理なし
            if (!"1".equals(fxhdd03Info[0])) {
                // 実績が無ければ処理を抜ける
                return;
            }

            // 磁器QCより合否判定を取得
            String gouhiHantei = getSrJikiqcGouhiHantei(queryRunnerXHD, kojyoCd, lotNo, edaban, fxhdd03Info[1], fxhdd03Info[2]);
            if (StringUtil.isEmpty(gouhiHantei)) {
                // 実績が取得できなかった場合リターン
                messageList.add(MessageUtil.getMessage("XHD-000063"));
                return;
            }

            // 合否判定をもとにメニューを追加
            switch (gouhiHantei) {
                case "再酸化":
                    if (!addMenu(menuListGXHDO101, addCountMap, ++addIdx, fxhdm01Saisanka, menuNameSaisanka)) {
                        addIdx--; //メニューが追加できない場合はデクリメント
                    }
                    if (!addMenu(menuListGXHDO101, addCountMap, ++addIdx, fxhdm01Keisu, "")) {
                        addIdx--; //メニューが追加できない場合はデクリメント
                    }
                    if (!addMenu(menuListGXHDO101, addCountMap, ++addIdx, fxhdm01JikiQC, "")) {
                        addIdx--; //メニューが追加できない場合はデクリメント
                    }
                    break;
                case "再ﾊﾞﾚﾙ":
                    if (!addMenu(menuListGXHDO101, addCountMap, ++addIdx, fxhdm01Barrel, menuNameBarrel)) {
                        addIdx--; //メニューが追加できない場合はデクリメント
                    }
                    if (!addMenu(menuListGXHDO101, addCountMap, ++addIdx, fxhdm01Keisu, "")) {
                        addIdx--; //メニューが追加できない場合はデクリメント
                    }
                    if (!addMenu(menuListGXHDO101, addCountMap, ++addIdx, fxhdm01JikiQC, "")) {
                        addIdx--; //メニューが追加できない場合はデクリメント
                    }
                    break;
                case "MV":
                    if (!addMenu(menuListGXHDO101, addCountMap, ++addIdx, fxhdm01JikiQC, "")) {
                        addIdx--; //メニューが追加できない場合はデクリメント
                    }
                    break;
            }
        }

    }

    /**
     * メニュー追加処理
     *
     * @param menuListGXHDO101 メニューリスト
     * @param jissekiNoMap 実績Noマップ
     * @return メニューを追加出来た場合true
     * @throws CloneNotSupportedException
     */
    private boolean addMenu(List<FXHDM01> menuListGXHDO101, Map addCountMap, int addIdx, FXHDM01 fxhdm01, String setName) throws CloneNotSupportedException {
        if (fxhdm01 == null) {
            return false;
        }
        menuListGXHDO101.add(addIdx, fxhdm01.clone());
        if (!StringUtil.isEmpty(setName)) {
            int addCount = 0;
            if (addCountMap.containsKey(fxhdm01.getFormId())) {
                addCount = (int) addCountMap.get(fxhdm01.getFormId());
            }
            addCount++;
            addCountMap.put(fxhdm01.getFormId(), addCount);
            menuListGXHDO101.get(addIdx).setMenuName(setName + "(追加" + addCount + "回目)");
        }

        return true;
    }

    /**
     * 条件テーブルより規格値(再酸化情報)※レコードなしの場合はNULLを返却
     *
     * @param queryRunnerXHD データオブジェクト
     * @param sekkeiNo 設計No
     * @return 規格値
     * @throws SQLException
     */
    private String getSaisankaKikakuchi(QueryRunner queryRunnerXHD, String sekkeiNo) throws SQLException {

        // 条件ﾃｰﾌﾞﾙを検索
        String sql = "SELECT KIKAKUCHI "
                + "  FROM da_joken "
                + " WHERE SEKKEINO = ? "
                + "   AND KOUTEIMEI = '酸化処理' "
                + "   AND KOUMOKUMEI = '再酸化' "
                + "   AND KANRIKOUMOKU = '回数' ";

        List<Object> params = new ArrayList<>();
        params.add(sekkeiNo);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        Map resultMap = queryRunnerXHD.query(sql, new MapHandler(), params.toArray());
        if (resultMap == null) {
            return null;
        }

        return StringUtil.nullToBlank(resultMap.get("KIKAKUCHI"));
    }

    /**
     * [品質DB登録実績]から、状態ﾌﾗｸﾞ,実績No,ﾘﾋﾞｼﾞｮﾝ,を取得
     *
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param formId 画面ID(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<String[]> loadFxhdd03InfoList(QueryRunner queryRunnerDoc, String kojyo, String lotNo,
            String edaban, String formId) throws SQLException {

        // 品質DB登録実績情報の取得
        String sql = "SELECT jotai_flg, jissekino, rev "
                + "FROM fxhdd03 "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND gamen_id = ? "
                + "ORDER BY jissekino ASC ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(formId);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        List processResult = (List) queryRunnerDoc.query(sql, new MapListHandler(), params.toArray());

        List<String[]> fxhdd03InfoList = new ArrayList<>();
        for (Iterator i = processResult.iterator(); i.hasNext();) {
            HashMap m = (HashMap) i.next();
            fxhdd03InfoList.add(new String[]{m.get("jotai_flg").toString(), m.get("jissekino").toString(), m.get("rev").toString()});

        }

        return fxhdd03InfoList;
    }

    /**
     * [磁器QC]から、ﾃﾞｰﾀ(合否判定)を取得(値が無い場合はNULLを返却)
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param jissekino 実績No(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private String getSrJikiqcGouhiHantei(QueryRunner queryRunnerXHD, String kojyo, String lotNo,
            String edaban, String jissekino, String rev) throws SQLException {

        String sql = "SELECT gouhihantei "
                + "FROM sr_jikiqc "
                + "WHERE KOJYO = ? AND LOTNO = ? AND EDABAN = ? AND JISSEKINO = ? AND revision = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);
        params.add(rev);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        Map resultMap = queryRunnerXHD.query(sql, new MapHandler(), params.toArray());
        if (resultMap == null) {
            return null;
        }

        return StringUtil.nullToBlank(resultMap.get("gouhihantei"));
    }

    /**
     * 画面ID別の実績No取得
     *
     * @param gamenId 画面ID
     * @param gamenIdList 画面IDリスト
     * @return 実績No
     */
    private int getJissekino(Map mapJissekiNo, List<FXHDM01> menuListGXHDO101, String gamenId) {
        if ("GXHDO101B015".equals(gamenId)) {
            // 焼成・窒素脱脂の場合
            for (FXHDM01 fxhdm01 : menuListGXHDO101) {
                if ("GXHDO101B014".equals(fxhdm01.getFormId())) {
                    return 2;
                }
            }
            return 1;
        } else if ("GXHDO101B014".equals(gamenId)) {
            // 焼成・Air脱脂の場合
            return 1;
        }

        int jissekiNo = 1;
        if (mapJissekiNo.containsKey(gamenId)) {
            jissekiNo = (int) mapJissekiNo.get(gamenId) + 1;
        }

        mapJissekiNo.put(gamenId, jissekiNo);

        return jissekiNo;
    }

    /**
     * 実績Noの設定処理
     *
     * @param menuListGXHDO101 メニュー一覧
     */
    private void setMenuJisekiNo(List<FXHDM01> menuListGXHDO101) {
        Map mapJissekiNo = new HashMap<>();
        for (FXHDM01 fxhdm01 : menuListGXHDO101) {
            fxhdm01.setJissekiNo(getJissekino(mapJissekiNo, menuListGXHDO101, fxhdm01.getFormId()));
        }
    }

    /**
     * 実績状態の設定を行う。
     *
     * @param menuListGXHDO101 メニューリスト
     * @param queryRunnerDoc データオブジェクト
     * @param kojyo 工場コード
     * @param lotNo ロットNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void setJissekiJotai(List<FXHDM01> menuListGXHDO101, QueryRunner queryRunnerDoc, String kojyo, String lotNo,
            String edaban) throws SQLException {
        List<String[]> fxhdd03List = loadFxhdd03LotNoTaniInfoList(queryRunnerDoc, kojyo, lotNo, edaban);

        boolean existJiseki;
        for (FXHDM01 fxhdm01 : menuListGXHDO101) {
            existJiseki = false;
            for (String[] fxhdd03Info : fxhdd03List) {
                if (fxhdm01.getFormId().equals(fxhdd03Info[0]) && StringUtil.nullToBlank(fxhdm01.getJissekiNo()).equals(fxhdd03Info[1])) {
                    switch (fxhdd03Info[2]) {
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
                fxhdm01.setMenuComment("未登録");
            }
        }
    }

    /**
     * [品質DB登録実績]から、ﾛｯﾄNo単位で状態ﾌﾗｸﾞ,実績No,ﾘﾋﾞｼﾞｮﾝ,を取得
     *
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<String[]> loadFxhdd03LotNoTaniInfoList(QueryRunner queryRunnerDoc, String kojyo, String lotNo,
            String edaban) throws SQLException {

        // 品質DB登録実績情報の取得
        String sql = "SELECT gamen_id, jissekino, jotai_flg "
                + "FROM fxhdd03 "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        List processResult = (List) queryRunnerDoc.query(sql, new MapListHandler(), params.toArray());

        List<String[]> fxhdd03InfoList = new ArrayList<>();
        for (Iterator i = processResult.iterator(); i.hasNext();) {
            HashMap m = (HashMap) i.next();
            fxhdd03InfoList.add(new String[]{m.get("gamen_id").toString(), m.get("jissekino").toString(), m.get("jotai_flg").toString()});
        }

        return fxhdd03InfoList;
    }

    /**
     * 画面IDチェック
     *
     * @param messageList メッセージリスト
     * @param menuList メニューリスト
     * @throws SQLException
     */
    private void checkMenuFormId(List<String> messageList, List<FXHDM01> menuList) throws SQLException {

        // 印刷画面IDチェック
        if (!existFormIds(menuList, "GXHDO101B001", "GXHDO101B002", "GXHDO101B003", "GXHDO101B006", "GXHDO101B023")) {
            messageList.add(MessageUtil.getMessage("XHD-000061", "印刷"));
        }
        // 積層画面IDチェック
        if (!existFormIds(menuList, "GXHDO101B004", "GXHDO101B005", "GXHDO101B006")) {
            messageList.add(MessageUtil.getMessage("XHD-000061", "積層"));
        }
        // ﾌﾟﾚｽ画面IDチェック
        if (!existFormIds(menuList, "GXHDO101B007", "GXHDO101B008", "GXHDO101B009")) {
            messageList.add(MessageUtil.getMessage("XHD-000061", "ﾌﾟﾚｽ"));
        }
        // ｶｯﾄ画面IDチェック
        if (!existFormIds(menuList, "GXHDO101B010", "GXHDO101B011")) {
            messageList.add(MessageUtil.getMessage("XHD-000061", "ｶｯﾄ"));
        }
        // 生品質検査チェック
        if (!existFormIds(menuList, "GXHDO101B012")) {
            messageList.add(MessageUtil.getMessage("XHD-000061", "生品質検査"));
        }
        // ｾｯﾀ詰めチェック
        if (!existFormIds(menuList, "GXHDO101B013")) {
            messageList.add(MessageUtil.getMessage("XHD-000061", "ｾｯﾀ詰め"));
        }
        // 脱脂チェック
        if (!existFormIds(menuList, "GXHDO101B014", "GXHDO101B015")) {
            messageList.add(MessageUtil.getMessage("XHD-000061", "脱脂"));
        }
        // 焼成チェック
        if (!existFormIds(menuList, "GXHDO101B017", "GXHDO101B018")) {
            messageList.add(MessageUtil.getMessage("XHD-000061", "焼成"));
        }
        // ﾊﾞﾚﾙチェック
        if (!existFormIds(menuList, "GXHDO101B020")) {
            messageList.add(MessageUtil.getMessage("XHD-000061", "ﾊﾞﾚﾙ"));
        }
        // 計数チェック
        if (!existFormIds(menuList, "GXHDO101B021")) {
            messageList.add(MessageUtil.getMessage("XHD-000061", "計数"));
        }
        // 磁器QCチェック
        if (!existFormIds(menuList, "GXHDO101B022")) {
            messageList.add(MessageUtil.getMessage("XHD-000061", "磁器QC"));
        }
    }

/**
     * 画面IDがメニュー存在しているか確認(1項目でも存在すればtrue)
     *
     * @param menuList
     * @param formIds
     * @return
     */
    private boolean existFormIds(List<FXHDM01> menuList, String... formIds) {
        for (String formId : formIds) {
            for (FXHDM01 gamenInfo : menuList) {
                if (formId.equals(gamenInfo.getFormId())) {
                    return true;
                }
            }
        }

        return false;
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
        String sql = "SELECT gamen_id,gamen_title,title_setting,menu_no,menu_name,link_char,menu_parameter,menu_comment,gamen_classname,hyouji_kensu "
                + "FROM fxhdm01 WHERE menu_group_id = 'QCDB' ";

        if (!listGamenID.isEmpty()) {
            sql += " AND ";
            sql += DBUtil.getInConditionPreparedStatement("gamen_id", listGamenID.size());
        }
        sql += " AND " + DBUtil.getInConditionPreparedStatement("user_role", userGrpList.size());

        if (!isPC) {
            sql += " AND pc_flg = '0'";
        }

        sql += " ORDER BY menu_no ASC ";

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

        List<Object> params3 = new ArrayList<>();
        params3.addAll(listGamenID);
        params3.addAll(userGrpList);

        if (listGamenID.isEmpty()) {
            DBUtil.outputSQLLog(sql, userGrpList.toArray(), LOGGER);
            return queryRunnerDoc.query(sql, beanHandler, userGrpList.toArray());
        } else {
            DBUtil.outputSQLLog(sql, params3.toArray(), LOGGER);
            return queryRunnerDoc.query(sql, beanHandler, params3.toArray());
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
        String sql = "SELECT gamen_id,menu_name "
                + "FROM fxhdm01 WHERE menu_group_id = 'QCDB' ";

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

        List<Object> params3 = new ArrayList<>();
        params3.addAll(listGamenID);

        DBUtil.outputSQLLog(sql, params3.toArray(), LOGGER);
        return queryRunnerDoc.query(sql, beanHandler, params3.toArray());

    }

    /**
     * メニューリストフィルタリング処理
     *
     * @param menuList メニューリスト
     * @param filterGamenIdList フィルタリングID
     */
    private void menuListFiltering(List<FXHDM01> menuList, List<String> filterGamenIdList) {
        if (!menuList.isEmpty()) {

            // データが取得できた場合、フィルターしたデータのみ表示
            List<FXHDM01> removeMenuList = new ArrayList();
            for (FXHDM01 data : menuList) {
                if (filterGamenIdList.contains(data.getFormId())) {
                    removeMenuList.add(data);
                }
            }
            for (FXHDM01 removeData : removeMenuList) {
                menuList.remove(removeData);
            }
        }
    }
    
    
    /**
     * 総重量の取得処理(研磨計測より総重量を取得する。)
     * @param menuListAll メニューデータ(権限でのフィルタリング無し)
     * @return 総重量
     */
    private BigDecimal getSoujuryou(List<FXHDM01> menuListAll) throws SQLException {

        // メニューに研磨(計数)が存在しない場合、0を返却
        if (0 == menuListAll.stream().filter(fxhdm01 -> FORM_ID_KENMA_KEISU.equals(fxhdm01.getFormId())).count()) {
            return BigDecimal.ZERO;
        }
        String strKojyo = this.searchLotNo.substring(0, 3);
        String strLotNo = this.searchLotNo.substring(3, 11);
        String strEdaban = this.searchLotNo.substring(11, 14);
        QueryRunner queryRunnerDoc = new QueryRunner(dataSourceDocServer);
        QueryRunner queryRunnerXHD = new QueryRunner(dataSourceXHD);

        // データが存在しないまたは状態フラグが"1"以外の場合、0を返却
        List<String[]> fxhdd03List = loadFxhdd03InfoListJisekiNoDesc(queryRunnerDoc, strKojyo, strLotNo, strEdaban, FORM_ID_KENMA_KEISU);
        if (fxhdd03List.isEmpty() || !"1".equals(fxhdd03List.get(0)[0])) {
            return BigDecimal.ZERO;
        }

        return getSrSyoseikeisuuSojuryo(queryRunnerXHD, strKojyo, strLotNo, strEdaban, fxhdd03List.get(0)[1], fxhdd03List.get(0)[2]);
    }
    
     /**
     * [品質DB登録実績]から、状態ﾌﾗｸﾞ,実績No,ﾘﾋﾞｼﾞｮﾝを取得
     *
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param formId 画面ID(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<String[]> loadFxhdd03InfoListJisekiNoDesc(QueryRunner queryRunnerDoc, String kojyo, String lotNo,
            String edaban, String formId) throws SQLException {

        // 品質DB登録実績情報の取得
        String sql = "SELECT jotai_flg, jissekino, rev "
                + "FROM fxhdd03 "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND gamen_id = ? "
                + "ORDER BY jissekino DESC ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(formId);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        List processResult = (List) queryRunnerDoc.query(sql, new MapListHandler(), params.toArray());

        List<String[]> fxhdd03InfoList = new ArrayList<>();
        for (Iterator i = processResult.iterator(); i.hasNext();) {
            HashMap m = (HashMap) i.next();
            fxhdd03InfoList.add(new String[]{m.get("jotai_flg").toString(), m.get("jissekino").toString(), m.get("rev").toString()});

        }

        return fxhdd03InfoList;
    }
    
    /**
     * [計数]から、総重量を取得(値が無い場合は0を返却)
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param jissekino 実績No(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private BigDecimal getSrSyoseikeisuuSojuryo(QueryRunner queryRunnerXHD, String kojyo, String lotNo,
            String edaban, String jissekino, String rev) throws SQLException {

        String sql = "SELECT soujuryou "
                + "FROM sr_syoseikeisuu "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND jissekino = ? AND revision = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);
        params.add(rev);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        Map resultMap = queryRunnerXHD.query(sql, new MapHandler(), params.toArray());
        BigDecimal soujuryou = BigDecimal.ZERO;
        if (resultMap != null && !StringUtil.isEmpty(StringUtil.nullToBlank(resultMap.get("soujuryou")))) {
            soujuryou = new BigDecimal(StringUtil.nullToBlank(resultMap.get("soujuryou")));
        }

        return soujuryou;
    }
}
