package com.flora.note.filter;

import cn.hutool.core.util.StrUtil;
import sun.util.resources.cldr.rw.CurrencyNames_rw;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 请求乱码解决
 * 乱码原因
 * 服务器默认的解析编码为ISO-8859-1,不支持中文
 * 乱码情况：
 * POST请求：解决方法：request.setCharacterEncoding("UTF-8")
 * Tomcat7及以下版本 乱码
 * Tomcat8及以上版本 乱码
 * GET请求：解决方法：new String(request.getParameter("xxx").getBytes("ISO-8859-1"),"UTF-8")
 * Tomcat7及以下版本 乱码
 * Tomcat8及以上版本 不乱码
 *
 * @Author qinxiang
 * @Date 2022/11/30-下午4:35
 */
@WebFilter("/*")//过滤所有的资源
public class EncodingFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //基于HTTP
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //处理POST请求（只针对POST请求有效，GET请求不受影响）
        request.setCharacterEncoding("UTF-8");
        //得到请求类型
        String method = request.getMethod();
        if ("GET".equalsIgnoreCase(method)) {
            //得到服务器版本
            String serverInfo = request.getServletContext().getServerInfo();
            //通过截取字符串，得到具体的版本号
            String version = serverInfo.substring(serverInfo.lastIndexOf("/") + 1, serverInfo.indexOf("."));
            //判断服务器版本是否是Tomcat7及以下
            if (version != null && Integer.parseInt(version) <= 7) {
                MyWapper myWapper = new MyWapper(request);
                //放行资源
                filterChain.doFilter(request, response);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }

    /**
     * 1.定义内部类（类的本质是request对象）
     * 2、继承HttpServletRequestWrapper包装类
     * 3、重写getPrameter()方法
     */
    class MyWapper extends HttpServletRequestWrapper {
        //定义成员变量 HttpServletRequest对象（提升构造器中request对象的作用域）
        private HttpServletRequest request;

        /**
         * 带参构造
         * 可以得到需要处理的request对象
         *
         * @param request
         */
        public MyWapper(HttpServletRequest request) {
            super(request);
            this.request = request;
        }

        @Override
        public String getParameter(String name) {
            //获取参数（乱码的参数值）
            String value = request.getParameter(name);
            //判断参数是否为空
            if (StrUtil.isBlank(value)) {
                return value;
            }
            //通过new String()处理乱码
            try {
                value = new String(value.getBytes("ISO-8859-1"), "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return value;
        }
    }
}
