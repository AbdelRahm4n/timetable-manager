import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.time.LocalTime;

/**
 * @author abdelrahmanibrahim
 * @date: 14/07/2023
 * @description: Cette classe permet de gérer l'emploi du temps de l'étudiant,
 * elle permet d'ajouter un cours, de supprimer un cours, de modifier un cours
 * ainsi que de gerer les conflits d'horaire.
 *
 */
public class GestionHoraireEtudiant extends Affichage {
    private static final int MAX_COURSES = 10; // Nombre maximum de cours pouvant être ajoutés à l'emploi du temps

    /**
     *
     * @param emploiDuTemps : l'emploi du temps de l'étudiant
     * @param scanner : scanner pour lire les entrées de l'utilisateur
     * Cette méthode permet d'ajouter un cours à l'emploi du temps.
     * On demande à l'utilisateur d'entrer les informations du cours, puis on vérifie si le cours peut être ajouté.
     *
     */
    public static void ajouterCours(List<Cours> emploiDuTemps, Scanner scanner) {
        // Verifier si le nombre maximum de cours a été atteint
        if (emploiDuTemps.size() >= MAX_COURSES) {
            System.out.println("Le nombre maximum de cours a été atteint. Impossible d'ajouter plus de cours.");
            return;
        }
        // Demander à l'utilisateur d'entrer les informations du cours
        System.out.print("Nom du cours : ");
        String nom = scanner.nextLine();

        System.out.print("Numéro du cours : ");
        String numero = scanner.nextLine();

        System.out.print("Nombre de crédits du cours : ");
        int credits = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Horaire du cours magistral :");
        List<Horaire>  horaireCoursMagistral = creerHoraire(scanner);


        System.out.println("Horaire du TP :");
        List<Horaire> horaireTP = creerHoraire(scanner);


        System.out.println("Horaire de l'examen :");
        List<HoraireExamen> horaireExamen = creerHoraireExamen(scanner);

        // Vérifier s'il y a un conflit d'horaire entre le cours magistral et le TP
        if (isConflictMemeCours(horaireCoursMagistral, horaireTP)) {
            System.out.println("Le cours magistral et le TP entrent en conflit.");
            return;
        }
        // Vérifier s'il y a un conflit d'horaire entre le cours magistral et un autre cours, ou entre le TP et un autre cours
        if (isConflictNewCours(horaireCoursMagistral, emploiDuTemps) || isConflictNewCours(horaireTP, emploiDuTemps)) {
            System.out.println("Le cours entre en conflit avec un autre cours déjà planifié.");
            return;
        }
        // Vérifier s'il y a un conflit d'horaire entre les dates d'examens elles mêmes
        if (hasExamDateConflict(horaireExamen)) {
            System.out.println("Conflit d'horaire : Deux examens sont prévus pour la même date.");
            return;
        }

        // verifier s'il y a un conflit d'horaire entre les dates examens et celles des autres cours
        if (isExamConflictNewCours(horaireExamen, emploiDuTemps)) {
            System.out.println("Conflit d'horaire : Deux examens sont prévus pour la même date.");
            return;
        }
        // Ajouter le cours à l'emploi du temps
        Cours cours = new Cours(nom, numero, credits, horaireCoursMagistral, horaireTP, horaireExamen);
        emploiDuTemps.add(cours);

        // Afficher les détails du cours ajouté
        System.out.println("Le cours a été ajouté à l'emploi du temps.");

    }

