import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.xml.crypto.Data;


/**
 * <p>SlideViewerComponent is a graphical component that ca display Slides.</p>
 *
 * @author Ian F. Darwin, ian@darwinsys.com, Gert Florijn, Sylvia Stuurman
 * @version 1.6 2014/05/16 Sylvia Stuurman
 */

public class SlideViewerComponent extends JComponent implements FontBuilder{

    private Slide slide; //The current slide
    //private Presentation presentation = null; //The presentation
    private JFrame frame = null;

    private static final long serialVersionUID = 227L;
    private Presentation presentation;

    private CustomFont customfont;
    private static final Color BGCOLOR = Color.white;
    private static final int XPOS = 1100;
    private static final int YPOS = 20;

    public SlideViewerComponent(Presentation presentation, JFrame frame) {
        setBackground(BGCOLOR);
        this.presentation = presentation;
        customfont = createFont(Color.BLACK, "Dialog", Font.BOLD, 10);
        this.frame = frame;
    }

    public Dimension getPreferredSize() {
        return new Dimension(Slide.WIDTH, Slide.HEIGHT);
    }

    public void update(Presentation presentation, Slide data) {
        if (data == null) {
            repaint();
            return;
        }
		this.presentation = presentation;
        this.slide = data;
        repaint();
		frame.setTitle(presentation.getTitle());
	}

    //Draw the slide
    public void paintComponent(Graphics g) {
        g.setColor(BGCOLOR);
        g.fillRect(0, 0, getSize().width, getSize().height);
        if (presentation.getSlideNumber() < 0 || slide == null) {
            return;
        }
        g.setFont(customfont);
        g.setColor(customfont.getColor());
        g.drawString("Slide " + (1 + presentation.getSlideNumber()) + " of " +
                presentation.getSize(), XPOS, YPOS);
        Rectangle area = new Rectangle(0, YPOS, getWidth(), (getHeight() - YPOS));
        slide.draw(g, area, this);
    }

    @Override
    public CustomFont createFont(Color color , String fontName, int fontStyle, int fontSize) {
        customfont = new CustomFont(color, fontName, fontStyle, fontSize);
        return  customfont;
    }
}
