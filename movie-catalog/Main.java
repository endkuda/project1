public class Main {
    public static void main(String[] args) {
        MovieCatalog catalog = new MovieCatalog();

        // демо-данные
        catalog.add("Начало", "Кристофер Нолан", 2010, Genre.SCIFI, 8.8);
        catalog.add("Побег из Шоушенка", "Фрэнк Дарабонт", 1994, Genre.DRAMA, 9.3);
        catalog.add("Крёстный отец", "Фрэнсис Форд Коппола", 1972, Genre.DRAMA, 9.2);
        catalog.add("Матрица", "Лана и Лилли Вачовски", 1999, Genre.SCIFI, 8.7);
        catalog.add("Интерстеллар", "Кристофер Нолан", 2014, Genre.SCIFI, 8.7);

        new ConsoleMenu(catalog).run();
    }
}
