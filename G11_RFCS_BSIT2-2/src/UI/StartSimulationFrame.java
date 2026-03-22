package UI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SuppressWarnings("serial")
public class StartSimulationFrame extends JFrame {
    private JComboBox<String> algorithmDropdown;
    private JTextField framesField, referenceField;
    private DefaultTableModel tableModel;
    private JLabel[] summaryValues;

    private static final String SIM_DIR = "simulations";

    public StartSimulationFrame() {
        initGUI();
        setUndecorated(true);
    }

    public StartSimulationFrame(String algo, int frames, String refs) {
        initGUI();
        loadAndRun(algo, frames, refs);
    }

    private void initGUI() {
        setTitle("Page Replacement Simulator");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);
        
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("src/fonts/Poppins-Regular.ttf")));
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("src/fonts/Poppins-Medium.ttf")));
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("src/fonts/Poppins-Bold.ttf")));
        } catch (Exception e) {
            System.out.println("Poppins fonts not loaded, using defaults.");
        }

        Font titleFont = new Font("Poppins", Font.BOLD, 28);
        Font labelFont = new Font("Poppins", Font.PLAIN, 20);
        Font buttonFont = new Font("Poppins", Font.PLAIN, 18);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(40, 80, 40, 80));

        JLabel backLabel = new JLabel("<- Back to Menu");
        backLabel.setFont(new Font("Poppins", Font.PLAIN, 20));
        backLabel.setForeground(new Color(0, 51, 153));
        backLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                new MainMenuFrame().setVisible(true);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                backLabel.setForeground(new Color(0, 80, 220));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                backLabel.setForeground(new Color(0, 51, 153));
            }
        });
        leftPanel.add(backLabel, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JLabel algoLabel = new JLabel("Choose Page Replacement Algorithm");
        algoLabel.setFont(labelFont);
        algoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        algorithmDropdown = new JComboBox<>(new String[]{"FIFO", "LRU"});
        algorithmDropdown.setFont(buttonFont);
        algorithmDropdown.setMaximumSize(new Dimension(360, 45));
        algorithmDropdown.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel framesLabel = new JLabel("Enter Number of Frames");
        framesLabel.setFont(labelFont);
        framesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        framesField = new JTextField();
        framesField.setFont(buttonFont);
        framesField.setMaximumSize(new Dimension(360, 45));
        framesField.setHorizontalAlignment(JTextField.CENTER);
        framesField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel referenceLabel = new JLabel("Enter Reference String (space-separated)");
        referenceLabel.setFont(labelFont);
        referenceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        referenceField = new JTextField();
        referenceField.setFont(buttonFont);
        referenceField.setMaximumSize(new Dimension(360, 45));
        referenceField.setHorizontalAlignment(JTextField.CENTER);
        referenceField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton startBtn = createStyledButton("Run Simulation", new Color(0, 61, 165), Color.WHITE);
        JButton resetBtn = createStyledButton("Reset", new Color(240, 240, 240), new Color(0, 61, 165));

        inputPanel.add(Box.createVerticalGlue());
        inputPanel.add(algoLabel);
        inputPanel.add(Box.createVerticalStrut(10));
        inputPanel.add(algorithmDropdown);
        inputPanel.add(Box.createVerticalStrut(30));
        inputPanel.add(framesLabel);
        inputPanel.add(Box.createVerticalStrut(10));
        inputPanel.add(framesField);
        inputPanel.add(Box.createVerticalStrut(30));
        inputPanel.add(referenceLabel);
        inputPanel.add(Box.createVerticalStrut(10));
        inputPanel.add(referenceField);
        inputPanel.add(Box.createVerticalStrut(40));
        inputPanel.add(startBtn);
        inputPanel.add(Box.createVerticalStrut(12));
        inputPanel.add(resetBtn);
        inputPanel.add(Box.createVerticalGlue());
        leftPanel.add(inputPanel, BorderLayout.CENTER);

        add(leftPanel, BorderLayout.WEST);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(new Color(0, 51, 153));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(28, 36, 28, 36));

        JLabel titleLabel = new JLabel("Simulation Table", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(titleFont);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        rightPanel.add(titleLabel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel();
        JTable table = new JTable(tableModel);
        table.setRowHeight(46);
        table.setFont(new Font("Poppins", Font.PLAIN, 16));
        table.setEnabled(false);
        table.getTableHeader().setFont(new Font("Poppins", Font.BOLD, 16));
        table.getTableHeader().setBackground(Color.WHITE);
        table.getTableHeader().setForeground(Color.BLACK);
        table.setGridColor(new Color(220, 220, 220));
        table.setShowGrid(true);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.setBackground(Color.WHITE);
        tableWrapper.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        tableWrapper.add(scroll, BorderLayout.CENTER);
        rightPanel.add(tableWrapper, BorderLayout.CENTER);

        JPanel summaryPanel = new JPanel(new BorderLayout());
        summaryPanel.setOpaque(false);
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(16, 0, 0, 0));

        JLabel summaryTitle = new JLabel("Summary", SwingConstants.CENTER);
        summaryTitle.setFont(new Font("Poppins", Font.BOLD, 22));
        summaryTitle.setForeground(Color.WHITE);

        JPanel summaryGrid = new JPanel(new GridLayout(6, 2, 12, 8));
        summaryGrid.setOpaque(false);

        String[] labels = {"Algorithm:", "Frames:", "References:", "Unique Pages:", "Page Faults:", "Hits:"};
        summaryValues = new JLabel[labels.length];

        for (int i = 0; i < labels.length; i++) {
            JLabel l = new JLabel(labels[i]);
            l.setFont(new Font("Poppins", Font.PLAIN, 16));
            l.setForeground(Color.WHITE);
            summaryGrid.add(l);

            summaryValues[i] = new JLabel("-");
            summaryValues[i].setFont(new Font("Poppins", Font.PLAIN, 16));
            summaryValues[i].setForeground(Color.WHITE);
            summaryGrid.add(summaryValues[i]);
        }

        summaryPanel.add(summaryTitle, BorderLayout.NORTH);
        summaryPanel.add(summaryGrid, BorderLayout.CENTER);
        rightPanel.add(summaryPanel, BorderLayout.SOUTH);
        add(rightPanel, BorderLayout.CENTER);

        startBtn.addActionListener(e -> runSimulationAndSave());
        resetBtn.addActionListener(e -> {
            framesField.setText("");
            referenceField.setText("");
            tableModel.setRowCount(0);
            tableModel.setColumnCount(0);
            for (JLabel val : summaryValues) val.setText("-");
        });

        File simDir = new File(SIM_DIR);
        if (!simDir.exists()) simDir.mkdirs();
    }

    public void loadAndRun(String algo, int frames, String refs) {
        algorithmDropdown.setSelectedItem(algo);
        framesField.setText(String.valueOf(frames));
        referenceField.setText(refs);
        runSimulation(true);
    }

    private void runSimulationAndSave() {
        runSimulation(true);
    }

    private void runSimulation(boolean saveToFile) {
        String algo = (String) algorithmDropdown.getSelectedItem();
        String framesText = framesField.getText().trim();
        String refsText = referenceField.getText().trim();

        if (framesText.isEmpty() || refsText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int frames;
        
        try {
            frames = Integer.parseInt(framesText);
            if (frames <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number of frames.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] refs = refsText.split("\\s+");
        Vector<String> cols = new Vector<>();
        cols.add("");
        for (String r : refs) cols.add(r);
        tableModel.setDataVector(new Object[0][0], cols.toArray());

        String[][] frameStates = new String[frames][refs.length];
        boolean[] faults = new boolean[refs.length];

        if ("FIFO".equalsIgnoreCase(algo)) runFIFOLogic(refs, frames, frameStates, faults);
        else runLRULogic(refs, frames, frameStates, faults);

        tableModel.setRowCount(0);
        for (int f = 0; f < frames; f++) {
            Vector<Object> row = new Vector<>();
            row.add("fr" + (f + 1));
            for (int c = 0; c < refs.length; c++)
                row.add(frameStates[f][c] == null ? "" : frameStates[f][c]);
            tableModel.addRow(row);
        }

        Vector<Object> hitRow = new Vector<>();
        hitRow.add("Hit");
        int totalFaults = 0, totalHits = 0;
        for (boolean fault : faults) {
            if (fault) {
                hitRow.add("X");
                totalFaults++;
            } else {
                hitRow.add("/");
                totalHits++;
            }
        }
        tableModel.addRow(hitRow);

        int uniquePages = (int) Arrays.stream(refs).distinct().count();
        summaryValues[0].setText(algo);
        summaryValues[1].setText(String.valueOf(frames));
        summaryValues[2].setText(String.valueOf(refs.length));
        summaryValues[3].setText(String.valueOf(uniquePages));
        summaryValues[4].setText(String.valueOf(totalFaults));
        summaryValues[5].setText(String.valueOf(totalHits));

        if (saveToFile) saveSimulationToFile(algo, frames, refsText, totalFaults);
    }

    private void saveSimulationToFile(String algo, int frames, String references, int totalFaults) {
        try {
            File dir = new File(SIM_DIR);
            if (!dir.exists()) dir.mkdirs();

            DateTimeFormatter fm = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String filename = LocalDateTime.now().format(fm) + "_" + algo + ".txt";
            File f = new File(dir, filename);

            try (PrintWriter pw = new PrintWriter(new FileWriter(f))) {
                pw.println("Algorithm:" + algo);
                pw.println("Frames:" + frames);
                pw.println("References:" + references);
                pw.println("Date:" + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                pw.println("PageFaults:" + totalFaults);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to save simulation file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void runFIFOLogic(String[] refs, int frames, String[][] frameStates, boolean[] faults) {
        String[] memory = new String[frames];
        Arrays.fill(memory, "-");
        int pointer = 0;
        for (int i = 0; i < refs.length; i++) {
            String page = refs[i];
            boolean found = Arrays.asList(memory).contains(page);
            boolean fault = false;
            if (!found) {
                memory[pointer] = page;
                pointer = (pointer + 1) % frames;
                fault = true;
            }
            for (int f = 0; f < frames; f++) frameStates[f][i] = memory[f].equals("-") ? "" : memory[f];
            faults[i] = fault;
        }
    }

    private void runLRULogic(String[] refs, int frames, String[][] frameStates, boolean[] faults) {
        String[] memory = new String[frames];
        Arrays.fill(memory, "-");
        Map<String, Integer> lastUsed = new HashMap<>();
        for (int i = 0; i < refs.length; i++) {
            String page = refs[i];
            boolean found = Arrays.asList(memory).contains(page);
            boolean fault = false;
            if (!found) {
                int replaceIndex = -1;
                for (int j = 0; j < frames; j++) 
                	if (memory[j].equals("-")) { 
                		replaceIndex = j; break; 
                	}
                if (replaceIndex == -1) {
                    int lruIndex = 0;
                    int min = Integer.MAX_VALUE;
                    for (int j = 0; j < frames; j++) {
                        int usage = lastUsed.getOrDefault(memory[j], -1);
                        if (usage < min) { 
                        	min = usage; lruIndex = j; 
                        }
                    }
                    replaceIndex = lruIndex;
                }
                memory[replaceIndex] = page;
                fault = true;
            }
            lastUsed.put(page, i);
            for (int f = 0; f < frames; f++) {
            	frameStates[f][i] = memory[f].equals("-") ? "" : memory[f];
            }
            faults[i] = fault;
        }
    }

    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Poppins", Font.PLAIN, 18));
        btn.setFocusPainted(false);
        btn.setBackground(bgColor);
        btn.setForeground(fgColor);
        btn.setMaximumSize(new Dimension(220, 45));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBorder(BorderFactory.createLineBorder(new Color(0, 61, 165), 2, true));
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(bgColor.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(bgColor);
            }
        });

        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StartSimulationFrame().setVisible(true));
    }
}