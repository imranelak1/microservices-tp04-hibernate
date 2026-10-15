# TP4 — Hibernate et MySQL

J'ai réalisé la gestion des salles et des machines avec Hibernate 5.6.15 et MySQL 8. Les entités `Salle` et `Machine` ont une relation bidirectionnelle. Chaque entité dispose d'un DAO et d'un service qui implémentent `IDao<T>`. `HibernateUtil` partage une seule `SessionFactory`.

J'utilise Java 17 et Maven. Pour lancer le projet :

```sh
docker compose up -d --wait
mvn clean test
mvn exec:java
```

La base `db_tp_hibernate` écoute sur 13317, avec `root` et le mot de passe local `labroot`. `DB_URL`, `DB_USER` et `DB_PASSWORD` permettent d'utiliser une autre instance. Les tests utilisent réellement MySQL. Ils couvrent le CRUD des deux entités, la cascade, la requête HQL `findByMarque` et un doublon de code qui annule toutes les écritures de la transaction. La démonstration insère une salle et une machine, les consulte, modifie la marque puis supprime ses données.

Les captures ci-dessous proviennent directement de ma fenêtre IntelliJ IDEA, avec une vraie instance MySQL **8.0.46** démarrée par Docker Compose.

**Démonstration CRUD dans le terminal intégré IntelliJ** : création d'une salle et d'une machine Dell, lecture des données, modification de la marque en Lenovo, recherche HQL par marque, puis suppression vérifiée (`true`). Les traces SQL sont désactivées pour rendre les résultats lisibles.

![IntelliJ — démonstration CRUD et recherche HQL sur MySQL](screenshots/results.png)

**Tests JUnit dans IntelliJ** : les **5 tests passent** sur MySQL : factory singleton et connexion réelle, CRUD salle, CRUD machine et HQL paramétré, cascade salle/machines et rollback après doublon. Le test de rollback vérifie qu'aucune écriture de la transaction échouée ne reste en base.

![IntelliJ — les 5 tests de persistance passent sur MySQL](screenshots/tests.png)
