package com.ws.inpe.util;

import com.ws.inpe.model.Status;
import org.json.JSONObject;

public class Util {

    /**
     * Formata o status para o formato Json
     *
     * @param status
     * @return JsonObject
     */
    public static JSONObject formatStatus(Status status) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", status.name());
        return jsonObject;
    }
}
