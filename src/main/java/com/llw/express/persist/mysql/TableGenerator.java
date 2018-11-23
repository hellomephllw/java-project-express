package com.llw.express.persist.mysql;

import com.llw.util.FileUtil;
import com.llw.util.LoggerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;

/**
 * @description: 表生成器
 * @author: llw
 * @date: 2018-11-22
 */
public class TableGenerator {

    /**logger*/
    private static Logger logger = LoggerFactory.getLogger(TableGenerator.class);

    /**模式*/
    private static String _MODE;

    /**用户配置的基础包路径*/
    private static String _USER_CONFIG_BASE_PACKAGE_PATH;
    /**源码路径, 对根路径的补充*/
    private final static String _BASE_SOURCE_CODE_PATH = "/src/main/java/";

    /**检查模式*/
    private final static String _MODE_CHECK = "check";
    /**增量模式: 只执行增加的操作*/
    private final static String _MODE_INCREMENT = "increment";
    /**强制执行模式: 会执行删除和更新操作*/
    private final static String _MODE_FORCE = "force";

    /**
     * 生成表格
     * @param env 环境变量
     * @throws Exception
     */
    public static void generate(String env) throws Exception {
        //连接数据库
        DatabaseHelper.connectDatabase(env);
        //读取所有实体
        EntityReader.readAllEntities(getBasePackagePath());
        //读取所有表格
//        TableReader.readAllTables();
    }

    /**
     * 获取用户提供的包的基本路径
     * @return 包路径
     * @throws Exception
     */
    public static String getBasePackagePath() throws Exception {
        if (getUserConfigBasePackageFilePath() == null) throw new Exception("用户没有配置包路径");

        return FileUtil.getLocalRootAbsolutePath() + _BASE_SOURCE_CODE_PATH + getUserConfigBasePackageFilePath();
    }

    /**
     * 获取用户配置包的文件路径
     * @return 用户配置包的文件路径
     * @throws Exception
     */
    public static String getUserConfigBasePackageFilePath() throws Exception {
        return _USER_CONFIG_BASE_PACKAGE_PATH;
    }

    /**
     * 获取用户项目构建的class路径
     * @return class路径
     * @throws Exception
     */
    public static String getBuildClassPath() throws Exception {
        return FileUtil.getLocalRootAbsolutePath() + "/build/classes/java/main";
    }

    /**
     * 初始化模式
     * @param mode 模式
     * @throws Exception
     */
    private static void initMode(String mode) throws Exception {
        _MODE = mode;
    }

    /**
     * 初始化用户配置的基础包路径
     * @param userConfigBasePackagePath 用户配置的基础包路径
     * @throws Exception
     */
    private static void initUserConfigBasePackagePath(String userConfigBasePackagePath) throws Exception {
        _USER_CONFIG_BASE_PACKAGE_PATH = userConfigBasePackagePath.replaceAll("\\.", "/");
    }

    public static void main(String[] args) {
        try {
            //初始化模式
            initMode(args[1]);
            //初始化用户配置的基础包路径
            initUserConfigBasePackagePath(args[2]);
            //生成表格
            generate(args[0]);
        } catch (Exception e) {
            LoggerUtil.printStackTrace(logger, e);
        } finally {
            try {
                Connection connection = DatabaseHelper.getConn();
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                LoggerUtil.printStackTrace(logger, e);
            }
        }
    }

}
