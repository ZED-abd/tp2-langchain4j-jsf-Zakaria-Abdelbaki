package com.zakariaabdelbaki.tp2.jsf;

import com.zakariaabdelbaki.tp2.llm.LlmClient;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class Bb implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final String ROLE_ASSISTANT = "You are a helpful assistant. Answer the user's questions.";
    private static final String ROLE_TRADUCTEUR = """
            You are an interpreter. Translate French to English and English to French.
            For one to three words, also give examples of usage in English.
            """;
    private static final String ROLE_GUIDE = """
            You are a travel guide. For a country or city, suggest the main places to visit
            and give the average price of a meal.
            """;

    private String roleSysteme = ROLE_ASSISTANT;
    private boolean roleSystemeChangeable = true;
    private String question;
    private String reponse;
    private final StringBuilder conversation = new StringBuilder();

    @Inject
    private LlmClient llmClient;

    public String getRoleSysteme() {
        return roleSysteme;
    }

    public void setRoleSysteme(String roleSysteme) {
        this.roleSysteme = roleSysteme;
    }

    public boolean isRoleSystemeChangeable() {
        return roleSystemeChangeable;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getReponse() {
        return reponse;
    }

    public String getConversation() {
        return conversation.toString();
    }

    public List<SelectItem> getRolesSysteme() {
        return List.of(
                new SelectItem(ROLE_ASSISTANT, "Assistant"),
                new SelectItem(ROLE_TRADUCTEUR, "Traducteur Anglais-Français"),
                new SelectItem(ROLE_GUIDE, "Guide touristique"));
    }

    public String envoyer() {
        if (question == null || question.isBlank()) {
            signalerErreur("Il manque le texte de la question.");
            return null;
        }
        try {
            if (roleSystemeChangeable) {
                llmClient.setSystemRole(roleSysteme);
            }
            reponse = llmClient.envoyerRequete(question);
            roleSystemeChangeable = false;
            conversation.append("== User :\n").append(question)
                    .append("\n== Serveur :\n").append(reponse).append('\n');
        } catch (RuntimeException e) {
            signalerErreur("Gemini n’a pas pu répondre. Réessayez dans un instant.");
        }
        return null;
    }

    public String nouveauChat() {
        return "index?faces-redirect=true";
    }

    private void signalerErreur(String texte) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, texte, texte));
    }
}
