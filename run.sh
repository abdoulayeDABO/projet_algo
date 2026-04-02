#!/bin/bash
javac -d out src/polynomes/*.java && java -cp out polynomes.Main
java -cp out polynomes.Main


# chmod +x run.sh
# ./run.sh