import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedList;

public class LamundiScanner extends WebsiteScanner {
    private String currentProvince;
    private LinkedList<String> urlsToVisit;
    private int citiesInCurrentProvince;
    private String currentCity;
    private int currentCityIndex;
    private String currentURL;
    private int pagesInCurrentURL;
    private int page;
    private GeographicDivision phGeoDivisions;
    private City cityProperties;

    public LamundiScanner() {
        page = 1;
        cityProperties = new City();
        System.out.println("Please select which country you would like to scan:");
        urlsToVisit = new LinkedList<>(); // each url is a different city
        urlsToVisit = FileScanner.scanFileForLinks("C:\\Users\\llimge\\Documents\\Mcgill Classes\\REE Project\\REE 3 Actual\\urlCollection\\lamudi.com.ph\\RentHouse.html"); // SCAN EVERYTHING
        // urlsToVisit = FileScanner.scanFileForLinks("C:\\Users\\llimge\\Documents\\Mcgill Classes\\REE Project\\REE 3 Actual\\urlCollection\\lamudi.com.ph\\metro-manila.html");
        // urlsToVisit.add("https://www.lamudi.com.ph/metro-manila/quezon-city/rent/");

    }

    public LamundiScanner(TransactionType transactionType, PropertyType propertyType) {
        page = 1;
        cityProperties = new City();
        System.out.println("Please select which country you would like to scan:");
        urlsToVisit = FileScanner.scanPhilippines(transactionType, propertyType);
        for (String str : urlsToVisit) {
            System.out.println(str);
        }
    }

    public void scanMarket() {
        Document html;
        //cityProperties = new City("philippines", "metro-manila", "all-cities");
        for (String urlString : urlsToVisit) {
            System.out.println(cityProperties.rBpropertyBST.size() + " properties collected");
            System.out.println(urlString);
            cityProperties = new City("philippines", getSubdirectory(urlString, 0), getSubdirectory(urlString, 1));
            html = getHtmlDoc(urlString);
            scanHtml(html);
            Elements pageElement = html.select("select.sorting.nativeDropdown.js-pagination-dropdown");
            pagesInCurrentURL = Integer.parseInt(pageElement.attr("data-pagination-end"));
            System.out.println("Total pages = " + pagesInCurrentURL);
            for (int i = 1; i <= pagesInCurrentURL; i++) {
                html = getHtmlDoc(urlString + "?page=" + i);
                System.out.println("page: " + page++);
                scanHtml(html);
            }
            page = 1;
            cityProperties.saveCityCSV("C:\\Users\\llimge\\Documents\\Mcgill Classes\\REE Project\\REE 3 Actual\\PhilippinesProperties\\"
                    + cityProperties.getProvinceStateName(), cityProperties.getCityName() + ".csv");
            cityProperties.removeAreaPriceOutliers();
            cityProperties.removeSpatialOutliers(10);
            CityMapperVoronoi cityPngSave = new CityMapperVoronoi(cityProperties);
        }
        // for city by city, remove 2 lines below and uncomment 2 lines above
//        cityProperties.saveCityCSV("C:\\Users\\llimge\\Documents\\Mcgill Classes\\REE Project\\REE 3 Actual\\PhilippinesProperties\\"
//                    + cityProperties.getProvinceStateName(), "all-cities-by-url" + ".csv");
        //scanHtml("https://www.lamudi.com.ph/metro-manila/quezon-city/house/buy/?page=100");
        //cityProperties.printProperties();
    }
    public void scanHtml(Document html) {
        Elements propertyElements = WebsiteScanner.getPropertyElements(html, "div.row.ListingCell-row.ListingCell-agent-redesign");
        // div.ListingCell-AllInfo.ListingUnit

        int counter;
        double price;
        int bedrooms;
        double bathrooms;
        double floorArea;
        double landSize;
        double latitude;
        double longitude;
        boolean furnished;
        String propertyUrl;
        Property property;
        String[] latLongPair;
        for (Element propertyElement : propertyElements) {
            // get values for property
            counter = 0;
            price = 0;
            bedrooms = 0;
            bathrooms = 0;
            floorArea = 0;
            landSize = 0;
            latitude = 0;
            longitude = 0;
            furnished = false;
            propertyUrl = "";

            Elements infoElement = propertyElement.select("div.ListingCell-AllInfo.ListingUnit");

            for (Attribute attribute : infoElement.remove(0).attributes()){
                switch (attribute.getKey()){
                    case "data-price":
                        price = Double.parseDouble(attribute.getValue());
                        break;
                    case "data-bedrooms":
                        bedrooms = (int) Double.parseDouble(attribute.getValue());
                        break;
                    case "data-bathrooms":
                        bathrooms = Double.parseDouble(attribute.getValue());
                        break;
                    case "data-building_size":
                        floorArea = Double.parseDouble(attribute.getValue());
                        break;
                    case "data-land_size":
                        landSize = Double.parseDouble(attribute.getValue());
                        break;
                    case "data-geo-point":
                        // clean up string then split by comma
                        latLongPair = attribute.getValue().replaceAll("\\[|\\]", "").split(",");

                        // Convert the strings to doubles
                        longitude = Double.parseDouble(latLongPair[0]);
                        latitude = Double.parseDouble(latLongPair[1]);
                }
                // System.out.println(counter + ":::" + attribute.toString());
                counter ++;
            }

            // get property url
            Elements urlElement = propertyElement.select("a[href]");
            for (Attribute attribute : urlElement.remove(0).attributes()) {
                if (attribute.getKey().equals("href"))
                    propertyUrl = attribute.toString();
            }
            property = new Property(price, bedrooms, bathrooms, landSize, floorArea, latitude, longitude, propertyUrl);
            cityProperties.addProperty(property);
            //System.out.println(property);
            //System.out.println("-------------------------------------------------------------------------------------");
        }
    }

    public City getCityProperties() {
        return cityProperties;
    }
    public String generateNextURL(GeographicDivision gd) {
        if (page == pagesInCurrentURL)
            System.out.println("No more pages here");
        return null;
    }


}
