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
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import jp.co.kccs.xhd.db.model.FXHDM01;
import jp.co.kccs.xhd.util.DBUtil;
import jp.co.kccs.xhd.util.ErrUtil;
import jp.co.kccs.xhd.util.MessageUtil;
import jp.co.kccs.xhd.util.StringUtil;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.lang3.math.NumberUtils;

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
@Named
@ViewScoped
public class GXHDO101A implements Serializable {
    private static final Logger LOGGER = Logger.getLogger(GXHDO101A.class.getName());
    private static final String CHARSET = "MS932";
    private static final int LOTNO_BYTE = 14;
    
    // ロットチェック用ウェイト(1桁目)
    private static final BigDecimal LOT_CHECK_WEIGHT_1 = BigDecimal.valueOf(9);
    // ロットチェック用ウェイト(2桁目)
    private static final BigDecimal LOT_CHECK_WEIGHT_2 = BigDecimal.valueOf(5);
    // ロットチェック用ウェイト(3桁目)
    private static final BigDecimal LOT_CHECK_WEIGHT_3 = BigDecimal.valueOf(8);
    // ロットチェック用ウェイト(4桁目)
    private static final BigDecimal LOT_CHECK_WEIGHT_4 = BigDecimal.valueOf(7);
    // ロットチェック用ウェイト(5桁目)
    private static final BigDecimal LOT_CHECK_WEIGHT_5 = BigDecimal.valueOf(4);
    // ロットチェック用ウェイト(6桁目)
    private static final BigDecimal LOT_CHECK_WEIGHT_6 = BigDecimal.valueOf(3);
    // ロットチェック用ウェイト(7桁目)
    private static final BigDecimal LOT_CHECK_WEIGHT_7 = BigDecimal.valueOf(2);
    
    /**
     * DataSource
     */
    @Resource(mappedName = "jdbc/DocumentServer")
    private transient DataSource dataSource;
    /**
     * DataSource
     */
    @Resource(mappedName = "jdbc/qcdb")
    private transient DataSource dataSourceXHD;
    /**
     * DataSource(WIP)
     */
    @Resource(mappedName = "jdbc/wip")
    private transient DataSource dataSourceWip;
    /**
     * メニュー項目
     */
    private List<FXHDM01> menuListGXHDO101;
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
     * コンストラクタ
     */
    public GXHDO101A() {
    }

    /**
     * 一覧取得処理
     * @return 一覧データ
     */
    public List<FXHDM01> getMenuListGXHDO101() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        List<String> userGrpList = (List<String>)session.getAttribute("login_user_group");
        
        menuListGXHDO101 = new ArrayList<>();
        FacesContext facesContext = FacesContext.getCurrentInstance();
      
        // ユーザーグループを取得する
        String strGamenLotNo = getLotNo();
        
        String strProcess = "";

