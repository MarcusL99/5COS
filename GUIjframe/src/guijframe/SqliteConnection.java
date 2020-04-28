/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guijframe;

import static guijframe.PasswordUtils.generateSecurePassword;
import static guijframe.PasswordUtils.verifyUserPassword;
import java.sql.*;

/**
 *
 * @author w1682889 Hou In Lei
 */
public class SqliteConnection {

    /**
     * connect to the database on sqlite
     */
    private Connection con = null;
    private Statement st;

    public Connection connection() {
        try {
            String url = "jdbc:sqlite:databases.db";
            con = DriverManager.getConnection(url);
            System.out.println("successful");
        } catch (Exception ex) {
            System.out.println(" Connection failed");

        }
        return con;
    }

    /**
     * insert user detail into database
     */
    public void insert(String FirstName, String Surname, String Username, String Email, String MemorableWord, String Password, String salt) {
        String sql = "INSERT INTO Users(FirstName,Surname,Username,Email,MemorableWord,Password,UserType,salt) VALUES(?,?,?,?,?,?,?,?)";

        try (Connection con = this.connection();
                PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, FirstName);
            pstmt.setString(2, Surname);
            pstmt.setString(3, Username);
            pstmt.setString(4, Email);
            pstmt.setString(5, MemorableWord);
            pstmt.setString(6, Password);
            pstmt.setString(7, "M");
            pstmt.setString(8, salt);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * get user detail from database and compare them with parameter username
     * and password
     */
    public boolean matchAccount(String name, String pass) {
        String sql = "SELECT * FROM Users WHERE Username=?";
        boolean found = false;
        try (Connection con = this.connection();
                PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                boolean valid = verifyUserPassword(pass, rs.getString("Password"), rs.getString("salt"));
                if (rs.getString("Username").equals(name) && valid) {
                    found = true;
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        if (found) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * get user type from database by username with parameter username and
     * password
     */
    public String getUserType(String name, String pass) {
        String sql = "SELECT * FROM Users WHERE Username=?";
        boolean found = false;
        String Type = null;
        try (Connection con = this.connection();
                PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                boolean valid = verifyUserPassword(pass, rs.getString("Password"), rs.getString("salt"));
                if (rs.getString("Username").equals(name) && valid) {
                    found = true;
                    Type = rs.getString("UserType");
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return Type;
    }

    /**
     * get password from database by email with parameter email
     */
    public String getPassword(String email) {
        String sql = "SELECT * FROM Users WHERE email=?";
        boolean found = false;
        String pw = null;
        try (Connection con = this.connection();
                PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                if (rs.getString("Email").equals(email)) {
                    found = true;
                    pw = rs.getString("Password");
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return pw;
    }

    /**
     * get email from database by email and match it with parameter email
     */
    public boolean matchEmail(String email) {
        String sql = "SELECT * FROM Users WHERE Email=?";
        boolean found = false;
        try (Connection con = this.connection();
                PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                if (rs.getString("Email").equals(email)) {
                    found = true;
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        if (found) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * get email and memorable word from database by email and match it with
     * parameter email and memorable word
     */
    public boolean matchMW(String email, String mw) {
        String sql = "SELECT * FROM Users WHERE Email=?";
        boolean found = false;
        try (Connection con = this.connection();
                PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                if (rs.getString("MemorableWord").equals(mw)) {
                    found = true;
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        if (found) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * update password and salt in database by email
     */
    public void update(String pass, String salt, String mail) {
        String sql = "UPDATE Users SET Password = ? , "
                + "salt = ? "
                + "WHERE Email = ?";

        try (Connection con = this.connection();
                PreparedStatement pstmt = con.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setString(1, pass);
            pstmt.setString(2, salt);
            pstmt.setString(3, mail);
            // update 
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
