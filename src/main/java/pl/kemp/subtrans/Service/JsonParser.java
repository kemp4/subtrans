package pl.kemp.subtrans.Service;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonParser {
    public String getTranslation(String json){
        JSONObject object = new JSONObject(json);
        JSONArray translations = object.getJSONObject("result")
                .getJSONArray("translations");
        JSONArray beams = translations.getJSONObject(0)
                .getJSONArray("beams");
        StringBuilder result= new StringBuilder();
        for(int i = 0 ; i < beams.length(); i++){
            result.append(" | "+beams.getJSONObject(i).getString("postprocessed_sentence"));
        }
        result.append(" |");
        return result.toString();
    }
}
