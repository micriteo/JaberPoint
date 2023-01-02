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
    private Presentation presentation; //Commands are given to the presentation

    private static final long serialVersionUID = 227L;

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

    protected static final String TESTFILE = "testPresentation.xml";
    protected static final String SAVEFILE = "savedPresentation.xml";

    protected static final String IOEX = "IO Exception: ";
    protected static final String LOADERR = "Load Error";
    protected static final String SAVEERR = "Save Error";

    public MenuController(Frame frame, Presentation presentation) {
        this.parent = frame;
        this.presentation = presentation;
        MenuItem menuItem;
        Menu fileMenu = new Menu(FILE);
//		fileMenu.add(menuItem = mkMenuItem(OPEN));
//		menuItem.addActionListener(actionEvent -> {
//			presentation.clear();
//
//			Accessor xmlAccessor = new XMLAccessor();
//			try {
//				xmlAccessor.loadFile(presentation, TESTFILE);
//				presentation.setSlideNumber(0);
//			} catch (IOException exc) {
//				JOptionPane.showMessageDialog(parent, IOEX + exc,
//				 LOADERR, JOptionPane.ERROR_MESSAGE);
//			}
//			parent.repaint();
//		});

        fileMenu.add(menuItem = mkMenuItem(OPEN));
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                presentation.clear();
                // Selecting a file
                JFileChooser fileChooser = new JFileChooser();
                // Set the current directory to the current directory
                fileChooser.setCurrentDirectory(new File("."));
                // Only show xml files
                // Create a filter for xml files
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Xml files", "xml");
                // Set the filter
                fileChooser.setFileFilter(filter);
                // Show the file chooser

                int response = fileChooser.showOpenDialog(null);

                // If the user selects a file
                if (response == JFileChooser.APPROVE_OPTION) {
                    // Get the selected file
                    File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                    // Create a new XMLAccessor
                    try (Scanner ignored = new Scanner(file)) {
                        if (file.isFile()) {
                            try {
                                // Load the file
                                Accessor xmlAccessor = new XMLAccessor();
                                xmlAccessor.loadFile(presentation, file.getAbsolutePath());
                                // Set the slide number to 0
                                presentation.setSlideNumber(0);
                            } catch (IOException exc) {
                                JOptionPane.showMessageDialog(parent, IOEX + exc,
                                        LOADERR, JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                parent.repaint();
            }
        });


        fileMenu.add(menuItem = mkMenuItem(NEW));
        menuItem.addActionListener(actionEvent -> {
            presentation.clear();
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
                    xmlAccessor.saveFile(presentation, userInput + ".xml");
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
        menuItem.addActionListener(actionEvent -> presentation.exit(0));
        add(fileMenu);
        Menu viewMenu = new Menu(VIEW);
        viewMenu.add(menuItem = mkMenuItem(NEXT));
        menuItem.addActionListener(actionEvent -> presentation.nextSlide());
        viewMenu.add(menuItem = mkMenuItem(PREV));
        menuItem.addActionListener(actionEvent -> presentation.prevSlide());
        viewMenu.add(menuItem = mkMenuItem(GOTO));
        menuItem.addActionListener(actionEvent -> {
            String pageNumberStr = JOptionPane.showInputDialog((Object) PAGENR);
            int pageNumber = Integer.parseInt(pageNumberStr);
            if (pageNumber > 0 && pageNumber <= presentation.getSize()) {
                presentation.setSlideNumber(pageNumber - 1);
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
