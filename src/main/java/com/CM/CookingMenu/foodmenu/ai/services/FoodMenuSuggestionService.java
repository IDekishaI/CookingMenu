package com.CM.CookingMenu.foodmenu.ai.services;

import com.CM.CookingMenu.foodmenu.ai.dtos.DailyMenuSuggestion;
import com.CM.CookingMenu.foodmenu.ai.dtos.FoodMenuSuggestionRequestDTO;
import com.CM.CookingMenu.foodmenu.ai.dtos.FoodMenuSuggestionResponseDTO;
import com.CM.CookingMenu.foodmenu.ai.dtos.WeeklyNutritionSummary;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FoodMenuSuggestionService {
    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private final WebClient.Builder webClientBuilder;
    private final ObjectMapper objectMapper;

    private String buildPrompt(FoodMenuSuggestionRequestDTO request){
        StringBuilder prompt = new StringBuilder();
        prompt.append("Generate lunch menu plans for 5 days of the week starting on ");
        prompt.append(request.getStartDate());
        if(request.getPreferredIngredients() != null) {
            prompt.append(". Preferred ingredients are: ");
            prompt.append(String.join(", ", request.getPreferredIngredients()));
        }
        if(request.getAvoidIngredients() != null) {
            prompt.append(". Ingredients to avoid are: ");
            prompt.append(String.join(", ", request.getAvoidIngredients()));
        }
        if(request.getBudgetContstraint() != null)
            prompt.append(". Budget constraint is: ").append(request.getBudgetContstraint());
        if(request.getNutritionalFocus() != null)
            prompt.append(". Nutritional focus is: ").append(request.getNutritionalFocus());
        if(request.getFastingFriendlyRequired() != null)
            prompt.append(". Requirement: All menus must be fasting-friendly (no meat, dairy or eggs)");

        prompt.append("\nGenerate a response in this EXACT JSON format:\n");
        prompt.append("{\n");
        prompt.append("  \"weeklyMenu\": {\n");
        prompt.append("    \"dailyMenus\": [\n");
        prompt.append("      {\n");
        prompt.append("        \"date\": \"2024-03-18\",\n");
        prompt.append("        \"dayName\": \"Monday\",\n");
        prompt.append("        \"suggestedDishes\": [\"Dish Name 1\", \"Dish Name 2\"],\n");
        prompt.append("        \"theme\": \"Italian Monday\",\n");
        prompt.append("        \"isFastingFriendly\": true,\n");
        prompt.append("      }\n");
        prompt.append("    ],\n");
        prompt.append("    \"nutritionSummary\": {\n");
        prompt.append("      \"averageCaloriesPerDay\": 650,\n");
        prompt.append("      \"averageProteinPerDay\": 25,\n");
        prompt.append("      \"averageCarbsPerDay\": 45,\n");
        prompt.append("      \"averageFatPerDay\": 15,\n");
        prompt.append("      \"balanceRating\": \"EXCELLENT\"\n");
        prompt.append("    },\n");
        prompt.append("    \"costEstimatePerPerson\": 8.50,\n");
        prompt.append("    \"notes\": \"Menu designed with balanced nutrition...\"\n");
        prompt.append("  }\n");
        prompt.append("}\n");

        prompt.append("\nGeneration guidelines:\n");
        prompt.append("- Ensure variety across the week\n");
        prompt.append("- Balance nutrition across all days\n");
        prompt.append("- Consider seasonal ingredients\n");
        prompt.append("- Provide 2-3 dishes per day\n");
        prompt.append("- Create appealing daily themes\n");
        prompt.append("- Estimate realistic portions for lunch meals\n");


        return prompt.toString();
    }
    private String extractJsonFromText(String text){
        int jsonStart = text.indexOf('{');
        int jsonEnd = text.lastIndexOf('}');

        if(jsonStart != -1 && jsonEnd != -1 && jsonEnd>jsonStart)
            return text.substring(jsonStart, jsonEnd + 1);

        return text;
    }
    private FoodMenuSuggestionResponseDTO parseGeminiResponse(String response) {
        try {
            JsonNode jsonResponse = objectMapper.readTree(response);

            JsonNode candidates = jsonResponse.path("candidates");
            if (candidates.isEmpty())
                throw new RuntimeException("No response from Gemini API");

            String content = candidates.get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();

            if (content.isEmpty())
                throw new RuntimeException("Empty content in Gemini response");

            content = extractJsonFromText(content);

            JsonNode menuJson = objectMapper.readTree(content);
            JsonNode weeklyMenu = menuJson.path("weeklyMenu");

            FoodMenuSuggestionResponseDTO result = new FoodMenuSuggestionResponseDTO();

            List<DailyMenuSuggestion> dailyMenuSuggestions = new ArrayList<>();

            JsonNode dailyMenusNode = weeklyMenu.path("dailyMenus");

            for (int i = 0; i < 5; i++) {
                JsonNode dailyMenu = dailyMenusNode.get(i);

                DailyMenuSuggestion suggestion = new DailyMenuSuggestion();
                suggestion.setDate(dailyMenu.path("date").asText());
                suggestion.setIsFastingFriendly(dailyMenu.path("isFastingFriendly").asBoolean(false));
                suggestion.setTheme(dailyMenu.path("theme").asText("Special Day"));

                List<String> dishes = new ArrayList<>();
                JsonNode dishesNode = dailyMenu.path("suggestedDishes");
                if (dishesNode.isArray())
                    for (JsonNode dish : dishesNode)
                        dishes.add(dish.asText());
                suggestion.setSuggestedDishes(dishes);
                dailyMenuSuggestions.add(suggestion);
            }
            result.setDailyMenuSuggestionList(dailyMenuSuggestions);


            JsonNode nutritionNode = weeklyMenu.path("nutritionSummary");
            WeeklyNutritionSummary weeklyNutritionSummary = new WeeklyNutritionSummary();
            weeklyNutritionSummary.setAverageCaloriesPerDay(nutritionNode.path("averageCaloriesPerDay").asDouble(0));
            weeklyNutritionSummary.setAverageCarbsPerDay(nutritionNode.path("averageCarbsPerDay").asDouble(0));
            weeklyNutritionSummary.setAverageFatPerDay(nutritionNode.path("averageFatPerDay").asDouble(0));
            weeklyNutritionSummary.setAverageProteinPerDay(nutritionNode.path("averageProteinPerDay").asDouble(0));
            weeklyNutritionSummary.setBalanceRating(nutritionNode.path("balanceRating").asText("Good"));
            result.setWeeklyNutritionSummary(weeklyNutritionSummary);

            Double costEstimatePerPerson = weeklyMenu.path("costEstimatePerPerson").asDouble();
            result.setCostEstimatePerPerson(costEstimatePerPerson);

            String notes = weeklyMenu.path("notes").asText("AI-Generated Weekly Plan");
            result.setNotes(notes);

            return result;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Gemini AI response: " + e.getMessage());
        }
    }
    public FoodMenuSuggestionResponseDTO generateFoodMenuSuggestion(FoodMenuSuggestionRequestDTO request){
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
                    .uri(uriBuilder -> uriBuilder
                            .path("/v1beta/models/gemini-2.5-flash:generateContent")
                            .queryParam("key", geminiApiKey)
                            .build())
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(Map.of(
                            "contents", List.of(
                                    Map.of("parts", List.of(
                                            Map.of("text", prompt)
                                    ))
                            ),
                            "generationConfig", Map.of(
                                    "temperature", 0.7,
                                    "maxOutputTokens", 2000  // Increased for weekly menus
                            )
                    ))
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError(), clientResponse -> {
                        return clientResponse.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    System.err.println("API Error Response: " + errorBody);
                                    return Mono.error(new RuntimeException("API Error: " + errorBody));
                                });
                    })
                    .bodyToMono(String.class)
                    .block();

            return parseGeminiResponse(response);

        } catch (Exception e) {
            throw new RuntimeException("Failed to get Gemini food menu suggestion: " + e.getMessage());
        }
    }
}
