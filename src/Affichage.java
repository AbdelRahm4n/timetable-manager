import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author abdelrahmanibrahim
 * @date 14/07/2023
 * Classe pour afficher les horaires des cours
 */
public class Affichage {

    /**
     * Méthode pour afficher les horaires des cours et des TP
     * @param horaires List<Horaire>
     * @param type String
     * @return String
     */
    public static String afficherHoraire(List<Horaire> horaires, String type) {
        StringBuilder sb = new StringBuilder();  // StringBuilder pour concaténer les chaînes de caractères
        sb.append(type).append(" : \n"); // Ajouter le type (cours ou TP) à la chaîne de caractères

        for (int i = 0; i < horaires.size(); i++) { // Parcourir la liste des horaires
            Horaire horaire = horaires.get(i);
            sb.append("Horaire ").append(i + 1).append(": ") // Ajouter le numéro de l'horaire à la chaîne de caractères
                    .append("Jour de la semaine : ").append(horaire.getJourSemaine()) // Ajouter le jour de la semaine à la chaîne de caractères
                    .append(", Heure de début : ").append(horaire.getHeureDebut())  // Ajouter l'heure de début à la chaîne de caractères
                    .append(", Heure de fin : ").append(horaire.getHeureFin()).append("\n");    // Ajouter l'heure de fin à la chaîne de caractères
        }

        return sb.toString();
    }

