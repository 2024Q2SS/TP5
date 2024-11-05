#!/bin/bash

# Carpeta de destino para los archivos output.csv
OUTPUT_FOLDER="./new_multiple_n_outputs/"
# Carpeta donde se encuentra el archivo config.json
CONFIG_FOLDER="."
# Carpeta donde se debe ejecutar el comando Maven
CPM_FOLDER="./CPM/"

# Valores de 'n' para iterar
declare -a n_values=(20 25)  # Añade aquí los valores de 'n' que necesitas

# Combinaciones específicas de 'a' y 'b' para cada valor de 'n'
declare -a a_values=(0.5 0.5)
declare -a b_values=(4 6)

# Función para actualizar los valores de 'a', 'b' y 'blueN' en config.json
update_config() {
  local a_value=$1
  local b_value=$2
  local n_value=$3

  # Modifica el config.json en la carpeta correspondiente
  jq --argjson a "$a_value" --argjson b "$b_value" --argjson blueN "$n_value" \
    '.a = $a | .b = $b | .blueN = $blueN' "$CONFIG_FOLDER/config.json" > "$CONFIG_FOLDER/temp_config.json" && \
    mv "$CONFIG_FOLDER/temp_config.json" "$CONFIG_FOLDER/config.json"
}

# Verifica si jq está instalado
if ! command -v jq &> /dev/null; then
  echo "Error: 'jq' no está instalado. Instálalo para ejecutar este script."
  exit 1
fi

# Recorre todos los valores de 'n'
for n in "${n_values[@]}"; do
  echo "Ejecutando para n=$n"

  # Recorre las combinaciones específicas de 'a' y 'b'
  for idx in "${!a_values[@]}"; do
    a="${a_values[$idx]}"
    b="${b_values[$idx]}"
    echo "Ejecutando con a=$a, b=$b, y n=$n"

    # Actualiza config.json con los valores de 'a', 'b' y 'blueN'
    update_config "$a" "$b" "$n"

    # Crear la carpeta específica para estos valores de a, b y n si no existe
    RESULT_FOLDER="$OUTPUT_FOLDER/a_${a}_b_${b}/n_${n}"
    mkdir -p "$RESULT_FOLDER"


      # Cambia a la carpeta CPM y ejecuta Maven
      cd "$CPM_FOLDER" || { echo "Error: no se pudo acceder a $CPM_FOLDER"; exit 1; }
      MAVEN_OPTS="-Xmx8g" mvn exec:java -Dexec.mainClass="ar.edu.itba.ss.App2"

      # Regresa a la carpeta inicial después de ejecutar Maven
      cd - >/dev/null

      # Verifica si output.csv fue generado en la carpeta CPM y muévelo a la carpeta de destino con un nombre único
      if [ -f "$CPM_FOLDER/obs_output.csv" ]; then
        mv "$CPM_FOLDER/obs_output.csv" "$RESULT_FOLDER/output.csv"
        echo "obs_output.csv movido a $RESULT_FOLDER/output.csv"
      else
        echo "Error: output.csv no se encontró después de la ejecución $i para a=$a, b=$b, y n=$n."
        break
      fi
    done
  done

