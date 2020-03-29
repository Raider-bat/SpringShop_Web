package kz.gexa.spring.shop.controller.web;

import kz.gexa.spring.shop.entity.user.User;
import kz.gexa.spring.shop.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Controller
public class UserProfileController {

    final
    UserService userService;
    final
    HttpServletRequest httpServletRequest;
    final
    PasswordEncoder passwordEncoder;

    public UserProfileController(UserService userService, HttpServletRequest httpServletRequest, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.httpServletRequest = httpServletRequest;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/profile")
    public String profile(Model model){
        String userName = httpServletRequest.getRemoteUser();
        User user = (User) userService.loadUserByUsername(userName);

        model.addAttribute("isAdmin", userService.isAdmin(userName));
        model.addAttribute("user", user);

        return "profile";
    }


    @PostMapping("/profile")
    public String editUser(
            User user,
            @RequestParam(required = false) String newPassword,
            @RequestParam(required = false) String confNewPassword
    ) throws ServletException {
        User userExist =(User) userService.loadUserByUsername(httpServletRequest.getRemoteUser());
        boolean passwordsMatches = passwordEncoder.matches(user.getPassword(), userExist.getPassword());
        if (
                passwordsMatches &&
                        newPassword.equals(confNewPassword) &&
                        newPassword.length() > 5 &&
                        newPassword.length() <50 &&
                        user.getUsername().length() >2 &&
                        user.getUsername().length() <20
        ) {
            userService.changePassword(user,userExist,newPassword);
            httpServletRequest.logout();
            return "successedituser";
        }

        return "redirect:/profile";
    }
}
