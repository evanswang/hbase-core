Key Value schema installation guide:

1.Install and deploy transmartApp version
Please download here
https://git.etriks.org/transmart-postgresql/tree/hbase-0
// This is the main tranSMART project.

2.HBase-0.98.0 (or CDH5.2) installation:
wget
https://archive.apache.org/dist/hbase/hbase-0.98.0/hbase-0.98.0-hadoop2-bin.tar.gz


tar xzf hbase-0.98.0.tar.gz
cd hbase-0.98.0
chmod +x bin/*.sh
vim conf/hbase-env.sh
//add export JAVA_HOME=${your jdk home}, 1.6 or 1.7
bin/start-hbase.sh
bin/hbase classpath
//copy this class path {HBASECLASSPATH}

3.Install the schema using data-loading for mrna
https://git.etriks.org/s.wang11/hbase-data-loading/tree/master

4. Add configurations into Config.groovy (!!!This is
just an
e.g., please change the parameter to you own setting!!!)
org.transmart.kv.enable = true
hbase.rootdir = "hdfs://{hadoop-master-hostname}:{hdfs-port}/hbase"
hbase.master = "{hbase-master-hostname}"
hbase.zookeeper.quorum = "{zookeeper-node-hostname}"
hbase.tmp.dir = "{hbase-data-dir}"
hbase.zookeeper.property.clientPort = "{zookeeper-port}"
fs.default.name = "hdfs://{hadoop-master-hostname}:{hdfs-port}"

5. Deploy hbase-core plugin
Please download from
https://github.com/evanswang/hbase-core/edit/master
Please add grails.plugin.location.'key-value-core' =
"{your-path}/key-value-core" to BuildConfig.groovy

6. Start transmart app
Please go to transmartApp folder and run 'grails run-app’.
