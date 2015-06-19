package org.transmart.db.dataquery;

import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.codehaus.groovy.grails.commons.ConfigurationHolder;

/**
 * Created by EvanSWang on 15-2-10.
 * This class is for testing only.
 */

public class HBaseConfig {
    public static Configuration getHConfig() {
        Configuration config = HBaseConfiguration.create();
        config.clear();
        // hbase config
        System.err.println("@wsc print hbase.rootdir is " + ConfigurationHolder.getFlatConfig().get("hbase.rootdir"));//+ (String) appConfig.get("hbase.rootdir"));
        config.set("hbase.rootdir", (String) ConfigurationHolder.getFlatConfig().get("hbase.rootdir"));
        config.set("hbase.cluster.distributed", (String) ConfigurationHolder.getFlatConfig().get("hbase.cluster.distributed"));
        config.set("hbase.master", (String) ConfigurationHolder.getFlatConfig().get("hbase.master"));
        config.set("hbase.zookeeper.quorum", (String) ConfigurationHolder.getFlatConfig().get("hbase.zookeeper.quorum"));
        //config.set("local.dir", (String) ConfigurationHolder.getFlatConfig().get("local.dir"));
        config.set("hbase.tmp.dir", (String) ConfigurationHolder.getFlatConfig().get("hbase.tmp.dir"));
        config.set("hbase.zookeeper.property.clientPort", (String) ConfigurationHolder.getFlatConfig().get("hbase.zookeeper.property.clientPort"));
        //config.set("hbase.zookeeper.property.dataDir", (String) ConfigurationHolder.getFlatConfig().get("hbase.zookeeper.property.dataDir"));
        // hadoop config
        config.set("fs.default.name", (String) ConfigurationHolder.getFlatConfig().get("fs.default.name"));
        return config;
    }

    public static Configuration validateHConfig(Configuration conf) {
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

