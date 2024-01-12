import java.time.LocalTime;

/**
 * @author abdelrahmanibrahim
 * @date 14/07/2023
 * Classe pour représenter les horaires des cours et des TP
 *
 */

public class Horaire {

    // Attributs
    private String jourSemaine;
    private LocalTime heureDebut;
    private LocalTime heureFin;

    // Constructeur pour les horaires des cours et des TP

    public Horaire(String jourSemaine, LocalTime heureDebut, LocalTime heureFin) {
        this.jourSemaine = jourSemaine;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
    }


    // Getters and setters...

    public String getJourSemaine() {
        return jourSemaine;
    }

    public void setJourSemaine(String jourSemaine) {
        this.jourSemaine = jourSemaine;
    }

    public LocalTime getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(LocalTime heureDebut) {
        this.heureDebut = heureDebut;
    }

    public LocalTime getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(LocalTime heureFin) {
        this.heureFin = heureFin;
    }


}
