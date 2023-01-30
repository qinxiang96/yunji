package com.flora.note.web;

import com.flora.note.po.User;
import com.flora.note.service.UserService;
import com.flora.note.vo.ResultInfo;
import org.apache.commons.io.FileUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * @Author qinxiang
 * @Date 2022/11/28-下午2:14
 */
@WebServlet("/user")
@MultipartConfig
public class UserServlet extends HttpServlet {
    private UserService userService = new UserService();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置首页导航高亮
        request.setAttribute("menu_page", "user");
        //接收用户行为
        String actionName = request.getParameter("actionName");
        //判断用户行为，调用对应的方法
        if ("login".equals(actionName)) {
            //用户登录
            userLogin(request, response);
        } else if ("logout".equals(actionName)) {
            //用户退出
            userLogOut(request, response);
        } else if ("userCenter".equals(actionName)) {
            //进入个人中心
            userCenter(request, response);
        } else if ("userHead".equals(actionName)) {
            //加载头像
            userhead(request, response);
        } else if ("checkNick".equals(actionName)) {
            //验证昵称的唯一性
            checkNick(request, response);
        } else if ("updateUser".equals(actionName)) {
            //修改用户信息
            updateUser(request, response);
        }
    }

    /**
     * 修改用户信息
     * 文件上传必须在Servlet类上加注解@MutipartConfig
     * 1、调用Service层的方法，传递request对象作为参数，返回resultInfo对象
     * 2、将resultInfo对象存到request作用域中
     * 3、请求转发跳转到个人中心页面 user?actionName=userCenter
     *
     * @param request
     * @param response
     */
    private void updateUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResultInfo<User> resultInfo = userService.updateUser(request);
        request.setAttribute("resultInfo", resultInfo);
        request.getRequestDispatcher("user?actionName=userCenter").forward(request, response);
    }

    /**
     * 验证昵称的唯一性
     * 1、获取参数（昵称）
     * 2、从session作用域获取用户对象，得到用户ID
     * 3、调用Service层的方法，得到返回对的结果
     * 4、通过字符输出流将结果响应给前台的ajax的回调函数
     * 5、关闭资源
     *
     * @param request
     * @param response
     */
    private void checkNick(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String nick = request.getParameter("nick");
        User user = (User) request.getSession().getAttribute("user");
        Integer code = userService.checkNick(nick, user.getUserId());
        response.getWriter().write(code + "");
        response.getWriter().close();
    }

    /**
     * 加载头像
     * 1、获取参数（图片名称）
     * 2、得到图片的存放路径 request.getServletContext().getRealPath("/")
     * 3、通过图片的完整路径，得到file对象
     * 4、通过截取，得到图片的后缀
     * 5、通过不同的图片后缀，设置不同的响应的类型
     * 6、利用FileUtils的copyFile()方法，将图片拷贝给浏览器
     *
     * @param request
     * @param response
     */
    private void userhead(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String head = request.getParameter("imageName");
        String realPath = request.getServletContext().getRealPath("/webapp/WEB-INF/upload/");
        File file = new File(realPath + "/" + head);
        String pic = head.substring(head.lastIndexOf(".") + 1);
        if ("png".equalsIgnoreCase(pic)) {
            response.setContentType("image/png");
        } else if ("jpg".equalsIgnoreCase(pic) || "jpeg".equalsIgnoreCase(pic)) {
            response.setContentType("image/jpg");
        } else if ("gif".equalsIgnoreCase(pic)) {
            response.setContentType("image/gif");
        }
        FileUtils.copyFile(file, response.getOutputStream());
    }

    /**
     * 进入个人中心
     * 1、设置首页动态包含的页面值
     * 2、请求转发跳转到index
     *
     * @param request
     * @param response
     */
    private void userCenter(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("changePage", "user/info.jsp");
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    /**
     * 用户退出
     * 1、销毁Session对象
     * 2、删除Cookie对象
     * 3、重定向跳转到登录页面
     *
     * @param request
     * @param response
     */
    private void userLogOut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().invalidate();
        Cookie cookie = new Cookie("user", null);
        cookie.setMaxAge(0);//设置0表示删除cookie
        response.addCookie(cookie);
        response.sendRedirect("login.jsp");
    }

    private void userLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取参数
        String userName = request.getParameter("userName");
        String userPwd = request.getParameter("userPwd");
        //调用service层的方法，返回resultInfo对象
        ResultInfo<User> resultInfo = userService.userLogin(userName, userPwd);
        //判断是否登录成功
        if (resultInfo.getCode() == 1) {
            //将用户信息设置到session作用域中
            request.getSession().setAttribute("user", resultInfo.getResult());
            //判断用户是否选择记住密码（rem的值是1）
            String rem = request.getParameter("rem");
            if ("1".equals(rem)) {
                //得到cookie对象
                Cookie cookie = new Cookie("user", userName + "-" + userPwd);
                //设置失效时间
                cookie.setMaxAge(3 * 24 * 60 * 60);
                //响应给客户端
                response.addCookie(cookie);
            } else {
                //清空原有的cookie对象
                Cookie cookie = new Cookie("user", null);
                //删除cookie,设置maxage为0
                cookie.setMaxAge(0);
            }
            //重定向跳转到Index页面
            response.sendRedirect("index.jsp");
        } else {
            //将resultInfo对象设置到request作用域中
            request.setAttribute("resultInfo", resultInfo);
            //请求转发跳转到登录页面
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
