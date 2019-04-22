package com.imooc.shop.service;


import com.imooc.shop.bean.Article;
import com.imooc.shop.bean.ArticleType;
import com.imooc.shop.bean.User;
import com.imooc.shop.repository.ArticleMapper;
import com.imooc.shop.repository.ArticleTypeMapper;
import com.imooc.shop.repository.UserMapper;
import com.imooc.shop.utils.Pager;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("shopService")
public class ShopServiecImpl implements ShopService {
    @Resource
    private ArticleTypeMapper articleTypeMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private ArticleMapper articleMapper;


    @Override
    public List<ArticleType> getArticleTypes() {
        return articleTypeMapper.getArticleTypes();
    }

    @Override
    public Map<String, Object> login(String loginName, String passWord) {


        Map<String, Object> result = new HashMap<>();
        if (StringUtils.isEmpty(loginName)||StringUtils.isEmpty(passWord)){
            result.put("code",1);
            result.put("msg","参数为空");
        }else{
            User user = userMapper.login(loginName);
            if (user!=null){
                if (user.getPassword().equals(passWord)){
                    result.put("code",0);
                    result.put("msg",user);
                }else{
                    result.put("code",2);
                    result.put("msg","密码错误");
                }
            }else {
                result.put("code",3);
                result.put("msg","用户名不存在");
            }
        }
        return result;

    }

    @Override
    public List<ArticleType> loadFirstArticleTypes() {
        List<ArticleType> articleTypes = articleTypeMapper.getFirstArticleTypes();
        return articleTypes;
    }

    @Override
    public List<ArticleType> loadSecondTypes(String typeCode) {
        List<ArticleType> articleTypes = articleTypeMapper.loadSecondTypes(typeCode+"%" , typeCode.length()+4);
        return articleTypes;
    }


    @Override
    public List<Article> searchArticles(String typeCode, String secondType, String title, Pager pager) {
        int count = articleMapper.count(typeCode,secondType,title);
        pager.setTotalCount(count);
        return articleMapper.searchArticles(typeCode,secondType,title,pager);
    }
}
