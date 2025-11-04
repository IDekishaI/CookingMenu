package com.CM.CookingMenu.foodmenu.ai.services;

import com.CM.CookingMenu.common.utils.JsonUtils;
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

    private String buildPrompt(FoodMenuSuggestionRequestDTO request) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Generate lunch menu plans for 5 days of the week starting on ");
        prompt.append(request.startDate());
        if (request.preferredIngredients() != null) {
            prompt.append(". Preferred ingredients are: ");
            prompt.append(String.join(", ", request.preferredIngredients()));
        }
        if (request.avoidIngredients() != null) {
            prompt.append(". Ingredients to avoid are: ");
            prompt.append(String.join(", ", request.avoidIngredients()));
        }
        if (request.budgetContstraint() != null)
            prompt.append(". Budget constraint is: ").append(request.budgetContstraint());
        if (request.nutritionalFocus() != null)
            prompt.append(". Nutritional focus is: ").append(request.nutritionalFocus());
        if (request.fastingFriendlyRequired() != null)
            if (request.fastingFriendlyRequired())
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

            content = JsonUtils.extractJsonFromText(content);

            JsonNode menuJson = objectMapper.readTree(content);
            JsonNode weeklyMenu = menuJson.path("weeklyMenu");

            List<DailyMenuSuggestion> dailyMenuSuggestions = new ArrayList<>();

            JsonNode dailyMenusNode = weeklyMenu.path("dailyMenus");

            for (int i = 0; i < 5; i++) {
                JsonNode dailyMenu = dailyMenusNode.get(i);

                String date = dailyMenu.path("date").asText();

                List<String> dishes = new ArrayList<>();
                JsonNode dishesNode = dailyMenu.path("suggestedDishes");
                if (dishesNode.isArray())
                    for (JsonNode dish : dishesNode)
                        dishes.add(dish.asText());

                String theme = dailyMenu.path("theme").asText("Special Day");

                Boolean fastingFriendly = dailyMenu.path("isFastingFriendly").asBoolean(false);

                DailyMenuSuggestion suggestion = new DailyMenuSuggestion(date, dishes, theme, fastingFriendly);

                dailyMenuSuggestions.add(suggestion);
            }


            JsonNode nutritionNode = weeklyMenu.path("nutritionSummary");
            Double averageCalPD = nutritionNode.path("averageCaloriesPerDay").asDouble(0);
            Double averageCbPD = nutritionNode.path("averageCarbsPerDay").asDouble(0);
            Double averageFPD = nutritionNode.path("averageFatPerDay").asDouble(0);
            Double averagePPD = nutritionNode.path("averageProteinPerDay").asDouble(0);
            String balanceRating = nutritionNode.path("balanceRating").asText("Good");

            WeeklyNutritionSummary weeklyNutritionSummary = new WeeklyNutritionSummary(averageCalPD, averageCbPD, averageFPD, averagePPD, balanceRating);
            Double costEstimatePerPerson = weeklyMenu.path("costEstimatePerPerson").asDouble();

            String notes = weeklyMenu.path("notes").asText("AI-Generated Weekly Plan");

            return new FoodMenuSuggestionResponseDTO(dailyMenuSuggestions, weeklyNutritionSummary, costEstimatePerPerson, notes);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Gemini AI response: " + e.getMessage());
        }
    }

    public FoodMenuSuggestionResponseDTO generateFoodMenuSuggestion(FoodMenuSuggestionRequestDTO request) {
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
                                    "maxOutputTokens", 8000  // Increased for weekly menus
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
