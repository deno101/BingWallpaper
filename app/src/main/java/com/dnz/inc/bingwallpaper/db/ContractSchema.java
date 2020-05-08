package com.dnz.inc.bingwallpaper.db;

import android.provider.BaseColumns;

public class ContractSchema {

    // ensure no instantiation of the class

    public static class ImageDataTable implements BaseColumns{

        // NOTE COLUMNS ARE LINKED TO (String[] COLUMNS) and MyQueries.java
        public static final String TABLE_NAME = "image_data";

        public static final String COLUMN_TITLE = "title";// TEXT
        public static final String COLUMN_D_C = "date_created";// TEXT 2019-sep-20
        public static final String COLUMN_PATH = "image_path"; //TEXT
        public static final String COLUMN_EXP_DATE = "exp_date"; // INT epoch time
        public static final String COLUMN_IS_FAVORITE = "is_favorite"; // int 1 or 0

        public static final String[] COLUMNS = {
                COLUMN_D_C,
                COLUMN_EXP_DATE,
                COLUMN_IS_FAVORITE,
                COLUMN_PATH,
                COLUMN_TITLE,
        };

    }
}
