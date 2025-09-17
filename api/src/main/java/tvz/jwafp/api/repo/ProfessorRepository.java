package tvz.jwafp.api.repo;

import org.apache.ibatis.annotations.Mapper;
import tvz.jwafp.api.entity.Professor;

import java.util.List;

@Mapper
public interface ProfessorRepository extends BaseRepository<Professor> {
    List<Professor> findAll();
    List<Professor> findAllUnauthorized();
    void authorize(String id);
}
