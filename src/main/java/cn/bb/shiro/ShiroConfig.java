package cn.bb.shiro;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {


    /**
     *  创建ShiroFilterFactoryBean
     * @param securityManager
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager securityManager
                                                       ){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //添加Shiro内置过滤器
        /*
                常用过滤器:
                    anon: 无需认证(登录) 可以访问
                    authc: 必须认证才可以访问
                    user: 如果使用rememberMe的功能可以直接访问
                    perms: 该资源必须得到资源权限才可以访问
                    role: 该资源必须得到角色权限才可以访问
         */
        //修改认证失败后跳转页面
       shiroFilterFactoryBean.setLoginUrl("/user/toLogin");
        //设置拦截路径 和 过滤器  拦截这些请求URL(controller) 如果没有对应的权限就会定向到login页面
        //只能用Link
        Map<String,String> filterMap = new LinkedHashMap<>();
        filterMap.put("/static/**", "anon");
        //登录注册放行
        filterMap.put("/user/index","authc");
        filterMap.put("/user/set","authc");
        filterMap.put("/user/notify","authc");
        filterMap.put("/user/addFriend/**","authc");
        filterMap.put("/index/toAddPost","authc");
        filterMap.put("/post/collectionPost/**","authc");
        filterMap.put("/index/toAddPost/**","authc");





        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
        return shiroFilterFactoryBean;
    }


    /**
     * 创建DefaultWebSecurityManager
     * @param userRealm
     * @return
     */
    @Bean(name="securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm){
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        //关联Realm
        defaultWebSecurityManager.setRealm(userRealm);
        return defaultWebSecurityManager;
    }

    /**
     * 创建Realm
     * @return
     */
    @Bean("userRealm")
    public UserRealm getUserRealm(){
        return new UserRealm();
    }

    /**
     * 配置shiroDialect 用于thymelefa和shiro标签配合使用
     * @return
     */
    @Bean("shiroDialect")
    public ShiroDialect getShiroDialect(){
        return new ShiroDialect();
    }


}