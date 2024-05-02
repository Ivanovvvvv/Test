import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/*
-1) На вход программы в качестве аргумента командной строки передается путь до файла ( для примера file.txt) (По своему усмотрению можно назвать по-другому);
-2) В file.txt хранятся целые числа массива неопределенной длины. Числа должны быть написаны по одному в каждую строку;
-3) Программа учитывает возможные пробелы и пропуски в файле file.txt. Она ищет только целые числа;
-4) Вывод результата числовой согласно ТЗ, без подробных описаний;
-5) В программе учтены следующите случаи:
    -отсутсвие файлов или неправильно указанный путь до них;
    -нечисловые символы, лишние числа, пустые файлы, лишние пробелы, нецелые числа.
-5) Вывод полученного пути выполнен согласно ТЗ. В консоли(ответе) выводится только число, без сопрводительных записей.
*/

public class Task4 {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Путь к файлу не указан или указан неверно. Пожалуйста укажите путь к файлу в качестве аргумента командной строки");
            return;
        }

        String filePath = args[0];
        try {
            List<Integer> nums = readNumbersFromFile(filePath);
            if (nums.isEmpty()) {
                System.err.println("Файл " +filePath+  " пуст");
                return;
            }
            int minMoves = findMinMoves(nums);
            System.out.println(minMoves);//Вывод минимального количества ходов

        } catch (FileNotFoundException e) {
            System.err.println("Файл "+filePath+ " не найден");
        } catch (NumberFormatException e) {
            System.err.println("Число в одной из строк файла указано некорректно:\n-Либо указан нечисловой символ;\n-Либо число не целое;\n-Либо в строке содержаться несколько чисел. Необходимо оставить только одно число.");
        }
    }

    // Считываем числа из файла и перобразуем его в список
    private static List<Integer> readNumbersFromFile(String filePath) throws FileNotFoundException {
        List<Integer> nums = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.trim().isEmpty()) {
                    int num = Integer.parseInt(line.trim());
                    nums.add(num);
                }
            }
        }
        return nums;
    }

    // Находим минимальное число шагов для того чтобы числа в массиве были одинаковыми
    private static int findMinMoves(List<Integer> nums) {
        int median = findMedian(nums);
        int sum = 0;
        for (int num : nums) {
            sum += Math.abs(num - median);
        }
        return sum;
    }

    // Находим медиану массива. В зависимости от четности количнства элементов, метод будет отличаться.
    private static int findMedian(List<Integer> nums) {
        Collections.sort(nums);
        int sizeofList = nums.size();
        //При
        return sizeofList % 2 == 0 ? (nums.get(sizeofList / 2 - 1) + nums.get(sizeofList / 2)) / 2 : nums.get(sizeofList / 2);
    }
}
