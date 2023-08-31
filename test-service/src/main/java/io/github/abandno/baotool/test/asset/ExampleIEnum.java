package io.github.abandno.baotool.test.asset;

import io.github.abandno.baotool.core.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 *
 * @author L&J
 * @date 2021/10/1 6:34 下午
 */
@Getter
@AllArgsConstructor
public enum ExampleIEnum implements IEnum<Integer> {

    INIT(1, "初始"),
    ORDERED(2, "已下单"),
    NON_PAY(3, "未支付"),
    PAYED(4, "已支付"),
    ;

    private final Integer value;
    private final String label;

}
