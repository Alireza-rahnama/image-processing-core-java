import java.awt.*;

// LineSegment class defines line segments to be plotted
class LineSegment {
    // location and color of the line segment
    int x0, y0, x1, y1;
    Color color;
    // Constructor
    public LineSegment(Color clr, int x0, int y0, int x1, int y1) {
        color = clr;
        this.x0 = x0; this.x1 = x1;
        this.y0 = y0; this.y1 = y1;
    }
    public void draw(Graphics g, int xoffset, int yoffset, int height) {
        g.setColor(color);
        g.drawLine(x0+xoffset, height-y0-yoffset, x1+xoffset, height-y1-yoffset);
    }
}