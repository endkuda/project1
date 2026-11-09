import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

/** Каталог фильмов в памяти (без БД). */
public class MovieCatalog {
    private final List<Movie> movies = new ArrayList<>();
    private int nextId = 1;

    public Movie add(String title, String director, int year, Genre genre, double rating) {
        Movie movie = new Movie(nextId, title, director, year, genre, rating); // бросит исключение при ошибке
        movies.add(movie);
        nextId++;
        return movie;
    }

    public Optional<Movie> findById(int id) {
        return movies.stream().filter(m -> m.getId() == id).findFirst();
    }

    public boolean remove(int id) {
        return movies.removeIf(m -> m.getId() == id);
    }

    /** Поиск по названию или режиссёру (без учёта регистра). */
    public List<Movie> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            throw new IllegalArgumentException("Ключевое слово не может быть пустым");
        }
        String k = keyword.trim().toLowerCase();
        return movies.stream()
                .filter(m -> m.getTitle().toLowerCase().contains(k)
                        || m.getDirector().toLowerCase().contains(k))
                .toList();
    }

    public List<Movie> filterByGenre(Genre genre) {
        return movies.stream().filter(m -> m.getGenre() == genre).toList();
    }

    public List<Movie> filterByYears(int from, int to) {
        if (from > to) {
            throw new IllegalArgumentException("Начальный год больше конечного");
        }
        return movies.stream()
                .filter(m -> m.getYear() >= from && m.getYear() <= to)
                .sorted(Comparator.comparingInt(Movie::getYear))
                .toList();
    }

    public List<Movie> getAll() {
        return List.copyOf(movies);
    }

    public List<Movie> getSortedByRating() {
        return movies.stream()
                .sorted(Comparator.comparingDouble(Movie::getRating).reversed()
                        .thenComparing(Movie::getTitle))
                .toList();
    }

    public List<Movie> getSortedByYear() {
        return movies.stream()
                .sorted(Comparator.comparingInt(Movie::getYear).thenComparing(Movie::getTitle))
                .toList();
    }

    public int count() {
        return movies.size();
    }

    public OptionalDouble averageRating() {
        return movies.stream().mapToDouble(Movie::getRating).average();
    }

    public Optional<Movie> best() {
        return movies.stream().max(Comparator.comparingDouble(Movie::getRating));
    }
}
