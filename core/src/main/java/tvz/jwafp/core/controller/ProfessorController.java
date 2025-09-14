package tvz.jwafp.core.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tvz.jwafp.core.entity.Professor;
import tvz.jwafp.core.entity.User;
import tvz.jwafp.core.security.AuthenticationService;
import tvz.jwafp.core.service.ProfessorService;

import static tvz.jwafp.core.utils.ModelInitialization.initialize;
import static tvz.jwafp.core.config.Urls.*;

@Controller
@RequestMapping(URL_PROFESSOR)
@SessionAttributes({ "userLogin", "action" })
public class ProfessorController {
    private static final String ACTION_ADD_GRADE = "addGrade";
    private static final String ACTION_VIEW_STUDENTS = "viewStudents";
    private static final String ACTION_VIEW_ASSIGNMENTS = "viewAssignments";

    private final ProfessorService professorService;
    private final AuthenticationService authenticationService;
    private final LocaleResolver localeResolver;

    public ProfessorController(ProfessorService professorService,
                               AuthenticationService authenticationService,
                               LocaleResolver localeResolver) {
        this.professorService = professorService;
        this.authenticationService = authenticationService;
        this.localeResolver = localeResolver;
    }

    @GetMapping
    public String showProfessorView(Model model, HttpServletRequest request) {
        authenticationService.refresh();
        initModel(model, localeResolver, request);
        return "professor";
    }

    @PostMapping
    public String handleRedirects(Model model,
                                  RedirectAttributes redirectAttributes,
                                  @RequestParam("action") String action,
                                  HttpServletRequest request) {
        User userLogin = (User) model.getAttribute("userLogin");
        redirectAttributes.addFlashAttribute("userLogin", userLogin);
        return switch (action) {
            case ACTION_ADD_GRADE -> "redirect:" + URL_GRADES;
            case ACTION_VIEW_STUDENTS -> "redirect:" + URL_VIEW_STUDENTS;
            case ACTION_VIEW_ASSIGNMENTS -> "redirect:" + URL_ASSIGNMENT;
            default -> {
                initModel(model, localeResolver, request);
                yield "professor";
            }
        };
    }

    private void initModel(Model model, LocaleResolver localeResolver, HttpServletRequest request) {
        initialize(model, URL_PROFESSOR, localeResolver, request);
        User userLogin = (User) model.getAttribute("userLogin");
        Professor professor = professorService.getProfessorById(userLogin.getUserUuid());
        model.addAttribute("professor", professor);
    }
}