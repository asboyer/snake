import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame("Snake Game"); //making a new window object with title "snake game"
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //saying if the window closes, close the program
        window.setBounds(0, 0, 800, 800 + 22); //these are the boundries of the window
        Panel panel = new Panel(window.getWidth(), window.getHeight()); // new panel object
        panel.setFocusable(true);
        panel.grabFocus();
        window.add(panel); //adding panel to window
        window.setVisible(true);
        window.setResizable(true); // allowing window to be resizable

    }
}