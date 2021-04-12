import automated_evaluation.FACERStage2RelatedMethodsMaha;
import com.google.gson.Gson;
import methodsearch.StudentsEvaluatorStage1;
import org.json.simple.JSONArray;

import java.util.ArrayList;

public class FACERSearchService   {

    private static FACERSearchService instance = null;
    private static ArrayList<Method> querySearchResults;
    private static ArrayList<Method> relatedSearchResults;
    FACERConfigurationComponent configurationComponent = FACERConfigurationComponent.getInstance();

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
            JSONArray results = studentsEvaluator.searchFACERStage1Methods(query, 10, configurationComponent.getDatabaseURL(), configurationComponent.getStopwordsPath(), configurationComponent.getLucenePath());
            if (results != null) {
                querySearchResults = new ArrayList();
                relatedSearchResults = null;
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
        ArrayList methodNames = new ArrayList();
        try {
            FACERStage2RelatedMethodsMaha relatedMethodsEvaluator = new FACERStage2RelatedMethodsMaha();
            JSONArray relatedMethods = relatedMethodsEvaluator.getRelatedMethods(methodId, 10, configurationComponent.getDatabaseURL());
            if (relatedMethods != null) {
                relatedSearchResults = new ArrayList();
                int len = relatedMethods.size();
                for (int i = 0; i < len; i++){
                    Object methodJson = relatedMethods.get(i);
                    Method method = new Gson().fromJson(methodJson.toString(), Method.class);
                    relatedSearchResults.add(method);
                    methodNames.add(method.id + ": " + method.name);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return methodNames;
    }

    public Method getQueryResultMethod(int index) {
        if (index < querySearchResults.size()) {
            return querySearchResults.get(index);
        }
        return null;
    }

    public Method getRelatedMethod(int index) {
        if (index < relatedSearchResults.size()) {
            return relatedSearchResults.get(index);
        }
        return null;
    }
}
