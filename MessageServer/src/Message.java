public class Message {

    private String text;
    private User sourceUser;
    private User destinationUser;

    public Message(String text, User sourceUser, User destinationUser) {
        this.text = text;
        this.sourceUser = sourceUser;
        this.destinationUser = destinationUser;
    }

    public String getText() {
        return text;
    }
    public User getSourceUser() { return sourceUser; }
    public User getDestinationUser() { return destinationUser; }

    @Override
    public String toString() {
        return sourceUser.getUsername() + ": " + text;
    }
}