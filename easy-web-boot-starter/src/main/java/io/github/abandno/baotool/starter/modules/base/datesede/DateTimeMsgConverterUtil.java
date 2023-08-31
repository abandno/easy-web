package io.github.abandno.baotool.starter.modules.base.datesede;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;


/**http message converter 日期时间类型转换工具类,
 * url参数和body参数转换都会用得着.
 * - Date
 * - LocalDateTime
 * - LocalDate
 * - LocalTime
 * @author dafei
 * @version 0.1
 * @date 2020/9/28 23:44
 */
public class DateTimeMsgConverterUtil {
    /**
     * 纯数字  时间戳, 秒|毫秒
     */
    private static final String TIME_STAMP_RE = "\\d{10,20}";


    public static Date toDate(String source) {
        if (StringUtils.isEmpty(source)) {
            return null;
        }
        if (source.matches(TIME_STAMP_RE)) {
            return new Date(Long.parseLong(source));
        }

        return DateUtil.parse(source);
    }

    public static LocalDateTime toLocalDateTime(String source) {
        if (StringUtils.isEmpty(source)) {
            return null;
        }
        if (source.matches(TIME_STAMP_RE)) {
            return LocalDateTimeUtil.of(Long.parseLong(source));
        }
        // return LocalDateTime.parse(source, DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN));
        DateTime date = DateUtil.parse(source);
        return LocalDateTimeUtil.of(date);
    }

    public static LocalDate toLocalDate(String source) {
        if (StringUtils.isEmpty(source)) {
            return null;
        }
        if (source.matches(TIME_STAMP_RE)) {
            return LocalDateTimeUtil.of(Long.parseLong(source)).toLocalDate();
        }
        // return LocalDate.parse(source, DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN));
        DateTime date = DateUtil.parse(source);
        return LocalDateTimeUtil.of(date).toLocalDate();
    }

    public static LocalTime toLocalTime(String source) {
        if (StringUtils.isEmpty(source)) {
            return null;
        }
        if (source.matches(TIME_STAMP_RE)) {
            return LocalDateTimeUtil.of(Long.parseLong(source)).toLocalTime();
        }
        // return LocalTime.parse(source, DateTimeFormatter.ofPattern(DatePattern.NORM_TIME_PATTERN));
        DateTime date = DateUtil.parse(source);
        return LocalDateTimeUtil.of(date).toLocalTime();
    }



}
