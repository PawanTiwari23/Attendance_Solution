/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package forms;


import com.google.gson.Gson;
import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author PAWAN
 */
public class GenerateAndSaveQr extends JFrame {
    
    
     private JTable userTable;
    private JLabel lblImage;
    private JButton btnSaveQr;
     private JButton btnExit;
    private ByteArrayOutputStream out = null;
    private String email = null;

    public GenerateAndSaveQr() {
        initComponents();
        loadData();
        
         //BDUtility.setImage(this, "images/abc1.jpg", 1268, 560);//1268,560
        this.getRootPane().setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.BLACK));
    }

    private void initComponents() {
        setTitle("QR Code Generator");
        setSize(1000, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(20, 10));
        getContentPane().setBackground(new Color(245, 245, 245));

        // Top Label
        JLabel headerLabel = new JLabel("Generate QR Code for Users", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(new Color(33, 150, 243));
        headerLabel.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(headerLabel, BorderLayout.NORTH);

        // Table
        userTable = new JTable(new DefaultTableModel(new Object[][]{}, new String[]{"ID", "Name", "Email", "Registration ID"}));
        userTable.setRowHeight(30);
        userTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        userTable.setGridColor(Color.LIGHT_GRAY);
        userTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                generateQrFromTable(evt);
            }
        });
        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        add(scrollPane, BorderLayout.CENTER);

        // Right panel for image and button
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        rightPanel.setBackground(Color.WHITE);

        lblImage = new JLabel("QR Preview", SwingConstants.CENTER);
        lblImage.setPreferredSize(new Dimension(300, 300));
        lblImage.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        rightPanel.add(lblImage, BorderLayout.CENTER);

        btnSaveQr = new JButton("Save QR Code");
        btnSaveQr.setFocusPainted(false);
        btnSaveQr.setBackground(new Color(33, 150, 243));
        btnSaveQr.setForeground(Color.WHITE);
        btnSaveQr.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSaveQr.setPreferredSize(new Dimension(150, 40));
        btnSaveQr.addActionListener(this::saveQrCode);

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(btnSaveQr);
        
        btnExit = new JButton("Exit");
        btnExit.setFocusPainted(false);
        btnExit.setBackground(new Color(244, 67, 54));
        btnExit.setForeground(Color.WHITE);
        btnExit.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnExit.setPreferredSize(new Dimension(100, 40));
        btnExit.addActionListener(e -> dispose());
        btnPanel.add(btnExit);
        rightPanel.add(btnPanel, BorderLayout.SOUTH);

        add(rightPanel, BorderLayout.EAST);
    }

    private void loadData() {
        DefaultTableModel model = (DefaultTableModel) userTable.getModel();
        model.setRowCount(0);

        try {
            Connection con = dao.ConnectionProvider.getCon();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, name, email, uniqueregid FROM userdetails");

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("uniqueregid")
                });
            }

            rs.close();
            stmt.close();
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
        }
    }

    private void generateQrFromTable(MouseEvent evt) {
        int row = userTable.getSelectedRow();
        if (row == -1) return;

        TableModel model = userTable.getModel();
        String id = model.getValueAt(row, 0).toString();
        String name = model.getValueAt(row, 1).toString();
        email = model.getValueAt(row, 2).toString();
        String registrationId = model.getValueAt(row, 3).toString();

        Map<String, String> data = new HashMap<>();
        data.put("id", id);
        data.put("name", name);
        data.put("email", email);
        data.put("registrationId", registrationId);

        String jsonData = new Gson().toJson(data);

        try {
            out = QRCode.from(jsonData).withSize(322, 286).to(ImageType.PNG).stream();
            ByteArrayInputStream bis = new ByteArrayInputStream(out.toByteArray());
            Image image = ImageIO.read(bis);
            lblImage.setIcon(new ImageIcon(image));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "QR Generation Error: " + ex.getMessage());
        }
    }

    private void saveQrCode(ActionEvent evt) {
        if (out == null || email == null || email.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a user and generate the QR code first.");
            return;
        }

        String userHome = System.getProperty("user.dir");
        String folderPath = userHome  + File.separator + "qrCodes";
        File directory = new File(folderPath);

        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                JOptionPane.showMessageDialog(this, "Failed to create directory.");
                return;
            }
        }

        File outputFile = new File(directory, email + ".png");
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(out.toByteArray());
            JOptionPane.showMessageDialog(this, "QR Code saved to: " + outputFile.getAbsolutePath());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving QR Code: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GenerateAndSaveQr().setVisible(true));
    }
    
}
