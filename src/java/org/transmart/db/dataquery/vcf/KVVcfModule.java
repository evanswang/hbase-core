package org.transmart.db.dataquery.vcf;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.transmart.db.dataquery.HBaseConfig;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;

/**
 * Created by EvanSWang on 15-3-7.
 */
public class KVVcfModule {
    static final String COL_FAMILY_POSITION = "pos";
    static Configuration config;
    static HBaseAdmin hadmin;
    static HTable VcfTable;

    public KVVcfModule(String table) throws IOException {
        config = HBaseConfig.getHConfig();
        VcfTable = new HTable(config, table);
    }

    private void writeAllRecords (PrintWriter pw, String trialName, String conceptCD, String subjectID) throws IOException {
        // TODO cache result to speed up export
        if (pw == null)
            throw new IOException("@KVVcfModule writeAllRecords complaints that pw is null *******************");

        Scan s = new Scan();
        s.addFamily(Bytes.toBytes(COL_FAMILY_POSITION));
        s.setCacheBlocks(true);
        s.setCaching(10000);
        s.setStartRow(Bytes.toBytes(trialName + ":" + conceptCD + ":" + subjectID + ":"));
        s.setStopRow(Bytes.toBytes(trialName + ":" + conceptCD + ":" + (Long.parseLong(subjectID + "") + 1) + ":"));
        ResultScanner scanner = null;
        long count = 0;
        try {
            scanner = VcfTable.getScanner(s);
            long ts1 = System.currentTimeMillis();
            boolean isFirst;
            for (Result rr = scanner.next(); rr != null; rr = scanner.next()) {
                isFirst = true;
                for (Cell kv : rr.rawCells()) {
                    // only the first key should be print
                    if (isFirst) {
                        pw.print(Bytes.toString(CellUtil.cloneRow(kv)).replaceAll(":", "\t") + "\t");
                        isFirst = false;
                    }
                    pw.print(Bytes.toString(CellUtil.cloneValue(kv)) + "\t");
                }
                pw.println();
                count++;
            }
            System.out.println("time is " + (System.currentTimeMillis() - ts1));
            System.out.println("total amount is " + count);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}