    /**
     *
     * @param scanner : scanner pour lire les entrées de l'utilisateur
     * @param emploiDuTemps : l'emploi du temps de l'étudiant
     * Cette méthode permet de choisir le cours à modifier dans l'emploi du temps,
     * puis de choisir le type d'horaire à modifier (cours magistral, TP ou examen).
     */
    public static void modifierCours(List<Cours> emploiDuTemps, Scanner scanner) {
        System.out.print("Entrez le numéro du cours à modifier : "); // Demander à l'utilisateur d'entrer le numéro du cours à modifier
        String numeroCours = scanner.nextLine();

        Cours coursSelectionne = null;  // Rechercher le cours dans l'emploi du temps
        for (Cours cours : emploiDuTemps) {
            if (cours.getNumero().equalsIgnoreCase(numeroCours)) {
                coursSelectionne = cours;
                break;
            }
        }

        if (coursSelectionne != null) {  // Si le cours est trouvé, afficher ses détails
            System.out.println("Détails actuels du cours :");
            System.out.println("Nom du cours : " + coursSelectionne.getNom());
            System.out.println("Numéro du cours : " + coursSelectionne.getNumero());
            System.out.println("Nombre de crédits : " + coursSelectionne.getCredits());

            System.out.print("Entrez 'C' pour modifier les horaires de cours, 'TP' pour modifier les horaires de TP, 'E' pour modifier les horaires d'examens : ");
            String option = scanner.nextLine();

            if (option.equalsIgnoreCase("C")) {
                modifierHoraires(coursSelectionne.getHorairesCours(), "Cours", emploiDuTemps,  scanner);
            } else if (option.equalsIgnoreCase("TP")) {
                modifierHoraires(coursSelectionne.getHorairesTP(), "TP", emploiDuTemps, scanner);
            } else if (option.equalsIgnoreCase("E")) {
                if (coursSelectionne.getHorairesExamen().isEmpty()) {
                    System.out.println("Aucun horaire d'examen trouvé pour ce cours.");
                } else {
                    modifierHorairesExamen(coursSelectionne.getHorairesExamen(), emploiDuTemps, scanner);
                }
            } else {
                System.out.println("Option invalide !");
            }
        } else {
            System.out.println("Cours introuvable !");
        }
    }

    /**
     *
     * @param horaires : liste des horaires à modifier
     * @param type : type d'horaire à modifier (cours magistral ou TP)
     * @param scanner
     *
     * Cette méthode permet de modifier les horaires d'un cours magistral ou d'un TP.
     * Elle verifie aussi si cette modification ne crée pas de conflit d'horaire.
     */
    public static void modifierHoraires(List<Horaire> horaires, String type, List<Cours> emploiDuTemps, Scanner scanner) {
        System.out.println("Horaires actuels " + type + " :");
        int count = 1;
        for (Horaire horaire : horaires) {
            System.out.println("Horaire " + count + " :");
            System.out.println("Type : " + type);
            System.out.println("Jour de la semaine : " + horaire.getJourSemaine());
            System.out.println("Heure de début : " + horaire.getHeureDebut());
            System.out.println("Heure de fin : " + horaire.getHeureFin());
            count++;
        }

        System.out.print("Entrez l'index de l'horaire " + type + " à modifier : ");
        String indiceHoraireString = scanner.nextLine();
        if (!indiceHoraireString.isEmpty()) {
            try {
                int indiceHoraire = Integer.parseInt(indiceHoraireString);
                if (indiceHoraire >= 1 && indiceHoraire <= horaires.size()) {
                    Horaire horaireSelectionne = horaires.get(indiceHoraire - 1);

                    System.out.println("Détails actuels de l'horaire " + type + " :");
                    System.out.println("Jour de la semaine : " + horaireSelectionne.getJourSemaine());
                    System.out.println("Heure de début : " + horaireSelectionne.getHeureDebut());
                    System.out.println("Heure de fin : " + horaireSelectionne.getHeureFin());

                    System.out.print("Nouveau jour de la semaine : ");
                    String nouveauJourSemaine = scanner.nextLine();
                    if (!nouveauJourSemaine.isEmpty()) {
                        if (!jourValide(nouveauJourSemaine)) {
                            System.out.println("Jour de la semaine invalide. Veuillez entrer un jour valide (du lundi au vendredi).");
                            return;
                        }
                    }

                    System.out.print("Nouvelle heure de début (hh:mm) : ");
                    String nouvelleHeureDebutString = scanner.nextLine();
                    if (!nouvelleHeureDebutString.isEmpty()) {
                        try {
                            LocalTime nouvelleHeureDebut = LocalTime.parse(nouvelleHeureDebutString);

                            // Verifier si la modification ne crée pas de conflit d'horaire
                            if (isModificationConflit(new Horaire(nouveauJourSemaine, nouvelleHeureDebut, horaireSelectionne.getHeureFin()), emploiDuTemps)) {
                                System.out.println("Il y a un conflit d'horaire avec un horaire existant. Veuillez choisir un horaire différent.");
                                return;
                            } else {
                                horaireSelectionne.setHeureDebut(nouvelleHeureDebut);
                            }
                        } catch (DateTimeParseException e) {
                            System.out.println("Format d'heure invalide. Veuillez entrer l'heure au format hh:mm.");
                            return;
                        }
                    }

                    System.out.print("Nouvelle heure de fin (hh:mm) : ");
                    String nouvelleHeureFinString = scanner.nextLine();
                    if (!nouvelleHeureFinString.isEmpty()) {
                        try {
                            LocalTime nouvelleHeureFin = LocalTime.parse(nouvelleHeureFinString);

                            // Verifier si la modification ne crée pas de conflit d'horaire encore une fois
                            if (isModificationConflit(new Horaire(nouveauJourSemaine, horaireSelectionne.getHeureDebut(), nouvelleHeureFin), emploiDuTemps)) {
                                System.out.println("Il y a un conflit d'horaire avec un horaire existant. Veuillez choisir un horaire différent.");
                                return;
                            } else {
                                horaireSelectionne.setJourSemaine(nouveauJourSemaine);
                                horaireSelectionne.setHeureFin(nouvelleHeureFin);
                            }
                        } catch (DateTimeParseException e) {
                            System.out.println("Format d'heure invalide. Veuillez entrer l'heure au format hh:mm.");
                            return;
                        }
                    }

                    System.out.println("Horaire " + type + " modifié avec succès !");
                } else {
                    System.out.println("Index d'horaire " + type + " invalide !");
                }
            } catch (NumberFormatException e) {
                System.out.println("Index d'horaire " + type + " invalide. Veuillez entrer une valeur entière valide.");
            }
        }
    }


