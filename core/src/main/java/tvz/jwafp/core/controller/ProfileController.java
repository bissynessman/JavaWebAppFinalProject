package tvz.jwafp.core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tvz.jwafp.core.helper.Messages;
import tvz.jwafp.core.enums.Role;
import tvz.jwafp.core.helper.UserToRegister;
import tvz.jwafp.core.entity.Professor;
import tvz.jwafp.core.entity.Student;
import tvz.jwafp.core.entity.User;
import tvz.jwafp.core.security.AuthenticationService;
import tvz.jwafp.core.service.ProfessorService;
import tvz.jwafp.core.service.StudentService;
import tvz.jwafp.core.service.UserService;

import java.util.regex.*;

import static tvz.jwafp.core.utils.ModelInitialization.initialize;
import static tvz.jwafp.core.utils.PasswordUtils.hashPassword;
import static tvz.jwafp.core.config.Urls.*;

@Controller
@RequestMapping(URL_PROFILE)
@SessionAttributes({ "userToRegister", "profile" })
public class ProfileController {
    private static final String EMAIL_SUFFIX = "@jwafp.hr";

    private final UserService userService;
    private final ProfessorService professorService;
    private final StudentService studentService;
    private final AuthenticationService authenticationService;
    private final Messages messages;

    public ProfileController(UserService userService,
                             ProfessorService professorService,
                             StudentService studentService,
                             AuthenticationService authenticationService,
                             Messages messages) {
        this.userService = userService;
        this.professorService = professorService;
        this.studentService = studentService;
        this.authenticationService = authenticationService;
        this.messages = messages;
    }

    @GetMapping
    public String showProfileView(Model model) {
        authenticationService.refresh();
        initModel(model);
        return "profile";
    }

    @PostMapping
    public String handleProfileSetup(Model model, RedirectAttributes redirectAttributes,
                                     UserToRegister userToRegister,
                                     @ModelAttribute("profile") Object profile) {
        authenticationService.signup(userToRegister);
        if (profile instanceof Professor professorProfile) {
            Professor newProfessor = Professor.builder()
                    .firstName(professorProfile.getFirstName())
                    .lastName(professorProfile.getLastName())
                    .build();
            if (!alphaCheck(newProfessor.getFirstName())
                    || !alphaCheck(newProfessor.getLastName())) {
                model.addAttribute("error", messages.getMessage("error.invalid-characters"));
                return "profile";
            }
            professorService.saveProfessor(newProfessor);
            User savedUser = registerUser(userToRegister, newProfessor.getId());
            redirectAttributes.addFlashAttribute("userLogin", savedUser);
            return "redirect:" + URL_PROFESSOR;
        } else if (profile instanceof Student studentProfile) {
            Student newStudent = Student.builder()
                    .jmbag(studentProfile.getJmbag())
                    .firstName(studentProfile.getFirstName())
                    .lastName(studentProfile.getLastName())
                    .major(studentProfile.getMajor())
                    .build();
            if (!alphaCheck(newStudent.getFirstName())
                    || !alphaCheck(newStudent.getLastName())) {
                model.addAttribute("error", messages.getMessage("error.invalid-characters"));
                return "profile";
            } else if (!jmbagCheck(newStudent.getJmbag())) {
                model.addAttribute("error", messages.getMessage("error.invalid-jmbag"));
                return "profile";
            }
            studentService.saveStudent(newStudent);
            User savedUser = registerUser(userToRegister, newStudent.getId());
            redirectAttributes.addFlashAttribute("userLogin", savedUser);
            return "redirect:" + URL_STUDENT;
        }

        return "profile";
    }

    private User registerUser(UserToRegister userToRegister, String userUuid) {
        String spicedPassword = hashPassword(userToRegister.getPassword(), userToRegister.getUsername());
        User newUser = User.builder()
                .email(userToRegister.getUsername() + EMAIL_SUFFIX)
                .username(userToRegister.getUsername())
                .password(spicedPassword)
                .role(userToRegister.getRole())
                .userUuid(userUuid)
                .build();
        userService.saveUser(newUser);
        return newUser;
    }

    private boolean alphaCheck(String string) {
        String regex = "([a-z]|[A-Z])+";
        Pattern pattern = Pattern.compile(regex);

        if (string == null) return false;
        else return pattern.matcher(string).matches();
    }

    private boolean jmbagCheck(String string) {
        String regex = "^\\d{10}$";
        Pattern pattern = Pattern.compile(regex);

        if (string == null) return false;
        else return pattern.matcher(string).matches();
    }

    private void initModel(Model model) {
        initialize(model, URL_PROFILE);
        UserToRegister userToRegister = (UserToRegister) model.getAttribute("userToRegister");
        if (userToRegister.getRole().equals(Role.PROFESSOR))
            model.addAttribute("profile", Professor.builder().build());
        if (userToRegister.getRole().equals(Role.STUDENT))
            model.addAttribute("profile", Student.builder().build());
    }
}
