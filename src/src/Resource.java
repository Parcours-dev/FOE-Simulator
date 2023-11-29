public class Resource {
    // Instance unique de la classe (singleton)
    private static Resource instance;

    // Nom de la ressource
    private String name;

    // Quantité de la ressource
    private int quantity;

    // Constructeur privé initialisant une ressource avec un nom et une quantité
    Resource(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    // Méthode statique pour obtenir l'instance unique de la classe (singleton)
    public static Resource getInstance(String name, int quantity) {
        if (instance == null) {
            instance = new Resource(name, quantity);
        }
        return instance;
    }

    // Méthode pour obtenir le nom de la ressource
    public String getName() {
        return name;
    }

    // Méthode pour définir le nom de la ressource
    public void setName(String name) {
        this.name = name;
    }

    // Méthode pour obtenir la quantité de la ressource
    public int getQuantity() {
        return quantity;
    }

    // Méthode pour définir la quantité de la ressource
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Méthode pour obtenir une représentation textuelle de la ressource
    @Override
    public String toString() {
        return "Resource{" +
                "name='" + name + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
