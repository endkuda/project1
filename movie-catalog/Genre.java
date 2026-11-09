/** Жанр фильма. */
public enum Genre {
    DRAMA("Драма"),
    COMEDY("Комедия"),
    ACTION("Боевик"),
    THRILLER("Триллер"),
    HORROR("Ужасы"),
    SCIFI("Фантастика"),
    ANIMATION("Анимация"),
    DOCUMENTARY("Документальный");

    private final String label;

    Genre(String label) {
        this.label = label;
    }

    /** Номер из меню (1..N) -> жанр. */
    public static Genre fromNumber(int n) {
        Genre[] all = values();
        if (n < 1 || n > all.length) {
            throw new IllegalArgumentException("Жанр: введите число от 1 до " + all.length);
        }
        return all[n - 1];
    }

    /** Строка вида "1-Драма, 2-Комедия, ..." для подсказки в меню. */
    public static String menu() {
        StringBuilder sb = new StringBuilder();
        Genre[] all = values();
        for (int i = 0; i < all.length; i++) {
            if (i > 0) sb.append(", ");
            sb.append(i + 1).append('-').append(all[i].label);
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return label;
    }
}
