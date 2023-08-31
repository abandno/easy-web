package io.github.abandno.baotool.core.enums;

import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 枚举类接口
 * <p>
 * 定义业务友好的枚举类格式.
 *
 * @param <K> value 类型
 * @author L&J
 * @date 2021/9/30 9:18 下午
 */
public interface IEnum<K extends Serializable> extends Serializable {

    /**
     * 等价于 ID , 请自己确保唯一性
     */
    K getValue();

    /**
     * 缺省返回 value
     */
    default String getLabel() {
        return String.valueOf(this.getValue());
    }

    /**
     * 根据 id 判断是否相等
     *
     * ! 字符和数值类型不敏感 !
     * @param other 待匹配的枚举实例或其 ID
     * @return 是否相等
     */
    default boolean eq(Serializable other) {
        if (Objects.equals(this, other)) { // other 是同类枚举实例
            return true;
        }

        // other 当作 value
        K value = this.getValue();
        // 类型不敏感
        return Objects.equals(value, other) || (value != null && other != null && Objects.equals(String.valueOf(value), String.valueOf(other))); // null != 'null'
    }

    /**
     * Not Equal
     * eq() 取反
     */
    default boolean ne(Serializable other) {
        return !eq(other);
    }

    /**
     * 格式: ExampleEnum.ENUM1(id, name [, disabled=true, hidden=true, extra=...])
     */
    default String prettyString() {
        if (this instanceof Enum<?>) {
            String simpleName = this.getClass().getSimpleName();
            // 补偿: 枚举实例有方法时, getSimpleName 返回 ""
            if (simpleName.isEmpty()) {
                String fullName = this.getClass().getName();
                int lastDotIx = fullName.lastIndexOf(".");
                // 剔除包名
                simpleName = fullName.substring(lastDotIx + 1);

                // 剔除 $123 , 如有
                int ix = simpleName.lastIndexOf("$");
                String seq = simpleName.substring(ix + 1);
                if (Pattern.compile("\\d+").matcher(seq).matches()) {
                    simpleName = simpleName.substring(0, ix);
                }
            }

            String enumName = ((Enum<?>) this).name();
            StringBuilder sb = new StringBuilder();
            return sb.append(simpleName).append(".").append(enumName)
                    .append("(").append(this.getValue()).append(",").append(this.getLabel())
                    .append(")").toString();
        } else {
            return this.toString();
        }
    }

    /**
     * @return '{value},{label}'
     */
    default String simpleString() {
        return this.getValue() + "," + this.getLabel();
    }

}
