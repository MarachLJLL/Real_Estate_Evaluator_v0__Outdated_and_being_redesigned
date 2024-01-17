import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import java.util.Random;
public class CityMapperVoronoi extends JFrame{
    private static int maxXPixels = 1000;
    private static int maxYPixels = 1000;
    private double kmPerPixel;
    private int width;
    private int height;
    private double minLatitude;
    private double maxLatitude;
    private double minLongitude;
    private double maxLongitude;
    private double latitudeRange;
    private double longitudeRange;
    private double minPPSM;
    private double maxPPSM;
    private double pPSMRange;
    private PropertyPoint[] pPts;
    private int pCount;
    private BufferedImage I;
    private Random rand;
    private class PropertyPoint {
        public int longitudeX;
        public int latitudeY;
        public int color;
    }
    CityMapperVoronoi(City city) {
        super(city.getCityName());

        this.minLatitude = city.getMinLatitude();
        this.maxLatitude = city.getMaxLatitude();
        this.minLongitude = city.getMinLongitude();
        this.maxLongitude = city.getMaxLongitude();

        latitudeRange = Math.abs(maxLatitude - minLatitude);
        longitudeRange = Math.abs(maxLongitude - minLongitude);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPixelBounds(city);
        setBounds(0, 0, this.width, this.height);
        setLocationRelativeTo(null); // Frame appears in center of screen


        I = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);


        Property[] properties = city.getPropertiesArray();

        pCount = properties.length;
        if (pCount == 0) {
            System.out.println("No properties here");
            return;
        }
        pPts = new PropertyPoint[pCount];
        minPPSM = properties[0].getPricePerSqMeter();
        maxPPSM = properties[properties.length - 1].getPricePerSqMeter();
        pPSMRange = maxPPSM - minPPSM;

        rand = new Random();
        for (int i = 0; i < pCount; i++) {
            pPts[i] = new PropertyPoint();
            pPts[i].longitudeX = longitudeToX(properties[i].getLongitude());
            pPts[i].latitudeY = latitudeToY(properties[i].getLatitude());

            // testing:
            pPts[i].color = areaPriceToColor(properties[i].getPricePerSqMeter());

            //pPts[i].color = rand.nextInt(0xFFFFFF);
        }
        int closestPIndex;
        double shortestDistance;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                closestPIndex = 0;
                shortestDistance = distance(pPts[0].longitudeX, x, pPts[0].latitudeY, y);
                for (int i = 0; i < pCount; i++) {
                    if (distance(pPts[i].longitudeX, x, pPts[i].latitudeY, y) < shortestDistance) {
                        shortestDistance = distance(pPts[i].longitudeX, x, pPts[i].latitudeY, y);
                        closestPIndex = i;
                    }
                }
                // System.out.println(properties[closestPIndex]);
                I.setRGB(x, y, pPts[closestPIndex].color);
            }
        }
        //I = blur(I, 20);

        // create black dots
        Graphics2D g = I.createGraphics();
        g.setColor(Color.BLACK);
        for (int i = 0; i < pCount; i++) {
            g.fill(new Ellipse2D .Double(pPts[i].longitudeX - 2.5, pPts[i].latitudeY - 2.5, 5, 5));
        }
        g.dispose();

        try {
            ImageIO.write(I, "png", new File("C:\\Users\\llimge\\Documents\\Mcgill Classes\\REE Project\\REE 3 Actual\\PhilippinesProperties\\"
                    + city.getProvinceStateName(), city.getCityName() + ".png"));
        } catch (IOException e) {

        }
