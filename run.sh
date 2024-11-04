#!/bin/bash

# Carpeta de destino para los archivos output.csv
OUTPUT_FOLDER="./sorted_outputs/"
# Carpeta donde se encuentra el archivo config.json
CONFIG_FOLDER="."
# Carpeta donde se debe ejecutar el comando Maven
CPM_FOLDER="./CPM/"

# Lista de pares de valores para a y b
declare -a a_values=(0.25 0.5 1 2 3 4 5 6 7 8 9 10 11 12)
declare -a b_values=(12) 

# Número de ejecuciones por cada par de valores de a y b
NUM_RUNS=100

# Función para actualizar los valores de 'a' y 'b' en config.json
update_config() {
  local a_value=$1
  local b_value=$2

  # Modifica el config.json en la carpeta correspondiente
  jq --argjson a "$a_value" --argjson b "$b_value" \
    '.a = $a | .b = $b' "$CONFIG_FOLDER/config.json" > "$CONFIG_FOLDER/temp_config.json" && \
    mv "$CONFIG_FOLDER/temp_config.json" "$CONFIG_FOLDER/config.json"
}

# Verifica si jq está instalado
if ! command -v jq &> /dev/null; then
  echo "Error: 'jq' no está instalado. Instálalo para ejecutar este script."
  exit 1
fi

# Recorre todos los pares de valores de a y b
for a in "${a_values[@]}"; do
  for b in "${b_values[@]}"; do
    echo "Ejecutando con a=$a y b=$b"

    # Actualiza config.json
    update_config "$a" "$b"

    # Crear la carpeta específica para estos valores de a y b si no existe
    RESULT_FOLDER="$OUTPUT_FOLDER/a_${a}_b_${b}"
    mkdir -p "$RESULT_FOLDER"

    # Ejecuta el comando de Maven NUM_RUNS veces para este par de valores
    for ((i=1; i<=NUM_RUNS; i++)); do
      echo "Ejecución $i para a=$a y b=$b"

      # Cambia a la carpeta CPM y ejecuta Maven
      cd "$CPM_FOLDER" || { echo "Error: no se pudo acceder a $CPM_FOLDER"; exit 1; }
      MAVEN_OPTS="-Xmx8g" mvn exec:java -Dexec.mainClass="ar.edu.itba.ss.App"

      # Regresa a la carpeta inicial después de ejecutar Maven
      cd - >/dev/null

      # Verifica si output.csv fue generado en la carpeta CPM y muévelo a la carpeta de destino con un nombre único
      if [ -f "$CPM_FOLDER/output.csv" ]; then
        mv "$CPM_FOLDER/output.csv" "$RESULT_FOLDER/output_${i}.csv"
        echo "output.csv movido a $RESULT_FOLDER/output_${i}.csv"
      else
        echo "Error: output.csv no se encontró después de la ejecución $i para a=$a y b=$b."
        break
      fi
    done
  done
done

