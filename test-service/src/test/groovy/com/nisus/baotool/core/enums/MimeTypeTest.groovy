package com.nisus.baotool.core.enums

import io.github.abandno.baotool.core.enums.MimeType
import org.junit.jupiter.api.Test


class MimeTypeTest {

    @Test
    void foo() {
        def png = MimeType.PNG
        println "${png.getExplain()} : ${png.getMimeType()} , ${png.getExtension()}"

        // 根据扩展名获取枚举
        assert MimeType.getByExtension(".png").get() == MimeType.getByExtension("png").get()

        // 类型后缀 --> mime type
        println MimeType.getContentType(".xls").get()
    }

}
