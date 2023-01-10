import java.awt.MenuBar;
import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * <p>The controller for the menu</p>
 *
 * @author Ian F. Darwin, ian@darwinsys.com, Gert Florijn, Sylvia Stuurman
 * @version 1.6 2014/05/16 Sylvia Stuurman
 */
public class MenuController extends MenuBar {

    private Frame parent; //The frame, only used as parent for the Dialogs
    private SlideViewerComponent slideViwerComponent;

    private static final long serialVersionUID = 227L;

    protected static final String TESTFILE = "testPresentation.xml";
    protected static final String SAVEFILE = "savedPresentation.xml";

    protected static final String ABOUT = "About";
    protected static final String FILE = "File";
    protected static final String EXIT = "Exit";
    protected static final String GOTO = "Go to";
    protected static final String HELP = "Help";
    protected static final String NEW = "New";
    protected static final String NEXT = "Next";
    protected static final String OPEN = "Open";
    protected static final String PAGENR = "Page number?";
    protected static final String PREV = "Prev";
    protected static final String SAVE = "Save";
    protected static final String VIEW = "View";
    protected static final String IOEX = "IO Exception: ";
    protected static final String LOADERR = "Load Error";
    protected static final String SAVEERR = "Save Error";

    public MenuController(Frame frame, SlideViewerComponent slideViewerComponent) {
        this.parent = frame;
        this.slideViwerComponent = slideViewerComponent;
        MenuItem menuItem;
        Menu fileMenu = new Menu(FILE);
        fileMenu.add(menuItem = mkMenuItem(OPEN));
        menuItem.addActionListener(actionEvent -> {
            slideViewerComponent.clear();
            Accessor xmlAccessor = new XMLAccessor();
            try {
                xmlAccessor.loadFile(slideViewerComponent.getPresentation(), TESTFILE);
                slideViwerComponent.setSlideNumber(0);
            } catch (IOException exc) {
                JOptionPane.showMessageDialog(parent, IOEX + exc,
                        LOADERR, JOptionPane.ERROR_MESSAGE);
            }
            parent.repaint();
        });


        fileMenu.add(menuItem = mkMenuItem(NEW));
        menuItem.addActionListener(actionEvent -> {
            slideViwerComponent.clear();
            parent.repaint();
        });
        fileMenu.add(menuItem = mkMenuItem(SAVE));
        menuItem.addActionListener(e -> {
            // Selecting a file
            Accessor xmlAccessor = new XMLAccessor();
            // Create a new XMLAccessor
            String userInput = JOptionPane.showInputDialog(parent, "Name of the File");
            // Get the name of the file
            //if the name is not empty
            if (userInput != null && !userInput.isEmpty()) {
                try {
                    // Save the file
                    xmlAccessor.saveFile(slideViewerComponent.getPresentation(), userInput + ".xml");
                } catch (IOException exc) {
                    JOptionPane.showMessageDialog(parent, IOEX + exc,
                            SAVEERR, JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(parent, "Please enter a name for the file");
            }
        });
        fileMenu.addSeparator();
        fileMenu.add(menuItem = mkMenuItem(EXIT));
        menuItem.addActionListener(actionEvent -> slideViwerComponent.getPresentation().exit(0));
        add(fileMenu);
        Menu viewMenu = new Menu(VIEW);
        viewMenu.add(menuItem = mkMenuItem(NEXT));
        menuItem.addActionListener(actionEvent -> slideViewerComponent.nextSlide());
        viewMenu.add(menuItem = mkMenuItem(PREV));
        menuItem.addActionListener(actionEvent -> slideViewerComponent.prevSlide());
        viewMenu.add(menuItem = mkMenuItem(GOTO));
        menuItem.addActionListener(actionEvent -> {
            String pageNumberStr = JOptionPane.showInputDialog((Object) PAGENR);
            int pageNumber = Integer.parseInt(pageNumberStr);
            if (pageNumber > 0 && pageNumber <= slideViwerComponent.getPresentation().getSize()) {
                slideViwerComponent.setSlideNumber(pageNumber - 1);
            } else {
                JOptionPane.showMessageDialog(parent, "Page number out of range",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        add(viewMenu);
        Menu helpMenu = new Menu(HELP);
        helpMenu.add(menuItem = mkMenuItem(ABOUT));
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                AboutBox.show(parent);
            }
        });
        setHelpMenu(helpMenu);        //Needed for portability (Motif, etc.).
    }

    //Creating a menu-item
    public MenuItem mkMenuItem(String name) {
        return new MenuItem(name, new MenuShortcut(name.charAt(0)));
    }
}
