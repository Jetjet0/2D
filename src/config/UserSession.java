package config;

import internapage.loginpage;
import javax.swing.*;

/**
 * UserSession manages the logged-in user's session information.
 */
public class UserSession {

    // Static fields to store user data
    public static String username;
    public static String email;
    public static String role;

    /**
     * Clear the current session.
     */
    public static void clear() {
        username = null;
        email = null;
        role = null;
    }

    /**
     * Check if a user is logged in.
     * @return true if username is not null or empty
     */
    public static boolean isLoggedIn() {
        return username != null && !username.isEmpty();
    }

    /**
     * Guard method to enforce login.
     * Should be called at the start of any protected JFrame.
     * Example:
     * if (!UserSession.requireLogin(this)) return;
     *
     * @param currentFrame the JFrame attempting access
     * @return true if access granted, false if redirected to login
     */
    public static boolean requireLogin(JFrame currentFrame) {
        if (!isLoggedIn()) {
            // Notify user and redirect to login page
            JOptionPane.showMessageDialog(
                    currentFrame,
                    "You must login first to access this page.",
                    "Access Denied",
                    JOptionPane.WARNING_MESSAGE
            );
            new loginpage().setVisible(true); // Open login page
            currentFrame.dispose(); // Close the protected page
            return false;
        }
        return true; // user is logged in
    }

}