package tvz.jwafp.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tvz.jwafp.core.entity.Professor;
import tvz.jwafp.core.rest.DatabaseApi;
import tvz.jwafp.core.utils.JsonParser;

import java.util.List;

@Service
@Primary
@RequiredArgsConstructor
public class ProfessorServiceImpl implements ProfessorService {
    private static final String API_URL = DatabaseApi.PROFESSORS_API;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<Professor> getAll() {
        List<Professor> professors = null;
        try {
            professors = JsonParser.parseIntoList(
                    restTemplate.getForEntity(
                            API_URL,
                            String.class).getBody(),
                    Professor.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return professors;
    }

    @Override
    public Professor getProfessorById(String id) {
        Professor professor = null;
        try {
            professor = JsonParser.parseIntoObject(
                    restTemplate.getForEntity(API_URL + "/" + id,
                            String.class).getBody(),
                    Professor.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return professor;
    }

    @Override
    public List<Professor> getUnauthorizedProfessors() {
        List<Professor> professors = null;
        try {
            professors = JsonParser.parseIntoList(
                    restTemplate.getForEntity(
                            API_URL + "/unauthorized",
                            String.class).getBody(),
                    Professor.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return professors;
    }

    @Override
    public void saveProfessor(Professor professor) {
        restTemplate.postForEntity(API_URL, professor, void.class);
    }

    @Override
    public void updateProfessor(Professor professor) {
        restTemplate.put(API_URL, professor);
    }

    @Override
    public void authorizeProfessor(String id) {
        Professor professor = getProfessorById(id);
        professor.setAuthorized(true);
        restTemplate.put(API_URL + "/authorize", professor);
    }

    @Override
    public void deleteProfessors(List<String> ids) {
        for (String id : ids)
            restTemplate.delete(API_URL + "/" + id);
    }
}
