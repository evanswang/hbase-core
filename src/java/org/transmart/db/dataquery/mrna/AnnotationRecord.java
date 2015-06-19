package org.transmart.db.dataquery.mrna;

/**
 * Created by EvanSWang on 15-6-7.
 */
public class AnnotationRecord {
    /*patient_id, subject_id, assay_id,
    *      sample_type, trial_name, timepoint,
            *      tissue_type, gpl_id, sample_cd*/

    private String patientID ;
    private String subjectID ;
    private String assayID ;
    private String sampleType ;
    private String studyName ;
    private String timePoint ;
    private String tissueType ;
    private String gplID ;
    private String sampleCD ;

    public void setPatientID (String patientID) { this.patientID = patientID; }
    public void setSubjectID (String subjectID) { this.subjectID = subjectID; }
    public void setAssayID (String assayID) { this.assayID = assayID; }
    public void setSampleType (String sampleType) { this.sampleType = sampleType; }
    public void setStudyName (String studyName) { this.studyName = studyName; }
    public void setTimePoint (String timePoint) { this.timePoint = timePoint; }
    public void setTissueType (String tissueType) { this.tissueType = tissueType; }
    public void setGplID (String gplID) { this.gplID = gplID; }
    public void setSampleCD (String sampleCD) { this.sampleCD = sampleCD; }

    public String getPatientID () {return this.patientID;}
    public String getSubjectID () {return this.subjectID;}
    public String getAssayID () {return this.assayID;}
    public String getSampleType () {return this.sampleType;}
    public String getStudyName () {return this.studyName;}
    public String getTimePoint () {return this.timePoint;}
    public String getTissueType () {return this.tissueType;}
    public String getGplID () {return this.gplID;}
    public String getSampleCD () {return this.sampleCD;}

    public String toString() {
        return "patientID:" + this.patientID +
                    "subjectID:" + this.subjectID +
                    "assayID:" + this.assayID +
                    "sampleType:" + this.sampleType +
                    "studyName:" + this.studyName +
                    "timePoint:" + this.timePoint +
                    "tissueType:" + this.tissueType +
                    "gplID:" + this.gplID +
                    "sampleCD:" + this.sampleCD;
    }
}

