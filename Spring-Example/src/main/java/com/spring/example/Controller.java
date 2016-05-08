package com.spring.example;

import com.hansong.session.utils.JsonUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by xhans on 2016/5/7.
 */
@RestController
@RequestMapping("/users")
public class Controller {


    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public void addUser(@RequestBody String json, HttpSession httpSession){
        Map<String, String> map = JsonUtils.decode(json, Map.class);
        String name = map.get("name");
        String password = map.get("password");
        httpSession.setAttribute("name", name);
        httpSession.setAttribute("password", password);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public void login(@RequestBody String json, HttpServletRequest request, HttpServletResponse response){
        HttpSession httpSession = request.getSession();
        Map<String, String> map = JsonUtils.decode(json, Map.class);
        String nameTest = map.get("name");
        String passwordTest = map.get("password");
        String name = (String) httpSession.getAttribute("name");
        String password = (String) httpSession.getAttribute("password");
        if (name.equals(nameTest) && password.equals(passwordTest)){
            return;
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @ResponseBody
    public void logout(HttpSession httpSession){
        httpSession.invalidate();
    }
}
