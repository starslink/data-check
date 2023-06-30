package com.enjplatform.reconciliation;

import cn.hutool.cache.impl.FIFOCache;
import com.enjplatform.reconciliation.before.DefaultResourceReader;
import com.enjplatform.reconciliation.config.CheckConfig;
import com.enjplatform.reconciliation.domain.CheckEntry;
import com.enjplatform.reconciliation.dto.Order;
import com.enjplatform.reconciliation.entry.TestA;
import com.enjplatform.reconciliation.entry.TestB;
import com.enjplatform.reconciliation.enums.ExecutorStatusEnum;
import com.enjplatform.reconciliation.executor.CheckExecutor;
import com.enjplatform.reconciliation.executor.SimpleCache;
import com.enjplatform.reconciliation.loader.ExcelResourceLoader;
import com.enjplatform.reconciliation.loader.ExcelResourceLoader.ExcelResourceConfig;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author starslink
 */
public class ExecutorTest {

    private CheckConfig checkConfig;

    @Before
    public void before() {
        TestA a1 = new TestA("a1", "remark", BigDecimal.valueOf(1.03));
        TestA a2 = new TestA("a2", "remark", BigDecimal.valueOf(1.01));
        TestA a3 = new TestA("a3", "remark", BigDecimal.valueOf(1.01));

        TestB b1 = new TestB("a1", "remark", BigDecimal.valueOf(1.03));
        TestB b2 = new TestB("a2", "remark", BigDecimal.valueOf(1.03));
        TestB b3 = new TestB("b3", "remark", BigDecimal.valueOf(1.03));

//        ExcelResourceConfig oneExcelResourceConfig = new ExcelResourceConfig();
//        oneExcelResourceConfig.setClazz(Order.class);
//        oneExcelResourceConfig.setFilePathFormat("/Users/peter/Desktop/excel/a.xlsx");
//        oneExcelResourceConfig.setFileEntityParser(data -> data);
//
//        ExcelResourceLoader<Object> oneExcelResourceLoader = new ExcelResourceLoader<>(oneExcelResourceConfig);
//
//        ExcelResourceConfig twoExcelResourceConfig = new ExcelResourceConfig();
//        twoExcelResourceConfig.setClazz(Order.class);
//        twoExcelResourceConfig.setFilePathFormat("/Users/peter/Desktop/excel/b.xlsx");
//        twoExcelResourceConfig.setFileEntityParser(data -> data);
//
//        ExcelResourceLoader<Object> twoExcelResourceLoader = new ExcelResourceLoader<>(twoExcelResourceConfig);

        checkConfig = CheckConfig.builder()
                .srcLoader(date -> CheckEntry.wrap(Arrays.asList(a1, a2, a3)))
                .targetLoader(date -> CheckEntry.wrap(Arrays.asList(b1, b2, b3)))
                .allowableErrorRange(BigDecimal.valueOf(0.01))
//                .resourceReader(new DefaultResourceReader(oneExcelResourceLoader, twoExcelResourceLoader))
                .checkPre(context -> true)
//                .checkCacheEnum(CheckCacheEnum.MEMORY)
                .id("test")
                //.checkSync(null)
                .checkAfter(context -> System.out.println(context.getCheckResult().getDiffDetails()))
                .build();
    }


    @Test
    public void testExecutor() {
        CheckExecutor executor = CheckExecutor.buildExecutor(checkConfig);
        executor.process("20230530");
        Assert.assertNotNull(executor.getExecutorManager().getCurrentStatus());
        Assert.assertEquals(ExecutorStatusEnum.END.toString(), executor.getExecutorManager().getCurrentStatus());
    }

    @Test
    public void testSimpleCache() {
        FIFOCache<String, Object> instance = SimpleCache.getInstance();
        FIFOCache<String, Object> instance1 = SimpleCache.getInstance();
        Assert.assertEquals(instance, instance1);
    }

    @Test
    public void testReentrant() {
        checkConfig.setId("AAA");
        checkConfig.setCheckAfter(context -> {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        CheckExecutor aaa = CheckExecutor.buildExecutor(checkConfig);
        CheckExecutor aaaCopy = CheckExecutor.buildExecutor(checkConfig);
        //aaa.process("20210204");
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> aaa.process("20210204"));
        //线程睡眠模拟上一个任务开始但没执行完的情况
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //重复执行同一个id的executors,阻断
        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> aaaCopy.process("20210204"));

        future.join();
        future1.join();
    }
}
