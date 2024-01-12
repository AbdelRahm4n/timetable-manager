import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 *  @author abdelrahmanibrahim
 * @date 14/07/2023
 * Classe Main pour le programme de gestion d'horaire d'étudiant
 * Cette classe contient le menu principal du programme et les tests unitaires
 * Elle contient aussi la méthode main pour lancer le programme
 *
 * Ce programme permet de créer un emploi du temps pour un étudiant, en ajoutant, modifiant ou supprimant des cours
 * et des examens. On peut aussi voir l'emploi du temps créé. Il y a la possibilité de verifier les conflits entre
 * les cours, les tp et les examens.
 *
 */

public class Main {

    // Méthode main
    public static void main(String[] args) {
        List<Cours> emploiDuTemps = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        while (true) {                         // Menu principal
            System.out.println("Menu :");
            System.out.println("1. Ajouter un cours");
            System.out.println("2. Modifier un cours");
            System.out.println("3. Supprimer un cours");
            System.out.println("4. Voir l'emploi du temps créé");
            System.out.println("5. Tests unitaires");
            System.out.println("6. Quitter");
            System.out.print("Choix : ");
            int choix = scanner.nextInt();
            scanner.nextLine();               // Scanner pour lire les entrées de l'utilisateur

            switch (choix) {   // Switch case pour le menu principal
                case 1 -> GestionHoraireEtudiant.ajouterCours(emploiDuTemps, scanner);
                case 2 -> GestionHoraireEtudiant.modifierCours(emploiDuTemps, scanner);
                case 3 -> GestionHoraireEtudiant.supprimerCours(emploiDuTemps, scanner);
                case 4 -> GestionHoraireEtudiant.voirEmploiDuTemps(emploiDuTemps);
                case 5 -> runTestsUnitaires();
                case 6 -> {
                    System.out.println("Programme terminé.");
                    return;
                }
                default -> System.out.println("Choix invalide.");
            }
        }
    }

    // Tests unitaires
    private static void runTestsUnitaires() {
        testAjouterCoursMax();
        testModifierCours();
        testModifierCoursBis();
        testModifierCoursTer();
        AjouterCoursTest();
        AjouterCoursTestBis();
        AjouterCoursTestTer();
        testSupprimerCours();
        testVoirEmploiDuTemps();
    }

    /**
     * Test unitaire pour la méthode ajouterCours et voirEmploiDuTemps
     * Ajoute le maximum de cours (10 cours) et affiche l'emploi du temps
     * @see GestionHoraireEtudiant#ajouterCours(List, Scanner)
     */
    private static void testAjouterCoursMax() {
        List<Cours> emploiDuTemps = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        System.out.println("--------------------Test 1 : Ajouter le maximum de cours--------------------");
        // Ajouter le maximum de cours
        for (int i = 1; i <= 10; i++) {
            System.out.println("Cours " + i + " OK");
            String numero = "000" + i;
            String nom = "MAT " + i;
            int credits = 3;
            List<Horaire> horaireCoursMagistral = Arrays.asList(
                    new Horaire("Lundi", LocalTime.of(9, 0), LocalTime.of(10, 30)),
                    new Horaire("Mercredi", LocalTime.of(14, 0), LocalTime.of(15, 30))
            );
            List<Horaire> horaireTP = Arrays.asList(
                    new Horaire("Mardi", LocalTime.of(11, 0), LocalTime.of(12, 30)),
                    new Horaire("Jeudi", LocalTime.of(16, 0), LocalTime.of(17, 30))
            );
            List<HoraireExamen> horaireExamen = List.of(
                    new HoraireExamen(LocalDate.of(2023, 1, i), LocalTime.of(9, 0), LocalTime.of(11, 0))
            );
            Cours cours = new Cours(nom, numero, credits, horaireCoursMagistral, horaireTP, horaireExamen);
            emploiDuTemps.add(cours);
        }

        // ESSAYER D'AJOUTER UN COURS
        System.out.println("Cours 11 (Test limite)");
        GestionHoraireEtudiant.ajouterCours(emploiDuTemps, scanner);
    }

