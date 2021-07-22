/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo201;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.sql.DataSource;
import jp.co.kccs.xhd.model.GXHDO201B040AModel;
import jp.co.kccs.xhd.util.DBUtil;
import jp.co.kccs.xhd.util.ErrUtil;
import jp.co.kccs.xhd.util.StringUtil;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.lang3.StringUtils;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質情報管理システム<br>
 * <br>
 * 変更日	2021/07/06<br>
 * 計画書No	MB2106-DS017<br>
 * 変更者	KCSS gc<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 品質DB履歴検索・履歴情報画面クラスです。
 *
 * @author KCSS gc
 * @since  2021/07/06
 */
@SessionScoped
@Named("beanGXHDO201B040A")
public class GXHDO201B040A implements Serializable {

    /**
     * @return the LOGGER
     */
    public static Logger getLOGGER() {
        return LOGGER;
    }

    private static final Logger LOGGER = Logger.getLogger(GXHDO201B040A.class.getName());

    /**
     * DataSource(DocumentServer)
     */
    @Resource(mappedName = "jdbc/DocumentServer")
    private transient DataSource dataSourceDocServer;

    /**
     * ﾛｯﾄNo
     */
    private String lotno;


    /** 一覧表示データ */
    private List<GXHDO201B040AModel> listData = null;
    

    
    /**
     * コンストラクタ
     */
    public GXHDO201B040A() {
    }

    /**
     * ﾛｯﾄNo
     *
     * @return the lotno
     */
    public String getLotno() {
        return lotno;
    }

    /**
     * ﾛｯﾄNo
     *
     * @param lotno the lotno to set
     */
    public void setLotno(String lotno) {
        this.lotno = lotno;
    }


    /**
     * 一覧表示データ取得
     * @return 一覧表示データ
     */
    public List<GXHDO201B040AModel> getListData() {
        return listData;
    }

    /**
     * 一覧表示データ件数取得
     * @return 検索結果件数
     */
    public long selectListDataCount() {
         long count = 0;
          try {
            QueryRunner queryRunner = new QueryRunner(dataSourceDocServer);
            String sql = "SELECT COUNT(LOTNO) AS CNT "
                    + "FROM fxhdd09 "
                    + " WHERE ( kojyo = ?) "
                    + " AND ( lotno = ?) "
                    + " AND ( edaban = ?)"
                    + " AND (deleteflag = 0)";

            // パラメータ設定
            List<Object> params = createSearchParam();

            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            Map result = queryRunner.query(sql, new MapHandler(), params.toArray());
            count = (long) result.get("CNT");
        } catch (SQLException ex) {
            count = 0;
            listData = new ArrayList<>();
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
        }
        return count;
    }
   