//        System.out.println(minLatitude);
//        System.out.println(minLongitude);
//        System.out.println(maxLatitude);
//        System.out.println(maxLongitude);
    }


    private void setPixelBounds(City city) {
        double xDistance = Math.max(
                City.haversineDistance(city.getMinLatitude(), city.getMinLongitude(), city.getMinLatitude(), city.getMaxLongitude()),
                City.haversineDistance(city.getMaxLatitude(), city.getMinLongitude(), city.getMaxLatitude(), city.getMaxLongitude())
        ); // adjusts for curvature of earth (Math.max will select depending on hemisphere of Earth.
        double yDistance =
                City.haversineDistance(city.getMinLatitude(), city.getMinLongitude(), city.getMaxLatitude(), city.getMinLongitude());
        if (maxXPixels/maxYPixels < xDistance/yDistance) { // compare aspect ratio for scale image
            width = maxXPixels;
            height = (int) (maxXPixels/xDistance * yDistance);
        } else {
            width = (int) (maxYPixels/yDistance * xDistance);
            height = maxYPixels;
        }
        kmPerPixel = xDistance/maxXPixels;
        System.out.println("Width: " + width);
        System.out.println("Height: " + height);
    }


    private int latitudeToY(double latitude) {
        double proportion = (latitude - minLatitude) / latitudeRange;
        return (int) ((1.0 - proportion) * height);  // 1.0 - proportion to invert the Y axis
    }

    private int longitudeToX(double longitude) {
        double proportion = (longitude - minLongitude) / longitudeRange;
        return (int) (proportion * width);
    }

    private double xCoordToLongitude(int xCoord) {
        return longitudeRange * width / xCoord + minLongitude;
    }

    private double yCoordToLatitude(int yCoord) {
        return latitudeRange * height / yCoord + minLatitude;
    }

    private int areaPriceToColor(double value) {
        int r, g, b;

        if (value <= 0.5) {
            // Interpolate between blue and green
            r = 0;
            g = (int) (0xFF * (value / 0.5));
            b = (int) (0xFF * (1.0 - value / 0.5));
        } else {
            // Interpolate between green and red
            r = (int) (0xFF * ((value - 0.5) / 0.5));
            g = (int) (0xFF * (1.0 - (value - 0.5) / 0.5));
            b = 0;
        }

        return (r << 16) | (g << 8) | b;

//        double ppsmNorm = (ap - minPPSM) / pPSMRange;
//        int color = (int) (0xFFFFFF * ppsmNorm);
//        return color;
    }

    private double distance(int x1, int x2, int y1, int y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)); // Euclidian distance
        // Math.abs(x1 - x2) + Math.abs(y1 - y2); // Manhattan distance
    }

    public void paint(Graphics g) { // invoked automatically upon component, eg JFrame, instantiation
        g.drawImage(I, 0, 0, this);
    }
    public static BufferedImage blur(BufferedImage image, int radius) {
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                result.setRGB(x, y, averageColor(image, x, y, radius));
            }
        }

        return result;
    }

    private static int averageColor(BufferedImage image, int x, int y, int radius) {
        int redTotal = 0;
        int greenTotal = 0;
        int blueTotal = 0;
        int count = 0;

        for (int i = -radius; i <= radius; i++) {
            for (int j = -radius; j <= radius; j++) {
                int newX = x + j;
                int newY = y + i;
                if (newX >= 0 && newX < image.getWidth() && newY >= 0 && newY < image.getHeight() &&
                        Math.sqrt(j*j + i*i) <= radius) { // Ensure pixels are inside a circle
                    Color pixelColor = new Color(image.getRGB(newX, newY));
                    redTotal += pixelColor.getRed();
                    greenTotal += pixelColor.getGreen();
                    blueTotal += pixelColor.getBlue();
                    count++;
                }
            }
        }

        if (count == 0) return new Color(0, 0, 0).getRGB();

        int averageRed = redTotal / count;
        int averageGreen = greenTotal / count;
        int averageBlue = blueTotal / count;

        return new Color(averageRed, averageGreen, averageBlue).getRGB();
    }

    private int[] getLocationAP(double latitude, double longitude){
        int x = longitudeToX(longitude);
        int y = latitudeToY(latitude);
        int[] coords = {x, y};
        return coords;
    }

}
