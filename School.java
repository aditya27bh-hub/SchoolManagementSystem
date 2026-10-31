import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

// School keeps all students and teachers.
// FILE HANDLING: data is loaded from school.txt when the program starts
// and saved to school.txt after every change, so nothing is ever lost.
public class School {

    private static final String FILE_NAME = "school.txt";

    private Student[] students = new Student[50];    // arrays of objects
    private Teacher[] teachers = new Teacher[20];
    private int studentCount = 0;
    private int teacherCount = 0;
    private int nextId = 1;

    public School() {
        load();                                      // read old data from the file
    }

    // ---------- Add and find ----------
    public Student addStudent(String name, String className) {
        if (studentCount == students.length) {
            throw new IllegalStateException("Student list is full.");
        }
        Student s = new Student(nextId++, name, className);
        students[studentCount++] = s;
        save();                                      // write to file
        return s;                                    // returning an object
    }

    public Teacher addTeacher(int type, String name, String subject) {
        if (teacherCount == teachers.length) {
            throw new IllegalStateException("Teacher list is full.");
        }
        Teacher t;
        if (type == 2) {
            t = new HeadTeacher(nextId++, name, subject);
        } else {
            t = new Teacher(nextId++, name, subject);
        }
        teachers[teacherCount++] = t;
        save();
        return t;
    }

    public Student findStudent(int id) {
        for (int i = 0; i < studentCount; i++) {
            if (students[i].getId() == id) {
                return students[i];
            }
        }
        return null;
    }

    public void updateMarks(Student s, int english, int maths, int science)
            throws InvalidMarksException {
        s.setMarks(english, maths, science);
        save();                                      // write to file
    }

    // ---------- Reports ----------
    public void showAll() {
        if (studentCount == 0 && teacherCount == 0) {
            System.out.println("Nobody has been added yet.");
            return;
        }
        for (int i = 0; i < studentCount; i++) {
            System.out.println(students[i]);
        }
        for (int i = 0; i < teacherCount; i++) {
            System.out.println(teachers[i]);
        }
    }

    // Objects as parameters, and returning an object
    public Student better(Student a, Student b) {
        if (a.getTotal() >= b.getTotal()) {
            return a;
        }
        return b;
    }

    public Student findTopper() {
        if (studentCount == 0) {
            return null;
        }
        Student top = students[0];
        for (int i = 1; i < studentCount; i++) {
            top = better(top, students[i]);
        }
        return top;
    }

    // Recursion: a method that calls itself
    public int totalOfAll(int index) {
        if (index == studentCount) {
            return 0;                                // stop here
        }
        return students[index].getTotal() + totalOfAll(index + 1);
    }

    public double classAverage() {
        if (studentCount == 0) {
            return 0;
        }
        return totalOfAll(0) / (double) (studentCount * 3);
    }

    // ---------- File handling ----------
    private void save() {
        // try-with-resources closes the file automatically
        try (BufferedWriter w = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (int i = 0; i < studentCount; i++) {
                Student s = students[i];
                int[] m = s.getMarks();
                w.write("S," + s.getId() + "," + s.getName() + "," + s.getClassName()
                        + "," + m[0] + "," + m[1] + "," + m[2]);
                w.newLine();
            }
            for (int i = 0; i < teacherCount; i++) {
                Teacher t = teachers[i];
                String code = "T";
                if (t instanceof HeadTeacher) {
                    code = "H";
                }
                w.write(code + "," + t.getId() + "," + t.getName() + "," + t.getSubject());
                w.newLine();
            }
        } catch (IOException e) {
            System.out.println("Could not save data: " + e.getMessage());
        }
    }

    private void load() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("No saved data found. Starting fresh.");
            return;
        }
        try (BufferedReader r = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = r.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] p = line.split(",");        // type, id, name, class/subject, marks...
                int id = Integer.parseInt(p[1]);     // String -> int

                if (p[0].equals("S")) {
                    Student s = new Student(id, p[2], p[3]);
                    s.setMarks(Integer.parseInt(p[4]), Integer.parseInt(p[5]),
                            Integer.parseInt(p[6]));
                    students[studentCount++] = s;
                } else if (p[0].equals("H")) {
                    teachers[teacherCount++] = new HeadTeacher(id, p[2], p[3]);
                } else {
                    teachers[teacherCount++] = new Teacher(id, p[2], p[3]);
                }
                if (id >= nextId) {
                    nextId = id + 1;                 // keep ids unique
                }
            }
            System.out.println((studentCount + teacherCount) + " record(s) loaded from " + FILE_NAME);
        } catch (IOException | InvalidMarksException | NumberFormatException e) {
            System.out.println("Could not read saved data: " + e.getMessage());
        }
    }
}
