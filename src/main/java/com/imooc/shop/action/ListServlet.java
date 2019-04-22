package com.imooc.shop.action;

import com.imooc.shop.bean.Article;
import com.imooc.shop.bean.ArticleType;
import com.imooc.shop.service.ShopService;
import com.imooc.shop.utils.Pager;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/list")
public class ListServlet extends HttpServlet {

    // 定义业务层对象
    private ShopService shopService;

    private HttpServletRequest request;
    private HttpServletResponse response;

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
            try {

                this.request = req;
                this.response = resp;
                request.setCharacterEncoding("UTF-8");
                String method = request.getParameter("method");
                switch (method){
                    case "getAll":
                        getAll();
                        break;
                }
            }catch (Exception e){
                    e.printStackTrace();
            }
    }

    private void getAll() throws ServletException, IOException {
        // 考虑分页查询
        Pager pager = new Pager();
        // 看是否传入了分页参数的页码
        String pageIndex = request.getParameter("pageIndex");
        //判断分页穿过来的参数是否为空
        if(!StringUtils.isEmpty(pageIndex)){
           int pSize = Integer.valueOf(pageIndex);
           pager.setPageIndex(pSize);
        }

        //获得二级导航菜单
        String secondType = request.getParameter("secondType");
        //获得收缩栏的输入标题
        String title = request.getParameter("title");
        request.setAttribute("title",title);
        request.setAttribute("secondType",secondType);

        //获取商品类型
        String typeCode = request.getParameter("typeCode");
        if (!StringUtils.isEmpty(typeCode)){
            List<ArticleType> secondTypes = shopService.loadSecondTypes(typeCode);
            request.setAttribute("typeCode",typeCode);
            request.setAttribute("secondTypes",secondTypes);
        }
        //获取商品一级目录
        List<ArticleType> firstArticleTypes = shopService.loadFirstArticleTypes();
        //获取目录下所有的商品
        List<Article> articles = shopService.searchArticles(typeCode,secondType,title,pager);
        request.setAttribute("pager", pager);
        request.setAttribute("articles", articles);
        request.setAttribute("firstArticleTypes", firstArticleTypes);
        request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
    }
}
