/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.util;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import jp.co.kccs.xhd.db.model.FXHDD01;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2019/05/22<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	新規作成<br>
 * <br>
 * 変更日	2019/12/05<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	入力画面選択で使用するデータ取得を追加また直接データの取得メソッドが呼べるように変更<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * Commonユーティリティ
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2019/05/24
 */
public class CommonUtil {

    private static final Logger LOGGER = Logger.getLogger(CommonUtil.class.getName());

    /**
     * 対象画面のﾃｰﾌﾞﾙ情報を取得(該当データがない場合はNULLを返却)
     *
     * @param queryRunnerQcdb queryRunner
     * @param gamenId 画面ID
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param rev ﾘﾋﾞｼﾞｮﾝ
     * @param jissekino 実績No
     * @return 良品ｾｯﾄ数
     * @throws SQLException
     */
    public static Map getMaeKoteiData(QueryRunner queryRunnerQcdb, String gamenId, String kojyo, String lotNo, String edaban, String rev, int jissekino) throws SQLException {
        switch (gamenId) {
            // 印刷SPSｸﾞﾗﾋﾞｱ
            case "GXHDO101B001":
                return getSrSpsprintGraData(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
            // 印刷SPSｽｸﾘｰﾝ
            case "GXHDO101B002":
                return getSrSpsprintData(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
            // 印刷RSUS
            case "GXHDO101B003":
                return getSrRsusprnData(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
            // 積層・SPS
            case "GXHDO101B004":
                return getSrSpssekisouData(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
            // 積層・RSUS
            case "GXHDO101B005":
                return getSrRsussekData(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
            // 印刷積層・RHAPS
            case "GXHDO101B006":
                return getSrRhapsData(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
            // ﾌﾟﾚｽ・仮ﾌﾟﾚｽ
            case "GXHDO101B007":
                return getSrPrepressData(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
            // ﾌﾟﾚｽ・真空脱気
            case "GXHDO101B008":
                return getSrPressData(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
            // ﾌﾟﾚｽ・ﾒｶﾌﾟﾚｽ
            case "GXHDO101B009":
                return getSrMekapressData(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
            // ｶｯﾄ・押切ｶｯﾄ
            case "GXHDO101B010":
                return getSrHapscutData(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
            // ｶｯﾄ・ﾀﾞｲｼﾝｸﾞｶｯﾄ
            case "GXHDO101B011":
                return getSrCutData(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
            // 生品質検査
            case "GXHDO101B012":
                return getSrCutcheckData(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
            // 焼成・ｾｯﾀ詰め
            case "GXHDO101B013":
                return getSrSayadumeData(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
            case "GXHDO101B014":// 焼成・Air脱脂
            case "GXHDO101B015":// 焼成・窒素脱脂
                return getSrYobikanData(queryRunnerQcdb, kojyo, lotNo, edaban, rev, jissekino);
            // 焼成・ﾍﾞﾙﾄ脱脂
            case "GXHDO101B016":
                return getSrNijidasshiData(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
            // 焼成・焼成
            case "GXHDO101B017":
                return getSrSyoseiData(queryRunnerQcdb, kojyo, lotNo, edaban, rev, jissekino);
            // 焼成・RHK焼成
            case "GXHDO101B018":
                return getSrSyoseiData(queryRunnerQcdb, kojyo, lotNo, edaban, rev, jissekino);
            // 焼成・再酸化
            case "GXHDO101B019":
                return getSrSaisankaData(queryRunnerQcdb, kojyo, lotNo, edaban, rev, jissekino);
            // 研磨・ﾊﾞﾚﾙ
            case "GXHDO101B020":
                return getSrBarrel1Data(queryRunnerQcdb, kojyo, lotNo, edaban, rev, jissekino);
            // 研磨・計数
            case "GXHDO101B021":
                return getSrSyoseikeisuuData(queryRunnerQcdb, kojyo, lotNo, edaban, rev, jissekino);
            // 磁器QC
            case "GXHDO101B022":
                return getSrJikiqcData(queryRunnerQcdb, kojyo, lotNo, edaban, rev, jissekino);
            // 印刷・DP2
            case "GXHDO101B023":
                return getSrDpprintData(queryRunnerQcdb, kojyo, lotNo, edaban, rev, jissekino);
            // 外部電極・疎水処理
            case "GXHDO101B024":
                return getSrGdsosuiData(queryRunnerQcdb, kojyo, lotNo, edaban, rev, jissekino);
            // 外部電極・外部電極塗布(三端子・4端子)
            case "GXHDO101B025":
                return getSrGdtermtData(queryRunnerQcdb, kojyo, lotNo, edaban, rev, jissekino);
            // 外部電極・外部電極塗布
            case "GXHDO101B026":
                return getSrGdtermData(queryRunnerQcdb, kojyo, lotNo, edaban, rev, jissekino);                
            // 外部電極・外部電極焼成(ｻﾔ詰め)
            case "GXHDO101B027":
                return getSrGdsayadumeData(queryRunnerQcdb, kojyo, lotNo, edaban, rev, jissekino);
            // 外部電極・外部電極焼成(脱ﾊﾞｲ)
            case "GXHDO101B028":
                return getSrGddatubaiData(queryRunnerQcdb, kojyo, lotNo, edaban, rev, jissekino);
            // 外部電極・外部電極焼成(焼成)
            case "GXHDO101B029":
                return getSrGdyakitukeData(queryRunnerQcdb, kojyo, lotNo, edaban, rev, jissekino);
            // 外部電極・外部電極焼成(焼成外観)
            case "GXHDO101B030":
                return getSrGdyakitukegaikanData(queryRunnerQcdb, kojyo, lotNo, edaban, rev, jissekino);
            // 外部電極・外部電極焼成(樹脂電極硬化)
            case "GXHDO101B031":
                return getSrGdjusikoukaData(queryRunnerQcdb, kojyo, lotNo, edaban, rev, jissekino);
            // 外部電極・外部電極洗浄(撥水処理)
            case "GXHDO101B032":
                return getSrGdhassuiData(queryRunnerQcdb, kojyo, lotNo, edaban, rev, jissekino);
            // 外部電極・外部電極洗浄(ﾊﾞﾚﾙ)
            case "GXHDO101B033":
                return getSrGdnijibarrelData(queryRunnerQcdb, kojyo, lotNo, edaban, rev, jissekino);
            // 外部電極・外部電極洗浄(超音波)
            case "GXHDO101B034":
                return getSrGdsenjouData(queryRunnerQcdb, kojyo, lotNo, edaban, rev, jissekino);
            // 外部電極・外部電極洗浄(乾燥)
            case "GXHDO101B035":
                return getSrGdkansouData(queryRunnerQcdb, kojyo, lotNo, edaban, rev, jissekino);
            // 外部電極・親水処理
            case "GXHDO101B036":
                return getSrGdsinsuiData(queryRunnerQcdb, kojyo, lotNo, edaban, rev, jissekino);
            // 外部電極・計数
            case "GXHDO101B037":
                return getSrGdkeisuuData(queryRunnerQcdb, kojyo, lotNo, edaban, rev, jissekino);
            // 外部電極・ﾒｯｷ品質検査
            case "GXHDO101B038":
                return getSrMekkiData(queryRunnerQcdb, kojyo, lotNo, edaban, rev, jissekino);
            // 外部電極・ﾒｯｷ真空乾燥
            case "GXHDO101B039":
                return getSrMksinkuukansouData(queryRunnerQcdb, kojyo, lotNo, edaban, rev, jissekino);

            default:
                break;
        }
        return new HashMap();
    }
    
    /**
     * 前工程情報設定処理
     * @param itemData 項目データ
     * @param maekoteiInfo 前工程情報
     * @param setDataId データID(前工程情報の項目ID)
     * @param showNoDataMessage データなし時にメッセージを表示するか(表示する場合、true)
     * @param checkZero 値が0の場合データなしとして扱うか(扱う場合、true)
     */
    public static void setMaekoteiInfo(FXHDD01 itemData, Map maekoteiInfo, String setDataId, boolean showNoDataMessage, boolean checkZero) {
        if (itemData == null) {
            return;
        }

        if (existKeyData(maekoteiInfo, setDataId, checkZero)) {
            //値が入っている場合は値をセットする。
            itemData.setValue(StringUtil.nullToBlank(maekoteiInfo.get(setDataId)));
        } else if (showNoDataMessage) {
            itemData.setValue(MessageUtil.getMessage("XHD-000051"));
            itemData.setFontColorInput("Red");
            itemData.setLabel2("");
            itemData.setCustomStyleInput("font-weight: bold;");
        }
    }

    /**
     * マップ内に指定のKeyデータが存在するかチェックする。
     * @param mapInfo マップ情報
     * @param key キー
     * @param checkZero 0値チェック
     * @return true:値あり,false:値なし
     */
    public static boolean existKeyData(Map mapInfo, String key, boolean checkZero){
        if(mapInfo == null || mapInfo.get(key) == null){
            return false;
        }
        
        // 値が0かどうかチェックする(0の場合データなしとする。)
        if(checkZero){
            BigDecimal checkValue;
            try{
                checkValue = new BigDecimal(StringUtil.nullToBlank(mapInfo.get(key)));
            }catch(NumberFormatException e){
                checkValue = BigDecimal.ZERO;
            }
            if(BigDecimal.ZERO.compareTo(checkValue) == 0){
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * 印刷・SPSｸﾞﾗﾋﾞｱ工程のデータを取得する
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @return 工程データ
     * @throws SQLException 例外エラー
     */
    public static Map getSrSpsprintGraData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT kojyo,lotno,edaban,tapelotno,petfilmsyurui,taperollno1,taperollno2,taperollno3,pastelotno,pastenendo,"
                + "pasteondo,pkokeibun1,pastelotno2,pastenendo2,pasteondo2,pkokeibun2,handoumei,handouno,handoumaisuu,bladeno,"
                + "bladegaikan,BladeATu,AtudoNo,AtudoMaisuu,AtuDoATu,gouki,kansouondo,kansouondo2,kansouondo3,kansouondo4,kansouondo5,"
                + "hansouspeed,startdatetime,tantousya,makuatuave_start,makuatumax_start,makuatumin_start,makuatucv_start,nijimikasure_start,"
                + "start_ptn_dist_x,start_ptn_dist_y,TensionS_sum,TensionStemae,TensionSoku,enddatetime,tanto_end,printmaisuu,makuatuave_end,"
                + "makuatumax_end,makuatumin_end,makuatucv_end,nijimikasure_end,end_ptn_dist_x,end_ptn_dist_y,TensionE_sum,TensionEtemae,"
                + "TensionEoku,printzure1_surihajime_start,printzure2_center_start,printzure3_suriowari_start,abzure_heikin_start,"
                + "printzure1_surihajime_end,printzure2_center_end,printzure3_suriowari_end,abzure_heikin_end,genryoukigou,bikou1,"
                + "bikou2,torokunichiji,kosinnichiji,revision,kcpno "
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
     * 印刷・SPSｽｸﾘｰﾝ工程のデータを取得する
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @return 工程データ
     * @throws SQLException 例外エラー
     */
    public static Map getSrSpsprintData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT kojyo,lotno,edaban,tapesyurui,tapelotno,TapeSlipKigo,genryoukigou,pastelotno,pastenendo,pasteondo,"
                + "seihanno,seihanmaisuu,startdatetime,enddatetime,skeegeno,skeegemaisuu,gouki,tantousya,kakuninsya,kansouondo,prnprofile,"
                + "kansoutime,saatu,skeegespeed,skeegeangle,mld,clearrance,bikou1,bikou2,makuatu1,makuatu2,makuatu3,makuatu4,makuatu5,"
                + "pastelotno2,pastelotno3,pastelotno4,pastelotno5,pastenendo2,pastenendo3,pastenendo4,pastenendo5,pasteondo2,pasteondo3,"
                + "pasteondo4,pasteondo5,bikou3,bikou4,bikou5,kansouondo2,kansouondo3,kansouondo4,kansouondo5,skeegemaisuu2,taperollno1,"
                + "taperollno2,taperollno3,taperollno4,taperollno5,pastehinmei,seihanmei,makuatuave_start,makuatumax_start,makuatumin_start,"
                + "start_ptn_dist_x,start_ptn_dist_y,tanto_setting,makuatuave_end,makuatumax_end,makuatumin_end,end_ptn_dist_x,end_ptn_dist_y,"
                + "tanto_end,kcpno,sijiondo,sijiondo2,sijiondo3,sijiondo4,sijiondo5,TensionS,TensionE,TensionStemae,TensionEtemae,TensionSoku,"
                + "TensionEoku,AtuDoATu,BladeATu,AtuDoKeiLEnd,AtuDoKeiLSide,AtuDoKeiCenter,AtuDoKeiRSide,AtuDoKeiREnd,pkokeibun1,pkokeibun2,"
                + "petfilmsyurui,makuatucv_start,nijimikasure_start,makuatucv_end,nijimikasure_end,printmaisuu,table_clearrance,scraperspeed,"
                + "skeegegaikan,torokunichiji,kosinnichiji,revision "
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
     * 印刷・SPSｽｸﾘｰﾝ工程のデータを取得する
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @return 工程データ
     * @throws SQLException 例外エラー
     */
    public static Map getSrRsusprnData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT KOJYO,LOTNO,EDABAN,KCPNO,TAPESYURUI,TAPELOTNO,TapeSlipKigo,GENRYOKIGO,KAISINICHIJI,SYURYONICHIJI,"
                + "GOKI,SKEEGENO,SKEEGEMAISUU,SKEEGESPEED,KANSOONDO,CLEARANCE,SAATU,MAKUATU1,SEIHANNO,SEIHANMAISUU,PASTELOTNO,"
                + "PASTENENDO,PASTEONDO,INSATUROLLNO,INSATUROLLNO2,INSATUROLLNO3,INSATUROLLNO4,INSATUROLLNO5,INSATUHABASAVE,"
                + "INSATUHABAEAVE,MLD,BIKO1,BIKO2,TOROKUNICHIJI,KOSINNICHIJI,TANTOSYA,pkokeibun1,pastelotno2,pastenendo2,pasteondo2,"
                + "pkokeibun2,petfilmsyurui,kansoondo2,kansoondo3,kansoondo4,kansoondo5,seihanmei,makuatsu_ave_start,makuatsu_max_start,"
                + "makuatsu_min_start,makuatucv_start,nijimikasure_start,nijimikasure_end,tanto_end,printmaisuu,kansouroatsu,printhaba,"
                + "table_clearrance,revision "
                + "FROM sr_rsusprn "
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
     * 積層・SPS工程のデータを取得する
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @return 工程データ
     * @throws SQLException 例外エラー
     */
    public static Map getSrSpssekisouData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT kojyo,lotno,edaban,tntapesyurui,tntapeno,tntapegenryou,gouki,startdatetime,enddatetime,sekisouzure,"
                + "tantousya,kakuninsya,sekisouzure2,bikou1,bikou2,bikou3,bikou4,bikou5,KCPNO,GoukiCode,TpLot,HakuriSpeed,KanaOndo,"
                + "DAturyoku,DKaatuJikan,CPressAturyoku,CPressKaatuJikan,CPressKankakuSosuu,LastKaaturyoku,LastKaatuJikan,FourSplitTantou,"
                + "STaoreryo1,STaoreryo2,STaoreryo3,STaoreryo4,STsunAve,STsunMax,STsunMin,STsunSiguma,HNGKaisuu,GaikanTantou,CPressKaisuu,"
                + "HNGKaisuuAve,GNGKaisuu,GNGKaisuuAve,tapelotno,taperollno1,taperollno2,taperollno3,genryoukigou,petfilmsyurui,Kotyakugouki,"
                + "Kotyakusheet,ShitaTanshigouki,UwaTanshigouki,ShitaTanshiBukunuki,ShitaTanshi,HakuriKyuin,HakuriClearrance,HakuriCutSpeed,"
                + "ShitaPanchiOndo,UwaPanchiOndo,KaatuJikan,KaatuAturyoku,UwaTanshi,GaikanKakunin1,GaikanKakunin2,GaikanKakunin3,GaikanKakunin4,"
                + "SyoriSetsuu,RyouhinSetsuu,StartTantosyacode,EndTantousyacode,TanshiTapeSyurui,torokunichiji,kosinnichiji,revision "
                + "FROM sr_spssekisou "
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
     * 積層・RSUS工程のデータを取得する。
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @return 工程データ
     * @throws SQLException 例外エラー
     */
    public static Map getSrRsussekData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {
        String sql = "SELECT KOJYO,LOTNO,EDABAN,KCPNO,TNTAPESYURUI,TNTAPENO,TNTAPEGENRYO,KAISINICHIJI,SYURYONICHIJI,GOKI,"
                + "JITUATURYOKU,SEKISOZURE2,TANTOSYA,KAKUNINSYA,INSATUROLLNO,HAPPOSHEETNO,SKJIKAN,TAKUTO,BIKO1,TOROKUNICHIJI,"
                + "KOSINNICHIJI,SKOJYO,SLOTNO,SEDABAN,tapelotno,taperollno1,taperollno2,taperollno3,genryoukigou,petfilmsyurui,"
                + "Kotyakugouki,Kotyakusheet,ShitaTanshigouki,UwaTanshigouki,ShitaTanshiBukunuki,ShitaTanshi,UwaTanshi,"
                + "SyoriSetsuu,RyouhinSetsuu,GaikanKakunin1,GaikanKakunin2,GaikanKakunin3,GaikanKakunin4,EndTantousyacode,"
                + "TanshiTapeSyurui,HNGKaisuu,HNGKaisuuAve,GNGKaisuu,GNGKaisuuAve,bikou2,revision "
                + "FROM sr_rsussek "
                + "WHERE KOJYO = ? AND LOTNO = ? "
                + "AND EDABAN = ? AND revision = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * 印刷積層・RHAPS工程のデータを取得する。
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @return 工程データ
     * @throws SQLException 例外エラー
     */
    public static Map getSrRhapsData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {
        String sql = "SELECT KOJYO,LOTNO,EDABAN,KCPNO,KAISINICHIJI,SYURYONICHIJI,TTAPESYURUI,TTAPELOTNO,TTapeSlipKigo,TTapeRollNo1,"
                + "TTapeRollNo2,TTapeRollNo3,TTapeRollNo4,TTapeRollNo5,TGENRYOKIGO,STSIYO,ESEKISOSIYO,ETAPESYURUI,ETAPEGLOT,ETAPELOT,ETapeSlipKigo,"
                + "ETapeRollNo1,ETapeRollNo2,ETapeRollNo3,ETapeRollNo4,ETapeRollNo5,SPTUDENJIKAN,SKAATURYOKU,SKHEADNO,SUSSKAISUU,ECPASTEMEI,EPASTELOTNO,"
                + "EPASTENENDO,EPASTEONDO,ESEIHANMEI,ESEIHANNO,ESEIMAISUU,ECLEARANCE,ESAATU,ESKEEGENO,ESKMAISUU,ESKSPEED,ESCCLEARANCE,ESKKMJIKAN,ELDSTART,"
                + "ESEIMENSEKI,EMAKUATU,ESLIDERYO,EKANSOONDO,EKANSOJIKAN,CPASTELOTNO,CPASTENENDO,CPASTEONDO,CSEIHANMEI,CSEIHANNO,CSEIMAISUU,CCLEARANCE,CSAATU,"
                + "CSKEEGENO,CSKMAISUU,CSCCLEARANCE,CSKKMJIKAN,CSHIFTINSATU,CLDSTART,CSEIMENSEKI,CSLIDERYO,CKANSOONDO,CKANSOJIKAN,CMAKUATU,AINSATUSRZ1,"
                + "AINSATUSRZ2,AINSATUSRZ3,AINSATUSRZ4,AINSATUSRZ5,AINSATUSRZAVE,UTSIYO,UTTUDENJIKAN,UTKAATURYOKU,STAHOSEI,TICLEARANCE,TISAATU,TISKSPEED,FSTHUX1,"
                + "FSTHUX2,FSTHUY1,FSTHUY2,FSTHSX1,FSTHSX2,FSTHSY1,FSTHSY2,FSTCX1,FSTCX2,FSTCY1,FSTCY2,FSTMUX1,FSTMUX2,FSTMUY1,FSTMUY2,FSTMSX1,FSTMSX2,FSTMSY1,"
                + "FSTMSY2,LSTHUX1,LSTHUX2,LSTHUY1,LSTHUY2,LSTHSX1,LSTHSX2,LSTHSY1,LSTHSY2,LSTCX1,LSTCX2,LSTCY1,LSTCY2,LSTMUX1,LSTMUX2,LSTMUY1,LSTMUY2,LSTMSX1,"
                + "LSTMSX2,LSTMSY1,LSTMSY2,BIKO1,BIKO2,TOROKUNICHIJI,KOSINNICHIJI,GOKI,TTANSISUUU,TTANSISUUS,SHUNKANKANETSUJIKAN,PETFILMSYURUI,KAATURYOKU,"
                + "GAIKANKAKUNIN,SEKIJSSKIRIKAEICHI,SEKIKKSKIRIKAEICHI,KAATUJIKAN,TAPEHANSOUPITCH,TAPEHANSOUKAKUNIN,EMAKUATSUSETTEI,ENEPPUFURYOU,EMAKUATSUAVE,"
                + "EMAKUATSUMAX,EMAKUATSUMIN,NIJIMISOKUTEIPTN,PRNSAMPLEGAIKAN,PRNICHIYOHAKUNAGASA,CTABLECLEARANCE,CMAKUATSUSETTEI,CSKSPEED,CNEPPUFURYOU,KABURIRYOU,"
                + "SGAIKAN,NIJIMISOKUTEISEKISOUGO,SEKISOUHINGAIKAN,SEKISOUZURE,UWAJSSKIRIKAEICHI,SHITAKKSKIRIKAEICHI,TINKSYURYUI,TINKLOT,TGAIKAN,STARTTANTOU,ENDTANTOU,"
                + "TENDDAY,TENDTANTOU,SYORISETSUU,RYOUHINSETSUU,HEADKOUKANTANTOU,SEKISOUJOUKENTANTOU,ESEIHANSETTANTOU,CSEIHANSETTANTOU,DANSASOKUTEITANTOU,revision "
                + "FROM sr_rhaps "
                + "WHERE KOJYO = ? AND LOTNO = ? "
                + "AND EDABAN = ? AND revision = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * ﾌﾟﾚｽ・仮ﾌﾟﾚｽ工程のデータを取得する。
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @return 工程データ
     * @throws SQLException 例外エラー
     */
    public static Map getSrPrepressData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {
        String sql = "SELECT kojyo,lotno,edaban,startdatetime,enddatetime,ondohyoji,aturyokusetteiti,kaatujikan,goki,"
                + "tantosya,kakuninsya,biko1,torokunichiji,kosinnichiji,sagyokubun,setsuu,tansimaisuu,aturyokuhyouji,"
                + "pressside,jissekino,koteicode,kansyouzai,shinkuuhoji,aturyokusetteiti2,kaatujikan2,aturyokusetteiti3,"
                + "kaatujikan3,shinkuudo,Hokanjouken,EndTantousyacode,RyouhinSetsuu,biko2,revision "
                + "FROM sr_prepress "
                + "WHERE KOJYO = ? AND LOTNO = ? "
                + "AND EDABAN = ? AND revision = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * ﾌﾟﾚｽ・真空脱気工程の良品ｾｯﾄ数を取得する。 ※データ無し時は"nodata"の文字列を返す。
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @return 工程データ
     * @throws SQLException 例外エラー
     */
    public static Map getSrPressData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {
        String sql = "SELECT kojyo,lotno,edaban,startdatetime,enddatetime,gouki,ondo,tantousya,kakuninsya,bikou1,"
                + "bikou2,bikou3,bikou4,bikou5,situon,situdo,aturyoku,jikan1,jikan2,atumimin,atumimax,shinkuuhojicheck,"
                + "cerapeel,kansyouzai1,susborad,kansyouzai2,seisuiatupressgouki,yonetujikan1,yonetujikan2,yonetujikan3,"
                + "aturyoku1max,aturyoku2max,mizunureSetsuu,Pressgoreikyakujikan,pressmaeaging,EndTantousyacode,Setsuu,"
                + "RyouhinSetsuu,Presskaisuu,torokunichiji,kosinnichiji,revision "
                + "FROM sr_press "
                + "WHERE KOJYO = ? AND LOTNO = ? "
                + "AND EDABAN = ? AND revision = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());

    }

    /**
     * ﾌﾟﾚｽ・ﾒｶﾌﾟﾚｽ工程のデータを取得する
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @return 工程データ
     * @throws SQLException 例外エラー
     */
    public static Map getSrMekapressData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {
        String sql = "SELECT KOJYO,LOTNO,EDABAN,KCPNO,KAISINICHIJI,SYURYONICHIJI,UKEIRESETSUU,RyouhinSetsuu,GOKI,ONDO,ATURYOKU,"
                + "SITUON,SITUDO,ATUMIMIN,ATUMIMAX,ATUMITANTOSYA,TANTOSYA,KAKUNINSYA,BIKO1,BIKO2,TOROKUNICHIJI,KOSINNICHIJI,"
                + "airnuki,dakkijikan,dakkiaturyoku,EndTantousyacode,revision "
                + "FROM sr_mekapress "
                + "WHERE KOJYO = ? AND LOTNO = ? "
                + "AND EDABAN = ? AND revision = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * ｶｯﾄ・押切ｶｯﾄ工程のデータを取得する。
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @return 工程データ
     * @throws SQLException 例外エラー
     */
    public static Map getSrHapscutData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {
        String sql = "SELECT KOJYO,LOTNO,EDABAN,KCPNO,KAISINICHIJI,SYURYONICHIJI,CUTBAMAISUU,GOKI,CUTTABLEONDO,CUTTANTOSYA,KAKUNINSYA,CHKTANTOSYA,"
                + "BTANTOSYA,ATANTOSYA,UKEIREKOSUU,RYOHINKOSUU,Atumi01,Atumi02,Atumi03,Atumi04,Atumi05,Atumi06,Atumi07,Atumi08,Atumi09,Atumi10,"
                + "ATUMIMIN,ATUMIMAX,BIKO1,BIKO2,BIKO3,BIKO4,TOROKUNICHIJI,KOSINNICHIJI,TENSYASYA,NIJIMICNT,Soujyuryo,Tanijyuryo,cutbashuruicheck,"
                + "cutbachokushindo,cutbasiyoukaisuuST1,cutbasiyoukaisuuST2,programmei,gyoretukakunin,marktorisuu,cuthoseiryou,tableondoset,tableondosoku,"
                + "gaikancheck,hatakasang,syorisetsuu,RyouhinSetsuu,sagyoubasyo,revision "
                + "FROM sr_hapscut "
                + "WHERE KOJYO = ? AND LOTNO = ? "
                + "AND EDABAN = ? AND revision = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * ｶｯﾄ・ﾀﾞｲｼﾝｸﾞｶｯﾄ工程の良品ｾｯﾄ数を取得する。 ※データ無し時は"nodata"の文字列を返す。
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @return 工程データ
     * @throws SQLException 例外エラー
     */
    public static Map getSrCutData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {
        String sql = "SELECT kojyo,lotno,edaban,startdatetime,enddatetime,cutbamaisuu,gouki,cuttableondo,tantousya,kakuninsya,"
                + "bikou1,bikou2,bikou3,bikou4,bikou5,housiki,atumimin,atumimax,cutbashurui,cutmuki,happosheetcolor,kansou,"
                + "hoseihantei,EndTantousyacode,syorisetsuu,RyouhinSetsuu,torokunichiji,kosinnichiji,revision "
                + "FROM sr_cut "
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
     * 生品質検査工程のデータを取得する。
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @return 工程データ
     * @throws SQLException 例外エラー
     */
    public static Map getSrCutcheckData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {
        String sql = "SELECT kojyo,lotno,edaban,startdatetime,enddatetime,tantousya,kakuninsya,AtumiMin,AtumiMax,Atumi01,"
                + "Atumi02,Atumi03,Atumi04,Atumi05,Atumi06,Atumi07,Atumi08,Atumi09,Atumi10,bikou1,bikou2,bikou3,bikou4,bikou5,"
                + "Soujyuryo,Tanijyuryo,gaikankensatantousya,barasshi,joken,barashistartnichiji,batashistarttantousya,"
                + "barashiendnichiji,barashiendtantousya,konamabushi,syorisetsuu,RyouhinSetsuu,budomari,torokunichiji,"
                + "kosinnichiji,revision "
                + "FROM sr_cutcheck "
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
     * ｾｯﾀ詰めのデータを取得する。
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @return 工程データ
     * @throws SQLException 例外エラー
     */
    public static Map getSrSayadumeData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {
        String sql = "SELECT kojyo,lotno,edaban,kcpno,kaishinichiji,syuuryounichiji,sayadumegouki,sayasuu,kosuu,tantousya,"
                + "jissekino,bikou1,bikou2,bikou3,bikou4,bikou5,tourokunichiji,koushinnichiji,settertumeryou,settertumehouhou,"
                + "settersyurui,konamabushi,StartKakuninsyacode,EndTantousyacode,revision "
                + "FROM sr_sayadume "
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
     * 1次脱脂(Air_窒素)のデータを取得する。
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @param jissekino 実績No(検索キー)
     * @return 工程データ
     * @throws SQLException 例外エラー
     */
    public static Map getSrYobikanData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev, int jissekino) throws SQLException {
        String sql = "SELECT kojyo,lotno,edaban,kcpno,kosuu,kaisinichiji,syuryoyoteinichiji,syuryonichiji,"
                + "yobikangoki,sayasuu,yobikanjikan,yobikanondo,peakjikan,kaisitantosya,syuryotantosya,jissekino,"
                + "biko1,biko2,biko3,biko4,biko5,torokunichiji,kosinnichiji,NijiKaishiNichiji,NijiGoki,programno,"
                + "tounyusettersuu,StartKakuninsyacode,kaisyusettersuu,dasshisyurui,revision "
                + "FROM sr_yobikan "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND revision = ? "
                + "AND jissekino = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        params.add(jissekino);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }
    
    /**
     * 2次脱脂(ﾍﾞﾙﾄ)のデータを取得する。
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @return 工程データ
     * @throws SQLException 例外エラー
     */
    public static Map getSrNijidasshiData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {
        
        // 画面実装時に再確認
        String sql = "SELECT kojyo,lotno,edaban,kcpno,ukeiresettamaisuu,kaisinichiji,StartTantosyacode,StartKakuninsyacode,tounyusettasuu,"
                + "Nijidasshigouki,NijidasshisetteiPT,Nijidasshikeepondo,Nijidasshispeed,syuuryounichiji,EndTantosyacode,kaishuusettasuu,"
                + "bikou1,bikou2,tourokunichiji,koushinnichiji,revision "
                + "FROM sr_nijidasshi "
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
     * 焼成のデータを取得する。
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @param jissekino 実績No(検索キー)
     * @return 工程データ
     * @throws SQLException 例外エラー
     */
    public static Map getSrSyoseiData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev, int jissekino) throws SQLException {
        String sql = "SELECT kojyo,lotno,edaban,jissekino,kcpno,kosuu,genryohinsyumei,genryogroup,skaisinichiji,ssyuryonichiji,bprogramno,"
                + "syoseiondo,goki,ssettermaisuu,nyurodaibanmaisuu,skaisitantosya,ssyuryotantosya,biko1,biko2,biko3,biko4,biko5,bkaisinichiji,"
                + "bsyuryonichiji,bsettermaisuu,potsuu,potno,btantosya,biko6,biko7,torokunichiji,kosinnichiji,SankaGoki,SankaOndo,SankaSyuryoNichiJi,"
                + "tounyusettasuu,setteipattern,dansuu,gaikancheck,kaishusettasuu,StartKakuninsyacode,nijidasshigouki,NijidasshisetteiPT,"
                + "Nijidasshikeepondo,Nijidasshispeed,peakondo,syoseispeed,syoseipurge,saisankagouki1,saisankagouki2,saisankasetteiPT,saisankakeepondo,saisankaCsokudo,saisankagogaikan,syoseisyurui,revision "
                + "FROM sr_syosei "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND revision = ? "
                + "AND jissekino = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        params.add(jissekino);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }
      
    /**
     * 再酸化のデータを取得する。
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @param jissekino 実績No(検索キー)
     * @return 工程データ
     * @throws SQLException 例外エラー
     */
    public static Map getSrSaisankaData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev, int jissekino) throws SQLException {
        String sql = "SELECT kojyo,lotno,edaban,jissekino,KCPNO,ukeiresettamaisuu,siteisaisaka,tounyusettasuu,gouki,setteipattern,"
                + "keepondo,atogaikan,kaishusettasuu,kaisinichiji,StartTantosyacode,StartKakuninsyacode,syuuryounichiji,"
                + "EndTantosyacode,bikou1,bikou2,tourokunichiji,koushinnichiji,revision "
                + "FROM sr_saisanka "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND revision = ? "
                + "AND jissekino = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        params.add(jissekino);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }
    
    /**
     * ﾊﾞﾚﾙのデータを取得する。
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @param jissekino 実績No(検索キー)
     * @return 工程データ
     * @throws SQLException 例外エラー
     */
    public static Map getSrBarrel1Data(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev, int jissekino) throws SQLException {
        String sql = "SELECT kojyo,lotno,edaban,jissekino,kcpno,bkaisinichiji,bsyuryonichiji,bjyokensetteimode,bjyokensyusokudo,"
                + "bgoki,bjikan,potsuu,chiphahenkakunin,potkakunin,btantosya,ptantosya,bpotno1,kankaisinichiji,kansyuryonichiji,"
                + "kantantosya,mediasenbetu,bpotno2,keinichiji,ukeirekosuu,tanijyuryo,ryohinkosuu,furyosuu,budomari,keitantosya,"
                + "biko1,biko2,torokunichiji,kosinNichiji,kenma,kenmazairyo,kenmazaisyurui,tamaishisyurui,tamaishiryou,gaikancheck,"
                + "StartKakuninsyacode,EndTantosyacode,revision "
                + "FROM sr_barrel1 "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND revision = ? "
                + "AND jissekino = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        params.add(jissekino);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }
    
    /**
     * ﾊﾞﾚﾙ・計数のデータを取得する。
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @param jissekino 実績No(検索キー)
     * @return 工程データ
     * @throws SQLException 例外エラー
     */
    public static Map getSrSyoseikeisuuData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev, int jissekino) throws SQLException {
        String sql = "SELECT kojyo,lotno,edaban,jissekino,kcpno,ukeirekosuu,tanijyuryo,soujuryou,ryohinkosuu,keinichiji,"
                + "keitantosya,budomari,biko1,biko2,barrelgohantei,torokunichiji,kosinNichiji,revision "
                + "FROM sr_syoseikeisuu "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND revision = ? "
                + "AND jissekino = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        params.add(jissekino);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }
    
    /**
     * 磁器QCのデータを取得する。
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @param jissekino 実績No(検索キー)
     * @return 工程データ
     * @throws SQLException 例外エラー
     */
    public static Map getSrJikiqcData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev, int jissekino) throws SQLException {
        String sql = "SELECT kojyo,lotno,edaban,jissekino,kcpno,gouhihantei,checkbi,checktantousyacode,sijinaiyou1,"
                + "sijinaiyou2,sijinaiyou6,torokunichiji,kosinnichiji,revision "
                + "FROM sr_jikiqc "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND revision = ? "
                + "AND jissekino = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        params.add(jissekino);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }
    
    /**
     * 印刷・DP2のデータを取得する。
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @param jissekino 実績No(検索キー)
     * @return 工程データ
     * @throws SQLException 例外エラー
     */
    public static Map getSrDpprintData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev, int jissekino) throws SQLException {
        String sql = "SELECT kojyo,lotno,edaban,kaisuu,kcpno,kouteikubun,tapelotno,petfilmsyurui,taperollno1,taperollno2,taperollno3,"
                + "pastelotno,pastenendo,pasteondo,pkokeibun1,pastelotno2,handoumei,handouno,handoumaisuu,bladeno,bladegaikan,BladeATu,"
                + "AtudoNo,AtudoMaisuu,AtuDoATu,gouki,kansouondo,kansouondo2,kansouondo3,kansouondo4,kansouondo5,hansouspeed,startdatetime,"
                + "tantousya,kakuninsya,makuatuave_start,makuatumax_start,makuatumin_start,makuatucv_start,nijimikasure_start,"
                + "start_ptn_dist_x,start_ptn_dist_y,TensionS_sum,TensionStemae,TensionSoku,enddatetime,tanto_end,printmaisuu,makuatuave_end,"
                + "makuatumax_end,makuatumin_end,makuatucv_end,nijimikasure_end,end_ptn_dist_x,end_ptn_dist_y,TensionE_sum,TensionEtemae,"
                + "TensionEoku,genryoukigou,bikou1,bikou2,torokunichiji,kosinnichiji,revision "
                + "FROM sr_dpprint "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND revision = ? "
                + "AND kaisuu = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        params.add(jissekino);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }
    
    /**
     * 外部電極・疎水処理のデータを取得する。
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @param jissekino 実績No(検索キー)
     * @return 工程データ
     * @throws SQLException 例外エラー
     */
    public static Map getSrGdsosuiData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev, int jissekino) throws SQLException {
        String sql = "SELECT kojyo,lotno,edaban,kcpno,syorisuu,syorigoki,chargeryou,traymaisuu,programno,kaisinichiji,"
                + "StartTantosyacode,StartKakuninsyacode,syuuryounichiji,EndTantosyacode,sagyoubasyo,biko1,biko2,kaisuu,torokunichiji,"
                + "kosinnichiji,revision "
                + "FROM sr_gdsosui "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND revision = ? "
                + "AND kaisuu = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        params.add(jissekino);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }
    
    /**
     * 外部電極・外部電極塗布(三端子・4端子)のデータを取得する。
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @param jissekino 実績No(検索キー)
     * @return 工程データ
     * @throws SQLException 例外エラー
     */
    public static Map getSrGdtermtData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev, int jissekino) throws SQLException {
        String sql = "SELECT kojyo,lotno,edaban,kaisuu,kcpno,syorisuu,lotpre,tofugoki,pastehinmei,pastelotno,"
                + "pastesaiseikaisuu,pastenendo,setteisya,startdatetime,StartTantosyacode,StartKakuninsyacode,carriertape,"
                + "insatsuhaba,mawarikomi,itizure,tansikanhaba,tofugogaikan,syorikosuu,juryou,enddatetime,EndTantosyacode,"
                + "sagyobasyo,biko1,biko2,torokunichiji,kosinnichiji,revision "
                + "FROM sr_gdtermt "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND revision = ? "
                + "AND kaisuu = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        params.add(jissekino);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }
    
    /**
     * 外部電極・外部電極塗布のデータを取得する。
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @param jissekino 実績No(検索キー)
     * @return 工程データ
     * @throws SQLException 例外エラー
     */
    public static Map getSrGdtermData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev, int jissekino) throws SQLException {
        String sql = "SELECT kojyo,lotno,edaban,lotpre,kcpno,suuryou,kyakusaki,sagyobasyo,gouki1,setteisya1,gouki2,setteisya2,pastehinmei,"
                + "pastelotno,pastesaiseikaisuu,pastekoukanjikan,pastenendo,pasteondo,pastekansannendo,pastekigen,startdatetime,enddatetime,"
                + "senkougaikan,jikilsunpou,jikitsunpou,psunpou1a,psunpou1b,psunpou1c,psunpou1d,psunpou1e,psunpouave1,psunpourange1,lsunpou1a,"
                + "lsunpou1b,lsunpou1c,lsunpou1d,lsunpou1e,tanmenatsumi1,sunpouhantei1,psunpou2a,psunpou2b,psunpou2c,psunpou2d,psunpou2e,psunpouave2,"
                + "psunpourange2,lsunpou2a,lsunpou2b,lsunpou2c,lsunpou2d,lsunpou2e,tanmenatsumi2,sunpouhantei2,pasteatsumi1,pasteatsumi2,dipjigusize,"
                + "dipjigumaisuu,dipgogaikankekka,syochinaiyou,inkuatsumia,inkuatsumib,setteijyouken,bikou1,bikou2,bikou3,psunpou1f,psunpou1g,psunpou1h,"
                + "psunpou1i,psunpou1j,lsunpou1f,lsunpou1g,lsunpou1h,lsunpou1i,lsunpou1j,psunpou2f,psunpou2g,psunpou2h,psunpou2i,psunpou2j,lsunpou2f,"
                + "lsunpou2g,lsunpou2h,lsunpou2i,lsunpou2j,lsunpouave1,lsunpouave2,pastekokeibun,Dip1AtamaDashiSettei,Dip1Clearance,Dip1SkeegeSettei,"
                + "Dip1BlotClearance,Dip1Reveler,Dip2AtamaDashiSettei,Dip2Clearance,Dip2SkeegeSettei,Dip2BlotClearance,Dip2Reveler,psunpou2k,psunpou2l,"
                + "psunpou2m,psunpou2n,psunpou2o,psunpou2p,psunpou2q,psunpou2r,psunpou2s,psunpou2t,psunpou2u,psunpou2v,psunpou2w,psunpou2x,psunpou2y,"
                + "psunpou2z,psunpou2aa,psunpou2ab,psunpou2ac,psunpou2ad,psunpou2ae,psunpou2af,psunpou2ag,psunpou2ah,psunpou2ai,psunpou2aj,psunpou2ak,"
                + "psunpou2al,psunpou2am,psunpou2an,lsunpou2k,lsunpou2l,lsunpou2m,lsunpou2n,lsunpou2o,lsunpou2p,lsunpou2q,lsunpou2r,lsunpou2s,lsunpou2t,"
                + "lsunpou2u,lsunpou2v,lsunpou2w,lsunpou2x,lsunpou2y,lsunpou2z,lsunpou2aa,lsunpou2ab,lsunpou2ac,lsunpou2ad,lsunpou2ae,lsunpou2af,lsunpou2ag,"
                + "lsunpou2ah,lsunpou2ai,lsunpou2aj,lsunpou2ak,lsunpou2al,lsunpou2am,lsunpou2an,lsunpourange2,lsunpoumin2,lsunpoumax2,wtsunpou2a,wtsunpou2b,"
                + "wtsunpou2c,wtsunpou2d,wtsunpou2e,wtsunpou2f,wtsunpou2g,wtsunpou2h,wtsunpou2i,wtsunpou2j,wtsunpou2k,wtsunpou2l,wtsunpou2m,wtsunpou2n,"
                + "wtsunpou2o,wtsunpou2p,wtsunpou2q,wtsunpou2r,wtsunpou2s,wtsunpou2t,wtsunpou2u,wtsunpou2v,wtsunpou2w,wtsunpou2x,wtsunpou2y,wtsunpou2z,"
                + "wtsunpou2aa,wtsunpou2ab,wtsunpou2ac,wtsunpou2ad,wtsunpou2ae,wtsunpou2af,wtsunpou2ag,wtsunpou2ah,wtsunpou2ai,wtsunpou2aj,wtsunpou2ak,"
                + "wtsunpou2al,wtsunpou2am,wtsunpou2an,wtsunpouave2,wtsunpourange2,wtsunpoumin2,wtsunpoumax2,psunpoumin2,psunpoumax2,setubisyurui,tofukaisuu,"
                + "hojijigu,nentyakusheetlot1,nentyakusheetlot2,tofujigutorikosuu,StartTantosyacode,StartKakuninsyacode,juryou,syorikosuu,EndTantosyacode,"
                + "pasteatsumi1ji,pasteatsumi2ji,atsumiinkua,atsumiinkub,kaisuu,torokunichiji,kosinnichiji,revision "
                + "FROM sr_gdterm "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND revision = ? "
                + "AND kaisuu = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        params.add(jissekino);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }
    
    /**
     * 外部電極・外部電極焼成(ｻﾔ詰め)のデータを取得する。
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @param jissekino 実績No(検索キー)
     * @return 工程データ
     * @throws SQLException 例外エラー
     */
    public static Map getSrGdsayadumeData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev, int jissekino) throws SQLException {
        String sql = "SELECT kojyo,lotno,edaban,kaisuu,kcpno,tokuisaki,lotkubuncode,ownercode,lotpre,syorisuu,"
                + "sayadumehouhou,konamabushi,juryou,bnfunmaturyou,bnfunmaturyoukakunin,sayasussyurui,sayamaisuukeisan,"
                + "SJyuuryouRangeMin,SJyuuryouRangeMax,sayajyuuryou,sayamaisuu,saysusacharge,startdatetime,StartTantosyacode,"
                + "StartKakuninsyacode,enddatetime,EndTantosyacode,biko1,biko2,torokunichiji,kosinnichiji,revision "
                + "FROM sr_gdsayadume "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND revision = ? "
                + "AND kaisuu = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        params.add(jissekino);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }
    
    /**
     * 外部電極・外部電極焼成(脱ﾊﾞｲ)のデータを取得する。
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @param jissekino 実績No(検索キー)
     * @return 工程データ
     * @throws SQLException 例外エラー
     */
    public static Map getSrGddatubaiData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev, int jissekino) throws SQLException {
        String sql = "SELECT kojyo,lotno,edaban,kaisuu,kcpno,tokuisaki,lotkubuncode,ownercode,lotpre,syorisuu,"
                + "gouki,ondo,jikan,ptnno,sayamaisuu,startdatetime,StartTantosyacode,StartKakuninsyacode,enddatetime,"
                + "EndTantosyacode,biko1,biko2,torokunichiji,kosinnichiji,revision "
                + "FROM sr_gddatubai "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND revision = ? "
                + "AND kaisuu = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        params.add(jissekino);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }
    
    /**
     * 外部電極・外部電極焼成(焼成)のデータを取得する。
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @param jissekino 実績No(検索キー)
     * @return 工程データ
     * @throws SQLException 例外エラー
     */
    public static Map getSrGdyakitukeData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev, int jissekino) throws SQLException {
        String sql = "SELECT kojyo,lotno,edaban,kaisuu,kcpno,tokuisaki,lotkubuncode,ownercode,lotpre,"
                + "syorisuu,gouki,sayamaisuu,ondo,okurispeed,startdatetime,StartTantosyacode,StartKakuninsyacode,"
                + "enddatetime,EndTantosyacode,biko1,biko2,biko3,torokunichiji,kosinnichiji,revision "
                + "FROM sr_gdyakituke "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND revision = ? "
                + "AND kaisuu = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        params.add(jissekino);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }
    
    /**
     * 外部電極・外部電極焼成(焼成外観)のデータを取得する。
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @param jissekino 実績No(検索キー)
     * @return 工程データ
     * @throws SQLException 例外エラー
     */
    public static Map getSrGdyakitukegaikanData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev, int jissekino) throws SQLException {
        String sql = "SELECT kojyo,lotno,edaban,kaisuu,kcpno,tokuisaki,lotkubuncode,ownercode,lotpre,"
                + "syorisuu,gaikan,ryohinjuryou,furyoujuryou,furyouritsu,gaikandatetime,gaikantantosyacode,"
                + "biko1,biko2,torokunichiji,kosinnichiji,revision "
                + "FROM sr_gdyakitukegaikan "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND revision = ? "
                + "AND kaisuu = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        params.add(jissekino);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }
    
    /**
     * 外部電極・外部電極焼成(樹脂電極硬化)のデータを取得する。
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @param jissekino 実績No(検索キー)
     * @return 工程データ
     * @throws SQLException 例外エラー
     */
    public static Map getSrGdjusikoukaData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev, int jissekino) throws SQLException {
        String sql = "SELECT kojyo,lotno,edaban,kaisuu,kcpno,tokuisaki,lotkubuncode,ownercode,lotpre,syorisuu,gouki,ondo,"
                + "jikan,ptnno,startdatetime,StartTantosyacode,StartKakuninsyacode,enddatetime,EndTantosyacode,biko1,biko2,"
                + "torokunichiji,kosinnichiji,revision "
                + "FROM sr_gdjusikouka "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND revision = ? "
                + "AND kaisuu = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        params.add(jissekino);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }
    
    
    /**
     * 外部電極・外部電極洗浄(撥水処理)のデータを取得する。
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @param jissekino 実績No(検索キー)
     * @return 工程データ
     * @throws SQLException 例外エラー
     */
    public static Map getSrGdhassuiData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev, int jissekino) throws SQLException {
        String sql = "SELECT kojyo,lotno,edaban,kaisuu,kcpno,tokuisaki,lotkubuncode,ownercode,lotpre,syorisuu,sinsekijikan,senjoujikan,"
                + "methanolkoukanjikan,methanolkoukantantousya,hassuidatetime,hassuitantosyacode,kansoujikan,kansouondo,kansoudatetime,"
                + "kansoutantosyacode,biko1,biko2,torokunichiji,kosinnichiji,revision "
                + "FROM sr_gdhassui "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND revision = ? "
                + "AND kaisuu = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        params.add(jissekino);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }
    
    /**
     * 外部電極・外部電極洗浄(ﾊﾞﾚﾙ)のデータを取得する。
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @param jissekino 実績No(検索キー)
     * @return 工程データ
     * @throws SQLException 例外エラー
     */
    public static Map getSrGdnijibarrelData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev, int jissekino) throws SQLException {
        String sql = "SELECT kojyo,lotno,edaban,kaisuu,kcpno,tokuisaki,lotkubuncode,ownercode,lotpre,syorisuu,gouki,"
                + "syorijikan,kaitensuu,potsuu,methanolkoukanjikan,methanolkoukantantousya,methanolkoukanpotruikeisuu,"
                + "startdatetime,StartTantosyacode,StartKakuninsyacode,enddatetime,EndTantosyacode,biko1,biko2,biko3,"
                + "torokunichiji,kosinnichiji,revision "
                + "FROM sr_gdnijibarrel "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND revision = ? "
                + "AND kaisuu = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        params.add(jissekino);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }
    
    /**
     * 外部電極・外部電極洗浄(超音波)のデータを取得する。
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @param jissekino 実績No(検索キー)
     * @return 工程データ
     * @throws SQLException 例外エラー
     */
    public static Map getSrGdsenjouData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev, int jissekino) throws SQLException {
        String sql = "SELECT kojyo,lotno,edaban,kaisuu,kcpno,tokuisaki,lotkubuncode,ownercode,lotpre,syorisuu,gouki,jikan,"
                + "chargeroyu,vatsuu,methanolkoukanjikan,methanolkoukantantousya,startdatetime,StartTantosyacode,StartKakuninsyacode,"
                + "enddatetime,EndTantosyacode,biko1,biko2,torokunichiji,kosinnichiji,revision "
                + "FROM sr_gdsenjou "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND revision = ? "
                + "AND kaisuu = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        params.add(jissekino);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }
    
    /**
     * 外部電極・外部電極洗浄(乾燥)のデータを取得する。
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @param jissekino 実績No(検索キー)
     * @return 工程データ
     * @throws SQLException 例外エラー
     */
    public static Map getSrGdkansouData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev, int jissekino) throws SQLException {
        String sql = "SELECT kojyo,lotno,edaban,kaisuu,kcpno,tokuisaki,lotkubuncode,ownercode,lotpre,syorisuu,gouki,startdatetime,"
                + "StartTantosyacode,StartKakuninsyacode,enddatetime,EndTantosyacode,furuisenbetu,gaikan,biko1,biko2,torokunichiji,"
                + "kosinnichiji,revision "
                + "FROM sr_gdkansou "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND revision = ? "
                + "AND kaisuu = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        params.add(jissekino);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }
    
    /**
     * 外部電極・親水処理のデータを取得する。
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @param jissekino 実績No(検索キー)
     * @return 工程データ
     * @throws SQLException 例外エラー
     */
    public static Map getSrGdsinsuiData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev, int jissekino) throws SQLException {
        String sql = "SELECT kojyo,lotno,edaban,kcpno,syorisuu,syorigoki,chargeryou,traymaisuu,programno,kaisinichiji,StartTantosyacode,"
                + "StartKakuninsyacode,syuuryounichiji,EndTantosyacode,sagyoubasyo,biko1,biko2,kaisuu,torokunichiji,kosinnichiji,revision "
                + "FROM sr_gdsinsui "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND revision = ? "
                + "AND kaisuu = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        params.add(jissekino);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * 外部電極・計数のデータを取得する。
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @param jissekino 実績No(検索キー)
     * @return 工程データ
     * @throws SQLException 例外エラー
     */
    public static Map getSrGdkeisuuData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev, int jissekino) throws SQLException {
        String sql = "SELECT kojyo,lotno,edaban,kaisuu,kcpno,tokuisaki,lotkubuncode,ownercode,lotpre,syorisuu,tanijyuryo,"
                + "soujuryou,ryohinkosuu,keinichiji,keitantosya,budomari,biko1,biko2,torokunichiji,kosinnichiji,revision "
                + "FROM sr_gdkeisuu "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND revision = ? "
                + "AND kaisuu = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        params.add(jissekino);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }
    
    /**
     * 外部電極・ﾒｯｷ品質検査のデータを取得する。
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @param jissekino 実績No(検索キー)
     * @return 工程データ
     * @throws SQLException 例外エラー
     */
    public static Map getSrMekkiData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev, int jissekino) throws SQLException {
        String sql = "SELECT kojyo,lotno,edaban,kcpno,ukeiresuu,domekosuu,gouki,tantousya,mekkikaishinichiji,mekkijyoukennia,"
                + "mekkijyoukenniam,mekkijyoukensna,mekkijyoukensnam,shukkakosuu,budomari,makuatsunimin,makuatsunimax,makuatsuniave,"
                + "makuatsunistd,makuatsusnmin,makuatsusnmax,makuatsusnave,makuatsusnstd,nurekensakekka,tainetsukensakekka,gaikankensakekka,"
                + "bikou1,bikou2,bikou3,jissekino,tourokunichiji,koushinnichiji,domemeisai,tyoseimaeph1,tyoseigoph1,tyoseijikan1,tyoseimaeph2,"
                + "tyoseigoph2,tyoseijikan2,tsunpou,barrelno,makuatsunicpl,makuatsusncpl,sokuteinichiji,makuatsunicv,makuatsusncv,kensanichiji,"
                + "kensatantousya,makuatsutantosya,kaishinichiji_sn,tokuisaki,lotkubuncode,ownercode,ukeiretannijyuryo,ukeiresoujyuryou,"
                + "mekkibasyo,mekkibasyosetubi,mekkisyuryounichiji,syuryousya,kensatannijyuryo,kensasoujyuryou,netusyorijyouken,netusyorikaisinichiji,"
                + "netusyoritantousya,jisyakusenbetukaisinichiji,jisyakusenbetutantousya,ijouhakkou,ijourank,makuatsukakunin,testhin,tsunpouave,"
                + "mekkisyurui,revision "
                + "FROM sr_mekki "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND revision = ? "
                + "AND jissekino = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        params.add(jissekino);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }
    
       
    /**
     * 外部電極・ﾒｯｷ真空乾燥のデータを取得する。
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @param jissekino 実績No(検索キー)
     * @return 工程データ
     * @throws SQLException 例外エラー
     */
    public static Map getSrMksinkuukansouData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev, int jissekino) throws SQLException {
        String sql = "SELECT GoukiCode,Kojyo,LotNo,EdaBan,Syorikaisuu,KCPNO,TantousyaCode,KansouJikan,Ondo,Sinkuudo,KaisiNichiji,"
                + "Bikou,TourokuNichiji,KousinNichiji,tokuisaki,lotkubuncode,ownercode,syorisuu,sagyoubasyo,barrelkaishinichiji,"
                + "barreltantousya,bikou2,revision "
                + "FROM sr_mksinkuukansou "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND revision = ? "
                + "AND Syorikaisuu = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        params.add(jissekino);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }
    
}
