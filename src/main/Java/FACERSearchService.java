import java.util.ArrayList;

public class FACERSearchService   {

    private static FACERSearchService instance = null;

    private FACERSearchService() {}

    public static FACERSearchService getInstance() {
        if (instance == null) {
            instance = new FACERSearchService();
        }
        return instance;
    }

    public ArrayList getRecommendationsForQuery(String query) {

        ArrayList results = new ArrayList();
        results.add("Item 11");
        results.add("Item 12");
        results.add("Item 13");
        results.add("Item 14");
        results.add("Item 15");
        return results;
    }
}
