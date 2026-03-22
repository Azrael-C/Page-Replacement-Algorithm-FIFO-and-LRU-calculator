package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@SuppressWarnings("serial")
public class AboutUsFrame extends JFrame {

    @SuppressWarnings("unused")
	private Font poppinsRegular;
    private Font poppinsMedium;
    private Font poppinsBold;

    public AboutUsFrame() {
    	setUndecorated(true);
        setTitle("About Us");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        poppinsRegular = loadFont("src/fonts/Poppins-Regular.ttf", 18f);
        poppinsMedium = loadFont("src/fonts/Poppins-Medium.ttf", 22f);
        poppinsBold = loadFont("src/fonts/Poppins-Bold.ttf", 28f);

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        topBar.setBackground(Color.WHITE);

        JLabel backLabel = new JLabel("<- Back to Menu");
        backLabel.setFont(poppinsMedium.deriveFont(20f));
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
                backLabel.setForeground(new Color(102, 102, 204));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                backLabel.setForeground(new Color(0, 51, 153));
            }
        });

        topBar.add(backLabel);
        add(topBar, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 300, 40, 300));

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        add(scrollPane, BorderLayout.CENTER);

        JLabel title = new JLabel("  Meet the Developers", SwingConstants.CENTER);
        title.setFont(poppinsBold.deriveFont(32f));
        title.setForeground(new Color(0, 51, 153));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(title);
        mainPanel.add(Box.createVerticalStrut(40));

        addMember(mainPanel, "Arvin Jay Ramirez", "24-0615", "BS Information Technology", "2-2");
        addMember(mainPanel, "Kueight Chester C. Franco", "24-0610", "BS Information Technology", "2-2");
        addMember(mainPanel, "Luzel Kate DC. Santos", "23-2568", "BS Information Technology", "2-6");
        addMember(mainPanel, "Denise G. Cabiles", "23-2567", "BS Information Technology", "2-6");

        setVisible(true);
    }

    private void addMember(JPanel mainPanel, String name, String id, String course, String block) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(0, 51, 153), 1));
        card.setMaximumSize(new Dimension(700, 160));

        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel textBox = new JPanel();
        textBox.setLayout(new BoxLayout(textBox, BoxLayout.Y_AXIS));
        textBox.setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel("Name: " + name);
        JLabel idLabel = new JLabel("ID-Number: " + id);
        JLabel courseLabel = new JLabel("Course: " + course);
        JLabel blockLabel = new JLabel("Block: " + block);

        Font infoFont = poppinsMedium.deriveFont(18f);
        
        Color blue = new Color(0, 51, 153);
        for (JLabel label : new JLabel[]{nameLabel, idLabel, courseLabel, blockLabel}) {
            label.setFont(infoFont);
            label.setForeground(blue);
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            textBox.add(label);
        }

        infoPanel.add(textBox, gbc);
        
        card.add(infoPanel, BorderLayout.CENTER);

        mainPanel.add(card);
        mainPanel.add(Box.createVerticalStrut(20));
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
        SwingUtilities.invokeLater(AboutUsFrame::new);
    }
}