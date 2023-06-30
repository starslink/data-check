package com.enjplatform.reconciliation;

import com.enjplatform.reconciliation.loader.ExcelResourceLoader;
import com.enjplatform.reconciliation.loader.ExcelResourceLoader.ExcelResourceConfig;
import com.enjplatform.reconciliation.domain.CheckAdapter;
import com.enjplatform.reconciliation.domain.CheckEntry;
import com.enjplatform.reconciliation.dto.Order;
import com.enjplatform.reconciliation.entry.TestA;
import com.enjplatform.reconciliation.entry.TestB;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author starslink
 */
public class CheckEntryTest {

    @Test
    public void testAnnotation() {
//        TestA a = new TestA("a", "etst", 13d);
//        TestB b = new TestB("b", "tee", 13d);
//        CheckEntry entryA = CheckEntry.wrap(a, "name", Arrays.asList("amount"));
//        CheckEntry entryB = CheckEntry.wrap(b);
//        Assert.assertNotNull(entryA.getCheckData());
//        Assert.assertNotNull(entryB.getCheckData());
    }

    @Test
    public void testAdapter() {
//        TestA a = new TestA("a", "etst", 13d);
//        TestB b = new TestB("b", "tee", 13d);

//        List<CheckAdapter> list = Arrays.asList(a, b);
//        list.forEach(e -> {
//            System.out.println(e.getKey());
//            System.out.println(e.getCheckData());
//        });
//
//        CheckEntry entryA = new CheckEntry(a);
//        CheckEntry entryB = new CheckEntry(b);
////        Assert.assertNotNull(entryA.getCheckData());
//        Assert.assertEquals(entryA.getCheckData(), entryB.getCheckData());
    }

    @Test
    public void testOrder() {
        ExcelResourceConfig excelResourceConfig = new ExcelResourceConfig();
        excelResourceConfig.setClazz(Order.class);
        excelResourceConfig.setFilePathFormat("/Users/peter/Desktop/excel/a.xlsx");
        excelResourceConfig.setFileEntityParser(data -> data);
        ExcelResourceLoader excelResourceLoader = new ExcelResourceLoader(excelResourceConfig);
        List load = excelResourceLoader.load("");
        Assert.assertNotNull(load);
    }

}