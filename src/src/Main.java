import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();
        ResourceManager resourceManager = ResourceManager.getInstance();

        // Thread pour exécuter manager.manageResources() en continu
        Thread resourceThread = new Thread(() -> {
            while (true) {
                manager.manageResources();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        resourceThread.start(); // Démarrer le thread

        Scanner scanner = new Scanner(System.in);


        while (true) {
            System.out.println("Choisissez une action :");
            System.out.println("1. Afficher les ressources");
            System.out.println("2. Construire un bâtiment");
            System.out.println("3. Détruire un bâtiment");
            System.out.println("4. Ajouter un habitant à un bâtiment");
            System.out.println("5. Supprimer un habitant d'un bâtiment");
            System.out.println("6. Afficher la liste des bâtiments");
            System.out.println("0. Quitter");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    resourceManager.showRessource();
                    break;
                case 2:
                    System.out.println("Choisissez un bâtiment à construire : ");
                    // Afficher la liste des types de bâtiments disponibles
                    BuildingFactory.getBuildingTypes().forEach(System.out::println);
                    String buildingType = scanner.next();
                    Building newBuilding = BuildingFactory.createBuilding(buildingType);
                    manager.addBuilding(newBuilding);
                    newBuilding.build();
                    break;
                case 3:
                    System.out.println("Choisissez un bâtiment à détruire : ");
                    manager.showBuildings();
                    int buildingIndexToRemove = scanner.nextInt();
                    if (buildingIndexToRemove >= 0 && buildingIndexToRemove < manager.getBuildings().size()) {
                        Building buildingToRemove = manager.getBuildings().get(buildingIndexToRemove);
                        manager.removeBuilding(buildingToRemove);
                    } else {
                        System.out.println("Indice de bâtiment invalide.");
                    }
                    break;
                case 4:
                    System.out.println("Choisissez un bâtiment pour ajouter un habitant : ");
                    manager.showBuildings();
                    int buildingIndexToAddInhabitant = scanner.nextInt();
                    if (buildingIndexToAddInhabitant >= 0 && buildingIndexToAddInhabitant < manager.getBuildings().size()) {
                        Building buildingToAddInhabitant = manager.getBuildings().get(buildingIndexToAddInhabitant);
                        System.out.println("Nombre d'habitants à ajouter : ");
                        int inhabitantsToAdd = scanner.nextInt();
                        buildingToAddInhabitant.addInhabitant(inhabitantsToAdd);
                    } else {
                        System.out.println("Indice de bâtiment invalide.");
                    }
                    break;
                case 5:
                    System.out.println("Choisissez un bâtiment pour supprimer un habitant : ");
                    manager.showBuildings();
                    int buildingIndexToRemoveInhabitant = scanner.nextInt();
                    if (buildingIndexToRemoveInhabitant >= 0 && buildingIndexToRemoveInhabitant < manager.getBuildings().size()) {
                        Building buildingToRemoveInhabitant = manager.getBuildings().get(buildingIndexToRemoveInhabitant);
                        System.out.println("Nombre d'habitants à supprimer : ");
                        int inhabitantsToRemove = scanner.nextInt();
                        buildingToRemoveInhabitant.removeInhabitant(inhabitantsToRemove);
                    } else {
                        System.out.println("Indice de bâtiment invalide.");
                    }
                    break;
                case 6:
                    // Afficher la liste des bâtiments avec leur production, consommation et le nombre d'habitants
                    System.out.println("Liste des bâtiments :");
                    for (int i = 0; i < manager.getBuildings().size(); i++) {
                        Building building = manager.getBuildings().get(i);
                        System.out.println("Bâtiment " + (i + 1) + ": " +
                                "Population: " + building.getPopulation() + "/" + building.getPopulationLimit() +
                                ", Consommation: " + building.getResourceConsumption() +
                                ", Production: " + building.getResourceProduction());
                    }
                    break;
                case 0:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Choix invalide.");
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