    /**
     * Test unitaire pour la méthode modifierCours
     * Modifie un cours et affiche l'emploi du temps
     * @see GestionHoraireEtudiant#modifierCours(List, Scanner)
     */
    private static void testModifierCours() {
        List<Cours> emploiDuTemps = new ArrayList<>();

        System.out.println("--------------------Test 2.1 : Modifier un cours--------------------");
        // AJOUTER UN COURS
        String numero = "1000";
        String nom = "MAT";
        int credits = 3;
        List<Horaire> horaireCoursMagistral = Arrays.asList(
                new Horaire("Lundi", LocalTime.of(9, 0), LocalTime.of(10, 30)),
                new Horaire("Mercredi", LocalTime.of(14, 0), LocalTime.of(15, 30))
        );
        List<Horaire> horaireTP = Arrays.asList(
                new Horaire("Mardi", LocalTime.of(11, 0), LocalTime.of(12, 30)),
                new Horaire("Jeudi", LocalTime.of(16, 0), LocalTime.of(17, 30))
        );
        List<HoraireExamen> horaireExamen = List.of(
                new HoraireExamen(LocalDate.of(2023, 1, 1), LocalTime.of(9, 0), LocalTime.of(11, 0))
        );
        Cours cours = new Cours(nom, numero, credits, horaireCoursMagistral, horaireTP, horaireExamen);
        emploiDuTemps.add(cours);

        // Modify the course
        Scanner scanner = new Scanner("1000\nC\n1\nVendredi\n10:00\n11:00\n");
        GestionHoraireEtudiant.modifierCours(emploiDuTemps, scanner);
        // Print the updated schedule
        GestionHoraireEtudiant.voirEmploiDuTemps(emploiDuTemps);
    }

    private static void testModifierCoursBis() {
        List<Cours> emploiDuTemps = new ArrayList<>();

        System.out.println("--------------------Test 2.2 : Modifier un cours (date avec conflit) --------------------");
        // AJOOUTER UN COURS
        String numero = "1000";
        String nom = "MAT";
        int credits = 3;
        List<Horaire> horaireCoursMagistral = Arrays.asList(
                new Horaire("Lundi", LocalTime.of(9, 0), LocalTime.of(10, 30)),
                new Horaire("Mercredi", LocalTime.of(14, 0), LocalTime.of(15, 30))
        );
        List<Horaire> horaireTP = Arrays.asList(
                new Horaire("Mardi", LocalTime.of(11, 0), LocalTime.of(12, 30)),
                new Horaire("Jeudi", LocalTime.of(16, 0), LocalTime.of(17, 30))
        );
        List<HoraireExamen> horaireExamen = List.of(
                new HoraireExamen(LocalDate.of(1111, 11, 11), LocalTime.of(9, 0), LocalTime.of(11, 0))
        );
        Cours cours = new Cours(nom, numero, credits, horaireCoursMagistral, horaireTP, horaireExamen);
        emploiDuTemps.add(cours);

        String numero2 = "111";
        String nom2 = "BAT";
        int credits2 = 3;
        List<Horaire> horaireCoursMagistral2 = Arrays.asList(
                new Horaire("Vendredi", LocalTime.of(9, 0), LocalTime.of(10, 30)),
                new Horaire("Mercredi", LocalTime.of(20, 0), LocalTime.of(21, 30))
        );
        List<Horaire> horaireTP2 = Arrays.asList(
                new Horaire("Mardi", LocalTime.of(18, 0), LocalTime.of(19, 30)),
                new Horaire("Jeudi", LocalTime.of(19, 0), LocalTime.of(20, 30))
        );
        List<HoraireExamen> horaireExamen2 = List.of(
                new HoraireExamen(LocalDate.of(2024, 1, 1), LocalTime.of(9, 0), LocalTime.of(11, 0)),
                new HoraireExamen(LocalDate.of(2023, 1, 1), LocalTime.of(9, 0), LocalTime.of(11, 0))
        );
        Cours cours2 = new Cours(nom2, numero2, credits2, horaireCoursMagistral2, horaireTP2, horaireExamen2);
        emploiDuTemps.add(cours2);

        // Modify the course
        Scanner scanner = new Scanner("111\nE\n2\n11/11/1111\n09:00\n11:00\n");
        GestionHoraireEtudiant.modifierCours(emploiDuTemps, scanner);

        // Print the updated schedule
        GestionHoraireEtudiant.voirEmploiDuTemps(emploiDuTemps);
    }

