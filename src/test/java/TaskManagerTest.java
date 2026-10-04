import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TaskManagerTest {
    private final TaskManager taskManager = new TaskManager();

    @Test
    void createTaskAssignsIdAndCreationDateAutomatically() {
        LocalDateTime beforeCreate = LocalDateTime.now();

        ToDo task = taskManager.createTask("Read", "Read a book");

        assertEquals(1L, task.getId());
        assertEquals("Read", task.getTitle());
        assertEquals("Read a book", task.getDescription());
        assertEquals(ToDo.Status.NOT_STARTED, task.getStatus());
        assertFalse(task.isCompleted());
        assertNotNull(task.getCreatedAt());
        assertFalse(task.getCreatedAt().isBefore(beforeCreate));
    }

    @Test
    void createTaskRejectsNullOrBlankTitle() {
        assertThrows(IllegalArgumentException.class, () -> taskManager.createTask(null, "Description"));
        assertThrows(IllegalArgumentException.class, () -> taskManager.createTask("  ", "Description"));
    }

    @Test
    void getTaskByIdReturnsTaskOrEmptyWhenMissing() {
        ToDo task = taskManager.createTask("Read", "Description");

        assertEquals(Optional.of(task), taskManager.getTaskById(task.getId()));
        assertEquals(Optional.empty(), taskManager.getTaskById(99L));
    }

    @Test
    void getAllTasksReturnsTasksInCreationOrderAndReadOnlyList() {
        ToDo first = taskManager.createTask("First", "Description");
        ToDo second = taskManager.createTask("Second", "Description");

        List<ToDo> tasks = taskManager.getAllTasks();

        assertEquals(List.of(first, second), tasks);
        assertThrows(UnsupportedOperationException.class, () -> tasks.add(first));
    }

    @Test
    void updateTaskChangesFieldsAndSetsCompletionDate() {
        ToDo created = taskManager.createTask("Read", "Old description");
        LocalDateTime beforeUpdate = LocalDateTime.now();

        Optional<ToDo> updated = taskManager.updateTask(
                created.getId(), "Study", "New description", ToDo.Status.COMPLETED);

        assertTrue(updated.isPresent());
        assertEquals("Study", updated.get().getTitle());
        assertEquals("New description", updated.get().getDescription());
        assertEquals(ToDo.Status.COMPLETED, updated.get().getStatus());
        assertTrue(updated.get().isCompleted());
        assertNotNull(updated.get().getCompletedAt());
        assertFalse(updated.get().getCompletedAt().isBefore(beforeUpdate));
    }

    @Test
    void updateTaskReturnsEmptyWhenTaskDoesNotExist() {
        assertEquals(Optional.empty(), taskManager.updateTask(
                99L, "Title", "Description", ToDo.Status.IN_PROGRESS));
    }

    @Test
    void updateTaskRejectsInvalidTitleOrNullStatus() {
        ToDo task = taskManager.createTask("Read", "Description");

        assertThrows(IllegalArgumentException.class, () -> taskManager.updateTask(
                task.getId(), "", "Description", ToDo.Status.IN_PROGRESS));
        assertThrows(IllegalArgumentException.class, () -> taskManager.updateTask(
                task.getId(), "Read", "Description", null));
    }

    @Test
    void deleteTaskRemovesExistingTaskAndReturnsFalseWhenMissing() {
        ToDo task = taskManager.createTask("Read", "Description");

        assertTrue(taskManager.deleteTask(task.getId()));
        assertEquals(Optional.empty(), taskManager.getTaskById(task.getId()));
        assertFalse(taskManager.deleteTask(task.getId()));
    }
}
