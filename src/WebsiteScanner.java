import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/*
Each of the classes that inherit from website scanner only scan one html at a time
 */
public abstract class WebsiteScanner {
    protected String websiteName;
    protected City currentCity;
    protected static String getHtmlStr(String urlString) {
        String html = "";
        try {
            Document document = Jsoup.connect(urlString).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                    "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36").referrer("http://www." +
                    "google.com").get();
            html = document.outerHtml();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return html;
    }

    protected static Document getHtmlDoc(String urlString) {
        try {
            Document document = Jsoup.connect(urlString).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                    "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36").referrer("http://www." +
                    "google.com").get();
            return document;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected static Elements getPropertyElements(Document html, String cssQuery) {
        try {
            return html.select(cssQuery); // replace spaces with dots
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void testPrintHtml(String urlString) throws Exception {
        String htmlStr = getHtmlStr(urlString);
        System.out.println(htmlStr);
    }

    public static void testSaveUrlAsHtml(String urlString) {
        String fileName = "chegg_cheat.txt";
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(getHtmlStr(urlString));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getSubdirectory(String urlString, int subdirectoryIndex) {
        String[] pathParts = urlString.split("/");
        if (pathParts.length > 4 + subdirectoryIndex) { // url should end in /
            return pathParts[3 + subdirectoryIndex];
        }
        return null;
    }

    public abstract void scanMarket();
    public abstract  void scanHtml(Document html);
    public abstract  String generateNextURL(GeographicDivision gd);
}
