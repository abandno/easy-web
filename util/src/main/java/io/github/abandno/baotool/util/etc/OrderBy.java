package io.github.abandno.baotool.util.etc;

import cn.hutool.core.lang.Tuple;
import cn.hutool.core.util.StrUtil;
import io.github.abandno.baotool.core.enums.CaseTransform;
import io.github.abandno.baotool.util.collection.skmap.SsMap;
import io.github.abandno.baotool.util.function.SFunction;
import io.github.abandno.baotool.util.reflect.FieldNameUtil;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 *
 * 注: 字段别名永远指的数据库表字段的别名. 也就是说, 先转换命名风格为最终字段名, 然后换成别名.
 * @author L&J
 * @date 2021/10/18 2:25 下午
 */
public class OrderBy {
    public static final String ASC = "ASC";
    public static final String DESC = "DESC";


    /**
     * list of (column, direction)
     */
    private final Set<Tuple> orderBys = new LinkedHashSet<>(); // Tuple 重写过 hashCode+equals
    private final Map<String, String> columnAliasMapping = new HashMap<>();
    /**
     * 决定字段风格转换规则, 默认 Low camel -> Low underscore.
     * 注: 在最终生成 SQL 片段时转换类名.
     */
    private CaseTransform caseTransform = CaseTransform.LC2LU;

    public static OrderBy of() {
        return of(CaseTransform.LC2LU);
    }

    /**
     *
     * @param columnAlias 注意: 字段别名永远指的数据库表字段的别名. 也就是说, 先转换命名风格为最终字段名, 然后换成别名.
     */
    public static OrderBy of(String... columnAlias) {
        return of(CaseTransform.LC2LU, columnAlias);
    }

    /**
     * @param caseTransform 命名风格转换规则
     * @param columnAlias 注意: 字段别名永远指的数据库表字段的别名. 也就是说, 先转换命名风格为最终字段名, 然后换成别名.
     */
    public static OrderBy of(CaseTransform caseTransform, String... columnAlias) {
        OrderBy orderBy = new OrderBy();
        if (columnAlias != null) {
            orderBy.configColumnAlias(columnAlias);
        }
        if (caseTransform != null) {
            orderBy.configCaseTransform(caseTransform);
        }
        return orderBy;
    }

    private OrderBy() {
        // private
    }

    /**
     * 配置列别名
     * <p>
     * 注意: 字段别名永远指的数据库表字段的别名. 也就是说, 先转换命名风格为最终字段名, 然后换成别名.
     *
     * @param columnAlias 原名, 别名, 原名, 别名, ...
     */
    public OrderBy configColumnAlias(String... columnAlias) {
        return this.configColumnAlias(SsMap.of(columnAlias));
    }

    /**
     * 配置列别名
     *
     * @param columnAlias 原名, 别名, 原名, 别名, ...
     */
    public OrderBy configColumnAlias(Map<String, String> columnAlias) {
        this.columnAliasMapping.putAll(columnAlias);
        return this;
    }

    /**
     * 配置字段风格转换规则
     * @param caseTransform 风格转换规则
     */
    public OrderBy configCaseTransform(CaseTransform caseTransform) {
        this.caseTransform = caseTransform;
        return this;
    }

    public <T> OrderBy orderBy(SFunction<T, ?> column, boolean isAsc) {
        return orderBy(column, isAsc ? ASC : DESC);
    }

    /**
     * 字段 lambda 表达式, 默认 驼峰->下划线 风格
     *
     * @param column    字段 getter
     * @param direction 方向
     * @param <T>       实体类
     */
    public <T> OrderBy orderBy(SFunction<T, ?> column, String direction) {
        return orderBy(FieldNameUtil.get(column, caseTransform), direction);
    }

    public OrderBy orderBy(String column, boolean isAsc) {
        return this.orderBy(column, isAsc ? ASC : DESC);
    }

    public OrderBy orderBy(String column, String direction) {
        // 无列名, 略过
        if (StrUtil.isBlank(column)) {
            return this;
        }
        // 无排序方向, 默认 ASC
        if (StrUtil.isBlank(direction)) {
            direction = ASC;
        } else {
            // 排序方向关键词要合法, 非法忽略
            if (!ASC.equalsIgnoreCase(direction) && !DESC.equalsIgnoreCase(direction)) {
                return this;
            }
        }

        // // 使用来动态排序的列名是安全的，可以放心拼接到SQL中
        // if (SecSdk.isValidSqlIdentifier(column) && SecSdk.isValidSqlIdentifier(direction)) {
        //     orderBys.add(new Tuple(column, direction));
        // }
        // 暂无字段名安全校验
        orderBys.add(new Tuple(column, direction));
        return this;
    }

    private String caseFormatting(String origin) {
        return this.caseTransform.FROM.to(this.caseTransform.TO, origin);
    }

    /**
     * 终端操作: 获取 sql 片段
     * 不含 order by 关键字
     */
    public String orderSegment() {
        if (orderBys.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (Tuple tuple : orderBys) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            String column = tuple.get(0);
            String direction = tuple.get(1);
            // 先转换命名风格
            column = caseFormatting(column);
            // 再别名转换
            column = this.columnAliasMapping.getOrDefault(column, column);
            sb.append(column).append(" ").append(direction);
        }

        return sb.toString();
    }

    /**
     * 终端操作: 获取 sql 片段
     * 含 order by 关键字
     */
    public String fullOrderSegment() {
        String bys = this.orderSegment();
        return StrUtil.isEmpty(bys) ? "" : "ORDER BY " + this.orderSegment();
    }

    /**
     * 清除已有 order by s
     * <p>
     * 可在复用 OrderBy 实例前使用
     */
    public OrderBy clearOrderBys() {
        this.orderBys.clear();
        return this;
    }

    @Override
    public String toString() {
        return orderSegment();
    }
}
