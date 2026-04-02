# Code of Conduct

## Règles de codage

### Nommage
- Classes : `PascalCase` → `Monome`, `Polynome`
- Méthodes : `camelCase` → `ajouterMonome()`, `afficher()`
- Variables : `camelCase` → `coefficient`, `exposant`

### Structure des classes
- Une classe par fichier
- Attributs en haut, constructeurs ensuite, méthodes en bas
- Toujours mettre `package polynomes;` en première ligne

### Commits Git
- `feat:` pour une nouvelle fonctionnalité
- `fix:` pour une correction
- `docs:` pour la documentation

Exemples :
```
feat: ajout classe Monome
feat: implémentation analyseur syntaxique
fix: correction tri décroissant
docs: mise à jour README
```

### Ordre d'implémentation
1. `Monome.java`
2. `Polynome.java`
3. `Analyseur.java`
4. `Operations.java`
5. `GarbageCollector.java`