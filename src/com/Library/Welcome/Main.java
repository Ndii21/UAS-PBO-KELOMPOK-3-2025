package com.Library.Welcome;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // try {
        //     UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }
        
        // Jalankan aplikasi di Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new Welcome().setVisible(true);
        });
    }
}