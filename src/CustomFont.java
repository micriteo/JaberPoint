import java.awt.*;

public class CustomFont extends Font {

    private Color color;
    private String fontname;
    private int fontstyle;
    private int fontweight;

    public CustomFont(Color color, String fontname, int fontstyle, int fontweight) {
        super(fontname, fontstyle, fontweight);
        this.color = color;
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getFontname() {
        return this.fontname;
    }

    public void setFontname(String fontname) {
        this.fontname = fontname;
    }

    public int getFontstyle() {
        return this.fontstyle;
    }

    public void setFontstyle(int fontstyle) {
        this.fontstyle = fontstyle;
    }

    public int getFontweight() {
        return this.fontweight;
    }

    public void setFontweight(int fontweight) {
        this.fontweight = fontweight;
    }
}
