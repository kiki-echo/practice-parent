//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.example.utils;


import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.filter.FilterList.Operator;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

@Component
public class HBaseMRSUtils {
    private static final Logger log = LoggerFactory.getLogger(HBaseMRSUtils.class);
    private Connection conn = null;
    private HBaseAdmin admin = null;
    Configuration conf = null;
    @Autowired
    LoginUtil loginUtil;

    public HBaseMRSUtils() {
    }

    @PostConstruct
    public void initHbaseConnection() throws IOException {
        log.info("开始初始化MRS-Hbase~");

        this.initConf();
        String userdir = System.getProperty("user.dir") + File.separator + "conf" + File.separator;
        String userKeytabFile = userdir + "user.keytab";
        String krb5File = userdir + "krb5.conf";
        System.out.println("Hbase debug krbFile路径============" + krb5File);
        System.out.println("Hbase debug userKeyTableFile路径===========" + userKeytabFile);
        this.loginUtil.hhabseLogin("sjzhnyfwpt_kf_001", userKeytabFile, krb5File);
        if (this.conn == null) {
            try {
                this.conf.setBoolean("HBASE_ZK_SSL_ENABLED", false);
                this.handlZkSslEnabled(this.conf);
                this.conn = ConnectionFactory.createConnection(this.conf);
                this.admin = (HBaseAdmin) this.conn.getAdmin();
                log.info("MRS-hbase 初始化成功！");

            } catch (IOException var5) {
                var5.printStackTrace();
                throw new RuntimeException("创建Hbase数据库连接失败");
            }
        }

    }


    private synchronized void initConf() throws IOException {
        if (this.conf == null) {
            this.conf = HBaseConfiguration.create();
            String userDir = System.getProperty("user.dir") + File.separator + "conf" + File.separator;
            this.conf.addResource(new Path(userDir + "conf/core-site.xml"), false);
            this.conf.addResource(new Path(userDir + "hdfs-site.xml"), false);
            this.conf.addResource(new Path(userDir + "hbase-site.xml"), false);
            boolean isUseYarn = Boolean.parseBoolean(System.getProperty("useYarn", "false"));
            if (isUseYarn) {
                this.conf.addResource(new Path(userDir + "conf/yarn-site.xml"), false);
            }
        }

    }

    private void handlZkSslEnabled(Configuration conf) {
        boolean zkSslEnabled = conf.getBoolean("HBASE_ZK_SSL_ENABLED", true);
        if (zkSslEnabled) {
            System.setProperty("zookeeper.clientCnxnSocket", "org.apache.zookeeper.ClientCnxnSocketNetty");
            System.setProperty("zookeeper.client.secure", "true");
        } else {
            if (System.getProperty("zookeeper.clientCnxnSocket") != null) {
                System.clearProperty("zookeeper.clientCnxnSocket");
            }

            if (System.getProperty("zookeeper.client.secure") != null) {
                System.clearProperty("zookeeper.client.secure");
            }
        }

        System.err.println("zkSslEnabled:" + zkSslEnabled);
    }

