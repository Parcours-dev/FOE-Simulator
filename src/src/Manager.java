import java.util.*;


public class Manager implements Observer, Observable {
    // Liste des bâtiments gérés par le manager
    private List<Building> buildings;
    // Liste des observateurs du manager
    private List<Observer> observers;

    // Constructeur du manager
    public Manager() {
        // Initialisation des listes
        buildings = new ArrayList<>();
        observers = new ArrayList<>();
    }

    // Méthode pour ajouter un bâtiment au manager
    public void addBuilding(Building building) {
        buildings.add(building);
        building.addObserver(this);
    }

    // Méthode pour supprimer un bâtiment
    public void removeBuilding(Building building) {
        // Récupérer la population du bâtiment avant la destruction
        int buildingPopulation = building.getPopulation();
        ResourceManager resourceManager = ResourceManager.getInstance();

        // Restituer la population à la population disponible dans la ville
        ResourceManager.getInstance().produceResource("Population", buildingPopulation);

        // Restituer les ressources de la construction
        Map<String, Integer> constructionCosts = building.getResourceCosts();
        for (Map.Entry<String, Integer> entry : constructionCosts.entrySet()) {
            String resourceName = entry.getKey();
            int cost = entry.getValue();
            resourceManager.produceResource(resourceName, cost);
        }

        // Supprimer le bâtiment de la liste des bâtiments
        buildings.remove(building);
        building.removeObserver(this);
    }

    // Méthode pour gérer les ressources des bâtiments
    public void manageResources() {
        // Parcourir tous les bâtiments
        for (Building building : buildings) {
            // Appliquer la consommation de population et de ressources
            building.populationConsumption(buildings);
            building.consumeResources();
            building.produceResources();
        }
        // Notifier les observateurs du manager
        notifyObservers();
    }

    // Méthode pour afficher la liste des bâtiments
    public void showBuildings() {
        System.out.println("Liste des bâtiments :");
        for (int i = 0; i < buildings.size(); i++) {
            Building building = buildings.get(i);
            System.out.println(i + 1 + ". " + building.getType());
        }
    }

    // Méthode pour obtenir la liste des bâtiments
    public List<Building> getBuildings() {
        return buildings;
    }

    // Méthode de mise à jour en tant qu'observateur
    @Override
    public void update() {
        // Logique à exécuter en réponse à une mise à jour d'un bâtiment
    }

    // Méthode pour afficher le menu principal du jeu
    static void printMenu() {
        System.out.println("Choisissez une action :");
        System.out.println("1. Afficher les ressources");
        System.out.println("2. Construire un bâtiment");
        System.out.println("3. Détruire un bâtiment");
        System.out.println("4. Ajouter un habitant à un bâtiment");
        System.out.println("5. Supprimer un habitant d'un bâtiment");
        System.out.println("6. Afficher la liste des bâtiments");
        System.out.println("0. Quitter");
    }

    // Méthode pour choisir un bâtiment à construire
    static void chooseBuilding(Manager manager, Scanner scanner) {
        System.out.println("Choisissez un bâtiment à construire : ");

        // Affichez la liste des types de bâtiments avec des indices numériques
        List<String> buildingTypes = BuildingFactory.getBuildingTypes();
        for (int i = 0; i < buildingTypes.size(); i++) {
            System.out.println((i + 1) + ". " + buildingTypes.get(i) + " - " + BuildingFactory.getBuildingCost(buildingTypes.get(i)));
        }

        // Demandez à l'utilisateur de choisir un indice
        try {
            System.out.print("Entrez le numéro du bâtiment : ");
            int buildingIndex = scanner.nextInt();

            // Vérifiez si l'indice est valide
            if (buildingIndex >= 1 && buildingIndex <= buildingTypes.size()) {
                // Obtenez le type de bâtiment correspondant à l'indice
                String buildingType = buildingTypes.get(buildingIndex - 1);

                // Créez et exécutez la commande pour ajouter le bâtiment
                Command addBuildingCommand = new AddBuildingCommand(manager, buildingType);
                addBuildingCommand.execute();
            } else {
                throw new GameExceptions.InvalidBuildingIndexException();
            }
        } catch (InputMismatchException e) {
            System.out.println("Veuillez entrer un nombre valide.");
            scanner.nextLine(); // Pour consommer la nouvelle ligne restante
        } catch (GameExceptions.InvalidBuildingIndexException e) {
            System.out.println(e.getMessage());
        }
    }


    // Méthode pour vérifier si un indice de bâtiment est valide
    private static boolean isValidBuildingIndex(Manager manager, int index) {
        return index >= 1 && index <= manager.getBuildings().size();
    }

