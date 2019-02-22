package pl.kemp.subtrans.Service;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.concurrent.RecursiveTask;

@Service
public class DeepLService implements TranslationService {
    @Override
    public String translateWord(String word) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("https://www2.deepl.com/jsonrpc");

        String jsonRequest = "{\"jsonrpc\":\"2.0\",\"method\": \"LMT_handle_jobs\",\"params\":{\"jobs\":[{\"kind\":\"default\",\"raw_en_sentence\":\""+word+"\",\"raw_en_context_before\":[],\"raw_en_context_after\":[]}],\"lang\":{\"user_preferred_langs\":[\"EN\"],\"source_lang_user_selected\":\"auto\",\"target_lang\":\"PL\"},\"priority\":1,\"timestamp\":1536432903162},\"id\":86380001}";
        StringEntity entity = new StringEntity(jsonRequest);
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");

        CloseableHttpResponse response = client.execute(httpPost);
        InputStreamReader foo = new InputStreamReader(response.getEntity().getContent());
        String inputLine ;
        BufferedReader br = new BufferedReader(foo);
        StringBuilder json = new StringBuilder();
        while ((inputLine = br.readLine()) != null) {
            json.append(inputLine);
        }
        client.close();
        JsonParser jsonParser = new JsonParser();
        return jsonParser.getTranslation(json.toString());

    }
}
