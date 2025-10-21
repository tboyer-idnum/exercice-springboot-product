## 💡 1. Installation

### 1.1. Dépendances

Avant d'installer le projet assurez vous d'avoir `make`, `Docker` & `docker-compose` sur votre machine
> ⚠️ une machine Linux est recommandée

### 1.2. Initialisation

Après le clonage du dépôt, initialisez le projet via :
```Shell
make up
```

L'application est alors accessible aux adresses suivantes :

- POST [http://exo-spring-boot.localhost/api/products](http://exo-spring-boot.localhost/api/products)
```json
{
	"name": "test",
	"price": 666.0,
	"country": "FRANCE"
}
```
- GET [http://exo-spring-boot.localhost/api/products](http://exo-spring-boot.localhost/api/products)
- GET [http://exo-spring-boot.localhost/api/products/1](http://exo-spring-boot.localhost/api/products/1)
- PAGE [http://exo-spring-boot.localhost/swagger-ui/index.html](http://exo-spring-boot.localhost/swagger-ui/index.html)

### 1.2. Exécution des tests automatisés

Les tests sont exécutés lors de la commande :
```Shell
make test
```
