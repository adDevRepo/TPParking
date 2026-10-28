# Parc Horizon – calcul du prix à la sortie

Java 17 et Maven.

- Lancer les tests : `mvn test`
- Simuler la borne de sortie : `mvn compile` puis `java -cp target/classes fr.parchorizon.Main`

Le dossier `acceptance/` contient les tests d'acceptation de la demande d'évolution.
Ils ne compilent pas tant que la nouvelle méthode n'existe pas : ne les ajoutez à
`src/test/java/fr/parchorizon/` qu'à l'étape 4.
