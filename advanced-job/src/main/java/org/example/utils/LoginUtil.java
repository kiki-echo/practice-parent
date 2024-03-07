package org.example.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.security.UserGroupInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

@Component
public class LoginUtil {
    private static final Logger LOG = LoggerFactory.getLogger(LoginUtil.class);
    @Value("${zookeeper.server.principal}")
    String zookeeperServerPrincipal;
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private static final boolean IS_IBM_JDK = System.getProperty("java.vendor").contains("IBM");


    private Configuration conf = null;

    public LoginUtil() {
    }

    public static void setJaasFile(String principal, String keytabPath) throws IOException {
        String jaasPath = new File(System.getProperty("user.dir")) + File.separator + "conf" + File.separator + System.getProperty("user.name") + ".jaas.conf";
        deleteJaasFile(jaasPath);
        writeJaasFile(jaasPath, principal, keytabPath);
        System.setProperty("java.security.auth.login.config", jaasPath);
    }

    public static void setZookeeperServerPrincipal(String zkServerPrincipal) throws IOException {
        System.setProperty("zookeeper.server.principal", zkServerPrincipal);
        String ret = System.getProperty("zookeeper.server.principal");
        if (ret == null) {
            throw new IOException("zookeeper.server.principal is null.");
        } else if (!ret.equals(zkServerPrincipal)) {
            throw new IOException("zookeeper.server.principal is " + ret + " is not " + zkServerPrincipal + ".");
        }
    }

    public static void setKrb5Config(String krb5ConfFile) throws IOException {
        System.setProperty("java.security.krb5.conf", krb5ConfFile);
        String ret = System.getProperty("java.security.krb5.conf");
        if (ret == null) {
            throw new IOException("java.security.krb5.conf is null.");
        } else if (!ret.equals(krb5ConfFile)) {
            throw new IOException("java.security.krb5.conf is " + ret + " is not " + krb5ConfFile + ".");
        }
    }

    private static void writeJaasFile(String jaasPath, String principal, String keytabPath) throws IOException {
        FileWriter writer = new FileWriter(jaasPath);

        try {
            writer.write(getJaasConfContext(principal, keytabPath));
            writer.flush();
        } catch (IOException var8) {
            throw new IOException("Failed to create jaas.conf File");
        } finally {
            writer.close();
        }

    }

    private static void checkContext(String loginContextName, String principal, String keytabFile) throws IOException {
        if (loginContextName != null && loginContextName.length() > 0) {
            if (principal != null && principal.length() > 0) {
                if (keytabFile == null || keytabFile.length() <= 0) {
                    LOG.error("input keytabFile is invalid.");
                    throw new IOException("input keytabFile is invalid.");
                }
            } else {
                LOG.error("input principal is invalid.");
                throw new IOException("input principal is invalid.");
            }
        } else {
            LOG.error("input loginContextName is invalid.");
            throw new IOException("input loginContextName is invalid.");
        }
    }

    public synchronized void login(String userPrincipal, String userKeytabPath, String krb5ConfPath, Configuration conf) throws IOException {
        if (userPrincipal != null && userPrincipal.length() > 0) {
            if (userKeytabPath != null && userKeytabPath.length() > 0) {
                if (krb5ConfPath != null && krb5ConfPath.length() > 0) {
                    if (conf == null) {
                        LOG.error("input conf is invalid.");
                        throw new IOException("input conf is invalid.");
                    } else {
                        File userKeytabFile = new File(userKeytabPath);
                        if (!userKeytabFile.exists()) {
                            LOG.error("userKeytabFile(" + userKeytabFile.getCanonicalPath() + ") does not exsit.");
                            throw new IOException("userKeytabFile(" + userKeytabFile.getCanonicalPath() + ") does not exsit.");
                        } else if (!userKeytabFile.isFile()) {
                            LOG.error("userKeytabFile(" + userKeytabFile.getCanonicalPath() + ") is not a file.");
                            throw new IOException("userKeytabFile(" + userKeytabFile.getCanonicalPath() + ") is not a file.");
                        } else {
                            File krb5ConfFile = new File(krb5ConfPath);
                            if (!krb5ConfFile.exists()) {
                                LOG.error("krb5ConfFile(" + krb5ConfFile.getCanonicalPath() + ") does not exsit.");
                                throw new IOException("krb5ConfFile(" + krb5ConfFile.getCanonicalPath() + ") does not exsit.");
                            } else if (!krb5ConfFile.isFile()) {
                                LOG.error("krb5ConfFile(" + krb5ConfFile.getCanonicalPath() + ") is not a file.");
                                throw new IOException("krb5ConfFile(" + krb5ConfFile.getCanonicalPath() + ") is not a file.");
                            } else {
                                setKrb5Config(krb5ConfFile.getCanonicalPath());
                                setConfiguration(conf);
                                loginHadoop(userPrincipal, userKeytabFile.getCanonicalPath());
                                LOG.info("Login success!!!!!!!!!!!!!!");
                            }
                        }
                    }
                } else {
                    LOG.error("input krb5ConfPath is invalid.");
                    throw new IOException("input krb5ConfPath is invalid.");
                }
            } else {
                LOG.error("input userKeytabPath is invalid.");
                throw new IOException("input userKeytabPath is invalid.");
            }
        } else {
            LOG.error("input userPrincipal is invalid.");
            throw new IOException("input userPrincipal is invalid.");
        }
    }

