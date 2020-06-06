package com.example.demo.service.impl;

import com.example.demo.dao.AdminMapper;
import com.example.demo.dao.ConsumerMapper;
import com.example.demo.domain.Admin;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.GsonBuilderUtils;

import javax.annotation.Resource;

public class MyShiroRealm extends AuthorizingRealm {
    @Autowired
    private AdminMapper adminMapper;


    //此函数负责登陆认证，从传来的参数里拿到用户名，然后通过用户名
    //读取用户实体类（可以读取对应的角色、权限信息），密码，
    //salt（像上面数据库信息一样，跟随用户信息插入进去），realm函数名，构造simpleAuthenticationInfo返回
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 获取用户输入的账户
        String username = (String) authenticationToken.getPrincipal();
        System.out.println(authenticationToken.getPrincipal());
        // 通过username从数据库中查找 UserInfo 对象
        // 实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
        Admin userInfo = adminMapper.findByName(username);
        if (null == userInfo) {
            return null;
        }
        String newPassword = new SimpleHash("md5", "1111",  new Md5Hash(userInfo.getSalt()), 1).toHex();
        System.out.println(newPassword);
        System.out.println(newPassword.equals(userInfo.getPassword()));
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(
                userInfo, // 用户实体类
                userInfo.getPassword(), // 密码
                new Md5Hash(ByteSource.Util.bytes(userInfo.getSalt())), // salt=username+salt
                getName() // realm name
        );
        return simpleAuthenticationInfo;
    }
    //此函数负责shiro主体的权限认证，用于获取用户的真实权限信息，用于上层权限核实，
    //从参数里读取用户实体信息，在实体信息
    //里读取用户对应的角色、权限，打包为simpleAuthorizationInfo实体返回
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 能进入这里说明用户已经通过验证了
        Admin userInfo = (Admin) principalCollection.getPrimaryPrincipal();
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        String role = userInfo.getIdentify();
            simpleAuthorizationInfo.addRole(role);
        return simpleAuthorizationInfo;
    }
}