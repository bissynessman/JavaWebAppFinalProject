package tvz.jwafp.core.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tvz.jwafp.core.helper.Messages;
import tvz.jwafp.core.comparator.UserComparator;
import tvz.jwafp.core.helper.BooleanWrapper;
import tvz.jwafp.core.helper.DeleteBuffer;
import tvz.jwafp.core.enums.Role;
import tvz.jwafp.core.entity.User;
import tvz.jwafp.core.security.AuthenticationService;
import tvz.jwafp.core.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

import static tvz.jwafp.core.utils.ModelInitialization.initialize;
import static tvz.jwafp.core.utils.PasswordUtils.hashPassword;
import static tvz.jwafp.core.config.Urls.*;

@Controller
@RequestMapping(URL_USERS)
@SessionAttributes("userLogin")
public class UsersController {
    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final Messages messages;
    private final LocaleResolver localeResolver;

    public UsersController(UserService userService,
                           AuthenticationService authenticationService,
                           Messages messages,
                           LocaleResolver localeResolver) {
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.messages = messages;
        this.localeResolver = localeResolver;
    }

    @GetMapping
    public String showUsersView(Model model, HttpServletRequest request) {
        authenticationService.refresh();
        User userLogin = (User) model.getAttribute("userLogin");
        initModel(model, localeResolver, request);
        return "users";
    }

    @GetMapping(URL_EDIT_USER)
    public String editUser(Model model, @PathVariable String userId, HttpServletRequest request) {
        authenticationService.refresh();
        initEditModel(model, userId, localeResolver, request);
        return "editUser";
    }

    @PostMapping(URL_DELETE)
    public String deleteUser(Model model,
                             RedirectAttributes redirectAttributes,
                             @ModelAttribute DeleteBuffer userBuffer,
                             HttpServletRequest request) {
        authenticationService.refresh();
        List<String> usersToDelete = userBuffer.getItems().entrySet().stream()
                .filter(entry -> entry.getValue().getValue())
                .map(Map.Entry::getKey)
                .toList();

        if (!usersToDelete.isEmpty()) {
            userService.deleteUser(usersToDelete);
            redirectAttributes.addFlashAttribute("success", messages.getMessage("success.changes-saved"));
        } else
            redirectAttributes.addFlashAttribute("error", messages.getMessage("error.missing-user"));

        initModel(model, localeResolver, request);
        return "redirect:" + URL_USERS;
    }

    @PostMapping(URL_EDIT_USER)
    public String processEditUser(Model model,
                                  RedirectAttributes redirectAttributes,
                                  @PathVariable String userId,
                                  @ModelAttribute User userUpdate) {
        authenticationService.refresh();
        User user = userService.getById(userId);

        if (user != null) {
            user.setUsername(userUpdate.getUsername());
            if (!userUpdate.getPassword().isEmpty()) {
                String spicedPassword = hashPassword(userUpdate.getPassword(), userUpdate.getUsername());
                user.setPassword(spicedPassword);
            }
            userService.updateUser(user);
            redirectAttributes.addFlashAttribute("success", messages.getMessage("success.changes-saved"));
        } else
            redirectAttributes.addFlashAttribute("error", messages.getMessage("error.no-user"));

        return "redirect:" + URL_USERS;
    }

    private void initEditModel(Model model, String userId, LocaleResolver localeResolver, HttpServletRequest request) {
        initialize(model, URL_USERS + "/edit/" + userId, localeResolver, request);
        User user = userService.getById(userId);
        if (user != null) {
            user.setPassword("");
            model.addAttribute("userUpdate", user);
        }
    }

    private void initModel(Model model, LocaleResolver localeResolver, HttpServletRequest request) {
        initialize(model, URL_USERS, localeResolver, request);
        List<User> usersList = userService.getAll().stream()
                .filter(user -> user.getRole() != Role.ADMIN)
                .sorted(new UserComparator())
                .toList();
        DeleteBuffer userBuffer = DeleteBuffer.builder()
                .items(userService.getAll().stream()
                        .collect(Collectors.toMap(User::getId, user -> new BooleanWrapper(false))))
                .build();
        model.addAttribute("usersList", usersList);
        model.addAttribute("userBuffer", userBuffer);
    }
}
