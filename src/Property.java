
public class Property {
    // for calculations:
    private int bedrooms;
    private double bathrooms; // not readily available on html unless pages are visited individually
    private double price;
    private double floorArea;
    private double latitude;
    private double longitude;
    private double pricePerSqMeter;
    private double landSize;
    private int constructionYear;

    // for tracking
    private String url;

    /*
    private  String FSA; // Forward sortation area
    private String websiteSource;
    private String province;
    private String city;
     */
    public Property(double price, int bedrooms, double bathrooms, double landSize, double floorArea, double latitude,
                    double longitude, String url) {
        this.price = price;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.floorArea = floorArea;
        this.latitude = latitude;
        this.longitude = longitude;
        this.url = url;
        pricePerSqMeter = price / floorArea;
    }

    public Property(String csvRow) {
        String[] fields = csvRow.split(",");
        // Parsing the values from the string array fields
        price = Double.parseDouble(fields[0]);
        bedrooms = Integer.parseInt(fields[1]);
        bathrooms = Double.parseDouble(fields[2]);
        landSize = Double.parseDouble(fields[3]);
        floorArea = Double.parseDouble(fields[4]);
        latitude = Double.parseDouble(fields[5]);
        longitude = Double.parseDouble(fields[6]);
        url = fields[7];
    }

    public double getPrice() {
        return price;
    }

    public double getFloorArea() {
        return floorArea;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getPricePerSqMeter() {
        return pricePerSqMeter;
    }
    public String getUrl() {
        return url;
    }

    public String toString() {
        return String.format("%.2f", price) + "," + bedrooms + "," + bathrooms + "," + landSize + "," + floorArea +
                "," + latitude + "," + longitude + "," + String.format("%.2f", pricePerSqMeter) + "," + url;
    }

    public static Property stringToProperty(String inputStr) {
        String[] fields = inputStr.split(",");

        // Parsing the values from the string array fields
        double sPrice = Double.parseDouble(fields[0]);
        int sBedrooms = Integer.parseInt(fields[1]);
        double sBathrooms = Double.parseDouble(fields[2]);
        double sLandSize = Double.parseDouble(fields[3]);
        double sFloorArea = Double.parseDouble(fields[4]);
        double sLatitude = Double.parseDouble(fields[5]);
        double sLongitude = Double.parseDouble(fields[6]);
        String sUrl = fields[7];

        // Creating a new Property object using the parsed values
        Property property = new Property(sPrice, sBedrooms, sBathrooms, sLandSize, sFloorArea, sLatitude, sLongitude, sUrl);
        return property;
    }
}
