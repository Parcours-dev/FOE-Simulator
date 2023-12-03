import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.Thread.sleep;

public class GameInterface extends JFrame {
    private static Manager manager;
    private static ResourceManager resourceManager = ResourceManager.getInstance();

    private JPanel buildingsPanel;
    private JPanel gridPanel;

    private JPanel resourcesPanel;
    private Map<String, Resource> resources;
    private static Map<String, JLabel> resourceLabels;

    private JPanel infoPanel;
    private JLabel productionLabel;
    private JLabel consumptionLabel;
    private JLabel populationLabel;
    private JButton addInhabitantButton;
    private JButton removeInhabitantButton;

    private JButton destroyButton;
    private AtomicReference<String> selectedBuilding = new AtomicReference<>();
    private boolean destroyMode = false;

    private JTable buildingTable;
    private DefaultTableModel buildingTableModel;
    Map<String, ImageIcon> buildingIcons = new HashMap<>();

    public GameInterface() {
        initializeGUI();
        manager = new Manager();
        initializeResources();
        setupResourceTimer();
        initializeBuildingIcons();

        removeInhabitantButton.addActionListener(e -> {
            destroyMode = !destroyMode;
            if (destroyMode) {
                System.out.println("Mode destruction activé. Sélectionnez un bâtiment à détruire.");
            } else {
                System.out.println("Mode destruction désactivé.");
            }
        });
    }

    private void initializeBuildingIcons() {
        buildingIcons.put("Maison", new ImageIcon(getClass().getResource("/image/maison.jpg")));
        buildingIcons.put("Ferme", new ImageIcon(getClass().getResource("/image/ferme.jpg")));
        buildingIcons.put("Caserne", new ImageIcon(getClass().getResource("/image/caserne.jpg")));
        buildingIcons.put("Cabanne en Bois", new ImageIcon(getClass().getResource("/image/cabane_bois.jpg")));
        buildingIcons.put("Carrière", new ImageIcon(getClass().getResource("/image/carriere.jpg")));
        buildingIcons.put("Mine", new ImageIcon(getClass().getResource("/image/mine.jpg")));
        buildingIcons.put("Scierie", new ImageIcon(getClass().getResource("/image/scierie.jpg")));
        buildingIcons.put("Forge", new ImageIcon(getClass().getResource("/image/forge.jpg")));
    }

    private void initializeGUI() {
        setTitle("Forge of Empires 2");
        setSize(1300, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        buildingsPanel = new JPanel();
        gridPanel = new JPanel();
        add(buildingsPanel, BorderLayout.PAGE_END);
        add(gridPanel, BorderLayout.CENTER);

        infoPanel = new JPanel(new GridLayout(5, 1));
        productionLabel = new JLabel("Production: ");
        consumptionLabel = new JLabel("Consommation: ");
        populationLabel = new JLabel("Population: ");
        addInhabitantButton = new JButton("Ajouter Habitant");
        addInhabitantButton.setEnabled(false);
        removeInhabitantButton = new JButton("Supprimer Habitant");
        removeInhabitantButton.setEnabled(false);
        destroyButton = new JButton("Détruire un bâtiment");

        infoPanel = new JPanel(new GridLayout(5, 1));
        ((GridLayout) infoPanel.getLayout()).setVgap(30);
        ((GridLayout) infoPanel.getLayout()).setHgap(30);

        infoPanel.add(productionLabel);
        infoPanel.add(consumptionLabel);
        infoPanel.add(populationLabel);
        infoPanel.add(addInhabitantButton);
        infoPanel.add(removeInhabitantButton);
        infoPanel.add(destroyButton);
        // infoPanel.setPreferredSize(new Dimension(200, getHeight()));
        // add(infoPanel, BorderLayout.LINE_START);
        buildingTable = createBuildingTable();

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(infoPanel, BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(buildingTable), BorderLayout.CENTER);
        add(leftPanel, BorderLayout.LINE_START);
        leftPanel.setBorder(new EmptyBorder(0, 10, 0, 10));

        for (String buildingName : BuildingFactory.getBuildingTypes()) {
            JButton buildingButton = new JButton(buildingName);
            buildingButton.addActionListener(e -> selectedBuilding.set(buildingName));
            buildingsPanel.add(buildingButton);
        }

        gridPanel.setLayout(new GridLayout(10, 10));
        for (int i = 0; i < 100; i++) {
            JButton gridButton = new JButton();
            setupGridButton(gridButton);
            gridButton.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    if (gridButton.getIcon() != null) {
                        String buildingName = selectedBuilding.get();
                        ImageIcon originalIcon = buildingIcons.get(buildingName);
                        Image scaledImage = originalIcon.getImage().getScaledInstance(gridButton.getWidth(),
                                gridButton.getHeight(), Image.SCALE_SMOOTH);
                        gridButton.setIcon(new ImageIcon(scaledImage));
                    }
                }
            });

