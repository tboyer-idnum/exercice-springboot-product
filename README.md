## 💡 1. Installation

### 1.1. Dépendances

Avant d'installer le projet assurez vous d'avoit Maven

### 1.2. Initialisation

Après le clonage du dépôt, initialisez le projet via :
```Shell
mvn clean install
mvn spring-boot:run
```

L'application est alors accessible à l'adresse suivante :

- [localhost:8080/api/products](http://localhost:8080/api/products)
- [localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

### 1.2. Exécution des tests automatisés

Les tests sont exécutés lors de la commande
```Shell
mvn clean install
```

Vous avez la possibilité d'exécuter cette tâche unique via la commande
```Shell
mvn test
```
