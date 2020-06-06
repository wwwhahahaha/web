package com.example.demo.service.impl;

import com.example.demo.dao.AdminMapper;
import com.example.demo.service.AdminService;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public Boolean veritypasswd(String name, String password) {
        String salt = name;
        int times = 2;  // 加密次数：2
        String alogrithmName = "md5";   // 加密算法

        String encodePassword = new SimpleHash(alogrithmName, password, salt, times).toString();

        System.out.printf("原始密码是 %s , 盐是： %s, 运算次数是： %d, 运算出来的密文是：%s ",password,salt,times,encodePassword);

//        Subject subject = SecurityUtils.getSubject();
//        UsernamePasswordToken token = new UsernamePasswordToken(name, password);
//        try {
//            // 登录
//            subject.login(token);
//            // 登录成功后，获取菜单权限信息
//            if (subject.isAuthenticated()) {
//                return "登录成功";
//            }
//        } catch (IncorrectCredentialsException e) {
//            return "密码错误";
//        } catch (LockedAccountException e) {
//            return "登录失败，该用户已被冻结";
//        } catch (AuthenticationException e) {
//            return "该用户不存在";
//        } catch (Exception e) {
//            return e.getMessage();
//        }
//        return "登录失败";

       return adminMapper.verifyPassword(name, encodePassword)>0?true:false;
    }
}