    // Méthode pour détruire un bâtiment
    static void destroyBuilding(Manager manager, Scanner scanner) {
        System.out.println("Choisissez un bâtiment à détruire : ");
        manager.showBuildings();

        try {
            int buildingIndexToRemove = scanner.nextInt();

            if (isValidBuildingIndex(manager, buildingIndexToRemove)) {
                Building buildingToRemove = manager.getBuildings().get(buildingIndexToRemove - 1);
                Command removeBuildingCommand = new RemoveBuildingCommand(manager, buildingToRemove);
                removeBuildingCommand.execute();
            } else {
                throw new GameExceptions.InvalidBuildingIndexException();
            }
        } catch (InputMismatchException e) {
            System.out.println("Veuillez entrer un nombre valide.");
            scanner.nextLine(); // Pour consommer la nouvelle ligne restante
        } catch (GameExceptions.InvalidBuildingIndexException e) {
            System.out.println(e.getMessage());
        }
    }

    // Méthode pour ajouter un habitant à un bâtiment
    static void addInhabitant(Manager manager, Scanner scanner) {
        System.out.println("Choisissez un bâtiment pour ajouter un habitant : ");
        manager.showBuildings();

        try {
            int buildingIndexToAddInhabitant = scanner.nextInt();

            if (isValidBuildingIndex(manager, buildingIndexToAddInhabitant)) {
                Building buildingToAddInhabitant = manager.getBuildings().get(buildingIndexToAddInhabitant - 1);
                System.out.println("Nombre d'habitants à ajouter : ");

                try {
                    int inhabitantsToAdd = scanner.nextInt();

                    if (inhabitantsToAdd >= 0) {
                        Command addInhabitantCommand = new AddInhabitantCommand(buildingToAddInhabitant, inhabitantsToAdd);
                        addInhabitantCommand.execute();
                    } else {
                        throw new GameExceptions.InvalidInhabitantCountException();
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Veuillez entrer un nombre valide.");
                    scanner.nextLine(); // Pour consommer la nouvelle ligne restante
                } catch (GameExceptions.InvalidInhabitantCountException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                throw new GameExceptions.InvalidBuildingIndexException();
            }
        } catch (InputMismatchException e) {
            System.out.println("Veuillez entrer un nombre valide pour l'indice de bâtiment.");
            scanner.nextLine(); // Pour consommer la nouvelle ligne restante
        } catch (GameExceptions.InvalidBuildingIndexException e) {
            System.out.println(e.getMessage());
        }
    }

    // Méthode pour supprimer un habitant d'un bâtiment
    static void removeInhabitant(Manager manager, Scanner scanner) {
        System.out.println("Choisissez un bâtiment pour supprimer un habitant : ");
        manager.showBuildings();

        try {
            int buildingIndexToRemoveInhabitant = scanner.nextInt();

            if (isValidBuildingIndex(manager, buildingIndexToRemoveInhabitant)) {
                Building buildingToRemoveInhabitant = manager.getBuildings().get(buildingIndexToRemoveInhabitant - 1);
                System.out.println("Nombre d'habitants à supprimer : ");

                try {
                    int inhabitantsToRemove = scanner.nextInt();

                    if (inhabitantsToRemove >= 0) {
                        Command removeInhabitantCommand = new RemoveInhabitantCommand(buildingToRemoveInhabitant, inhabitantsToRemove);
                        removeInhabitantCommand.execute();
                    } else {
                        throw new GameExceptions.InvalidInhabitantCountException();
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Veuillez entrer un nombre valide.");
                    scanner.nextLine(); // Pour consommer la nouvelle ligne restante
                } catch (GameExceptions.InvalidInhabitantCountException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                throw new GameExceptions.InvalidBuildingIndexException();
            }
        } catch (InputMismatchException e) {
            System.out.println("Veuillez entrer un nombre valide pour l'indice de bâtiment.");
            scanner.nextLine(); // Pour consommer la nouvelle ligne restante
        } catch (GameExceptions.InvalidBuildingIndexException e) {
            System.out.println(e.getMessage());
        }
    }

    // Méthode pour afficher la liste des bâtiments avec leurs détails
    static void showBuildingList(Manager manager) {
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

    // Méthode pour quitter le jeu
    static void exitGame() {
        System.out.println("Merci d'avoir joué!");
        System.exit(0);
    }

    // Méthode pour afficher le message de fin de jeu
    static void gameOver() {
        System.out.println("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀ ⣀⣀⠀⠀⢀⣤⣤⣤⣶⣶⣷⣤⣀\n" +
                "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣶⣶⣶⠀⠀⠀⠀⣠⣾⣿⣿⡇⠀⣿⣿⣿⣿⠿⠛⠉⠉⠀\n" +
                "⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⣀⠀⠀⠀⠀⠀⢀⣿⣿⣶⡀⠀⠀⠀⠀⠀⣾⣿⣿⣿⡄⠀⢀⣴⣿⣿⣿⣿⠁⢸⣿⣿⣿⣀⣤⡀⠀⠀⠀\n" +
                "⠀⠀⠀⠀⠀⣠⣴⣶⣿⣿⣿⣿⣿⣷⠀⠀⠀⠀⣼⣿⣿⣿⣧⠀⠀⠀⠀⢰⣿⣿⣿⣿⣇⣠⣿⣿⣿⣿⣿⡏⢠⣿⣿⣿⣿⣿⡿⠗⠂⠀⠀\n" +
                "⠀⠀⠀⣰⣾⣿⣿⠟⠛⠉⠉⠉⠉⠋⠀⠀⠀⣰⣿⣿⣿⣿⣿⣇⣠⣤⣤⣿⣿⣿⢿⣿⣿⣿⣿⢿⣿⣿⡿⠀⣼⣿⣿⡟⠉⠁⢀⣀⡄⠀⠀\n" +
                "⠀⢀⣾⣿⡿⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢰⣿⣿⣿⣴⣿⣿⣿⣿⡿⣿⣿⣿⡏⠈⢿⣿⣿⠏⣾⣿⣿⠃⢠⣿⣿⣿⣶⣶⣿⣿⣿⡷⠦⠀\n" +
                "⢠⣾⣿⡿⠀⠀⠀⣀⣠⣴⣶⣿⣿⡷⠀⣠⣿⣿⣿⣿⡿⠟⣿⣿⣿⣠⣿⣿⣿⠀⠀⠀⠀⠁⢸⣿⣿⡏⠀⣼⣿⣿⣿⠿⠛⠛⠉⠀⠀⠀⠀\n" +
                "⢸⣿⣿⠣⣴⣾⣿⣿⣿⣿⣿⣿⡿⠃⣰⣿⣿⣿⠋⠁⠀⠀⠸⣿⣿⣿⣿⣿⠃⠀⠀⠀⠀⠀⠸⠿⠿⠀⠀⠛⠛⠟⠀⠀⠀⠀⠀⠀⠀⠀⠀\n" +
                "⠸⣿⣿⣆⣉⣻⣭⣿⣿⣿⡿⠋⠀⠀⢿⣿⡿⠁⠀⠀⠀⠀⠀⠹⠟⠛⠛⠛⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n" +
                "⠀⠙⠿⣿⣿⣿⣿⡿⠟⠋⠀⠀⠀⠀⠀⠈⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n" +
                "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣤⣤⣶⣶⣶⣶⣦⣄⠀⠀\n" +
                "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣷⠄⣤⣤⣤⣤⣶⣾⣷⣴⣿⣿⣿⣿⠿⠿⠛⣻⣿⣿⣷⡄\n" +
                "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣠⣤⣄⠀⣶⣶⣤⡀⠀⠀⠀⠀⠀⠀⢀⣴⣿⠋⢠⣿⣿⣿⠿⠛⠋⠉⠛⣿⣿⣿⠏⢀⣤⣾⣿⣿⡿⠋⠀\n" +
                "⠀⠀⠀⠀⠀⠀⠀⠀⣠⣴⣾⣿⣿⣿⣿⠓⢹⣿⣿⣷⠀⠀⠀⠀⢀⣶⣿⡿⠁⠀⣾⣿⣿⣟⣠⣤⠀⠀⢸⣿⣿⣿⣾⣿⣿⣿⡟⠋⠀⠀⠀\n" +
                "⠀⠀⠀⠀⠀⠀⣠⣾⣿⣿⡿⠛⠉⠸⣿⣦⡈⣿⣿⣿⡇⠀⠀⣰⣿⣿⡿⠁⠀⢸⣿⣿⣿⣿⣿⠿⠷⢀⣿⣿⣿⣿⡿⠛⣿⣿⣿⡀⠀⠀⠀\n" +
                "⠀⠀⠀⠀⢀⣼⣿⣿⡿⠋⠀⠀⠀⠀⣿⣿⣧⠘⣿⣿⣿⡀⣼⣿⣿⡟⠀⠀⢀⣿⣿⣿⠋⠁⠀⣀⣀⣼⣿⣿⡟⠁⠀⠀⠘⣿⣿⣧⠀⠀⠀\n" +
                "⠀⠀⠀⠀⣼⣿⣿⡟⠀⠀⠀⠀⠀⣠⣿⣿⣿⠀⢹⣿⣿⣿⣿⣿⡟⠀⠀⠀⣼⣿⣿⣷⣶⣿⣿⣿⣿⣿⣿⡟⠀⠀⠀⠀⠀⠸⣿⣿⡆⠀⠀\n" +
                "⠀⠀⠀⠀⢹⣿⣿⣇⠀⠀⢀⣠⣴⣿⣿⣿⡿⠀⠈⣿⣿⣿⣿⡟⠀⠀⠀⢰⣿⣿⣿⠿⠟⠛⠉⠁⠸⢿⡟⠀⠀⠀⠀⠀⠀⠀⠘⠋⠁⠀⠀\n" +
                "⠀⠀⠀⠀⠈⢻⣿⣿⣿⣾⣿⣿⣿⣿⣿⠟⠁⠀⠀⠸⣿⣿⡿⠁⠀⠀⠀⠈⠙⠛⠃⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n" +
                "⠀⠀⠀⠀⠀⠀⠉⠛⠿⠿⠿⠿⠟⠋⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
        System.exit(0);
    }



}
