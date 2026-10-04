import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ToDoTest {
    @Test
    void constructorSetsInitialValuesAndDefaults() {
        LocalDateTime createdAt = LocalDateTime.of(2026, 10, 4, 12, 0);

        ToDo task = new ToDo(1L, "Read", "Read a book", createdAt);

        assertEquals(1L, task.getId());
        assertEquals("Read", task.getTitle());
        assertEquals("Read a book", task.getDescription());
        assertFalse(task.isCompleted());
        assertEquals(ToDo.Status.NOT_STARTED, task.getStatus());
        assertEquals(createdAt, task.getCreatedAt());
        assertNull(task.getCompletedAt());
    }

    @Test
    void updateToCompletedSetsCompletionFlagAndDate() {
        ToDo task = new ToDo(1L, "Read", "Old description", LocalDateTime.now());
        LocalDateTime beforeUpdate = LocalDateTime.now();

        task.update("Study", "New description", ToDo.Status.COMPLETED);

        assertEquals("Study", task.getTitle());
        assertEquals("New description", task.getDescription());
        assertEquals(ToDo.Status.COMPLETED, task.getStatus());
        assertTrue(task.isCompleted());
        assertNotNull(task.getCompletedAt());
        assertFalse(task.getCompletedAt().isBefore(beforeUpdate));
    }

    @Test
    void reopeningCompletedTaskClearsCompletionDate() {
        ToDo task = new ToDo(1L, "Read", "Description", LocalDateTime.now());
        task.update("Read", "Description", ToDo.Status.COMPLETED);

        task.update("Read", "Description", ToDo.Status.IN_PROGRESS);

        assertEquals(ToDo.Status.IN_PROGRESS, task.getStatus());
        assertFalse(task.isCompleted());
        assertNull(task.getCompletedAt());
    }

    @Test
    void updatingCompletedTaskDoesNotResetCompletionDate() {
        ToDo task = new ToDo(1L, "Read", "Description", LocalDateTime.now());
        task.update("Read", "Description", ToDo.Status.COMPLETED);
        LocalDateTime completedAt = task.getCompletedAt();

        task.update("Read more", "Updated description", ToDo.Status.COMPLETED);

        assertEquals(completedAt, task.getCompletedAt());
    }

    @Test
    void lombokGeneratesValueMethods() {
        LocalDateTime createdAt = LocalDateTime.of(2026, 10, 4, 12, 0);
        ToDo first = new ToDo(1L, "Read", "Description", createdAt);
        ToDo same = new ToDo(1L, "Read", "Description", createdAt);
        ToDo different = new ToDo(2L, "Read", "Description", createdAt);

        assertEquals(first, same);
        assertEquals(first.hashCode(), same.hashCode());
        assertNotEquals(first, different);
        assertTrue(first.toString().contains("Read"));
    }
}
