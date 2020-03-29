package kz.gexa.spring.shop.controller.web;


import kz.gexa.spring.shop.entity.user.User;
import kz.gexa.spring.shop.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class RegistrationController {


    final HttpServletRequest httpServletRequest;
    final UserService userService;

    public RegistrationController(HttpServletRequest httpServletRequest, UserService userService) {
        this.httpServletRequest = httpServletRequest;
        this.userService = userService;
    }


    @GetMapping("/registration")
    public String registration(Model model){
       if (httpServletRequest.getRemoteUser() != null){
           return "redirect:main";
       }
        model.addAttribute("error", null);
        return "registration";
    }

    @PostMapping("/registration")
    public String saveUserInDB(User user,
                               @RequestParam String confPassword,
                               Model model
    ){
        User existUser =(User) userService.loadUserByUsername(user.getUsername());

        if(existUser != null){
            model.addAttribute("error", true);
            return "registration";
        }

        if (
                user.getUsername().length() > 3 &&
                        user.getUsername().length() < 20 &&
                        user.getPassword().length() > 6 &&
                        user.getPassword().length() < 61 &&
                        user.getPassword().equals(confPassword)
        ){
            userService.registrationUser(user);
            return "redirect:login";
        }else {
            model.addAttribute("error", true);
        }

        return "registration";
    }
}
