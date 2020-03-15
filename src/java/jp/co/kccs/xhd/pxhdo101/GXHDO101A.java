/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
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
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import jp.co.kccs.xhd.GetModel;
import jp.co.kccs.xhd.SelectParam;
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
import org.apache.commons.dbutils.DbUtils;
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
 * 変更日	2019/11/04<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	K.Hisanaga<br>
 * 変更理由	各種機能メニュー追加処理<br>
 * <br>
 * 変更日	2019/12/22<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	K.Hisanaga<br>
 * 変更理由	電気特性パラメータ設定処理追加<br>
 * <br>
 * 変更日	2020/02/17<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	K.Hisanaga<br>
 * 変更理由	熱処理情報と磁器QC情報の受渡し追加<br>
 * <br>
 * 変更日	2020/03/02<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	K.Hisanaga<br>
 * 変更理由	メニューの追加削除機能を実装<br>
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
    private static final String FORM_ID_DP2 = "GXHDO101B023";
    private static final String FORM_ID_GAIBUDENKYOKU_TOFU_TANSHI = "GXHDO101B025";
    private static final String FORM_ID_GAIBUDENKYOKU_TOFU = "GXHDO101B026";
    private static final String FORM_ID_GAIBUDENKYOKU_SYOSEI = "GXHDO101B029";
    private static final String FORM_ID_GAIBUDENKYOKU_SYOSEIGAIKAN = "GXHDO101B030";
    private static final String FORM_ID_GAIBUDENKYOKU_MEKKI_HINSHITSU_KENSA = "GXHDO101B038";
    private static final String FORM_ID_GAIBUDENKYOKU_MEKKI_SHINKU_KANSO = "GXHDO101B039";
    private static final String FORM_ID_DENKITOKUSEI_ESI = "GXHDO101B040";
    private static final String FORM_ID_DENKITOKUSEI_3TANSHI_4TANSHI = "GXHDO101B041";
    private static final String FORM_ID_DENKITOKUSEI_IPPANHIN = "GXHDO101B042";
    private static final String FORM_ID_DENKITOKUSEI_NETSUSHORI = "GXHDO101B043";
    private static final String FORM_ID_GAIKAN_KENSA = "GXHDO101B046";
    private static final String FORM_ID_TP_SAGYO = "GXHDO101B048";
    private static final String FORM_ID_TP_CHECK = "GXHDO101B049";

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
     * パラメータマスタ操作
     */
    @Inject
    private SelectParam selectParam;

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
     * ロット参照表示render有無
     */
    private boolean sanshouBtnRender;

    /**
     * 総合判定表示render有無
     */
    private boolean sougouHanteiBtnRender;

    /**
     * 総合判定表示render有無
     */
    private boolean addLinkRender;

    /**
     * 印刷工程画面ID
     */
    private String insatsuKoteiGamenID;

    /**
     * 参照元ﾃﾞｰﾀ
     */
    private FXHDM01 sanshouMotoInfo = null;

    /**
     * 参照元ﾃﾞｰﾀ
     */
    private FXHDM01 deleteMenuInfo = null;

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
            getMenuListGXHDO101(this.lotNo, this.tantoshaCd, false);
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
    public List<FXHDM01> getMenuListGXHDO101(String strGamenLotNo, String gamenTantoshaCd, boolean onInitCheck) {
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
            setSanshouBtnRender(false);
            setSougouHanteiBtnRender(false);

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

            // メッセージリスト
            List<String> messageListDummy = new ArrayList<>(); // 権限絞り込みありメニューの処理で使用するリスト(引数として渡すだけのものとして使用)
            List<String> messageListMain = new ArrayList<>();  // 権限絞り込みなしメニューの処理で使用するリスト(メイン)

            // 繰り返しメニューは事前にメニューの名称を保持しておく
            String menuNameSaisanka = this.menuListGXHDO101Nofiltering.stream().filter(n -> FORM_ID_SAISANKA.equals(n.getFormId())).findFirst().map(f -> f.getMenuName()).orElse("");
            String menuNameBarrel = this.menuListGXHDO101Nofiltering.stream().filter(n -> FORM_ID_KENMA_BARREL.equals(n.getFormId())).findFirst().map(f -> f.getMenuName()).orElse("");
            String menuNameDp2 = this.menuListGXHDO101Nofiltering.stream().filter(n -> FORM_ID_DP2.equals(n.getFormId())).findFirst().map(f -> f.getMenuName()).orElse("");
            String menuNameGaibuDenkyokuSyosei = this.menuListGXHDO101Nofiltering.stream().filter(n -> FORM_ID_GAIBUDENKYOKU_SYOSEI.equals(n.getFormId())).findFirst().map(f -> f.getMenuName()).orElse("");
            String menuNameGaibuDenkyokuTofu = this.menuListGXHDO101Nofiltering.stream().filter(n -> FORM_ID_GAIBUDENKYOKU_TOFU.equals(n.getFormId())).findFirst().map(f -> f.getMenuName()).orElse("");
            String menuNameGaibuDenkyokuMekki = this.menuListGXHDO101Nofiltering.stream().filter(n -> FORM_ID_GAIBUDENKYOKU_MEKKI_HINSHITSU_KENSA.equals(n.getFormId())).findFirst().map(f -> f.getMenuName()).orElse("");

            // 該当のメニューIDが存在するか確認する。
            boolean insertUseFlg = existFormIds(this.menuListGXHDO101,
                    new String[]{FORM_ID_DENKITOKUSEI_ESI, FORM_ID_DENKITOKUSEI_3TANSHI_4TANSHI, FORM_ID_DENKITOKUSEI_IPPANHIN,
                        FORM_ID_DENKITOKUSEI_NETSUSHORI, FORM_ID_GAIKAN_KENSA, FORM_ID_TP_SAGYO, FORM_ID_TP_CHECK});

            //ﾛｯﾄ参照ﾎﾞﾀﾝ表示/非表示の設定
            if (insertUseFlg && userGrpList.contains("画面制御_利用ﾕｰｻﾞｰ")) {
                setAddLinkRender(true);
            } else {
                setAddLinkRender(false);
            }

            //画面ID(メニュー)追加(再酸化)
            addMenuSaisanka(strSekkeiNo, this.menuListGXHDO101, this.menuListGXHDO101Nofiltering, messageListMain, menuNameSaisanka, queryRunnerXHD, session);

            //画面ID(メニュー)追加(磁器QC)
            // 実績情報(状態ﾌﾗｸﾞ、実績No、Revを取得)※磁器QCの画面IDが存在する場合のみ使用するが「権限絞り込みあり・なし」で2回呼び出される可能性があるため事前に取得
            List<String[]> fxhdd03InfoList = loadFxhdd03InfoList(queryRunnerDoc, strKojyo, strLotNo, strEdaban, FORM_ID_JIKI_QC);
            addMenuJikiQC(this.menuListGXHDO101, messageListDummy, queryRunnerXHD, strKojyo, strLotNo, strEdaban, fxhdd03InfoList, menuNameSaisanka, menuNameBarrel);// 権限絞り込みありメニュー
            addMenuJikiQC(this.menuListGXHDO101Nofiltering, messageListMain, queryRunnerXHD, strKojyo, strLotNo, strEdaban, fxhdd03InfoList, menuNameSaisanka, menuNameBarrel);// 権限絞り込みメニュー

            //画面ID(メニュー)追加(DP2)
            addMenuDp2(strSekkeiNo, this.menuListGXHDO101, this.menuListGXHDO101Nofiltering, messageListMain, menuNameDp2, queryRunnerXHD, session);

            //画面ID(メニュー)追加(外部電極焼成(焼成))
            addMenuGaibuDenkyokuSyosei(strSekkeiNo, this.menuListGXHDO101, this.menuListGXHDO101Nofiltering, messageListMain, menuNameGaibuDenkyokuSyosei, queryRunnerXHD, session);

            //画面ID(メニュー)追加(外部電極塗布)
            addMenuGaibuDenkyokuTofu(this.menuListGXHDO101, this.menuListGXHDO101Nofiltering, messageListMain, menuNameGaibuDenkyokuTofu, queryRunnerXHD, queryRunnerDoc, session);

            //画面ID(メニュー)追加(外部電極・ﾒｯｷ品質検査)
            addMenuGaibuDenkyokuMekkiHinshitsuKensa(this.menuListGXHDO101, this.menuListGXHDO101Nofiltering, messageListMain, menuNameGaibuDenkyokuMekki, queryRunnerXHD, queryRunnerDoc);

            //画面ID(動的メニュー)追加(外観検査、電気特性、TPチェック)
            addRecurringMenu(this.menuListGXHDO101, messageListDummy, strKojyo, strLotNo, strEdaban, queryRunnerXHD, queryRunnerDoc);
            addRecurringMenu(this.menuListGXHDO101Nofiltering, messageListMain, strKojyo, strLotNo, strEdaban, queryRunnerXHD, queryRunnerDoc);

            // 実績状態を表示(権限絞り込みありメニューのみ)
            List<String[]> fxhdd03List = loadFxhdd03LotNoTaniInfoList(queryRunnerDoc, strKojyo, strLotNo, strEdaban);
            setJissekiJotai(this.menuListGXHDO101, fxhdd03List);
            setJissekiJotai(this.menuListGXHDO101Nofiltering, fxhdd03List);

            // 画面IDチェック(権限絞り込みなしのメニューでチェック)
            checkMenuFormId(messageListMain, this.menuListGXHDO101Nofiltering);

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

            //⑭ﾛｯﾄ参照ﾎﾞﾀﾝ表示/非表示
            //  以下の2つの条件の両方を満たす場合、【ﾛｯﾄ参照】ﾎﾞﾀﾝを画面に表示する。また、【印刷工程画面ID】を決定する。
            //  1.印刷工程の画面IDが以下のいずれかの画面IDであること。(【画面IDﾘｽﾄ】に以下の画面IDが存在すること)
            // 画面IDﾌｨﾙﾀﾘﾝｸﾞ
            List<String> filterGamenList = new ArrayList<>();
            filterGamenList.add("GXHDO101B001");
            filterGamenList.add("GXHDO101B002");
            filterGamenList.add("GXHDO101B006");
            boolean gamenExistFlg = false;
            gamenExistFlg = lotSanshouMenuListFiltering(this.menuListGXHDO101, filterGamenList);

            //ﾛｯﾄ参照ﾎﾞﾀﾝ表示/非表示の設定
            if (gamenExistFlg && userGrpList.contains("ﾛｯﾄ参照_利用ﾕｰｻﾞｰ")) {
                setSanshouBtnRender(true);
            } else {
                setSanshouBtnRender(false);
            }
            //総合判定ﾎﾞﾀﾝ表示
            setSougouHanteiBtnRender(true);

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
     * 参照元画面を開き
     *
     * @param isFormError エラー判定
     * @return 遷移先画面
     */
    public String openNextXhdForm(boolean isFormError) {
        String nextPageUrl = "";
        if (!isFormError) {
            nextPageUrl = openXhdForm(sanshouMotoInfo);
            GXHDO101C012 beanGXHDO101C012 = (GXHDO101C012) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C012);
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpSession session = (HttpSession) externalContext.getSession(false);
            session.setAttribute("maeGamenID", "GXHDO101C012");
            session.setAttribute("lotNo", beanGXHDO101C012.getSanshouSakiLotNo());
            session.setAttribute("sanshouMotoLotNo", this.searchLotNo);
        }
        return nextPageUrl;
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

            // 電気特性画面用の情報をｾｯｼｮﾝにセットする。
            setDenkitokuseiSessionData(rowData.getFormId(), session);

            // 検査・外観検査用の情報をｾｯｼｮﾝにセットする。
            setGaikanKensaSessionData(rowData.getFormId(), maeKoteiMenuInfo, rowData, session);

            // 前工程が存在するかつ前工程のデータが取得できなかった場合
            if (maeKoteiMenuInfo != null && maekoteiInfo == null) {
                this.warnMessage = MessageUtil.getMessage("XHD-000035", maeKoteiMenuInfo.getMenuName());
                this.linkPage = rowData.getLinkChar() + "?faces-redirect=true";
                RequestContext context = RequestContext.getCurrentInstance();
                context.addCallbackParam("firstParam", "warning");
                return "";
            }

            //ﾛｯﾄ参照ﾎﾞﾀﾝ非表示にする
            setSanshouBtnRender(false);

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
        getMenuListGXHDO101(this.lotNo, this.tantoshaCd, true);
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
        menuListGXHDO101 = new ArrayList<>();
        getMenuListGXHDO101(this.searchLotNo, this.searchTantoshaCd, false);
    }

    /**
     * 品質DB画面ﾛｯﾄ参照ボタン押下時
     */
    public void getLotSanshou() {

        try {
            QueryRunner queryRunnerDoc = new QueryRunner(dataSourceDocServer);
            QueryRunner queryRunnerQcdb = new QueryRunner(dataSourceXHD);
            FacesContext facesContext = FacesContext.getCurrentInstance();

            String strKojyo = "";
            String strLotNo = "";
            String strEdaban = "";

            if (!"".equals(searchLotNo) && searchLotNo != null) {
                strKojyo = this.searchLotNo.substring(0, 3);
                strLotNo = this.searchLotNo.substring(3, 11);
                strEdaban = this.searchLotNo.substring(11, 14);
            }

            //■ﾛｯﾄ参照ﾎﾞﾀﾝ 押下時
            // (1)ﾛｯﾄ状態ﾁｪｯｸ
            //  参照元のﾛｯﾄは登録済であること。また、参照先のﾛｯﾄは未登録であることを確認する。
            //  ①【印刷工程画面ID】をもとに、Ⅲ.画面表示仕様(26)を発行する。
            //   1.取得できなかった場合
            //    ｴﾗｰﾒｯｾｰｼﾞを表示し、処理を中断する。
            //      ・ｴﾗｰｺｰﾄﾞ:XHD-000160
            //      ・ｴﾗｰﾒｯｾｰｼﾞ:参照元のﾛｯﾄ({0})が登録済ではありません。
            //      0には、【印刷工程画面ID】をもとにした、ﾒﾆｭｰ名(ﾌｫｰﾑﾊﾟﾗﾒｰﾀ)を設定。
            Map fxhdd03JissekinoInfo = loadFxhdd03JissekinoInfo(queryRunnerDoc, strKojyo, strLotNo, strEdaban, getInsatsuKoteiGamenID());
            if (fxhdd03JissekinoInfo == null) {
                setErrorMessage(MessageUtil.getMessage("XHD-000160", getMenuName(this.menuListGXHDO101, getInsatsuKoteiGamenID())));
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, getErrorMessage(), null);
                facesContext.addMessage(null, message);
                return;
            }

            //   2.取得できた場合、登録済であるか確認を行う。
            //    A.状態ﾌﾗｸﾞ == '0' の場合
            //     ｴﾗｰﾒｯｾｰｼﾞを表示し、処理を中断する。
            //       ・ｴﾗｰｺｰﾄﾞ:XHD-000160
            //       ・ｴﾗｰﾒｯｾｰｼﾞ:参照元のﾛｯﾄ({0})が登録済ではありません。
            //       0には、【印刷工程画面ID】をもとにした、ﾒﾆｭｰ名(ﾌｫｰﾑﾊﾟﾗﾒｰﾀ)を設定。
            //    B.状態ﾌﾗｸﾞ == '1' の場合
            //     以降の処理を実行する。
            //    C.上記以外の場合
            //     ｴﾗｰﾒｯｾｰｼﾞを表示し、処理を中断する。
            //       ・ｴﾗｰｺｰﾄﾞ:XHD-000160
            //       ・ｴﾗｰﾒｯｾｰｼﾞ:参照元のﾛｯﾄ({0})が登録済ではありません。
            //       0には、【印刷工程画面ID】をもとにした、ﾒﾆｭｰ名(ﾌｫｰﾑﾊﾟﾗﾒｰﾀ)を設定。
            String jotaiFlg = StringUtil.nullToBlank(getMapData(fxhdd03JissekinoInfo, "jotai_flg"));
            if (!"1".equals(jotaiFlg)) {
                setErrorMessage(MessageUtil.getMessage("XHD-000160", getMenuName(this.menuListGXHDO101, getInsatsuKoteiGamenID())));
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, getErrorMessage(), null);
                facesContext.addMessage(null, message);
                return;
            }

            //  ②【印刷工程画面ID】をもとに、対象ﾃｰﾌﾞﾙからﾃﾞｰﾀを取得する。
            //   1.【印刷工程画面ID】 == "GXHDO101B001" の場合
            String jissekiInforev = StringUtil.nullToBlank(getMapData(fxhdd03JissekinoInfo, "rev"));
            if ("GXHDO101B001".equals(getInsatsuKoteiGamenID())) {
                //    A.Ⅲ.画面表示仕様(27),(28)を発行する。
                Map srSpsprintGraInfo = loadSrSpsprintGraInfo(queryRunnerQcdb, strKojyo, strLotNo, strEdaban, jissekiInforev);
                Map subSrSpsprintGraInfo = loadSubSrSpsprintGraInfo(queryRunnerQcdb, strKojyo, strLotNo, strEdaban, jissekiInforev);
                if (srSpsprintGraInfo == null || subSrSpsprintGraInfo == null) {
                    //     ｱ.取得できなかった場合
                    //      ｴﾗｰﾒｯｾｰｼﾞを表示し、処理を中断する。
                    //        ・ｴﾗｰｺｰﾄﾞ:XHD-000083
                    //        ・ｴﾗｰﾒｯｾｰｼﾞ:ﾃﾞｰﾀ取得ｴﾗｰ。ｼｽﾃﾑに連絡してください。{0}
                    //        0には、”EXHD-000006” を設定。
                    setErrorMessage(MessageUtil.getMessage("XHD-000083", "EXHD-000006"));
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, getErrorMessage(), null);
                    facesContext.addMessage(null, message);
                    return;
                }
                //     ｲ.取得できた場合
                //      以降の処理を実行する。
            }

            //   2.【印刷工程画面ID】 == "GXHDO101B002" の場合
            if ("GXHDO101B002".equals(getInsatsuKoteiGamenID())) {
                //    A.Ⅲ.画面表示仕様(29),(30)を発行する。
                Map srSpsprintInfo = loadSrSpsprintInfo(queryRunnerQcdb, strKojyo, strLotNo, strEdaban, jissekiInforev);
                Map subSrSpsprintScrInfo = loadSubSrSpsprintScrInfo(queryRunnerQcdb, strKojyo, strLotNo, strEdaban, jissekiInforev);
                if (srSpsprintInfo == null || subSrSpsprintScrInfo == null) {
                    //      ｱ.取得できなかった場合
                    //       ｴﾗｰﾒｯｾｰｼﾞを表示し、処理を中断する。
                    //         ・ｴﾗｰｺｰﾄﾞ:XHD-000083
                    //         ・ｴﾗｰﾒｯｾｰｼﾞ:ﾃﾞｰﾀ取得ｴﾗｰ。ｼｽﾃﾑに連絡してください。{0}
                    //         0には、”EXHD-000006” を設定。
                    setErrorMessage(MessageUtil.getMessage("XHD-000083", "EXHD-000006"));
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, getErrorMessage(), null);
                    facesContext.addMessage(null, message);
                    return;
                }
                //      ｲ.取得できた場合
                //       以降の処理を実行する。
            }

            //   3.【印刷工程画面ID】 == "GXHDO101B006" の場合
            if ("GXHDO101B006".equals(getInsatsuKoteiGamenID())) {
                //    A.Ⅲ.画面表示仕様(29),(30)を発行する。
                Map srSpRhapsInfo = loadSrRhapsInfo(queryRunnerQcdb, strKojyo, strLotNo, strEdaban, jissekiInforev);
                Map subSrSpRhapsInfo = loadSubSrRhapsInfo(queryRunnerQcdb, strKojyo, strLotNo, strEdaban, jissekiInforev);
                if (srSpRhapsInfo == null || subSrSpRhapsInfo == null) {
                    //      ｱ.取得できなかった場合
                    //       ｴﾗｰﾒｯｾｰｼﾞを表示し、処理を中断する。
                    //         ・ｴﾗｰｺｰﾄﾞ:XHD-000083
                    //         ・ｴﾗｰﾒｯｾｰｼﾞ:ﾃﾞｰﾀ取得ｴﾗｰ。ｼｽﾃﾑに連絡してください。{0}
                    //         0には、”EXHD-000006” を設定。
                    setErrorMessage(MessageUtil.getMessage("XHD-000083", "EXHD-000006"));
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, getErrorMessage(), null);
                    facesContext.addMessage(null, message);
                    return;
                }
                //      ｲ.取得できた場合
                //       以降の処理を実行する。
            }

            // ﾛｯﾄ参照の値をサブ画面の表示用の値に渡す
            GXHDO101C012 beanGXHDO101C012 = (GXHDO101C012) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C012);
            //参照元ﾛｯﾄNo
            beanGXHDO101C012.setSanshouMotoLotNo(searchLotNo);
            //参照元画面ID
            beanGXHDO101C012.setSanshouGamenID(getInsatsuKoteiGamenID());
            //参照先ﾛｯﾄNo
            beanGXHDO101C012.setSanshouSakiLotNo("");
            //参照先ﾛｯﾄNo背景色
            beanGXHDO101C012.setSanshousakiTextBackColor("#ffffffff");

            FXHDM01 sanshouMotoData = null;
            for (FXHDM01 fxhdm01 : this.menuListGXHDO101) {
                if (fxhdm01.getFormId().equals(getInsatsuKoteiGamenID())) {
                    sanshouMotoData = fxhdm01;
                    setSanshouMotoInfo(fxhdm01);
                    break;
                }
            }
            // 参照元ﾃﾞｰﾀ
            beanGXHDO101C012.setSanshouMotoInfo(sanshouMotoData);

            Map gamenInfo = getSekkeiInfoList(queryRunnerQcdb, strKojyo, strLotNo);
            if (gamenInfo != null) {
                //設計.printfmt
                beanGXHDO101C012.setPrintfmt(StringUtil.nullToBlank(getMapData(gamenInfo, "PrintFmt")));
                //設計.pattern
                beanGXHDO101C012.setPattern(StringUtil.nullToBlank(getMapData(gamenInfo, "PATTERN")));
            }

            // (2)ﾛｯﾄ参照画面【GXHDO101C012】へ遷移する。
            RequestContext context = RequestContext.getCurrentInstance();
            context.addCallbackParam("firstParam", "gxhdo101c012");

        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("ﾒﾆｭｰ名未登録", ex, LOGGER);
        }

    }

    /**
     * 総合判定ﾎﾞﾀﾝ押下
     */
    public void doSougouHantei() {

        try {
            // ﾛｯﾄNoを分割、桁数については検索がされていること前提の為、ﾁｪｯｸしない
            String kojyo = this.searchLotNo.substring(0, 3);
            String lotno = this.searchLotNo.substring(3, 11);
            String edaban = this.searchLotNo.substring(11, 14);
            QueryRunner queryRunnerQcdb = new QueryRunner(dataSourceXHD);

            String sogoHantei = getSogoHantei(queryRunnerQcdb, kojyo, lotno, edaban);
            String hanteiMsg;
            switch (sogoHantei) {
                case "OK":
                    hanteiMsg = "合格";
                    break;
                case "NG":
                    hanteiMsg = "不合格";
                    break;
                default:
                    hanteiMsg = "未判定";
                    break;
            }

            FacesContext facesContext = FacesContext.getCurrentInstance();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, hanteiMsg, null);
            facesContext.addMessage(null, message);

        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("総合判定処理エラー", ex, LOGGER);
        }

    }

    /**
     * [設計]から、ﾃﾞｰﾀを取得(設計.printfmt,設計.pattern)
     *
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map getSekkeiInfoList(QueryRunner queryRunnerQcdb, String kojyo, String lotNo) throws SQLException {

        String sql = "SELECT PrintFmt,PATTERN FROM da_sekkei WHERE KOJYO = ? AND LOTNO = ? AND EDABAN = ? ";
        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add("001");

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
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
     * 表示render有無
     *
     * @param sanshouBtnRender
     */
    public void setSanshouBtnRender(boolean sanshouBtnRender) {
        this.sanshouBtnRender = sanshouBtnRender;
    }

    /**
     * 表示render有無
     *
     * @return sanshouBtnRender
     */
    public boolean getSanshouBtnRender() {
        return sanshouBtnRender;
    }

    /**
     * 総合判定表示render有無
     *
     * @return the sougouHanteiBtnRender
     */
    public boolean getSougouHanteiBtnRender() {
        return sougouHanteiBtnRender;
    }

    /**
     * 総合判定表示render有無
     *
     * @param sougouHanteiBtnRender the sougouHanteiBtnRender to set
     */
    public void setSougouHanteiBtnRender(boolean sougouHanteiBtnRender) {
        this.sougouHanteiBtnRender = sougouHanteiBtnRender;
    }

    /**
     * 追加リンクrender有無
     *
     * @return the addLinkRender
     */
    public boolean isAddLinkRender() {
        return addLinkRender;
    }

    /**
     * 追加リンクrender有無
     *
     * @param addLinkRender the addLinkRender to set
     */
    public void setAddLinkRender(boolean addLinkRender) {
        this.addLinkRender = addLinkRender;
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
     * [品質DB登録実績]から、ﾃﾞｰﾀを取得(印刷工程入力ﾃﾞｰﾀ取得)
     *
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param formId 画面ID(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadFxhdd03JissekinoInfo(QueryRunner queryRunnerDoc, String kojyo, String lotNo,
            String edaban, String formId) throws SQLException {

        // 品質DB登録実績情報の取得
        String sql = "SELECT jissekino,rev, jotai_flg "
                + "FROM fxhdd03 "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND gamen_id = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(formId);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerDoc.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * [印刷SPSｸﾞﾗﾋﾞｱ]から、ﾃﾞｰﾀを取得
     *
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev (26)で取得したREV(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadSrSpsprintGraInfo(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        // 印刷SPSｸﾞﾗﾋﾞｱ情報の取得
        String sql = "SELECT kojyo "
                + "FROM sr_spsprint_gra "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND revision = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * [印刷SPSｸﾞﾗﾋﾞｱ_ｻﾌﾞ画面]から、ﾃﾞｰﾀを取得
     *
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev (26)で取得したREV(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadSubSrSpsprintGraInfo(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        // 印刷SPSｸﾞﾗﾋﾞｱ情報の取得
        String sql = "SELECT kojyo "
                + "FROM sub_sr_spsprint_gra "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND revision = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * [印刷SPSｽｸﾘｰﾝ]から、ﾃﾞｰﾀを取得
     *
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev (26)で取得したREV(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadSrSpsprintInfo(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        // 印刷SPSｽｸﾘｰﾝ情報の取得
        String sql = "SELECT kojyo "
                + "FROM sr_spsprint "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND revision = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * [印刷SPSｽｸﾘｰﾝ_ｻﾌﾞ画面]から、ﾃﾞｰﾀを取得
     *
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev (26)で取得したREV(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadSubSrSpsprintScrInfo(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        // 印刷SPSｽｸﾘｰﾝ_ｻﾌﾞ情報の取得
        String sql = "SELECT kojyo "
                + "FROM sub_sr_spsprint_scr "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND revision = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * [印刷積層RHAPS]から、ﾃﾞｰﾀを取得
     *
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev (26)で取得したREV(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadSrRhapsInfo(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        // 印刷SPSｽｸﾘｰﾝ情報の取得
        String sql = "SELECT kojyo "
                + "FROM sr_rhaps "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND revision = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * [印刷積層RHAPS_ｻﾌﾞ画面]から、ﾃﾞｰﾀを取得
     *
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev (26)で取得したREV(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadSubSrRhapsInfo(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        // 印刷SPSｽｸﾘｰﾝ_ｻﾌﾞ情報の取得
        String sql = "SELECT kojyo "
                + "FROM sub_sr_rhaps "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND revision = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
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
     * @param strSekkeiNo 設計No
     * @param menuListGXHDO101 メニューリスト(絞込有り)
     * @param menuListGXHDO101Nofiltering メニューリスト(絞込無し)
     * @param messageList メッセージリスト
     * @param menuNameSaisanka メニュー名(再酸化)
     * @param queryRunnerXHD データオブジェクト
     * @param session セッション
     * @throws SQLException 例外エラー
     * @throws CloneNotSupportedException 例外エラー
     */
    private void addMenuSaisanka(String strSekkeiNo, List<FXHDM01> menuListGXHDO101, List<FXHDM01> menuListGXHDO101Nofiltering, List<String> messageList,
            String menuNameSaisanka, QueryRunner queryRunnerXHD, HttpSession session) throws SQLException, CloneNotSupportedException {
        // 再酸化IDが存在しない場合
        if (!existFormIds(menuListGXHDO101Nofiltering, FORM_ID_SAISANKA)) {
            return;
        }

        // パラメータ取得(再酸化)
        String paramSaisanka = StringUtil.nullToBlank(selectParam.getValue("xhd_条件_再酸化回数", session));
        String[] spParamSaisanka = paramSaisanka.split(",");
        if (spParamSaisanka.length != 3) {
            // パラメータが取得出来なかったまたは3分割出なかった場合
            messageList.add(MessageUtil.getMessage("XHD-000130"));
            return;
        }

        // 再酸化の規格値を取得
        String kikakuchi = getKikakuchi(queryRunnerXHD, strSekkeiNo, spParamSaisanka[0], spParamSaisanka[1], spParamSaisanka[2]);

        // 規格値がnullの場合データなしエラー
        if (kikakuchi == null) {
            messageList.add(MessageUtil.getMessage("XHD-000070"));
            return;
        }

        // 規格値を数値(再酸化回数)に変換
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

        // メニューを回数分追加
        addMenuKaisu(menuListGXHDO101, saisankaKaisu, menuNameSaisanka, FORM_ID_SAISANKA);// 権限絞り込みありメニュー
        addMenuKaisu(menuListGXHDO101Nofiltering, saisankaKaisu, menuNameSaisanka, FORM_ID_SAISANKA);// 権限絞り込みメニュー
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
        FXHDM01 fxhdm01GaikanKensa = null;   //外観検査
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
                case FORM_ID_GAIKAN_KENSA:
                    // 外観検査に関しては
                    if (fxhdm01GaikanKensa == null) {
                        fxhdm01GaikanKensa = fxhdm01;
                    }
                    break;
            }

            // 全ての値がセットされたらbreak
            if (fxhdm01Saisanka != null && fxhdm01Barrel != null && fxhdm01Keisu != null && fxhdm01JikiQC != null && fxhdm01GaikanKensa != null) {
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
                    if (!addMenu(menuListGXHDO101, addCountMap, ++addIdx, fxhdm01GaikanKensa, "")) {
                        addIdx--; //メニューが追加できない場合はデクリメント
                    }
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
     * @param addCountMap 追加回数マップ
     * @param addIdx 追加位置
     * @param fxhdm01 メニュー情報
     * @param setName 設定メニュー名
     * @return 処理結果(true：メニュー追加、false：メニュー追加無し)
     * @throws CloneNotSupportedException
     */
    private boolean addMenu(List<FXHDM01> menuListGXHDO101, Map addCountMap, int addIdx, FXHDM01 fxhdm01, String setName) throws CloneNotSupportedException {
        if (fxhdm01 == null) {
            return false;
        }
        menuListGXHDO101.add(addIdx, fxhdm01.clone());
        menuListGXHDO101.get(addIdx).setDeleteBtnRender(false);
        if (!StringUtil.isEmpty(setName)) {
            int addCount = 0;
            if (addCountMap.containsKey(fxhdm01.getFormId())) {
                addCount = (int) addCountMap.get(fxhdm01.getFormId());
            }
            addCount++;
            addCountMap.put(fxhdm01.getFormId(), addCount);
            menuListGXHDO101.get(addIdx).setMenuName(setName + "(追加" + addCount + "回目)");
        } else if (FORM_ID_GAIKAN_KENSA.equals(fxhdm01.getFormId())) {
            //外観検査の場合名前固定
            menuListGXHDO101.get(addIdx).setMenuName("検査(外観検査2次)");
        }

        return true;
    }

    /**
     * 条件テーブルより規格値※レコードなしの場合はNULLを返却
     *
     * @param queryRunnerXHD データオブジェクト
     * @param sekkeiNo 設計No
     * @param kouteimei 工程名
     * @param koumokumei 項目名
     * @param kanrikoumoku 管理項目
     * @return 規格値
     * @throws SQLException 例外エラー
     */
    private String getKikakuchi(QueryRunner queryRunnerXHD, String sekkeiNo, String kouteimei,
            String koumokumei, String kanrikoumoku) throws SQLException {

        // 条件ﾃｰﾌﾞﾙを検索
        String sql = "SELECT KIKAKUCHI "
                + "  FROM da_joken "
                + " WHERE SEKKEINO = ? "
                + "   AND KOUTEIMEI = ? "
                + "   AND KOUMOKUMEI = ? "
                + "   AND KANRIKOUMOKU = ? ";

        List<Object> params = new ArrayList<>();
        params.add(sekkeiNo);
        params.add(kouteimei);
        params.add(koumokumei);
        params.add(kanrikoumoku);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        Map resultMap = queryRunnerXHD.query(sql, new MapHandler(), params.toArray());
        if (resultMap == null) {
            return null;
        }

        return StringUtil.nullToBlank(resultMap.get("KIKAKUCHI"));
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
     * [品質DB登録実績]から、状態ﾌﾗｸﾞ,実績No,ﾘﾋﾞｼﾞｮﾝを取得
     *
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param formId 画面ID(検索キー)
     * @param jissekino 画面ID(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<String[]> loadFxhdd03InfoList(QueryRunner queryRunnerDoc, String kojyo, String lotNo,
            String edaban, String formId, int jissekino) throws SQLException {

        // 品質DB登録実績情報の取得
        String sql = "SELECT jotai_flg, jissekino, rev "
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
        List processResult = (List) queryRunnerDoc.query(sql, new MapListHandler(), params.toArray());

        List<String[]> fxhdd03InfoList = new ArrayList<>();
        for (Iterator i = processResult.iterator(); i.hasNext();) {
            HashMap m = (HashMap) i.next();
            fxhdd03InfoList.add(new String[]{m.get("jotai_flg").toString(), m.get("jissekino").toString(), m.get("rev").toString()});
        }

        return fxhdd03InfoList;
    }

    /**
     * [品質DB登録実績]から登録済状態の、実績No,ﾘﾋﾞｼﾞｮﾝを取得
     *
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param formId 画面ID(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<String[]> loadFxhdd03InfoRegisteredList(QueryRunner queryRunnerDoc, String kojyo, String lotNo,
            String edaban, String formId) throws SQLException {

        // 品質DB登録実績情報の取得
        String sql = "SELECT jissekino, rev "
                + "FROM fxhdd03 "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND gamen_id = ? "
                + "AND jotai_flg = ? "
                + "ORDER BY jissekino ASC ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(formId);
        params.add("1");
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        List processResult = (List) queryRunnerDoc.query(sql, new MapListHandler(), params.toArray());

        List<String[]> fxhdd03InfoList = new ArrayList<>();
        for (Iterator i = processResult.iterator(); i.hasNext();) {
            HashMap m = (HashMap) i.next();
            fxhdd03InfoList.add(new String[]{m.get("jissekino").toString(), m.get("rev").toString()});

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
    private int getJissekino(Map mapJissekiNo, List<FXHDM01> menuListGXHDO101, String gamenId, List<Map<String, Object>> fxhdd08Info) {
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

        // 最大の実績Noを取得
        int jissekiNo = getMaxJissekiNo(mapJissekiNo, gamenId, fxhdd08Info);

        // 最大実績No更新
        if (!mapJissekiNo.containsKey(gamenId) || (int) mapJissekiNo.get(gamenId) < jissekiNo) {
            mapJissekiNo.put(gamenId, jissekiNo);
        }

        return jissekiNo;
    }

    /**
     * 実績状態の設定を行う。
     *
     * @param menuListGXHDO101 メニューリスト
     * @param fxhdd03List 実績リスト
     * @throws SQLException 例外エラー
     */
    private void setJissekiJotai(List<FXHDM01> menuListGXHDO101, List<String[]> fxhdd03List) throws SQLException {

        boolean existJiseki;
        for (FXHDM01 fxhdm01 : menuListGXHDO101) {
            existJiseki = false;
            for (String[] fxhdd03Info : fxhdd03List) {
                if (fxhdm01.getFormId().equals(fxhdd03Info[0]) && StringUtil.nullToBlank(fxhdm01.getJissekiNo()).equals(fxhdd03Info[1])) {
                    fxhdm01.setJotaiFlg(fxhdd03Info[2]);
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
                fxhdm01.setJotaiFlg("");
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
     * @param menuList メニューリスト
     * @param formIds 対象画面ID
     * @return true:存在する、false:存在しない
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
     * メニュー 追加処理(回数指定)
     *
     * @param menuListGXHDO101 メニューリスト
     * @param kaisu 回数
     * @param menuName メニュー名
     * @param targetFormId 対象画面ID
     * @throws SQLException 例外エラー
     * @throws CloneNotSupportedException 例外エラー
     */
    private void addMenuKaisu(List<FXHDM01> menuListGXHDO101, int kaisu, String menuName, String targetFormId) throws CloneNotSupportedException {

        // 回数が1の場合処理なし
        if (kaisu == 1) {
            return;
        }

        boolean existData = false;
        int saisankaIdx = -1;
        // 対象の画面IDが存在するかと、メニュー位置を取得
        for (FXHDM01 fxhdm01 : menuListGXHDO101) {
            saisankaIdx++;
            if (targetFormId.equals(fxhdm01.getFormId())) {
                existData = true;
                break;
            }
        }

        // 画面IDが存在しない場合
        if (!existData) {
            return;
        }

        // メニュに回数分のメニューを追加
        for (int i = 0; i < kaisu; i++) {
            if (0 < i) {
                FXHDM01 addData = menuListGXHDO101.get(saisankaIdx).clone();
                addData.setDeleteBtnRender(false);
                menuListGXHDO101.add(i + saisankaIdx, addData);
            }
            // メニューリストの名称と実績Noを設定
            menuListGXHDO101.get(i + saisankaIdx).setMenuName(menuName + "(" + (i + 1) + "回目)");
        }
    }

    /**
     * メニュー 追加処理(回数指定)
     * @param menuListGXHDO101 メニューリスト
     * @param kaisu 回数
     * @param targetFormId 対象画面ID
     * @param startJissekiNo 開始実績No
     * @param mapJissekiNo 実績NoMap
     * @param fxhdd08Info 画面制御情報
     * @param gaikankensasyurui 外観検査種類
     * @throws CloneNotSupportedException 
     */
    private void addMenuKaisu(List<FXHDM01> menuListGXHDO101, int kaisu, String targetFormId, int startJissekiNo, Map mapJissekiNo, List<Map<String, Object>> fxhdd08Info, List<String> gaikankensasyuruiList) throws CloneNotSupportedException {

        String menuName = "";

        boolean existData = false;
        int menuIdx = -1;
        // 対象の画面IDが存在するかと、メニュー位置を取得
        for (FXHDM01 fxhdm01 : menuListGXHDO101) {
            menuIdx++;
            if (targetFormId.equals(fxhdm01.getFormId()) && startJissekiNo == fxhdm01.getJissekiNo()) {
                existData = true;
                menuName = fxhdm01.getMenuName();
                break;
            }
        }

        // 画面IDが存在しない場合
        if (!existData) {
            return;
        }

        // 初回は実績Noが0で来るため、1をセット
        int jissekiNo;
        if (startJissekiNo == 0) {
            jissekiNo = getMaxJissekiNo(mapJissekiNo, targetFormId, fxhdd08Info);
        } else {
            jissekiNo = startJissekiNo;
        }

        // メニュに回数分のメニューを追加
        String setMenuName = menuName;
        for (int i = 0; i < kaisu; i++) {
            if (0 < i) {
                FXHDM01 addData = menuListGXHDO101.get(menuIdx).clone();
                addData.setDeleteBtnRender(false);
                menuListGXHDO101.add(i + menuIdx, addData);
                jissekiNo++;
            }

            if (1 < kaisu) {
                setMenuName = menuName + "(" + (i + 1) + "回目)";
            }

            // メニューリストの名称と実績Noを設定
            menuListGXHDO101.get(i + menuIdx).setMenuName(setMenuName);

            if (0 == startJissekiNo || 0 < i) {
                while (existJisekiNo(fxhdd08Info, targetFormId, jissekiNo)) {
                    jissekiNo++;
                }
            }

            menuListGXHDO101.get(i + menuIdx).setJissekiNo(jissekiNo);
            if(i < gaikankensasyuruiList.size()){
                menuListGXHDO101.get(i + menuIdx).setGaikankensasyurui(gaikankensasyuruiList.get(i));
            }
        }

        // 最大の実績Noを保持
        if (!mapJissekiNo.containsKey(targetFormId) || (int) mapJissekiNo.get(targetFormId) < jissekiNo) {
            mapJissekiNo.put(targetFormId, jissekiNo);
        }

    }

    private boolean existJisekiNo(List<Map<String, Object>> fxhdd08Info, String targetFormId, int jissekino) {
        return 0 < fxhdd08Info.stream().filter(n -> targetFormId.equals(String.valueOf(n.get("gamen_id")))).filter(n -> jissekino == (int) n.get("jissekino")).count();
    }

    /**
     * DP2メニュー 追加処理
     *
     * @param sekkeiNo 設計No
     * @param menuListGXHDO101 メニューリスト(絞込有り)
     * @param menuListGXHDO101Nofiltering メニューリスト(絞込無し)
     * @param messageList メッセージリスト
     * @param menuNameDp2 メニュー名(Dp2)
     * @param queryRunnerXHD データオブジェクト
     * @param session セッション
     * @throws SQLException 例外エラー
     * @throws CloneNotSupportedException 例外エラー
     */
    private void addMenuDp2(String sekkeiNo, List<FXHDM01> menuListGXHDO101, List<FXHDM01> menuListGXHDO101Nofiltering, List<String> messageList,
            String menuNameDp2, QueryRunner queryRunnerXHD, HttpSession session) throws SQLException, CloneNotSupportedException {
        // DP2IDが存在しない場合
        if (!existFormIds(menuListGXHDO101Nofiltering, FORM_ID_DP2)) {
            return;
        }

        // パラメータ取得(DP2)
        String param = StringUtil.nullToBlank(selectParam.getValue("xhd_条件_DP2回数", session));
        String[] spParam = param.split(",");
        if (spParam.length != 3) {
            // パラメータが取得出来なかったまたは3分割出なかった場合
            messageList.add(MessageUtil.getMessage("XHD-000131"));
            return;
        }

        // 再酸化の規格値を取得
        String kikakuchi = getKikakuchi(queryRunnerXHD, sekkeiNo, spParam[0], spParam[1], spParam[2]);

        // 規格値がnullの場合データなしエラー
        if (kikakuchi == null) {
            messageList.add(MessageUtil.getMessage("XHD-000133"));
            return;
        }

        // 規格値を数値(DP2回数)に変換
        int kaisu = 0;
        try {
            kaisu = Integer.parseInt(kikakuchi);
        } catch (NumberFormatException e) {
            //処理なし
        }

        // 回数が0以下の場合、エラー
        if (kaisu <= 0) {
            messageList.add(MessageUtil.getMessage("XHD-000132"));
            return;
        }

        // メニューを回数分追加
        addMenuKaisu(menuListGXHDO101, kaisu, menuNameDp2, FORM_ID_DP2);// 権限絞り込みありメニュー
        addMenuKaisu(menuListGXHDO101Nofiltering, kaisu, menuNameDp2, FORM_ID_DP2);// 権限絞り込みメニュー
    }

    /**
     * 外部電極焼成メニュー 追加処理
     *
     * @param sekkeiNo 設計No
     * @param menuListGXHDO101 メニューリスト(絞込有り)
     * @param menuListGXHDO101Nofiltering メニューリスト(絞込無し)
     * @param messageList メッセージリスト
     * @param menuName メニュー名
     * @param queryRunnerXHD データオブジェクト
     * @param session セッション
     * @throws SQLException 例外エラー
     * @throws CloneNotSupportedException 例外エラー
     */
    private void addMenuGaibuDenkyokuSyosei(String sekkeiNo, List<FXHDM01> menuListGXHDO101, List<FXHDM01> menuListGXHDO101Nofiltering, List<String> messageList,
            String menuName, QueryRunner queryRunnerXHD, HttpSession session) throws SQLException, CloneNotSupportedException {
        // 外部電極焼成(焼成)IDが存在しない場合
        if (!existFormIds(menuListGXHDO101Nofiltering, FORM_ID_GAIBUDENKYOKU_SYOSEI)) {
            return;
        }

        String param = StringUtil.nullToBlank(selectParam.getValue("xhd_条件_外部電極焼成(焼成)回数", session));
        String[] spParam = param.split(",");
        if (spParam.length != 3) {
            // パラメータが取得出来なかったまたは3分割出なかった場合
            messageList.add(MessageUtil.getMessage("XHD-000134"));
            return;
        }

        // 再酸化の規格値を取得
        String kikakuchi = getKikakuchi(queryRunnerXHD, sekkeiNo, spParam[0], spParam[1], spParam[2]);

        // 規格値がnullの場合データなしエラー
        if (kikakuchi == null) {
            messageList.add(MessageUtil.getMessage("XHD-000136"));
            return;
        }

        // 規格値を数値(外部電極焼成(焼成))に変換
        int kaisu = 0;
        try {
            kaisu = Integer.parseInt(kikakuchi);
        } catch (NumberFormatException e) {
            //処理なし
        }

        // 回数が0以下の場合、エラー
        if (kaisu <= 0) {
            messageList.add(MessageUtil.getMessage("XHD-000135"));
            return;
        }

        // メニューを回数分追加
        addMenuKaisu(menuListGXHDO101, kaisu, menuName, FORM_ID_GAIBUDENKYOKU_SYOSEI);// 権限絞り込みありメニュー
        addMenuKaisu(menuListGXHDO101Nofiltering, kaisu, menuName, FORM_ID_GAIBUDENKYOKU_SYOSEI);// 権限絞り込みなしメニュー
    }

    /**
     * 外部電極塗布メニュー 追加処理
     *
     * @param sekkeiNo 設計No
     * @param menuListGXHDO101 メニューリスト(絞込有り)
     * @param menuListGXHDO101Nofiltering メニューリスト(絞込無し)
     * @param messageList メッセージリスト
     * @param menuName メニュー名
     * @param queryRunnerXHD データオブジェクト
     * @param session セッション
     * @throws SQLException 例外エラー
     * @throws CloneNotSupportedException 例外エラー
     */
    private void addMenuGaibuDenkyokuTofu(List<FXHDM01> menuListGXHDO101, List<FXHDM01> menuListGXHDO101Nofiltering, List<String> messageList,
            String menuName, QueryRunner queryRunnerXHD, QueryRunner queryRunnerDoc, HttpSession session) throws SQLException, CloneNotSupportedException {
        // 外部電極塗布の画面IDが存在しない場合、処理なし
        if (!existFormIds(menuListGXHDO101Nofiltering, FORM_ID_GAIBUDENKYOKU_TOFU)) {
            return;
        }
        // 外部電極焼成(焼成外観)の画面IDが存在しない場合、処理なし
        if (!existFormIds(menuListGXHDO101Nofiltering, FORM_ID_GAIBUDENKYOKU_SYOSEIGAIKAN)) {
            return;
        }

        // ﾛｯﾄNoを分割(チェック処理が実行されている前提の為、桁数の不足などは考慮しない)
        String strKojyo = this.lotNo.substring(0, 3);
        String strLotNo = this.lotNo.substring(3, 11);
        String strEdaban = this.lotNo.substring(11, 14);
        // [品質DB登録実績]から、データを取得(外部電極焼成(焼成外観)入力ﾃﾞｰﾀ取得)
        List<String[]> fxhdd03InfoList = loadFxhdd03InfoList(queryRunnerDoc, strKojyo, strLotNo, strEdaban, FORM_ID_GAIBUDENKYOKU_SYOSEIGAIKAN, 1);
        // 品質DB登録実績が取得できなかった場合リターン
        if (fxhdd03InfoList.isEmpty()) {
            return;
        }

        // 品質DB登録情報(状態フラグ、実績No、REV)を取得
        String[] fxhdd03Info = fxhdd03InfoList.get(0);
        if (!"1".equals(fxhdd03Info[0])) {
            // 状態ﾌﾗｸﾞが1以外の場合処理しない
            return;
        }

        //KCPNoを外部電極焼成(焼成外観)から取得
        String kcpNo = getSrGdyakitukegaikanKcpNo(queryRunnerXHD, strKojyo, strLotNo, strEdaban, fxhdd03Info[1], fxhdd03Info[2]);
        if (kcpNo == null) {
            // 外部電極焼成(焼成外観)が取得出来ない場合はエラー
            messageList.add(MessageUtil.getMessage("XHD-000137"));
            return;
        }

        // パラメータ(KCPNo)取得(外部電極焼成外観後_外部電極塗布追加)
        String paramKcpNo = getParamData("xhd_外部電極焼成外観後_外部電極塗布追加", session);
        if (paramKcpNo == null) {
            // パラメータが取得出来なかった場合
            messageList.add(MessageUtil.getMessage("XHD-000138"));
            return;
        }

        // KCPNoがパラメータと前方一致しない場合、処理終了
        if (!kcpNo.startsWith(paramKcpNo)) {
            return;
        }

        // 権限絞り込みありメニューのメニュー追加処理
        // 1回目の外部電極塗布メニューデータ取得
        FXHDM01 itemTofuFirst = menuListGXHDO101.stream().filter(n -> FORM_ID_GAIBUDENKYOKU_TOFU.equals(n.getFormId())).findFirst().orElse(null);
        if (itemTofuFirst != null) {
            // 新しくメニュー名をセット
            itemTofuFirst.setMenuName(menuName + "(1回目)");
            //外部電極焼成(焼成外観)下にメニュ名を追加
            addMenuTargetFormId(menuListGXHDO101, FORM_ID_GAIBUDENKYOKU_SYOSEIGAIKAN, itemTofuFirst, menuName + "(2回目)");
        }

        // 権限絞り込みなしメニューのメニュー追加処理
        // 1回目の外部電極塗布メニューデータ取得
        FXHDM01 itemTofuFirstNoFilter = menuListGXHDO101Nofiltering.stream().filter(n -> FORM_ID_GAIBUDENKYOKU_TOFU.equals(n.getFormId())).findFirst().orElse(null);
        if (itemTofuFirstNoFilter != null) {
            // 新しくメニュー名をセット
            itemTofuFirstNoFilter.setMenuName(menuName + "(1回目)");
            addMenuTargetFormId(menuListGXHDO101Nofiltering, FORM_ID_GAIBUDENKYOKU_SYOSEIGAIKAN, itemTofuFirstNoFilter, menuName + "(2回目)");
        }

    }

    /**
     * [外部電極焼成(焼成外観)]から、KCPNoを取得(値が無い場合はNULLを返却)
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param jissekino 実績No(検索キー)
     * @param rev revision(検索キー)
     * @return KCPNo
     * @throws SQLException 例外エラー
     */
    private String getSrGdyakitukegaikanKcpNo(QueryRunner queryRunnerXHD, String kojyo, String lotNo,
            String edaban, String jissekino, String rev) throws SQLException {

        String sql = "SELECT kcpno "
                + "FROM sr_gdyakitukegaikan "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND kaisuu = ? AND revision = ? ";

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

        return StringUtil.nullToBlank(resultMap.get("kcpno"));
    }

    /**
     * パラメータデータの取得
     *
     * @param key キー
     * @param session セッション
     * @return keyをもとに取得したデータ ※keyが存在しない場合NULLを返却
     */
    private String getParamData(String key, HttpSession session) {
        if (!selectParam.existKey(key, session)) {
            return null;
        }
        return StringUtil.nullToBlank(selectParam.getValue(key, session));
    }

    /**
     * 指定の画面IDの下にメニューを追加
     *
     * @param menuListGXHDO101 メニューリスト
     * @param targetFormId 対象画面ID
     * @param fxhdm01 追加メニューデータ
     * @param setName 設定名称
     * @throws CloneNotSupportedException 例外エラー
     */
    private void addMenuTargetFormId(List<FXHDM01> menuListGXHDO101, String targetFormId, FXHDM01 fxhdm01, String setName) throws CloneNotSupportedException {
        if (fxhdm01 == null) {
            return;
        }

        boolean existData = false;
        int addidx = 0;
        // 対象の画面IDの位置を検索
        for (FXHDM01 menuData : menuListGXHDO101) {
            addidx++;
            if (targetFormId.equals(menuData.getFormId())) {
                existData = true;
                break;
            }
        }

        // 画面IDが存在しない場合
        if (!existData) {
            return;
        }

        FXHDM01 addData = fxhdm01.clone();
        addData.setMenuName(setName);
        addData.setDeleteBtnRender(false);
        menuListGXHDO101.add(addidx, addData);

    }

    /**
     * 外部電極焼成(メッキ・品質検査)メニュー 追加処理
     *
     * @param menuListGXHDO101 メニューリスト(絞込有り)
     * @param menuListGXHDO101Nofiltering メニューリスト(絞込無し)
     * @param messageList メッセージリスト
     * @param menuName メニュー名
     * @param queryRunnerXHD データオブジェクト
     * @throws SQLException 例外エラー
     * @throws CloneNotSupportedException 例外エラー
     */
    private void addMenuGaibuDenkyokuMekkiHinshitsuKensa(List<FXHDM01> menuListGXHDO101, List<FXHDM01> menuListGXHDO101Nofiltering, List<String> messageList,
            String menuName, QueryRunner queryRunnerXHD, QueryRunner queryRunnerDoc) throws SQLException, CloneNotSupportedException {
        // 外部電極・メッキ品質検査IDが存在しない場合
        if (!existFormIds(menuListGXHDO101Nofiltering, FORM_ID_GAIBUDENKYOKU_MEKKI_HINSHITSU_KENSA)) {
            return;
        }
        // ﾛｯﾄNoを分割(チェック処理が実行されている前提の為、桁数の不足などは考慮しない)
        String strKojyo = this.lotNo.substring(0, 3);
        String strLotNo = this.lotNo.substring(3, 11);
        String strEdaban = this.lotNo.substring(11, 14);

        // 品質DB登録実績情報(登録済)取得
        List<String[]> fxhdd03InfoList = loadFxhdd03InfoRegisteredList(queryRunnerDoc, strKojyo, strLotNo, strEdaban, FORM_ID_GAIBUDENKYOKU_MEKKI_HINSHITSU_KENSA);
        int jissekiNo = 1;
        for (String[] info : fxhdd03InfoList) {
            // 実績Noを1から順番にチェック、一致しなくなったタイミングでループを抜ける
            if (!String.valueOf(jissekiNo).equals(info[0])) {
                break;
            }

            // 外部電極・ﾒｯｷから外観を取得
            String gaikan = getSrMekkiGaikan(queryRunnerXHD, strKojyo, strLotNo, strEdaban, info[0], info[1]);
            // データが存在しない場合、エラー
            if (gaikan == null) {
                messageList.add(MessageUtil.getMessage("XHD-000139"));
                return;
            }

            // 再ﾒｯｷ以外ループを抜ける
            if (!"再ﾒｯｷ".equals(gaikan)) {
                break;
            }

            jissekiNo++;
        }

        // メニューを回数(実績No)分追加
        addMenuKaisu(menuListGXHDO101, jissekiNo, menuName, FORM_ID_GAIBUDENKYOKU_MEKKI_HINSHITSU_KENSA);// 権限絞り込みありメニュー
        addMenuKaisu(menuListGXHDO101Nofiltering, jissekiNo, menuName, FORM_ID_GAIBUDENKYOKU_MEKKI_HINSHITSU_KENSA);// 権限絞り込みなしメニュー

    }

    /**
     * [外部電極・ﾒｯｷ]から、外観を取得(値が無い場合はNULLを返却)
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param jissekino 実績No(検索キー)
     * @param rev revision(検索キー)
     * @return 外観(gaikankensakekka)
     * @throws SQLException 例外エラー
     */
    private String getSrMekkiGaikan(QueryRunner queryRunnerXHD, String kojyo, String lotNo,
            String edaban, String jissekino, String rev) throws SQLException {

        String sql = "SELECT gaikankensakekka "
                + "FROM sr_mekki "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND jissekino = ? AND revision = ? ";

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

        return StringUtil.nullToBlank(resultMap.get("gaikankensakekka"));
    }

    /**
     * 総重量の取得処理(研磨計測より総重量を取得する。)
     *
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
        String sql = "SELECT kcpno, oyalotedaban, suuryo, torikosuu, lotkubuncode, ownercode, tokuisaki"
                + " FROM sikakari WHERE kojyo = ? AND lotno = ? AND edaban = ?";

        List<Object> params = new ArrayList<>();
        params.add(lotNo1);
        params.add(lotNo2);
        params.add(lotNo3);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerWip.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * ﾛｯﾄ参照【画面IDﾘｽﾄ】に画面IDが存在すること
     *
     * @param menuList メニューリスト
     * @param filterGamenIdList フィルタリングID
     */
    private boolean lotSanshouMenuListFiltering(List<FXHDM01> menuList, List<String> filterGamenIdList) {
        if (!menuList.isEmpty()) {
            for (String formId : filterGamenIdList) {
                if (0 < menuList.stream().filter(n -> formId.equals(n.getFormId())).count()) {
                    setInsatsuKoteiGamenID(formId);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * ﾒﾆｭｰ名を取得する
     *
     * @param menuList メニューリスト
     * @param filterGamenId フィルタリングID
     */
    private String getMenuName(List<FXHDM01> menuList, String filterGamenId) {
        if (!menuList.isEmpty()) {
            for (FXHDM01 data : menuList) {
                if (filterGamenId.equals(data.getFormId())) {
                    return data.getMenuName();
                }
            }
        }
        return "";
    }

    /**
     * 電気特性画面に必要な情報をセッションにセットする。
     *
     * @param formId 画面ID
     * @param session セッション情報
     * @throws SQLException 例外エラー
     */
    private void setDenkitokuseiSessionData(String formId, HttpSession session) throws SQLException {

        if (!FORM_ID_DENKITOKUSEI_ESI.equals(formId) && !FORM_ID_DENKITOKUSEI_3TANSHI_4TANSHI.equals(formId) && !FORM_ID_DENKITOKUSEI_IPPANHIN.equals(formId)) {
            // 電気特性の画面ID以外は処理なし
            return;
        }

        QueryRunner queryRunnerDoc = new QueryRunner(dataSourceDocServer);
        QueryRunner queryRunnerQcdb = new QueryRunner(dataSourceXHD);
        String strKojyo = this.searchLotNo.substring(0, 3);
        String strLotNo = this.searchLotNo.substring(3, 11);
        String strEdaban = this.searchLotNo.substring(11, 14);

        // ﾒｯｷ品質検査(登録済みのデータが存在すれば値を取得する。)
        Map srMekkiInfo = null;
        List<String[]> Fxhdd03InfoListB038 = loadFxhdd03InfoListJisekiNoDesc(queryRunnerDoc, strKojyo, strLotNo, strEdaban, "GXHDO101B038");
        if (!Fxhdd03InfoListB038.isEmpty() && "1".equals(Fxhdd03InfoListB038.get(0)[0])) {
            srMekkiInfo = CommonUtil.getSrMekkiData(queryRunnerQcdb, strKojyo, strLotNo, strEdaban, Fxhdd03InfoListB038.get(0)[2], Integer.parseInt(Fxhdd03InfoListB038.get(0)[1]));
        }
        session.setAttribute("SrMekkiInfo", srMekkiInfo);

        //外部電極焼成
        Map srGdyakitukeInfo = null;
        List<String[]> Fxhdd03InfoListB029 = loadFxhdd03InfoListJisekiNoDesc(queryRunnerDoc, strKojyo, strLotNo, strEdaban, "GXHDO101B029");
        if (!Fxhdd03InfoListB029.isEmpty() && "1".equals(Fxhdd03InfoListB029.get(0)[0])) {
            srGdyakitukeInfo = CommonUtil.getSrGdyakitukeData(queryRunnerQcdb, strKojyo, strLotNo, strEdaban, Fxhdd03InfoListB029.get(0)[2], Integer.parseInt(Fxhdd03InfoListB029.get(0)[1]));
        }
        session.setAttribute("SrGdyakitukeInfo", srGdyakitukeInfo);

        //磁器QC
        Map srJikiqcInfo = null;
        List<String[]> Fxhdd03InfoListB022 = loadFxhdd03InfoListJisekiNoDesc(queryRunnerDoc, strKojyo, strLotNo, strEdaban, "GXHDO101B022");
        if (!Fxhdd03InfoListB022.isEmpty() && "1".equals(Fxhdd03InfoListB022.get(0)[0])) {
            srJikiqcInfo = CommonUtil.getSrJikiqcData(queryRunnerQcdb, strKojyo, strLotNo, strEdaban, Fxhdd03InfoListB022.get(0)[2], Integer.parseInt(Fxhdd03InfoListB022.get(0)[1]));
        }
        session.setAttribute("SrJikiqcInfo", srJikiqcInfo);

        //熱処理
        Map srShinkuukansou = null;
        List<String[]> Fxhdd03InfoListB043 = loadFxhdd03InfoListJisekiNoDesc(queryRunnerDoc, strKojyo, strLotNo, strEdaban, "GXHDO101B043");
        if (!Fxhdd03InfoListB043.isEmpty() && "1".equals(Fxhdd03InfoListB043.get(0)[0])) {
            srShinkuukansou = CommonUtil.getSrShinkuukansouData(queryRunnerQcdb, strKojyo, strLotNo, strEdaban, Fxhdd03InfoListB043.get(0)[2], Integer.parseInt(Fxhdd03InfoListB043.get(0)[1]));
        }
        session.setAttribute("SrShinkuukansou", srShinkuukansou);

    }

    /**
     * 検査・外観検査画面に必要な情報をセッションにセットする。
     *
     * @param formId 画面ID
     * @param maeKoteiMenuInfo 前工程メニュー情報
     * @param menuInfo メニュー情報
     * @param session セッション情報
     * @throws SQLException 例外エラー
     */
    private void setGaikanKensaSessionData(String formId, FXHDM01 maeKoteiMenuInfo, FXHDM01 menuInfo, HttpSession session) throws SQLException {

        if (!FORM_ID_GAIKAN_KENSA.equals(formId)) {
            // 検査・外観検査の画面ID以外は処理なし
            return;
        }

        // 前工程の画面IDを取得
        String maekoteiFormId = "";
        if (maeKoteiMenuInfo != null) {
            maekoteiFormId = maeKoteiMenuInfo.getFormId();
        }

        // 前工程の画面により検査種類をセッションにセット
        switch (maekoteiFormId) {
            // 磁器QC
            case FORM_ID_JIKI_QC:
                session.setAttribute("kensashuri46", "2");
                break;
            // 外部電極・ﾒｯｷ真空乾燥
            case FORM_ID_GAIBUDENKYOKU_MEKKI_SHINKU_KANSO:
                session.setAttribute("kensashuri46", "3");
                break;
            default:
                if (StringUtil.isEmpty(menuInfo.getGaikankensasyurui())) {
                    session.setAttribute("kensashuri46", "4");
                } else {
                    session.setAttribute("kensashuri46", menuInfo.getGaikankensasyurui());
                }
                break;
        }

        QueryRunner queryRunnerDoc = new QueryRunner(dataSourceDocServer);
        QueryRunner queryRunnerQcdb = new QueryRunner(dataSourceXHD);
        String strKojyo = this.searchLotNo.substring(0, 3);
        String strLotNo = this.searchLotNo.substring(3, 11);
        String strEdaban = this.searchLotNo.substring(11, 14);

        //磁器QC
        Map srJikiqcInfo = null;
        List<String[]> Fxhdd03InfoListB022 = loadFxhdd03InfoListJisekiNoDesc(queryRunnerDoc, strKojyo, strLotNo, strEdaban, FORM_ID_JIKI_QC);
        if (!Fxhdd03InfoListB022.isEmpty() && "1".equals(Fxhdd03InfoListB022.get(0)[0])) {
            srJikiqcInfo = CommonUtil.getSrJikiqcData(queryRunnerQcdb, strKojyo, strLotNo, strEdaban, Fxhdd03InfoListB022.get(0)[2], Integer.parseInt(Fxhdd03InfoListB022.get(0)[1]));
        }
        session.setAttribute("SrJikiqcInfo", srJikiqcInfo);

    }

    /**
     * 電気特性メニュー 追加処理
     *
     * @param menuListGXHDO101 メニューリスト
     * @param messageList メッセージリスト
     * @param fxhdd03InfoList 実績リスト
     * @param formId 画面ID
     * @param startJisskeino 開始実績No
     * @param fxhdd08InfoList 画面制御情報リスト
     * @param mapMaxJissekiNo 最大実績NoMap
     * @param queryRunnerXHD queryRunner オブジェクト
     * @return true：追加あり、false：追加無し
     * @throws SQLException 例外エラー
     * @throws CloneNotSupportedException 例外エラー
     */
    private boolean addMenuDenkitokusei(List<FXHDM01> menuListGXHDO101, List<String> messageList, List<String[]> fxhdd03InfoList,
            String formId, int startJisskeino, List<Map<String, Object>> fxhdd08InfoList, Map mapMaxJissekiNo, QueryRunner queryRunnerXHD) throws SQLException, CloneNotSupportedException {

        FXHDM01 targetMenu = menuListGXHDO101.stream().filter(n -> formId.equals(n.getFormId())).filter(n -> startJisskeino == n.getJissekiNo()).findFirst().orElse(null);
        // 対象のメニューが存在しない場合
        if (targetMenu == null) {
            return false;
        }

        int jissekiNo;
        if (startJisskeino == 0) {
            // 初回は実績Noが0で来るため、1か最大値+1をセット
            jissekiNo = getMaxJissekiNo(mapMaxJissekiNo, formId, fxhdd08InfoList);
        } else {
            jissekiNo = startJisskeino;
        }

        // ﾛｯﾄNoを分割(チェック処理が実行されている前提の為、桁数の不足などは考慮しない)
        String strKojyo = this.lotNo.substring(0, 3);
        String strLotNo = this.lotNo.substring(3, 11);
        String strEdaban = this.lotNo.substring(11, 14);

        // 品質DB登録実績情報(登録済)取得
        int jissekiNoCnt = 1;
        for (String[] info : fxhdd03InfoList) {

            // 処理開始の実績Noまでは処理をしない
            if (Integer.parseInt(info[1]) < jissekiNo) {
                continue;
            }

            // 1回目の処理が対象の実績Noの処理で無かった場合ブレイク
            if (jissekiNoCnt == 1 && Integer.parseInt(info[1]) != jissekiNo) {
                break;
            }

            // 実績Noが連続で無くなったタイミングでブレイク
            if (jissekiNoCnt != Integer.parseInt(info[1]) - jissekiNo + 1) {
                break;
            }

            // 状態ﾌﾗｸﾞが"1"(登録済)以外の場合ブレイク
            if (!"1".equals(info[0])) {
                break;
            }

            // 電気特性から電気特性再検を取得
            String saiken = getSrDenkitokuseiesi(queryRunnerXHD, strKojyo, strLotNo, strEdaban, info[1], info[2], formId);
            // データが存在しない場合、エラー
            if (saiken == null) {
                messageList.add(MessageUtil.getMessage("XHD-000170"));
                return false;
            }

            // 再検査以外ループを抜ける
            if (!"再検査".equals(saiken)) {
                break;
            }

            jissekiNoCnt++;
        }

        // メニューを回数(実績No)分追加
        addMenuKaisu(menuListGXHDO101, jissekiNoCnt, formId, startJisskeino, mapMaxJissekiNo, fxhdd08InfoList, new ArrayList<>());

        return true;
    }

    /**
     * [電気特性]から、電気特性再検を取得(値が無い場合はNULLを返却)
     *
     * @param queryRunnerXHD queryRunnerオブジェクト(XHD)
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param rev revision
     * @param formId 画面ID
     * @return 電気特性再検
     * @throws SQLException 例外エラー
     */
    private String getSrDenkitokuseiesi(QueryRunner queryRunnerXHD, String kojyo, String lotNo,
            String edaban, String jissekino, String rev, String formId) throws SQLException {

        String sql = "SELECT saiken "
                + "FROM sr_denkitokuseiesi "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND kaisuu = ? AND revision = ? AND setubikubun = ?";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);
        params.add(rev);
        params.add(formId);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        Map resultMap = queryRunnerXHD.query(sql, new MapHandler(), params.toArray());
        if (resultMap == null) {
            return null;
        }

        return StringUtil.nullToBlank(resultMap.get("saiken"));
    }

    /**
     * 検査・外観検査メニュー 追加処理
     *
     * @param menuListGXHDO101 メニューリスト
     * @param messageList メッセージリスト
     * @param fxhdd03InfoList 実績情報リスト
     * @param startJisskeino 開始実績No
     * @param fxhdd08InfoList 画面制御情報リスト
     * @param mapMaxJissekiNo 最大実績NoMap
     * @param queryRunnerXHD queryRunnerオブジェクト(XHD)
     * @return true:追加あり、false：追加無し
     * @throws SQLException 例外エラー
     * @throws CloneNotSupportedException 例外エラー
     */
    private boolean addMenuKensaGaikanKensa(List<FXHDM01> menuListGXHDO101, List<String> messageList, List<String[]> fxhdd03InfoList,
            int startJisskeino, List<Map<String, Object>> fxhdd08InfoList, Map mapMaxJissekiNo, QueryRunner queryRunnerXHD) throws SQLException, CloneNotSupportedException {

        // 対象の検査・外観検査のメニューが存在しない場合
        FXHDM01 targetMenu = menuListGXHDO101.stream().filter(n -> FORM_ID_GAIKAN_KENSA.equals(n.getFormId())).filter(n -> startJisskeino == n.getJissekiNo()).findFirst().orElse(null);
        // 対象のメニューが存在しない場合
        if (targetMenu == null) {
            return false;
        }

        // 初回は実績Noが0で来るため、1か最大値+1をセット
        int jissekiNo;
        if (startJisskeino == 0) {
            // 初回は実績Noが0で来るため、1か最大値+1をセット
            jissekiNo = getMaxJissekiNo(mapMaxJissekiNo, FORM_ID_GAIKAN_KENSA, fxhdd08InfoList);

        } else {
            jissekiNo = startJisskeino;
        }

        // ﾛｯﾄNoを分割(チェック処理が実行されている前提の為、桁数の不足などは考慮しない)
        String strKojyo = this.lotNo.substring(0, 3);
        String strLotNo = this.lotNo.substring(3, 11);
        String strEdaban = this.lotNo.substring(11, 14);

        
        // 品質DB登録実績情報(登録済)取得
        int jissekiNoCnt = 1;
        List<String> gaikankensasyuruiList = new ArrayList<>();
        for (String[] info : fxhdd03InfoList) {
            // 処理開始の実績Noまでは処理をしない
            if (Integer.parseInt(info[1]) < jissekiNo) {
                continue;
            }

            // 1回目の処理が対象の実績Noの処理で無かった場合ブレイク
            if (jissekiNoCnt == 1 && Integer.parseInt(info[1]) != jissekiNo) {
                break;
            }

            // 実績Noが連続で無くなったタイミングでブレイク
            if (jissekiNoCnt != Integer.parseInt(info[1]) - jissekiNo + 1) {
                break;
            }

            // 状態ﾌﾗｸﾞが"1"(登録済)以外の場合ブレイク
            if (!"1".equals(info[0])) {
                break;
            }

            // QA外観抜き取り検査を取得
            String[] gaikan = getSrGaikankensaQAGaikan(queryRunnerXHD, strKojyo, strLotNo, strEdaban, info[1], info[2]);
            // データが存在しない場合、エラー
            if (gaikan == null) {
                messageList.add(MessageUtil.getMessage("XHD-000184", "検査・外観検査"));
                return false;
            }

            // NG以外ループを抜ける
            if (!"NG".equals(gaikan[0])) {
                break;
            }
            // 外観検査種類を保持
            gaikankensasyuruiList.add(gaikan[1]);
            jissekiNoCnt++;
        }

        // メニューを回数(実績No)分追加
        addMenuKaisu(menuListGXHDO101, jissekiNoCnt, FORM_ID_GAIKAN_KENSA, startJisskeino, mapMaxJissekiNo, fxhdd08InfoList, gaikankensasyuruiList);

        return true;
    }

    /**
     * 繰り返しメニュー追加処理
     *
     * @param menuListGXHDO101 メニューリスト
     * @param messageList メッセージリスト
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param queryRunnerXHD queryRunnerオブジェクト(XHD)
     * @param queryRunnerDoc queryRunnerオブジェクト(DocumentServer)
     * @throws SQLException 例外エラー
     * @throws CloneNotSupportedException 例外エラー
     */
    private void addRecurringMenu(List<FXHDM01> menuListGXHDO101, List<String> messageList,
            String kojyo, String lotNo, String edaban, QueryRunner queryRunnerXHD, QueryRunner queryRunnerDoc) throws SQLException, CloneNotSupportedException {

        // 工程別のメニュー名を保持
        Map<String, String> mapMenuName = new HashMap<>();
        for (FXHDM01 fxhdm01 : menuListGXHDO101) {
            mapMenuName.put(fxhdm01.getFormId(), fxhdm01.getMenuName());
        }

        // 実績NoのMAX値を保持するMAP
        Map mapMaxJissekiNo = new HashMap();

        // 複数回ループすることになるので動的メニューの実績を先に全て取得して置く
        Map<String, List<String[]>> mapFxhdd03Info = new HashMap<>();
        mapFxhdd03Info.put(FORM_ID_DENKITOKUSEI_ESI, loadFxhdd03InfoList(queryRunnerDoc, kojyo, lotNo, edaban, FORM_ID_DENKITOKUSEI_ESI));
        mapFxhdd03Info.put(FORM_ID_DENKITOKUSEI_3TANSHI_4TANSHI, loadFxhdd03InfoList(queryRunnerDoc, kojyo, lotNo, edaban, FORM_ID_DENKITOKUSEI_3TANSHI_4TANSHI));
        mapFxhdd03Info.put(FORM_ID_DENKITOKUSEI_IPPANHIN, loadFxhdd03InfoList(queryRunnerDoc, kojyo, lotNo, edaban, FORM_ID_DENKITOKUSEI_IPPANHIN));
        mapFxhdd03Info.put(FORM_ID_DENKITOKUSEI_NETSUSHORI, loadFxhdd03InfoList(queryRunnerDoc, kojyo, lotNo, edaban, FORM_ID_DENKITOKUSEI_NETSUSHORI));
        mapFxhdd03Info.put(FORM_ID_GAIKAN_KENSA, loadFxhdd03InfoList(queryRunnerDoc, kojyo, lotNo, edaban, FORM_ID_GAIKAN_KENSA));
        mapFxhdd03Info.put(FORM_ID_TP_CHECK, loadFxhdd03InfoList(queryRunnerDoc, kojyo, lotNo, edaban, FORM_ID_TP_CHECK));

        // 画面制御情報取得
        List<Map<String, Object>> fxhdd08Info = loadFxhdd08InfoList(queryRunnerDoc, kojyo, lotNo, edaban);
        Map addCountMap = new HashMap();
        int idx = 0;
        while (true) {

            if (menuListGXHDO101.size() <= idx) {
                break;
            }

            // 画面制御情報をもとにメニュー追加
            FXHDM01 fxhdm01 = menuListGXHDO101.get(idx);
            if (addInsertMenu(menuListGXHDO101, messageList, idx, fxhdd08Info, mapMenuName, mapMaxJissekiNo, mapFxhdd03Info, queryRunnerXHD, addCountMap)) {
                continue;
            }

            // 外観検査によるメニュー追加
            if (fxhdm01.getJissekiNo() == 0 && FORM_ID_GAIKAN_KENSA.equals(fxhdm01.getFormId())) {
                if (addMenuKensaGaikanKensa(menuListGXHDO101, messageList, mapFxhdd03Info.get(fxhdm01.getFormId()), fxhdm01.getJissekiNo(), fxhdd08Info, mapMaxJissekiNo, queryRunnerXHD)) {
                    continue;
                }
            }

            // 電気特性によるメニュー追加
            if (fxhdm01.getJissekiNo() == 0 && (FORM_ID_DENKITOKUSEI_ESI.equals(fxhdm01.getFormId()) || FORM_ID_DENKITOKUSEI_3TANSHI_4TANSHI.equals(fxhdm01.getFormId()) || FORM_ID_DENKITOKUSEI_IPPANHIN.equals(fxhdm01.getFormId()))) {
                if (addMenuDenkitokusei(menuListGXHDO101, messageList, mapFxhdd03Info.get(fxhdm01.getFormId()), fxhdm01.getFormId(), fxhdm01.getJissekiNo(), fxhdd08Info, mapMaxJissekiNo, queryRunnerXHD)) {
                    continue;
                }
            }

            // TPﾁｪｯｸによるメニュー追加
            if (fxhdm01.getJissekiNo() == 0 && FORM_ID_TP_CHECK.equals(fxhdm01.getFormId())) {
                addMenuTpCheck(menuListGXHDO101, idx, fxhdm01.getJissekiNo(), messageList, queryRunnerXHD, mapFxhdd03Info.get(fxhdm01.getFormId()), mapMaxJissekiNo, addCountMap, mapMenuName, fxhdd08Info);
                continue;
            }

            // その他メニューの実績No割り当て処理
            if (fxhdm01.getJissekiNo() == 0) {
                // 番号の割り当て
                fxhdm01.setJissekiNo(getJissekino(mapMaxJissekiNo, menuListGXHDO101, fxhdm01.getFormId(), fxhdd08Info));
                continue;
            }

            idx++;
        }

    }

    /**
     * 画面制御情報をもとにしたメニュー追加
     *
     * @param menuListGXHDO101 メニューリスト
     * @param messageList メッセージリスト
     * @param menuIdx メニュー位置
     * @param fxhdd08InfoList 画面制御情報リスト
     * @param mapMenuName メニュー名Map
     * @param mapMaxJissekiNo 最大実績NoMap
     * @param mapFxhdd03Info 実績情報Map
     * @param queryRunnerXHD queryRunnerオブジェクト(XHD)
     * @param addCountMap 追加回数Map
     * @return true：追加あり、false:追加無し
     * @throws CloneNotSupportedException 例外エラー
     * @throws SQLException 例外エラー
     */
    private boolean addInsertMenu(List<FXHDM01> menuListGXHDO101, List<String> messageList, int menuIdx, List<Map<String, Object>> fxhdd08InfoList,
            Map<String, String> mapMenuName, Map mapMaxJissekiNo, Map<String, List<String[]>> mapFxhdd03Info, QueryRunner queryRunnerXHD, Map addCountMap) throws CloneNotSupportedException, SQLException {

        // 対象のメニューを取得
        FXHDM01 fxhdm01 = menuListGXHDO101.get(menuIdx);
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

        // 追加対象のメニューを取得
        FXHDM01 targetMenu = menuListGXHDO101.stream().filter(n -> n.getFormId().equals(gamenId)).findFirst().orElse(null);
        if (targetMenu == null) {
            // 追加対象が無い場合はfalseをリターン
            return false;
        }

        FXHDM01 addMenu = targetMenu.clone();
        addMenu.setMenuName(StringUtil.nullToBlank(mapMenuName.get(gamenId)));
        addMenu.setJissekiNo(jissekino);
        addMenu.setDeleteBtnRender(this.addLinkRender);
        if (fxhdd08Info.get("koshin_date") != null) {
            addMenu.setKoshinDateFxhdd08((Timestamp) fxhdd08Info.get("koshin_date"));
        }
        menuListGXHDO101.add(menuIdx + 1, addMenu);

        // 最大の実績Noを保持
        if (!mapMaxJissekiNo.containsKey(gamenId) || (int) mapMaxJissekiNo.get(gamenId) < jissekino) {
            mapMaxJissekiNo.put(gamenId, jissekino);
        }

        // メニュー追加
        addDynamicMenu(addMenu, menuListGXHDO101, messageList, mapFxhdd03Info, fxhdd08InfoList, mapMaxJissekiNo, queryRunnerXHD, menuIdx + 1, mapMenuName, addCountMap);

        return true;
    }

    /**
     * 動的メニュー追加
     *
     * @param addMenu 追加メニュー
     * @param menuListGXHDO101 メニューリスト
     * @param messageList メッセージリスト
     * @param mapFxhdd03Info 実績情報Map
     * @param fxhdd08InfoList 画面制御情報リスト
     * @param mapMaxJissekiNo 最大実績NoMap
     * @param queryRunnerXHD queryRunnerオブジェクト(XHD)
     * @param menuIdx メニュー位置
     * @param mapMenuName メニュー名Map
     * @param addCountMap 追加回数Map
     * @throws SQLException 例外エラー
     * @throws CloneNotSupportedException 例外エラー
     */
    private void addDynamicMenu(FXHDM01 addMenu, List<FXHDM01> menuListGXHDO101, List<String> messageList, Map<String, List<String[]>> mapFxhdd03Info, List<Map<String, Object>> fxhdd08InfoList, Map mapMaxJissekiNo, QueryRunner queryRunnerXHD, int menuIdx, Map mapMenuName, Map addCountMap) throws SQLException, CloneNotSupportedException {
        switch (addMenu.getFormId()) {
            case FORM_ID_DENKITOKUSEI_ESI:             // 電気特性・ESI
            case FORM_ID_DENKITOKUSEI_3TANSHI_4TANSHI: // 電気特性・3端子・4端子
            case FORM_ID_DENKITOKUSEI_IPPANHIN:        // 電気特性・一般品
                addMenuDenkitokusei(menuListGXHDO101, messageList, mapFxhdd03Info.get(addMenu.getFormId()), addMenu.getFormId(), addMenu.getJissekiNo(), fxhdd08InfoList, mapMaxJissekiNo, queryRunnerXHD);
                break;
            case FORM_ID_GAIKAN_KENSA: //外観検査
                addMenuKensaGaikanKensa(menuListGXHDO101, messageList, mapFxhdd03Info.get(addMenu.getFormId()), addMenu.getJissekiNo(), fxhdd08InfoList, mapMaxJissekiNo, queryRunnerXHD);
                break;
            case FORM_ID_TP_CHECK: //TPチェック
                addMenuTpCheck(menuListGXHDO101, menuIdx, addMenu.getJissekiNo(), messageList, queryRunnerXHD, mapFxhdd03Info.get(addMenu.getFormId()), mapMaxJissekiNo, addCountMap, mapMenuName, fxhdd08InfoList);
                break;
        }
    }

    /**
     * TPﾁｪｯｸメニュー 追加処理
     *
     * @param menuListGXHDO101 メニューリスト
     * @param menuIdx メニュー位置
     * @param startJisskeino 開始実績No
     * @param messageList メッセージリスト
     * @param queryRunnerXHD queryRunnerオブジェクト(XHD)
     * @param fxhdd03InfoList 実績情報リスト
     * @param mapMaxJissekiNo 最大実績NoMap
     * @param addCountMap 追加回数Map
     * @param mapMenuName メニュー名Map
     * @param fxhdd08InfoList 画面制御情報リスト
     * @throws SQLException 例外エラー
     * @throws CloneNotSupportedException 例外エラー
     */
    private void addMenuTpCheck(List<FXHDM01> menuListGXHDO101, int menuIdx, int startJisskeino, List<String> messageList, QueryRunner queryRunnerXHD,
            List<String[]> fxhdd03InfoList, Map mapMaxJissekiNo, Map addCountMap, Map<String, String> mapMenuName, List<Map<String, Object>> fxhdd08InfoList) throws SQLException, CloneNotSupportedException {

        int jissekiNo;
        if (startJisskeino == 0) {
            // 初回は実績Noが0で来るため、1か最大値+1をセット
            jissekiNo = getMaxJissekiNo(mapMaxJissekiNo, FORM_ID_TP_CHECK, fxhdd08InfoList);

            // 実績Noをｾｯﾄ
            menuListGXHDO101.get(menuIdx).setJissekiNo(jissekiNo);

        } else {
            jissekiNo = startJisskeino;
        }

        if (!mapMaxJissekiNo.containsKey(FORM_ID_TP_CHECK) || (int) mapMaxJissekiNo.get(FORM_ID_TP_CHECK) < jissekiNo) {
            mapMaxJissekiNo.put(FORM_ID_TP_CHECK, jissekiNo);
        }

        // ﾛｯﾄNoを分割(チェック処理が実行されている前提の為、桁数の不足などは考慮しない)
        String kojyo = this.lotNo.substring(0, 3);
        String lotno = this.lotNo.substring(3, 11);
        String edaban = this.lotNo.substring(11, 14);

        // 品質DB登録実績情報(登録済)取得
        String[] info = null;
        for (String[] jissekiInfo : fxhdd03InfoList) {
            if (Integer.parseInt(jissekiInfo[1]) == jissekiNo) {
                info = jissekiInfo;
                break;
            }
        }

        if (info == null || !"1".equals(info[0])) {
            return;
        }

        // TPﾁｪｯｸより再検査かどうかを取得
        String[] kensaKekka = getSrTapingCheckKensaKekka(queryRunnerXHD, kojyo, lotno, edaban, info[1], info[2]);

        if (kensaKekka == null) {
            // 実績が取得できなかった場合リターン
            messageList.add(MessageUtil.getMessage("XHD-000184", "TP・TPﾁｪｯｸ"));
            return;
        }

        int addCount = 0;
        if (addCountMap.containsKey(FORM_ID_TP_CHECK)) {
            addCount = (int) addCountMap.get(FORM_ID_TP_CHECK);
        }
        addCount++;

        int menuAddIdx = menuIdx;
        // 電気特性再検査ありの場合
        if ("あり".equals(kensaKekka[0])) {

            if (!addMenuTpCheckInsMenu(menuListGXHDO101, FORM_ID_DENKITOKUSEI_ESI, ++menuAddIdx, 0, mapMenuName, addCount)) {
                menuAddIdx--;
            }

            if (!addMenuTpCheckInsMenu(menuListGXHDO101, FORM_ID_DENKITOKUSEI_3TANSHI_4TANSHI, ++menuAddIdx, 0, mapMenuName, addCount)) {
                menuAddIdx--;
            }

            if (!addMenuTpCheckInsMenu(menuListGXHDO101, FORM_ID_DENKITOKUSEI_IPPANHIN, ++menuAddIdx, 0, mapMenuName, addCount)) {
                menuAddIdx--;
            }

        }

        // 外観検査再検査ありの場合
        if ("あり".equals(kensaKekka[1])) {
            if (!addMenuTpCheckInsMenu(menuListGXHDO101, FORM_ID_GAIKAN_KENSA, ++menuAddIdx, 0, mapMenuName, addCount)) {
                menuAddIdx--;
            }
        }

        // 上記でメニューが追加された場合
        if (menuIdx < menuAddIdx) {
            if (!addMenuTpCheckInsMenu(menuListGXHDO101, FORM_ID_TP_SAGYO, ++menuAddIdx, 0, mapMenuName, addCount)) {
                menuAddIdx--;
            }
            if (!addMenuTpCheckInsMenu(menuListGXHDO101, FORM_ID_TP_CHECK, ++menuAddIdx, 0, mapMenuName, addCount)) {
                menuAddIdx--;
            }
            addCountMap.put(FORM_ID_TP_CHECK, addCount);
        }

    }

    /**
     * TPﾁｪｯｸ 追加メニュー
     *
     * @param menuListGXHDO101 メニューリスト
     * @param formId 画面ID
     * @param addIdx 追加位置
     * @param jissekino 実績No
     * @param mapMenuName メニュー名Map
     * @param addCount 追加回数
     * @return true:追加 fasle：追加無し
     * @throws CloneNotSupportedException
     */
    private boolean addMenuTpCheckInsMenu(List<FXHDM01> menuListGXHDO101, String formId, int addIdx, int jissekino, Map<String, String> mapMenuName, int addCount) throws CloneNotSupportedException {
        FXHDM01 baseMenu = menuListGXHDO101.stream().filter(n -> formId.equals(n.getFormId())).findFirst().orElse(null);
        if (baseMenu == null) {
            return false;
        }
        String menuName = mapMenuName.get(formId);
        FXHDM01 addMenu = baseMenu.clone();
        
        if(FORM_ID_GAIKAN_KENSA.equals(formId)){
            menuName = "検査(外観検査4次)";
            addMenu.setGaikankensasyurui("4");
        }

        addMenu.setMenuName(menuName + "(追加" + addCount + "回目)");
        addMenu.setJissekiNo(jissekino);
        addMenu.setDeleteBtnRender(false);

        menuListGXHDO101.add(addIdx, addMenu);

        return true;
    }

    /**
     * [検査・外観検査]から、QA外観抜き取り検査を取得(値が無い場合はNULLを返却)
     *
     * @param queryRunnerXHD QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param jissekino 実績No(検索キー)
     * @param rev revision(検索キー)
     * @return QA外観抜き取り検査(QAgaikannukitorikensa)
     * @throws SQLException 例外エラー
     */
    private String[] getSrGaikankensaQAGaikan(QueryRunner queryRunnerXHD, String kojyo, String lotNo,
            String edaban, String jissekino, String rev) throws SQLException {

        String sql = "SELECT QAgaikannukitorikensa,gaikankensasyurui "
                + "FROM sr_gaikankensa "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND kaisuu = ? AND revision = ? ";

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

        return new String[] {StringUtil.nullToBlank(resultMap.get("QAgaikannukitorikensa")), StringUtil.nullToBlank(resultMap.get("gaikankensasyurui"))};
    }

    /**
     * [出荷検査判定]から総合判定を取得
     *
     * @param queryRunnerXHD QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @return 総合判定
     * @throws SQLException 例外エラー
     */
    private String getSogoHantei(QueryRunner queryRunnerXHD, String kojyo, String lotNo,
            String edaban) throws SQLException {

        // 出荷検査判定情報の取得
        String sql = "SELECT sogohantei "
                + "FROM sr_sqchantei "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? "
                + "ORDER BY jissekino desc"
                ;

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        Map map = queryRunnerXHD.query(sql, new MapHandler(), params.toArray());
        if (map != null && !map.isEmpty()) {
            return String.valueOf(map.get("sogohantei"));
        }

        return "";
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
     * [TPﾁｪｯｸ]から、再検査情報を取得(値が無い場合はNULLを返却)
     *
     * @param queryRunnerXHD QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param jissekino 実績No(検索キー)
     * @param rev revision(検索キー)
     * @return 検査値(電気特性再検査,外観再検査)
     * @throws SQLException 例外エラー
     */
    private String[] getSrTapingCheckKensaKekka(QueryRunner queryRunnerXHD, String kojyo, String lotNo,
            String edaban, String jissekino, String rev) throws SQLException {

        String sql = "SELECT denkitokuseisaikensa,gaikansaikensa "
                + "FROM sr_taping_check "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND kaisuu = ? AND revision = ? ";

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

        return new String[]{StringUtil.nullToBlank(resultMap.get("denkitokuseisaikensa")), StringUtil.nullToBlank(resultMap.get("gaikansaikensa"))};
    }

    /**
     * 削除ﾁｪｯｸ
     *
     * @param itemInfo メニュー情報
     */
    public void checkDeleteMenu(FXHDM01 itemInfo) {

        try {

            QueryRunner queryRunnerDoc = new QueryRunner(dataSourceDocServer);
            String strKojyo = this.searchLotNo.substring(0, 3);
            String strLotNo = this.searchLotNo.substring(3, 11);
            String strEdaban = this.searchLotNo.substring(11, 14);
            if (existJisseki(queryRunnerDoc, strKojyo, strLotNo, strEdaban, itemInfo.getFormId(), itemInfo.getJissekiNo())) {
                this.warnMessage = MessageUtil.getMessage("XHD-000191", itemInfo.getMenuName());
                this.deleteMenuInfo = itemInfo;
                RequestContext context = RequestContext.getCurrentInstance();
                context.addCallbackParam("firstParam", "deleteConf");
                return;
            }

            this.deleteMenuInfo = itemInfo;
            // メニュー削除呼出
            deleteMenu();

        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("メニュー削除エラー", ex, LOGGER);
        }

    }

    /**
     * メニュー削除処理
     */
    public void deleteMenu() {
        QueryRunner queryRunnerDoc = new QueryRunner(dataSourceDocServer);

        Connection conDoc = null;

        String strKojyo = this.searchLotNo.substring(0, 3);
        String strLotNo = this.searchLotNo.substring(3, 11);
        String strEdaban = this.searchLotNo.substring(11, 14);

        try {
            // トランザクション開始
            conDoc = DBUtil.transactionStart(queryRunnerDoc.getDataSource().getConnection());

            Timestamp systemTime = new Timestamp(System.currentTimeMillis());

            Map<String, Object> delFxhdd08Info = loadFxhdd08Info(queryRunnerDoc, conDoc, strKojyo, strLotNo, strEdaban, this.deleteMenuInfo.getFormId(), this.deleteMenuInfo.getJissekiNo(), 0);
            if (delFxhdd08Info == null || delFxhdd08Info.isEmpty() || this.deleteMenuInfo.getKoshinDateFxhdd08().before((Timestamp) delFxhdd08Info.get("koshin_date"))) {
                FacesMessage message
                        = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000194"), null);
                FacesContext.getCurrentInstance().addMessage(null, message);

                // ロールバックしてリターン
                DBUtil.rollbackConnection(conDoc, LOGGER);
                return;
            }

            int maxDeleteFlag = getMaxDeleteFlagFxhdd08(queryRunnerDoc, strKojyo, strLotNo, strEdaban, this.deleteMenuInfo.getFormId(), this.deleteMenuInfo.getJissekiNo()) + 1;
            deleteFxhdd08(queryRunnerDoc, conDoc, strKojyo, strLotNo, strEdaban, StringUtil.nullToBlank(delFxhdd08Info.get("gamen_id")), (int) delFxhdd08Info.get("jissekino"), maxDeleteFlag, this.searchTantoshaCd, systemTime);

            Map<String, Object> delFxhdd08InfoAto = loadFxhdd08InfoAtoKotei(queryRunnerDoc, conDoc, strKojyo, strLotNo, strEdaban, this.deleteMenuInfo.getFormId(), this.deleteMenuInfo.getJissekiNo(), 0);
            if (delFxhdd08InfoAto != null && !delFxhdd08InfoAto.isEmpty()) {

                updateFxhdd08AtoKotei(queryRunnerDoc, conDoc, strKojyo, strLotNo, strEdaban,
                        StringUtil.nullToBlank(delFxhdd08Info.get("maekoutei_gamen_id")), (int) delFxhdd08Info.get("maekoutei_jissekino"),
                        StringUtil.nullToBlank(delFxhdd08Info.get("gamen_id")), (int) delFxhdd08Info.get("jissekino"), this.searchTantoshaCd, systemTime);
            }

            DbUtils.commitAndCloseQuietly(conDoc);

            // メニューの再検索
            reSearchHinshitsuData(false);

        } catch (SQLException e) {

            DBUtil.rollbackConnection(conDoc, LOGGER);
            ErrUtil.outputErrorLog("SQLException発生", e, LOGGER);

        } catch (Exception e) {
            DBUtil.rollbackConnection(conDoc, LOGGER);
            ErrUtil.outputErrorLog("Exception発生", e, LOGGER);
        }

    }

    /**
     * [品質DB登録実績]から、実績の有無を取得
     *
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param formId 画面ID(検索キー)
     * @param jissekino 画面ID(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private boolean existJisseki(QueryRunner queryRunnerDoc, String kojyo, String lotNo,
            String edaban, String formId, int jissekino) throws SQLException {

        // 品質DB登録実績情報の取得
        String sql = "SELECT jotai_flg "
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
        List<Map<String, Object>> list = queryRunnerDoc.query(sql, new MapListHandler(), params.toArray());
        if (list != null && !list.isEmpty()) {
            String jotai = StringUtil.nullToBlank(list.get(0).get("jotai_flg"));
            if ("1".equals(jotai) || "0".equals(jotai)) {
                // 実績あり
                return true;
            }
        }

        // 実績なし
        return false;
    }

    /**
     * [画面制御]から情報を取得
     *
     * @param queryRunnerDoc queryRunnerオブジェクト
     * @param conDoc コネクション
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param formId 画面ID
     * @param jissekino 実績No
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @return 画面制御情報
     * @throws SQLException 例外エラー
     */
    private Map<String, Object> loadFxhdd08Info(QueryRunner queryRunnerDoc, Connection conDoc, String kojyo, String lotNo,
            String edaban, String formId, int jissekino, int deleteflag) throws SQLException {

        // 品質DB登録実績情報の取得
        String sql = "SELECT gamen_id, jissekino,maekoutei_gamen_id, maekoutei_jissekino, koshin_date "
                + "FROM fxhdd08 "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? "
                + "AND gamen_id = ?  AND jissekino = ? AND deleteflag = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(formId);
        params.add(jissekino);
        params.add(deleteflag);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerDoc.query(conDoc, sql, new MapHandler(), params.toArray());

    }

    /**
     * [画面制御]から情報を取得
     *
     * @param queryRunnerDoc queryRunnerオブジェクト
     * @param conDoc コネクション
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param formId 画面ID
     * @param jissekino 実績No
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @return 画面制御情報
     * @throws SQLException 例外エラー
     */
    private Map<String, Object> loadFxhdd08InfoAtoKotei(QueryRunner queryRunnerDoc, Connection conDoc, String kojyo, String lotNo,
            String edaban, String formId, int jissekino, int deleteflag) throws SQLException {

        // 品質DB登録実績情報の取得
        String sql = "SELECT koshin_date "
                + "FROM fxhdd08 "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? "
                + "AND maekoutei_gamen_id = ?  AND maekoutei_jissekino = ? AND deleteflag = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(formId);
        params.add(jissekino);
        params.add(deleteflag);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerDoc.query(conDoc, sql, new MapHandler(), params.toArray());

    }

    /**
     * [画面制御]から削除ﾌﾗｸﾞ(最大)を取得
     *
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param formId 画面ID(検索キー)
     * @param jissekino 実績No(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private int getMaxDeleteFlagFxhdd08(QueryRunner queryRunnerDoc, String kojyo, String lotNo,
            String edaban, String formId, int jissekino) throws SQLException {

        // 品質DB登録実績情報の取得
        String sql = "SELECT MAX(deleteflag) AS deleteflag "
                + "FROM fxhdd08 "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? "
                + "AND gamen_id = ?  AND jissekino = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(formId);
        params.add(jissekino);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        Map<String, Object> map = queryRunnerDoc.query(sql, new MapHandler(), params.toArray());

        int deleteflag = 0;
        if (map != null && !map.isEmpty() && map.get("deleteflag") != null) {
            deleteflag = (int) map.get("deleteflag");
        }

        return deleteflag;

    }

    /**
     * 画面制御更新処理
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト(DocmentServer)
     * @param conDoc コネクション(DocmentServer)
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param newMaekouteiGamenId 画面ID
     * @param newMaekouteiJissekino 実績No
     * @param maekouteiGamenId 画面ID(前工程)
     * @param maekouteiJissekino 実績No(前工程)
     * @param tantoshaCd 担当者ｺｰﾄﾞ
     * @param systemTime システム日付
     * @throws SQLException 例外エラー
     */
    private void updateFxhdd08AtoKotei(QueryRunner queryRunnerDoc, Connection conDoc, String kojyo, String lotNo, String edaban,
            String newMaekouteiGamenId, int newMaekouteiJissekino, String maekouteiGamenId, int maekouteiJissekino,
            String tantoshaCd, Timestamp systemTime) throws SQLException {
        String sql = "UPDATE fxhdd08 SET "
                + "maekoutei_gamen_id = ?, maekoutei_jissekino = ?, "
                + "koshin_id = ?, koshin_date = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? "
                + "  AND maekoutei_gamen_id = ? AND  maekoutei_jissekino = ? "
                + "  AND deleteflag = 0 ";

        List<Object> params = new ArrayList<>();
        // 更新内容
        params.add(newMaekouteiGamenId); //前工程画面ID(NEW)
        params.add(newMaekouteiJissekino); //前工程実績No(NEW)
        params.add(tantoshaCd); //更新者
        params.add(systemTime); //更新日

        // 検索条件
        params.add(kojyo); //工場ｺｰﾄﾞ
        params.add(lotNo); //ﾛｯﾄNo
        params.add(edaban); //枝番
        params.add(maekouteiGamenId); //前工程画面ID
        params.add(maekouteiJissekino); //前工程実績No

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerDoc.update(conDoc, sql, params.toArray());
    }

    /**
     * 画面制御更新処理
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト(DocmentServer)
     * @param conDoc コネクション(DocmentServer)
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param gamenId 画面ID
     * @param jissekino 実績No
     * @param maxDeleteflag 削除ﾌﾗｸﾞ最大値
     * @param tantoshaCd 担当者ｺｰﾄﾞ
     * @param systemTime システム日付
     * @throws SQLException 例外エラー
     */
    private void deleteFxhdd08(QueryRunner queryRunnerDoc, Connection conDoc, String kojyo, String lotNo, String edaban,
            String gamenId, int jissekino, int maxDeleteflag,
            String tantoshaCd, Timestamp systemTime) throws SQLException {
        String sql = "UPDATE fxhdd08 SET "
                + "deleteflag = ?, "
                + "koshin_id = ?, koshin_date = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? "
                + "  AND gamen_id = ? AND  jissekino = ? "
                + "  AND deleteflag = 0 ";

        List<Object> params = new ArrayList<>();
        // 更新内容
        params.add(maxDeleteflag); //削除ﾌﾗｸﾞ
        params.add(tantoshaCd); //更新者
        params.add(systemTime); //更新日

        // 検索条件
        params.add(kojyo); //工場ｺｰﾄﾞ
        params.add(lotNo); //ﾛｯﾄNo
        params.add(edaban); //枝番
        params.add(gamenId); //画面ID
        params.add(jissekino); //実績No

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerDoc.update(conDoc, sql, params.toArray());
    }

    /**
     * メニュー追加ﾁｪｯｸ
     *
     * @param itemInfo メニュー情報
     */
    public void checkAddMenu(FXHDM01 itemInfo) {

        if (!"1".equals(itemInfo.getJotaiFlg())) {
            FacesMessage message
                    = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000189"), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }

        FXHDM01 atoKoteiMenuInfo = null; // 後工程情報
        boolean existKotei = false;
        for (FXHDM01 fxhdm01 : this.menuListGXHDO101Nofiltering) {
            if (existKotei) {
                //後工程情報を保持
                atoKoteiMenuInfo = fxhdm01;
                break;
            }

            //該当情報が見つかった場合
            if (fxhdm01.getFormId().equals(itemInfo.getFormId()) && fxhdm01.getJissekiNo() == itemInfo.getJissekiNo()) {
                existKotei = true;
            }
        }

        if (atoKoteiMenuInfo != null && ("0".equals(atoKoteiMenuInfo.getJotaiFlg()) ||  "1".equals(atoKoteiMenuInfo.getJotaiFlg())) ) {
            FacesMessage message
                    = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000190"), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }

        QueryRunner queryRunnerDoc = new QueryRunner(dataSourceDocServer);
        String param = loadParamData(queryRunnerDoc, "common_user", "xhd_条件_追加画面ID");
        if (StringUtil.isEmpty(param)) {
            FacesMessage message
                    = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000192"), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }

        List<FXHDM01> selectMenuList = getSelectMenuList(queryRunnerDoc, Arrays.asList(param.split(",")));
        if (selectMenuList.isEmpty()) {
            FacesMessage message
                    = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000031"), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }

        List<String> menuList = new ArrayList<>();
        for (FXHDM01 fxhdm01 : selectMenuList) {
            if(FORM_ID_GAIKAN_KENSA.equals(fxhdm01.getFormId())){
                menuList.add("検査(外観検査4次)");
                continue;
            }
            menuList.add(fxhdm01.getFormTitle());
        }

        // サブ画面情報の受け渡し
        GXHDO101C017 beanGXHDO101C017 = (GXHDO101C017) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C017);
        beanGXHDO101C017.setMenuList(selectMenuList);
        beanGXHDO101C017.setMenuListName(menuList);
        beanGXHDO101C017.setInsPositionInfoMenu(itemInfo);
        beanGXHDO101C017.setLotno(this.searchLotNo);
        beanGXHDO101C017.setTantoushaCd(this.searchTantoshaCd);
        beanGXHDO101C017.setAtoKoteiMenu(atoKoteiMenuInfo);

        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("firstParam", "gxhdo101c017");
    }

    /**
     * [ﾊﾟﾗﾒｰﾀﾏｽﾀ]から、データを取得
     *
     * @param queryRunnerDoc オブジェクト
     * @param userName ユーザー名
     * @param key Key
     * @return 取得データ
     */
    private String loadParamData(QueryRunner queryRunnerDoc, String userName, String key) {
        try {

            // ﾊﾟﾗﾒｰﾀﾏｽﾀデータの取得
            String sql = "SELECT data "
                    + " FROM fxhbm03 "
                    + " WHERE user_name = ? AND key = ? ";

            List<Object> params = new ArrayList<>();
            params.add(userName);
            params.add(key);
            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            Map data = queryRunnerDoc.query(sql, new MapHandler(), params.toArray());
            if (data != null && !data.isEmpty()) {
                return StringUtil.nullToBlank(data.get("data"));
            }

        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
        }
        return null;

    }

    /**
     * メニューリスト取得
     *
     * @param queryRunnerDoc データオブジェクト
     * @param listGamenID 画面IDリスト
     * @return メニューリスト
     * @throws SQLException 例外エラー
     */
    private List<FXHDM01> getSelectMenuList(QueryRunner queryRunnerDoc, List<String> listGamenID) {
        try {
            String sql = "SELECT gamen_id,gamen_title "
                    + "FROM fxhdm01 WHERE  ";
            sql += DBUtil.getInConditionPreparedStatement("gamen_id", listGamenID.size());

            sql += " GROUP BY gamen_id,gamen_title ";
            sql += " ORDER BY MIN(menu_no) ASC ";

            Map<String, String> mapping = new HashMap<>();
            mapping.put("gamen_id", "formId");
            mapping.put("gamen_title", "formTitle");

            BeanProcessor beanProcessor = new BeanProcessor(mapping);
            RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
            ResultSetHandler<List<FXHDM01>> beanHandler = new BeanListHandler<>(FXHDM01.class, rowProcessor);

            List<Object> params = new ArrayList<>();
            params.addAll(listGamenID);

            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            return queryRunnerDoc.query(sql, beanHandler, params.toArray());

        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
        }

        return new ArrayList<>();
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

}
