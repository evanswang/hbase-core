package org.transmart.db.dataquery.mrna;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;


import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * Created by EvanSWang on 15-2-10.
 */

public class HBaseConfig {
    public static org.apache.hadoop.conf.Configuration getHHConfig(Configuration conf) {
        InputStream confResourceAsInputStream = conf.getConfResourceAsInputStream("/data/hbase-0.96.0-hadoop1/conf/hbase-site.xml");
        int available = 0;
        try {
            available = confResourceAsInputStream.available();
        } catch (Exception e) {
            //for debug purpose
            System.err.println("@wsc got errors when configurating the hbase ***********************");
        } finally {
            IOUtils.closeQuietly(confResourceAsInputStream);
        }
        if (available == 0 ) {
            System.err.println("@wsc print default hbase config is not available ***********************");
            conf = new Configuration();
            conf.addResource("/data/hadoop-1.0.3/conf/core-site.xml");
            conf.addResource("/data/hbase-0.96.0-hadoop1/conf/hbase-site.xml");

        }
        return conf;
    }
}



