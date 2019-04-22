package com.imooc.shop.action;

import com.imooc.shop.bean.User;
import com.imooc.shop.service.ShopService;
import com.imooc.shop.utils.Constants;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;


@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private ShopService shopService;

    @Override
    public void init() throws ServletException {
        super.init();
        // 获取sping的容器。然后从容器中得到业务层对象
        ServletContext servletContext = this.getServletContext() ;
        WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        shopService = (ShopService) context.getBean("shopService");
    }


    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.request = req;
        this.response = resp;
        String method = req.getParameter("method");
        switch (method){
            case "getjsp":
                req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req,resp);
                break;
            case "login":
                login();
                break;
        }
    }

    private void login() throws IOException {

        String loginName = request.getParameter("loginName");
        String passWord = request.getParameter("passWord");
        Map<String,Object> result = shopService.login(loginName,passWord);
        if((int)result.get("code") == 0){
            // 登陆成功的
            // 把登陆成功的用户注入到session会话中去
            // 跳转到主界面
            User user = (User) result.get("msg");
            request.setAttribute(Constants.USER_SESSION , user);
            response.sendRedirect(request.getContextPath()+"/list?method=getAll");

            // 请求跳转到主界面的servlet
//            try {
//                request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request,response);
//            } catch (ServletException e) {
//                e.printStackTrace();
//            }
//            response.sendRedirect("jsp/list.jsp");
        }else{
            String msg = result.get("msg")+"";
            request.setAttribute("msg",msg);
            try {
                request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request,response);
            } catch (ServletException e) {
                e.printStackTrace();
            }
        }
    }



    }

