// Abstract base class for Student and Teacher
public abstract class Person {

    public static final String SCHOOL_NAME = "Green Valley School";   // final constant

    private String name;       // private: only this class can use it
    protected int id;          // protected: child classes can use it too

    public Person(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public final int getId() {         // final method: cannot be overridden
        return id;
    }

    public String getName() {
        return name;
    }

    public abstract String getRole();  // abstract method: children must write the body

    @Override
    public String toString() {
        return String.format("%-12s | %-4d | %-12s", getRole(), id, name);
    }
}
