package tvz.jwafp.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tvz.jwafp.api.entity.Student;
import tvz.jwafp.api.service.CronService;

import java.util.List;

import static tvz.jwafp.api.config.Urls.*;

@RestController
@RequestMapping(URL_CRON)
public class CronController {
    private final CronService cronService;

    public CronController(CronService cronService) {
        this.cronService = cronService;
    }

    @GetMapping(URL_STUDENTS)
    public List<Student> students() {
        return cronService.getAllStudents();
    }

    @GetMapping(URL_EMAIL)
    public String email(@PathVariable String userId) {
        return cronService.getEmailByUserId(userId);
    }
}
