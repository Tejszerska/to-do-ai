import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

class TaskManager {
    private final Map<Long, ToDo> tasks = new LinkedHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);

    public synchronized ToDo createTask(String title, String description) {
        validateTitle(title);
        long id = nextId.getAndIncrement();
        ToDo task = new ToDo(id, title, description, LocalDateTime.now());
        tasks.put(id, task);
        return task;
    }

    public synchronized Optional<ToDo> getTaskById(long id) {
        return Optional.ofNullable(tasks.get(id));
    }

    public synchronized List<ToDo> getAllTasks() {
        return Collections.unmodifiableList(new ArrayList<>(tasks.values()));
    }

    public synchronized Optional<ToDo> updateTask(
            long id, String title, String description, ToDo.Status status) {
        validateTitle(title);
        if (status == null) {
            throw new IllegalArgumentException("Status must not be null");
        }

        ToDo task = tasks.get(id);
        if (task == null) {
            return Optional.empty();
        }

        task.update(title, description, status);
        return Optional.of(task);
    }

    public synchronized boolean deleteTask(long id) {
        return tasks.remove(id) != null;
    }

    private void validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title must not be blank");
        }
    }
}
