// Multilevel inheritance:  Person -> Teacher -> HeadTeacher
public class HeadTeacher extends Teacher {

    public HeadTeacher(int id, String name, String subject) {
        super(id, name, subject);
    }

    @Override
    public String getRole() {
        return "Head Teacher";
    }
}
