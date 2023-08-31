package io.github.abandno.baotool.util.etc;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 滚动查询工具
 * <p>
 * 注意: 最后一次扫出空结果集的, 不算作批次, 不计入批次数内, 没有 batchInfo 封装.
 * @author L&J
 * @version 0.1
 * @since 2022/3/31 7:47 下午
 * @param <E> 扫描行记录类型
 * @param <I> 偏移量类型
 */
@Slf4j
@AllArgsConstructor
public class ScrollScanner<E, I> {
    /**
     * 初始偏移量. 必填.
     */
    private I initOffset;
    /**
     * E 记录
     * I 上一次偏移量信息
     * I 返回新的偏移量
     * 必填.
     */
    private BiFunction<E, I, I> offsetGetter;
    /**
     * 扫描取数逻辑. 必填.
     * 务必排序 + 偏移量, 避免无限制扫描.
     */
    private Function<I, List<E>> scanner;
    /**
     * 方便日志查看
     */
    private String logTag = "ScrollScanner";
    /**
     * 可选.
     * 一定会被执行
     * 异常不会中断扫描
     */
    private Consumer<BatchInfo<E, I>> onOnceFinish;
    /**
     * 所有批次扫描, 处理 后结束的回调. 入参是 最后一次的批次信息
     * 一定会被执行
     */
    private Consumer<BatchInfo<E, I>> onAllFinish;
    /**
     * 默认 true, 没有任何数据时, false 容易懵
     */
    @Getter
    @Builder.Default
    private BatchInfo<E, I> lastBatchInfo = BatchInfo.<E, I>builder().isOk(true).build();

    private ScrollScanner() {
        // private
    }

    /**
     * 快捷方式, 不会提前结束
     */
    public void scan(Consumer<List<E>> handler) {
        scan(list -> {
            handler.accept(list);
            return true;
        });
    }

    public void scan(Function<List<E>, Boolean> handler) {
        Assert.notNull(initOffset, "`initOffset` is null");
        Assert.notNull(offsetGetter, "`offsetGetter` is null");
        Assert.notNull(scanner, "`scanner` is null");

        long t = System.currentTimeMillis();
        int loopcnt = 0;
        I preOffset = null;
        I offset = initOffset;
        int cnt = 0;
        try {
            while (++loopcnt < 1_0000_0000) {
                log.info("[{}] 开始批次 {}", logTag, loopcnt);
                long t0 = System.currentTimeMillis();
                List<E> es = Safes.of(scanner.apply(offset));
                if (CollUtil.isEmpty(es)) {
                    // 最后一次空跑不计入批次数
                    loopcnt--;
                    break;
                }
                // 最后一次空跑不算做有效批次
                cnt += es.size();
                log.info("[{}] 结束批次 {}, size: {}, totalCount: {}, offset: {}, cost: {}ms", logTag, loopcnt, es.size(), cnt, offset, System.currentTimeMillis() - t0);

                boolean currSuccess = true;
                Boolean res = true;
                try {
                    res = handler.apply(es);
                } catch (Throwable e) {
                    currSuccess = false;
                    log.error("[{}] 当前批次异常, offset: {}, current size: {}, 已到批次: {}. {}", logTag, offset, es.size(), loopcnt, e.getMessage(), e);
                    throw e;
                } finally {
                    preOffset = offset;
                    offset = offsetGetter.apply(CollUtil.getLast(es), preOffset);

                    BatchInfo<E, I> batchInfo = BatchInfo.<E, I>builder()
                            .isOk(currSuccess)
                            .totalCount(cnt)
                            .currentCount(es.size())
                            .previousOffset(preOffset)
                            .nextOffset(offset)
                            .currentRecords(es)
                            .currentBatch(loopcnt)
                            .build();
                    lastBatchInfo = batchInfo;
                    if (onOnceFinish != null) {
                        try {
                            onOnceFinish.accept(batchInfo);
                        } catch (Throwable e) {
                            log.error("onOnceFinish failed: {}", batchInfo, e);
                        }
                    }
                }

                if (!res) {
                    // 返回 false, 提前结束
                    break;
                }
            }
        } finally {
            if (onAllFinish != null) {
                try {
                    onAllFinish.accept(lastBatchInfo);
                } catch (Exception e) {
                    log.error("onAllFinish failed: {}", lastBatchInfo, e);
                }
            }
        }

        log.info("[{}] 结束扫描, 总批次: {}, 扫描到总记录数: {}, cost: {}", logTag, loopcnt, cnt, DateUtil.formatBetween(System.currentTimeMillis() - t, BetweenFormatter.Level.MILLISECOND));
    }

