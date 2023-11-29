public class AddInhabitantCommand implements Command {
    private final Building building;
    private final int inhabitantsToAdd;

    public AddInhabitantCommand(Building building, int inhabitantsToAdd) {
        this.building = building;
        this.inhabitantsToAdd = inhabitantsToAdd;
    }

    @Override
    public void execute() {
        // Ajouter le nombre d'habitants spécifié au bâtiment
        building.addInhabitant(inhabitantsToAdd);

        // Afficher un message pour informer de l'ajout d'habitants
        System.out.println(inhabitantsToAdd + " habitant(s) ajouté(s) au bâtiment.");
    }
}
