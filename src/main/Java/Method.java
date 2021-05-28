import com.google.gson.annotations.SerializedName;

public class Method {

    public final int TYPE_QUERY = 0;
    public final int TYPE_RELATED = 1;
    public final int TYPE_CALLED = 2;

    @SerializedName("ID")
    public int id;

    @SerializedName("Name")
    public String name;

    @SerializedName("Body")
    public String body;

    @SerializedName("Algo")
    public String algo;

    private int type;

    public void setType(String typeString) {
        switch (typeString) {
            case "query":
                type = TYPE_QUERY;
                break;
            case "related":
                type = TYPE_RELATED;
                break;
            case "called":
                type = TYPE_CALLED;
                break;
            default:
                type = TYPE_QUERY;
                break;
        }
    }

    public boolean isQueryMethod(){
        return type == TYPE_QUERY;
    }

    public boolean isRelatedMethod(){
        return type == TYPE_RELATED;
    }

    public boolean isCalledMethod(){
        return type == TYPE_CALLED;
    }
}
