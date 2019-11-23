/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.common.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import jp.co.kccs.xhd.common.ColumnInformation;
import jp.co.kccs.xhd.util.StringUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

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
 * 変更日	2019/11/18<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	複数シートを書き込めるように対応<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 履歴検索画面の一覧データをExcelファイルに出力します。
 *
 * @author KCCS D.Yanagida
 * @since 2019/01/27
 */
public class ExcelExporter {
    /**
     * Excelファイル生成
     * 
     * @param <T> 一覧データのジェネリクス
     * @param data 一覧データ
     * @param columnInfo Excel出力定義情報
     * @param tempDir 一時出力フォルダ
     * @param sheetName シート名
     * @return 出力ファイル
     * @throws Throwable 
     */
    public static <T> File outputExcel(List<T> data, List<ColumnInformation> columnInfo, String tempDir, String sheetName) throws Throwable {
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        SXSSFSheet sheet = (SXSSFSheet) workbook.createSheet();
        //sheet.trackAllColumnsForAutoSizing();
        
        workbook.setSheetName(0, sheetName);

        int rowIdx = 0;

        // ヘッダ行出力
        CellStyle headerCellStyle = createHeaderCellStyle(workbook);

        int headerCol = 0;
        SXSSFRow headerRow = (SXSSFRow)sheet.createRow(rowIdx++);
        for (ColumnInformation column : columnInfo) {
            SXSSFCell cell = (SXSSFCell) headerRow.createCell(headerCol++);
            cell.setCellStyle(headerCellStyle);
            cell.setCellValue(column.getColumnname());
        }            

        // データ行出力
        Map<Integer, CellStyle> decStyleMap = new HashMap<>();
        decStyleMap.put(0, createNumericCellStyle(workbook, 0));

        Map<String, CellStyle> dateStyleMap = new HashMap<>();
        
        CellStyle stringCellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("ＭＳ Ｐゴシック");
        stringCellStyle.setFont(font);

        Iterator<T> iterator = data.iterator();

        while (iterator.hasNext()) {
            SXSSFRow row = (SXSSFRow)sheet.createRow(rowIdx++);
            T rowData = iterator.next();
            int colIdx = 0;

            for (ColumnInformation column : columnInfo) {
                Field f = rowData.getClass().getDeclaredField(column.getId());
                f.setAccessible(true);
                Object value = f.get(rowData);

                if ("Integer".equals(column.getType()) || "Long".equals(column.getType()) || "BigDecimal".equals(column.getType())) {
                    // 数値項目
                    int decLength = getCellDecimalLength(column);

                    CellStyle decCellStyle = null;
                    if (!decStyleMap.containsKey(decLength)) {
                        decCellStyle = createNumericCellStyle(workbook, decLength);
                        decStyleMap.put(decLength, decCellStyle);
                    } else {
                        decCellStyle = decStyleMap.get(decLength);
                    }

                    Double decValue = null;
                    if ("BigDecimal".equals(column.getType())) {
                        decValue = f.get(rowData) != null ? ((BigDecimal)f.get(rowData)).doubleValue() : null;
                    } else if ("Long".equals(column.getType())) {
                        decValue = f.get(rowData) != null ? ((Long)f.get(rowData)).doubleValue(): null;
                    } else if ("Integer".equals(column.getType())) {
                        decValue = f.get(rowData) != null ? ((Integer)f.get(rowData)).doubleValue(): null;
                    }

                    SXSSFCell cell = (SXSSFCell)row.createCell(colIdx++);
                    cell.setCellStyle(decCellStyle);
                    if (null != decValue) {
                        cell.setCellValue(decValue);
                    } else {
                        cell.setCellValue("");
                    }

                } else if ("timestamp".equals(column.getType()) && !StringUtil.isEmpty(column.getFormat())) {
                    // 日付 ※フォーマットが指定されている場合のみ
                    CellStyle dateCellStyle = null;
                    if (!dateStyleMap.containsKey(column.getFormat())) {
                        dateCellStyle = createDateCellStyle(workbook, column.getFormat());
                        dateStyleMap.put(column.getFormat(), dateCellStyle);
                    } else {
                        dateCellStyle = dateStyleMap.get(column.getFormat());
                    }

                    Date dateValue = null;
                    if (f.get(rowData) != null) {
                        dateValue = (Date)f.get(rowData);
                    }

                    SXSSFCell cell = (SXSSFCell)row.createCell(colIdx++);
                    cell.setCellStyle(dateCellStyle);
                    if (dateValue != null) {
                        cell.setCellValue(dateValue);
                    } else {
                        cell.setCellValue("");
                    }
                } else {
                    // その他：文字列
                    String strValue = StringUtil.nullToBlank(f.get(rowData));
                    SXSSFCell cell = (SXSSFCell)row.createCell(colIdx++);
                    cell.setCellStyle(stringCellStyle);
                    if (!StringUtil.isEmpty(strValue)) {
                        cell.setCellValue(strValue);
                    } else {
                        cell.setCellValue("");
                    }
                }
            }
        }
        
//        // 列幅自動調整
//        int colIdx = 0;
//        for (ColumnInformation column : columnInfo) {
//            sheet.autoSizeColumn(colIdx++);
//        }
        
        // ランダムなファイル名を生成
        String tempFileName = RandomStringUtils.randomAlphanumeric(15);
        
        File outputFile = new File(tempDir, tempFileName);
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            workbook.write(fos);
            fos.flush();
        }

