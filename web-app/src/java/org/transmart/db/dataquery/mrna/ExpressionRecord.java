package org.transmart.db.dataquery.mrna;

/**
 * Created by EvanSWang on 15-2-10.
 */
public class ExpressionRecord {
    String patientID;
    String probeset;
    String value;
    public ExpressionRecord (String patientID, String probeset, String value) {
        this.patientID = patientID;
        this.probeset = probeset;
        this.value = value;
    }
    public String getPatientID () {
        return patientID;
    }
    public String getProbeset () {
        return probeset;
    }
    public String getValue () {
        return value;
    }
}

