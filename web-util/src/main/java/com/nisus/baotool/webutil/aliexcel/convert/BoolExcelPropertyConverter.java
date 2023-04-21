package com.nisus.baotool.webutil.aliexcel.convert;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

/**
 * Boolean -> 是|否
 * <p>
 * 例子:
 * <pre>
 * \@ExcelProperty(value = "是否美女", converter = BoolExcelPropertyConverter.class)
 * </pre>
 *
 * supportJavaTypeKey
 * supportExcelTypeKey
 * convertToJavaData
 * 上面接口层 default 抛异常的, 应该是用不着, debug 发现没有走.
 * @author L&J
 * @date 2021/12/29 2:59 下午
 */
public class BoolExcelPropertyConverter implements Converter<Boolean> {

    public Class<?> supportJavaTypeKey() {
        return Boolean.class;
    }

    /**
     * Back to object enum in excel
     *
     * @return Support for {@link CellDataTypeEnum}
     */
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    /**
     * Convert excel objects to Java objects
     *
     * @param cellData            Excel cell data.NotNull.
     * @param contentProperty     Content property.Nullable.
     * @param globalConfiguration Global configuration.NotNull.
     * @return Data to put into a Java object
     * @throws Exception Exception.
     */
    public Boolean convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty,
                                GlobalConfiguration globalConfiguration) throws Exception {
        return null;
    }

    public WriteCellData<?> convertToExcelData(Boolean value, ExcelContentProperty contentProperty,
                                                GlobalConfiguration globalConfiguration) throws Exception {
        return new WriteCellData<String>(value == null ? "" : (value ? "是" : "否"));
    }

}
