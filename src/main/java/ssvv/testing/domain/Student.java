package ssvv.testing.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"nume", "grupa"})
@ToString
public class Student implements HasID<String> {
    private String idStudent;
    private String nume;
    private Integer grupa;

    @Override
    public String getID() {
        return this.idStudent;
    }

    @Override
    public void setID(final String idStudent) {
        this.idStudent = idStudent;
    }
}