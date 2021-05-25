import com.google.gson.annotations.SerializedName;

public class Method {
    @SerializedName("ID")
    public int id;

    @SerializedName("Name")
    public String name;

    @SerializedName("Body")
    public String body;

    @SerializedName("Algo")
    public String algo;
}
