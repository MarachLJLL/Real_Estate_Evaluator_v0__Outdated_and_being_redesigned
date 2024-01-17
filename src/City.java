import java.util.Date;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class City {
    //    private TransactionType transactionType;
//    private PropertyType propertyType;
    protected TreeMap<String, Property> rBpropertyBST;
    private String countryName;
    private String cityName;
    private String provinceStateName;
    private double minLatitude;
    private double maxLatitude;
    private double minLongitude;
    private double maxLongitude;
    private int propertyCount;
    private double medLat;
    private double medLong;

    public City() {
        rBpropertyBST = new TreeMap<>();
        minLatitude = 91;
        maxLatitude = -1;
        minLongitude = 181;
        maxLongitude = -1;
    }

    public City(String countryName, String provinceStateName, String cityName) {
        rBpropertyBST = new TreeMap<>();
        minLatitude = 91;
        maxLatitude = -1;
        minLongitude = 181;
        maxLongitude = -1;
        this.countryName = countryName;
        this.provinceStateName = provinceStateName;
        this.cityName = cityName;
    }

    public String getProvinceStateName() {
        return provinceStateName;
    }
    public String getCityName() {
        return cityName;
    }

    public double getMinLatitude() { return minLatitude;}

    public double getMaxLatitude() {
        return  maxLatitude;
    }

    public double getMinLongitude() {
        return minLongitude;
    }

    public double getMaxLongitude() {
        return maxLongitude;
    }

    public int getPropertyCount() {
        return propertyCount;
    }

    public void addProperty(Property p) {
        if (p.getPrice() == 0 || p.getFloorArea() == 0 || p.getLatitude() == 0 || p.getLongitude() == 0) {
            System.out.println("yonkers");
            return;
        }
        rBpropertyBST.put(p.getUrl(), p);
        minLatitude = Math.min(minLatitude, p.getLatitude());
        maxLatitude = Math.max(maxLatitude, p.getLatitude());
        minLongitude = Math.min(minLongitude, p.getLongitude());
        maxLongitude = Math.max(maxLongitude, p.getLongitude());
        propertyCount++;
    }

    public void printProperties() {
        int count = 0;
        for (Property property : getPropertiesArray()) {
            System.out.println(count++ + ": " + property.toString());
        }
    }

    public Property[] getPropertiesArray() {
        Property[] properties = new Property[rBpropertyBST.size()];
        int i = 0;
        for (Property property : rBpropertyBST.values()) {
            properties[i] = property;
            i++;
        }
        return properties;
    }
    private double getMedianLatitude() {
        List<Double> latitudes = new ArrayList<>();
        for (Property property : rBpropertyBST.values()) {
            latitudes.add(property.getLatitude());
        }
        Collections.sort(latitudes);

        return latitudes.get(latitudes.size() / 2);
    }

    private double getMedianLongitude() {
        List<Double> longitudes = new ArrayList<>();
        for (Property property : rBpropertyBST.values()) {
            longitudes.add(property.getLongitude());
        }
        Collections.sort(longitudes);

        return longitudes.get(longitudes.size() / 2);
    }

    public static double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS = 6371; // Earth radius in kilometers (km)

        // Convert degrees to radians
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        // Haversine formula
        double dLat = lat2Rad - lat1Rad;
        double dLon = lon2Rad - lon1Rad;
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c; // returns distance in kilometers
    }

    private double calculateSTDFromMed(List<Double> givenList) {
        double sum = 0.0;
        double standardDeviation = 0.0;

        double median;
        Collections.sort(givenList);
        median = givenList.get(givenList.size() / 2);

        // Calculate the variance
        for (double num : givenList) {
            standardDeviation += Math.pow(num - median, 2);
        }

        // Finalize the calculation of standard deviation
        return Math.sqrt(standardDeviation / givenList.size());
    }
    public void removeSpatialOutliers(int maxDistanceKm) {
        int initialPropertyCount = propertyCount;
        List<Double> distances = new ArrayList<>();
        medLat = getMedianLatitude();
        medLong = getMedianLongitude();
        for (Property property : getPropertiesArray()) {
            double distance = haversineDistance(medLat, medLong, property.getLatitude(), property.getLongitude());
            if (distance < maxDistanceKm /*km*/) distances.add(distance);
        }
        double stdDistance = calculateSTDFromMed(distances);
        double outlierSTD = stdDistance * 3; // scuffed
        int count = 0;
        for (Property property : getPropertiesArray()) {
            if (haversineDistance(medLat, medLong, property.getLatitude(), property.getLongitude()) > outlierSTD){
                // System.out.println(count + "-" + haversineDistance(medLat, medLong, property.getLatitude(), property.getLongitude()));
                rBpropertyBST.remove(property.getUrl());
                count++;
                propertyCount--;
            }
        }
        minLatitude = 91;
        maxLatitude = -1;
        minLongitude = 181;
        maxLongitude = -1;
        for (Property p : getPropertiesArray()) {
            minLatitude = Math.min(minLatitude, p.getLatitude());
            maxLatitude = Math.max(maxLatitude, p.getLatitude());
            minLongitude = Math.min(minLongitude, p.getLongitude());
            maxLongitude = Math.max(maxLongitude, p.getLongitude());
        }
        System.out.println(initialPropertyCount - propertyCount + " area price outliers removed");
    }

    public void removeAreaPriceOutliers() {
        int initialPropertyCount = propertyCount;
        List<Double> areaPrices = new ArrayList<>();
        for (Property property : getPropertiesArray()) {
            areaPrices.add(property.getPricePerSqMeter());
        }
        Collections.sort(areaPrices);
        double medianAp = areaPrices.get(areaPrices.size() / 2);
        double stdDistance = calculateSTDFromMed(areaPrices);
        double outlierSTD = stdDistance * 1.5; // scuffed
        int count = 0;
        for (Property property : getPropertiesArray()) {
            if (Math.abs(medianAp - property.getPricePerSqMeter()) > outlierSTD){
                //System.out.println(count + "-" + property.getPricePerSqMeter());
                rBpropertyBST.remove(property.getUrl());
                count++;
                propertyCount--;
            }
        }
        minLatitude = 91;
        maxLatitude = -1;
        minLongitude = 181;
        maxLongitude = -1;
        for (Property p : getPropertiesArray()) {
            minLatitude = Math.min(minLatitude, p.getLatitude());
            maxLatitude = Math.max(maxLatitude, p.getLatitude());
            minLongitude = Math.min(minLongitude, p.getLongitude());
            maxLongitude = Math.max(maxLongitude, p.getLongitude());
        }
        System.out.println(initialPropertyCount - propertyCount + " area price outliers removed");
    }
    public void saveCityCSV(String filePath, String fileName) {
        File directory = new File(filePath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(directory, fileName)))) {
            // Adjust to reduce system calls
            writer.write("Price,Bedrooms,Bathrooms,LandSize,FloorArea,Latitude,Longitude,PricePerSqMeter,url\n");
            for (Property property : rBpropertyBST.values()) { // adjust buffer for this
                writer.write(property.toString() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static City loadCityFromCSV(String filePath, String fileName) {
        String provinceName = FileScanner.getFolderOrFileFromPath(filePath, 0);
        City city = new City("philippines", provinceName, fileName.split(".csv")[0]);
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(filePath, fileName)))) {
            // Skip the header line
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                Property property = Property.stringToProperty(line);
                city.addProperty(property);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return city;
    }
}
