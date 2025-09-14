package tvz.jwafp.api.entity;

public class Professor extends BaseEntity {
    private String firstName;
    private String lastName;
    private boolean authorized;

    private String name;

    public Professor() {
        super(null);
    }

    public Professor(final String id, final String firstName, final String lastName, final boolean authorized) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.authorized = authorized;
    }

    public String getId() {
        return super.getId();
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public boolean isAuthorized() {
        return this.authorized;
    }

    public String getName() {
        return name;
    }

    public void setId(final String id) {
        super.setId(id);
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public void setAuthorized(final boolean authorized) {
        this.authorized = authorized;
    }

    public void setName(String name) {
        this.name = name;
    }
}
