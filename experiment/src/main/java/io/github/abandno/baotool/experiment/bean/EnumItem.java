package io.github.abandno.baotool.experiment.bean;

import io.github.abandno.baotool.core.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 枚举项 VO
 * <p>
 * 简单的枚举项 vo, 只有 value, label 属性, 可用于前后端交互.
 * @author L&J
 * @date 2021/10/2 11:56 上午
 */
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnumItem<K extends Serializable> implements IEnum<K> {

    private K value;

    private String label;

}