        // ロットNo桁数チェック
        if (LOTNO_BYTE != StringUtil.getByte(strGamenLotNo, CHARSET, LOGGER)) {
            setErrorMessage(MessageUtil.getMessage("XHC-000003", "ロットNo", LOTNO_BYTE));
            setMenuTableRender(false);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, getErrorMessage(), null);
            facesContext.addMessage(null, message);
            return null;
        }
        
        // ﾛｯﾄNoのﾁｪｯｸﾃﾞｼﾞｯﾄﾁｪｯｸ
        if (!checkLotNoDigit(strGamenLotNo)) {            
            setMenuTableRender(false);
            setErrorMessage(MessageUtil.getMessage("XHC-000010"));
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, getErrorMessage(), null);
            facesContext.addMessage(null, message);
            return null;                    
        }

        List<Object> listGamenID = new ArrayList<>();
        String strKojyo = strGamenLotNo.substring(0,3);
        String strLotNo = strGamenLotNo.substring(3,11);
        String strEdaban = strGamenLotNo.substring(11,14);
        
            // ユーザーグループでメニューマスタを検索
            try {

                QueryRunner queryRunner = new QueryRunner(dataSourceXHD);
                String sqlsearchProcess = "SELECT PrintFmt FROM da_sekkei WHERE KOJYO = ? AND LOTNO = ? AND EDABAN = ? ";
                List<Object> params = new ArrayList<>();
                params.add(strKojyo);
                params.add(strLotNo);
                params.add(strEdaban);

                ResultSetHandler rsh = new MapListHandler();
                List processResult =  (List)queryRunner.query(sqlsearchProcess, rsh, params.toArray());
                for (Iterator i = processResult.iterator(); i.hasNext();) {
                    HashMap m = (HashMap)i.next();
                    strProcess = m.get("PrintFmt").toString();
                }
                
                if (processResult.isEmpty()){
                    setMenuTableRender(false);
                    setInfoMessage("データは存在しません。");
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, getInfoMessage(), null);
                    facesContext.addMessage(null, message);
                    return null;
                }
                
                String sqlsearchGamenID = "SELECT gamen_id, kotei_jun FROM fxhdm03 WHERE koteijun_hantei_jouhou = ? order by kotei_jun ";
                List<Object> params2 = new ArrayList<>();
                params2.add(strProcess);

                rsh = new MapListHandler();
                queryRunner = new QueryRunner(dataSource);
                List sqlsearchResult =  (List)queryRunner.query(sqlsearchGamenID, rsh, params2.toArray());
                for (Iterator i = sqlsearchResult.iterator(); i.hasNext();) {
                    HashMap m = (HashMap)i.next();                    
                    listGamenID.add(m.get("gamen_id").toString());
                }

                if (sqlsearchResult.isEmpty()){
                    setMenuTableRender(false);
                    setInfoMessage("データは存在しません。");
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, getInfoMessage(), null);
                    facesContext.addMessage(null, message);
                    return null;
                }
                
                String sql = "SELECT gamen_id,gamen_title,title_setting,menu_no,menu_name,link_char,menu_parameter,menu_comment,gamen_classname,hyouji_kensu FROM fxhdm01 "
                           + "WHERE menu_group_id = 'QCDB' " ;
                        if(!listGamenID.isEmpty()) {
                            sql += " AND ";
                            sql += DBUtil.getInConditionPreparedStatement("gamen_id", listGamenID.size());
                        }
                        sql += " AND " + DBUtil.getInConditionPreparedStatement("user_role", userGrpList.size());

                        sql += " AND pc_flg = '0' OR pc_flg = '1' ";
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
                
                DBUtil.outputSQLLog(sql, userGrpList.toArray(), LOGGER);
                if(listGamenID.isEmpty()) {
                    menuListGXHDO101 = queryRunner.query(sql, beanHandler, userGrpList.toArray()); 
                }else{
                    menuListGXHDO101 = queryRunner.query(sql, beanHandler,params3.toArray()); 
                }

                if (menuListGXHDO101.isEmpty()){
                    setMenuTableRender(false);
                    setInfoMessage("データは存在しません。");
                }else{
                    setMenuTableRender(true);
                    setInfoMessage("データを表示しました。");
                }
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, getInfoMessage(), null);
            facesContext.addMessage(null, message);

            } catch (SQLException ex) {
                ErrUtil.outputErrorLog("メニュー項目未登録", ex, LOGGER);
                menuListGXHDO101 = new ArrayList<>();
            }

        return menuListGXHDO101;
    }
     
    /**
     * 品質DBメンテナンス画面open
     * @param rowData 選択行のデータ
     * @return 遷移先画面
     */
    public String openXhdForm(FXHDM01 rowData) {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        
        // 画面遷移パラメータをセッション変数に保持
        session.setAttribute("formId", rowData.getFormId());
        session.setAttribute("callerFormId", "GXHDO301A");
        session.setAttribute("formTitle", rowData.getFormTitle());
        session.setAttribute("menuName", rowData.getMenuName());
        session.setAttribute("menuComment", rowData.getMenuComment());
        session.setAttribute("titleSetting", rowData.getTitleSetting());
        session.setAttribute("formClassName", rowData.getFormClassName());
        session.setAttribute("hyojiKensu", rowData.getHyojiKensu());
        session.setAttribute("lotNo", getLotNo());
        
        return rowData.getLinkChar() + "?faces-redirect=true";
    }
    
    /**
     * 品質DB画面データ検索
     */
    public void searchHinshitsuData() {
        menuListGXHDO101 = getMenuListGXHDO101();

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
     * @return the GamenID
     */
    public String getGamenID() {
        return gamenID;
    }

    /**
     * 画面ID
     * @param gamenID
     */
    public void setGamenID(String gamenID) {
        this.gamenID = gamenID;
    }
    
    /**
     * ロットNo
     * @return the lotNo
     */  
    public String getLotNo() {
        return lotNo;
    }

    /**
     * ロットNo
     * @param lotNo
     */
    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }
    
    /**
     * 表示render有無
     * @param menuTableRender
     */
    public void setMenuTableRender(boolean menuTableRender) {
        this.menuTableRender = menuTableRender;
    }

    /**
     * 表示render有無
     * @return menuTableRender
     */
    public boolean getMenuTableRender() {
        return menuTableRender;
    }

    /**
     * エラーメッセージ
     * @return the errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * エラーメッセージ
     * @param errorMessage the errorMessage to set
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * 情報メッセージ
     * @return the infoMessage
     */
    public String getInfoMessage() {
        return infoMessage;
    }

    /**
     * 情報メッセージ
     * @param infoMessage the infoMessage to set
     */
    public void setInfoMessage(String infoMessage) {
        this.infoMessage = infoMessage;
    }

    /**
     * 警告メッセージ
     * @return the warnMessage
     */
    public String getWarnMessage() {
        return warnMessage;
    }

    /**
     * 警告メッセージ
     * @param warnMessage the warnMessage to set
     */
    public void setWarnMessage(String warnMessage) {
        this.warnMessage = warnMessage;
    }

    /**
     * LotNo DigitCheck
     * @param lotNo ロットNo(13桁)
     * @return チェック結果(true:エラーなし、false:エラー有り)
     */
    private boolean checkLotNoDigit(String lotNo) {
        Map<String, BigDecimal> lotMap = new HashMap<>();
        // ロットNo4桁目から11桁を数値化してMapに設定、数値化できない場合はエラーとしてリターン
        String spLotNo;
        for (int i = 0; i < 8; i++) {
            spLotNo = StringUtil.mid(lotNo, i + 4, i + 4);
            if (StringUtil.isEmpty(spLotNo) || !NumberUtils.isDigits(spLotNo)) {
                return false;
            }

            lotMap.put("LOT" + (i + 4), new BigDecimal(spLotNo));
        }

        //ロットNoの各桁にWEIGHT(9587432)をかけての合計を取得
        BigDecimal sum1 = BigDecimal.ZERO;
        sum1 = sum1.add(lotMap.get("LOT4").multiply(LOT_CHECK_WEIGHT_1));
        sum1 = sum1.add(lotMap.get("LOT5").multiply(LOT_CHECK_WEIGHT_2));
        sum1 = sum1.add(lotMap.get("LOT6").multiply(LOT_CHECK_WEIGHT_3));
        sum1 = sum1.add(lotMap.get("LOT7").multiply(LOT_CHECK_WEIGHT_4));
        sum1 = sum1.add(lotMap.get("LOT8").multiply(LOT_CHECK_WEIGHT_5));
        sum1 = sum1.add(lotMap.get("LOT9").multiply(LOT_CHECK_WEIGHT_6));
        sum1 = sum1.add(lotMap.get("LOT10").multiply(LOT_CHECK_WEIGHT_7));

        // 10で割った余りを取得
        BigDecimal checkValue = sum1.remainder(BigDecimal.TEN);
        
        // ロットNoの11桁目の値がチェック値と一致しない場合、エラー
        return checkValue.compareTo(lotMap.get("LOT11")) == 0;
    }

}