package tvz.jwafp.core.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tvz.jwafp.core.helper.JwtHolder;
import tvz.jwafp.core.helper.LoginRequest;
import tvz.jwafp.core.helper.Messages;
import tvz.jwafp.core.enums.Role;
import tvz.jwafp.core.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import tvz.jwafp.core.security.AuthenticationService;
import tvz.jwafp.core.service.UserService;

import static tvz.jwafp.core.utils.ModelInitialization.initialize;
import static tvz.jwafp.core.config.Urls.*;

@Controller
@RequestMapping(URL_LOGIN)
public class LoginController {
    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final Messages messages;
    private final JwtHolder jwtHolder;

    public LoginController(UserService userService,
                           AuthenticationService authenticationService,
                           Messages messages,
                           JwtHolder jwtHolder) {
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.messages = messages;
        this.jwtHolder = jwtHolder;
    }

    @GetMapping
    public String showLoginView(Model model) {
        initModel(model);
        return "login";
    }

    @PostMapping
    public String processLogin(Model model, RedirectAttributes redirectAttributes, LoginRequest userLogin) {
        try {
            authenticationService.login(userLogin.getUsername(), userLogin.getPassword());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (jwtHolder.getAccessToken() == null)
            model.addAttribute("error", messages.getMessage("error.invalid-username-password"));
        else {
            User currentUser = userService.getByUsername(userLogin.getUsername());
            redirectAttributes.addFlashAttribute("userLogin", currentUser);
            if (currentUser.getRole().equals(Role.ADMIN))
                return "redirect:" + URL_ADMIN;
            else if (currentUser.getRole().equals(Role.PROFESSOR))
                return "redirect:" + URL_PROFESSOR;
            else if (currentUser.getRole().equals(Role.STUDENT))
                return "redirect:" + URL_STUDENT;
        }

        initModel(model);
        return "login";
    }

    void initModel(Model model) {
        initialize(model, URL_LOGIN);
        model.addAttribute("userLogin", new LoginRequest());
    }
}
