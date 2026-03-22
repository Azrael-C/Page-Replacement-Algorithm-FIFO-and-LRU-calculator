package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

@SuppressWarnings("serial")
public class HistoryFrame extends JFrame {
    private static final String SIM_DIR = "simulations";

    public HistoryFrame() {
    	setUndecorated(true);
        setTitle("History");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        JLabel back = new JLabel("<- Back to Menu");
        back.setFont(new Font("Poppins", Font.PLAIN, 20));
        back.setForeground(new Color(0, 51, 153));
        back.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        back.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                new MainMenuFrame().setVisible(true);
            }
        });

        JLabel title = new JLabel("History");
        title.setFont(new Font("Poppins", Font.BOLD, 40));
        title.setForeground(new Color(0, 51, 153));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        leftPanel.add(back);
        leftPanel.add(Box.createVerticalStrut(18));
        leftPanel.add(title);
        leftPanel.add(Box.createVerticalStrut(40));

        // Filter section
        JLabel chooseLabel = new JLabel("Choose Algorithm");
        chooseLabel.setFont(new Font("Poppins", Font.PLAIN, 18));
        chooseLabel.setForeground(new Color(0, 51, 153));
        chooseLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JComboBox<String> algoFilter = new JComboBox<>(new String[]{"All", "FIFO", "LRU"});
        algoFilter.setMaximumSize(new Dimension(300, 40));
        algoFilter.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel dateLabel = new JLabel("Date of Simulation (optional)");
        dateLabel.setFont(new Font("Poppins", Font.PLAIN, 18));
        dateLabel.setForeground(new Color(0, 51, 153));
        dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField dateField = new JTextField();
        dateField.setMaximumSize(new Dimension(300, 40));
        dateField.setAlignmentX(Component.LEFT_ALIGNMENT);
        dateField.setToolTipText("Enter year (YYYY), month (YYYY-MM), or day (YYYY-MM-DD)");

        leftPanel.add(chooseLabel);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(algoFilter);
        leftPanel.add(Box.createVerticalStrut(25));
        leftPanel.add(dateLabel);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(dateField);
        leftPanel.add(Box.createVerticalGlue());

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(new Color(0, 51, 153));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel listsTitle = new JLabel("Simulation History", SwingConstants.LEFT);
        listsTitle.setFont(new Font("Poppins", Font.BOLD, 34));
        listsTitle.setForeground(Color.WHITE);
        listsTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        rightPanel.add(listsTitle, BorderLayout.NORTH);

        JPanel cardsGrid = new JPanel();
        cardsGrid.setBackground(new Color(0, 51, 153));
        cardsGrid.setLayout(new GridLayout(0, 2, 25, 25));

        JScrollPane scrollPane = new JScrollPane(cardsGrid);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setResizeWeight(0.35);
        splitPane.setDividerSize(0);
        splitPane.setBorder(null);
        add(splitPane, BorderLayout.CENTER);

        loadCards(cardsGrid, algoFilter, dateField);

        ActionListener refresh = e -> loadCards(cardsGrid, algoFilter, dateField);
        algoFilter.addActionListener(refresh);
        dateField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                refresh.actionPerformed(null);
            }
        });

        setVisible(true);
    }

    private void loadCards(JPanel cardsGrid, JComboBox<String> algoFilter, JTextField dateField) {
        cardsGrid.removeAll();
        java.util.List<File> files = loadSimulationFiles();
        files.sort(Comparator.comparingLong(File::lastModified).reversed());

        String filterAlgo = (String) algoFilter.getSelectedItem();
        String dateText = dateField.getText().trim();

        for (File f : files) {
            Map<String, String> info = parseSimulationFile(f);
            if (info == null) { 
            	continue;
            }

            if (!"All".equals(filterAlgo) && !filterAlgo.equalsIgnoreCase(info.getOrDefault("Algorithm", ""))) {
                continue;
            }
            
            if (!dateText.isEmpty()) {
                String simDate = info.getOrDefault("Date", "");
                try {
                    LocalDateTime dt = LocalDateTime.parse(simDate);
                    String yearMonthDay = dt.toLocalDate().toString();
                    String yearMonth = yearMonthDay.substring(0, 7);
                    String year = yearMonthDay.substring(0, 4);

                    if (!year.equals(dateText) && !yearMonth.equals(dateText) && !yearMonthDay.equals(dateText)) {
                        continue;
                    }
                } catch (Exception e) {
                    if (!simDate.contains(dateText)) continue;
                }
            }

            cardsGrid.add(buildCard(info, f));
        }

        cardsGrid.revalidate();
        cardsGrid.repaint();
    }

    private java.util.List<File> loadSimulationFiles() {
        java.util.List<File> list = new ArrayList<>();
        File dir = new File(SIM_DIR);
        if (!dir.exists()) return list;
        File[] files = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".txt"));
        if (files != null) list.addAll(Arrays.asList(files));
        return list;
    }

    private Map<String, String> parseSimulationFile(File f) {
        Map<String, String> map = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                int idx = line.indexOf(':');
                if (idx > 0) {
                    map.put(line.substring(0, idx).trim(), line.substring(idx + 1).trim());
                }
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private JPanel buildCard(Map<String, String> info, File f) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(8, 8));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 51, 153), 3, true),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        JLabel dateLabel = new JLabel(formatDateForCard(info.getOrDefault("Date", f.getName())));
        dateLabel.setFont(new Font("Poppins", Font.BOLD, 16));
        dateLabel.setForeground(new Color(0, 51, 153));

        JLabel algoLabel = new JLabel("Algorithm: " + info.getOrDefault("Algorithm", "-"));
        JLabel framesLabel = new JLabel("Frames: " + info.getOrDefault("Frames", "-"));
        JLabel refLabel = new JLabel("Reference String: " + info.getOrDefault("References", "-"));

        for (JLabel l : new JLabel[]{algoLabel, framesLabel, refLabel}) {
            l.setFont(new Font("Poppins", Font.PLAIN, 14));
            l.setForeground(new Color(0, 51, 153));
        }

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.add(algoLabel);
        infoPanel.add(Box.createVerticalStrut(6));
        infoPanel.add(framesLabel);
        infoPanel.add(Box.createVerticalStrut(6));
        infoPanel.add(refLabel);

        card.add(dateLabel, BorderLayout.NORTH);
        card.add(infoPanel, BorderLayout.CENTER);

        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String algo = info.getOrDefault("Algorithm", "FIFO");
                int frames = Integer.parseInt(info.getOrDefault("Frames", "3"));
                String refs = info.getOrDefault("References", "");
                SwingUtilities.invokeLater(() -> {
                    StartSimulationFrame s = new StartSimulationFrame();
                    s.setVisible(true);
                    s.loadAndRun(algo, frames, refs);
                });
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(245, 247, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(Color.WHITE);
            }
        });

        return card;
    }

    private String formatDateForCard(String raw) {
        try {
            LocalDateTime dt = LocalDateTime.parse(raw);
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");
            return dt.format(fmt);
        } catch (Exception e) {
            return raw;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HistoryFrame().setVisible(true));
    }
}