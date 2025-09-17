package tvz.jwafp.core.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import static tvz.jwafp.core.config.Urls.URL_ERROR;

@Controller
@RequestMapping(URL_ERROR)
public class GenericErrorController implements ErrorController {
    @RequestMapping
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute("javax.servlet.error.status_code");

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            model.addAttribute("statusCode", statusCode);

            if (statusCode == 404)
                return "error/404";
            else if (statusCode == 500)
                return "error/500";
        }

        return "error/generic";
    }
}
