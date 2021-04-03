import com.google.gson.Gson;
import methodsearch.StudentsEvaluatorStage1;
import org.json.simple.JSONArray;

import java.util.ArrayList;

public class FACERSearchService   {

    private static FACERSearchService instance = null;
    private static ArrayList<Method> querySearchResults;
    private static ArrayList<Method> relatedSearchResults;

    private FACERSearchService() {}

    public static FACERSearchService getInstance() {
        if (instance == null) {
            instance = new FACERSearchService();
        }
        return instance;
    }

    public ArrayList getRecommendationsForQuery(String query) {
        ArrayList methodNames = new ArrayList();
        try {
            StudentsEvaluatorStage1 studentsEvaluator = new StudentsEvaluatorStage1();
            JSONArray results = studentsEvaluator.searchFACERStage1Methods(query, 10, "jdbc:mysql://localhost/FACER_test?useSSL=false&user=root", "D:\\FACER\\FACER_Artifacts\\FACER_Replication_Pack\\stopwords.txt", "D:\\FACER\\FACER_Artifacts\\FACER_Replication_Pack\\LuceneSearchIndex");
            if (results != null) {
                querySearchResults = new ArrayList();
                int len = results.size();
                for (int i = 0; i < len; i++){
                    Object methodJson = results.get(i);
                    Method method = new Gson().fromJson(methodJson.toString(), Method.class);
                    querySearchResults.add(method);
                    methodNames.add(method.id + ": " + method.name);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return methodNames;
    }

    public ArrayList getRelatedMethods(int methodId) {

        ArrayList results = new ArrayList();
        results.add("getMethodsListMouseAdapter");
        results.add("getMethodsListMouseAdapter");
        results.add("getMethodsListMouseAdapter");
        results.add("getMethodsListMouseAdapter");
        results.add("getMethodsListMouseAdapter");
        results.add("getMethodsListMouseAdapter");
        results.add("getMethodsListMouseAdapter");
        results.add("getMethodsListMouseAdapter");
        results.add("getMethodsListMouseAdapter");
        results.add("getMethodsListMouseAdapter");
        return results;
    }

    public Method getQueryResultMethod(int index) {
        if (index < querySearchResults.size()) {
            return querySearchResults.get(index);
        }
        return null;
    }

    public String getRelatedMethodBody(int index) {
        String methodBody = "";
        if (relatedSearchResults.size() < index) {
            return relatedSearchResults.get(index).body;
        }
        return methodBody;
    }
}
