package tvz.jwafp.api.repo;

import org.apache.ibatis.annotations.Mapper;
import tvz.jwafp.api.entity.Grade;

import java.util.List;

@Mapper
public interface GradeRepository extends BaseRepository<Grade> {
    List<Grade> findAll();
    List<Grade> findByStudentId(String studentId);
    List<Grade> findByCourseId(String courseId);
}
