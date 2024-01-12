import java.util.List;

/**
 * @author abdelrahmanibrahim
 * @date 14/07/2023
 * @description Classe pour représenter les cours
 * Les getters et setters non utilisés sont commentés
 *
 */
public class Cours {

    // Attributs
    private final String nom;
    private final String numero;
    private final int credits;
    private  List<HoraireExamen> horairesExamen;
    private List<Horaire> horairesCours;
    private List<Horaire> horairesTP;

    // Constructeur pour les cours
    public Cours(String nom, String numero, int credits, List<Horaire> horairesCours, List<Horaire> horairesTP, List<HoraireExamen> horairesExamen) {
        this.nom = nom;
        this.numero = numero;
        this.horairesCours = horairesCours;
        this.horairesTP = horairesTP;
        this.horairesExamen = horairesExamen;
        this.credits = credits;
    }


    // Getters et setters

    public String getNom() {
        return nom;
    }

//    public void setNom(String nom) {
//        this.nom = nom;
//    }

    public int getCredits() {
        return credits;
    }

//    public void setCredits(int credits) {
//        this.credits = credits;
//    }

    public String getNumero() {
        return numero;
    }

//    public void setNumero(String numero) {
//        this.numero = numero;
//    }

    public List<HoraireExamen> getHorairesExamen() {
        return horairesExamen;
    }

//    public void setHoraireExamen(List<HoraireExamen> horaireExamen) {
//        this.horairesExamen = horairesExamen;
//    }

    public List<Horaire> getHorairesCours() {
        return horairesCours;
    }

    public List<Horaire> getHorairesTP() {
        return horairesTP;
    }

//    public void setHorairesCours(List<Horaire> horairesCours) {
//        this.horairesCours = horairesCours;
//    }

//    public void setHorairesTP(List<Horaire> horairesTP) {
//        this.horairesTP = horairesTP;
//    }

}


