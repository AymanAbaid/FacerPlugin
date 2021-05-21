import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;

import static java.lang.System.currentTimeMillis;

public class EventLoggerService {


    ArrayList<Event> Event_INFO;
    String file = "";
    private static EventLoggerService instance = null;

    private EventLoggerService() {
        Event_INFO = new ArrayList<Event>();
        Event_INFO.add(new Event(0, "QUERY_SEARCH_INVOCATION"));
        Event_INFO.add(new Event(1, "QUERY_SEARCH_RETURN"));
        Event_INFO.add(new Event(2, "QUERY_SEARCH_METHOD_CLICK"));
        Event_INFO.add(new Event(3, "QUERY_SEARCH_METHOD_USED"));
        Event_INFO.add(new Event(4, "RELATED_SEARCH_INVOCATION"));
        Event_INFO.add(new Event(5, "RELATED_SEARCH_RETURN"));
        Event_INFO.add(new Event(6, "RELATED_SEARCH_METHOD_CLICK"));
        Event_INFO.add(new Event(7, "RELATED_SEARCH_METHOD_USED"));
        Event_INFO.add(new Event(8, "RELATED_SEARCH_METHOD_UPVOTE"));

    }

    public static EventLoggerService getInstance(){
        if (instance == null){
            instance = new EventLoggerService();
            instance.file = FACERConfigurationComponent.getInstance().getLogFilePath() + "/log.csv";
        }

        return instance;

    }

    public void log(int Event_ID, ArrayList<String> Params) {
        int i=0;
        Timestamp timestamp = new Timestamp(currentTimeMillis());

        StringBuilder string_builder = new StringBuilder();
        string_builder.append(timestamp + ",");

        if (this.Event_INFO.get(Event_ID).ID == Event_ID) {
            Event e = Event_INFO.get(Event_ID);
            string_builder.append("ID:" + e.ID + ",");
            string_builder.append("Event_Type:" + e.Type + ",");
            for (i = 0; i < Params.size()-1; i++) {
                string_builder.append(Params.get(i) + ",");
            }
            string_builder.append(Params.get(i));

            try
            {
                System.out.println(string_builder);
                FileWriter fw = new FileWriter(file, true);
                PrintWriter pw = new PrintWriter(fw, true);
                pw.println(string_builder);
                pw.close();
            }
            catch (IOException ioe ) {
                System.out.println(ioe);
            }
        }





    }
}
