/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guijframe;

/**
 *
 * @author w1682889 Hou In Lei
 */
public class GUIjframe extends javax.swing.JFrame {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /**
         * create the login page before everything and set it visible
         */
        login mylogin = new login();
        mylogin.setVisible(true);
    }

}
