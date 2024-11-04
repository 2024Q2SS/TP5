import os
import glob


def remove_files_with_pattern(folder_path, pattern):
    # Create the full pattern for glob
    full_pattern = os.path.join(folder_path, pattern)

    # Use glob to find files matching the pattern
    files_to_remove = glob.glob(full_pattern)

    # Remove each file
    for file_path in files_to_remove:
        try:
            os.remove(file_path)
            print(f"Removed: {file_path}")
        except Exception as e:
            print(f"Error removing {file_path}: {e}")


# Example usage
folder_path = "blob"  # Replace with your folder path
pattern = "*"  # Replace with your desired pattern (e.g., '*.jpg', '*.log', etc.)
remove_files_with_pattern(folder_path, pattern)

import pandas as pd
import matplotlib.pyplot as plt

# Cargar datos desde el CSV
df = pd.read_csv("../sorted_outputs/a_4_b_1/output_43.csv")
df.columns = df.columns.str.strip()
print(df.head())

# Recorrer cada fila del DataFrame
import json

# Opening JSON file
f = open(
    "../config.json",
)

# returns JSON object as
# a dictionary
data = json.load(f)
min_r = data["minRadius"]
N = data["blueN"]
resize = 2
for index, row in df.iterrows():
    fig, ax = plt.subplots(figsize=(20, 14))
    # Dibujar el círculo azul (mx, my, mr)
    circle = plt.Circle((row["mx"], row["my"]), min_r * resize, color="blue")
    circle_dashed = plt.Circle(
        (row["mx"], row["my"]),
        row["mr"] * resize,
        color="blue",
        fill=False,
        linestyle="--",
    )
    ax.add_patch(circle)
    ax.add_patch(circle_dashed)

    # Dibujar los puntos rojos (p0x, p0y, ..., p14x, p14y) con su radio correspondiente (p0r, p1r, ..., p14r)
    for i in range(N):
        circle = plt.Circle((row[f"p{i}x"], row[f"p{i}y"]), min_r * resize, color="red")
        circle_dashed = plt.Circle(
            (row[f"p{i}x"], row[f"p{i}y"]),
            row[f"p{i}r"] * resize,
            color="red",
            fill=False,
            linestyle="--",
        )  # Radio ajustado con pnr
        ax.add_patch(circle)
        ax.add_patch(circle_dashed)

    # Configuración de ejes
    ax.set_aspect("equal")
    ax.set_xlim(0, 100)  # Limite en el eje X de 0 a 100
    ax.set_ylim(0, 70)  # Limite en el eje Y de 0 a 70
    # Guardar la imagen con un nombre único (por ejemplo usando el índice)
    time = row["t"]
    plt.title(f"time {time:.2f}")
    plt.savefig(f"blob/imagen_{index}.png")

    plt.close("all")
