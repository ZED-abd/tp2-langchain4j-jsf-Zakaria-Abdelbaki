# Utilisation de l’IA

- Problème rencontré — adapter la page du TP1 à un service LangChain4j et à une mémoire de vue.
- Raison du recours à l’IA — réutiliser le code local, préparer le build et relever le résultat de la recette.
- Demande — « Exécuter intégralement PLAN_TP2.md » en gardant le client simple de l’énoncé.
- Réponse — le service IA utilise une fenêtre de 20 messages et le backing bean fixe le rôle en début de conversation.
- Limite constatée à la lecture du code — la classe CDI déclare `Serializable`, mais le proxy IA et la mémoire d’exécution ne sont pas conçus pour une sérialisation complète de la vue. Une passivation ou une réplication qui tente de sérialiser ces champs reste hors du scénario visé.
- Ce que j’ai appris — à compléter par Zakaria après la recette et sa relecture.