   /**
     * 一覧表示データ検索
     */
    public void selectListData() {
        try {
            QueryRunner queryRunner = new QueryRunner(dataSourceDocServer);
            //検査場所データリスト  
            String sql = "SELECT kojyo"
                    + ", lotno"
                    + ", edaban"
                    + ", kaisu"
                    + ", rireki_start "
                    + ", rireki_end"
                    + ", bin1low"
                    + ", bin1high"
                    + ", bin1countersuu"
                    + ", bin2low"
                    + ", bin2high"
                    + ", bin2countersuu"
                    + ", bin3low"
                    + ", bin3high"
                    + ", bin3countersuu"
                    + ", bin4low"
                    + ", bin4high"
                    + ", bin4countersuu"
                    + ", bin5low"
                    + ", bin5high"
                    + ", bin5countersuu"
                    + ", bin6low"
                    + ", bin6high"
                    + ", bin6countersuu"
                    + ", bin7low"
                    + ", bin7high"
                    + ", bin7countersuu"
                    + ", bin8low"
                    + ", bin8high"
                    + ", bin8countersuu"
                    + "  FROM fxhdd09 "
                    + " WHERE ( kojyo = ?) "
                    + " AND   ( lotno = ?) "
                    + " AND   ( edaban = ?)"
                    + " AND (deleteflag = 0) "
                    + " ORDER BY kaisu ASC";
   
                // パラメータ設定
                List<Object> params = createSearchParam();
                // モデルクラスとのマッピング定義
                Map<String, String> mapping = new HashMap<>();

                mapping.put("kojyo", "kojyo");//工場ｺｰﾄ
                mapping.put("lotno", "lotno");//ﾛｯﾄNo
                mapping.put("edaban", "edaban");//枝番
                mapping.put("kaisu", "kaisu");//回数
                mapping.put("rireki_start", "rirekistart");//ｶｯﾄ値履歴開始
                mapping.put("rireki_end", "rirekiend");//ｶｯﾄ値履歴終了
                mapping.put("bin1low", "bin1low");//ｶｯﾄ値履歴BIN1カット値下限
                mapping.put("bin1high", "bin1high");//ｶｯﾄ値履歴BIN1カット値上限
                mapping.put("bin1countersuu", "bin1countersuu");//ｶｯﾄ値履歴BIN1カウンター数
                mapping.put("bin2low", "bin2low");//ｶｯﾄ値履歴BIN2カット値下限
                mapping.put("bin2high", "bin2high");//ｶｯﾄ値履歴BIN2カット値上限
                mapping.put("bin2countersuu", "bin2countersuu");//ｶｯﾄ値履歴BIN2カウンター数
                mapping.put("bin3low", "bin3low");//ｶｯﾄ値履歴BIN3カット値下限
                mapping.put("bin3high", "bin3high");//ｶｯﾄ値履歴BIN3カット値上限
                mapping.put("bin3countersuu", "bin3countersuu");//ｶｯﾄ値履歴BIN3カウンター数
                mapping.put("bin4low", "bin4low");//ｶｯﾄ値履歴BIN4カット値下限
                mapping.put("bin4high", "bin4high");//ｶｯﾄ値履歴BIN4カット値上限
                mapping.put("bin4countersuu", "bin4countersuu");//ｶｯﾄ値履歴BIN4カウンター数
                mapping.put("bin5low", "bin5low");//ｶｯﾄ値履歴BIN5カット値下限
                mapping.put("bin5high", "bin5high");//ｶｯﾄ値履歴BIN5カット値上限
                mapping.put("bin5countersuu", "bin5countersuu");//ｶｯﾄ値履歴BIN5カウンター数
                mapping.put("bin6low", "bin6low");//ｶｯﾄ値履歴BIN6カット値下限
                mapping.put("bin6high", "bin6high");//ｶｯﾄ値履歴BIN6カット値上限
                mapping.put("bin6countersuu", "bin6countersuu");//ｶｯﾄ値履歴BIN6カウンター数
                mapping.put("bin7low", "bin7low");//ｶｯﾄ値履歴BIN7カット値下限
                mapping.put("bin7high", "bin7high");//ｶｯﾄ値履歴BIN7カット値上限
                mapping.put("bin7countersuu", "bin7countersuu");//ｶｯﾄ値履歴BIN7カウンター数
                mapping.put("bin8low", "bin8low");//ｶｯﾄ値履歴BIN8カット値下限
                mapping.put("bin8high", "bin8high");//ｶｯﾄ値履歴BIN8カット値上限
                mapping.put("bin8countersuu", "bin8countersuu");//ｶｯﾄ値履歴BIN8カウンター数

                BeanProcessor beanProcessor = new BeanProcessor(mapping);
                RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
                ResultSetHandler<List<GXHDO201B040AModel>> beanHandler = 
                    new BeanListHandler<>(GXHDO201B040AModel.class, rowProcessor);
                DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
                listData = queryRunner.query(sql, beanHandler, params.toArray());

        } catch (SQLException ex) {
            listData = new ArrayList<>();
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
        }
    }

    /**
     * 検索パラメータ生成
     * @return パラメータ
     */
    private List<Object> createSearchParam() {
        // パラメータ設定
        //検査場所データリスト
        String paramKojo = StringUtils.substring(this.lotno, 0, 3);
        String paramLotNo = StringUtils.substring(this.lotno, 3, 11);
        String paramEdaban = StringUtil.blankToNull(StringUtils.substring(this.lotno, 11, 14));
        List<Object> params = new ArrayList<>();
        
        params.addAll(Arrays.asList( paramKojo));
        params.addAll(Arrays.asList( paramLotNo));
        params.addAll(Arrays.asList( paramEdaban));
        return params;
    }

}
