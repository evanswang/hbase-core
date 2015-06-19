package org.transmart.db.dataquery.mrna

import org.springframework.context.ApplicationContext;

/**
 * Created by EvanSWang on 15-2-10.
 */

class SQLModule {
    static ApplicationContext ctx = org.codehaus.groovy.grails.web.context.ServletContextHolder.getServletContext().getAttribute(org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes.APPLICATION_CONTEXT)
    def static dataSource = ctx.getBean('dataSource')

    public static List<BigDecimal> getPatients (String resultInstanceId) {
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
                patientList.add(row.patient_num)
            })
            patientList.each { id ->
                System.err.println("Each patient number is **************************** " + id.toString())
            }
        } finally {
            sql.close()
        }
        return patientList
    }

    public static String getTrial(String ontologyTerm) {
        groovy.sql.Sql sql = null
        String studyName = null
        try {
            // get study name via sql
            System.err.println("start get study name for **************************** " + ontologyTerm)
            sql = new groovy.sql.Sql(dataSource)
            StringBuilder assayS = new StringBuilder()
            assayS.append("""       SELECT
                                        sourcesystem_cd
                                    FROM
                                        i2b2demodata.concept_dimension
                                    WHERE
                                        concept_path = ? """);
            sql.eachRow(assayS.toString(), [ontologyTerm], { row ->
                studyName = row.sourcesystem_cd
            })
            System.err.println("Each study name is **************************** " + studyName.toString())

        } finally {
            sql.close()
        }
        studyName.toString()
    }
}

