public class Resource {
    private static Resource instance;
    private String name;
    private int quantity;

    Resource(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public static Resource getInstance(String name, int quantity) {
        if (instance == null) {
            instance = new Resource(name, quantity);
        }
        return instance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "name='" + name + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
