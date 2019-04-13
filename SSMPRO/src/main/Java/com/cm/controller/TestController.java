package com.cm.controller;

import com.cm.entity.User;
import com.cm.service.IUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huaban.analysis.jieba.JiebaSegmenter;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@CrossOrigin(origins = "*",maxAge = 3600)
@Controller
@RequestMapping(value = "/test")
@ResponseBody
public class TestController {

    @Resource(name="userService")
    private IUserService userService;


//    两种不同的参数请求方式

    @RequestMapping("/showUser")
    public void selectUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        long userId = Long.parseLong(request.getParameter("id"));
        User user = this.userService.selectUser(userId);

        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(user));
        response.getWriter().close();
    }

    @RequestMapping("/getUser")
    public User getUser(@RequestParam("id") int id) {

        return this.userService.selectUser(id);
    }

//    止


//    两种不同的参数请求方式

    @RequestMapping("/test")
    public Object test(@RequestParam("id") String id){
        HashMap<String,Object> mp = new HashMap<String, Object>();
        mp.put("res","TestString");
        return mp;
    }

    @RequestMapping("/check")
    public void check(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        HashMap<String,Object>mp = new HashMap<String, Object>();
        mp.put("res","CheckString");

        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(mp));
        response.getWriter().close();
    }

//    止



    //登录认证
    @RequestMapping(value = "/login")
    public String Login(String username, String password, HttpSession session, Model model){
        if(username==null){
            model.addAttribute("message", "账号不为空");
            return "login";
        }
        //主体,当前状态为没有认证的状态“未认证”
        Subject subject = SecurityUtils.getSubject();
        // 登录后存放进shiro token
        UsernamePasswordToken token=new UsernamePasswordToken(username,password);
        User user;
        //登录方法（认证是否通过）
        //使用subject调用securityManager,安全管理器调用Realm
        try {
            //利用异常操作
            //需要开始调用到Realm中
            System.out.println("========================================");
            System.out.println("1、进入认证方法");
            subject.login(token);
            user = (User)subject.getPrincipal();
            session.setAttribute("user",subject);
            model.addAttribute("message", "登录完成");
            System.out.println("登录完成");
        } catch (UnknownAccountException e) {
            model.addAttribute("message", "账号密码不正确");
            return "index";
        }
        return "test";
    }

//    shiro已经帮我们验证了，所以我们只需要写基本业务逻辑就可以，不需要再写权限验证代码了
    @RequestMapping("/readName")
    public String readName(HttpSession session){

        return "name";
    }

    @RequestMapping("/readData")
    public String readData(){

        return "data";
    }


    @RequestMapping("/nopermission")
    public String noPermission(){
        return "error";
    }
}
