/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package forms;

/**
 *
 * @author PAWAN
 */
         
    
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.google.gson.Gson;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class Cameraopen extends JFrame implements Runnable {

    private Webcam webcam;
    private WebcamPanel panel;
    private JPanel camPanel;
    private JLabel lblName, lblImage;
    private boolean running = true;

    public Cameraopen() {
        setTitle("QR Attendance");
        setLayout(new BorderLayout());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        camPanel = new JPanel(new BorderLayout());
        lblName = new JLabel("Name will appear here", JLabel.CENTER);
        lblName.setFont(new Font("Arial", Font.BOLD, 18));

        lblImage = new JLabel();
        lblImage.setHorizontalAlignment(JLabel.CENTER);

        add(camPanel, BorderLayout.CENTER);
        add(lblName, BorderLayout.SOUTH);
        add(lblImage, BorderLayout.EAST);

        initWebcam();
        setVisible(true);
    }

    private void initWebcam() { /*
        webcam = Webcam.getDefault();
        if (webcam != null) {
            webcam.setViewSize(new Dimension(640, 480));
            panel = new WebcamPanel(webcam);
            panel.setFPSDisplayed(true);
            camPanel.add(panel, BorderLayout.CENTER);
            webcam.open();
            new Thread(this).start();
        } else {
            JOptionPane.showMessageDialog(this, "Webcam not found");
        }*/
        
    new Thread(() -> {
        try {
            webcam = Webcam.getDefault();
            if (webcam != null) {
                webcam.setViewSize(new Dimension(640, 480));
                panel = new WebcamPanel(webcam);
                panel.setFPSDisplayed(true);
                webcam.open(); // open on background thread

                SwingUtilities.invokeLater(() -> {
                    camPanel.add(panel, BorderLayout.CENTER);
                    camPanel.revalidate();
                });

                new Thread(this).start(); // start scanning
            } else {
                JOptionPane.showMessageDialog(this, "No webcam found");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error opening webcam:\n" + ex.getMessage());
        }
    }).start();


    }

    @Override
    public void run() {
        do {
            try {
                Thread.sleep(1000);
                BufferedImage image = null;
                if (webcam != null && webcam.isOpen()) {
                    image = webcam.getImage();
                    if (image == null) continue;

                    LuminanceSource source = new BufferedImageLuminanceSource(image);
                    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                    try {
                        Result result = new MultiFormatReader().decode(bitmap);
                        if (result != null) {
                            processQRCode(result.getText());
                            Thread.sleep(5000); // pause for 5 sec after successful scan
                        }
                    } catch (NotFoundException e) {
                        // QR not found, continue scanning
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (running);
    }

    private void processQRCode(String qrText) {
        /*
        Gson gson = new Gson();
       // Type type = new TypeToken<Map<String, String>>(){}.getType();
       // Map<String, String> resultMap = gson.fromJson(qrText, type);

       // String name = resultMap.get("name");
        //String email = resultMap.get("email");

       // lblName.setText("Welcome: " + name);

        // Load image if available
       // String imagePath = "images/" + email + ".jpg";
       // File imageFile = new File(imagePath);
        if (imageFile.exists()) {
            try {
                BufferedImage image = ImageIO.read(imageFile);
                lblImage.setIcon(new ImageIcon(image.getScaledInstance(200, 200, Image.SCALE_SMOOTH)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            lblImage.setIcon(null);
        }

        // You can insert attendance logic here
        System.out.println("Attendance marked for: " + name + " (" + email + ")");*/
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Cameraopen());
    }
}

