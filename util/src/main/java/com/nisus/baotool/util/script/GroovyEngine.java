package com.nisus.baotool.util.script;

import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * <p>
 *
 * @author L&J
 * @date 2021/10/12 3:46 下午
 */
public class GroovyEngine {
    protected static final ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
    protected static final String GROOVY = "groovy";

    public static Object eval(String script) {
        return eval(script, null);
    }

    /**
     * 直接编译 script
     */
    public static Object eval(String script, ScriptContext context) {
        ScriptEngine engine = scriptEngineManager.getEngineByName(GROOVY);
        Object res = null;
        try {
            if (context != null) {
                res = engine.eval(script, context);
            } else {
                res = engine.eval(script);
            }
        } catch (ScriptException e) {
            throw new RuntimeException("eval script 异常", e);
        }
        return res;
    }

    public static Invocable getEngine(String script) {
        return getEngine(script, null);
    }

    /**
     * 编译 script 获取 engine
     */
    public static Invocable getEngine(String script, ScriptContext context) {
        ScriptEngine engine = scriptEngineManager.getEngineByName(GROOVY);
        try {
            if (context != null) {
                engine.eval(script, context);
            } else {
                engine.eval(script);
            }
            return (Invocable) engine;
        } catch (ScriptException e) {
            throw new RuntimeException("eval script 异常", e);
        }
    }
}
