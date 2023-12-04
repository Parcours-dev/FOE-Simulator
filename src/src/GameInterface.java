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
    // Gestionnaire principal du jeu
    private static Manager manager;

    // Gestionnaire des ressources du jeu
    private static ResourceManager resourceManager = ResourceManager.getInstance();

    // Panneaux de l'interface
    private JPanel buildingsPanel;
    private JPanel gridPanel;
    private JPanel resourcesPanel;

    // Ressources du jeu
    private Map<String, Resource> resources;
    private static Map<String, JLabel> resourceLabels;

    // Informations sur le bâtiment sélectionné
    private JPanel infoPanel;
    private JLabel productionLabel;
    private JLabel consumptionLabel;
    private JLabel populationLabel;
    private JButton addInhabitantButton;
    private JButton removeInhabitantButton;
    private JButton destroyButton;
    private AtomicReference<String> selectedBuilding = new AtomicReference<>();
    private boolean destroyMode = false;

    // Tableau des bâtiments
    private JTable buildingTable;
    private DefaultTableModel buildingTableModel;
    Map<String, ImageIcon> buildingIcons = new HashMap<>();

    /**
     * Constructeur de la classe GameInterface.
     * Initialise l'interface graphique, le gestionnaire, les ressources et les icônes des bâtiments.
     */
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

    /**
     * Initialise les icônes des bâtiments à utiliser dans l'interface.
     */
    private void initializeBuildingIcons() {
        // Chargement des icônes depuis les fichiers
        buildingIcons.put("Maison", new ImageIcon(getClass().getResource("/image/maison.jpg")));
        buildingIcons.put("Ferme", new ImageIcon(getClass().getResource("/image/ferme.jpg")));
        buildingIcons.put("Caserne", new ImageIcon(getClass().getResource("/image/caserne.jpg")));
        buildingIcons.put("Cabanne en Bois", new ImageIcon(getClass().getResource("/image/cabane_bois.jpg")));
        buildingIcons.put("Carrière", new ImageIcon(getClass().getResource("/image/carriere.jpg")));
        buildingIcons.put("Mine", new ImageIcon(getClass().getResource("/image/mine.jpg")));
        buildingIcons.put("Scierie", new ImageIcon(getClass().getResource("/image/scierie.jpg")));
        buildingIcons.put("Forge", new ImageIcon(getClass().getResource("/image/forge.jpg")));
    }

    /**
     * Initialise l'interface graphique en définissant les composants et leur disposition.
     */
    private void initializeGUI() {
        setTitle("Forge of Empires 2");
        setSize(1300, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialisation des panneaux
        buildingsPanel = new JPanel();
        gridPanel = new JPanel();
        add(buildingsPanel, BorderLayout.PAGE_END);
        add(gridPanel, BorderLayout.CENTER);

        // Initialisation du panneau d'informations sur les ressources
        infoPanel = new JPanel(new GridLayout(5, 1));
        productionLabel = new JLabel("Production: ");
        consumptionLabel = new JLabel("Consommation: ");
        populationLabel = new JLabel("Population: ");
        addInhabitantButton = new JButton("Ajouter Habitant");
        addInhabitantButton.setEnabled(false);
        removeInhabitantButton = new JButton("Supprimer Habitant");
        removeInhabitantButton.setEnabled(false);
        destroyButton = new JButton("Détruire un bâtiment");

        // Ajout des composants au panneau d'informations
        infoPanel = new JPanel(new GridLayout(5, 1));
        ((GridLayout) infoPanel.getLayout()).setVgap(30);
        ((GridLayout) infoPanel.getLayout()).setHgap(30);
        infoPanel.add(productionLabel);
        infoPanel.add(consumptionLabel);
        infoPanel.add(populationLabel);
        infoPanel.add(addInhabitantButton);
        infoPanel.add(removeInhabitantButton);
        infoPanel.add(destroyButton);

        // Création du tableau des bâtiments
        buildingTable = createBuildingTable();

        // Configuration du panneau gauche
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(infoPanel, BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(buildingTable), BorderLayout.CENTER);
        add(leftPanel, BorderLayout.LINE_START);
        leftPanel.setBorder(new EmptyBorder(0, 10, 0, 10));

        // Ajout des boutons de construction de bâtiments
        for (String buildingName : BuildingFactory.getBuildingTypes()) {
            JButton buildingButton = new JButton(buildingName);
            buildingButton.addActionListener(e -> selectedBuilding.set(buildingName));
            buildingsPanel.add(buildingButton);
        }

        // Configuration du panneau de la grille
        gridPanel.setLayout(new GridLayout(10, 10));
        for (int i = 0; i < 100; i++) {
            JButton gridButton = new JButton();
            setupGridButton(gridButton);
            gridButton.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    if (gridButton.getIcon() != null) {
                        ImageIcon originalIcon = (ImageIcon) gridButton.getIcon();
                        Image scaledImage = originalIcon.getImage().getScaledInstance(gridButton.getWidth(),
                                gridButton.getHeight(), Image.SCALE_SMOOTH);
                        gridButton.setIcon(new ImageIcon(scaledImage));
                    }
                }
            });

            gridPanel.add(gridButton);
        }

        // Configuration du panneau des ressources
        resourcesPanel = new JPanel();
        add(resourcesPanel, BorderLayout.PAGE_START);
        initializeResourceLabels();
    }

    /**
     * Configure un bouton de la grille pour la construction ou la destruction de bâtiments.
     *
     * @param gridButton Bouton de la grille à configurer.
     */
    private void setupGridButton(JButton gridButton) {
        Map<JButton, Building> buttonBuildingMap = new HashMap<>();

        // Paramètres d'apparence du bouton
        gridButton.setOpaque(true);
        gridButton.setBorderPainted(true);
        gridButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        // Actionneur du bouton en fonction du mode (construction ou destruction)
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
                Image scaledImage = originalIcon.getImage().getScaledInstance(gridButton.getWidth(), gridButton.getHeight(), Image.SCALE_DEFAULT);
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

    /**
     * Crée un tableau affichant les informations sur les bâtiments.
     *
     * @return Tableau des bâtiments.
     */
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

        // Ajout de styles personnalisés au tableau
        table.setFont(new Font("Serif", Font.PLAIN, 10)); // Change la police et la taille du texte
        table.setRowHeight(30); // Change la hauteur des lignes
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13)); // Change la police et la taille du texte
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

        // Ajout des bâtiments au tableau
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

    /**
     * Formatte les matériaux de construction d'un bâtiment en une chaîne de caractères.
     *
     * @param materials Matériaux de construction du bâtiment.
     * @return Chaîne de caractères représentant les matériaux.
     */
    private String formatMaterials(Map<String, Integer> materials) {
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, Integer> entry : materials.entrySet()) {
            result.append(entry.getValue()).append(" ").append(entry.getKey()).append("  ");
        }
        return result.toString();
    }

    /**
     * Initialise les ressources du jeu.
     */
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

        // Ajout de l'actionneur pour le mode destruction
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

    /**
     * Initialise les labels de ressources avec un minuteur pour les mettre à jour périodiquement.
     */
    private void initializeResourceLabels() {
        Timer timer = new Timer(1000, e -> updateResourceLabels(resourceManager.getResources()));
        timer.start();
    }


    // Méthode pour mettre à jour les étiquettes des ressources de manière asynchrone
    public static void updateResourceLabels(Map<String, Resource> resources) {
        SwingUtilities.invokeLater(() -> {
            for (String resourceName : resources.keySet()) {
                resourceLabels.get(resourceName)
                        .setText(resourceName + ": " + resources.get(resourceName).getQuantity());
            }
        });
    }

    // Méthode pour mettre à jour les informations d'un bâtiment sur l'interface graphique
    private void updateBuildingInfo(Building building) {
        productionLabel.setText("Production: " + building.getResourceProduction());
        consumptionLabel.setText("Consommation: " + building.getResourceConsumption());
        populationLabel.setText("Population: " + building.getPopulation());

        // Suppression des auditeurs actuels pour éviter les doublons
        for (ActionListener listener : addInhabitantButton.getActionListeners()) {
            addInhabitantButton.removeActionListener(listener);
        }
        for (ActionListener listener : removeInhabitantButton.getActionListeners()) {
            removeInhabitantButton.removeActionListener(listener);
        }

        // Ajout de nouveaux auditeurs pour les boutons d'ajout et de suppression d'habitants
        addInhabitantButton.addActionListener(e -> addInhabitant(building));
        removeInhabitantButton.addActionListener(e -> removeInhabitant(building));
    }

    // Méthode pour effacer les informations d'un bâtiment sur l'interface graphique
    private void clearBuildingInfo() {
        productionLabel.setText("Production: ");
        consumptionLabel.setText("Consommation: ");
        populationLabel.setText("Population: ");

        // Suppression des auditeurs actuels pour éviter les doublons
        for (ActionListener listener : addInhabitantButton.getActionListeners()) {
            addInhabitantButton.removeActionListener(listener);
        }
        for (ActionListener listener : removeInhabitantButton.getActionListeners()) {
            removeInhabitantButton.removeActionListener(listener);
        }
    }

    // Méthode pour ajouter un habitant à un bâtiment
    private void addInhabitant(Building building) {
        building.addInhabitant(1);
        updateBuildingInfo(building);
    }

    // Méthode pour supprimer un habitant d'un bâtiment
    private void removeInhabitant(Building building) {
        building.removeInhabitant(1);
        updateBuildingInfo(building);
    }

    // Méthode pour configurer le thread de gestion des ressources
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

    // Méthode pour afficher la consommation et la production de toutes les ressources
    public static void showResource() {
        for (Map.Entry<String, Resource> entry : resourceManager.getResources().entrySet()) {
            String resourceName = entry.getKey();
            int amount = entry.getValue().getQuantity();
            System.out.println(resourceName + " : " + amount);
        }
    }

    // Méthode principale pour lancer l'interface graphique du jeu
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GameInterface().setVisible(true));
    }
}