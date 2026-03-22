package UI;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.swing.*;

public class MainMenuFrame extends JFrame {
private static final long serialVersionUID = 1L;

private Font poppinsRegular;
private Font poppinsMedium;
private Font poppinsBold;

public MainMenuFrame() {
	setUndecorated(true);
    setTitle("Page Replacement Algorithm");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setExtendedState(JFrame.MAXIMIZED_BOTH);
    setLocationRelativeTo(null);
    getContentPane().setLayout(new BorderLayout());

    poppinsRegular = loadFont("src/fonts/Poppins-Regular.ttf", 22f);
    poppinsMedium = loadFont("src/fonts/Poppins-Medium.ttf", 26f);
    poppinsBold = loadFont("src/fonts/Poppins-Bold.ttf", 36f);

    JPanel topPanel = new JPanel(new BorderLayout());
    topPanel.setBackground(new Color(0, 51, 153));

    JLabel titleLabel = new JLabel("                           COMSCI 2101 – OPERATING SYSTEMS", SwingConstants.CENTER);
    titleLabel.setForeground(Color.WHITE);
    titleLabel.setFont(poppinsBold.deriveFont(26f));
    titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
    topPanel.add(titleLabel, BorderLayout.CENTER);

    JLabel aboutLabel = new JLabel("About Us ->");
    aboutLabel.setForeground(Color.WHITE);
    aboutLabel.setFont(poppinsRegular.deriveFont(20f));
    aboutLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    aboutLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    aboutLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

    aboutLabel.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            AboutUsFrame aboutFrame = new AboutUsFrame();
            aboutFrame.setVisible(true);
            dispose();
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            aboutLabel.setForeground(new Color(204, 204, 255));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            aboutLabel.setForeground(Color.WHITE);
        }
    });

    topPanel.add(aboutLabel, BorderLayout.EAST);
    getContentPane().add(topPanel, BorderLayout.NORTH);

    JPanel wrapper = new JPanel(new GridBagLayout());
    wrapper.setBackground(new Color(0, 61, 165));

    JPanel mainPanel = new JPanel();
    mainPanel.setBackground(new Color(0, 61, 165));
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    mainPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
    mainPanel.setBorder(BorderFactory.createEmptyBorder(100, 0, 100, 0));

    JLabel welcomeLabel = new JLabel("Welcome to");
    welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    welcomeLabel.setForeground(Color.WHITE);
    welcomeLabel.setFont(poppinsRegular);
    mainPanel.add(welcomeLabel);

    JLabel systemLabel = new JLabel("Page Replacement System");
    systemLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    systemLabel.setForeground(Color.WHITE);
    systemLabel.setFont(poppinsBold.deriveFont(42f));
    mainPanel.add(systemLabel);

    mainPanel.add(Box.createVerticalStrut(40));

    JLabel chooseLabel = new JLabel("Choose the Desired Option");
    chooseLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    chooseLabel.setForeground(Color.WHITE);
    chooseLabel.setFont(poppinsMedium);
    mainPanel.add(chooseLabel);

    mainPanel.add(Box.createVerticalStrut(60));

    JLabel startLabel = createClickableText("Start Simulation", poppinsMedium);
    JLabel historyLabel = createClickableText("History", poppinsMedium);
    JLabel exitLabel = createClickableText("Exit", poppinsMedium); // new Exit button

    mainPanel.add(startLabel);
    mainPanel.add(Box.createVerticalStrut(25));
    mainPanel.add(historyLabel);
    mainPanel.add(Box.createVerticalStrut(25));
    mainPanel.add(exitLabel);

    wrapper.add(mainPanel);
    getContentPane().add(wrapper, BorderLayout.CENTER);

    startLabel.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            StartSimulationFrame startFrame = new StartSimulationFrame();
            startFrame.setVisible(true);
            dispose();
        }
    });

    historyLabel.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            HistoryFrame historyFrame = new HistoryFrame();
            historyFrame.setVisible(true);
            dispose();
        }
    });

    exitLabel.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            ThankYouFrame thankFrame = new ThankYouFrame();
            thankFrame.setVisible(true);
            dispose();
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            exitLabel.setBackground(new Color(240, 240, 255));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            exitLabel.setBackground(Color.WHITE);
        }
    });
}

private JLabel createClickableText(String text, Font font) {
    JLabel label = new JLabel(text, SwingConstants.CENTER);
    label.setOpaque(true);
    label.setBackground(Color.WHITE);
    label.setForeground(new Color(0, 61, 165));
    label.setFont(font.deriveFont(22f));
    label.setPreferredSize(new Dimension(280, 60));
    label.setMaximumSize(new Dimension(280, 60));
    label.setAlignmentX(Component.CENTER_ALIGNMENT);
    label.setBorder(BorderFactory.createLineBorder(new Color(0, 61, 165), 2, true));
    label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

    label.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
            label.setBackground(new Color(240, 240, 255));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            label.setBackground(Color.WHITE);
        }
    });

    return label;
}

private Font loadFont(String path, float size) {
    try {
        File fontFile = new File(path);
        InputStream fontStream = new FileInputStream(fontFile);
        Font font = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(size);
        fontStream.close();
        return font;
    } catch (Exception e) {
        System.out.println("Could not load font: " + path);
        return new Font("SansSerif", Font.PLAIN, (int) size);
    }
}

public static void main(String[] args) {
    try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception ignored) {}

    EventQueue.invokeLater(() -> {
        MainMenuFrame frame = new MainMenuFrame();
        frame.setVisible(true);
    });
}
}