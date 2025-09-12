package tvz.jwafp.core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tvz.jwafp.core.helper.Messages;
import tvz.jwafp.core.helper.UserToRegister;
import tvz.jwafp.core.security.AuthenticationService;
import tvz.jwafp.core.service.UserService;

import static tvz.jwafp.core.utils.ModelInitialization.initialize;
import static tvz.jwafp.core.config.Urls.*;

@Controller
@RequestMapping(URL_SIGNUP)
public class SignupController {
    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final Messages messages;

    public SignupController(UserService userService, AuthenticationService authenticationService, Messages messages) {
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.messages = messages;
    }

    @GetMapping
    public String showRegisterView(Model model) {
        initModel(model);
        return "signup";
    }

    @PostMapping
    public String processSignup(Model model, RedirectAttributes redirectAttributes, UserToRegister userToRegister) {
        authenticationService.signup(userToRegister);
        if (userService.getByUsername(userToRegister.getUsername()) != null) {
            model.addAttribute("error", messages.getMessage("error.username-exists"));
            return "signup";
        } else if (!userToRegister.getPassword().equals(userToRegister.getRepeatPassword())) {
            model.addAttribute("error", messages.getMessage("error.password-match"));
            return "signup";
        }

        redirectAttributes.addFlashAttribute("userToRegister", userToRegister);
        return "redirect:" + URL_PROFILE;
    }

    private void initModel(Model model) {
        initialize(model, URL_SIGNUP);
        model.addAttribute("userToRegister", new UserToRegister());
    }
}
