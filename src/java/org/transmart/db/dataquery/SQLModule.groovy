package org.transmart.db.dataquery

import org.springframework.context.ApplicationContext
import org.transmart.db.dataquery.mrna.AnnotationRecord;

/**
 * Created by EvanSWang on 15-2-10.
 */

class SQLModule {
    static ApplicationContext ctx = org.codehaus.groovy.grails.web.context.ServletContextHolder.getServletContext().getAttribute(org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes.APPLICATION_CONTEXT)
    def static dataSource = ctx.getBean('dataSource')

    public static List<String> getPatients (String resultInstanceId) {
        groovy.sql.Sql sql = null
        def patientList = []
        try {
            // get patient list via sql
            sql = new groovy.sql.Sql(dataSource)
            StringBuilder assayS = new StringBuilder()
            assayS.append("""	SELECT
                    patient_num
                    FROM
                    i2b2demodata.qt_patient_set_collection
                    WHERE
                    result_instance_id = ? """)
            sql.eachRow(assayS.toString(), [resultInstanceId], { row ->
                patientList.add(row.patient_num.toString())
                System.err.println("******************** wsc ************ " + row.patient_num.toString());
            })
        } finally {
            sql.close()
        }
        return patientList
    }

    public static Map<String, String> getTrialandConceptCD(String ontologyTerm) {
        groovy.sql.Sql sql = null
        Map<String, String> resultMap = new HashMap<String, String>()
        String studyName = null
        String conceptCD = null
        try {
            // get study name via sql
            sql = new groovy.sql.Sql(dataSource)
            StringBuilder assayS = new StringBuilder()
            assayS.append("""       SELECT
                                        sourcesystem_cd, concept_cd
                                    FROM
                                        i2b2demodata.concept_dimension
                                    WHERE
                                        concept_path = ? """);
            sql.eachRow(assayS.toString(), [ontologyTerm], { row ->
                studyName = row.sourcesystem_cd
                conceptCD = row.concept_cd
            })
            resultMap.put("study_name", studyName)
            resultMap.put("concept_cd", conceptCD)
        } finally {
            sql.close()
        }
        resultMap
    }

    public static Map<String, String> getGeneName (List<String> searchkeyList) {
        // may need update
        groovy.sql.Sql sql = null
        def geneMap = [:]
        try {
            // get gene nick names from search keys
            sql = new groovy.sql.Sql(dataSource)
            StringBuilder assayS = new StringBuilder()
            StringBuilder searchkeyListStr = new StringBuilder()
            searchkeyList.each {
                searchkeyListStr.append(it + ",")
            }
            assayS.append("""   SELECT
                                    search_keyword_id, keyword_term
                                FROM
                                    searchapp.search_keyword_term
                                WHERE
                                    search_keyword_id IN ( """ + searchkeyListStr.substring(0, searchkeyListStr.length() - 1) + """ )
                                AND
                                    rank = 1 """)
            sql.eachRow(assayS.toString(), { row ->
                geneMap.put(row.search_keyword_id, row.keyword_term)
            })
        } finally {
            sql.close()
        }
        return geneMap
    }

    public static Map<String, String> getProbes2GeneMap (List<String> searchkeyList) {
        // may need update
        groovy.sql.Sql sql = null
        def geneMap = [:]

        try {
            // get gene nick names from search keys
            sql = new groovy.sql.Sql(dataSource)
            StringBuilder assayS = new StringBuilder()
            StringBuilder searchkeyListStr = new StringBuilder()
            searchkeyList.each {
                searchkeyListStr.append(it + ",")
            }
            assayS.append("""   SELECT
                                    s1.keyword_term gene, d1.probe_id probe
                                FROM
                                    searchapp.search_keyword_term s1
                                INNER JOIN
                                    deapp.de_mrna_annotation d1
                                ON
                                    s1.keyword_term = d1.gene_symbol
                                WHERE
                                    s1.search_keyword_id IN ( """ + searchkeyListStr.substring(0, searchkeyListStr.length() - 1) + """ )
                                AND
                                    s1.rank = 1 """)
            sql.eachRow(assayS.toString(), { row ->
                // @wsc is it possibe one probe has multiple genes?
                geneMap.put(probe, gene);
            })
        } finally {
            sql.close()
        }
        return geneMap
    }

