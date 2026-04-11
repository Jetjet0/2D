/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package costumer;

import config.UserSession;
import config.cconfig;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author USER33
 */
public class rturn extends javax.swing.JFrame {

    private JPanel bookGrid;

    public rturn() {
        if (!UserSession.requireLogin(this)) return;
        initComponents();

        // Build a scrollable book grid
        bookGrid = new JPanel();
        bookGrid.setLayout(new GridLayout(0, 3, 20, 20));
        bookGrid.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(bookGrid);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Add to jPanel1 with AbsoluteConstraints
        jPanel1.add(scrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 90, 680, 550));

        loadBorrowedBooks();
    }

    private void loadBorrowedBooks() {
        bookGrid.removeAll();

        try {
            Connection conn = cconfig.connectDB();
            String sql = "SELECT p_id, bo_id, book_title, price, days, status " +
                         "FROM tbl_pending WHERE username = ? AND status = 'Approved'";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, UserSession.username);
            ResultSet rs = pst.executeQuery();

            boolean hasBooks = false;

            while (rs.next()) {
                hasBooks = true;
                int borrowId   = rs.getInt("p_id");      // primary key in tbl_pending
                int bookId     = rs.getInt("bo_id");     // actual book ID
                String title   = rs.getString("book_title");
                int price      = rs.getInt("price");
                int days       = rs.getInt("days");
                String status  = rs.getString("status");

                // Fetch book cover from tbl_books
                byte[] imageBytes = null;
                try {
                    Connection conn2 = cconfig.connectDB();
                    PreparedStatement pst2 = conn2.prepareStatement(
                            "SELECT picture FROM tbl_books WHERE bo_id = ?");
                    pst2.setInt(1, bookId);
                    ResultSet rs2 = pst2.executeQuery();
                    if (rs2.next()) imageBytes = rs2.getBytes("picture");
                    rs2.close(); pst2.close(); conn2.close();
                } catch (Exception ignored) {}

                JPanel card = new JPanel(new BorderLayout());
                card.setPreferredSize(new Dimension(200, 330));
                card.setBackground(Color.WHITE);
                card.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                JLabel picLabel = new JLabel();
                picLabel.setHorizontalAlignment(JLabel.CENTER);
                picLabel.setPreferredSize(new Dimension(190, 250));

                if (imageBytes != null && imageBytes.length > 0) {
                    ImageIcon icon = new ImageIcon(imageBytes);
                    Image img = icon.getImage().getScaledInstance(190, 250, Image.SCALE_SMOOTH);
                    picLabel.setIcon(new ImageIcon(img));
                } else {
                    picLabel.setText("No Image");
                }

                JLabel infoLabel = new JLabel(
                    "<html><center>" + title +
                    "<br>₱" + price +
                    " | " + days + " day(s)" +
                    "<br><font color='green'>" + status + "</font>" +
                    "</center></html>", JLabel.CENTER);
                infoLabel.setFont(new Font("Tahoma", Font.BOLD, 12));

                card.add(picLabel, BorderLayout.CENTER);
                card.add(infoLabel, BorderLayout.SOUTH);

                // Click to confirm return
                card.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        int confirm = JOptionPane.showConfirmDialog(
                                rturn.this,
                                "Return \"" + title + "\"?",
                                "Return Book",
                                JOptionPane.YES_NO_OPTION);

                        if (confirm == JOptionPane.YES_OPTION) {
                            returnBook(borrowId, bookId, title);
                        }
                    }
                });

                bookGrid.add(card);
            }

            if (!hasBooks) {
                JLabel empty = new JLabel("No approved borrowed books found.", JLabel.CENTER);
                empty.setFont(new Font("Tahoma", Font.PLAIN, 16));
                bookGrid.add(empty);
            }

            rs.close();
            pst.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load borrowed books!");
        }

        bookGrid.revalidate();
        bookGrid.repaint();
    }

    private void returnBook(int borrowId, int bookId, String title) {
        try {
            Connection conn = cconfig.connectDB();

            // Delete from tbl_pending using p_id
            PreparedStatement pst = conn.prepareStatement(
                    "DELETE FROM tbl_pending WHERE p_id = ?");
            pst.setInt(1, borrowId);
            pst.executeUpdate();

            // Update book status to Available
            PreparedStatement pst2 = conn.prepareStatement(
                    "UPDATE tbl_books SET status = 'Available' WHERE bo_id = ?");
            pst2.setInt(1, bookId);
            pst2.executeUpdate();

            conn.close();

            JOptionPane.showMessageDialog(this,
                    "\"" + title + "\" returned successfully!");
            loadBorrowedBooks();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to return book.");
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
        jToggleButton1 = new javax.swing.JToggleButton();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

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
        jTextField1.setText("BOOK YOU BORROWED");
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jPanel1.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 20, 560, 50));

        jPanel4.setBackground(new java.awt.Color(0, 0, 255));
        jPanel4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 3, true));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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

        jButton1.setText("Transaction");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 340, 150, 30));

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 360, 680));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1065, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jToggleButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jToggleButton1MouseClicked
        setc lp = new setc();
        lp.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jToggleButton1MouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        bok lp = new bok();
        lp.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        rturn lp = new rturn();
        lp.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(rturn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(rturn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(rturn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(rturn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new rturn().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JToggleButton jToggleButton1;
    // End of variables declaration//GEN-END:variables
}
