import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();
        ResourceManager resourceManager = ResourceManager.getInstance();

        // Thread pour exécuter manager.manageResources() en continu
        Thread resourceThread = new Thread(() -> {
            while (true) {
                manager.manageResources();
                sleep(1000);
            }
        });

        resourceThread.start(); // Démarrer le thread

        Scanner scanner = new Scanner(System.in);

        while (true) {
            Manager.printMenu();

            try {
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        resourceManager.showResource();
                        break;
                    case 2:
                        Manager.chooseBuilding(manager, scanner);
                        break;
                    case 3:
                        Manager.destroyBuilding(manager, scanner);
                        break;
                    case 4:
                        Manager.addInhabitant(manager, scanner);
                        break;
                    case 5:
                        Manager.removeInhabitant(manager, scanner);
                        break;
                    case 6:
                        Manager.showBuildingList(manager);
                        break;
                    case 0:
                        Manager.exitGame();
                        break;
                    default:
                        throw new GameExceptions.MenuChoiceNotFoundException();
                }
            } catch (GameExceptions.MenuChoiceNotFoundException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("Erreur lors de la saisie. Veuillez entrer une valeur valide.");
                scanner.nextLine(); // Consommer la nouvelle ligne restante
            }

            sleep(1000);
        }
    }

    // Méthode privée pour mettre en pause le thread pendant une durée spécifiée
    private static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
