import java.util.Date;

public class Topic {
    private String message, category;
    private int timeout;
    private Date date;

    public Topic(String category, int timeout, String message) {
        this.category = category;
        this.timeout = timeout;
        this.message = message;
        this.date = new Date();
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) { this.message = message; }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public int getTimeout() {
        return timeout;
    }
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
    public Date getDate() {
        return date;
    }
//    public boolean isExpired() {
//        long time = new Date().getTime() - this.date.getTime();
//        return (timeout < time || Server.getMaxMessageTimeout() < time);
//    }
}
