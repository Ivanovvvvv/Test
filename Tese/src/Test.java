import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/*
-1) На вход программы в качестве аргументов командной строки передаются пути до файлов file1.txt и file2.txt. (По своему усмотрению можно назвать по-другому)
-2) В file1.txt хранятся координаты X и Y центра окружности и радиус окружности. Координаты должны быть написаны в первую строку файла, радиус - во вторую.
-3) В file2.txt хранятся координаты точек. Координаты - рациональные числа от -(10^38) до 10^38. В ТЗ этот предел указан неккоректно: указан от 10^(-38) до 10^38.
Хотя в примере присутствуют координаты (0 0). В каждой строке должны быть 2 числа- координат X и Y точки. Координаты отделяются пробелом
-4) todo расписать условия, допилить программу на предмет исключений и сокращений
*/
public class Test {
    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.out.println("Введите пути к двум файлам в качестве аргумента командной строки. Сначала путь к файлу с центром и радиусом окружности(file1.txt), затем через пробел к файлу с координатами точек(file2.txt)");
            return;
        }

        String pathFile1 = args[0];
        String pathFile2 = args[1];

        // Получение центра и радиуса окружности из файла 1
        double[] centerAndRadius = readFile1(pathFile1);
        if (centerAndRadius == null) {
            return; // Прекращаем выполнение программы, если чтение данных из файла1 прошло неудачно
        }
        double centerX = centerAndRadius[0];
        double centerY = centerAndRadius[1];
        double radius = centerAndRadius[2];

        // Обработка точек из файла 2 и вывод на консоль результатов
        readDotsFromFile2(pathFile2, radius, centerX, centerY);
    }

    // Метод считывает данные координат и радиуса из файла1
    private static double[] readFile1(String file1) throws IOException {
        double[] centerAndRadiusFromFile1 = new double[3];
        BufferedReader radiusAndCentre = new BufferedReader(new FileReader(file1));

        // Считываем 1-ую строку файла1(file1.txt). Там должны быть указаны координаты центра окружности
        String line1 = radiusAndCentre.readLine();
        if (line1 != null && !line1.trim().isEmpty()) {
            String[] stringArray = line1.trim().split("\\s+");
            // Проверяем, что в первой строке файла1 есть только 2 числа, и нет лишних символов.
            if (stringArray.length == 2 && stringArray[0].matches("-?\\d+(\\.\\d+)?") && stringArray[1].matches("-?\\d+(\\.\\d+)?")) {
                centerAndRadiusFromFile1[0] = Double.parseDouble(stringArray[0]);
                centerAndRadiusFromFile1[1] = Double.parseDouble(stringArray[1]);
                if (!isValidCoordinate(centerAndRadiusFromFile1[0]) || !isValidCoordinate(centerAndRadiusFromFile1[1])) {
                    return null; // Завершаем выполнение программы, если координаты некорректны
                }

            } else {
                System.err.println("Координаты центра окружности в файле1 указаны неккоректно:\n-Либо количество указанных чисел(координат) отлично от двух;\n-Либо указаны нечисловые символы;\n-Внимание! радиус необходимо указать в строке, находящейся после строки с координатами.\n");
                return null;
            }
        } else {
            System.err.println("Первая строка в файле1(file1.txt) пуста. Пожалуйста напишите координаты центра окружности в первую строку файла1(file1.txt)");
            return null;
        }

        // Считываем 2-ую строку файла1(file1.txt). Там должен быть указан радиус окружности
        String line2 = radiusAndCentre.readLine();
        if (line2 != null && !line2.trim().isEmpty()) {
            String[] stringArray2 = line2.trim().split("\\s+");
            // Проверяем, что во второй строке файла1 есть только 1 число, и нет лишних символов.
            if (stringArray2.length == 1 && stringArray2[0].matches("-?\\d+(\\.\\d+)?")) {
                centerAndRadiusFromFile1[2] = Double.parseDouble(stringArray2[0]);
            } else {
                System.err.println("Радиус окружности в файле1 задан некорректно.\n-Либо количество указаных чисел отлично(величина радиуса) отлично от одного,\n-Либо содержат нечисловые символы\n");
                return null;
            }
        } else {
            System.err.println("Вторая строка в файле1(file1.txt) пуста. Пожалуйста напишите величину радиуса окружности во вторую строку файла1(file1.txt)");
            return null;
        }
        return centerAndRadiusFromFile1;
    }

    // Метод считывает координаты точек из файла2 и выводит результаты на консоль
    private static void readDotsFromFile2(String file2, double radius, double centerX, double centerY) throws IOException {
        int dotCount = 0;
        BufferedReader dots = new BufferedReader(new FileReader(file2));
        String line;

        while ((line = dots.readLine()) != null && dotCount < 100) {
            line = line.trim();
            if (line.isEmpty()) {
                continue; // Пропускаем пустые строки
            }
            String[] coordinates = line.split("\\s+");
            if (coordinates.length != 2 || !coordinates[0].matches("-?\\d+(\\.\\d+)?") || !coordinates[1].matches("-?\\d+(\\.\\d+)?")) {
                System.out.println("Количесвто координат точки отлично от двух. Необходимо задать координату Х и через пробел координату Y");
                break;
            }

            double dotX = Double.parseDouble(coordinates[0]);
            double dotY = Double.parseDouble(coordinates[1]);
            if (!isValidCoordinate(dotX) || !isValidCoordinate(dotY)) {
                break;
            }

            double distance = Math.sqrt(Math.pow(dotX - centerX, 2) + Math.pow(dotY - centerY, 2));

            // Результат
            if (distance == radius) {
                System.out.println("0"); // 0 - точка лежит на окружности
            } else if (distance < radius) {
                System.out.println("1"); // 1 - точка внутри
            } else {
                System.out.println("2"); // 2 - точка снаружи
            }
            dotCount++;
        }
    }

    // Проверка корректности координат
    private static boolean isValidCoordinate(double coordinate) {
        if (coordinate < -(Math.pow(10, 38)) || coordinate > Math.pow(10, 38)) {
            System.out.println("просто запись");
            return false;
        }
        return true;
    }
}


