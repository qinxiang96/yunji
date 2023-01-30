package com.flora.note.web;

import javax.jws.WebService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author qinxiang
 * @Date 2022/11/30-下午3:31
 */
@WebServlet("/index")
public class IndexServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置首页导航高亮
        request.setAttribute("menu_page", "index");
        //设置首页动态包含的页面
        request.setAttribute("changePage", "note/list.jsp");
        //请求转发到index.jsp
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
}
