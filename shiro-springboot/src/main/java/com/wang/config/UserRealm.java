package com.wang.config;

import com.wang.pojo.User;
import com.wang.service.UserServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

//自定义的 UserRealm, 继承AuthorizingRealm即可
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private UserServiceImpl userService;

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("执行了 => AuthorizationInfo 授权");

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        //拿到当前登录的这个对象, user在下面已经放到了subject中
        Subject subject = SecurityUtils.getSubject();
        //拿到user对象
        User currentUser = (User) subject.getPrincipal();
        //放到perms中, 在ShiroConfig中以K-V调用
        info.addStringPermission(currentUser.getPerms());

        return info;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("执行了 => AuthenticationInfo 认证");

        //用户名, 密码 ==> 数据库中取
        //从token中获得用户名
        UsernamePasswordToken userToken = (UsernamePasswordToken) authenticationToken;

        String username = userToken.getUsername();
        User user = userService.queryUserByName(username);

        //输入的用户名在数据库中不存在
        if (user == null) {
            return null;
        }

        Subject currentSubject = SecurityUtils.getSubject();
        Session session = currentSubject.getSession();
        session.setAttribute("loginUser", user);

        String password = user.getPwd();

        //密码认证 : shiro做(password与token从前端取出的密码进行比较)
        //此处传入一个user, 放到了Subject中
        return new SimpleAuthenticationInfo(user, password, "");

    }
}
