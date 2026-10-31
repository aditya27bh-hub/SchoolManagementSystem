// Teacher is also a child of Person
public class Teacher extends Person {

    protected String subject;

    public Teacher(int id, String name, String subject) {
        super(id, name);
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    @Override
    public String getRole() {
        return "Teacher";
    }

    @Override
    public String toString() {
        return super.toString() + " | Subject " + subject;
    }
}
