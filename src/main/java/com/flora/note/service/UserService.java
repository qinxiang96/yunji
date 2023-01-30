package com.flora.note.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.flora.note.dao.UserDao;
import com.flora.note.po.User;
import com.flora.note.vo.ResultInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

/**
 * @Author qinxiang
 * @Date 2022/11/28-下午12:24
 */
public class UserService {
    private UserDao userDao = new UserDao();

    public ResultInfo<User> userLogin(String userName, String userPwd) {
        ResultInfo<User> resultInfo = new ResultInfo<>();
        //数据回显：当登录实现时，将登录信息返回给页面显示
        User u = new User();
        u.setUname(userName);
        u.setUpwd(userPwd);
        //设置到resultInfo对象中
        resultInfo.setResult(u);
        //判断参数是否为空
        if (StrUtil.isBlank(userName) || StrUtil.isBlank(userPwd)) {
            //if(userName == null || userPwd == null){
            resultInfo.setCode(0);
            resultInfo.setMsg("用户姓名或密码不能为空");
            return resultInfo;
        }
        //如果不为空，通过用户名查询用户对象
        User user = userDao.queryUserByName(userName);
        //判断用户对象是否为空
        if (user == null) {
            resultInfo.setCode(0);
            resultInfo.setMsg("该用户不存在！");
            return resultInfo;
        }
        //判断密码是否正确
        //将前台传递的密码按照MD5算法的方式加密
        userPwd = DigestUtil.md5Hex(userPwd);
        if (userPwd.equals(user.getUpwd())) {
            resultInfo.setCode(1);
            resultInfo.setResult(user);
            return resultInfo;
        } else {
            resultInfo.setCode(0);
            resultInfo.setMsg("用户密码不正确");
            return resultInfo;
        }
    }

    /**
     * 验证昵称的唯一性
     * 1、判断昵称是否为空
     * 如果为空，返回0
     * 2、调用Dao层，通过用户ID和昵称查询用户对象
     * 3、判断用户对象存在
     * 存在 返回0
     * 不存在，返回1
     *
     * @param nick
     * @param userId
     * @return
     */
    public Integer checkNick(String nick, Integer userId) {
        if (StrUtil.isBlank(nick)) {
            return 0;
        }
        User user = userDao.queryUserByNickAndUserId(nick, userId);
        if (user != null) {
            return 0;
        }
        return 1;
    }

    /**
     * 修改用户信息
     * 1、获取参数（昵称，心情）
     * 2、参数的非空校验（判断必填参数非空）
     * 如果昵称为空，将状态码和错误信息设置resultInfo对象中，返回resultInfo对象
     * 3、从session作用域中获取用户对象（获取用户对象中默认的头像）
     * 4、实现上传文件
     * 获取part对象 request.getPart("name"); name代表的是file文件域的name属性值
     * 通过part对象获取上传文件的文件名
     * 判断文件名是否为空
     * 获取文件存放的路径 WEB-INF/upload/目录中
     * 上传文件到指定目录
     * 5、更新用户头像（将原本用户对象中的默认的头像设置为上传的文件名）
     * 6、调用Dao层的更新方法，返回受影响的行数
     * 7、判断受影响的行数
     * 如果大于0， 则修改成功；否则修改失败
     * 8、返回resultInfo对象
     *
     * @param request
     * @return
     */
    public ResultInfo<User> updateUser(HttpServletRequest request) {
        ResultInfo<User> resultInfo = new ResultInfo<>();
        String nick = request.getParameter("nick");
        String mood = request.getParameter("mood");
        if (StrUtil.isBlank(nick)) {
            resultInfo.setCode(0);
            resultInfo.setMsg("用户昵称不能为空！");
            return resultInfo;
        }
        User user = (User) request.getSession().getAttribute("user");
        //更新信息
        user.setNick(nick);
        user.setMood(mood);
        try {
            Part part = request.getPart("img");
            String header = part.getHeader("Content-Disposition");
            String str = header.substring(header.lastIndexOf("=") + 2);
            String fileName = str.substring(0, str.length() - 1);
            if (!StrUtil.isBlank(fileName)) {
                user.setHead(fileName);
                String filePath = request.getServletContext().getRealPath("WEB-INF/upload/");
                part.write(filePath + "/" + fileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        int row = userDao.updateUser(user);
        if (row > 0) {
            resultInfo.setCode(1);
            //更新session中用户对象
            request.getSession().setAttribute("user", user);
        } else {
            resultInfo.setCode(0);
            resultInfo.setMsg("更新失败！");
        }

        return resultInfo;
    }
}
