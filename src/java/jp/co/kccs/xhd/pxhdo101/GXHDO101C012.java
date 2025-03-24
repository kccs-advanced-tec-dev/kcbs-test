/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.sql.DataSource;
import jp.co.kccs.xhd.db.model.FXHDM01;
import jp.co.kccs.xhd.util.CommonUtil;
import jp.co.kccs.xhd.util.DBUtil;
import jp.co.kccs.xhd.util.ErrUtil;
import jp.co.kccs.xhd.util.MessageUtil;
import jp.co.kccs.xhd.util.StringUtil;
import jp.co.kccs.xhd.util.ValidateUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.primefaces.context.RequestContext;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2019/12/06<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * 変更日	2020/09/21<br>
 * 計画書No	MB2008-DK001<br>
 * 変更者	KCSS D.Yanagida<br>
 * 変更理由	ロット混合対応<br>
 * <br>
 * 変更日	2025/02/17<br>
 * 計画書No	MB2501-D004<br>
 * 変更者	KCSS H.Aruba<br>
 * 変更理由	【MLCC】印刷・積層　要望対応<br>
 * <br>
 * 変更日	2025/03/12<br>
 * 計画書No	MB2501-D004<br>
 * 変更者	KCSS A.Hayashi<br>
 * 変更理由	項目追加・変更<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101C012_ﾛｯﾄ参照
 *
 * @author KCSS K.Jo
 * @since 2019/12/06
 */
@ManagedBean(name = "beanGXHDO101C012")
@SessionScoped
public class GXHDO101C012 implements Serializable {
    private static final Logger LOGGER = Logger.getLogger(GXHDO101C012.class.getName());

    /**
     * 通常カラーコード(テキスト)
     */
    private static final String DEFAULT_BACK_COLOR = "#ffffffff";
    
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
     * DataSource(wip)
     */
    @Resource(mappedName = "jdbc/wip")
    private transient DataSource dataSourceWip;
    
    /**
     * 参照元ﾛｯﾄNo
     */
    private String sanshouMotoLotNo;
    
    /**
     * 参照元画面ID
     */
    private String sanshouGamenID;

    /**
     * 参照先ﾛｯﾄNo
     */
    private String sanshouSakiLotNo;
    
    /**
     * 参照先ﾛｯﾄNo背景色
     */
    private String sanshousakiTextBackColor;
    
    /**
     * 参照元ﾃﾞｰﾀ
     */
    private FXHDM01 sanshouMotoInfo = null;

    /**
     * 設計.printfmt
     */
    private String printfmt;

    /**
     * 設計.pattern
     */
    private String pattern;

    /**
     * フォームエラー判定
     */
    private boolean isFormError;
    
    /**
     * 回数ｺﾝﾎﾞﾎﾞｯｸｽ設定値
     */
    private List<String> kaisuList;
    
    /**
     * 回数の表示非表示
     */
    private String kaisuDisplay;
    
    /**
     * 選択されている回数
     */
    private String selectKaisu;
    
    /**
     * コンストラクタ
     */
    public GXHDO101C012() {
    }

    /**
     * 参照元ﾛｯﾄNo
     *
     * @return the sanshouMotoLotNo
     */
    public String getSanshouMotoLotNo() {
        return sanshouMotoLotNo;
    }
    
    /**
     * 参照元ﾛｯﾄNo
     *
     * @param sanshouMotoLotNo the sanshouMotoLotNo to set
     */
    public void setSanshouMotoLotNo(String sanshouMotoLotNo) {
        this.sanshouMotoLotNo = sanshouMotoLotNo;
    }

    /**
     * 参照元画面ID
     *
     * @return the sanshouGamenID
     */
    public String getSanshouGamenID() {
        return sanshouGamenID;
    }
    
    /**
     * 参照元画面ID
     *
     * @param sanshouGamenID the sanshouGamenID to set
     */
    public void setSanshouGamenID(String sanshouGamenID) {
        this.sanshouGamenID = sanshouGamenID;
    }
    
