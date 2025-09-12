package tvz.jwafp.core.helper;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import tvz.jwafp.core.entity.Professor;

import java.util.List;

@Getter
@Setter
@Builder
public class ProfessorBuffer {
    private List<Professor> professors;
}
