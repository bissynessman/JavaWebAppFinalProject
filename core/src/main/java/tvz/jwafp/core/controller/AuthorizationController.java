package tvz.jwafp.core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tvz.jwafp.core.helper.Messages;
import tvz.jwafp.core.comparator.ProfessorComparator;
import tvz.jwafp.core.helper.ProfessorBuffer;
import tvz.jwafp.core.entity.Professor;
import tvz.jwafp.core.entity.User;
import tvz.jwafp.core.security.AuthenticationService;
import tvz.jwafp.core.service.ProfessorService;

import static tvz.jwafp.core.utils.ModelInitialization.initialize;
import static tvz.jwafp.core.config.Urls.*;

@Controller
@RequestMapping(URL_AUTHORIZATION)
public class AuthorizationController {
    private final ProfessorService professorService;
    private final AuthenticationService authenticationService;
    private final Messages messages;

    public AuthorizationController(ProfessorService professorService,
                                   AuthenticationService authenticationService,
                                   Messages messages) {
        this.professorService = professorService;
        this.authenticationService = authenticationService;
        this.messages = messages;
    }

    @GetMapping
    public String showAuthorizationView(Model model) {
        authenticationService.refresh();
        User userLogin = (User) model.getAttribute("userLogin");
        initModel(model);
        return "authorization";
    }

    @PostMapping
    public String processAuthorization(Model model, @ModelAttribute ProfessorBuffer professorBuffer) {
        authenticationService.refresh();
        User userLogin = (User) model.getAttribute("userLogin");

        if (!professorBuffer.getProfessors().isEmpty()) {
            for (Professor professor : professorBuffer.getProfessors())
                professorService.authorizeProfessor(professor.getId());
            model.addAttribute("success", messages.getMessage("success.changes-saved"));
        } else
            model.addAttribute("error", messages.getMessage("error.missing-professor"));

        initModel(model);
        return "authorization";
    }

    private void initModel(Model model) {
        initialize(model, URL_AUTHORIZATION);
        ProfessorBuffer professorBuffer = ProfessorBuffer.builder()
                .professors(professorService.getUnauthorizedProfessors().stream()
                        .sorted(new ProfessorComparator())
                        .toList())
                .build();
        model.addAttribute("professorBuffer", professorBuffer);
    }
}
