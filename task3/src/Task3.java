import com.google.gson.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
/*
В данной программе использовалась бибилотека GSON. Jar данной библиотеки находится в папке lib.
 */

public class Task3 {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Введите пути к трем файлам в качестве аргумента командной строки.\nСначала путь к файлу tests.json, затем через пробел к файлу values.json, затем через пробел к файлу report.json");
            return;
        }

        String testsJsonPath = args[0];//tests.json
        String valuesJsonPath = args[1];//values.json
        String reportJsonPath = args[2];//report.json;

        try {
            // Чтение содержимого файла values.json
            JsonObject valuesJsonObject = readJsonFile(valuesJsonPath);

            // Создание Map для хранения значений по id
            Map<Integer, String> idToValueMap = parseValuesJson(valuesJsonObject);

            // Чтение содержимого файла tests.json
            JsonObject testsJsonObject = readJsonFile(testsJsonPath);

            // Обновление значений в tests.json на основе данных из values.json
            updateTestValues(testsJsonObject, idToValueMap);

            // Запись обновленных данных в новый файл report.json
            writeJsonFile(reportJsonPath, testsJsonObject);

            System.out.println("Файл report.json успешно создан с обновленными значениями.");
        } catch (IOException e) {
            System.err.println("Ошибка при обработке файлов: " + e.getMessage());
        }
    }

    // Метод для чтения JSON файла
    private static JsonObject readJsonFile(String filePath) throws IOException {
        try (FileReader reader = new FileReader(filePath)) {
            Gson gson = new Gson();
            return gson.fromJson(reader, JsonObject.class);
        }
    }

    // Метод для создания Map из данных файла values.json
    private static Map<Integer, String> parseValuesJson(JsonObject valuesJsonObject) {
        Map<Integer, String> idToValueMap = new HashMap<>();
        JsonArray valuesArray = valuesJsonObject.getAsJsonArray("values");
        for (JsonElement element : valuesArray) {
            JsonObject valueObject = element.getAsJsonObject();
            int id = valueObject.get("id").getAsInt();
            String value = valueObject.get("value").getAsString();
            idToValueMap.put(id, value);
        }
        return idToValueMap;
    }

    // Метод для обновления значений в tests.json на основе данных из values.json
    private static void updateTestValues(JsonObject tests, Map<Integer, String> idToValueMap) {
        JsonArray testsArray = tests.getAsJsonArray("tests");
        for (JsonElement element : testsArray) {
            updateTestValuesRecursive(element.getAsJsonObject(), idToValueMap);
        }
    }

    // Рекурсивный метод для обновления значений в тестах
    private static void updateTestValuesRecursive(JsonObject testObject, Map<Integer, String> idToValueMap) {
        int id = testObject.get("id").getAsInt();
        if (idToValueMap.containsKey(id)) {
            String value = idToValueMap.get(id);
            testObject.addProperty("value", value);
        }

        // Рекурсивно обновляем значения для вложенных объектов, если они есть
        if (testObject.has("values")) {
            JsonArray valuesArray = testObject.getAsJsonArray("values");
            for (JsonElement valueElement : valuesArray) {
                updateTestValuesRecursive(valueElement.getAsJsonObject(), idToValueMap);
            }
        }
    }

    // Метод для записи JSON объекта в файл
    private static void writeJsonFile(String filePath, JsonObject jsonObject) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create(); // выводим читабельную версию
            gson.toJson(jsonObject, writer);
        }
    }
}