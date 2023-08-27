package com.nisus.baotool.test.asset;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author nisus
 * @version 0.1
 * @since 2023/4/20 17:45
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long userId;
    @NotBlank
    private String name;
    private int age;

    private boolean isGoodMan;

}
