package com.nisus.baotool.util.etc;


import cn.hutool.core.util.RandomUtil;
import com.nisus.baotool.test.asset.User;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class ScrollScannerTest {

    @Test
    public void demo() {
        //构造
        //行记录是 User 类型, 偏移量是 Long 类型
        ScrollScanner<User, Long> scrollScanner = ScrollScanner.<User, Long>builder()
                .logTag("业务日志标识") // 方便日志查看
                .initOffset(0L)
                .offsetGetter((user, preOffset) -> {
                    return user.getUserId();
                })
                // in: 偏移量, out: 单批扫描结果集
                .scanner((offset) -> {
                    return doScan(offset);
                })
                .onOnceFinish((batchInfo) -> {
                    Sout.println("once finished ---- ", batchInfo);
                })
                .onAllFinish((batchInfo) -> {
                    Sout.println("all finished ===== ", batchInfo);
                })
                .build();

        scrollScanner.scan((users) -> {
            for (User u : users) {
                Sout.println("处理扫描的结果: {}", u);
            }
        });

    }

    // SELECT * FROM t_user WHERE user_id > #{offset} ORDER BY user_id ASC LIMIT 10;
    //!一定要排序
    private List<User> doScan(Long offset) {
        // 我这里 mock 一下
        List<User> result = new ArrayList<>();
        if (offset > 100) {
            return result; // 扫不到结果了
        }
        for (Long i = offset; i < offset+10; i++) {
            User u = new User();
            u.setUserId(i);
            u.setName(RandomUtil.randomString(10));
            u.setAge(RandomUtil.randomInt(100));
            result.add(u);
        }
        return result;
    }


}
