package tvz.jwafp.core.comparator;

import tvz.jwafp.core.entity.Grade;

import java.util.Comparator;

public class GradeComparator implements Comparator<Grade> {
    @Override
    public int compare(Grade g1, Grade g2) {
        return g1.getGrade().compareTo(g2.getGrade());
    }
}
