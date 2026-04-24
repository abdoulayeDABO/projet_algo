make        # compile
make run    # compile + execute
make clean  # nettoie obj/ et out/



## Compilation a la main


# Créer le dossier de sortie
mkdir -p out

# Compiler
gcc -Wall -Wextra -std=c11 -Iinclude src/polynome.c src/analyseur.c src/main.c -o out/polynomes.exe -lm

# Exécuter
./out/polynomes.exe