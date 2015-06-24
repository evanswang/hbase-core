Key Value schema installation guide:

1.Install and deploy transmartApp version
Please download here
https://git.etriks.org/transmart-postgresql/tree/hbase-0
// This is the main tranSMART project.

2.HBase-0.98.0-hadoop2 (or CDH5.2) and hadoop-2.5.2 installation:
wget https://archive.apache.org/dist/hbase/hbase-0.98.0/hbase-0.98.0-hadoop2-bin.tar.gz
wget http://archive.apache.org/dist/hadoop/common/hadoop-2.5.2/hadoop-2.5.2.tar.gz
please read the docs folder in each package to know how to deploy a proper HBase cluster.

3.Install the schema using data-loading for mrna
https://git.etriks.org/s.wang11/hbase-data-loading/tree/master

4. Add configurations into Config.groovy (!!!This is just an e.g., please change the parameter to you own setting!!!)
org.transmart.kv.enable = true
hbase.rootdir = "hdfs://{hadoop-master-hostname}:{hdfs-port}/hbase"
hbase.master = "{hbase-master-hostname}"
hbase.zookeeper.quorum = "{zookeeper-node-hostname}"
hbase.tmp.dir = "{hbase-data-dir}"
hbase.zookeeper.property.clientPort = "{zookeeper-port}"
fs.default.name = "hdfs://{hadoop-master-hostname}:{hdfs-port}"

5. Deploy hbase-core plugin
Please add grails.plugin.location.'hbase-core' = "{your-path}/hbase-core" to BuildConfig.groovy
