import java.time.Year;
import java.util.Objects;

/** Фильм. Все поля проверяются в конструкторе и сеттерах. */
public final class Movie {
    public static final int MAX_TEXT = 100;
    public static final int MIN_YEAR = 1888; // первый в истории фильм
    public static final double MIN_RATING = 0.0;
    public static final double MAX_RATING = 10.0;

    private final int id;
    private String title;
    private String director;
    private int year;
    private Genre genre;
    private double rating;

    public Movie(int id, String title, String director, int year, Genre genre, double rating) {
        if (id <= 0) {
            throw new IllegalArgumentException("id должен быть положительным числом");
        }
        this.id = id;
        setTitle(title);
        setDirector(director);
        setYear(year);
        setGenre(genre);
        setRating(rating);
    }

    // ---------- валидаторы (их же использует консольное меню) ----------

    public static String validateTitle(String title) {
        return validateText(title, "Название");
    }

    public static String validateDirector(String director) {
        return validateText(director, "Режиссёр");
    }

    private static String validateText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " не может быть пустым");
        }
        String v = value.trim();
        if (v.length() > MAX_TEXT) {
            throw new IllegalArgumentException(field + " не длиннее " + MAX_TEXT + " символов");
        }
        return v;
    }

    public static int validateYear(int year) {
        int max = Year.now().getValue() + 5; // допускаем анонсированные фильмы
        if (year < MIN_YEAR || year > max) {
            throw new IllegalArgumentException("Год должен быть в диапазоне " + MIN_YEAR + "–" + max);
        }
        return year;
    }

    public static double validateRating(double rating) {
        if (Double.isNaN(rating) || rating < MIN_RATING || rating > MAX_RATING) {
            throw new IllegalArgumentException("Рейтинг должен быть от " + MIN_RATING + " до " + MAX_RATING);
        }
        return rating;
    }

    // ---------- геттеры / сеттеры ----------

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDirector() { return director; }
    public int getYear() { return year; }
    public Genre getGenre() { return genre; }
    public double getRating() { return rating; }

    public void setTitle(String title) {
        this.title = validateTitle(title);
    }

    public void setDirector(String director) {
        this.director = validateDirector(director);
    }

    public void setYear(int year) {
        this.year = validateYear(year);
    }

    public void setGenre(Genre genre) {
        this.genre = Objects.requireNonNull(genre, "Жанр не может быть null");
    }

    public void setRating(double rating) {
        this.rating = validateRating(rating);
    }

    // ---------- Object ----------

    @Override
    public boolean equals(Object o) {
        return this == o || (o instanceof Movie other && id == other.id);
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        return String.format("#%d «%s» (%d) | режиссёр: %s | жанр: %s | рейтинг: %.1f",
                id, title, year, director, genre, rating);
    }
}