            gridPanel.add(gridButton);
        }

        resourcesPanel = new JPanel();
        add(resourcesPanel, BorderLayout.PAGE_START);
        initializeResourceLabels();

        buildingTable = createBuildingTable();
        // add(buildingTable, BorderLayout.LINE_START);

    }

    private void setupGridButton(JButton gridButton) {
        Map<JButton, Building> buttonBuildingMap = new HashMap<>();

        gridButton.setOpaque(true);
        gridButton.setBorderPainted(true);
        gridButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        gridButton.addActionListener(e -> {
            if (destroyMode && gridButton.getIcon() != null) {
                Building building = buttonBuildingMap.get(gridButton);
                if (building != null) {
                    manager.removeBuilding(building);
                    gridButton.setIcon(null);
                    clearBuildingInfo();
                    System.out.println("Le bâtiment a été enlevé du terrain.");
                    System.out.println(manager.getBuildings());
                    addInhabitantButton.setEnabled(false);
                    removeInhabitantButton.setEnabled(false);
                    buttonBuildingMap.remove(gridButton);
                }
            } else if (selectedBuilding.get() != null && gridButton.getIcon() == null) {
                System.out.println(selectedBuilding.get() + " est sur le terrain :)");
                ImageIcon originalIcon = buildingIcons.get(selectedBuilding.get());
                Image scaledImage = originalIcon.getImage().getScaledInstance(gridButton.getWidth(),
                        gridButton.getHeight(), Image.SCALE_DEFAULT);
                gridButton.setIcon(new ImageIcon(scaledImage));
                Building building = BuildingFactory.createBuilding(selectedBuilding.get());
                manager.addBuilding(building);
                building.build();
                updateBuildingInfo(building);
                addInhabitantButton.setEnabled(true);
                removeInhabitantButton.setEnabled(true);
                buttonBuildingMap.put(gridButton, building);
            } else if (gridButton.getIcon() != null) {
                Building building = buttonBuildingMap.get(gridButton);
                if (building != null) {
                    updateBuildingInfo(building);
                    addInhabitantButton.setEnabled(true);
                    removeInhabitantButton.setEnabled(true);
                }
            }
        });
    }

    private JTable createBuildingTable() {
        String[] columnNames = { "Bâtiment", "Nb habitants/travailleurs", "Mat constructions",
                "Tps de construction (en ms)" };
        buildingTableModel = new DefaultTableModel(null, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(buildingTableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Ajout de styles personnalisés
        table.setFont(new Font("Serif", Font.PLAIN, 10)); // Change la police et la taille du texte
        table.setRowHeight(30); // Change la hauteur des lignes
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13)); // Change la police et la taille du texte
        // de l'en-tête
        table.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Ajoute une bordure noire autour du tableau

        // Ajout du renderer pour les tooltips des cellules
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (c instanceof JComponent) {
                    JComponent jc = (JComponent) c;
                    jc.setToolTipText(value.toString());
                }
                return c;
            }
        };
        table.setDefaultRenderer(Object.class, cellRenderer);

        // Récupération du renderer par défaut de l'en-tête
        TableCellRenderer defaultHeaderRenderer = table.getTableHeader().getDefaultRenderer();

        // Création du renderer pour les tooltips des en-têtes
        TableCellRenderer headerRenderer = new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component c = defaultHeaderRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus,
                        row, column);
                if (c instanceof JComponent) {
                    JComponent jc = (JComponent) c;
                    jc.setToolTipText(value.toString());
                }
                return c;
            }
        };

        // Application du renderer aux en-têtes
        JTableHeader header = table.getTableHeader();
        for (int i = 0; i < table.getModel().getColumnCount(); i++) {
            header.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

        for (String buildingName : BuildingFactory.getBuildingTypes()) {
            Building building = BuildingFactory.createBuilding(buildingName);
            Object[] rowData = {
                    buildingName,
                    building.getPopulationLimit(),
                    formatMaterials(building.getResourceCosts()),
                    building.gettConstruction()
            };
            buildingTableModel.addRow(rowData);
        }

        return table;
    }

    private String formatMaterials(Map<String, Integer> materials) {
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, Integer> entry : materials.entrySet()) {
            result.append(entry.getValue()).append(" ").append(entry.getKey()).append("  ");
        }
        return result.toString();
    }

    private void initializeResources() {
        ResourceManager resourceManager = ResourceManager.getInstance();
        resources = new HashMap<>();
        resources.put(resourceManager.getResource("Nourriture").getName(), resourceManager.getResource("Nourriture"));
        resources.put(resourceManager.getResource("Bois").getName(), resourceManager.getResource("Bois"));
        resources.put(resourceManager.getResource("Pierre").getName(), resourceManager.getResource("Pierre"));
        resources.put(resourceManager.getResource("Charbon").getName(), resourceManager.getResource("Charbon"));
        resources.put(resourceManager.getResource("Fer").getName(), resourceManager.getResource("Fer"));
        resources.put(resourceManager.getResource("Acier").getName(), resourceManager.getResource("Acier"));
        resources.put(resourceManager.getResource("Population").getName(), resourceManager.getResource("Population"));

        resourceLabels = new HashMap<>();
        for (String resourceName : resources.keySet()) {
            JLabel resourceLabel = new JLabel(resourceName + ": " + resources.get(resourceName).getQuantity());
            resourcesPanel.add(resourceLabel);
            resourceLabels.put(resourceName, resourceLabel);
        }

        destroyButton.addActionListener(e -> {
            destroyMode = !destroyMode;
            addInhabitantButton.setEnabled(false);
            removeInhabitantButton.setEnabled(false);
            if (destroyMode) {
                System.out.println("Mode destruction activé. Sélectionnez un bâtiment à détruire.");
            } else {
                System.out.println("Mode destruction désactivé.");
            }

            selectedBuilding.set(null);
        });

        infoPanel.add(destroyButton);
    }

    private void initializeResourceLabels() {
        Timer timer = new Timer(1000, e -> updateResourceLabels(resourceManager.getResources()));
        timer.start();
    }

    public static void updateResourceLabels(Map<String, Resource> resources) {
        SwingUtilities.invokeLater(() -> {
            for (String resourceName : resources.keySet()) {
                resourceLabels.get(resourceName)
                        .setText(resourceName + ": " + resources.get(resourceName).getQuantity());
            }
        });
    }

    private void updateBuildingInfo(Building building) {
        productionLabel.setText("Production: " + building.getResourceProduction());
        consumptionLabel.setText("Consommation: " + building.getResourceConsumption());
        populationLabel.setText("Population: " + building.getPopulation());

        for (ActionListener listener : addInhabitantButton.getActionListeners()) {
            addInhabitantButton.removeActionListener(listener);
        }
        for (ActionListener listener : removeInhabitantButton.getActionListeners()) {
            removeInhabitantButton.removeActionListener(listener);
        }

        addInhabitantButton.addActionListener(e -> addInhabitant(building));
        removeInhabitantButton.addActionListener(e -> removeInhabitant(building));
    }

    private void clearBuildingInfo() {
        productionLabel.setText("Production: ");
        consumptionLabel.setText("Consommation: ");
        populationLabel.setText("Population: ");

        for (ActionListener listener : addInhabitantButton.getActionListeners()) {
            addInhabitantButton.removeActionListener(listener);
        }
        for (ActionListener listener : removeInhabitantButton.getActionListeners()) {
            removeInhabitantButton.removeActionListener(listener);
        }
    }

    private void addInhabitant(Building building) {
        building.addInhabitant(1);
        updateBuildingInfo(building);
    }

    private void removeInhabitant(Building building) {
        building.removeInhabitant(1);
        updateBuildingInfo(building);
    }

    private void setupResourceTimer() {
        Thread resourceThread = new Thread(() -> {
            while (true) {
                manager.manageResources();
                updateResourceLabels(resourceManager.getResources());
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        resourceThread.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GameInterface().setVisible(true));
    }
}