    /**
     * {@link ScrollScanner}
     * @param <E> 扫描行记录类型
     * @param <I> 偏移量类型
     */
    public static <E, I> ScrollScannerBuilder<E, I> builder() {
        return new ScrollScannerBuilder<>();
    }

    public static class ScrollScannerBuilder<E, I> {
        /**
         * 初始偏移量. 必填.
         */
        private I initOffset;
        /**
         * E 记录
         * I 上一次偏移量信息
         * I 返回新的偏移量
         * 必填
         */
        private BiFunction<E, I, I> offsetGetter;
        /**
         * 扫描取数逻辑. 必填.
         * 务必排序 + 偏移量, 避免无限制扫描.
         */
        private Function<I, List<E>> scanner;
        /**
         * 方便日志查看
         */
        private String logTag;
        /**
         * 可选.
         *
         * !! 没有受 ScrollScanner#scan 事务的覆盖, 需要自行处理异常情况 !!
         * 一定会被执行
         */
        private Consumer<BatchInfo<E, I>> onOnceFinish;
        /**
         * 所有批次扫描, 处理 后结束的回调. 入参是 最后一次的批次信息
         * 一定会被执行
         */
        private Consumer<BatchInfo<E, I>> onAllFinish;

        ScrollScannerBuilder() {
        }

        public ScrollScannerBuilder<E, I> initOffset(final I initOffset) {
            this.initOffset = initOffset;
            return this;
        }

        public ScrollScannerBuilder<E, I> offsetGetter(final BiFunction<E, I, I> offsetGetter) {
            this.offsetGetter = offsetGetter;
            return this;
        }

        public ScrollScannerBuilder<E, I> scanner(final Function<I, List<E>> scanner) {
            this.scanner = scanner;
            return this;
        }

        public ScrollScannerBuilder<E, I> logTag(final String logTag) {
            this.logTag = logTag;
            return this;
        }

        public ScrollScannerBuilder<E, I> onOnceFinish(final Consumer<BatchInfo<E, I>> onOnceFinish) {
            this.onOnceFinish = onOnceFinish;
            return this;
        }

        public ScrollScannerBuilder<E, I> onAllFinish(final Consumer<BatchInfo<E, I>> onAllFinish) {
            this.onAllFinish = onAllFinish;
            return this;
        }

        public ScrollScanner<E, I> build() {
            ScrollScanner<E, I> inst = new ScrollScanner<>();
            inst.initOffset = this.initOffset;
            inst.offsetGetter = this.offsetGetter;
            inst.scanner = this.scanner;
            inst.logTag = this.logTag;
            inst.onOnceFinish = this.onOnceFinish;
            inst.onAllFinish = this.onAllFinish;

            return inst;
        }

        @Override
        public String toString() {
            return "ScrollScanner.ScrollScannerBuilder(initOffset=" + this.initOffset + ", offsetGetter=" + this.offsetGetter + ", scanner=" + this.scanner + ", logTag=" + this.logTag + ", onFinish=" + this.onOnceFinish + ")";
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BatchInfo<E, I> {
        /**
         * 当前批次是否成功(无异常==成功)
         */
        boolean isOk;
        int totalCount;
        int currentCount;
        I previousOffset;
        I nextOffset;
        List<E> currentRecords;
        int currentBatch;

        @Override
        public String toString() {
            return "BatchInfo{" +
                    "isOk=" + isOk +
                    ", totalCount=" + totalCount +
                    ", currentCount=" + currentCount +
                    ", previousOffset=" + previousOffset +
                    ", nextOffset=" + nextOffset +
                    ", currentBatch=" + currentBatch +
                    ", currentRecords.size=" + (currentRecords == null ? 0 : currentRecords.size()) +
                    '}';
        }
    }
}
