import java.time.LocalDate;
import java.time.LocalTime;



/**
 * @author abdelrahmanibrahim
 * @date 14/07/2023
 * Classe pour représenter les horaires des examens
 *
 */
public class HoraireExamen {

    // Attributs
    private LocalDate date;
    private LocalTime heureDebut;
    private LocalTime heureFin;


    // Constructeurs pour les horaires des examens
    public HoraireExamen(LocalDate date, LocalTime heureDebut, LocalTime heureFin) {
        this.date = date;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
    }

    // Getters et setters

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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
