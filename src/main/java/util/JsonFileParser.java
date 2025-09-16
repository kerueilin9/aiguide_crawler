package util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.util.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class JsonFileParser {
    private JsonElement config;

    public JsonFileParser(String filePath) {
        try {
            Reader rd;
            if(filePath.toLowerCase().matches("http(.*)")) {
                InputStream is = new URL(filePath).openStream();
                rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                this.config = readJsonFile(rd);
                is.close();
            }
            else{
                rd = new FileReader(filePath);
                this.config = readJsonFile(rd);
            }
            rd.close();
        }catch (IOException e){
            e.printStackTrace();
            throw new RuntimeException("Json file path error!!");
        }
    }

    private JsonElement readJsonFile(Reader reader) {
//        return JsonParser.parseReader(reader);
        return new JsonParser().parse(reader);
    }

    private JsonElement getJsonFileValueAsJsonElement(String... elementNames) {
        JsonElement tmp = config;
        for(String str: elementNames) {
            tmp = tmp.getAsJsonObject().get(str);
        }
        return tmp;
    }

    private JsonArray getJsonFileValueAsJsonArray(String... elementNames) {
        JsonElement tmp;
        tmp = getJsonFileValueAsJsonElement(elementNames);
        return tmp.getAsJsonArray();
    }

    private Map<String, String> getJsonObjectValues(JsonObject jsonObject) {
        Map<String, String> data = new LinkedHashMap<>();
        for(String s: jsonObject.keySet()) {
            data.put(s, jsonObject.get(s).getAsString());
        }
        return data;
    }

    private Map<String, List<String>> getJsonObjectValuesAsArray(JsonObject jsonObject) {
        Map<String, List<String>> data = new LinkedHashMap<>();
        for(String s: jsonObject.keySet()) {
            JsonArray jsonArray = jsonObject.get(s).getAsJsonArray();
            List<String> strings = new ArrayList<>();
            for(JsonElement ele: jsonArray){
                strings.add(ele.getAsString());
            }
            data.put(s, strings);
        }
        return data;
    }

    public List<String> getJsonFileArray(String... elementNames) {
        JsonElement jsonElement = getJsonFileValueAsJsonElement(elementNames);
        List<String> arraryStrings = new ArrayList<>();
        for(JsonElement ele: jsonElement.getAsJsonArray()){
            arraryStrings.add(ele.getAsString());
        }
        return arraryStrings;
    }

    public String getJsonFileValue(String... elementNames) {
        JsonElement tmp = getJsonFileValueAsJsonElement(elementNames);
        return tmp.getAsString();
    }

    public Map<String, String> getJsonFileValues(String... elementNames) {
        JsonElement jsonElement = getJsonFileValueAsJsonElement(elementNames);
        return getJsonObjectValues(jsonElement.getAsJsonObject());
    }

    public Map<String, List<String>> getJsonFileValuesAsArray(String... elementNames) {
        JsonElement jsonElement = getJsonFileValueAsJsonElement(elementNames);
        return getJsonObjectValuesAsArray(jsonElement.getAsJsonObject());
    }

    public List<Map<String, String>> getJsonFileArrayValues(String... elementNames) {
        JsonArray jsonArray = getJsonFileValueAsJsonArray(elementNames);
        List<Map<String, String>> tmpData = new ArrayList<>();

        for (JsonElement a : jsonArray) {
            tmpData.add(getJsonObjectValues(a.getAsJsonObject()));
        }
        return tmpData;
    }

    public List<String> getAllKeys(String... elementNames) {
        JsonElement jsonElement = getJsonFileValueAsJsonElement(elementNames);
        return new ArrayList<>(jsonElement.getAsJsonObject().keySet());
    }

}
