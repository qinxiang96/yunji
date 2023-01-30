package com.flora.note;

import com.flora.note.dao.BaseDao;
import com.flora.note.dao.UserDao;
import com.flora.note.po.User;
import org.junit.Test;

import java.util.ArrayList;

/**
 * @Author qinxiang
 * @Date 2022/11/28-下午3:09
 */
public class TestUser {
    @Test
    public void testQueryUserByName() {
        UserDao userDao = new UserDao();
        User user = userDao.queryUserByName("zhangsan");
        System.out.println(user.getUpwd());
    }

    @Test
    public void testAdd() {
        String sql = "insert into tb_user(uname,upwd,nick,head,mood) values(?,?,?,?,?)";
        ArrayList<Object> params = new ArrayList<>();
        params.add("lisi");
        params.add("e10adc3949ba59abbe56e057f20f883e");
        params.add("lisi");
        params.add("404.jpg");
        params.add("hello");
        int row = BaseDao.executeUpdate(sql, params);
        System.out.println(row);


    }
}
