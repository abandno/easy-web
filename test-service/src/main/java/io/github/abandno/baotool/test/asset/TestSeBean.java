package io.github.abandno.baotool.test.asset;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.abandno.baotool.webutil.jackson.ann.EnumProperty;
import io.github.abandno.baotool.webutil.jackson.ann.NumberProperty;
import io.github.abandno.baotool.webutil.jackson.de.EmptyToNullPropertyDeserializer;
import io.github.abandno.baotool.webutil.jackson.se.EmptyToNullPropertySerializer;
import lombok.Data;

/**
 * <p>
 *
 * @author L&J
 * @date 2022/1/6 5:11 下午
 */
@Data
public class TestSeBean {
    @EnumProperty(ExampleIEnum.class)
    // 或 @JsonSerialize(using = IEnumSerializer.class)
    private ExampleIEnum exampleIEnum;
    @EnumProperty(ExampleIEnum.class)
    // 或 @JsonSerialize(using = IEnumSerializer.class)
    protected Integer exampleIEnumCode;
    // @JsonDeserialize(using = EnumDeserializer.class)
    private Object e1;
    // @JsonDeserialize(using = EnumDeserializer.class)
    private ExampleIEnum e2;
    // @JsonDeserialize(using = EnumDeserializer.class)
    private ExampleEnum e3;
    @NumberProperty
    private Integer num1 = 123; // expect: 123
    @NumberProperty(scale = 3)
    private Double num2 = 998.78957439; // expect: 998.790
    @NumberProperty
    private String num3 = "456.78"; // expect: 原样输出
    @NumberProperty
    private Object num4 = 45636.34637; // expect: 原样输出
    @NumberProperty(pattern = "价格: $#.00元")
    private Object num5 = 1234.45678; // expect: 价格: $1234.46
    @NumberProperty(pattern = "价格: $#.00元")
    private Object num6 = null; // expect: 价格: $1234.46
    // @EmptyToNullProperty
    // private Object empty1 = 99; // expect: 99
    // @EmptyToNullProperty(otherEmptyCases = "[99]")
    // private Object empty2 = 99; // expect: null
    // @EmptyToNullProperty(otherEmptyCases = "[\"空\"]")
    // private Object empty3; // "空" expect: null
    @JsonDeserialize(using = EmptyToNullPropertyDeserializer.class)
    private Object empty4; // "" expect: null
    @JsonSerialize(using = EmptyToNullPropertySerializer.class)
    private Object empty5 = ""; // expect: null
    // @EmptyToNullProperty(se = false, otherEmptyCases = "[\"空\"]")
    // private Object empty6;
    // @EmptyToNullProperty(de = false, otherEmptyCases = "[\"空\"]")
    // private Object empty7;

    // @JsonDeserialize(using = EmptyToNullPropertyDeserializer.class)
    // public void setEmpty3(Object v) {
    //     this.empty3 = v;
    // }
}
