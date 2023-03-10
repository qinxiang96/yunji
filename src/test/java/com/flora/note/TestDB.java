package com.flora.note;

import com.flora.note.util.DBUtil;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import org.junit.Test;

/**
 * @Author qinxiang
 * @Date 2022/11/25-上午9:24
 */
public class TestDB {
    //使用日志工厂类，记录日志
    private Logger logger = LoggerFactory.getLogger(TestDB.class);

    /**
     * 单元测试方法
     * 1、方法的返回值，建议使用void,一般没有返回值
     * 2、参数列表，建议空参，一般是没有参数
     * 3、方法上需要设置@Test注解
     * 4、每个方法都能独立运行
     */
    @Test
    public void testDB() {
        System.out.println(DBUtil.getConnection());
        logger.debug("获取数据库连接：{}" + DBUtil.getConnection());
    }

}
