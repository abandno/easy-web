package com.nisus.baotool.webutil.aliexcel.convert;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.nisus.baotool.webutil.jackson.ann.NumberProperty;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * 数字格式化
 * <p>
 * NumberUtil.decimalFormat 或 round
 * <pre>
 * \@ExcelProperty(value = "秒杀坑位爆发系数", converter = NumberExcelPropertyConverter.class)
 * \@NumberProperty(pattern = "#.##") // 搭配使用才有效果
 * </pre>
 *
 * supportJavaTypeKey
 * supportExcelTypeKey
 * convertToJavaData
 * 上面接口层 default 抛异常的, 应该是用不着, debug 发现没有走.
 * @see NumberProperty
 * @author L&J
 * @date 2022-12-17 22:06:41
 */
public class NumberExcelPropertyConverter implements Converter<Object> {

    public Class<?> supportJavaTypeKey() {
        return Object.class;
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

    public WriteCellData<?> convertToExcelData(Object value, ExcelContentProperty contentProperty,
                                                GlobalConfiguration globalConfiguration) throws Exception {
        if (value == null) {
            return new WriteCellData<String>("");
        }

        NumberProperty annotation = AnnotationUtils.getAnnotation(contentProperty.getField(), NumberProperty.class);
        if (annotation == null) {
            // 不知道格式, 暂不支持定制格式化
            return new WriteCellData<String>(String.valueOf(value));
        }

        String pattern = annotation.pattern();
        if (StrUtil.isNotBlank(pattern)) {
            String fmt = NumberUtil.decimalFormat(pattern, value, annotation.roundingMode());
            return new WriteCellData<String>(fmt);
        }

        Number number = null;
        try {
            number = NumberUtil.parseNumber(String.valueOf(value));
        } catch (NumberFormatException e) {
        }

        if (number == null) {
            // 非有效数字, 不知道怎么格式化
            return new WriteCellData<String>(String.valueOf(value));
        }

        if (annotation.scale() >= 0) {
            if (number instanceof Double || number instanceof Float) { // 浮点型, 才应用 round 规则
                number = NumberUtil.round((Double) number, annotation.scale(), annotation.roundingMode());
            }
        }

        return new WriteCellData<String>(number + "");
    }

}
