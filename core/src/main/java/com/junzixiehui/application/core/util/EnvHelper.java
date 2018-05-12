package com.junzixiehui.application.core.util;

import org.apache.commons.lang3.StringUtils;

/**
 * <p>Description: 读取环境</p>
 *
 * @author: by qulibin
 * @date: 2017/12/13  12:01
 * @version: 1.0
 */
public class EnvHelper {

    private static String active;

    public static final String ENV_DEV = "development";
    public static final String ENV_TEST = "testing";
    public static final String ENV_PRO = "production";
    private static final String[] evnArray = {ENV_DEV, ENV_TEST, ENV_PRO};

    private static String env;

    public EnvHelper() {
    }

    public static boolean isDev() {
        return EnvHelper.getEnv().equals(ENV_DEV);
    }

    public static boolean isTest() {
        return EnvHelper.getEnv().equals(ENV_TEST);
    }

    public static boolean isPro() {
        return EnvHelper.getEnv().equals(ENV_PRO);
    }

    /**
     * 获取配置的当前环境变量
     *
     * @return
     */
    public static String getEnv() {
        if (StringUtils.isNoneBlank(env)) {
            return env;
        }
        env = active;
        if (StringUtils.isBlank(env)) {
            env = System.getProperty("environment");
            if (StringUtils.isBlank(env)) {
                env = System.getenv("environment");
            }
            if (StringUtils.isNotBlank(env)){
                return env;
            }
            throw new RuntimeException("environment is null");
        }

        for (String envAllowed : evnArray) {
            if (envAllowed.equalsIgnoreCase(env)) {
                return env;
            }
        }

        throw new RuntimeException("Set the environment to one in (development,testing,production)");
    }


    public static void setActive(String active){
        EnvHelper.active = active;
    }

}
