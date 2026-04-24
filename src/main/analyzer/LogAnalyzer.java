import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import static org.apache.spark.sql.functions.*;

public class LogAnalyzer {
    public static void main(String[] args) {
        
        // 1. Inicializando o "Motor" do Spark
        // O .master("local[*]") diz para o Spark usar todos os núcleos da sua máquina para processar.
        SparkSession spark = SparkSession.builder()
                .appName("LogAnalyzerPipeline")
                .master("local[*]") 
                .getOrCreate();

        // Para evitar que o console fique poluído com logs do próprio Spark
        spark.sparkContext().setLogLevel("ERROR");

        System.out.println("Lendo os logs brutos...");

        // 2. Lendo o arquivo de texto bruto (simulando nosso futuro HDFS)
        Dataset<Row> rawLogs = spark.read().text("dados/access.log");

        // 3. O coração do processamento: Parsing com Regex
        // Vamos extrair o IP, o Endpoint (ex: /api/v1/users) e o Código de Status (ex: 200, 404, 500)
        Dataset<Row> parsedLogs = rawLogs.select(
                regexp_extract(col("value"), "^([^\\s]+)", 1).alias("ip"),
                regexp_extract(col("value"), "\"(?:GET|POST|PUT|DELETE|PATCH)\\s([^\\s]+)", 1).alias("endpoint"),
                regexp_extract(col("value"), "\"\\s(\\d{3})", 1).alias("status_code")
        ).filter(col("status_code").notEqual("")); // Filtra linhas que não deram match no regex

        System.out.println("Amostra dos dados estruturados:");
        parsedLogs.show(5, false);

        // 4. Agregação e Regra de Negócio: Top 5 Endpoints com Erro 500
        System.out.println("Calculando endpoints com mais falhas (Erro 500)...");
        Dataset<Row> erros500 = parsedLogs
                .filter(col("status_code").equalTo("500"))
                .groupBy("endpoint")
                .count()
                .orderBy(desc("count"))
                .limit(5);

        // Exibe o resultado no console
        erros500.show();

        // Encerra a sessão
        spark.stop();
    }
}