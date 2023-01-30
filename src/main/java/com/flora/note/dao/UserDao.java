package com.flora.note.dao;

import com.flora.note.po.User;
import com.flora.note.util.DBUtil;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author qinxiang
 * @Date 2022/11/28-下午12:23
 * 通过用户名查询用户对象，返回用户对象
 * 1、获取数据库连接
 * 2、定义SQL语句
 * 3、预编译
 * 4、设置参数
 * 5、执行查询，返回结果集
 * 6、判断并分析结果集
 * 7、关闭资源
 */
public class UserDao {
    /**
     * 通过用户查询用户对象
     * 1、定义SQL语句
     * 2、设置参数集合
     * 3、调用BaseDao的查询方法
     *
     * @param userName
     * @return
     */
    public User queryUserByName(String userName) {
        User user = null;
        String sql = "select * from tb_user where uname = ?";
        List<Object> params = new ArrayList<>();
        params.add(userName);
        user = (User) BaseDao.queryRow(sql, params, User.class);
        return user;
    }

    public User queryUserByName02(String userName) {
        User user = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DBUtil.getConnection();
            String sql = "select * from tb_user where uname = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userName);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = new User();
                user.setUserId(resultSet.getInt("userId"));
                user.setUname(userName);
                user.setHead(resultSet.getString("head"));
                user.setMood(resultSet.getString("mood"));
                user.setNick(resultSet.getString("nick"));
                user.setUpwd(resultSet.getString("upwd"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(resultSet, preparedStatement, connection);
        }
        return user;
    }

    /**
     * 通过昵称和用户ID查询用户对象
     *
     * @param nick
     * @param userId
     * @return
     */
    public User queryUserByNickAndUserId(String nick, Integer userId) {
        String sql = "select * from tb_user where nick = ? and userId != ?";
        ArrayList<Object> params = new ArrayList<>();
        params.add(nick);
        params.add(userId);
        User user = (User) BaseDao.queryRow(sql, params, User.class);
        return user;
    }

    /**
     * 通过用户ID修改用户信息
     * 1、定义SQL语句
     * String sql = "update tb_user set nick = ?, mood = ?, head = ? where userId = ?";
     * 2、设置参数集合
     * 3、调用BaseDao的更新方法，返回受影响的行数
     * 4、返回受影响的行数
     *
     * @param user
     * @return
     */
    public int updateUser(User user) {
        String sql = "update tb_user set nick = ?, mood = ?, head = ? where userId = ?";
        ArrayList<Object> params = new ArrayList<>();
        params.add(user.getNick());
        params.add(user.getMood());
        params.add(user.getHead());
        params.add(user.getUserId());
        int row = BaseDao.executeUpdate(sql, params);
        return row;
    }
}
