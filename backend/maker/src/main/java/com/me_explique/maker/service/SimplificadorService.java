package com.me_explique.maker.service;


import com.google.gson.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.*;
import java.io.IOException;

@Service
public class SimplificadorService {

    @Value("${together.api.key}")
    private String apiKey;

    private static final String API_URL = "https://api.together.xyz/v1/chat/completions";
    private final HttpClient client = HttpClient.newHttpClient();

    public String simplificarTexto(String textoOCR) throws IOException, InterruptedException {
   
    	String prompt = """
    			Você é um assistente especializado em acessibilidade para pessoas neurodivergentes, como autistas e disléxicos.

    			Simplifique e reescreva o texto abaixo em linguagem clara, direta e fácil de entender, com frases curtas.

    			Evite metáforas, expressões complexas e frases longas. Use termos simples e explique instruções ou sentimentos, se houver.

    			Se for um texto para comunicação (ex: e-mail), faça uma versão simples que possa ser usada diretamente.

    			Responda apenas com o texto simplificado e realize correções de acordo com a norma padrão da lingua portuguesa,
    			
    			sem explicações ou comentários.

    			Texto original:
    			\"\"\"%s\"\"\"
    			""".formatted(textoOCR);



  
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", "deepseek-ai/DeepSeek-V3");

        JsonArray messages = new JsonArray();

        JsonObject userMessage = new JsonObject();
        userMessage.addProperty("role", "user");
        userMessage.addProperty("content", prompt);

        messages.add(userMessage);
        requestBody.add("messages", messages);

        requestBody.addProperty("temperature", 0.7);


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(requestBody)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


        return extrairResposta(response.body());
    }

    private String extrairResposta(String json) {
        JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
        JsonArray choices = obj.getAsJsonArray("choices");

        if (choices != null && choices.size() > 0) {
            JsonObject message = choices.get(0).getAsJsonObject().getAsJsonObject("message");
            return message.get("content").getAsString().trim();
        }

        return "Não foi possível simplificar o texto.";
    }


}
