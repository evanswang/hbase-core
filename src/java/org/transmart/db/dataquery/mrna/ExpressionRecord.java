package org.transmart.db.dataquery.mrna;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by EvanSWang on 15-2-10.
 */
public class ExpressionRecord {
    String patientID;
    String gene;
    String probeset;
    String value;
    Map<String, String> values = new HashMap<String,String>();
    public ExpressionRecord (String patientID, String gene, String probeset, String value) {
        this.patientID = patientID;
        this.gene = gene;
        this.probeset = probeset;
        this.value = value;
    }
    public void addValue (String dataType, String value) {
        values.put(dataType, value);
    }
    public String getPatientID () {
        return patientID;
    }
    public String getGene () { return gene; }
    public String getProbeset () { return probeset; }
    public String getValue () {
        return value;
    }
    public Map<String, String> getValues () {
        return values;
    }
}

