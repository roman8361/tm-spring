package ru.kravchenko.spring.controler;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kravchenko.spring.api.IUserRepository;
import ru.kravchenko.spring.constant.FieldConst;
import ru.kravchenko.spring.entity.User;

import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author Roman Kravchenko
 */

@Controller
public class UserController {

    @Autowired
    private IUserRepository userRepository;

    @PostMapping("authorization")
    public String authorization(
            @RequestParam("login") final String login,
            @RequestParam("password") final String password,
            @NotNull final HttpSession session
    ) {
        if (userRepository.loginExist(login)) {
            System.out.println("login: " + login + " password: " + password);
            final User user = new User();
            user.setPassword(password);
            user.setLogin(login);
            session.setAttribute(FieldConst.USER, user);
            return "redirect:/main";
        }
        System.out.println("LOGIN NOT FOUND");
        return "redirect:/error";
    }

    @PostMapping("registry")
    public String registry(
            @RequestParam("login") String login,
            @RequestParam("password") String password
    ) throws IOException {
        if (!userRepository.loginExist(login)) {
            final User user = new User();
            user.setLogin(login);
            user.setPassword(password);
            userRepository.merge(user);
            System.out.println("User - " + login + " registry");
            return "redirect:/registry";
        }
        System.out.println("LOGIN IS BUSY");
        return "redirect:/loginbusy";
    }

    @GetMapping("logout")
    public String logout(@NotNull final HttpSession session) {
        session.invalidate();
        System.out.println("User logout");
        return "redirect:/index.jsp";
    }

}
