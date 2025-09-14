package tvz.jwafp.core.comparator;

import tvz.jwafp.core.entity.Professor;

import java.util.Comparator;

public class ProfessorComparator implements Comparator<Professor> {
    @Override
    public int compare(Professor p1, Professor p2) {
        int lastNameComparison = p1.getLastName().compareToIgnoreCase(p2.getLastName());
        if (lastNameComparison != 0) {
            return lastNameComparison;
        }
        return p1.getFirstName().compareToIgnoreCase(p2.getFirstName());
    }
}
