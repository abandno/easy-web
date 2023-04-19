package com.nisus.baotool.core.ext.lang;

/**
 * <p>
 *
 * @author L&J
 * @date 2021/10/26 6:29 下午
 */
public class ColorFlag extends AbstractFlag {

    public static final int RED         = 1;
    public static final int ORANGE      = 1 << 1;
    public static final int YELLOW      = 1 << 2;
    public static final int GREEN       = 1 << 4;
    public static final int BLUE      = 1 << 5;
    public static final int VIOLET      = 1 << 6;

    public ColorFlag(int state) {
        super(state);
    }

    // 提供快捷方式
    public boolean isGreen() {
        return this.has(GREEN);
    }
}
