package io.github.abandno.baotool.core.ext.lang;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Flag 模板类
 *
 * 约定:
 * 使用 static final int 的常量字段定义 flag 名称. toString() 会友好输出
 * @author L_J
 * @date 2021/10/23 3:56
 */
public abstract class BitFlag {

    protected List<Field> flagNameFields = new ArrayList<>();
    protected int state;

    public int getState() {
        return this.state;
    }

    public BitFlag() {
        Field[] declaredFields = this.getClass().getDeclaredFields();
        for (Field f : declaredFields) {
            int modifiers = f.getModifiers();
            // static final int|Integer
            if (Modifier.isFinal(modifiers) && Modifier.isStatic(modifiers)
                    && (f.getType().isAssignableFrom(Integer.TYPE) || f.getType().isAssignableFrom(Integer.class))) {
                flagNameFields.add(f);
            }
        }
        this.state = 0;
    }

    public BitFlag(int state) {
        this();
        this.state = state;
    }

    public void add(int flag) {
        this.state |= flag;
    }

    public void remove(int flag) {
        this.state &= ~flag;
    }

    public boolean has(int flag) {
        return (this.state & flag) == flag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BitFlag that = (BitFlag) o;

        return state == that.state;
    }

    @Override
    public int hashCode() {
        return state;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Field f : this.flagNameFields) {
            f.setAccessible(true);
            int flag = 0;
            try {
                flag = (int) f.get(this);
            } catch (Exception e) {
            }
            if (this.has(flag)) {
                sb.append(f.getName()).append("|");
            }
        }
        if (sb.length() > 0) {
            return sb.substring(0, sb.length() - 1);
        }
        return "";
    }
}
