import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class FileScanner {
    public static LinkedList<String> scanFileForLinks(String filepath) {
        Document document;
        //Get Document object after parsing the html from given url.
        document = Jsoup.parse(readFileAsString(filepath));
        //Get links from document object.
        Elements links = document.select("a[href]");
        LinkedList<String> urls = new LinkedList<>();
        //Iterate links and print link attributes.
        for (Element link : links) {
            urls.add(link.attr("href"));
        }
        return urls;
    }

    public static LinkedList<String> scanPhilippines(TransactionType transactionType, PropertyType propertyType) {
        String filePath = "C:\\Users\\llimge\\Documents\\Mcgill Classes\\REE Project\\REE 3 Actual\\urlCollection\\lamudi.com.ph\\";
        String fileName = "";
        switch (transactionType) {
            case RENTAL:
                fileName += "Rent";
                break;
            case SALE:
                fileName += "Sale";
                break;
        }
        switch (propertyType) {
            case APARTMENT:
                fileName += "Apartment";
            case COMMERCIAL:
                fileName += "Commercial";
            case CONDOMINIUM:
                fileName += "Condominium";
            case HOUSE:
                fileName += "House";
        }
        fileName += ".html";
        return scanFileForLinks(filePath + fileName);
    }
    public static LinkedList<String> scanPhilippinesRent() {
        return scanFileForLinks("C:\\Users\\llimge\\Documents\\Mcgill Classes\\REE Project\\REE 3 Actual\\urlCollection\\lamudi.com.ph_links_rent.html");
    }
    public static String readFileAsString(String filepath) {
        StringBuilder contentBuilder = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {

            String line;
            while ((line = br.readLine()) != null) {
                contentBuilder.append(line).append("\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return contentBuilder.toString();
    }

    public static String getFolderOrFileFromPath(String filePath, int indexFromEnd) {
        String[] parts = filePath.split("\\\\");

        // Check if the index is valid.
        if (indexFromEnd < 0 || indexFromEnd >= parts.length) {
            return null;
        }

        return parts[parts.length - 1 - indexFromEnd];
    }
}
