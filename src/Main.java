public class Main {
    public static void main(String[] args) {
        WebsiteScanner.testSaveUrlAsHtml("https://www.chegg.com/homework-help/questions-and-answers/let-f-x-cos-4x-2-1-x-2-evaluate-6th-derivative-x-0-hint-build-maclaurin-series-f-x-series--q3941293");

       /* City testCity = City.loadCityFromCSV("C:\\Users\\llimge\\Documents\\Mcgill Classes\\REE Project\\REE 3 Actual\\PhilippinesProperties\\metro-manila",
                "all-cities-by-url.csv");
//        testCity.printProperties();

        testCity.removeSpatialOutliers(50);

        testCity.removeAreaPriceOutliers();

        CityMapperVoronoi testCityMapper = new CityMapperVoronoi(testCity);
        //WebsiteScanner.testSaveUrlAsHtml("https://www.realtor.ca/map#ZoomLevel=16&Center=47.383113%2C-68.312017&LatitudeMax=47.38770&LongitudeMax=-68.29914&LatitudeMin=47.37853&LongitudeMin=-68.32489&Sort=6-D&PGeoIds=g30_f2rmt2ck&GeoName=Edmundston%2C%20NB&PropertyTypeGroupID=1&TransactionTypeId=2&PropertySearchTypeId=0&Currency=CAD");

        /*System.out.println("hi");
        LamundiScanner lamundiScanner = new LamundiScanner();
        lamundiScanner.scanMarket();
/*





        //CityMapper testCityMapper = new CityMapper(1920, 1080, testCity,
        //        14.364015, 120.744064, 14.851534, 121.364349);
        // LamundiScanner lamundiScanner = new LamundiScanner(TransactionType.RENTAL, PropertyType.HOUSE);
        // lamundiScanner.scanMarket();
        /*
        for (String str : FileScanner.scanPhilippines(TransactionType.RENTAL, PropertyType.HOUSE)) {
            System.out.println(str);
        }




        CityMapper testCityMapper = new CityMapper(1920, 1080, lamundiScanner.getCityProperties(),
        14.364015, 120.744064, 14.851534, 121.364349);

        /*


        // WebsiteScanner.testSaveUrlAsHtml("https://www.lamudi.com.ph/metro-manila/quezon-city/house/buy/?page=100");
        /*
        GeographicDivision philippines = GeographicDivision.loadPhilippines();
        Iterator<GeographicDivision> iterator = philippines.iterator();
        GeographicDivision division;

        while (iterator.hasNext()) {
            division = iterator.next();
            System.out.println(division.getLocationName());
        }
         */
    }
    public static String formatCityText(String text) {
        return "\""+ text.toLowerCase().replace("\n", "\",\"").replace(" ", "-") + "\"";

    }
}