package com.nisus.baotool.core.enums

import io.github.abandno.baotool.core.enums.CaseTransform
import org.junit.jupiter.api.Test


class CaseTransformTest {

    @Test
    void foo() {
        println CaseTransform.LC2LU.transform("helloWorld")
        println CaseTransform.LU2LC.transform("hello_world")

        // ---
        // hello_world
        // helloWorld
    }

}
