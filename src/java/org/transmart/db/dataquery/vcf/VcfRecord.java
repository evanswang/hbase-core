package org.transmart.db.dataquery.vcf;

/**
 * Created by EvanSWang on 15-3-7.
 */
public class VcfRecord {
    String key;
    String SAMPLETYPE;
    String TIMEPOINT;
    String TISSUETYPE;
    String GPLID;
    String ASSAYID;
    String SAMPLECODE;
    String REFERENCE;
    String VARIANT;
    String VARIANTTYPE;
    String RSID;
    String REFERENCEALLELE;

    public VcfRecord(String key, String SAMPLETYPE,
                     String TIMEPOINT, String TISSUETYPE,
                     String GPLID, String ASSAYID,
                     String SAMPLECODE, String REFERENCE,
                     String VARIANT, String VARIANTTYPE,
                     String RSID, String REFERENCEALLELE) {
        this.key = key;
        this.SAMPLETYPE = SAMPLETYPE;
        this.TIMEPOINT = TIMEPOINT;
        this.TISSUETYPE = TISSUETYPE;
        this.GPLID = GPLID;
        this.ASSAYID = ASSAYID;
        this.SAMPLECODE = SAMPLECODE;
        this.REFERENCE = REFERENCE;
        this.VARIANT = VARIANT;
        this.VARIANTTYPE = VARIANTTYPE;
        this.RSID = RSID;
        this.REFERENCEALLELE = REFERENCEALLELE;
    }

    String getkey() {return this.key;}
    String getSAMPLETYPE() {return this.SAMPLETYPE;}
    String getTIMEPOINT() {return this.TIMEPOINT;}
    String getTISSUETYPE() {return this.TISSUETYPE;}
    String getGPLID() {return this.GPLID;}
    String getASSAYID() {return this.ASSAYID;}
    String getSAMPLECODE() {return this.SAMPLECODE;}
    String getREFERENCE() {return this.REFERENCE;}
    String getVARIANT() {return this.VARIANT;}
    String getVARIANTTYPE() {return this.VARIANTTYPE;}
    String getRSID() {return this.RSID;}
    String getREFERENCEALLELE() {return this.REFERENCEALLELE;}
}

