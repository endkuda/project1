import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Function;

/** Консольный интерфейс. Некорректный ввод не роняет программу — просим повторить. */
public class ConsoleMenu {
    private final MovieCatalog catalog;
    private final Scanner in = new Scanner(System.in);

    public ConsoleMenu(MovieCatalog catalog) {
        this.catalog = catalog;
    }

    public void run() {
        boolean running = true;
        while (running) {
            printMenu();
            int choice;
            try {
                choice = toInt(readLine("Выбор: "));
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: " + e.getMessage());
                continue;
            }
            switch (choice) {
                case 1 -> printMovies(catalog.getAll());
                case 2 -> addMovie();
                case 3 -> searchMovies();
                case 4 -> filterByGenre();
                case 5 -> filterByYears();
                case 6 -> printMovies(catalog.getSortedByRating());
                case 7 -> printMovies(catalog.getSortedByYear());
                case 8 -> editMovie();
                case 9 -> deleteMovie();
                case 10 -> printStats();
                case 0 -> running = false;
                default -> System.out.println("Нет такого пункта меню.");
            }
        }
        System.out.println("До свидания!");
    }

    // ---------- действия ----------

    private void addMovie() {
        String title = readValid("Название: ", Movie::validateTitle);
        String director = readValid("Режиссёр: ", Movie::validateDirector);
        int year = readValid("Год выпуска: ", s -> Movie.validateYear(toInt(s)));
        Genre genre = readValid("Жанр (" + Genre.menu() + "): ", s -> Genre.fromNumber(toInt(s)));
        double rating = readValid("Рейтинг (0-10): ", s -> Movie.validateRating(toDouble(s)));
        System.out.println("Добавлено: " + catalog.add(title, director, year, genre, rating));
    }

    private void searchMovies() {
        try {
            printMovies(catalog.search(readLine("Название или режиссёр: ")));
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void filterByGenre() {
        Genre genre = readValid("Жанр (" + Genre.menu() + "): ", s -> Genre.fromNumber(toInt(s)));
        printMovies(catalog.filterByGenre(genre));
    }

    private void filterByYears() {
        int from = readValid("Год от: ", ConsoleMenu::toInt);
        int to = readValid("Год до: ", ConsoleMenu::toInt);
        try {
            printMovies(catalog.filterByYears(from, to));
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void editMovie() {
        int id;
        try {
            id = toInt(readLine("ID фильма: "));
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
            return;
        }
        Optional<Movie> found = catalog.findById(id);
        if (found.isEmpty()) {
            System.out.println("Фильм с таким ID не найден.");
            return;
        }
        Movie m = found.get();
        System.out.println("Пустой ввод — оставить как есть.");
        editField("Название [" + m.getTitle() + "]: ", m::setTitle);
        editField("Режиссёр [" + m.getDirector() + "]: ", m::setDirector);
        editField("Год [" + m.getYear() + "]: ", s -> m.setYear(toInt(s)));
        editField("Жанр (" + Genre.menu() + ") [" + m.getGenre() + "]: ",
                s -> m.setGenre(Genre.fromNumber(toInt(s))));
        editField("Рейтинг [" + m.getRating() + "]: ", s -> m.setRating(toDouble(s)));
        System.out.println("Результат: " + m);
    }

    private void deleteMovie() {
        try {
            int id = toInt(readLine("ID для удаления: "));
            System.out.println(catalog.remove(id) ? "Удалено." : "Фильм с таким ID не найден.");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void printStats() {
        System.out.println("Фильмов в каталоге: " + catalog.count());
        catalog.averageRating().ifPresent(avg -> System.out.printf("Средний рейтинг: %.2f%n", avg));
        catalog.best().ifPresent(m -> System.out.println("Лучший фильм: " + m));
    }

    // ---------- вывод ----------

    private void printMenu() {
        System.out.println("""

                ===== Каталог фильмов =====
                1. Показать все          6. Топ по рейтингу
                2. Добавить              7. Сортировка по году
                3. Поиск                 8. Редактировать
                4. Фильтр по жанру       9. Удалить
                5. Фильтр по годам      10. Статистика
                0. Выход""");
    }

    private void printMovies(List<Movie> list) {
        if (list.isEmpty()) {
            System.out.println("Ничего не найдено.");
            return;
        }
        list.forEach(System.out::println);
    }

    // ---------- ввод с проверкой ----------

    private String readLine(String prompt) {
        System.out.print(prompt);
        return in.hasNextLine() ? in.nextLine() : "";
    }

    /** Спрашивает, пока parser не примет значение (ошибка = IllegalArgumentException). */
    private <T> T readValid(String prompt, Function<String, T> parser) {
        while (true) {
            try {
                return parser.apply(readLine(prompt));
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    /** Одна попытка изменить поле; пустой ввод — не менять. */
    private void editField(String prompt, Consumer<String> setter) {
        String s = readLine(prompt);
        if (s.isBlank()) return;
        try {
            setter.accept(s);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static int toInt(String s) {
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Введите целое число");
        }
    }

    private static double toDouble(String s) {
        try {
            return Double.parseDouble(s.trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Введите число (например, 8.5)");
        }
    }
}
