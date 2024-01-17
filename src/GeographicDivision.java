import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.HashSet;
import java.util.Set;
public class GeographicDivision {
    private GDNode node;
    private GeographicDivision firstSubdivision;
    private GeographicDivision next;
    private GeographicDivision updivision;
    private GeoDivType divType;
    private String locationName;
    private City cityContents;

    public GeographicDivision(String locationName, GeoDivType divType) {
        this.locationName = locationName;
        this.divType = divType;
        node = new GDNode(this);
        firstSubdivision = null;
        next = null;
        updivision = null;
    }

    public GeographicDivision(String locationName, GeoDivType divType, GeographicDivision updivision) {
        this.locationName = locationName;
        this.divType = divType;
        node = new GDNode(this);
        firstSubdivision = null;
        next = null;
        this.updivision = updivision;
    }
    public GeographicDivision getDirectSubdivision(String locationNameSearch) {
        GeographicDivision curr = this.firstSubdivision;
        while (curr != null) {
            if (curr.locationName.equalsIgnoreCase(locationNameSearch)) {
                return curr;
            }
            curr = curr.next;
        }
        return null;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setSubdivisions(String[] subdivisionNames, GeoDivType subdivType) {
        firstSubdivision = new GeographicDivision(subdivisionNames[0], subdivType, this);
        GeographicDivision currSubdivision = firstSubdivision;
        for (int i = 1; i < subdivisionNames.length; i++) {
            currSubdivision.next = new GeographicDivision(subdivisionNames[i], subdivType, this);
            currSubdivision = currSubdivision.next;
        }
    }



    public boolean setInnerSubdivisions(String locationNameSearch, String[] subdivisionNames, GeoDivType
            subSubdivType) {
        GeographicDivision searchDivision = getDirectSubdivision(locationNameSearch);
        if (searchDivision == null) return false;
        searchDivision.setSubdivisions(subdivisionNames, subSubdivType);
        return true;
    }

    public Iterator<GeographicDivision> iterator() {
        return new GDIterator();
    }

    private class GDNode {
        private GeographicDivision gd;

        private GDNode(GeographicDivision gd) {
            this.gd = gd;
        }
    }

    private class GDIterator implements Iterator<GeographicDivision> {
        GDNode curr;
        Set<GeographicDivision> visited;

        public GDIterator() {
            curr = node;
            visited = new HashSet<>();
        }

        public boolean hasNext() {
            return curr != null;
        }

        public GeographicDivision next() {
            if (!this.hasNext())
                throw new NoSuchElementException();
            GeographicDivision gd = curr.gd;
            visited.add(gd);
            if (curr.gd.firstSubdivision != null && !visited.contains(curr.gd.firstSubdivision)) {
                curr = curr.gd.firstSubdivision.node;
            } else {
                curr = findNextNode(curr);
            }
            return gd;
        }

        private GDNode findNextNode(GDNode curr) {
            if (curr.gd.next != null && !visited.contains(curr.gd.next)) {
                return curr.gd.next.node;
            } else if (curr.gd.next != null) {
                return  findNextNode(curr.gd.next.node);
            } else if (curr.gd.updivision != null) {
                return findNextNode(curr.gd.updivision.node);
            }
            return null;
        }
    }

    public static GeographicDivision loadPhilippines() {
        GeographicDivision philippines = new GeographicDivision("philippines", GeoDivType.COUNTRY);

        philippines.setSubdivisions(new String[]{"metro-manila", "cebu", "cavite", "laguna", "rizal", "davao-del-sur",
                "pampanga", "iloilo", "batangas", "bulacan", "misamis-oriental", "negros-oriental"}, GeoDivType.PROVINCE);
        philippines.setInnerSubdivisions("metro-manila", new String[]{"quezon-city", "makati", "taguig", "manila",
                "mandaluyong", "muntinlupa", "san-juan", "las-pinas", "paranaque", "pasig"}, GeoDivType.CITY);
        philippines.setInnerSubdivisions("cebu", new String[]{"cebu", "lapu-lapu", "mandaue", "naga",
                "talisay", "consolacion", "liloan", "minglanilla", "cordova"}, GeoDivType.CITY);
        philippines.setInnerSubdivisions("cavite", new String[]{"tagaygay", "imus", "dasmarinas",
                "bacoor", "alfonso", "general-trias", "tanza", "silang", "carmona", "naic"}, GeoDivType.CITY);
        philippines.setInnerSubdivisions("laguna", new String[]{"santa-rosa", "los-banos", "calamba",
                "san-pedro", "binan", "cabuyao", "san-pablo", "santa-cruz"}, GeoDivType.CITY);
        philippines.setInnerSubdivisions("rizal", new String[]{"antipolo", "taytay", "cainta",
                "san-mateo", "binangonan", "rodriguez", "teresa", "angono", "baras"}, GeoDivType.CITY);
        philippines.setInnerSubdivisions("davao-del-sur", new String[]{"davao", "digos", "malalag"},
                GeoDivType.CITY);
        philippines.setInnerSubdivisions("pampanga", new String[]{"angeles", "san-fernando", "mexico",
                "mabalacat", "arayat", "floridablanca", "magalang", "porac", "bacolor", "apalit"}, GeoDivType.CITY);
        philippines.setInnerSubdivisions("iloilo", new String[]{"iloilo", "santa-barbara", "guimbal"},
                GeoDivType.CITY);
        philippines.setInnerSubdivisions("batangas", new String[]{"santo-tomas", "batangas-city",
                "lipa", "nasugbu", "san-juan", "calatagan", "tanauan", "lian"}, GeoDivType.CITY);
        philippines.setInnerSubdivisions("bulacan", new String[]{"santa-maria", "san-jose-del-monte",
                "meycauayan", "malolos", "san-rafael", "plaridel", "marilao", "baliuag", "guiguinto"}, GeoDivType.CITY);
        philippines.setInnerSubdivisions("misamis-oriental", new String[]{"cagayan-de-oro", "alubijid",
                "tagaloan"}, GeoDivType.CITY);
        philippines.setInnerSubdivisions("negros-oriental", new String[]{"dumaguete", "valencia",
                "sibulan", "zamboanguita"}, GeoDivType.CITY);
        System.out.println(philippines.getDirectSubdivision("cebu").firstSubdivision.next.getLocationName());
        return philippines;
    }
}
