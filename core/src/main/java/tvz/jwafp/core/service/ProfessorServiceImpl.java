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
    @Autowired
    private DatabaseApi databaseApi;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<Professor> getAll() {
        List<Professor> professors = null;
        try {
            professors = JsonParser.parseIntoList(
                    restTemplate.getForEntity(
                            databaseApi.getProfessorsApi(),
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
                    restTemplate.getForEntity(databaseApi.getProfessorsApi() + "/" + id,
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
                            databaseApi.getProfessorsApi() + "/unauthorized",
                            String.class).getBody(),
                    Professor.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return professors;
    }

    @Override
    public void saveProfessor(Professor professor) {
        restTemplate.postForEntity(databaseApi.getProfessorsApi(), professor, void.class);
    }

    @Override
    public void updateProfessor(Professor professor) {
        restTemplate.put(databaseApi.getProfessorsApi(), professor);
    }

    @Override
    public void authorizeProfessor(String id) {
        Professor professor = getProfessorById(id);
        professor.setAuthorized(true);
        restTemplate.put(databaseApi.getProfessorsApi() + "/authorize", professor);
    }

    @Override
    public void deleteProfessors(List<String> ids) {
        for (String id : ids)
            restTemplate.delete(databaseApi.getProfessorsApi() + "/" + id);
    }
}
