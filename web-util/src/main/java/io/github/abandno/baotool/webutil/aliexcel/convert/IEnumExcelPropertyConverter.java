package io.github.abandno.baotool.webutil.aliexcel.convert;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import io.github.abandno.baotool.core.enums.IEnum;
import io.github.abandno.baotool.util.lang.EnumUtil;
import io.github.abandno.baotool.webutil.jackson.ann.EnumProperty;
import org.springframework.core.annotation.AnnotationUtils;

import java.io.Serializable;
import java.util.Optional;

/**
 * IEnum Excel 转换器
 * <p>
 * - 枚举码值 -> 枚举文案
 * - 枚举实例 -> 枚举文案
 *
 * IEnum 取 getLabel(); Enum 取 name()
 * 
 * <pre>
 * \@ExcelProperty(value = "风险等级", converter = IEnumExcelPropertyConverter.class)
 * \@EnumProperty(RiskLevelEnum.class) // 搭配使用才有效果
 * </pre>
 * @see EnumProperty
 * @author L&J
 * @date 2022/1/6 6:15 下午
 */
public class IEnumExcelPropertyConverter implements Converter<Serializable> {
    public Class<?> supportJavaTypeKey() {
        return Serializable.class;
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

    public WriteCellData<?> convertToExcelData(Serializable value, ExcelContentProperty contentProperty,
                                               GlobalConfiguration globalConfiguration) throws Exception {
        if (value == null) {
            return new WriteCellData<String>("");
        }
        /*
         * 合法枚举 code 应该是 Number 或 String
         * 利用 @EnumProperty 获取枚举类信息
         */
        String res = "";
        if (value instanceof Number || value instanceof CharSequence) {
            EnumProperty enumPropAnn = AnnotationUtils.getAnnotation(contentProperty.getField(), EnumProperty.class);
            if (enumPropAnn != null) {
                Class enumType = enumPropAnn.value();
                if (IEnum.class.isAssignableFrom(enumType)) {
                    Optional opt = EnumUtil.fromValue(enumType, value);
                    if (opt.isPresent()) {
                        res = ((IEnum<?>) opt.get()).getLabel();
                    }
                } else { // Enum
                    Enum e = EnumUtil.fromStringQuietly(enumType, String.valueOf(value));
                    if (e != null) {
                        res = e.name();
                    }
                }
            } // else 不知道具体枚举类型, 无法转换

            return new WriteCellData<String>(res);
        }

        /*
         * 也可以是 Enum 类型 或 IEnum 类型
         */
        if (value instanceof IEnum) {
            res = (((IEnum<?>) value).getLabel());
            return new WriteCellData<String>(res);
        }
        if (value instanceof Enum) {
            res = ((Enum<?>) value).name();
            return new WriteCellData<String>(res);
        }

        // 剩下我不认咯

        return new WriteCellData<String>(res);
    }
}
