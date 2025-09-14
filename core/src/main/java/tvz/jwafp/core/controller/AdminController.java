package tvz.jwafp.core.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tvz.jwafp.core.entity.User;

import static tvz.jwafp.core.utils.ModelInitialization.initialize;
import static tvz.jwafp.core.config.Urls.*;

@Controller
@RequestMapping(URL_ADMIN)
@AllArgsConstructor
@SessionAttributes("userLogin")
public class AdminController {
    @GetMapping
    public String showAdminView(Model model) {
        User userLogin = (User) model.getAttribute("userLogin");
        initModel(model);
        return "admin";
    }

    @PostMapping
    public String handleRedirect(Model model, RedirectAttributes redirectAttributes,
                                 @RequestParam("action") String action) {
        User userLogin = (User) model.getAttribute("userLogin");
        redirectAttributes.addFlashAttribute("userLogin", userLogin);

        return switch (action) {
            case "authorizeProf" -> "redirect:" + URL_AUTHORIZATION;
            case "editCourses" -> "redirect:" + URL_COURSES;
            case "editUsers" -> "redirect:" + URL_USERS;
            default -> {
                initModel(model);
                yield "admin";
            }
        };
    }

    private void initModel(Model model) {
        initialize(model, URL_ADMIN);
        model.addAttribute("admin", model.getAttribute("userLogin"));
    }
}
