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
        results.add("Item 1");
        results.add("Item 2");
        results.add("Item 3");
        results.add("Item 4");
        results.add("Item 5");
        results.add("Item 11");
        results.add("Item 12");
        results.add("Item 13");
        results.add("Item 14");
        results.add("Item 15");

        return results;
    }

    public ArrayList getRelatedMethods(String methodId) {

        ArrayList results = new ArrayList();
        results.add("Related Item 1");
        results.add("Related Item 2");
        results.add("Related Item 3");
        results.add("Related Item 4");
        results.add("Related Item 5");
        results.add("Related Item 11");
        results.add("Related Item 12");
        results.add("Related Item 13");
        results.add("Related Item 14");
        results.add("Related Item 15");
        return results;
    }
}
