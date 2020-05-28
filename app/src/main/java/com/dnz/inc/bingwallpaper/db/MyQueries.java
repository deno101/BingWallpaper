package com.dnz.inc.bingwallpaper.db;

import com.dnz.inc.bingwallpaper.db.ContractSchema.ImageDataTable;

public class MyQueries {

    public static final String CREATE_QUERY_IMAGE_TABLE = String.format("" +
                    "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT," +
                    " %s TEXT, %s TEXT)",
            ImageDataTable.TABLE_NAME, ImageDataTable._ID, ImageDataTable.COLUMN_TITLE,
            ImageDataTable.COLUMN_D_C,
            ImageDataTable.COLUMN_IS_FAVORITE);

    public static final String DROP_TABLE_QUERY =
            String.format("DROP TABLE IF EXISTS %s", ImageDataTable.TABLE_NAME);

    public static final String SELECT_ALL_QUERY = String.format("SELECT * FROM %s ORDER BY %s DESC",
            ImageDataTable.TABLE_NAME, ImageDataTable.COLUMN_D_C);

    public static final String SELECT_BY_DATE = String.format("SELECT * FROM %s WHERE %s = ",
            ImageDataTable.TABLE_NAME, ImageDataTable.COLUMN_D_C);
}
