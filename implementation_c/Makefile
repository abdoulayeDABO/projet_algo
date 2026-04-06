# ─────────────────────────────────────────────────────────────
#  Makefile – Polynômes en C
# ─────────────────────────────────────────────────────────────

CC      = gcc
CFLAGS  = -Wall -Wextra -std=c11 -Iinclude
LDFLAGS = -lm

SRC = src/polynome.c src/analyseur.c src/main.c
OBJ = $(SRC:.c=.o)
OUT = out
BIN = $(OUT)/polynomes.exe

.PHONY: all clean run

all: $(OUT) $(BIN)

# Cree le dossier out/ s'il n'existe pas
$(OUT):
	mkdir -p $(OUT)

$(BIN): $(OBJ)
	$(CC) $(CFLAGS) $^ -o $@ $(LDFLAGS)

%.o: %.c
	$(CC) $(CFLAGS) -c $< -o $@

run: all
	./$(BIN)

clean:
	rm -f $(OBJ) $(BIN)
# 	rmdir $(OUT) 2>/dev/null || true