    /**
     * Méthode pour afficher les horaires des examens
     * @param horairesExamen List<HoraireExamen>
     * @return String
     */
    public static String afficherHoraireExamen(List<HoraireExamen> horairesExamen) {
        StringBuilder sb = new StringBuilder();
        sb.append("Horaire de l'examen :");

        for (int i = 0; i < horairesExamen.size(); i++) {
            HoraireExamen horaireExamen = horairesExamen.get(i);
            sb.append("\nHoraire ").append(i + 1).append(": ")
                    .append("Date : ").append(horaireExamen.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))) // Ajouter la date à la chaîne de caractères
                    .append(", Heure de début : ").append(horaireExamen.getHeureDebut())
                    .append(", Heure de fin : ").append(horaireExamen.getHeureFin());
        }

        return sb.toString();
    }

    /**
     * Méthode pour créer ls horaires des cours et des TP
     * @param scanner Scanner
     * @return List<Horaire>
     */
    public static List<Horaire> creerHoraire(Scanner scanner) {
        // Attributs
        List<Horaire> horaires = new ArrayList<>();
        String jourSemaine;
        String[] joursCours;
        boolean jourValide;

        // Demander à l'utilisateur de saisir les jours de la semaine
        // Valider la saisie avec une boucle

        do {
            jourValide = true;
            System.out.print("Jours de la semaine (séparés par une virgule) : ");
            jourSemaine = scanner.nextLine();
            joursCours = jourSemaine.split(",");

            for (String jour : joursCours) {
                String jourTrimmed = jour.trim();
                if (!jourValide(jourTrimmed)) {
                    jourValide = false;
                    System.out.println("Jour de la semaine invalide. Veuillez réessayer.");
                    break;
                }
            }
        } while (!jourValide); // Tant que la saisie n'est pas valide, on redemande à l'utilisateur de saisir les jours de la semaine

        for (String jour : joursCours) {  // Parcourir les jours de la semaine
            String jourTrimmed = jour.trim();
            System.out.println("Jour de la semaine : " + jourTrimmed.toUpperCase());

            do {
                System.out.print("Heure de début " + jourTrimmed.toUpperCase() + " (hh:mm) : ");
                String heureDebutStr = scanner.nextLine();
                LocalTime heureDebut = convertirHeure(heureDebutStr);
                if (heureDebut != null) {
                    System.out.print("Heure de fin " + jourTrimmed.toUpperCase() + "  (hh:mm) : ");
                    String heureFinStr = scanner.nextLine();
                    LocalTime heureFin = convertirHeure(heureFinStr);

                    if (heureFin != null) {
                        if (heureDebut.equals(heureFin)) {
                            System.out.println("L'heure de début et l'heure de fin ne peuvent pas être identiques.");
                        } else if (heureDebut.isAfter(heureFin)) {
                            System.out.println("L'heure de début ne peut pas être après l'heure de fin.");
                        } else {
                            Horaire horaire = new Horaire(jourTrimmed, heureDebut, heureFin);
                            horaires.add(horaire);
                            break;
                        }
                    }
                }
            } while (true); // Tant que l'heure de début et l'heure de fin ne sont pas valides, on redemande à l'utilisateur de saisir les heures
        }

        return horaires; // Retourner la liste des horaires
    }

    /**
     * Méthode pour creer les horaires des examens
     * @param scanner Scanner
     * @return List<Horaire>
     */
    public static List<HoraireExamen> creerHoraireExamen(Scanner scanner) {
        List<HoraireExamen> horairesExamen = new ArrayList<>();

        System.out.print("Dates des examens (séparées par une virgule, jj/mm/aaaa) : "); // Demander à l'utilisateur de saisir les dates des examens
        String datesStr = scanner.nextLine();
        String[] datesArr = datesStr.split(",");

        for (String dateStr : datesArr) { // Parcourir les dates des examens
            LocalDate date = null;
            LocalTime heureDebut;
            LocalTime heureFin;

            boolean isDateValid = false; // Valider la saisie avec une boucle
            do {
                try {
                    date = LocalDate.parse(dateStr.trim(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    isDateValid = true;
                } catch (DateTimeParseException e) {
                    System.out.println("Format de date invalide. Veuillez réessayer ou appuyer sur Entrée pour annuler :");
                    dateStr = scanner.nextLine();
                    if (dateStr.isEmpty()) {
                        return new ArrayList<>();
                    }
                }
            } while (!isDateValid);

            do { // Demander à l'utilisateur de saisir l'heure de début et l'heure de fin
                System.out.print("Heure de début de l'examen du " + dateStr + " (hh:mm) : ");
                String heureDebutStr = scanner.nextLine();
                heureDebut = convertirHeure(heureDebutStr);
                if (heureDebut != null) {
                    System.out.print("Heure de fin de l'examen du " + dateStr + " (hh:mm) : ");
                    String heureFinStr = scanner.nextLine();
                    heureFin = convertirHeure(heureFinStr);

                    if (heureFin != null) {
                        if (heureDebut.equals(heureFin)) {
                            System.out.println("L'heure de début et l'heure de fin ne peuvent pas être identiques.");
                        } else if (heureDebut.isAfter(heureFin)) {
                            System.out.println("L'heure de début ne peut pas être après l'heure de fin.");
                        } else {
                            HoraireExamen horaireExamen = new HoraireExamen(date, heureDebut, heureFin);
                            horairesExamen.add(horaireExamen);
                            break;
                        }
                    }
                }
            } while (true); // Tant que l'heure de début et l'heure de fin ne sont pas valides, on redemande à l'utilisateur de saisir les heures
        }

        return horairesExamen; // Retourner la liste des horaires des examens
    }


    /**
     * Méthode pour valider le jour de la semaine
     * @param jourSemaine (String) : jour de la semaine
     * @return boolean
     */
    public static boolean jourValide(String jourSemaine) {
        String jourSemaineRegex = "(?i)Lundi|Mardi|Mercredi|Jeudi|Vendredi"; // Regex pour valider le jour de la semaine
        if (jourSemaine.matches(jourSemaineRegex)){ // Si le jour de la semaine est valide, on retourne true
            return true;
        } else {
            return false; // Sinon, on retourne false
        }
    }

    /**
     * Méthode pour convertir l'heure en objetLocalTime
     * @param heureStr (String) : heure saisie par l'utilisateur
     * @return LocalTime
     */
    public static LocalTime convertirHeure(String heureStr) {
        try {
            return LocalTime.parse(heureStr); // Essayer de convertir l'heure saisie en LocalTime
        } catch (DateTimeParseException e) { // Si l'heure n'est pas valide, on affiche un message d'erreur
            System.out.println("Format d'heure invalide. Veuillez entrer une heure au format hh:mm.");
            return null;
        }
    }

    /**
     * Méthode pour afficher l'horaire
     * @param emploiDuTemps (List<Cours>) : emploi du temps
     *
     */
    public static void voirEmploiDuTemps(List<Cours> emploiDuTemps) {
        if (emploiDuTemps.isEmpty()) { // Si l'emploi du temps est vide, on affiche un message
            System.out.println("L'emploi du temps est vide.");
        } else { // Sinon, on affiche l'emploi du temps
            System.out.println("Emploi du temps :");
            for (Cours cours : emploiDuTemps) {
                System.out.println("Nom du cours : " + cours.getNom());
                System.out.println("Numéro du cours : " + cours.getNumero());
                System.out.println("Horaire du " + afficherHoraire(cours.getHorairesCours(), "Cours magistral"));
                System.out.println("Horaire  du: " + afficherHoraire(cours.getHorairesTP() , "TP"));
                System.out.println(afficherHoraireExamen(cours.getHorairesExamen()));
                System.out.println("--------------------");
            }
        }
    }
}
