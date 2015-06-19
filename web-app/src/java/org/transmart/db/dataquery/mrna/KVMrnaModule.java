package org.transmart.db.dataquery.mrna;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

public class KVMrnaModule {
    private static final String COL_FAMILY_RAW = "raw";
    private static final String COL_FAMILY_LOG = "log";
    private static final String COL_FAMILY_MEAN = "mean";
    private static final String COL_FAMILY_MEDIAN = "median";
    private static final String COL_FAMILY_ZSCORE = "zscore";

    private static final String PATIENT_ID = "patient_id";
    private static final String RAW_VALUE = "raw";
    private static final String LOG_VALUE = "log";
    private static final String MEAN_VALUE = "mean";
    private static final String MEDIAN_VALUE = "median";
    private static final String ZSCORE = "z_score";

    static Configuration config;

    static HBaseAdmin hadmin;
    static HTable MicroarrayTable;

    public KVMrnaModule(String table) throws IOException {
        config = HBaseConfiguration.create();
        //config = new Configuration();
        //config.addResource(new Path("/data/hbase-0.96.0-hadoop1/conf/hbase-default.xml"));
        //config.addResource(new Path("/data/hbase-0.96.0-hadoop1/conf/hbase-site.xml"));
        //config.addResource(new Path("/data/hadoop-1.0.3/conf/core-default.xml"));
        //config.addResource(new Path("/data/hadoop-1.0.3/conf/core-site.xml"));
        //config.addResource(new Path("/data/hadoop-1.0.3/conf/mapred-site.xml"));
        //config = HBaseConfig.getHHConfig(config);
        //System.err.println("@wsc print hbase config *************** " + config.toString());

        config.clear();
        // hbase config
        config.set("hbase.rootdir", "hdfs://mysql01:59000/hbase");
        config.set("hbase.cluster.distributed","true");
        config.set("hbase.master", "mysql01");
        config.set("hbase.zookeeper.quorum", "mysql01");
        config.set("local.dir", "/data/hbasedata/hbaselocal");
        config.set("hbase.tmp.dir", "/data/hbasedata/hbasetmp");
        config.set("hbase.zookeeper.property.clientPort", "52222");
        config.set("hbase.zookeeper.property.dataDir", "/data/zookeeper");
        // hadoop config
        config.set("fs.default.name", "hdfs://mysql01:59000");

        MicroarrayTable = new HTable(config, table);
    }

    /**
     * retrieve data from HBase microarray table
     * @param patientList
     */
    public List<ExpressionRecord> getRecord(String trailName, List<BigDecimal> patientList) throws IOException {
        long count = 0;
        long ts = System.currentTimeMillis();
        System.err.println("@wsc is getting records from hbase for study *********************" + trailName + ":" + patientList.get(0));

        List<ExpressionRecord> results = new ArrayList<ExpressionRecord>();
        for (BigDecimal patientID : patientList) {
            System.err.println("@wsc start GET ********************* " + patientID);
            Get g = new Get(Bytes.toBytes(trailName + ":" + patientID.toString()));
            g.addFamily(Bytes.toBytes(COL_FAMILY_RAW));
            // getlist.add(g);

            MicroarrayTable.setScannerCaching(10);
            Result r = MicroarrayTable.get(g);
            for (Cell cell : r.rawCells()) {
                String patient = Bytes.toString(CellUtil.cloneRow(cell));
                String probeset = Bytes.toString(CellUtil.cloneQualifier(cell));
                String value = Bytes.toString(CellUtil.cloneValue(cell));
                results.add(new ExpressionRecord(patient, probeset, value));
            }

            System.out.println("@wsc print ********** result " + count++);
            if (r.isEmpty())
                System.out.println("@wsc print ********** no result " + patientID);

        }

        System.out.println("@wsc print ********** get time is " + (System.currentTimeMillis() - ts));
        return results;
    }
}

