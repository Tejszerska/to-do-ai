import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode
@ToString
class ToDo {
    enum Status {
        NOT_STARTED,
        IN_PROGRESS,
        COMPLETED
    }

    private Long id;
    private String title;
    private String description;
    private boolean completed;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;

    ToDo(Long id, String title, String description, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.completed = false;
        this.status = Status.NOT_STARTED;
        this.createdAt = createdAt;
    }

    void update(String title, String description, Status status) {
        boolean wasCompleted = this.completed;
        this.title = title;
        this.description = description;
        this.status = status;
        this.completed = status == Status.COMPLETED;
        if (this.completed && !wasCompleted) {
            this.completedAt = LocalDateTime.now();
        } else if (!this.completed) {
            this.completedAt = null;
        }
    }
}
