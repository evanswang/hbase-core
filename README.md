Key Value schema installation guide:

1. Install and deploy transmartApp version
Please download here
https://git.etriks.org/transmart-postgresql/tree/hbase-0
// This is the main tranSMART project.

2. HBase-0.98.0-hadoop2 (or CDH5.2) and hadoop-2.5.2 installation:
wget https://archive.apache.org/dist/hbase/hbase-0.98.0/hbase-0.98.0-hadoop2-bin.tar.gz
wget http://archive.apache.org/dist/hadoop/common/hadoop-2.5.2/hadoop-2.5.2.tar.gz
please read the docs folder in each package to know how to deploy a proper HBase cluster.

3. Install the schema using data-loading for mrna
https://git.etriks.org/s.wang11/hbase-data-loading/tree/master
