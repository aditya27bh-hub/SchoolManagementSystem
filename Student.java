// Student is a child (derived class) of Person
public class Student extends Person {

    public static final String[] SUBJECTS = {"English", "Maths", "Science"};

    private String className;
    private int[] marks = new int[3];      // array of marks

    public Student(int id, String name, String className) {
        super(id, name);                   // call the parent constructor
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public int[] getMarks() {
        return marks;
    }

    // Method overloading: two methods with the same name but different parameters
    public void setMarks(int english, int maths, int science) throws InvalidMarksException {
        int[] m = {english, maths, science};
        setMarks(m);
    }

    public void setMarks(int[] m) throws InvalidMarksException {
        for (int i = 0; i < m.length; i++) {
            if (m[i] < 0 || m[i] > 100) {
                throw new InvalidMarksException("Marks must be between 0 and 100, but got " + m[i]);
            }
        }
        marks = m;
    }

    public int getTotal() {
        int total = 0;
        for (int i = 0; i < marks.length; i++) {
            total = total + marks[i];
        }
        return total;
    }

    public double getPercentage() {
        return getTotal() / 3.0;
    }

    public String getGrade() {
        double p = getPercentage();
        if (p >= 75) {
            return "A";
        } else if (p >= 50) {
            return "B";
        } else if (p >= 35) {
            return "C";
        } else {
            return "Fail";
        }
    }

    @Override                              // method overriding
    public String getRole() {
        return "Student";
    }

    @Override
    public String toString() {
        return super.toString() + " | Class " + className + " | Total " + getTotal();
    }

    public ReportCard getReportCard() {
        return new ReportCard();
    }

    // Inner class: a class inside a class, it can use the outer class variables
    public class ReportCard {
        public void print() {
            System.out.println("------ REPORT CARD ------");
            System.out.println("Name  : " + getName());
            System.out.println("Class : " + className);
            for (int i = 0; i < marks.length; i++) {
                System.out.println(SUBJECTS[i] + " : " + marks[i]);
            }
            System.out.println("Total : " + getTotal() + " / 300");
            System.out.println("Percentage : " + getPercentage() + "%   Grade: " + getGrade());
            System.out.println("-------------------------");
        }
    }
}
