Key Value schema installation guide:

1.Install and deploy transmartApp version
Please download here
https://git.etriks.org/transmart-postgresql/tree/hbase-0
# This is the main tranSMART project.

2.HBase-0.98.0 (or CDH5.2) installation:
wget
https://archive.apache.org/dist/hbase/hbase-0.98.0/hbase-0.98.0-hadoop2-bin.tar.gz


tar xzf hbase-0.98.0.tar.gz
cd hbase-0.98.0
chmod +x bin/*.sh
vim conf/hbase-env.sh
#add export JAVA_HOME=${your jdk home}, 1.6 or 1.7
bin/start-hbase.sh
bin/hbase classpath
#copy this class path {HBASECLASSPATH}

3.Install the schema using data-loading for mrna
3.1Download hbase-data-loading
Download link:
https://git.etriks.org/s.wang11/hbase-data-loading/tree/master

3.2unzip hbase-data-loading.zip
3.3cd hbase-data-loading/microarray
3.4compile java for microarray
javac -classpath {HBASECLASSPATH} HBaseTM.java
3.5initialize microarray schema
java -classpath {HBASECLASSPATH}:. HBaseTM init microarray
3.6insert microarray data
e.g.
java -classpath {HBASECLASSPATH}:. HBaseTM insert
microarray
data-sample/gse1456.patientnum sample/gse1456.probenames,
sample/gse1456.gene sample/gse1456.data.row 1000
3.7compile java for vcf data
cd ../vcf
javac -classpath {HBASECLASSPATH} HBaseTM.java
HBaseSNP.java
3.8initialize vcf schema
java -classpath {HBASECLASSPATH}:. HBaseTM init vcf
3.9 insert data
cd datasample
tar xzf 1000384994.tar.gz
tar xzf 1000384998.tar.gz
cd ..
java -classpath {HBASECLASSPATH}:. HBaseTM insertSubIndSNP
GSE1456 1000384994
java -classpath {HBASECLASSPATH}:. HBaseTM insertSubIndSNP
GSE1456 1000384998

4. Add configurations into Config.groovy (!!!This is
just an
e.g., please change the parameter to you own setting!!!)
org.transmart.kv.enable = true
hbase.rootdir = "hdfs://mysql01:59000/hbase"
hbase.cluster.distributed = "true"
hbase.master = "mysql01"
hbase.zookeeper.quorum = "mysql01"
local.dir = "/data/hbasedata/hbaselocal"
hbase.tmp.dir = "/data/hbasedata/hbasetmp"
hbase.zookeeper.property.clientPort = "52222"
hbase.zookeeper.property.dataDir = "/data/zookeeper"
fs.default.name = "hdfs://mysql01:59000"

5. Deploy hbase-core plugin
Please download from
https://git.etriks.org/s.wang11/hbase-core/tree/peter-0.2
Please add grails.plugin.location.'key-value-core' =
“{YOUR/PATH}/key-value-core" to BuildConfig.groovy

6. Start transmart app
Please go to transmartApp folder and run 'grails run-app’.
