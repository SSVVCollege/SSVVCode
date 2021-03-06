package ssvv.testing.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"nota", "saptamanaPredare", "feedback"})
@AllArgsConstructor
public class Nota implements HasID<Pair<String, String>> {
    private Pair<String, String> idNota;
    private Double nota;
    private Integer saptamanaPredare;
    private String feedback;

    @Override
    public Pair<String, String> getID() { return this.idNota; }

    @Override
    public void setID(final Pair<String, String> idNota) { this.idNota = idNota; }

    @Override
    public String toString() {
        return "Nota{" +
                "id_student = " + this.idNota.getObject1() +
                ", id_tema = " + this.idNota.getObject2() +
                ", nota = " + this.nota +
                ", saptamanaPredare = " + this.saptamanaPredare +
                ", feedback = '" + this.feedback + '\'' +
                '}';
    }
}
