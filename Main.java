import java.util.Scanner;

// Main: shows the menu and talks to the user
public class Main {

    private static Scanner sc = new Scanner(System.in);
    private static School school = new School();     // loads saved data automatically

    public static void main(String[] args) {
        System.out.println("===== SCHOOL MANAGEMENT SYSTEM =====");
        System.out.println("        " + Person.SCHOOL_NAME);
        boolean running = true;

        while (running) {

            printMenu();
            try {
                int choice = readInt("Enter choice: ");

                switch (choice) {
                    case 1: addStudent();          break;
                    case 2: addTeacher();          break;
                    case 3: enterMarks();          break;
                    case 4: reportCard();          break;
                    case 5: school.showAll();      break;
                    case 6: classAverage();        break;
                    case 7: topper();              break;
                    case 8: ThreadDemo.run();      break;
                    case 0: running = false;       break;
                    default: System.out.println("Invalid choice.");
                }
            } catch (InvalidMarksException e) {
                System.out.println("Marks rejected: " + e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        System.out.println("Goodbye! All data is saved in school.txt");
    }

    private static void printMenu() {
        System.out.println("\n1. Add student     2. Add teacher    3. Enter marks");
        System.out.println("4. Report card     5. Show everyone  6. Class average");
        System.out.println("7. Find topper     8. Thread demo    0. Exit");
    }

    // ---------- Reading input (wrapper classes + type conversion) ----------
    private static int readInt(String message){
        System.out.print(message);
        return  Integer.parseInt(sc.nextLine().trim());
    }


    private static String readString(String message) {
        System.out.print(message);
        return sc.nextLine().trim();
    }

    private static Student askForStudent() {
        int id = readInt("Student ID: ");
        Student s = school.findStudent(id);
        if (s == null) {
            throw new IllegalArgumentException("Student " + id + " not found.");  // throw keyword
        }
        return s;
    }

    // ---------- Menu actions ----------
    private static void addStudent() {
        String name = readString("Student name: ");
        String className = readString("Class (e.g. 10A): ");
        Student s = school.addStudent(name, className);
        System.out.println("Student added and saved. ID = " + s.getId());
    }

    private static void addTeacher() {
        String name = readString("Teacher name: ");
        String subject = readString("Subject: ");
        int type = readInt("Type (1 = Teacher, 2 = Head Teacher): ");
        Teacher t = school.addTeacher(type, name, subject);
        System.out.println(t.getRole() + " added and saved. ID = " + t.getId());
    }

    private static void enterMarks() throws InvalidMarksException {
        Student s = askForStudent();
        int english = readInt("English marks: ");
        int maths = readInt("Maths marks: ");
        int science = readInt("Science marks: ");
        school.updateMarks(s, english, maths, science);
        System.out.println("Marks saved for " + s.getName());
    }

    private static void reportCard() {
        Student s = askForStudent();
        Student.ReportCard card = s.getReportCard();
        card.print();
    }

    private static void classAverage() {
        double avg = school.classAverage();
        int rounded = (int) avg;                     // type casting: double -> int
        System.out.println("Class average: " + avg + " (about " + rounded + ")");
    }

    private static void topper() {
        Student top = school.findTopper();
        if (top == null) {
            System.out.println("No students yet.");
        } else {
            System.out.println("Topper: " + top.getName() + " with " + top.getTotal() + " marks");
        }
    }
}
