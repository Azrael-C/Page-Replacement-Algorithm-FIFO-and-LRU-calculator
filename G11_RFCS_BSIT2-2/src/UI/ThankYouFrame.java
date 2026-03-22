package UI;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.swing.*;

public class ThankYouFrame extends JFrame {
private static final long serialVersionUID = 1L;

private Font poppinsRegular;
private Font poppinsMedium;
private Font poppinsBold;

public ThankYouFrame() {
	setUndecorated(true);
    setTitle("Page Replacement Algorithm - Thank You");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setExtendedState(JFrame.MAXIMIZED_BOTH);
    setLocationRelativeTo(null);
    getContentPane().setLayout(new BorderLayout());

    poppinsRegular = loadFont("src/fonts/Poppins-Regular.ttf", 22f);
    poppinsMedium = loadFont("src/fonts/Poppins-Medium.ttf", 26f);
    poppinsBold = loadFont("src/fonts/Poppins-Bold.ttf", 36f);

    JPanel mainPanel = new JPanel(new GridBagLayout());
    mainPanel.setBackground(new Color(0, 61, 165)); // Deep blue

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 0, 10, 0);
    gbc.gridx = 0;
    gbc.anchor = GridBagConstraints.CENTER;

    JLabel thankYouLabel = new JLabel("Thank you for using the");
    thankYouLabel.setForeground(Color.WHITE);
    thankYouLabel.setFont(poppinsRegular.deriveFont(32f));
    mainPanel.add(thankYouLabel, gbc);

    gbc.gridy = 1;
    JLabel titleLabel = new JLabel("Page Replacement Algorithm");
    titleLabel.setForeground(Color.WHITE);
    titleLabel.setFont(poppinsBold.deriveFont(40f));
    mainPanel.add(titleLabel, gbc);

    gbc.gridy = 2;
    JLabel seeYouLabel = new JLabel("See you again!");
    seeYouLabel.setForeground(Color.WHITE);
    seeYouLabel.setFont(poppinsRegular.deriveFont(28f));
    mainPanel.add(seeYouLabel, gbc);

    gbc.gridy = 3;
    gbc.insets = new Insets(40, 0, 10, 0);
    JPanel buttonPanel = new JPanel();
    buttonPanel.setBackground(new Color(0, 61, 165));
    buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 0));

    JButton backButton = createStyledButton("Back to Main Menu");
    JButton exitButton = createStyledButton("Exit Application");

    buttonPanel.add(backButton);
    buttonPanel.add(exitButton);
    mainPanel.add(buttonPanel, gbc);

    backButton.addActionListener(e -> {
        MainMenuFrame mainMenu = new MainMenuFrame();
        mainMenu.setVisible(true);
        dispose();
    });

    exitButton.addActionListener(e -> System.exit(0));

    getContentPane().add(mainPanel, BorderLayout.CENTER);
}

private JButton createStyledButton(String text) {
    JButton button = new JButton(text);
    button.setFont(poppinsMedium.deriveFont(22f));
    button.setForeground(new Color(0, 61, 165));
    button.setBackground(Color.WHITE);
    button.setFocusPainted(false);
    button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2, true));
    button.setPreferredSize(new Dimension(260, 60));

    button.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
            button.setBackground(new Color(240, 240, 255));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            button.setBackground(Color.WHITE);
        }
    });
    return button;
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
        ThankYouFrame frame = new ThankYouFrame();
        frame.setVisible(true);
    });
}
}