package cn.bb.shiro;

import cn.bb.entity.User;
import cn.bb.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 自定义Realm
 * 1.第一个方法用来授权, 首先需要获取SecurityUtils->Subject -> Principal(第二个方法的返回值的第一个参数)
 *                               ->得到对象 取出 对应的权限 放到加到返回值里面去
 *
 * 2.第二个方法是用来认证的 参数传过来 转型 得到相应账号密码token 之后走数据库 查是否有这个对象
 *     如果没有那么返回null  这个时候 controller 会抛异常  UnknownAccountException
 *     然后new 一个返回值  参数写上  返回密码过去 如果不对 会抛出IncorrectCredentialsException
 */
public class UserRealm extends AuthorizingRealm {
    @Autowired
    UserService userService;

    /**
     * 执行授权逻辑
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //给资源授权
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //设置授权
        Subject subject = SecurityUtils.getSubject();
        //这个是下面认证返回结果的第一个参数
        User user = (User) subject.getPrincipal();
        Integer id = user.getUid();
        //查找当前用户所有角色
        List<String> roles = userService.findUserAllRoleByUserId(id);
        //添加角色
        info.addRoles(roles);
        return info;
    }


    /**
     *   执行认证逻辑
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //编写shiro判断逻辑,判断用户名和密码
        //1.转型之后才能得出账号密码 判断用户名
        UsernamePasswordToken token = (UsernamePasswordToken)authenticationToken;
        //根据token得到userName
        User user = userService.findUserByAccount(token.getUsername());
        //如果找不到该用户
        if(null == user){
            //shiro底层会自动抛出UnknownAccountException
            return null;
        }

        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getPassword(), super.getName());
        return info;
    }
}
