package org.derewah.skriptgpt.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.derewah.skriptgpt.SkriptGPT;
import org.derewah.skriptgpt.types.ConversationMessage;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequest {


    public static String mapToJson(Boolean is_chat, Boolean echo, Object message, Integer max_tokens, String model, Number temperature) throws JsonProcessingException {
        // create the messages list


        Map<String, Object> mainMap = new HashMap<>();
        mainMap.put("max_tokens", max_tokens);
        mainMap.put("model", model);
        mainMap.put("temperature", temperature);

        if (is_chat && message instanceof String) {
            List<Map<String, String>> messages = new ArrayList<>();
            Map<String, String> userMessage = new HashMap<>();
            userMessage.put("role", "user");
            userMessage.put("content", (String) message);
            messages.add(userMessage);
            mainMap.put("messages", messages);
        }
        else if(is_chat && message instanceof ConversationMessage[]){
            ObjectMapper objectMapper = new ObjectMapper();
            ArrayList convs = new ArrayList<Map<String, String>>();
            for (ConversationMessage conv : (ConversationMessage[]) message){
                Map<String, String> mess = new HashMap<>();
                mess.put("role", conv.role);
                mess.put("content", conv.content);
                convs.add(mess);
            }
            mainMap.put("messages", convs);
        } else {
            mainMap.put("prompt", message);
            if (echo){mainMap.put("echo", echo);}
        }

        ObjectMapper mapper = new ObjectMapper();


        return mapper.writeValueAsString(mainMap);
    }

    public static String main(Boolean is_chat, Boolean echo, Object message, Integer max_tokens, String model, Number temperature) throws Exception {
        // create a URL object


        URL url = (is_chat ? new URL("https://api.openai.com/v1/chat/completions") : new URL("https://api.openai.com/v1/completions")) ;
        // open a connection to the URL
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // set the HTTP request method (GET by default)
        con.setRequestMethod("POST");

        con.setRequestProperty("Accept-Charset", "UTF-8");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("User-Agent", "Skript-GPT");
        con.setRequestProperty("Authorization", "Bearer "+ SkriptGPT.config.getString("openai_token"));

        con.setDoOutput(true);

        String postData = mapToJson(is_chat, echo, message, max_tokens, model, temperature);
        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.write(postData.getBytes(StandardCharsets.UTF_8));
        out.flush();
        out.close();


        int responseCode = con.getResponseCode();


        // read the response from the server
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();


        String contentString;

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response.toString());
        JsonNode contentNode;
        if (is_chat){
            contentNode = rootNode.path("choices").get(0).path("message").path("content");
        } else {
            contentNode = rootNode.path("choices").get(0).path("text"); //the response from the completion API are in a different JSON path than the Chat Completion endpoint.
        }
        contentString = contentNode.asText();



        return contentString;
    }
}
