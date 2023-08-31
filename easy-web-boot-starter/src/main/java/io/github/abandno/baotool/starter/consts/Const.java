package io.github.abandno.baotool.starter.consts;

import java.util.regex.Pattern;

/**
 * <p>
 *
 * @author L&J
 * @date 2021/10/10 11:06 下午
 */
public interface Const {

    String EASY_WEB = "easy-web";

    /*正则*/
    Pattern COMMA_SPLIT_REG = Pattern.compile("\\s*[,]+\\s*");
    Pattern COLON_SPLIT_REG = Pattern.compile("\\s*[:]+\\s*");

}