    /**
     * 参照先ﾛｯﾄNo
     *
     * @return the sanshouSakiLotNo
     */
    public String getSanshouSakiLotNo() {
        return sanshouSakiLotNo;
    }
    
    /**
     * 参照先ﾛｯﾄNo
     *
     * @param sanshouSakiLotNo the sanshouSakiLotNo to set
     */
    public void setSanshouSakiLotNo(String sanshouSakiLotNo) {
        this.sanshouSakiLotNo = sanshouSakiLotNo;
    }

    /**
     * 参照先ﾛｯﾄNo背景色
     *
     * @return the sanshousakiTextBackColor
     */
    public String getSanshousakiTextBackColor() {
        return sanshousakiTextBackColor;
    }

    /**
     * 参照先ﾛｯﾄNo背景色
     *
     * @param sanshousakiTextBackColor the sanshousakiTextBackColor to set
     */
    public void setSanshousakiTextBackColor(String sanshousakiTextBackColor) {
        this.sanshousakiTextBackColor = sanshousakiTextBackColor;
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
     * 設計.printfmt
     *
     * @return the printfmt
     */
    public String getPrintfmt() {
        return printfmt;
    }
    
    /**
     * 設計.printfmt
     *
     * @param printfmt the printfmt to set
     */
    public void setPrintfmt(String printfmt) {
        this.printfmt = printfmt;
    }

    /**
     * 設計.pattern
     *
     * @return the pattern
     */
    public String getPattern() {
        return pattern;
    }

    /**
     * 設計.pattern
     *
     * @param pattern the pattern to set
     */
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    /**
     * フォームエラー判定
     *
     * @return the isFormError
     */
    public boolean isIsFormError() {
        return isFormError;
    }

    /**
     * フォームエラー判定
     *
     * @param isFormError the isFormError to set
     */
    public void setIsFormError(boolean isFormError) {
        this.isFormError = isFormError;
    }
    
    /**
     * 回数ｺﾝﾎﾞﾎﾞｯｸｽﾘｽﾄ
     *
kaisuList     */
    public void setKaisuMap(List<String> kaisuList) {
        this.kaisuList = kaisuList;
    }
    
    /**
     * 回数ｺﾝﾎﾞﾎﾞｯｸｽﾘｽﾄ
     * 
     * @return the kaisuMap
     */
    public List<String> getKaisuList() {
        return this.kaisuList;
    }
    
    /**
     * 回数disabled
     *
     * @param kaisuDisplay the kaisuDisable to set
     */
    public void setKaisuDisplay(String kaisuDisplay) {
        this.kaisuDisplay = kaisuDisplay;
    }
    
    /**
     * 回数disabled
     * 
     * @return the kaisuDisable
     */
    public String getKaisuDisplay() {
        return this.kaisuDisplay;
    }
    
    /**
     * 選択された回数
     * 
     * @param selectKaisu the selectKaisu to set
     */
    public void setSelectKaisu(String selectKaisu) {
        this.selectKaisu = selectKaisu;
    }
    
    /**
     * 選択された回数
     * 
     * @return the selectKaisu
     */
    public String getSelectKaisu() {
        return this.selectKaisu;
    }
    
    /**
     * OKボタン押下時のチェック処理を行う。
     */
    public void doOk() {
        if (!checkOK()) {
            setIsFormError(true);
            // エラーの場合はコールバック変数に"error"をセット
            RequestContext context = RequestContext.getCurrentInstance();
            context.addCallbackParam("firstParam", "error");
        }else{            
            setIsFormError(false);
        }
    }

    /**
     * OKボタンチェック処理
     *
     * @return 正常:true、異常:fasle
     */
    private boolean checkOK() {

        // 背景色をクリア
        clearBackColor();
        
        // 参照先ﾛｯﾄNoチェック処理
        if(!checkSanshouSakiLotNo()){
            return false;
        }

        return true;
    }

    /**
     * エラーチェック：
     * エラーが存在する場合ポップアップ用メッセージをセットする
     * @param errorMessage エラーメッセージ
     * @return エラーが存在する場合true
     */
    private boolean existError(String errorMessage) {
        if (StringUtil.isEmpty(errorMessage)) {
            return false;
        }
        
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
        return true;
    }
    
    /**
     * ﾛｯﾄ参照チェック処理
     *
     * @param lotSanshouData ﾛｯﾄ参照データ
     * @param itemName 項目名
     * @return チェック結果
     */
    private boolean checkSanshouSakiLotNo() {

        QueryRunner queryRunnerQcdb = new QueryRunner(dataSourceXHD);        
        QueryRunner queryRunnerDoc = new QueryRunner(dataSourceDocServer);
        QueryRunner queryRunnerWip = new QueryRunner(dataSourceWip);
            
        // 入力チェック処理
        ValidateUtil validateUtil = new ValidateUtil();
        // ロットNo
        if(StringUtil.isEmpty(sanshouSakiLotNo) ||  StringUtil.getLength(sanshouSakiLotNo) != 14){

            FacesContext facesContext = FacesContext.getCurrentInstance();
            FacesMessage message
                    = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000004","ロットNo","14"), null);
            facesContext.addMessage(null, message);
            setSanshousakiTextBackColor(ErrUtil.ERR_BACK_COLOR);
            return false;
        }
        if (!StringUtil.isEmpty(sanshouSakiLotNo) && existError(validateUtil.checkValueE001(sanshouSakiLotNo))) {
            setSanshousakiTextBackColor(ErrUtil.ERR_BACK_COLOR);
            return false;
        }
        
        String strKojyo = "";
        String strLotNo = "";

        if(!StringUtil.isEmpty(sanshouSakiLotNo)){
            strKojyo = this.sanshouSakiLotNo.substring(0, 3);
            strLotNo = this.sanshouSakiLotNo.substring(3, 11);
        }

        try {
            //②ﾌﾟﾛｾｽ判定情報ﾁｪｯｸ
            // 設計.printfmt及び、設計.patternが同一であるかﾁｪｯｸする。
            // 1.Ⅲ.画面表示仕様(3)を発行する。
            Map gamenInfo = CommonUtil.getSekkeiInfoTogoLot(queryRunnerQcdb, queryRunnerWip, strKojyo, strLotNo, "001");            
            if(gamenInfo == null){
                //  A.ﾚｺｰﾄﾞが取得出来なかった場合
                //   ｴﾗｰﾒｯｾｰｼﾞを表示し、処理を中断する。
                //     ・ｴﾗｰｺｰﾄﾞ:XHD-000162
                //     ・ｴﾗｰﾒｯｾｰｼﾞ:このﾛｯﾄは参照できません。ﾌﾟﾛｾｽ判定情報不一致。
                //     0には、【印刷工程画面ID】をもとにした、ﾒﾆｭｰ名(ﾌｫｰﾑﾊﾟﾗﾒｰﾀ)を設定。
                FacesContext facesContext = FacesContext.getCurrentInstance();
                FacesMessage message
                        = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000162"), null);
                facesContext.addMessage(null, message);
                return false;
            }else{
                //  B.ﾚｺｰﾄﾞが取得できた場合
                //   ｱ.参照元の設計.printfmtとⅢ.画面表示仕様(3).printfmtが一致するかﾁｪｯｸする。
                //    α.一致した場合 
                //     以降の処理を続行する。
                //    β.一致しなかった場合 
                //     ｴﾗｰﾒｯｾｰｼﾞを表示し、処理を中断する。
                //       ・ｴﾗｰｺｰﾄﾞ:XHD-000162
                //       ・ｴﾗｰﾒｯｾｰｼﾞ:このﾛｯﾄは参照できません。ﾌﾟﾛｾｽ判定情報不一致。
                String printFmt = StringUtil.nullToBlank(getMapData(gamenInfo, "PrintFmt"));
                if(!getPrintfmt().equals(printFmt)){
                    FacesContext facesContext = FacesContext.getCurrentInstance();
                    FacesMessage message
                            = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000162"), null);
                    facesContext.addMessage(null, message);
                    return false;
                }
            
                //   ｲ.参照元の設計.patternとⅢ.画面表示仕様(3).patternが一致するかﾁｪｯｸする。
                //    α.一致した場合 
                //     以降の処理を続行する。
                //    β.一致しなかった場合 
                //     ｴﾗｰﾒｯｾｰｼﾞを表示し、処理を中断する。
                //       ・ｴﾗｰｺｰﾄﾞ:XHD-000163
                //       ・ｴﾗｰﾒｯｾｰｼﾞ:このﾛｯﾄは参照できません。電極製版名不一致。
                String patternValue = StringUtil.nullToBlank(getMapData(gamenInfo, "PATTERN"));
                if(!getPattern().equals(patternValue)){
                    FacesContext facesContext = FacesContext.getCurrentInstance();
                    FacesMessage message
                            = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000163"), null);
                    facesContext.addMessage(null, message);
                    return false;
                }
            }
            
            //参照先のﾃﾞｰﾀ
            String strSakiKojyo = "";
            String strSakiLotNo = "";
            String strSakiEdaban = "";

            if(!"".equals(sanshouSakiLotNo) && sanshouSakiLotNo != null){
                strSakiKojyo = this.sanshouSakiLotNo.substring(0, 3);
                strSakiLotNo = this.sanshouSakiLotNo.substring(3, 11);
                strSakiEdaban = this.sanshouSakiLotNo.substring(11, 14);
            }
            //②ﾃﾞｰﾀﾁｪｯｸ
            // ﾃﾞｰﾀを引き渡す際に、参照先のﾃﾞｰﾀが登録(仮登録も含む)されていないことを確認する。

            // ■DP画面の場合
            // 1.G1).回数が選択されているかﾁｪｯｸを行う
            // 	A.選択されている場合
            //      以降の処理を続行する。
            // 	B.選択されていない場合
            //      ｴﾗｰﾒｯｾｰｼﾞを表示し、処理を中断する
            //          ・ｴﾗｰｺｰﾄﾞ:XHD-000133
            // 		・ｴﾗｰﾒｯｾｰｼﾞ:DP2回数が指定されていません。
            if ("GXHDO101B023".equals(sanshouGamenID)) {
                if(StringUtil.nullToBlank(selectKaisu).isEmpty()){
                    FacesContext facesContext = FacesContext.getCurrentInstance();
                    FacesMessage message
                            = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000133"), null);
                    facesContext.addMessage(null, message);
                    return false;
                }
                
                // 先頭の1文字を取得
                int kaisu = Integer.parseInt(String.valueOf(selectKaisu.charAt(0)));
                
                //参照元のﾃﾞｰﾀ
                String strMotoKojyo = "";
                String strMotoLotNo = "";
                String strMotoEdaban = "";
                        
                if(!"".equals(sanshouMotoLotNo) && sanshouMotoLotNo != null){
                    strMotoKojyo = this.sanshouMotoLotNo.substring(0, 3);
                    strMotoLotNo = this.sanshouMotoLotNo.substring(3, 11);
                    strMotoEdaban = this.sanshouMotoLotNo.substring(11, 14);
                }     
                
                // 2.G1).回数を数字に変換し、Ⅲ.画面表示仕様(4)を発行する。
                Map fxhdd03JissekinoDpinfoMoto = loadFxhdd03JissekinoDPInfo(queryRunnerDoc, strMotoKojyo, strMotoLotNo, strMotoEdaban, sanshouGamenID,kaisu);
                    if(fxhdd03JissekinoDpinfoMoto != null ){
                    //if(fxhdd03JissekinoDpinfoMoto != null || fxhdd03JissekinoDpinfoMoto.isEmpty()){
                    // A.取得できなかった場合
                    // 	以降の処理を続行する。
                    // B.取得できた場合、登録済であるか確認を行う。
                    //  ｱ.状態ﾌﾗｸﾞ == '1' 以外の場合
                    //      ｴﾗｰﾒｯｾｰｼﾞを表示し、処理を中断する。
                    //          ・ｴﾗｰｺｰﾄﾞ:XHD-000160
                    //          ・ｴﾗｰﾒｯｾｰｼﾞ:参照元のﾛｯﾄ({0})が登録済ではありません。
                    //            0には、【印刷工程画面ID】をもとにした、ﾒﾆｭｰ名(ﾌｫｰﾑﾊﾟﾗﾒｰﾀ)を設定。
                    //  ｲ.上記以外の場合
                    //      以降の処理を実行する。
                    String motoJotaiFlg = StringUtil.nullToBlank(getMapData(fxhdd03JissekinoDpinfoMoto, "jotai_flg"));
                    if(!"1".equals(motoJotaiFlg)){
                        FacesContext facesContext = FacesContext.getCurrentInstance();
                        FacesMessage message
                                = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000160","印刷(DP2)"), null);
                        facesContext.addMessage(null, message);
                        setSanshousakiTextBackColor(ErrUtil.ERR_BACK_COLOR);
                        return false;
                    }
                } else {
                    FacesContext facesContext = FacesContext.getCurrentInstance();
                    FacesMessage message
                            = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000161","印刷(DP2)"), null);
                    facesContext.addMessage(null, message);
                    setSanshousakiTextBackColor(ErrUtil.ERR_BACK_COLOR);
                    return false;   
                }
                // 2.G1).回数を数字に変換し、Ⅲ.画面表示仕様(5)を発行する。
                Map fxhdd03JissekinoDpinfoSaki = loadFxhdd03JissekinoDPInfo(queryRunnerDoc, strSakiKojyo, strSakiLotNo, strSakiEdaban, sanshouGamenID,kaisu);
                if(fxhdd03JissekinoDpinfoSaki == null){
                //if(fxhdd03JissekinoDpinfoSaki != null){
                    // A.取得できなかった場合
                    // 	以降の処理を続行する。
                    // B.取得できた場合、登録済であるか確認を行う。
                    //  ｱ.状態ﾌﾗｸﾞ == '0' の場合
                    //      ｴﾗｰﾒｯｾｰｼﾞを表示し、処理を中断する。
                    //          ・ｴﾗｰｺｰﾄﾞ:XHD-000161
                    //          ・ｴﾗｰﾒｯｾｰｼﾞ:参照元のﾛｯﾄ({0})が登録済ではありません。
                    //            0には、【印刷工程画面ID】をもとにした、ﾒﾆｭｰ名(ﾌｫｰﾑﾊﾟﾗﾒｰﾀ)を設定。
                    //  ｲ.状態ﾌﾗｸﾞ == '1' の場合
                    //      ｴﾗｰﾒｯｾｰｼﾞを表示し、処理を中断する。
                    //          ・ｴﾗｰｺｰﾄﾞ:XHD-000161
                    //          ・ｴﾗｰﾒｯｾｰｼﾞ:参照先のﾛｯﾄ({0})が未登録ではありません。
                    //            0には、【印刷工程画面ID】をもとにした、ﾒﾆｭｰ名(ﾌｫｰﾑﾊﾟﾗﾒｰﾀ)を設定。
                    //  ｳ.上記以外の場合
                    //      以降の処理を実行する。
                    String sakiJotaiFlg = StringUtil.nullToBlank(getMapData(fxhdd03JissekinoDpinfoSaki, "jotai_flg"));
                    if("0".equals(sakiJotaiFlg) || "1".equals(sakiJotaiFlg)){
                        FacesContext facesContext = FacesContext.getCurrentInstance();
                        FacesMessage message
                                = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000161","印刷(DP2)"), null);
                        facesContext.addMessage(null, message);
                        setSanshousakiTextBackColor(ErrUtil.ERR_BACK_COLOR);
                        return false;
                    }
                } else {
                    FacesContext facesContext = FacesContext.getCurrentInstance();
                    FacesMessage message
                            = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000161","印刷(DP2)"), null);
                    facesContext.addMessage(null, message);
                    setSanshousakiTextBackColor(ErrUtil.ERR_BACK_COLOR);
                    return false;
                }
            } else {
            // 1.Ⅲ.画面表示仕様(2)を発行する。
                Map fxhdd03JissekinoInfo = loadFxhdd03JissekinoInfo(queryRunnerDoc, strSakiKojyo, strSakiLotNo, strSakiEdaban, sanshouGamenID);
                if(fxhdd03JissekinoInfo != null){
                    //  A.取得できなかった場合
                    //   以降の処理を続行する。
                    //  B.取得できた場合、登録済(仮登録も含む)であるか確認を行う。
                    //   ｱ.状態ﾌﾗｸﾞ == '0' の場合
                    //    ｴﾗｰﾒｯｾｰｼﾞを表示し、処理を中断する。 
                    //      ・ｴﾗｰｺｰﾄﾞ:XHD-000161 
                    //      ・ｴﾗｰﾒｯｾｰｼﾞ:参照先のﾛｯﾄ({0})が未登録ではありません。 
                    //      0には、【印刷工程画面ID】をもとにした、ﾒﾆｭｰ名(ﾌｫｰﾑﾊﾟﾗﾒｰﾀ)を設定。 
                    //   ｲ.状態ﾌﾗｸﾞ == '1' の場合
                    //    ｴﾗｰﾒｯｾｰｼﾞを表示し、処理を中断する。
                    //      ・ｴﾗｰｺｰﾄﾞ:XHD-000161 
                    //      ・ｴﾗｰﾒｯｾｰｼﾞ:参照先のﾛｯﾄ({0})が未登録ではありません。 
                    //      0には、【印刷工程画面ID】をもとにした、ﾒﾆｭｰ名(ﾌｫｰﾑﾊﾟﾗﾒｰﾀ)を設定。 
                    //   ｳ.上記以外の場合
                    //    以降の処理を実行する。 
                    String jotaiFlg = StringUtil.nullToBlank(getMapData(fxhdd03JissekinoInfo, "jotai_flg"));
                    if("0".equals(jotaiFlg) || "1".equals(jotaiFlg)){
                        FacesContext facesContext = FacesContext.getCurrentInstance();
                        FacesMessage message
                                = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000161","印刷(DP2)"), null);
                        facesContext.addMessage(null, message);
                        setSanshousakiTextBackColor(ErrUtil.ERR_BACK_COLOR);
                        return false;
                    }
                }
            }

        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("ﾌﾟﾛｾｽ判定情報ﾁｪｯｸエラー", ex, LOGGER);
        }
            
        return true;
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
     * 背景色のクリア処理
     */
    private void clearBackColor() {
        //項目の色をリセット
        this.setSanshousakiTextBackColor(DEFAULT_BACK_COLOR);
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
     * (4)[品質DB登録実績]から、ﾃﾞｰﾀを取得(印刷工程入力ﾃﾞｰﾀ取得)
     *
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param formId 画面ID(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadFxhdd03JissekinoDPInfo(QueryRunner queryRunnerDoc, String kojyo, String lotNo,
            String edaban, String formId, int selectKaisu) throws SQLException {

        // 品質DB登録実績情報の取得
        String sql = "SELECT jissekino,rev, jotai_flg "
                + "FROM fxhdd03 "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND gamen_id = ? "
                + "AND jissekino = ?";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(formId);
        params.add(selectKaisu);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerDoc.query(sql, new MapHandler(), params.toArray());
    }
}
