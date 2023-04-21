package com.nisus.baotool.experiment;

import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * 单测时, System.out.println 是不是有点累?
 * @author L&J
 * @version 0.1
 * @since 2022/3/29 2:43 下午
 */
public class Sout {

    private Sout() {}

    /**
     * System.out.println
     * @param out 支持字符串模板 {@link StrUtil#format}
     * @param args 模板参数
     */
    public static void println(String out, Object ... args) {
        if (out != null && out.contains("{}")) {
            System.out.println(StrUtil.format(out, args));
        } else if (args != null){
            Object[] copyArgs = new Object[args.length + 1];
            copyArgs[0] = out;
            System.arraycopy(args, 0, copyArgs, 1, args.length);
            println(copyArgs);
        } else {
            System.out.println(out);
        }
    }

    /**
     * System.out.println 多个参数, ' ' 拼接
     * @param args 参数
     */
    public static void println(Object ... args) {
        if (args == null) {
            System.out.println("null");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Object arg : args) {
            if (sb.length()>0) {
                sb.append(" ");
            }
            sb.append(StrUtil.utf8Str(arg));
        }
        System.out.println(sb.toString());
    }

}
