import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class Realtor_caScanner extends WebsiteScanner{
    public Realtor_caScanner(){
        websiteName = "realtor.ca";
    }

    public void scanMarket() {
        String url = "test";
        System.out.println("URL: " + url);
        //String html = WebsiteScanner.getHtml(url);
    }

    public void scanHtml(String urlString) {

    }

    public String generateNextURL(GeographicDivision gd) {
        return null;
    }

    public String generateNextURL() {
        return null;
    }

    private static Coordinate postalCodeToCoords(String postalCode){
        Coordinate coords = new Coordinate(1.0, 1.0);
        return coords;
    };
    protected void scanRentalsByCoords(Coordinate coords){

    };
    protected void scanSalesByCoords(Coordinate coords){

    };
}
