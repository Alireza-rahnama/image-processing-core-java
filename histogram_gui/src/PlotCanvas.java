import java.awt.*;
import java.util.ArrayList;

// Canvas for plotting histogram
class PlotCanvas extends Canvas {
    // lines for plotting axes and mean color locations
    LineSegment x_axis, y_axis;
    LineSegment red, green, blue;
    boolean showMean = false;
    boolean showHistogram = false;
    ArrayList<LineSegment> lines = new ArrayList<LineSegment>();

    public PlotCanvas() {
        x_axis = new LineSegment(Color.BLACK, -10, 0, 256+10, 0);
        y_axis = new LineSegment(Color.BLACK, 0, -10, 0, 200+10);
    }
    // set mean image color for plot
    public void setMeanColor(Color clr) {
        red = new LineSegment(Color.RED, clr.getRed(), 0, clr.getRed(), 100);
        green = new LineSegment(Color.GREEN, clr.getGreen(), 0, clr.getGreen(), 100);
        blue = new LineSegment(Color.BLUE, clr.getBlue(), 0, clr.getBlue(), 100);
        showMean = true;
        repaint();
    }
    public void buildHistogram(float[][] histograms, String name)
    {
        //Receives 3 normalized color histograms
        //Histograms = {red, green, blue}
        lines.clear();
        int multiplier;
        if(name == "baboon.png")
        {
            multiplier = 200;
        }
        else
        {
            multiplier = 20;
        }
        for(int j = 0; j < histograms.length; j++)
        {
            for(int i = 0; i < 255; i++)
            {
                int point1 = (int) (histograms[j][i] * multiplier);
                int point2 = (int) (histograms[j][i + 1] * multiplier);
                Color color;
                if(j == 0)
                {
                    color = Color.RED;
                }
                else if(j == 1)
                {
                    color = Color.GREEN;
                }
                else
                {
                    color = Color.BLUE;
                }
                LineSegment line = new LineSegment(color, i,point1, i + 1,point2);
                lines.add(line);
            }
        }
        showHistogram = true;
        repaint();
    }
    // redraw the canvas
    public void paint(Graphics g) {
        // draw axis
        int xoffset = (getWidth() - 256) / 2;
        int yoffset = (getHeight() - 200) / 2;
        x_axis.draw(g, xoffset, yoffset, getHeight());
        y_axis.draw(g, xoffset, yoffset, getHeight());
        if ( showMean ) {
            red.draw(g, xoffset, yoffset, getHeight());
            green.draw(g, xoffset, yoffset, getHeight());
            blue.draw(g, xoffset, yoffset, getHeight());
        }
        if(showHistogram)
        {
            for(LineSegment line : lines)
            {
                line.draw(g, xoffset, yoffset, getHeight());
            }
            lines.clear();
        }
    }
}
