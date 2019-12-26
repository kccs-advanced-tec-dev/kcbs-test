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
    private static final String FORM_ID_DENKITOKUSEI_ESI = "GXHDO101B040";
    private static final String FORM_ID_DENKITOKUSEI_3TANSHI_4TANSHI = "GXHDO101B041";
    private static final String FORM_ID_DENKITOKUSEI_IPPANHIN = "GXHDO101B042";
    
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
    
    /** パラメータマスタ操作 */
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
            setSanshouBtnRender(false);

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
            addMenuGaibuDenkyokuMekkiHinshitsuKensa(this.menuListGXHDO101, this.menuListGXHDO101Nofiltering, messageListMain, menuNameGaibuDenkyokuMekki,queryRunnerXHD, queryRunnerDoc);
            
            // メニューに実績Noを設定
            setMenuJisekiNo(this.menuListGXHDO101);// 権限絞り込みありメニュー
            setMenuJisekiNo(this.menuListGXHDO101Nofiltering);// 権限絞り込みメニュー

            // 実績状態を表示(権限絞り込みありメニューのみ)
            setJissekiJotai(this.menuListGXHDO101, queryRunnerDoc, strKojyo, strLotNo, strEdaban);

            // 画面IDチェック(権限絞り込みなしのメニューでチェック)
            checkMenuFormId(messageListMain, this.menuListGXHDO101Nofiltering);

            // チェックメッセージの表示
            if (!messageListMain.isEmpty()) {

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
            this.searchLotNo = this.lotNo;
            this.searchTantoshaCd = this.tantoshaCd;

            //⑭ﾛｯﾄ参照ﾎﾞﾀﾝ表示/非表示
            //  以下の2つの条件の両方を満たす場合、【ﾛｯﾄ参照】ﾎﾞﾀﾝを画面に表示する。また、【印刷工程画面ID】を決定する。
            //  1.印刷工程の画面IDが以下のいずれかの画面IDであること。(【画面IDﾘｽﾄ】に以下の画面IDが存在すること)
            // 画面IDﾌｨﾙﾀﾘﾝｸﾞ
            List<String> filterGamenList = new ArrayList<>();
            filterGamenList.add("GXHDO101B001");
            filterGamenList.add("GXHDO101B002");
            boolean gamenExistFlg = false;
            gamenExistFlg = lotSanshouMenuListFiltering(this.menuListGXHDO101, filterGamenList);
            
            //ﾛｯﾄ参照ﾎﾞﾀﾝ表示/非表示の設定
            if(gamenExistFlg && userGrpList.contains("ﾛｯﾄ参照_利用ﾕｰｻﾞｰ")){
                setSanshouBtnRender(true);
            }else{
                setSanshouBtnRender(false);
            }
            
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
        menuListGXHDO101 = getMenuListGXHDO101();
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

            if(!"".equals(searchLotNo) && searchLotNo != null){
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
            if(fxhdd03JissekinoInfo == null){
                setErrorMessage(MessageUtil.getMessage("XHD-000160",getMenuName(this.menuListGXHDO101, getInsatsuKoteiGamenID())));
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
            if(!"1".equals(jotaiFlg)){
                setErrorMessage(MessageUtil.getMessage("XHD-000160",getMenuName(this.menuListGXHDO101, getInsatsuKoteiGamenID())));
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, getErrorMessage(), null);
                facesContext.addMessage(null, message);
                return;
            }

            //  ②【印刷工程画面ID】をもとに、対象ﾃｰﾌﾞﾙからﾃﾞｰﾀを取得する。
            //   1.【印刷工程画面ID】 == "GXHDO101B001" の場合
            String jissekiInforev = StringUtil.nullToBlank(getMapData(fxhdd03JissekinoInfo, "rev"));
            if("GXHDO101B001".equals(getInsatsuKoteiGamenID())){
                //    A.Ⅲ.画面表示仕様(27),(28)を発行する。
                Map srSpsprintGraInfo = loadSrSpsprintGraInfo(queryRunnerQcdb, strKojyo, strLotNo, strEdaban, jissekiInforev);
                Map subSrSpsprintGraInfo = loadSubSrSpsprintGraInfo(queryRunnerQcdb, strKojyo, strLotNo, strEdaban, jissekiInforev);
                if(srSpsprintGraInfo == null || subSrSpsprintGraInfo == null){
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
                if(srSpsprintInfo == null || subSrSpsprintScrInfo == null){
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
            if(gamenInfo != null){
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
     * 前工程画面ID、ﾒﾆｭｰ名取得
     *
     * @param gamenIdList 画面IDリスト
     * @param gamenId 画面ID
     * @return 前工程画面ID
     */
    private String[] getMaeKoteiGamenInfo(List<String[]> gamenIdList, String gamenId) {
        // 画面IDリストから指定の画面IDの一つ前の画面IDを取得
        String[] maeKoteiInfo = null;
        for (String[] compGamenInfo : gamenIdList) {
            if (gamenId.equals(compGamenInfo[0])) {
                return maeKoteiInfo;
            }
            maeKoteiInfo = compGamenInfo;
        }
        return maeKoteiInfo;
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
     * @return メニューを追加出来た場合:true
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
     * メニュー 追加処理(回数指定)
     *
     * @param menuListGXHDO101 メニューリスト
     * @param kaisu 回数
     * @param menuName メニュー名
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
                menuListGXHDO101.add(i + saisankaIdx, addData);
            }
            // メニューリストの名称と実績Noを設定
            menuListGXHDO101.get(i + saisankaIdx).setMenuName(menuName + "(" + (i + 1) + "回目)");
        }
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
        int dataCount = 0;
        boolean existFlg = false;
        if (!menuList.isEmpty()) {
            for (FXHDM01 data : menuList) {
                if (filterGamenIdList.contains(data.getFormId())) {
                    //【印刷工程画面ID】を決定する。
                    setInsatsuKoteiGamenID(data.getFormId());
                    dataCount++;
                    existFlg = true;
                }
            }
        }
        if(dataCount > 1){
            setInsatsuKoteiGamenID("GXHDO101B001");            
        }
        return existFlg;
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
      * @param formId 画面ID
      * @param session セッション情報
      * @throws SQLException 例外エラー
      */
    private void setDenkitokuseiSessionData(String formId, HttpSession session) throws SQLException {
        
        if(!FORM_ID_DENKITOKUSEI_ESI.equals(formId) && !FORM_ID_DENKITOKUSEI_3TANSHI_4TANSHI.equals(formId) && !FORM_ID_DENKITOKUSEI_IPPANHIN.equals(formId)){
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

    }
}
