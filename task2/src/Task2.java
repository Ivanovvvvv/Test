import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
/*
-1) На вход программы в качестве аргументов командной строки передаются пути до файлов (для примера file1.txt и file2.txt). (По своему усмотрению можно назвать по-другому);
-2) В file1.txt хранятся координаты X и Y центра окружности и радиус окружности. Координаты - рациональные числа от -(10^38) до 10^38.
    Координаты должны быть написаны в первую строку файла через пробел, радиус - во вторую;
-3) В file2.txt хранятся координаты точек. Координаты - рациональные числа от -(10^38) до 10^38. В ТЗ этот предел указан неккоректно: указан от 10^(-38) до 10^38.
Хотя в примере присутствуют координаты (0 0). В каждой строке должны быть 2 числа- координат X и Y точки. Координаты отделяются пробелом;
-4) Вывод результата числовой согласно ТЗ, без поясняющих записей.
-5) В программе учтены следующите случаи:
    -отсутсвие файлов или неправильно указанный путь до них;
    -нечисловые символы, лишние числа,недостающие числа, пустые файлы, лишние пробелы;
    -если в файле указаны координаты более 100 точек, программа будет выдавать результат основываясь на координатах 100 первых точек.
*/
public class Task2 {
    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.out.println("Введите пути к двум файлам в качестве аргумента командной строки. Сначала путь к файлу с центром и радиусом окружности,"+
                    " затем через пробел к файлу с координатами точек.");
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

        // Чтение координат точек из файла 2
        ArrayList<Dot> dots = readFile2(pathFile2);

        // Обработка точек и вывод на консоль результатов
        calculationOfResult(dots, radius, centerX, centerY);
    }

    // Метод считывает данные координат и радиуса из файла1
    private static double[] readFile1(String file1) throws IOException {
        double[] centerAndRadiusFromFile1 = new double[3];
        try (BufferedReader radiusAndCentre = new BufferedReader(new FileReader(file1))) {
            String line;
            boolean isEmptyFile = true;

            // Проверяем, что file1 не пустой
            while ((line = radiusAndCentre.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    isEmptyFile = false;
                    String[] stringArray = line.split("\\s+");
                    if (stringArray.length == 2) {
                        try {
                            centerAndRadiusFromFile1[0] = Double.parseDouble(stringArray[0]);
                            centerAndRadiusFromFile1[1] = Double.parseDouble(stringArray[1]);
                        } catch (NumberFormatException e) {
                            System.err.println("Некорректные координаты центра окружности: " + line +"\n-Либо указаны нечисловые символы;"+
                                    "\n-Либо дробное число, где отделение целой от дробной части выполнено запятой а не точкой.");
                            return null;
                        }
                        if (!isValidCoordinate(centerAndRadiusFromFile1[0]) || !isValidCoordinate(centerAndRadiusFromFile1[1])) {
                            return null; // Завершаем выполнение программы, если координаты некорректны
                        }
                        break; // После успешного чтения координат прерываем цикл
                    } else {
                        System.err.println("Координаты центра окружности в файле 1 " + file1+ " указаны некорректно:\n- Количество указанных чисел "+
                                "(координат) отлично от двух;;\n- Внимание! радиус необходимо указать в строке, находящейся после строки с координатами.\n");
                        return null;
                    }
                }
            }

            // Читаем радиус из следующей непустой строки
            while ((line = radiusAndCentre.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    String[] stringArray2 = line.split("\\s+");
                    if (stringArray2.length == 1) {
                        try {
                            centerAndRadiusFromFile1[2] = Double.parseDouble(stringArray2[0]);
                        } catch (NumberFormatException e) {
                            System.err.println("Некорректные радиус окружности: " + line +" \n-Либо указаны нечисловые символы;\n-Либо дробное число,"+
                                    " где отделение целой от дробной части выполнено запятой а не точкой.");
                            return null;
                        }
                        break; // После успешного чтения радиуса прерываем цикл
                    } else {
                        System.err.println("Радиус окружности в файле1 " + file1+ " задан некорректно:\n- Количество указанных чисел (величина радиуса) "+
                                "отлично от одного.");
                        return null;
                    }
                }
            }
            if (isEmptyFile) {
                System.err.println("Файл с координатами центра окружности и радиусом " + file1 + " пуст");
                return null;
            }
        }
        return centerAndRadiusFromFile1;
    }

    // Метод для чтения координат точек из файла
    private static ArrayList<Dot> readFile2(String file2) throws IOException {
        ArrayList<Dot> dots = new ArrayList<>();
        BufferedReader points = new BufferedReader(new FileReader(file2));
        String line;
        boolean isEmptyFile = true;
        int dotCount=0;

        while ((line = points.readLine()) != null&&dotCount<100) {
            line = line.trim();
            if (!line.isEmpty()) {
                isEmptyFile = false;
                String[] coordinates = line.split("\\s+");
                if (coordinates.length == 2) {
                    try {
                        double dotX = Double.parseDouble(coordinates[0]);
                        double dotY = Double.parseDouble(coordinates[1]);
                        dots.add(new Dot(dotX, dotY));
                        dotCount++;
                    } catch (NumberFormatException e) {
                        System.err.println("Имеются некорректно указанные координаты в файле "+file2+". \n-Возможно в файле имеются нечисловые символы;"+
                                "\n-Внимание! В дробных числах целую чать от дробной необходимо разделять через точку.");
                        break;
                    }
                } else {
                    System.err.println("Некорректное количество координат в одной из строк или нескольких. Необходимо задать координату Х и через пробел координату Y.");
                    break;
                }
            }
        }
        points.close();

        if (isEmptyFile) {
            System.err.println("Файл с координатами точек " + file2 + " пуст.");
        }
        return dots;
    }

    // Метод для обработки точек и вывода результата на консоль
    private static void calculationOfResult(ArrayList<Dot> dots, double radius, double centerX, double centerY) {
        for (Dot dot : dots) {
            double distance = Math.sqrt(Math.pow(dot.x - centerX, 2) + Math.pow(dot.y - centerY, 2));
            if (distance == radius) {
                System.out.print("0\n"); // 0 - точка лежит на окружности
            } else if (distance < radius) {
                System.out.print("1\n"); // 1 - точка внутри
            } else {
                System.out.print("2\n"); // 2 - точка снаружи
            }
        }
    }

    // Метод для проверки того, входит ли координата в заданый по ТЗ диапазон
    private static boolean isValidCoordinate(double coordinate) {
        if (coordinate < -(Math.pow(10, 38)) || coordinate > Math.pow(10, 38)) {
            System.err.println("Есть координаты, не входящие в диапазон чисел от -(10^38) до 10^38. "+
                    "Пожалуйста проверте координаты центра окружности и координаты точек.");
            return false;
        }
        return true;
    }

    // Класс для хранения координат точек
    static class Dot {
        double x;
        double y;

        Dot(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
}

