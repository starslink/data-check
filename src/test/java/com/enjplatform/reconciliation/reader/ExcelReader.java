package com.enjplatform.reconciliation.reader;

import com.enjplatform.reconciliation.before.AbstractResourceReader;
import com.enjplatform.reconciliation.before.ResourceLoader;

/**
 * @ClassName ExcelReader
 * @Description excel�Ķ���
 * @Author starslink
 * @Date 2023/5/30 21:16
 * @Version 1.0
 */
public class ExcelReader extends AbstractResourceReader {

    /**
     * ��ʼ��������
     *
     * @param sourceLoader
     * @param targetLoader
     */
    public ExcelReader(ResourceLoader sourceLoader, ResourceLoader targetLoader) {
        super(sourceLoader, targetLoader);
    }
}
