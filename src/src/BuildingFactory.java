public class BuildingFactory {
    public static Building createBuilding(String type) {
        switch (type) {
            case "Maison":
                return new Maison();
            case "Ferme":
                return new Ferme();
            case "Caserne":
                return new Caserne();
            case "Carrière":
                return new Carrière();
            case "Mine":
                return new Mine();
            case "Scierie":
                return new Scierie();
            /*case "Forge":
                return new Forge();
            case "Académie":
                return new Academie();*/
            default:
                return null;
        }
    }
}