        return outputFile;
    }
    
    /**
     * Excelファイル生成(複数シートを書き込む場合)
     * ※sheetNamesとdatasはサイズが同じであることが前提
     * @param <T> 一覧データのジェネリクス
     * @param datas 一覧データ
     * @param columnInfo Excel出力定義情報
     * @param tempDir 一時出力フォルダ
     * @param sheetNames シート名
     * @return 出力ファイル
     * @throws Throwable 
     */
    public static <T> File outputExcelMultipleSheet(List<ColumnInformation> columnInfo, String tempDir, String[] sheetNames, List<List<T>> datas) throws Throwable {
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        
        // シート書き込み処理
        for(int i = 0; i < sheetNames.length;i++){
            writeSheet(columnInfo, workbook, sheetNames[i], i, datas.get(i));
        }
                
        // ランダムなファイル名を生成
        String tempFileName = RandomStringUtils.randomAlphanumeric(15);
        
        File outputFile = new File(tempDir, tempFileName);
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            workbook.write(fos);
            fos.flush();
        }

        return outputFile;
    }
    
    /**
     * シート書き込み処理
     * @param <T> 一覧データのジェネリクス
     * @param columnInfo Excel出力定義情報
     * @param workbook workbook
     * @param sheetName シート名
     * @param sheetIdx シートインデックス
     * @param data data 一覧データ
     * @throws Throwable 
     */
    private static <T> void  writeSheet(List<ColumnInformation> columnInfo, SXSSFWorkbook workbook, String sheetName, int sheetIdx, List<T> data)  throws Throwable{
        
        SXSSFSheet sheet = (SXSSFSheet) workbook.createSheet();
        if(!StringUtil.isEmpty(sheetName)){
            workbook.setSheetName(sheetIdx, sheetName);
        }
        
        int rowIdx = 0;

        // ヘッダ行出力
        CellStyle headerCellStyle = createHeaderCellStyle(workbook);

        int headerCol = 0;
        SXSSFRow headerRow = (SXSSFRow)sheet.createRow(rowIdx++);
        for (ColumnInformation column : columnInfo) {
            SXSSFCell cell = (SXSSFCell) headerRow.createCell(headerCol++);
            cell.setCellStyle(headerCellStyle);
            cell.setCellValue(column.getColumnname());
        }            

        // データ行出力
        Map<Integer, CellStyle> decStyleMap = new HashMap<>();
        decStyleMap.put(0, createNumericCellStyle(workbook, 0));

        Map<String, CellStyle> dateStyleMap = new HashMap<>();
        
        CellStyle stringCellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("ＭＳ Ｐゴシック");
        stringCellStyle.setFont(font);

        Iterator<T> iterator = data.iterator();

        while (iterator.hasNext()) {
            SXSSFRow row = (SXSSFRow)sheet.createRow(rowIdx++);
            T rowData = iterator.next();
            int colIdx = 0;

            for (ColumnInformation column : columnInfo) {
                Field f = rowData.getClass().getDeclaredField(column.getId());
                f.setAccessible(true);
                
                if ("Integer".equals(column.getType()) || "Long".equals(column.getType()) || "BigDecimal".equals(column.getType())) {
                    // 数値項目
                    int decLength = getCellDecimalLength(column);

                    CellStyle decCellStyle;
                    if (!decStyleMap.containsKey(decLength)) {
                        decCellStyle = createNumericCellStyle(workbook, decLength);
                        decStyleMap.put(decLength, decCellStyle);
                    } else {
                        decCellStyle = decStyleMap.get(decLength);
                    }

                    Double decValue = null;
                    if ("BigDecimal".equals(column.getType())) {
                        decValue = f.get(rowData) != null ? ((BigDecimal)f.get(rowData)).doubleValue() : null;
                    } else if ("Long".equals(column.getType())) {
                        decValue = f.get(rowData) != null ? ((Long)f.get(rowData)).doubleValue(): null;
                    } else if ("Integer".equals(column.getType())) {
                        decValue = f.get(rowData) != null ? ((Integer)f.get(rowData)).doubleValue(): null;
                    }

                    SXSSFCell cell = (SXSSFCell)row.createCell(colIdx++);
                    cell.setCellStyle(decCellStyle);
                    if (null != decValue) {
                        cell.setCellValue(decValue);
                    } else {
                        cell.setCellValue("");
                    }

                } else if ("timestamp".equals(column.getType()) && !StringUtil.isEmpty(column.getFormat())) {
                    // 日付 ※フォーマットが指定されている場合のみ
                    CellStyle dateCellStyle = null;
                    if (!dateStyleMap.containsKey(column.getFormat())) {
                        dateCellStyle = createDateCellStyle(workbook, column.getFormat());
                        dateStyleMap.put(column.getFormat(), dateCellStyle);
                    } else {
                        dateCellStyle = dateStyleMap.get(column.getFormat());
                    }

                    Date dateValue = null;
                    if (f.get(rowData) != null) {
                        dateValue = (Date)f.get(rowData);
                    }

                    SXSSFCell cell = (SXSSFCell)row.createCell(colIdx++);
                    cell.setCellStyle(dateCellStyle);
                    if (dateValue != null) {
                        cell.setCellValue(dateValue);
                    } else {
                        cell.setCellValue("");
                    }
                } else {
                    // その他：文字列
                    String strValue = StringUtil.nullToBlank(f.get(rowData));
                    SXSSFCell cell = (SXSSFCell)row.createCell(colIdx++);
                    cell.setCellStyle(stringCellStyle);
                    if (!StringUtil.isEmpty(strValue)) {
                        cell.setCellValue(strValue);
                    } else {
                        cell.setCellValue("");
                    }
                }
            }
        }
    }
    
    
    
    /**
     * 数値フォーマット小数桁取得
     * 
     * @param column 項目情報
     * @return 小数桁
     */
    protected static int getCellDecimalLength(ColumnInformation column) {
        switch (column.getType()) {
            case "Integer":
            case "Long":
                return 0;
            case "BigDecimal":
                return Integer.parseInt(column.getFormat().split(",")[1]);
            default:
                return -1;
        }
    }
    
    /**
     * ヘッダセルスタイル取得
     * 
     * @param workbook ワークブック
     * @return ヘッダセルスタイル
     */
    protected static CellStyle createHeaderCellStyle(SXSSFWorkbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font font = workbook.createFont();
        font.setFontName("ＭＳ Ｐゴシック");
        cellStyle.setFont(font);
        return cellStyle;
    }
    
    /**
     * 数値セルスタイル取得
     *
     * @param workbook ワークブック
     * @param decimalLength 小数部桁数
     * @return 数値セルスタイル
     */
    protected static CellStyle createNumericCellStyle(SXSSFWorkbook workbook, int decimalLength) {

        CellStyle cellStyle = workbook.createCellStyle();
        DataFormat fmt = workbook.createDataFormat();

        StringBuilder fmtStr = new StringBuilder("#,##0");

        // 小数部のフォーマット設定
        if (decimalLength > 0) {
            fmtStr.append(".");
            for (int i = 0; i < decimalLength; i++) {
                    fmtStr.append("0");
            }
        }
        cellStyle.setDataFormat(fmt.getFormat(fmtStr.toString()));
        Font font = workbook.createFont();
        font.setFontName("ＭＳ Ｐゴシック");
        cellStyle.setFont(font);
        return cellStyle;
    }

    /**
     * 日付セルスタイル取得
     *
     * @param workbook セル
     * @param format 日付フォーマット
     * @return 日付セルスタイル
     */
    protected static CellStyle createDateCellStyle(SXSSFWorkbook workbook, String format) {

        CreationHelper createHelper = workbook.getCreationHelper();
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat(format));
        Font font = workbook.createFont();
        font.setFontName("ＭＳ Ｐゴシック");
        cellStyle.setFont(font);
        return cellStyle;
    }
}
