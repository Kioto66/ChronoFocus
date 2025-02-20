package ru.k4nk.chronofocus.http_api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
public class JsonMapper {
    private final ObjectMapper mapper;

    public JsonMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public JsonNode listToJson(List<?> objects) {
        ArrayNode jsonNodes = mapper.createArrayNode();
        objects.forEach(jsonNodes::addPOJO);
        return mapper.convertValue(jsonNodes, ArrayNode.class);
    }

    public <T> List<T> jsonToList(ArrayNode arrayNode, Class<T> tClass) {
        List<T> items = new ArrayList<>(arrayNode.size());
        arrayNode.forEach(jsonNode -> items.add(mapper.convertValue(jsonNode, tClass)));
        return items;
    }

    public JsonNode emptyArray() {
        return mapper.createArrayNode();
    }

    public JsonNode longToJson(Long inLong) {
        return mapper.convertValue(inLong, ValueNode.class);
    }

    public JsonNode stringToJson(String string) {
        return mapper.convertValue(string, ValueNode.class);
    }

    public JsonNode mapToJson(Map<String, Object> map) {
        return mapper.convertValue(map, ObjectNode.class);
    }

    public <T> JsonNode objectToJson(T object) {
        return mapper.convertValue(object, JsonNode.class);
    }
}
