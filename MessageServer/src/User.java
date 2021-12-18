public class User {
    private int id;
    private String username;

    public User(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    @Override
    public int hashCode() { return super.hashCode(); }
    @Override
    public boolean equals(Object o) {
        if (o instanceof User) {
            User newUser = (User)o;
            return this.username.equals(newUser.getUsername());
        }
        return false;
    }
}