    private static void testModifierCoursTer() {
        List<Cours> emploiDuTemps = new ArrayList<>();

        System.out.println("--------------------Test 2.3 : Modifier un cours avec conflit--------------------");
        // AJOOUTER UN COURS
        String numero = "1000";
        String nom = "MAT";
        int credits = 3;
        List<Horaire> horaireCoursMagistral = Arrays.asList(
                new Horaire("Lundi", LocalTime.of(9, 0), LocalTime.of(10, 30)),
                new Horaire("Mercredi", LocalTime.of(14, 0), LocalTime.of(15, 30))
        );
        List<Horaire> horaireTP = Arrays.asList(
                new Horaire("Mardi", LocalTime.of(11, 0), LocalTime.of(12, 30)),
                new Horaire("Jeudi", LocalTime.of(16, 0), LocalTime.of(17, 30))
        );
        List<HoraireExamen> horaireExamen = List.of(
                new HoraireExamen(LocalDate.of(1111, 11, 11), LocalTime.of(9, 0), LocalTime.of(11, 0))
        );
        Cours cours = new Cours(nom, numero, credits, horaireCoursMagistral, horaireTP, horaireExamen);
        emploiDuTemps.add(cours);

        String numero2 = "111";
        String nom2 = "BAT";
        int credits2 = 3;
        List<Horaire> horaireCoursMagistral2 = Arrays.asList(
                new Horaire("Vendredi", LocalTime.of(9, 0), LocalTime.of(10, 30)),
                new Horaire("Mercredi", LocalTime.of(20, 0), LocalTime.of(21, 30))
        );
        List<Horaire> horaireTP2 = Arrays.asList(
                new Horaire("Mardi", LocalTime.of(18, 0), LocalTime.of(19, 30)),
                new Horaire("Jeudi", LocalTime.of(19, 0), LocalTime.of(20, 30))
        );
        List<HoraireExamen> horaireExamen2 = List.of(
                new HoraireExamen(LocalDate.of(2024, 1, 1), LocalTime.of(9, 0), LocalTime.of(11, 0)),
                new HoraireExamen(LocalDate.of(2023, 1, 1), LocalTime.of(9, 0), LocalTime.of(11, 0))
        );
        Cours cours2 = new Cours(nom2, numero2, credits2, horaireCoursMagistral2, horaireTP2, horaireExamen2);
        emploiDuTemps.add(cours2);

        // Modifier le cours
        Scanner scanner = new Scanner("111\nC\n1\nLuNdI\n09:00\n11:00\n");
        GestionHoraireEtudiant.modifierCours(emploiDuTemps, scanner);

        // Imprimer l'emploi du temps
        GestionHoraireEtudiant.voirEmploiDuTemps(emploiDuTemps);
    }

    /**
     * Test unitaire pour la méthode ajouterCours
     * Ajoute un cours et teste les conflits
     * @see GestionHoraireEtudiant#ajouterCours(List, Scanner)
     */
    public static void AjouterCoursTest() {
        List<Cours> emploiDuTemps = new ArrayList<>(); //  creer un emploi du temps vide
        String input = "1025\nIFT\n3\nLundi, Mercredi \n09:30\n10:30\n14:00\n15:30\nLundi, Jeudi\n09:00\n12:30\n16:00\n17:30\n 01/01/2023, 01/01/2023 \n09:00\n11:00\n13:00\n15:00\n";
        Scanner scanner = new Scanner(input);

        System.out.println("--------------------Test 3 : Ajouter Cours avec conflit entre tp et cours magistral dans la même matière --------------------");
        GestionHoraireEtudiant.ajouterCours(emploiDuTemps, scanner);

    }


    /**
     * Test unitaire pour la méthode ajouterCours
     * Ajoute un cours et teste les conflits
     * @see GestionHoraireEtudiant#ajouterCours(List, Scanner)
     */
    public static void AjouterCoursTestBis() {
        List<Cours> emploiDuTemps = new ArrayList<>(); //  creer un emploi du temps vide
        String input = "1025\nIFT\n3\nLundi, Mercredi \n09:30\n10:30\n14:00\n15:30\nVendredi, Jeudi\n09:00\n12:30\n16:00\n17:30\n 01/01/2023, 01/01/2023 \n09:00\n11:00\n13:00\n15:00\n";
        Scanner scanner = new Scanner(input);

        System.out.println("--------------------Test 3.1 : Ajouter Cours avec conflit entre dates d'examen --------------------");
        GestionHoraireEtudiant.ajouterCours(emploiDuTemps, scanner);

    }

    /**
     * Test unitaire pour la méthode ajouterCours
     * Ajoute un cours et teste les conflits
     * @see GestionHoraireEtudiant#ajouterCours(List, Scanner)
     */
    public static void AjouterCoursTestTer(){
        List<Cours> emploiDuTemps = new ArrayList<>(); //  creer un emploi du temps vide
        String input = "1025\nIFT\n3\nLundi, Mercredi \n09:30\n10:30\n14:00\n15:30\nVendredi, Jeudi\n09:00\n12:30\n16:00\n17:30\n 01/01/2023, 01/02/2023 \n09:00\n11:00\n13:00\n15:00\n";
        String inputBis = "1025\nIFT\n3\nLundi,  Mercredi \n01:30\n02:30\n11:00\n12:30\nVendredi, Jeudi\n09:00\n20:30\n18:00\n19:30\n 01/05/2023, 01/09/2023 \n09:00\n11:00\n13:00\n15:00\n";
        Scanner scanner = new Scanner(input);
        Scanner scannerBis = new Scanner(inputBis);

        System.out.println("--------------------Test 3.2 : Ajouter des cours avec conflit dans des matières differentes --------------------");
        GestionHoraireEtudiant.ajouterCours(emploiDuTemps, scanner);
        GestionHoraireEtudiant.ajouterCours(emploiDuTemps, scannerBis);

    }