    public void createTable(String tableName, String... columnFamilies) throws IOException {
        TableName name = TableName.valueOf(tableName);
        if (!this.admin.isTableAvailable(name)) {
            TableDescriptorBuilder tableDescriptorBuilder = TableDescriptorBuilder.newBuilder(name);
            List<ColumnFamilyDescriptor> columnFamilyDescriptorList = new ArrayList();
            String[] var6 = columnFamilies;
            int var7 = columnFamilies.length;

            for (int var8 = 0; var8 < var7; ++var8) {
                String columnFamily = var6[var8];
                ColumnFamilyDescriptorBuilder columnFamilyDescriptorBuilder = ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes(columnFamily));
                ColumnFamilyDescriptor columnFamilyDescriptor = columnFamilyDescriptorBuilder.build();
                columnFamilyDescriptorList.add(columnFamilyDescriptor);
            }

            tableDescriptorBuilder.setColumnFamilies(columnFamilyDescriptorList);
            TableDescriptor tableDescriptor = tableDescriptorBuilder.build();
            this.admin.createTable(tableDescriptor);
        }

    }

    public void disableTable(String tableName) throws IOException {
        TableName name = TableName.valueOf(tableName);
        if (!this.admin.isTableDisabled(name)) {
            this.admin.disableTable(name);
        }

    }

    public void truncate(String tableName) throws IOException {
        TableName name = TableName.valueOf(tableName);
        this.disableTable(tableName);
        this.admin.truncateTable(name, true);
    }

    public void deleteTable(String tableName) throws IOException {
        TableName name = TableName.valueOf(tableName);
        if (this.admin.isTableDisabled(name)) {
            this.admin.deleteTable(name);
        }

    }

    public List<TableDescriptor> listTables() throws IOException {
        List<TableDescriptor> tableDescriptors = this.admin.listTableDescriptors();
        return tableDescriptors;
    }

    public String getRowKey(String prefixString, String suffixString, Object... options) {
        if (prefixString.length() <= 40 && suffixString.length() <= 40) {
            StringBuilder preStringBuilder = new StringBuilder();
            preStringBuilder.append(prefixString);

            for (int i = 0; i < 40 - preStringBuilder.length(); ++i) {
                preStringBuilder.append("0");
            }

            StringBuilder sufStringBuilder = new StringBuilder();
            sufStringBuilder.append(suffixString);

            for (int i = 0; i < 40 - sufStringBuilder.length(); ++i) {
                sufStringBuilder.append("0");
            }

            StringBuilder optBuilder = new StringBuilder();
            Object[] var7 = options;
            int i = options.length;

            for (int var9 = 0; var9 < i; ++var9) {
                Object option = var7[var9];
                optBuilder.append(option);
            }

            if (optBuilder.length() > 100) {
                return null;
            } else {
                StringBuilder optStringBuilder = new StringBuilder();

                for (i = 0; i < 100 - optBuilder.length(); ++i) {
                    optStringBuilder.append("0");
                }

                optStringBuilder.append(optBuilder);
                return preStringBuilder.append("|").append(optStringBuilder).append("|").append(sufStringBuilder).toString();
            }
        } else {
            return null;
        }
    }

    public void insertOne(String tableName, String rowKey, String columnFamily, String column, String value) throws IOException {
        Put put = new Put(Bytes.toBytes(rowKey));
        put.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(column), Bytes.toBytes(value));
        Table table = this.conn.getTable(TableName.valueOf(tableName));
        table.put(put);
    }

    public void insertAll(String tableName, String columnFamily, List<Map<String, String>> mapList) throws IOException {
        Table table = this.conn.getTable(TableName.valueOf(tableName));
        List<Put> puts = new ArrayList();
        Iterator var6 = mapList.iterator();

        while (var6.hasNext()) {
            Map<String, String> map = (Map) var6.next();
            Put put = new Put(Bytes.toBytes((String) map.get("rowKey")));
            Iterator var9 = map.entrySet().iterator();

            while (var9.hasNext()) {
                Entry<String, String> entry = (Entry) var9.next();
                if (!"rowKey".equals(entry.getKey())) {
                    put.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes((String) entry.getKey()), Bytes.toBytes((String) entry.getValue()));
                }
            }

            puts.add(put);
        }

        table.put(puts);
    }

    public void insertOne(String tableName, String rowKey, String columnFamily, Long timeStamp, Map<String, Double> map, String isDouble) throws IOException {
        Table table = this.conn.getTable(TableName.valueOf(tableName));
        Put put = new Put(Bytes.toBytes(rowKey));
        Iterator var9 = map.entrySet().iterator();

        while (var9.hasNext()) {
            Entry<String, Double> entry = (Entry) var9.next();
            put.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes((String) entry.getKey()), timeStamp, Bytes.toBytes((Double) entry.getValue()));
        }

        table.put(put);
    }

    public void insertOne(String tableName, String rowKey, String columnFamily, Long timeStamp, Map<String, Object> map) throws IOException {
        Table table = this.conn.getTable(TableName.valueOf(tableName));
        Put put = new Put(Bytes.toBytes(rowKey));
        Iterator var8 = map.entrySet().iterator();

        while (var8.hasNext()) {
            Entry<String, Object> entry = (Entry) var8.next();
            put.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes((String) entry.getKey()), timeStamp, Bytes.toBytes(String.valueOf(entry.getValue())));
        }

        table.put(put);
    }

    public void update(String tableName, String rowKey, String columnFamily, String column, String value) throws IOException {
        Table table = this.conn.getTable(TableName.valueOf(tableName));
        Put put = new Put(Bytes.toBytes(rowKey));
        put.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(column), Bytes.toBytes(value));
        table.put(put);
    }

    public void delete(String tableName, String rowKey, String columnFamily, String column) throws IOException {
        Table table = this.conn.getTable(TableName.valueOf(tableName));
        Delete delete = new Delete(Bytes.toBytes(rowKey));
        delete.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(column));
        table.delete(delete);
    }

    public void delete(String tableName, String rowKey, String columnFamily, String... columnList) throws IOException {
        Table table = this.conn.getTable(TableName.valueOf(tableName));
        Delete delete = new Delete(Bytes.toBytes(rowKey));
        String[] var7 = columnList;
        int var8 = columnList.length;

        for (int var9 = 0; var9 < var8; ++var9) {
            String column = var7[var9];
            delete.addColumns(Bytes.toBytes(columnFamily), Bytes.toBytes(column));
        }

        table.delete(delete);
    }

    public void delete(String tableName, String rowKey, String columnFamily) throws IOException {
        Table table = this.conn.getTable(TableName.valueOf(tableName));
        Delete delete = new Delete(Bytes.toBytes(rowKey));
        delete.addFamily(Bytes.toBytes(columnFamily));
        table.delete(delete);
    }

    public void delete(String tableName, String rowKey) throws IOException {
        Table table = this.conn.getTable(TableName.valueOf(tableName));
        Delete delete = new Delete(Bytes.toBytes(rowKey));
        table.delete(delete);
    }

    public void delete(String tableName, String... rowKeyList) throws IOException {
        Table table = this.conn.getTable(TableName.valueOf(tableName));
        ArrayList<Delete> deleteList = new ArrayList();
        String[] var5 = rowKeyList;
        int var6 = rowKeyList.length;

        for (int var7 = 0; var7 < var6; ++var7) {
            String rowKey = var5[var7];
            Delete delete = new Delete(Bytes.toBytes(rowKey));
            deleteList.add(delete);
        }

        table.delete(deleteList);
    }


    public List<Map<String, Object>> queryTableBatch(String tableName, List<String> rowKeys, String columnFamily, String dateStr1) {
        Table table = null;
        try {
            table = this.conn.getTable(TableName.valueOf(tableName));
            if (rowKeys.isEmpty()) {
                return Collections.emptyList();
            }
            List<Get> gets = new ArrayList<>();
            for (String rowKey : rowKeys) {
                Get get = new Get(Bytes.toBytes(rowKey));
                get.addFamily(Bytes.toBytes(columnFamily));
                gets.add(get);
            }
            Result[] results = table.get(gets);
            List<Map<String, Object>> mapList = new ArrayList<>();
            for (Result result : results) {
                Map<String, Object> resultMap = new HashMap<>();
                String rowKey = Bytes.toString(result.getRow());
                resultMap.put("rowKey", rowKey);
                if (StringUtils.isNotBlank(rowKey)) {
                    String[] cons = rowKey.split("_");
                    if (cons.length > 0) {
                        resultMap.put("consNo", cons[0]);
                    }
                }

                resultMap.put("dataDate", dateStr1);
                if (!result.isEmpty()) {
                    for (Cell cell : result.listCells()) {
                        String key = String.valueOf(Bytes.toLong(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength()));
                        if (key.indexOf("p") == -1) {
                            key = "p" + key;
                        }
                        try {
                            resultMap.put(key, Bytes.toDouble(CellUtil.cloneValue(cell)));
                        } catch (Exception var9) {
                            try {
                                resultMap.put(key, Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength()));
                            } catch (Exception var8) {
                                continue;
                            }
                        }
                    }
                    mapList.add(resultMap);

                }
            }
            log.info("根据rowKey批量查询,表名:{},列簇:{},rowKeys：{},查询结果：{}", tableName, columnFamily, rowKeys.size(), mapList.size());
            return mapList;
        } catch (IOException e) {
            e.printStackTrace();
            log.info("根据rowKey批量查询失败,表名:{},列簇:{},rowKeys：{}", tableName, columnFamily, rowKeys.size(), e);
        }
        return Collections.emptyList();
    }


    public List<Map<String, Object>> queryTableBatchNew(String tableName, List<String> rowKeys, String columnFamily, String dateStr1) {
        Table table = null;
        try {
            table = this.conn.getTable(TableName.valueOf(tableName));
            if (rowKeys.isEmpty()) {
                return Collections.emptyList();
            }
            List<Get> gets = new ArrayList<>();
            for (String rowKey : rowKeys) {
                Get get = new Get(Bytes.toBytes(rowKey));
                get.addFamily(Bytes.toBytes(columnFamily));
                gets.add(get);
            }
            Result[] results = table.get(gets);
            List<Map<String, Object>> mapList = new ArrayList<>();
            for (Result result : results) {
                Map<String, Object> resultMap = new HashMap<>();
                String rowKey = Bytes.toString(result.getRow());
                resultMap.put("rowKey", rowKey);
                resultMap.put("dataDate", dateStr1);
                if (!result.isEmpty()) {

                    for (Cell cell : result.listCells()) {
                        String key = Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength());

                        try {
                            resultMap.put(key, Bytes.toDouble(CellUtil.cloneValue(cell)));
                        } catch (Exception var9) {
                            try {
                                resultMap.put(key, Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength()));
                            } catch (Exception var8) {
                                System.out.println("rowKey:" + var8.getMessage());
                                continue;
                            }
                        }
                    }
                    mapList.add(resultMap);

                }
            }
            log.info("根据rowKey批量查询,表名:{},列簇:{},rowKeys：{},查询结果：{}", tableName, columnFamily, rowKeys.size(), mapList.size());
            return mapList;
        } catch (IOException e) {
            e.printStackTrace();
            log.info("根据rowKey批量查询失败,表名:{},列簇:{},rowKeys：{}", tableName, columnFamily, rowKeys.size(), e);
        }
        return Collections.emptyList();
    }


    public List<Map<String, Object>> queryTableBatchCurveMp(String tableName, List<String> rowKeys, String columnFamily, String dateStr1) {
        Table table = null;
        try {
            table = this.conn.getTable(TableName.valueOf(tableName));
            if (rowKeys.isEmpty()) {
                return Collections.emptyList();
            }
            List<Get> gets = new ArrayList<>();
            for (String rowKey : rowKeys) {
                Get get = new Get(Bytes.toBytes(rowKey));
                get.addFamily(Bytes.toBytes(columnFamily));
                gets.add(get);
                log.info("根据rowKey批量查询,表名:{},列簇:{},rowKeys：{},查询结果：{}", tableName, columnFamily, rowKeys.size(), rowKey);
            }

            Result[] results = table.get(gets);
            List<Map<String, Object>> mapList = new ArrayList<>();
            for (Result result : results) {
                Map<String, Object> resultMap = new HashMap<>();
                String rowKey = Bytes.toString(result.getRow());
                resultMap.put("rowKey", rowKey);
                resultMap.put("dataDate", dateStr1);
                if (!result.isEmpty()) {
                    for (Cell cell : result.listCells()) {
                        String key = Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength());
                        try {
                            resultMap.put(key, Bytes.toDouble(CellUtil.cloneValue(cell)));
                        } catch (Exception var9) {
                            try {
                                resultMap.put(key, Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength()));
                            } catch (Exception var8) {
                                continue;
                            }
                        }
                    }
                    mapList.add(resultMap);
                }
            }
            log.info("根据rowKey批量查询,表名:{},列簇:{},rowKeys：{},查询结果：{}", tableName, columnFamily, rowKeys.size(), mapList.size());
            return mapList;
        } catch (IOException e) {
            e.printStackTrace();
            log.info("根据rowKey批量查询失败,表名:{},列簇:{},rowKeys：{}", tableName, columnFamily, rowKeys.size(), e);
        }
        return Collections.emptyList();
    }


    public List<Map<String, Object>> qurryTableBatch(String tableName, List<String> rowkeyList, String dateStr1) throws IOException {
        List<Get> getList = new ArrayList();
        List<Map<String, Object>> resultList = new ArrayList<>();
        Map<String, Object> resultMap = new HashMap<>();
        Table table = this.conn.getTable(TableName.valueOf(tableName));
        for (String rowkey : rowkeyList) {
            Get get = new Get(Bytes.toBytes(rowkey));
            getList.add(get);
        }
        Result[] results = table.get(getList);//重点在这，直接查getList<Get>
        for (Result result : results) {
            if (result == null) {
                continue;
            }
            resultMap.put("rowKey", Bytes.toString(result.getRow()));
            resultMap.put("dataDate", dateStr1);
            for (Cell cell : result.rawCells()) {
                String key = String.valueOf(Bytes.toLong(CellUtil.cloneQualifier(cell)));
                log.info(key);
                if (key.indexOf("p") == -1) {
                    key = "p" + key;
                }
                try {
                    resultMap.put(key, Bytes.toDouble(CellUtil.cloneValue(cell))
                    );
                } catch (Exception var9) {
                    try {
                        resultMap.put(key, Bytes.toString(CellUtil.cloneValue(cell)));
                    } catch (Exception var8) {
                        continue;
                    }
                }
            }
            resultList.add(resultMap);

        }
        return resultList;
    }

    public Result select(String tableName, String rowKey) throws IOException {
        Table table = this.conn.getTable(TableName.valueOf(tableName));
        Get get = new Get(Bytes.toBytes(rowKey));
        Result result = table.get(get);
        return result;
    }

    public ResultScanner scan(String tableName) throws IOException {
        Table table = this.conn.getTable(TableName.valueOf(tableName));
        Scan scan = new Scan();
        ResultScanner scanner = table.getScanner(scan);
        return scanner;
    }

    public ResultScanner scan(String tableName, String columnFamily) throws IOException {
        Table table = this.conn.getTable(TableName.valueOf(tableName));
        Scan scan = new Scan();
        scan.addFamily(Bytes.toBytes(columnFamily));
        ResultScanner scanner = table.getScanner(scan);
        return scanner;
    }

    public ResultScanner scan(String tableName, String columnFamily, String column) throws IOException {
        Table table = this.conn.getTable(TableName.valueOf(tableName));
        Scan scan = new Scan();
        scan.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(column));
        ResultScanner scanner = table.getScanner(scan);
        return scanner;
    }

    public ResultScanner scan(String tableName, Filter filter) throws IOException {
        Table table = this.conn.getTable(TableName.valueOf(tableName));
        Scan scan = new Scan();
        scan.setFilter(filter);
        ResultScanner scanner = table.getScanner(scan);
        return scanner;
    }

    public ResultScanner scan(String tableName, Filter filter, String startRowKey) throws IOException {
        Table table = this.conn.getTable(TableName.valueOf(tableName));
        Scan scan = new Scan();
        scan.setFilter(filter);
        scan.withStartRow(Bytes.toBytes(startRowKey));
        ResultScanner scanner = table.getScanner(scan);
        return scanner;
    }

    public Filter pagetFilter(long size) {
        return new PageFilter(size);
    }

    public Filter singleColumnValueFilter(String columnFamily, String column, CompareOperator
            compareOperator, String value) {
        return new SingleColumnValueFilter(Bytes.toBytes(columnFamily), Bytes.toBytes(column), compareOperator, Bytes.toBytes(value));
    }

    public Filter rowFilter(CompareOperator compareOperator, ByteArrayComparable rowComparator) {
        return new RowFilter(compareOperator, rowComparator);
    }

    public Filter columnPrefixFilter(String prefix) {
        return new ColumnPrefixFilter(Bytes.toBytes(prefix));
    }

    public FilterList filterListPassAll(Filter... filterList) {
        FilterList list = new FilterList(Operator.MUST_PASS_ALL);
        Filter[] var3 = filterList;
        int var4 = filterList.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            Filter filter = var3[var5];
            list.addFilter(filter);
        }

        return list;
    }

    public FilterList filterListPassOne(Filter... filterList) {
        FilterList list = new FilterList(Operator.MUST_PASS_ONE);
        Filter[] var3 = filterList;
        int var4 = filterList.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            Filter filter = var3[var5];
            list.addFilter(filter);
        }

        return list;
    }

    public void close() {
        try {
            this.conn.close();
        } catch (IOException var5) {
            this.conn = null;
        } finally {
            this.conn = null;
        }

    }
}