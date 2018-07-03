package com.gzp.statisticssdk.utils.db;

import java.util.ArrayList;
import java.util.List;

/**
 * author: Gzp
 * Create on 2018/7/3
 * Description:
 */
public class DatabaseOperator extends AbstractDbHelper {

    /**
     * 数据库名称
     */
    private final String DATABASE_NAME = "";

    /**
     * 数据版本号
     */
    private final int DATABASE_VERSION = 1;


    @Override
    protected String dbName() {
        return DATABASE_NAME;
    }

    @Override
    protected int dbVersion() {
        return DATABASE_VERSION;
    }

    @Override
    protected List<String> createTables() {
        List<String> tables = new ArrayList<>();
        tables.add("");
        return tables;
    }


}
