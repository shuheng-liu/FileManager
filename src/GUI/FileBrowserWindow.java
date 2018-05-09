package GUI;

import javax.swing.*;

public class FileBrowserWindow extends JFrame {
    public FileBrowserWindow() {
        super();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FileBrowser fileBrowser = new FileBrowser();

        this.setContentPane(fileBrowser.getGUI());
        this.pack();
//        this.setLocationByPlatform(true);
        this.setMinimumSize(this.getSize());
        this.setVisible(true);
    }


    public static void main(String[] args) {
        FileBrowserWindow fw = new FileBrowserWindow();
    }
}
