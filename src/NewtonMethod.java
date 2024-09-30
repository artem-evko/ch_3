import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class NewtonMethod {

    // Точность поиска корня
    private static final double EPSILON = 1e-6;

    // Максимальное количество итераций
    private static final int MAX_ITERATIONS = 1000;

    // Пример функции f(x)
    public static double f(double x) {
        return Math.cos(x) - x;  // Пример: уравнение cos(x) = x
    }

    // Производная функции f(x), f'(x)
    public static double derivativeF(double x) {
        return -Math.sin(x) - 1; // Производная для cos(x) - x
    }

    // Метод Ньютона для поиска одного корня
    public static Double newtonMethod(double x0) {
        double x = x0;
        for (int i = 0; i < MAX_ITERATIONS; i++) {
            double fValue = f(x);
            double derivativeValue = derivativeF(x);

            // Проверка на случай, если производная равна нулю (избегаем деления на ноль)
            if (Math.abs(derivativeValue) < EPSILON) {
                return null; // Не удается найти корень (касательная параллельна оси x)
            }

            // Обновляем x по методу Ньютона
            double xNext = x - fValue / derivativeValue;

            // Если разница между x и xNext меньше заданной погрешности, считаем, что нашли корень
            if (Math.abs(xNext - x) < EPSILON) {
                return xNext;
            }

            x = xNext;
        }

        return null; // Если за MAX_ITERATIONS не нашли корень
    }

    // Нахождение всех корней на отрезке [a, b]
    public static List<Double> findAllRoots(double a, double b, int subdivisions) {
        List<Double> roots = new ArrayList<>();

        // Разбиваем интервал [a, b] на несколько подотрезков и ищем корни на каждом из них
        double step = (b - a) / subdivisions;
        for (int i = 0; i < subdivisions; i++) {
            double x0 = a + i * step;  // Начальная точка для метода Ньютона

            Double root = newtonMethod(x0);
            if (root != null && root >= a && root <= b) {
                // Проверяем, что корень не дублируется
                boolean isDuplicate = false;
                for (Double foundRoot : roots) {
                    if (Math.abs(foundRoot - root) < EPSILON) {
                        isDuplicate = true;
                        break;
                    }
                }
                if (!isDuplicate) {
                    roots.add(root);
                }
            }
        }

        return roots;
    }

    public static void main(String[] args) {
        // Путь к входному и выходному файлам
        String inputFilePath = "input.txt";
        String outputFilePath = "output.txt";

        try {
            // Чтение данных из файла
            BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
            String line = reader.readLine();
            String[] input = line.split(" ");
            double a = Double.parseDouble(input[0]);  // Начало интервала
            double b = Double.parseDouble(input[1]);  // Конец интервала
            int subdivisions = Integer.parseInt(input[2]); // Количество разбиений интервала
            reader.close();

            // Нахождение всех корней на интервале [a, b]
            List<Double> roots = findAllRoots(a, b, subdivisions);

            // Запись результатов в файл
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath));
            if (roots.isEmpty()) {
                writer.write("Корни не найдены.");
            } else {
                writer.write("Найденные корни:\n");
                for (Double root : roots) {
                    writer.write(root + "\n");
                }
            }
            writer.close();

            System.out.println("Результаты записаны в файл " + outputFilePath);

        } catch (IOException e) {
            System.err.println("Ошибка при работе с файлами: " + e.getMessage());
        }
    }
}
