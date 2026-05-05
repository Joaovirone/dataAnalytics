
from google.colab import drive
from pyspark.sql import SparkSession
import pyspark.sql.functions as F

drive.mount('/content/drive')

spark = SparkSession.builder \
    .master("local[*]") \
    .appName("Analise_Desigualdade_ENEM") \
    .config("spark.sql.shuffle.partitions", "8") \
    .getOrCreate()

path_participantes = "/content/drive/MyDrive/microdados_enem/PARTICIPANTES_2024.csv"
path_resultados = "/content/drive/MyDrive/microdados_enem/RESULTADOS_2024.csv"

# 6. Leitura (Dica de Performance abaixo)
df_p = spark.read.csv(path_participantes, sep=";", header=True, inferSchema=True)
df_r = spark.read.csv(path_resultados, sep=";", header=True, inferSchema=True)