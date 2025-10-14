package com.CM.CookingMenu.dish.ai.services;

import com.CM.CookingMenu.dish.ai.dtos.RecipeSuggestionRequestDTO;
import com.CM.CookingMenu.dish.ai.dtos.RecipeSuggestionResponseDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AIRecipeService {
    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private final WebClient.Builder webClientBuilder;
    private final ObjectMapper objectMapper;

    private String buildPrompt(RecipeSuggestionRequestDTO request){
        StringBuilder prompt = new StringBuilder();
        prompt.append("Generate 3 recipe suggestions using these ingredients: ");
        prompt.append(String.join(", ", request.availableIngredients()));
        if(request.mealType() != null)
            prompt.append(". Meal type: ").append(request.mealType());
        if(request.targetGroup() != null)
            prompt.append(". Target group: ").append(request.targetGroup());
        if(request.fastingFriendly() != null && request.fastingFriendly())
            prompt.append(". Must be fasting-friendly (no meat, dairy or eggs");

        prompt.append("\n\nRespond with JSON in this exact format: \n");
        prompt.append("{\n");
        prompt.append("  \"suggestions\": [\n");
        prompt.append("    {\n");
        prompt.append("      \"dishName\": \"Recipe Name\",\n");
        prompt.append("      \"requiredIngredients\": [\"ingredient1\", \"ingredient2\"],\n");
        prompt.append("      \"cookingInstructions\": \"Step by step instructions\",\n");
        prompt.append("      \"isFastingFriendly\": true,\n");
        prompt.append("    }\n");
        prompt.append("  ]\n");
        prompt.append("}");

        return prompt.toString();
    }
    private String extractJsonFromText(String text){
        int jsonStart = text.indexOf('{');
        int jsonEnd = text.lastIndexOf('}');

        if(jsonStart != -1 && jsonEnd != -1 && jsonEnd>jsonStart)
            return text.substring(jsonStart, jsonEnd + 1);

        return text;
    }
    private List<RecipeSuggestionResponseDTO> parseGeminiResponse(String response){
        try{
            JsonNode jsonResponse = objectMapper.readTree(response);

            JsonNode candidates = jsonResponse.path("candidates");
            if(candidates.isEmpty())
                throw new RuntimeException("No response from Gemini API");

            String content = candidates.get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();

            if(content.isEmpty())
                throw new RuntimeException("Empty content in Gemini response");

            content = extractJsonFromText(content);

            JsonNode suggestionsJson = objectMapper.readTree(content);
            JsonNode suggestions = suggestionsJson.path("suggestions");

            if(!suggestions.isArray())
                throw new RuntimeException("Suggestions is not an array in response");

            List<RecipeSuggestionResponseDTO> result = new ArrayList<>();

            for(JsonNode suggestion : suggestions){
                String dishName = suggestion.path("dishName").asText("Unknown Dish");

                List<String> ingredients = new ArrayList<>();
                JsonNode ingredientsNode = suggestion.path("requiredIngredients");

                if(ingredientsNode.isArray()){
                    for(JsonNode ingredient : ingredientsNode){
                        String ingredientName = ingredient.asText();
                        if(!ingredientName.isEmpty())
                            ingredients.add(ingredientName);
                    }
                }

                String cookingInstructions = suggestion.path("cookingInstructions").asText("No instructions provided");
                Boolean isFastingFriendly = suggestion.path("isFastingFriendly").asBoolean(false);

                RecipeSuggestionResponseDTO dto = new RecipeSuggestionResponseDTO(dishName, ingredients, cookingInstructions, isFastingFriendly);

                result.add(dto);
            }

            return result;
        }catch (Exception e){
            throw new RuntimeException("Failed to parse Gemini AI response: " + e.getMessage());
        }
    }
    public List<RecipeSuggestionResponseDTO> generateRecipeSuggestions(RecipeSuggestionRequestDTO request){
        if (geminiApiKey == null || geminiApiKey.isEmpty()) {
            throw new RuntimeException("Gemini API key not configured. Get one free at https://aistudio.google.com");
        }

        String prompt = buildPrompt(request);

        WebClient webClient = webClientBuilder
                .baseUrl("https://generativelanguage.googleapis.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        try {
            String response = webClient.post()
                    .uri("/v1beta/models/gemini-2.5-flash:generateContent?key=" + geminiApiKey)
                    .bodyValue(Map.of(
                            "contents", List.of(
                                    Map.of("parts", List.of(
                                            Map.of("text", prompt)
                                    ))
                            ),
                            "generationConfig", Map.of(
                                    "temperature", 0.7,
                                    "maxOutputTokens", 4000
                            )
                    ))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            return parseGeminiResponse(response);

        } catch (Exception e) {
            throw new RuntimeException("Failed to get Gemini recipe suggestions: " + e.getMessage());
        }
    }

}
