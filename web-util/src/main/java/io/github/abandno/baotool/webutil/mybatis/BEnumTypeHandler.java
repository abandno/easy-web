package io.github.abandno.baotool.webutil.mybatis;

import io.github.abandno.baotool.core.enums.IEnum;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaClass;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 自定义枚举属性转换器
 *
 * 在 {@link com.baomidou.mybatisplus.core.handlers.MybatisEnumTypeHandler} 基础上改造,
 * 支持 Enum, IEnum
 * ~~支持 MP 的 IEum, dianping 的 IEnum~~
 * ~~支持 @EnumValue 注解~~
 * @author L&J
 * @since 2021-10-11 22:21:45
 */
public class BEnumTypeHandler<E extends Enum<E>> extends BaseTypeHandler<E> {

    private static final Map<String, String> TABLE_METHOD_OF_ENUM_TYPES = new ConcurrentHashMap<>();
    private static final ReflectorFactory REFLECTOR_FACTORY = new DefaultReflectorFactory();
    private final Class<E> enumClassType;
    // private final Class<?> propertyType;
    // private final Invoker getInvoker;

    // 将类注册类型处理器时, mybatis 会看有没有 javaType, 有, 则取一个参数 javaType 构造的方法, 实例化
    public BEnumTypeHandler(Class<E> enumClassType) {
        if (enumClassType == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.enumClassType = enumClassType;
        MetaClass metaClass = MetaClass.forClass(enumClassType, REFLECTOR_FACTORY);
        String name = "value";
        // if (!IEnum.class.isAssignableFrom(enumClassType)) {
        //     name = findEnumValueFieldName(this.enumClassType).orElseThrow(() -> new IllegalArgumentException(String.format("Could not find @EnumValue in Class: %s.", this.enumClassType.getName())));
        // }
        // this.propertyType = ReflectionKit.resolvePrimitiveIfNecessary(metaClass.getGetterType(name));
        // this.getInvoker = metaClass.getGetInvoker(name);
    }

    // /**
    //  * 查找标记标记EnumValue字段
    //  *
    //  * @param clazz class
    //  * @return EnumValue字段
    //  * @since 3.3.1
    //  */
    // public static Optional<String> findEnumValueFieldName(Class<?> clazz) {
    //     if (clazz != null && clazz.isEnum()) {
    //         String className = clazz.getName();
    //         return Optional.ofNullable(CollectionUtils.computeIfAbsent(TABLE_METHOD_OF_ENUM_TYPES, className, key -> {
    //             Optional<Field> fieldOptional = findEnumValueAnnotationField(clazz);
    //             return fieldOptional.map(Field::getName).orElse(null);
    //         }));
    //     }
    //     return Optional.empty();
    // }
    //
    // private static Optional<Field> findEnumValueAnnotationField(Class<?> clazz) {
    //     return Arrays.stream(clazz.getDeclaredFields()).filter(field -> field.isAnnotationPresent(EnumValue.class)).findFirst();
    // }

    /**
     * 校验是否支持
     *
     * @param clazz class
     * @return 是否为MP枚举处理
     * @since 3.3.1
     */
    public static boolean isSupport(Class<?> clazz) {
        // return clazz != null && clazz.isEnum() && (IEnum.class.isAssignableFrom(clazz) || findEnumValueFieldName(clazz).isPresent());
        return clazz != null && clazz.isEnum();
    }

    private boolean isIEnum(Class<E> clazz) {
        return IEnum.class.isAssignableFrom(clazz);
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType)
        throws SQLException {
        if (jdbcType == null) {
            ps.setObject(i, this.getValue(parameter));
        } else {
            // see r3589
            ps.setObject(i, this.getValue(parameter), jdbcType.TYPE_CODE);
        }
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Object value = rs.getObject(columnName);
        if (null == value && rs.wasNull()) {
            return null;
        }
        return this.valueOf(value);
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Object value = rs.getObject(columnIndex);
        if (null == value && rs.wasNull()) {
            return null;
        }
        return this.valueOf(value);
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Object value = cs.getObject(columnIndex);
        if (null == value && cs.wasNull()) {
            return null;
        }
        return this.valueOf(value);
    }

    private E valueOf(Object value) {
        E[] es = this.enumClassType.getEnumConstants();
        return Arrays.stream(es).filter((e) -> equalsValue(value, getValue(e))).findAny().orElse(null);
    }

    /**
     * 值比较
     *
     * 字符串和数值类型不敏感
     * @param sourceValue 数据库字段值
     * @param targetValue 当前枚举属性值
     * @return 是否匹配
     * @since 3.3.0
     */
    protected boolean equalsValue(Object sourceValue, Object targetValue) {
        return Objects.equals(sourceValue, targetValue) || (sourceValue != null && targetValue != null && sourceValue.toString().equals(targetValue.toString()));
        // String sValue = String.valueOf(sourceValue).trim();
        // String tValue = String.valueOf(targetValue).trim();
        // if (sourceValue instanceof Number && targetValue instanceof Number
        //     && new BigDecimal(sValue).compareTo(new BigDecimal(tValue)) == 0) {
        //     return true;
        // }
        // return Objects.equals(sValue, tValue);
    }

    private Object getValue(E parameter) {
        if (parameter == null) {
            return null;
        }

        if (parameter instanceof IEnum) {
            return ((IEnum<?>) parameter).getValue();
        } else {
            return parameter.name();
        }
    }
}
