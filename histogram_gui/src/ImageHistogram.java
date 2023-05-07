import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

// Main class
public class ImageHistogram extends Frame implements ActionListener {
	BufferedImage input;
	int width, height, min, max, max_gray_level;
	float cutoff;
	TextField texRad, texThres;
	ImageCanvas source, target;
	PlotCanvas plot;
	String name;


	// Constructor
	public ImageHistogram(String name) {
		super("Image Histogram");
		// load image
		this.name = name;
		try {
			input = ImageIO.read(new BufferedInputStream(new FileInputStream(new File(getClass().getResource(name).toURI()))));
		}
		catch ( Exception ex ) {
			ex.printStackTrace();
		}
		width = input.getWidth();
		height = input.getHeight();
		max_gray_level = 255;
		// prepare the panel for image canvas.
		Panel main = new Panel();
		source = new ImageCanvas(input);
		plot = new PlotCanvas();
		target = new ImageCanvas(input);
		main.setLayout(new GridLayout(1, 3, 10, 10));
		main.add(source);
		main.add(plot);
		main.add(target);
		// prepare the panel for buttons.
		Panel controls = new Panel();
		Button button = new Button("Display Histogram");
		button.addActionListener(this);
		controls.add(button);
		button = new Button("Histogram Stretch");
		button.addActionListener(this);
		controls.add(button);
		controls.add(new Label("Cutoff fraction:"));
		texThres = new TextField("10", 2);
		controls.add(texThres);
		button = new Button("Aggressive Stretch");
		button.addActionListener(this);
		controls.add(button);
		button = new Button("Histogram Equalization");
		button.addActionListener(this);
		controls.add(button);
		// add two panels
		add("Center", main);
		add("South", controls);
		addWindowListener(new ExitListener());
		setSize(width*2+400, height+100);
		setVisible(true);
	}
	class ExitListener extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}
	// Action listener for button click events
	public void actionPerformed(ActionEvent e) {
		// example -- compute the average color for the image
		if ( ((Button)e.getSource()).getLabel().equals("Display Histogram") ) {
			if(HistogramBuilder.isGreyScale(input))
			{
				target.resetImage(input);
				plot.buildHistogram(new float[][]{HistogramBuilder.getGreyscaleHistogram(input)}, name);
			}
			else
			{
				//Send off the 3 normalized color histograms to be displayed
				target.resetImage(input);
				plot.buildHistogram(HistogramBuilder.getNormalizedHistograms(input), name);
			}
		}
		if ( ((Button)e.getSource()).getLabel().equals("Histogram Stretch") )
		{
			if(HistogramBuilder.isGreyScale(input))
			{
				BufferedImage stretchedImage = Transformations.greyscaleConservativeStretch(HistogramBuilder.getGreyscaleHistogram(input), input);
				plot.buildHistogram(new float[][]{HistogramBuilder.getGreyscaleHistogram(stretchedImage)}, name);
				target.resetImage(stretchedImage);
			}
			else
			{
				BufferedImage stretchedImage = Transformations.conservativeStretch(HistogramBuilder.getNormalizedHistograms(input), input);
				plot.buildHistogram(HistogramBuilder.getNormalizedHistograms(stretchedImage), name);
				target.resetImage(stretchedImage);

			}
		}

		if(((Button)e.getSource()).getLabel().equals("Aggressive Stretch"))
		{
			if(HistogramBuilder.isGreyScale(input))
			{
				float cutoff = Integer.parseInt(texThres.getText());
				BufferedImage aggStretchedImg = Transformations.greyscaleAggStretch(HistogramBuilder.getGreyscaleHistogram(input), input, cutoff);
				plot.buildHistogram(new float[][]{HistogramBuilder.getGreyscaleHistogram(aggStretchedImg)}, name);
				target.resetImage(aggStretchedImg);
			}
			else
			{

				float cutoff = Integer.parseInt(texThres.getText());
				BufferedImage aggStretchedImg = Transformations.aggStretch(HistogramBuilder.getNormalizedHistograms(input), input, cutoff);
				plot.buildHistogram(HistogramBuilder.getNormalizedHistograms(aggStretchedImg), name);
				target.resetImage(aggStretchedImg);

			}
		}

		if(((Button)e.getSource()).getLabel().equals("Histogram Equalization"))
		{

			if(HistogramBuilder.isGreyScale(input))
			{
				BufferedImage equalizedImage = Transformations.greyscaleEqualize(HistogramBuilder.getGreyscaleHistogram(input), input);
				plot.buildHistogram(new float[][]{HistogramBuilder.getGreyscaleHistogram(equalizedImage)}, name);
				target.resetImage(equalizedImage);
			}
			else
			{
				BufferedImage equalizedImage = Transformations.equalize(input);
				plot.buildHistogram(HistogramBuilder.getNormalizedHistograms(equalizedImage), name);
				target.resetImage(equalizedImage);
			}
		}

		
		
	}
	public static void main(String[] args) {
		if (args != null && args.length == 1) {
			new ImageHistogram(args[0]);
		} else {
			new ImageHistogram("signal_hill.png");
		}

	}
}
