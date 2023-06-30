package com.enjplatform.reconciliation.loader;

import com.enjplatform.reconciliation.before.ResourceLoader;
import com.enjplatform.reconciliation.domain.CheckEntry;
import java.util.List;

/**
 * @ClassName MySQLResourceLoader
 * @Description 数据库资源读取 todo 用户自定义
 * @Author starslink
 * @Date 2023/5/30 17:35
 * @Version 1.0
 */
public class MySQLResourceLoader implements ResourceLoader {

    @Override
    public List<CheckEntry> load(String date) {

        // 1. 查询一段时间的数据
        // 2. 查询到数据封装成对象 全部存储到内存里面


        return null;
    }
}
