import java.awt.*;

public interface FontBuilder {
    CustomFont createFont(Color color, String fontname, int fontstyle, int fontweight);
}