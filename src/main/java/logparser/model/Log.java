package logparser.model;

import javax.persistence.*;
import java.util.Date;

// Logs class for records in database
@Entity
@Table(name = "Logs")
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private Date time;

    private String thread;

    private String level;

    private String message;

    public Log(Date time, String thread, String level, String message) {
        this.time = time;
        this.thread = thread;
        this.level = level;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getThread() {
        return thread;
    }

    public void setThread(String thread) {
        this.thread = thread;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