    private synchronized void initHBaseConfiguration() throws IOException {
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

            LOG.info("配置文件设置成功~");
        }

    }

    public void hhabseLogin(String userName, String userKeytabFile, String krb5File) throws IOException {
        this.initHBaseConfiguration();
        setJaasFile(userName, userKeytabFile);
        this.login(userName, userKeytabFile, krb5File, this.conf);
    }

    private static void loginHadoop(String principal, String keytabFile) throws IOException {
        try {
            UserGroupInformation.loginUserFromKeytab(principal, keytabFile);
        } catch (IOException var3) {
            LOG.error("login failed with " + principal + " and " + keytabFile + ".");
            LOG.error("perhaps cause 1 is (wrong password) keytab file and user not match, you can kinit -k -t keytab user in client server to check.");
            LOG.error("perhaps cause 2 is (clock skew) time of local server and remote server not match, please check ntp to remote server.");
            LOG.error("perhaps cause 3 is (aes256 not support) aes256 not support by default jdk/jre, need copy local_policy.jar and US_export_policy.jar from remote server in path /opt/huawei/Bigdata/jdk/jre/lib/security.");
            LOG.error("perhaps cause 4 is (no rule) principal format not support by default, need add property hadoop.security.auth_to_local(in core-site.xml) value RULE:[1:$1] RULE:[2:$1].");
            LOG.error("perhaps cause 5 is (time out) can not connect to kdc server or there is fire wall in the network.");
            throw var3;
        }
    }

    private static void setConfiguration(Configuration conf) throws IOException {
        UserGroupInformation.setConfiguration(conf);
    }

    private static void deleteJaasFile(String jaasPath) throws IOException {
        File jaasFile = new File(jaasPath);
        if (jaasFile.exists() && !jaasFile.delete()) {
            throw new IOException("Failed to delete exists jaas file.");
        }
    }

    private static String getJaasConfContext(String principal, String keytabPath) {
        Module[] allModule = Module.values();
        StringBuilder builder = new StringBuilder();
        Module[] var4 = allModule;
        int var5 = allModule.length;

        for (int var6 = 0; var6 < var5; ++var6) {
            Module modlue = var4[var6];
            builder.append(getModuleContext(principal, keytabPath, modlue));
        }

        return builder.toString();
    }

    private static String getModuleContext(String userPrincipal, String keyTabPath, Module module) {
        StringBuilder builder = new StringBuilder();
        if (IS_IBM_JDK) {
            builder.append(module.getName()).append(" {").append(LINE_SEPARATOR);
            builder.append("com.ibm.security.auth.module.Krb5LoginModule  required").append(LINE_SEPARATOR);
            builder.append("credsType=both").append(LINE_SEPARATOR);
            builder.append("principal=\"" + userPrincipal + "\"").append(LINE_SEPARATOR);
            builder.append("useKeytab=\"" + keyTabPath + "\"").append(LINE_SEPARATOR);
            builder.append("debug=true;").append(LINE_SEPARATOR);
            builder.append("};").append(LINE_SEPARATOR);
        } else {
            builder.append(module.getName()).append(" {").append(LINE_SEPARATOR);
            builder.append("com.sun.security.auth.module.Krb5LoginModule  required").append(LINE_SEPARATOR);
            builder.append("useKeyTab=true").append(LINE_SEPARATOR);
            builder.append("keyTab=\"" + keyTabPath + "\"").append(LINE_SEPARATOR);
            builder.append("principal=\"" + userPrincipal + "\"").append(LINE_SEPARATOR);
            builder.append("useTicketCache=false").append(LINE_SEPARATOR);
            builder.append("storeKey=true").append(LINE_SEPARATOR);
            builder.append("debug=true;").append(LINE_SEPARATOR);
            builder.append("};").append(LINE_SEPARATOR);
        }

        return builder.toString();
    }

    public static void securityPrepare(String principal, String keyTabFile, String zookeeperServerPrincipal) throws IOException, URISyntaxException {
        String userdir = System.getProperty("user.dir") + File.separator + "conf" + File.separator;
        String userKeytabFile = userdir + keyTabFile;
        String krb5File = userdir + "krb5.conf";
        setKrb5Config(krb5File);
        setZookeeperServerPrincipal(zookeeperServerPrincipal);
        setJaasFile(principal, userKeytabFile);
    }

    public static Boolean isSecurityModel() {
        Boolean isSecurity = false;
        String krbFilePath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "conf/kafkaSecurityMode";
        Properties securityProps = new Properties();
        if (!isFileExists(krbFilePath)) {
            return isSecurity;
        } else {
            try {
                securityProps.load(new FileInputStream(krbFilePath));
                if ("yes".equalsIgnoreCase(securityProps.getProperty("kafka.client.security.mode"))) {
                    isSecurity = true;
                }
            } catch (Exception var4) {
                LOG.info("The Exception occured : {}.", var4);
            }

            return isSecurity;
        }
    }

    private static boolean isFileExists(String fileName) {
        File file = new File(fileName);
        return file.exists();
    }

    public static enum Module {
        STORM("StormClient"),
        KAFKA("KafkaClient"),
        ZOOKEEPER("Client");

        private String name;

        private Module(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
}