    /**
     * Test de la methode supprimerCours
     * Supprime un cours et affiche l'emploi du temps
     * @see GestionHoraireEtudiant#supprimerCours(List, Scanner)
     *
     */
    private static void testSupprimerCours() {
        List<Cours> emploiDuTemps = new ArrayList<>();

        System.out.println("--------------------Test 4 : Supprimer un cours--------------------");
        // Ajouter un cours
        String numero = "1000";
        String nom = "MAT";
        int credits = 3;
        List<Horaire> horaireCoursMagistral = Arrays.asList(
                new Horaire("Lundi", LocalTime.of(9, 0), LocalTime.of(10, 30)),
                new Horaire("Mercredi", LocalTime.of(14, 0), LocalTime.of(15, 30))
        );
        List<Horaire> horaireTP = Arrays.asList(
                new Horaire("Mardi", LocalTime.of(11, 0), LocalTime.of(12, 30)),
                new Horaire("Jeudi", LocalTime.of(16, 0), LocalTime.of(17, 30))
        );
        List<HoraireExamen> horaireExamen = List.of(
                new HoraireExamen(LocalDate.of(2023, 1, 1), LocalTime.of(9, 0), LocalTime.of(11, 0))
        );
        Cours cours = new Cours(nom, numero, credits, horaireCoursMagistral, horaireTP, horaireExamen);
        emploiDuTemps.add(cours);
        // Afficher l'emploi du temps
        GestionHoraireEtudiant.voirEmploiDuTemps(emploiDuTemps);
        // Créer un scanner avec le numéro du cours à supprimer
        Scanner scanner = new Scanner("1000\n");
        // Supprimer le cours
        GestionHoraireEtudiant.supprimerCours(emploiDuTemps, scanner);
        // Afficher l'emploi du temps mis à jour
        GestionHoraireEtudiant.voirEmploiDuTemps(emploiDuTemps);
    }

    /**
     * Test de la methode voirEmploiDuTemps
     * Affiche l'emploi du temps
     * @see GestionHoraireEtudiant#voirEmploiDuTemps(List)
     *
     */
    private static void testVoirEmploiDuTemps() {
        List<Cours> emploiDuTemps = new ArrayList<>(); //  creer un emploi du temps vide

        System.out.println("--------------------Test 5 : Voir l'emploi du temps créé--------------------");
        // Ajouter un cours
        String numero = "1025";
        String nom = "IFT";
        int credits = 3;
        List<Horaire> horaireCoursMagistral = Arrays.asList(
                new Horaire("Lundi", LocalTime.of(9, 30), LocalTime.of(10, 30)),
                new Horaire("Mercredi", LocalTime.of(14, 0), LocalTime.of(15, 30))
        );
        List<Horaire> horaireTP = Arrays.asList(
                new Horaire("Lundi", LocalTime.of(9, 0), LocalTime.of(12, 30)), // conflit!
                new Horaire("Jeudi", LocalTime.of(16, 0), LocalTime.of(17, 30))
        );
        List<HoraireExamen> horaireExamen = List.of(
                new HoraireExamen(LocalDate.of(2023, 1, 1), LocalTime.of(9, 0), LocalTime.of(11, 0)),
                new HoraireExamen(LocalDate.of(2023, 1, 1), LocalTime.of(13, 0), LocalTime.of(15, 0)) // conflit!
        );
        Cours cours = new Cours(nom, numero, credits, horaireCoursMagistral, horaireTP, horaireExamen);
        emploiDuTemps.add(cours);

        // Ajouter un autre cours
        String numeroBis= "IFT";
        String nomBis = "1065";
        int creditsBis = 3;
        List<Horaire> horaireCoursMagistralBis = Arrays.asList(
                new Horaire("Mardi", LocalTime.of(9, 0), LocalTime.of(10, 30)),
                new Horaire("Jeudi", LocalTime.of(14, 0), LocalTime.of(15, 30))
        );
        List<Horaire> horaireTPBis = Arrays.asList(
                new Horaire("Lundi", LocalTime.of(11, 0), LocalTime.of(12, 30)),
                new Horaire("Mercredi", LocalTime.of(16, 0), LocalTime.of(17, 30))
        );
        List<HoraireExamen> horaireExamenBis = List.of(
                new HoraireExamen(LocalDate.of(2023, 1, 2), LocalTime.of(9, 0), LocalTime.of(11, 0))
        );
        Cours coursBis = new Cours(nomBis, numeroBis, creditsBis, horaireCoursMagistralBis, horaireTPBis, horaireExamenBis);
        emploiDuTemps.add(coursBis);

        // VoIR l'emploi du temps
        GestionHoraireEtudiant.voirEmploiDuTemps(emploiDuTemps);
    }
}
