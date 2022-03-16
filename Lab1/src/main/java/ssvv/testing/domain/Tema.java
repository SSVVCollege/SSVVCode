package ssvv.testing.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"descriere", "deadline", "startline"})
@AllArgsConstructor
@ToString
public class Tema implements HasID<String> {
    private String idTema;
    private String descriere;
    private int deadline;
    private int startline;

    @Override
    public String getID() { return this.idTema; }

    @Override
    public void setID(final String idTema) { this.idTema = idTema; }
}
