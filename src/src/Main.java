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
            printMenu();

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    resourceManager.showResource();
                    break;
                case 2:
                    buildBuilding(manager, scanner);
                    break;
                case 3:
                    destroyBuilding(manager, scanner);
                    break;
                case 4:
                    addInhabitant(manager, scanner);
                    break;
                case 5:
                    removeInhabitant(manager, scanner);
                    break;
                case 6:
                    showBuildingList(manager);
                    break;
                case 0:
                    exitGame();
                    break;
                default:
                    System.out.println("Choix invalide.");
            }

            sleep(1000);
        }
    }

    // Méthodes utilitaires

    private static void printMenu() {
        System.out.println("Choisissez une action :");
        System.out.println("1. Afficher les ressources");
        System.out.println("2. Construire un bâtiment");
        System.out.println("3. Détruire un bâtiment");
        System.out.println("4. Ajouter un habitant à un bâtiment");
        System.out.println("5. Supprimer un habitant d'un bâtiment");
        System.out.println("6. Afficher la liste des bâtiments");
        System.out.println("0. Quitter");
    }

    private static void buildBuilding(Manager manager, Scanner scanner) {
        System.out.println("Choisissez un bâtiment à construire : ");
        BuildingFactory.getBuildingTypes().forEach(System.out::println);
        String buildingType = scanner.next();
        Command addBuildingCommand = new AddBuildingCommand(manager, buildingType);
        addBuildingCommand.execute();
    }

    private static void destroyBuilding(Manager manager, Scanner scanner) {
        System.out.println("Choisissez un bâtiment à détruire : ");
        manager.showBuildings();
        int buildingIndexToRemove = scanner.nextInt();
        if (isValidBuildingIndex(manager, buildingIndexToRemove)) {
            Building buildingToRemove = manager.getBuildings().get(buildingIndexToRemove - 1);
            Command removeBuildingCommand = new RemoveBuildingCommand(manager, buildingToRemove);
            removeBuildingCommand.execute();
        } else {
            System.out.println("Indice de bâtiment invalide.");
        }
    }

    private static void addInhabitant(Manager manager, Scanner scanner) {
        System.out.println("Choisissez un bâtiment pour ajouter un habitant : ");
        manager.showBuildings();
        int buildingIndexToAddInhabitant = scanner.nextInt();
        if (isValidBuildingIndex(manager, buildingIndexToAddInhabitant)) {
            Building buildingToAddInhabitant = manager.getBuildings().get(buildingIndexToAddInhabitant - 1);
            System.out.println("Nombre d'habitants à ajouter : ");
            int inhabitantsToAdd = scanner.nextInt();
            Command addInhabitantCommand = new AddInhabitantCommand(buildingToAddInhabitant, inhabitantsToAdd);
            addInhabitantCommand.execute();
        } else {
            System.out.println("Indice de bâtiment invalide.");
        }
    }

    private static void removeInhabitant(Manager manager, Scanner scanner) {
        System.out.println("Choisissez un bâtiment pour supprimer un habitant : ");
        manager.showBuildings();
        int buildingIndexToRemoveInhabitant = scanner.nextInt();
        if (isValidBuildingIndex(manager, buildingIndexToRemoveInhabitant)) {
            Building buildingToRemoveInhabitant = manager.getBuildings().get(buildingIndexToRemoveInhabitant - 1);
            System.out.println("Nombre d'habitants à supprimer : ");
            int inhabitantsToRemove = scanner.nextInt();
            Command removeInhabitantCommand = new RemoveInhabitantCommand(buildingToRemoveInhabitant, inhabitantsToRemove);
            removeInhabitantCommand.execute();
        } else {
            System.out.println("Indice de bâtiment invalide.");
        }
    }

    private static void showBuildingList(Manager manager) {
        // Afficher la liste des bâtiments avec leur production, consommation, le nombre d'habitants et le type
        System.out.println("Liste des bâtiments :");
        for (int i = 0; i < manager.getBuildings().size(); i++) {
            Building building = manager.getBuildings().get(i);
            System.out.println("Bâtiment " + (i + 1) + ": " +
                    "Type: " + building.getType() +
                    ", Population: " + building.getPopulation() + "/" + building.getPopulationLimit() +
                    ", Consommation: " + building.getResourceConsumption() +
                    ", Production: " + building.getResourceProduction());
        }
    }

    private static void exitGame() {
        System.out.println("Merci d'avoir joué!");
        System.exit(0);

    }

    private static boolean isValidBuildingIndex(Manager manager, int index) {
        return index >= 1 && index <= manager.getBuildings().size();
    }

    static void gameOver() {
        System.out.println("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡤⠖⠛⣻⣿⣻⣿⣿⣶⠶⣤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n" +
                "⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡴⠶⣦⡀⠀⠀⠀⠀⠀⠀⢀⡴⢋⣤⠶⣟⣛⣿⡿⠿⣿⣿⣷⡾⣿⣆⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n" +
                "⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⣇⣤⣿⡇⠀⠀⠀⠀⠀⢀⡞⣦⣨⣿⡳⠉⢛⣋⣤⣤⣘⣷⣿⡇⣼⣿⣷⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n" +
                "⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⠉⣿⣭⡇⠀⠀⠀⠀⠀⢸⡁⣿⡟⠉⠉⠓⠻⠿⠿⠟⠛⠉⠀⠀⠉⢫⣿⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n" +
                "⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⠀⠈⠀⠇⠀⠀⠀⠀⠀⢸⡿⠷⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢨⣿⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n" +
                "⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⣦⣤⡿⣂⠀⠀⠀⠀⠀⠘⣿⣿⡶⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣿⣷⡄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n" +
                "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⡇⠙⠋⢸⠀⠀⠀⠀⠀⢀⢿⣿⠁⠀⢀⣀⣤⣀⣀⠆⠀⣀⣤⣴⣶⣾⣿⣿⣿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n" +
                "⠀⠀⠀⠀⠀⠀⠀⠀⣠⠤⣿⠀⠀⢸⣀⣀⡀⠀⠀⣿⣻⣻⡂⠚⣫⣽⠿⣻⠟⢁⠀⣿⠛⠛⠹⠛⢿⣿⣿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n" +
                "⠀⠀⠀⠀⠀⠀⠀⢀⡇⠀⣾⠀⠀⠸⣇⣈⢹⣤⣄⠻⡿⡝⣇⠀⠀⠀⠈⠉⠀⠘⠚⣷⣄⠀⠀⠀⠘⣿⡏⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n" +
                "⠀⠀⠀⠀⠀⠀⠀⣼⠟⠛⣿⠀⠀⠙⢯⠉⠳⣿⠾⣷⡿⣷⢮⢷⡀⠀⠀⣠⠦⣗⠀⣹⣽⣆⠀⠀⢠⡿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n" +
                "⠀⠀⠀⠀⠀⢀⡞⠉⡇⢸⡟⣆⠀⠀⠀⠀⠀⡤⢧⡈⡇⠈⠻⣆⠙⢤⣼⣯⣀⣈⣛⣿⠿⣯⡗⢀⣾⠃⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n" +
                "⠀⠀⠀⠀⠀⣿⠛⠀⡇⢶⠀⠸⡄⠀⠀⠀⢸⠁⠀⢹⡇⠀⣰⣿⣄⠈⠃⠙⢿⣦⣤⡴⣾⢿⠇⢸⡿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n" +
                "⠀⠀⠀⠀⠀⠹⡀⢰⡇⠀⠀⠀⢻⠀⠀⠀⢸⡆⠀⠀⣷⣾⣿⣿⠈⢳⡀⠀⠀⠹⣷⣮⡵⠟⠀⣼⠇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n" +
                "⠀⠀⠀⠀⠀⠀⡇⠀⠀⠀⠀⠀⠐⠂⠀⠀⠀⠀⠀⠀⢹⣿⣿⣿⣧⡀⠘⠳⣄⠀⠀⠀⠀⢀⡴⣻⠀⠀⠰⣤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n" +
                "⠀⠀⠀⠀⠀⠀⠹⣄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣼⣿⣿⣿⣿⣿⣦⡀⠈⠙⠒⠒⣺⣿⣶⣿⣿⣿⣶⣽⣿⣿⣦⣄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n" +
                "⠀⠀⠀⠀⠀⠀⠀⠈⠓⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⣰⣿⣿⣿⣿⣿⣿⣯⢳⣀⠀⢀⣼⣷⣤⣞⣛⠿⣿⠈⠀⢹⣿⣿⣿⣷⡄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠀⠀⠀\n" +
                "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠳⢄⣀⡀⠀⠀⠀⠄⢰⡿⢿⣿⣿⣿⣿⣿⣿⣧⡻⣿⡿⠁⠈⠛⢿⣛⣻⣿⠀⠀⠀⢿⣿⣿⣿⣿⡀⠀⣀⣀⣤⣤⣴⣶⡾⠿⠿⣿⡄⠀⠀\n" +
                "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢈⣿⠀⠀⣀⣤⠖⠋⣠⣿⣿⣿⣿⣿⣿⣿⣿⣷⡄⠀⠀⠀⠀⠀⢹⠿⢛⣦⣀⣀⣨⣿⣿⣿⣿⣿⡿⢻⣿⣻⣭⣭⣤⣤⣄⠀⣿⣇⠀⠀\n" +
                "⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣴⡿⠟⠛⠉⠁⣀⣤⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣄⣀⣠⣤⣴⣿⣶⡿⠿⠿⠛⠛⢩⣭⢻⣷⣿⣿⡿⠿⠈⣿⣿⠉⠻⣿⡆⠸⣿⠀⠀\n" +
                "⠀⠀⠀⠀⠀⠀⠀⠀⠠⣎⣁⣠⣴⣶⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠿⠿⠛⠋⣙⣽⣦⣄⠀⢿⣷⡀⠀⢸⣿⠘⣿⣧⠀⠀⠀⠀⢹⣿⣶⣾⣿⣇⠀⣿⣆⠀\n" +
                "⠀⠀⠀⠀⠀⠀⠀⠀⠀⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠿⢿⡛⣿⣯⣭⣴⣾⣿⠁⠀⠀⢰⣿⡟⠛⢿⣷⠈⢿⣧⠀⢸⣿⠀⢹⣿⣿⠿⠇⠀⠘⣿⡏⠉⢹⣿⡄⢸⣿⠀\n" +
                "⠀⠀⠀⢀⣀⣀⣤⣤⣶⣾⡿⠿⢿⠻⠛⠋⣽⣅⠀⠀⢠⣿⣇⠸⣿⡟⠋⠉⠁⠀⠀⠀⠘⣿⡇⠀⠸⣿⡆⠈⢿⣷⣸⣿⠀⠘⣿⣇⢀⣀⣀⡄⢹⣿⡄⠈⠿⠷⠘⣿⡆\n" +
                "⠰⣶⡿⠿⠛⣛⣫⣉⠉⠀⢠⣾⣿⣿⣷⡄⢸⣿⣷⣤⣾⣿⣿⠀⣿⣷⣤⣶⣦⠀⠀⠀⠀⢿⣿⠀⠀⣿⣧⠀⠈⢿⣿⣿⠀⠀⢿⣿⠿⠿⠛⠃⢈⣋⣤⣤⣴⣶⣶⡿⠇\n" +
                "⠀⣿⣇⠀⣼⣿⠿⢿⣿⣆⣿⣿⠀⠈⢿⣷⠈⣿⡏⢿⣿⠉⣿⡇⢸⣿⡏⠉⠁⠀⠀⠀⠀⠘⢿⣷⣶⣿⠏⠀⠀⠈⠛⢃⣀⣀⣤⣴⣶⣾⠿⠿⠿⠛⠋⠉⠉⠀⠀⠀⠀\n" +
                "⠀⠸⣿⠀⢿⣿⠀⠀⢙⣃⠘⣿⣷⣶⣾⣿⡆⢻⣿⠀⠀⠀⢻⣿⠈⣿⣷⣶⣶⣿⠇⠀⠀⠀⢀⣈⣉⣤⣴⣶⣶⠿⠿⠟⠛⠋⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n" +
                "⠀⠀⣿⡇⢸⣿⡆⢸⣿⣿⡀⢿⣿⠉⠈⣿⣧⠸⣿⣧⠀⠀⠘⠿⡃⢘⣉⣡⣤⣤⣴⣾⠿⠿⠟⠛⠛⠋⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n" +
                "⠀⠀⢸⣿⠀⢿⣷⣤⣼⣿⠀⠸⣿⠆⠀⠘⣛⣀⣩⣥⣤⣶⣶⣿⠿⠟⠛⠛⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n" +
                "⠀⠀⠈⣿⡇⠀⠉⠛⣋⣡⣤⣤⣶⣶⣶⠿⠟⠛⠛⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n" +
                "⠀⠀⠀⢻⣿⣾⠿⠿⠟⠛⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
        System.exit(0);
    }


    private static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
