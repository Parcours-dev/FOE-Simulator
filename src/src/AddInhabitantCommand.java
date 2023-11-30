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

    }
}