    public static Map<String, String> getGeneInfo (List<String> probeList) {
        groovy.sql.Sql sql = null
        def geneMap = [:]
        try {
            // get gene nick names from search keys
            sql = new groovy.sql.Sql(dataSource)
            StringBuilder assayS = new StringBuilder()
            StringBuilder probeListStr = new StringBuilder()
            probeListStr.append("'")
            probeList.each {
                probeListStr.append(it + "','")
            }
            assayS.append("""   SELECT
                                    probe_id, gene_symbol, gene_id
                                FROM
                                    deapp.de_mrna_annotation
                                WHERE
                                    probe_id IN ( """ + probeListStr.substring(0, probeListStr.length() - 2) + """ )  """)
            sql.eachRow(assayS.toString(), { row ->
                geneMap.put(row.probe_id + "", row.gene_symbol + "\t" + row.gene_id)
            })
        } finally {
            sql.close()
        }
        return geneMap
    }


    //SELECT patient_num, sourcesystem_cd FROM i2b2demodata.patient_dimension WHERE patient_num = ?
    public static Map<BigDecimal, String> getPatientMap (List<BigDecimal> patientList) {
        groovy.sql.Sql sql = null
        def patientMap = [:]
        try {
            // get gene nick names from search keys
            sql = new groovy.sql.Sql(dataSource)
            StringBuilder assayS = new StringBuilder()
            StringBuilder patientListStr = new StringBuilder()
            patientList.each {
                patientListStr.append(it + ",")
            }
            assayS.append("""   SELECT
                                    patient_num, sourcesystem_cd
                                FROM
                                    i2b2demodata.patient_dimension
                                WHERE
                                    patient_num IN ( """ + patientListStr.substring(0, patientListStr.length() - 1) + """ ) """)
            sql.eachRow(assayS.toString(), { row ->
                String trial = row.sourcesystem_cd.toString()
                patientMap.put(row.patient_num, trial.substring(0, trial.indexOf(":")))
            })
        } finally {
            sql.close()
        }
        return patientMap
    }

    /*
     *  SELECT patient_id, subject_id, assay_id,
     *      sample_type, trial_name, timepoint,
     *      tissue_type, gpl_id, sample_cd
     *  FROM deapp.de_subject_sample_mapping
     *  WHERE concept_code = '1344440' and patient_id = 1000385063
     */
    public static Map<String, AnnotationRecord> getPatientMapping (List<BigDecimal> patientList, String conceptCD) {
        groovy.sql.Sql sql = null
        def patientMap = [:]
        try {
            // get gene nick names from search keys
            sql = new groovy.sql.Sql(dataSource)
            StringBuilder assayS = new StringBuilder()
            StringBuilder patientListStr = new StringBuilder()
            patientList.each {
                patientListStr.append(it + ",")
            }
            assayS.append("""   SELECT
                                    patient_id, subject_id, assay_id,
                                    sample_type, trial_name, timepoint,
                                    tissue_type, gpl_id, sample_cd
                                FROM
                                    deapp.de_subject_sample_mapping
                                WHERE
                                    concept_code = '""" + conceptCD + """'
                                AND
                                    patient_id IN ( """ + patientListStr.substring(0, patientListStr.length() - 1) + """ ) """)
            sql.eachRow(assayS.toString(), { row ->
                AnnotationRecord annotationRecord = new AnnotationRecord()
                //String studyName = row.sourcesystem_cd.toString()
                annotationRecord.setPatientID(row.patient_id.toString())
                annotationRecord.setSubjectID(row.subject_id.toString())
                annotationRecord.setAssayID(row.assay_id.toString())
                annotationRecord.setSampleType(row.sample_type.toString())
                annotationRecord.setStudyName(row.trial_name.toString())
                annotationRecord.setTimePoint(row.timepoint.toString())
                annotationRecord.setTissueType(row.tissue_type.toString())
                annotationRecord.setGplID(row.gpl_id.toString())
                annotationRecord.setSampleCD(row.sample_cd.toString())
                patientMap.put(row.subject_id.toString(), annotationRecord)
            })
        } finally {
            sql.close()
        }
        return patientMap
    }
}

