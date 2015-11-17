/***
 * @auther wsc
 * @date 20150607
 * @op add conceptCD to the row key due to multiple high dimensional concepts for one subject.
 * The corresponding code in export service in app is HighDimExportService.groovy
 */


package org.transmart.db.dataquery.mrna;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.filter.MultipleColumnPrefixFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.transmart.db.dataquery.HBaseConfig;

public class KVMrnaModule {
    private static final String COL_FAMILY_RAW = "raw";
    private static final String COL_FAMILY_LOG = "log";
    //private static final String COL_FAMILY_MEAN = "mean";
    //private static final String COL_FAMILY_MEDIAN = "median";
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
    static String dataType;

    public KVMrnaModule(String table, String dataType) throws IOException {
        //config = HBaseConfig.getHConfig();
		// debug simple hbase inst
		Configuration config = HBaseConfiguration.create();
        MicroarrayTable = new HTable(config, table);
        this.dataType = dataType;
    }

    /**
     * retrieve data from HBase microarray table
     * @param patientList
     */
    public List<ExpressionRecord> getRecord(String trialName, List<String> patientList, String conceptCD) throws IOException {
        long count = 0;
        long ts = System.currentTimeMillis();

        List<ExpressionRecord> results = new ArrayList<ExpressionRecord>();
        for (String patientID : patientList) {
            results.addAll(getKV(trialName, patientID, conceptCD));
        }
        return results;
    }

    /**
     * retrieve data from HBase microarray table
     * @param patientList
     */
    public List<ExpressionRecord> getRecord(String trialName, List<String> patientList, String conceptCD, List<String> filterList) throws IOException {
        long count = 0;
        long ts = System.currentTimeMillis();
        List<ExpressionRecord> results = new ArrayList<ExpressionRecord>();
        for (String patientID : patientList) {
            results.addAll(getKV(trialName, patientID, conceptCD, filterList, null));
        }
        return results;
    }

    /**
     * retrieve data from HBase microarray table
     *
     */
    public List<ExpressionRecord> getAllRecords (String trialName, String patientID, String conceptCD)
            throws IOException {
        List<ExpressionRecord> result = getKV(trialName, patientID, conceptCD, null, COL_FAMILY_RAW);
        addValue(trialName, patientID, conceptCD, null, COL_FAMILY_LOG, result);
        addValue(trialName, patientID, conceptCD, null, COL_FAMILY_ZSCORE, result);
        System.err.println("************************ @wsc print result number ++++++++++" + result.size());
        return result;
    }

    private List<ExpressionRecord> getKV (String trialName, String patientID, String conceptCD) throws IOException {
        return getKV(trialName, patientID, conceptCD, null, null);
    }

    private List<ExpressionRecord> getKV (String trialName, String patientID, String conceptCD, List<String> filterList, String specDataType) throws IOException {
        Get g = new Get(Bytes.toBytes(trialName + ":" + conceptCD + ":" + patientID.toString()));
        String family;
        if (specDataType == null) {
            family = dataType;
        } else {
            family = specDataType;
        }
        g.addFamily(Bytes.toBytes(family));

        if (filterList != null) {
            MultipleColumnPrefixFilter filter;
            byte [][] filter_prefix = new byte [filterList.size()][];
            int i = 0;
            for (String filterStr : filterList) {
                filter_prefix[i ++] = Bytes.toBytes(filterStr);
            }
            filter = new MultipleColumnPrefixFilter(filter_prefix);
            g.setFilter(filter);
        }
        List<ExpressionRecord> result = new ArrayList<ExpressionRecord>();
        MicroarrayTable.setScannerCaching(10);
        Result r = MicroarrayTable.get(g);
        int count = 0;
        for (Cell cell : r.rawCells()) {
            String patient = Bytes.toString(CellUtil.cloneRow(cell));
            String probeset = Bytes.toString(CellUtil.cloneQualifier(cell));
            String value = Bytes.toString(CellUtil.cloneValue(cell));
            ExpressionRecord record = new ExpressionRecord(patient, "null", probeset, value);
            record.addValue(family, value);
            result.add(record);
            count ++;
        }
        System.err.println("************************** @wsc print get records *******" + count);
        return result;
    }

    private void addValue (String trialName, String patientID, String conceptCD, List<String> filterList, String specDataType, List<ExpressionRecord> result) throws IOException {
        Get g = new Get(Bytes.toBytes(trialName + ":" + conceptCD + ":" + patientID.toString()));
        if (specDataType == null) {
            throw new IOException("@wsc ask to specify dataType");
        } else {
            g.addFamily(Bytes.toBytes(specDataType));
        }

        if (filterList != null) {
            MultipleColumnPrefixFilter filter;
            byte [][] filter_prefix = new byte [filterList.size()][];
            int i = 0;
            for (String filterStr : filterList) {
                filter_prefix[i ++] = Bytes.toBytes(filterStr);
            }
            filter = new MultipleColumnPrefixFilter(filter_prefix);
            g.setFilter(filter);
        }
        MicroarrayTable.setScannerCaching(10);
        Result r = MicroarrayTable.get(g);
        int i = 0;
        for (Cell cell : r.rawCells()) {
            result.get(i).addValue(specDataType, Bytes.toString(CellUtil.cloneValue(cell)));
            i++;
        }
        if (i != result.size())
            System.err.println("@wsc print ********** " + specDataType + " ********* result size is different, pls check");
        if (r.isEmpty())
            System.out.println("@wsc print ********** no result " + patientID);
    }
}