    /**
     * Cette méthode permet de modifier les horaires d'un examen.
     * @param horairesExamen : liste des horaires d'examen à modifier
     * @param emploiDuTemps : emploi du temps actuel
     * @param scanner : scanner pour lire les entrées de l'utilisateur
     */
    public static void modifierHorairesExamen(List<HoraireExamen> horairesExamen, List<Cours> emploiDuTemps, Scanner scanner) {
        System.out.println("Horaires actuels des examens :");
        int count = 1;
        for (HoraireExamen horaireExamen : horairesExamen) {
            System.out.println("Horaire " + count + " :");
            System.out.println("Date : " + horaireExamen.getDate());
            System.out.println("Heure de début : " + horaireExamen.getHeureDebut());
            System.out.println("Heure de fin : " + horaireExamen.getHeureFin());
            count++;
        }

        System.out.print("Entrez l'index de l'horaire d'examen à modifier (Appuyez sur Entrée pour passer) : ");
        String indiceHoraireString = scanner.nextLine();
        if (!indiceHoraireString.isEmpty()) {
            try {
                int indiceHoraire = Integer.parseInt(indiceHoraireString);
                if (indiceHoraire >= 1 && indiceHoraire <= horairesExamen.size()) {
                    HoraireExamen horaireExamenSelectionne = horairesExamen.get(indiceHoraire - 1);

                    System.out.println("Détails actuels de l'horaire d'examen :");
                    System.out.println("Date : " + horaireExamenSelectionne.getDate());
                    System.out.println("Heure de début : " + horaireExamenSelectionne.getHeureDebut());
                    System.out.println("Heure de fin : " + horaireExamenSelectionne.getHeureFin());

                    System.out.print("Nouvelle date de l'examen (jj/mm/aaaa) (Appuyez sur Entrée pour conserver l'actuelle) : ");
                    String nouvelleDateExamenString = scanner.nextLine();
                    if (!nouvelleDateExamenString.isEmpty()) {
                        try {
                            LocalDate nouvelleDateExamen = LocalDate.parse(nouvelleDateExamenString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                            // Check for conflicts with other exam dates
                            if (isExamModifConflit(emploiDuTemps, nouvelleDateExamen)) {
                                System.out.println("Il y a un conflit d'horaire avec un autre examen. Veuillez choisir une date différente.");
                                return;
                            } else {
                                horaireExamenSelectionne.setDate(nouvelleDateExamen);
                            }
                        } catch (DateTimeParseException e) {
                            System.out.println("Format de date invalide. Veuillez entrer la date au format jj/mm/aaaa.");
                            return;
                        }
                    }

                    System.out.print("Nouvelle heure de début (hh:mm) (Appuyez sur Entrée pour conserver l'actuelle) : ");
                    String nouvelleHeureDebutString = scanner.nextLine();
                    if (!nouvelleHeureDebutString.isEmpty()) {
                        try {
                            LocalTime nouvelleHeureDebut = LocalTime.parse(nouvelleHeureDebutString);

                            horaireExamenSelectionne.setHeureDebut(nouvelleHeureDebut);
                        } catch (DateTimeParseException e) {
                            System.out.println("Format d'heure invalide. Veuillez entrer l'heure au format hh:mm.");
                            return;
                        }
                    }

                    System.out.print("Nouvelle heure de fin (hh:mm) (Appuyez sur Entrée pour conserver l'actuelle) : ");
                    String nouvelleHeureFinString = scanner.nextLine();
                    if (!nouvelleHeureFinString.isEmpty()) {
                        try {
                            LocalTime nouvelleHeureFin = LocalTime.parse(nouvelleHeureFinString);

                            horaireExamenSelectionne.setHeureFin(nouvelleHeureFin);
                        } catch (DateTimeParseException e) {
                            System.out.println("Format d'heure invalide. Veuillez entrer l'heure au format hh:mm.");
                            return;
                        }
                    }

                    System.out.println("Horaire d'examen modifié avec succès !");
                } else {
                    System.out.println("Index d'horaire d'examen invalide !");
                }
            } catch (NumberFormatException e) {
                System.out.println("Index d'horaire d'examen invalide. Veuillez entrer une valeur entière valide.");
            }
        }
    }



    /**
     * Méthode pour supprimer un cours de l'emploi du temps
     * @param emploiDuTemps La liste des cours
     * @param scanner Le scanner pour lire les entrées de l'utilisateur
     */
    public static void supprimerCours(List<Cours> emploiDuTemps, Scanner scanner) {
        System.out.print("Numéro du cours à supprimer : "); // Demander à l'utilisateur d'entrer le numéro du cours à supprimer
        String numero = scanner.nextLine();

        for (int i = 0; i < emploiDuTemps.size(); i++) { // Parcourir la liste des cours
            Cours cours = emploiDuTemps.get(i);
            if (cours.getNumero().equals(numero)) { // Vérifier si le numéro du cours correspond à celui entré par l'utilisateur
                emploiDuTemps.remove(i); // Supprimer le cours de la liste
                System.out.println("Le cours a été supprimé de l'emploi du temps.");
                return;
            }
        }

        System.out.println("Aucun cours trouvé avec le numéro spécifié.");
    }


    /**
     *
     * Methode pour verifier qu'un un nouveau cours ne crée pas de conflit d'horaire
     * @return boolean
     * @param newHoraires La liste des horaires du nouveau cours
     * @param emploiDuTemps La liste des cours
     *
     */
    private static boolean isConflictNewCours(List<Horaire> newHoraires, List<Cours> emploiDuTemps) {
        for (Cours cours : emploiDuTemps) {
            List<Horaire> existingHorairesCours = cours.getHorairesCours();
            List<Horaire> existingHorairesTP = cours.getHorairesTP();

            for (Horaire newHoraire : newHoraires) {
                for (Horaire existingHoraireCours : existingHorairesCours) {
                    if (horaireConflict(newHoraire, existingHoraireCours)) {
                        return true; // Conflit entre cours magistral et cours magistral existant
                    }
                }

                for (Horaire existingHoraireTP : existingHorairesTP) {
                    if (horaireConflict(newHoraire, existingHoraireTP)) {
                        return true; // Conflict entre cours magistral et TP existant
                    }
                }
            }
        }
        return false;
    }


    /**
     * Methode pour verifier qu'une modification ne crée pas de conflit d'horaire
     *
     * @param newHoraire L'horaire à modifier
     * @param emploiDuTemps La liste des cours
     * @return boolean
     */
    public static boolean isModificationConflit(Horaire newHoraire, List<Cours> emploiDuTemps) {
        for (Cours cours : emploiDuTemps) {
            for (Horaire horaire : cours.getHorairesCours()) {
                if (horaireConflict(newHoraire, horaire)) {
                    return true; // Conflict trouvé
                }
            }
            for (Horaire horaire : cours.getHorairesTP()) {
                if (horaireConflict(newHoraire, horaire)) {
                    return true; // Conflit trouvé
                }
            }

        }
        return false; // Pas de conflit
    }


    /**
     * Methode generale pour verifier si deux horaires sont en conflit
     * @return boolean
     * @param horaire1 L'horaire 1
     * @param horaire2 L'horaire 2
     */
    private static boolean horaireConflict(Horaire horaire1, Horaire horaire2) {
        // Vérifier si les jours de la semaine sont les mêmes
        if (horaire1.getJourSemaine().equalsIgnoreCase(horaire2.getJourSemaine())) {
            // Vérifier si les heures de début et de fin sont en conflit
            LocalTime debut1 = horaire1.getHeureDebut();
            LocalTime fin1 = horaire1.getHeureFin();
            LocalTime debut2 = horaire2.getHeureDebut();
            LocalTime fin2 = horaire2.getHeureFin();

            return (debut1.isBefore(fin2) || debut1.equals(fin2))
                    && (debut2.isBefore(fin1) || debut2.equals(fin1)); // Conflit trouvé
        }
        return false;
    }

    /**
     * Methode pour verifier que les horiares d'un même cours ne sont pas en conflit
     * @return boolean
     * @param magistralHoraires La liste des horaires de cours magistral
     * @param tpHoraires La liste des horaires de TP
     */
    private static boolean isConflictMemeCours(List<Horaire> magistralHoraires, List<Horaire> tpHoraires) {
        for (Horaire magistralHoraire : magistralHoraires) { // Parcourir la liste des horaires de cours magistral
            for (Horaire tpHoraire : tpHoraires) { // Parcourir la liste des horaires de TP
                if (horaireConflict(magistralHoraire, tpHoraire)) {
                    return true; // Conflit trouvé
                }
            }
        }
        return false;
    }

    /**
     * Methode pour verifier que les horiares d'un examen ne sont pas en conflit
     * @return boolean
     * @param horaireExamen La liste des horaires d'examen
     */
    private static boolean hasExamDateConflict(List<HoraireExamen> horaireExamen) {
        Set<LocalDate> examDates = new HashSet<>(); // Utiliser un set pour éviter les doublons

        for (HoraireExamen horaire : horaireExamen) { // Parcourir la liste des horaires d'examen
            if (examDates.contains(horaire.getDate())) { // Vérifier si la date de l'examen existe déjà
                return true; // Conflit trouvé
            }
            examDates.add(horaire.getDate());
        }

        return false;
    }


    /**
     * Methode pour verifier qu'un nouvel examen ne crée pas de conflit d'horaire
     * @return boolean
     * @param horaireExamen La liste des horaires d'examen
     * @param emploiDuTemps La liste des cours
     */
    private static boolean isExamConflictNewCours(List<HoraireExamen> horaireExamen, List<Cours> emploiDuTemps) {
        Set<LocalDate> examDates = new HashSet<>(); // Utiliser un set pour éviter les doublons

        for (HoraireExamen horaire : horaireExamen) { // Parcourir la liste des horaires d'examen
            if (examDates.contains(horaire.getDate())) { // Vérifier si la date de l'examen existe déjà
                return true; // Conflit trouvé
            }
            examDates.add(horaire.getDate());
        }

        for (Cours cours : emploiDuTemps) { // Parcourir la liste des cours
            List<HoraireExamen> examens = cours.getHorairesExamen();
            for (HoraireExamen examen : examens) {
                for (HoraireExamen newExamen : horaireExamen) {
                    if (examen.getDate().equals(newExamen.getDate())) {
                        return true; // Conflit trouvé
                    }
                }
            }
        }

        return false;
    }

    /**
     * Methode pour verifier qu'une modification d'examen ne crée pas de conflit d'horaire
     * @return boolean
     * @param emploiDuTemps La liste des cours
     * @param examDate La date de l'examen
     */
    private static boolean isExamModifConflit(List<Cours> emploiDuTemps, LocalDate examDate) {
        for (Cours cours : emploiDuTemps) {
            List<HoraireExamen> horairesExamen = cours.getHorairesExamen(); // Récupérer les horaires d'examen du cours

            for (HoraireExamen horaireExamen : horairesExamen) { // Pour chaque horaire d'examen
                if (horaireExamen.getDate().equals(examDate)) { // Si la date de l'horaire d'examen est égale à la date de l'examen
                    return true; // Conflit d'horaire trouvé
                }
            }
        }

        return false; // Pas de conflit d'horaire
    }


}






