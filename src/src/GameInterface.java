import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class GameInterface extends JFrame {
    private JPanel buildingsPanel;
    private JPanel gridPanel;
    private Map<String, Color> buildingColors;
    private JPanel resourcesPanel;
    private Map<String, Resource> resources;
    private static Map<String, JLabel> resourceLabels;

    public GameInterface() {
        setTitle("FOE V2 - La concu de Rise of Kingdom");
        setSize(1300, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        buildingsPanel = new JPanel();
        gridPanel = new JPanel();

        add(buildingsPanel, BorderLayout.WEST);
        add(gridPanel, BorderLayout.CENTER);

        buildingColors = new HashMap<>();
        buildingColors.put("Maison", Color.BLUE);
        buildingColors.put("Ferme", Color.GREEN);
        buildingColors.put("Caserne", Color.RED);
        buildingColors.put("Carrière", Color.GRAY);
        buildingColors.put("Mine", Color.BLACK);
        buildingColors.put("Scierie", Color.ORANGE);
        buildingColors.put("Forge", Color.YELLOW);

        AtomicReference<String> selectedBuilding = new AtomicReference<>();
        for (String buildingName : BuildingFactory.getBuildingTypes()) {
            JButton buildingButton = new JButton(buildingName);
            buildingButton.addActionListener(e -> selectedBuilding.set(buildingName));
            buildingsPanel.add(buildingButton);
        }

        gridPanel.setLayout(new GridLayout(10, 10));
        for (int i = 0; i < 100; i++) {
            JButton gridButton = new JButton();
            gridButton.setOpaque(true);
            gridButton.setBorderPainted(true);
            gridButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            gridButton.addActionListener(e -> {
                if (selectedBuilding.get() != null && gridButton.getBackground().equals(new JButton().getBackground())) {
                    System.out.println("Un vrai Portugais ! " + selectedBuilding.get() + " est sur le terrain :)");
                    gridButton.setBackground(buildingColors.get(selectedBuilding.get()));
                    gridButton.repaint();
                }
            });
            gridPanel.add(gridButton);
        }

        resourcesPanel = new JPanel();
        add(resourcesPanel, BorderLayout.NORTH);

        resources = new HashMap<>();
        resources.put("Nourriture", new Resource("Nourriture", 400));
        resources.put("Bois", new Resource("Bois", 400));
        resources.put("Pierre", new Resource("Pierre", 400));
        resources.put("Charbon", new Resource("Charbon", 400));
        resources.put("Fer", new Resource("Fer", 200));
        resources.put("Acier", new Resource("Acier", 150));
        resources.put("Population", new Resource("Population", 20));

        resourceLabels = new HashMap<>();

        for (String resourceName : resources.keySet()) {
            JLabel resourceLabel = new JLabel(resourceName + ": " + resources.get(resourceName).getQuantity());
            resourcesPanel.add(resourceLabel);
            resourceLabels.put(resourceName, resourceLabel);
        }

        Timer timer = new Timer(1000, e -> {
            for (String resourceName : resources.keySet()) {
                resourceLabels.get(resourceName).setText(resourceName + ": " + resources.get(resourceName).getQuantity());
            }
        });
        timer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GameInterface().setVisible(true));

    }
}
