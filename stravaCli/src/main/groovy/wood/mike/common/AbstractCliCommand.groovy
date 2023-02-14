package wood.mike.common

import com.google.gson.Gson
import com.google.gson.JsonParser

class AbstractCliCommand {

    public static final Gson PRETTY_GSON = new Gson().newBuilder()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create();

    void prettyPrintJsonResponse(String resp) {
        println PRETTY_GSON.toJson(JsonParser.parseString(resp))
    }
}
