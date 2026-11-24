package com.zakariaabdelbaki.tp2.llm;

import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.google.genai.GoogleGenAiChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.enterprise.context.Dependent;

import java.io.Serial;
import java.io.Serializable;

@Dependent
public class LlmClient implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String systemRole;
    private final Assistant assistant;
    private final ChatMemory chatMemory;

    public LlmClient() {
        String cle = System.getenv("GEMINI_KEY");
        if (cle == null || cle.isBlank()) {
            throw new IllegalStateException("GEMINI_KEY manque dans l’environnement du serveur.");
        }
        ChatModel modele = GoogleGenAiChatModel.builder()
                .apiKey(cle)
                .modelName("gemini-3.1-flash-lite")
                .maxRetries(0)
                .build();
        chatMemory = MessageWindowChatMemory.withMaxMessages(20);
        assistant = AiServices.builder(Assistant.class)
                .chatModel(modele)
                .chatMemory(chatMemory)
                .build();
    }

    public void setSystemRole(String role) {
        systemRole = role;
        chatMemory.clear();
        chatMemory.add(SystemMessage.from(systemRole));
    }

    public String envoyerRequete(String question) {
        return assistant.chat(question);
    }
}
