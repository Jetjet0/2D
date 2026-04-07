/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admin;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.awt.Desktop;
import java.io.FileOutputStream;
import config.UserSession;
import config.cconfig;

public class sales extends javax.swing.JFrame {

    public sales() {
        if (!UserSession.requireLogin(this)) return;

        initComponents();

        // Correct headers
        jTable1.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Username", "Book Title", "Price", "Days", "Status"}
        ));

        loadBorrowedBooks();

        jButton4.addActionListener(e -> approveRecord());
    }

    private void loadBorrowedBooks() {
        try (Connection conn = cconfig.connectDB()) {
            String sql = "SELECT p_id, username, book_title, price, days, status FROM tbl_pending";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);

            while (rs.next()) {
                int id = rs.getInt("p_id");
                String username = rs.getString("username");
                String title = rs.getString("book_title");
                int price = rs.getInt("price");
                int days = rs.getInt("days");
                String status = rs.getString("status");

                model.addRow(new Object[]{id, username, title, price, days, status});
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load records.");
        }
    }

    // PDF Receipt generation method
    private void generateReceipt(int id, String username, String title, int price, int days) {
        try {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Save Receipt");
            chooser.setSelectedFile(new java.io.File("Receipt_" + id + ".pdf"));
            int userSelection = chooser.showSaveDialog(this);
            if (userSelection != JFileChooser.APPROVE_OPTION) return;

            String path = chooser.getSelectedFile().getAbsolutePath();
            Document document = new Document(PageSize.A5, 20, 20, 20, 20);
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();

            Font storeFont = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, BaseColor.BLUE);
            Paragraph store = new Paragraph("LIBRARY MANAGEMENT\n", storeFont);
            store.setAlignment(Element.ALIGN_CENTER);
            document.add(store);

            Paragraph date = new Paragraph("Date: " + java.time.LocalDate.now() +
                    "    Time: " + java.time.LocalTime.now().withNano(0));
            date.setAlignment(Element.ALIGN_CENTER);
            document.add(date);

            document.add(new Paragraph("\n-----------------------------------\n"));

            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            Font headFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            PdfPCell cell1 = new PdfPCell(new Phrase("Item", headFont));
            PdfPCell cell2 = new PdfPCell(new Phrase("Details", headFont));
            cell1.setBorder(Rectangle.NO_BORDER);
            cell2.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell1);
            table.addCell(cell2);

            table.addCell("Receipt ID"); table.addCell(String.valueOf(id));
            table.addCell("Username"); table.addCell(username);
            table.addCell("Book Title"); table.addCell(title);
            table.addCell("Price per Day"); table.addCell("₱" + price);
            table.addCell("Days"); table.addCell(String.valueOf(days));
            table.addCell("Total"); table.addCell("₱" + (price * days));

            document.add(table);
            document.add(new Paragraph("-----------------------------------\n"));

            Paragraph thanks = new Paragraph("Thank you for using LIBRARY MANAGEMENT!");
            thanks.setAlignment(Element.ALIGN_CENTER);
            document.add(thanks);

            document.close();

            // Auto-open PDF
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(new java.io.File(path));
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to generate receipt.");
        }
    }

    // Approve button
    private void approveRecord() {
        int row = jTable1.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a record first!");
            return;
        }

        int id = Integer.parseInt(jTable1.getValueAt(row, 0).toString());
        String username = jTable1.getValueAt(row, 1).toString();
        String bookTitle = jTable1.getValueAt(row, 2).toString();
        int price = Integer.parseInt(jTable1.getValueAt(row, 3).toString());
        int days = Integer.parseInt(jTable1.getValueAt(row, 4).toString());

        int confirm = JOptionPane.showConfirmDialog(this,
                "Approve this record?",
                "Confirm",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = cconfig.connectDB()) {
                String sql = "UPDATE tbl_pending SET status='Approved' WHERE p_id=?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setInt(1, id);
                pst.executeUpdate();

                JOptionPane.showMessageDialog(this, "Approved!");
                loadBorrowedBooks();

                // Generate and auto-open PDF receipt
                generateReceipt(id, username, bookTitle, price, days);

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Approval or receipt generation failed.");
            }
        }
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jToggleButton1 = new javax.swing.JToggleButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jTextField1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jTextField1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField1.setText("WELCOME TO BORROWED DASHBOARD");
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jPanel1.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 20, 510, 50));

        jPanel4.setBackground(new java.awt.Color(0, 0, 255));
        jPanel4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 3, true));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton1.setText("Users");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 140, 170, 30));

        jToggleButton1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jToggleButton1.setText("Settings");
        jToggleButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jToggleButton1MouseClicked(evt);
            }
        });
        jPanel4.add(jToggleButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 630, 140, 30));

        jButton2.setText("Books");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 270, 150, 30));

        jButton3.setText("Sales");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 330, 150, 30));

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 360, 680));

        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jPanel3.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 640, 310));

        jButton4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton4.setText("Approve");
        jPanel3.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 320, -1, -1));

        jButton5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton5.setText("Decline");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 320, 100, 30));

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 90, 640, 360));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1055, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 20, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        NewJFrame lp = new NewJFrame();
        lp.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jToggleButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jToggleButton1MouseClicked
        settingss lp = new settingss();
        lp.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jToggleButton1MouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        adbok lp = new adbok();
        lp.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        sales lp = new sales();
        lp.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
         int row = jTable1.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a record first!");
            return;
        }

        int id = Integer.parseInt(jTable1.getValueAt(row, 0).toString());

        int confirm = JOptionPane.showConfirmDialog(this,
                "Decline this record?",
                "Confirm",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Connection conn = cconfig.connectDB();

                String sql = "UPDATE tbl_pending SET status='Declined' WHERE p_id=?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setInt(1, id);
                pst.executeUpdate();

                conn.close();

                JOptionPane.showMessageDialog(this, "Declined!");
                loadBorrowedBooks();

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Decline failed.");
            }
        }
    
    }//GEN-LAST:event_jButton5ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(sales.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(sales.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(sales.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(sales.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new sales().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JToggleButton jToggleButton1;
    // End of variables declaration//GEN-END:variables
}
