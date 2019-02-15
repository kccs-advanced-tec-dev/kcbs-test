/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.List;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2019/01/27<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	KCCS D.Yanagida<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 履歴検索画面の列情報データをJsonファイルから生成します。
 *
 * @author KCCS D.Yanagida
 * @since 2019/01/27
 */
public class ColumnInfoParser {
    /**
     * 
     * @param jsonFile
     * @return 
     * @throws java.lang.Throwable 
     */
    public List<ColumnInformation> parseColumnJson(File jsonFile) throws Throwable {
        List<ColumnInformation> columnList =
                new ObjectMapper().readValue(jsonFile, new TypeReference<List<ColumnInformation>>(){});
        
        return columnList;
    }
}
