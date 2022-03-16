package ssvv.testing.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Nota implements HasID<Pair<String, String>> {
    private Pair<String, String> idNota;
    private double nota;
    private int saptamanaPredare;
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
