import java.io.*;
import java.util.*;

public class familyTree {

    private ArrayList<Person> persons = new ArrayList<Person>();
    private ArrayList<Person> family1 = new ArrayList<Person>();
    private ArrayList<Person> family2 = new ArrayList<Person>();
    private ArrayList<Person> family3 = new ArrayList<Person>();
    private ArrayList<Person> family4 = new ArrayList<Person>();

    private BufferedReader br;

    public ArrayList<Person> getPersons() {
        return persons;
    }

    public ArrayList<Person> getFamily1() {
        return family1;
    }

    public ArrayList<Person> getFamily2() {
        return family2;
    }

    public ArrayList<Person> getFamily3() {
        return family3;
    }

    public ArrayList<Person> getFamily4() {
        return family4;
    }

    public Person getPerson(int index) {
        return persons.get(index);
    }

    public Person getPerson(String name, String surname) {
        for (int i = 0; i < persons.size(); i++) {
            if (persons.get(i).getName().equals(name) && persons.get(i).getSurname().equals(surname)) {
                return persons.get(i);
            }
        }
        return null;
    }

    public void addPersonToArray(String txt, ArrayList<Person> family) {
        String line;
        try {
            br = new BufferedReader(new FileReader(txt));
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                Person person = new Person();
                person.setId(data[0]);
                person.setName(data[1]);
                person.setSurname(data[2]);
                person.setBirthdate(data[3]);
                person.setPartnerName(data[4]);
                person.setMotherName(data[5]);
                person.setFatherName(data[6]);
                person.setBloodGroup(data[7]);
                person.setJob(data[8]);
                person.setMaritalStatus(data[9]);
                person.setMaidenName(data[10]);
                person.setGender(data[11]);
                persons.add(person);
                family.add(person);
                for (int i = 0; i < persons.size() - 1; i++) {
                    if (persons.get(i).getId().equals(person.getId())) {
                        persons.remove(person);
                    }
                }

            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        familyTree ft = new familyTree();

        // ; ile ayrılmış 4 excel dosyası okunacak
        try {
            ft.addPersonToArray("dosya1.csv", ft.getFamily1());
            ft.addPersonToArray("dosya2.csv", ft.getFamily2());
            ft.addPersonToArray("dosya3.csv", ft.getFamily3());
            ft.addPersonToArray("dosya4.csv", ft.getFamily4());
        } catch (Exception e) {
            System.out.println(e);
        }

        Tree tree = new Tree(ft.persons);
        tree.createTree();
        tree.printTree();

        // Çocuğu olmayan kişileri buluyor
        tree.findPersonWithoutChildrens();

        System.out.println();

        // Üvey kardeşleri buluyor
        tree.stepSisterBrother(ft.getPersons());

        System.out.println();

        // Verilen kangrubuna sahip kişileri buluyor
        tree.sameBlood(ft.getPersons(), "A");
        System.out.println();

        // Aynı soydan olan ve aynı mesleği yapan kişileri buluyor
        tree.sameJob();

        System.out.println();

        // Aynı isme sahip kişileri buluyor
        tree.sameName();

        System.out.println();

        // Verilen kişilerin akrabalık bağlarını söylüyor
        try {
            tree.relation(ft.getPersons());
        } catch (Exception e) {
            System.out.println("Gecersiz isim veya soyisim");
        }

        System.out.println();

        // Verilen kişinin ağacını oluşturuyor
        try {
            tree.createTreeForPerson(ft.getPerson("Fikri", "Sipsik"), ft.getFamily1(),
                    ft.getFamily2(), ft.getFamily3(),
                    ft.getFamily4());
        } catch (Exception e) {
            System.out.println("Gecersiz isim veya soyisim");
        }

        System.out.println();

        // Ağacın derinliğini buluyor
        System.out.println("Agacin derinligi: " + tree.maxDepth());

        System.out.println();

        // Verilen kişiden sonraki nesil sayısını buluyor
        try {
            System.out.println(tree.generations(ft.getPersons()));
        } catch (Exception e) {
            System.out.println("Gecersiz isim veya soyisim");
        }
    }
}

class Person {
    private String id, name, surname, motherName, fatherName, bloodGroup, job, maidenName, gender, birthdate,
            maritalStatus, partnerName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getMaidenName() {
        return maidenName;
    }

    public void setMaidenName(String maidenName) {
        this.maidenName = maidenName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String toString() {
        return getName() + " " + getSurname();
    }
}

class Node {
    private Person person;
    private Node Father, Mother;
    private ArrayList<Node> childrens = new ArrayList<Node>();

    public Node(Person person) {
        this.person = person;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Node getFather() {
        return Father;
    }

    public void setFather(Node father) {
        Father = father;
    }

    public Node getMother() {
        return Mother;
    }

    public void setMother(Node mother) {
        Mother = mother;
    }

    public ArrayList<Node> getChildrens() {
        return childrens;
    }

    public void setChildrens(ArrayList<Node> childrens) {
        this.childrens = childrens;
    }

    public void addChild(Node child) {
        childrens.add(child);
    }

    public void removeChild(Node child) {
        childrens.remove(child);
    }

    public String toString() {
        return person.getName() + " " + person.getSurname();
    }

}

class Tree {
    private ArrayList<Node> grands = new ArrayList<>();
    private ArrayList<Node> nodes = new ArrayList<Node>();

    public Tree(ArrayList<Person> persons) {
        for (int i = 0; i < persons.size(); i++) {
            nodes.add(new Node(persons.get(i)));
        }
    }

    public ArrayList<Node> getGrands() {
        return grands;
    }

    public Node getGrandByName(String name) {
        for (int i = 0; i < grands.size(); i++) {
            if (grands.get(i).getPerson().getName().equals(name)) {
                return grands.get(i);
            }
        }
        return null;
    }

    public ArrayList<Node> getChildrens(Node node) {
        ArrayList<Node> childrens = new ArrayList<Node>();
        for (int i = 0; i < nodes.size(); i++) {
            if (node.getPerson().getName().equals(nodes.get(i).getPerson().getFatherName())
                    || node.getPerson().getName().equals(nodes.get(i).getPerson().getMotherName())) {
                childrens.add(nodes.get(i));
            }
        }
        return childrens;
    }

    public void createTree() {
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).getPerson().getFatherName().equals("null")
                    && nodes.get(i).getPerson().getMotherName().equals("null")) {
                grands.add(nodes.get(i));
            }
        }

        for (int i = 0; i < nodes.size(); i++) {
            for (int j = 0; j < nodes.size(); j++) {
                if (nodes.get(i).getPerson().getFatherName().equals(nodes.get(j).getPerson().getName())) {
                    nodes.get(i).setFather(nodes.get(j));
                    nodes.get(j).addChild(nodes.get(i));
                }
                if (nodes.get(i).getPerson().getMotherName().equals(nodes.get(j).getPerson().getName())) {
                    nodes.get(i).setMother(nodes.get(j));
                    nodes.get(j).addChild(nodes.get(i));
                }
                if (nodes.get(i).getFather() != null && nodes.get(i).getMother() != null) {
                    break;
                }
            }

        }

    }

    public void printTree() {
        for (int i = 0; i < grands.size(); i++) {
            if (grands.get(i).getPerson().getGender().equals("Erkek")) {
                printTree(grands.get(i), 0);

            }
        }
    }

    private void printTree(Node node, int level) {
        System.out.println();

        for (int i = 0; i < level; i++) {
            System.out.print("------->");
        }
        System.out.println(node.getPerson().toString() + " - " + node.getPerson().getPartnerName());
        for (int i = 0; i < node.getChildrens().size(); i++) {
            if (node.getChildrens().size() == 0)
                break;
            printTree(node.getChildrens().get(i), level + 1);
        }

    }

    public void findPersonWithoutChildrens() {
        for (int i = 0; i < grands.size(); i++) {
            findPersonWithoutChildrens(grands.get(i));

        }
        System.out.print("Cocugu olmayan kisiler:");
        for (Node node : childless) {
            System.out.print(" " + node.getPerson().toString() + " - ");
        }
        System.out.println();
    }

    private static HashSet<Node> childless = new HashSet<Node>();

    private void findPersonWithoutChildrens(Node node) {
        if (node.getChildrens().size() == 0) {
            childless.add(node);

        }
        for (int i = 0; i < node.getChildrens().size(); i++) {
            findPersonWithoutChildrens(node.getChildrens().get(i));
        }
    }

    public void stepSisterBrother(ArrayList<Person> persons) {

        for (int i = 0; i < persons.size(); i++) {
            for (int j = 0; j < persons.size(); j++) {
                if (nodes.get(i).getFather() != null && nodes.get(j).getFather() != null
                        && nodes.get(i).getMother() != null
                        && nodes.get(j).getMother() != null) {
                    if (nodes.get(i).getFather().toString().equals(nodes.get(j).getFather().toString())
                            && !nodes.get(i).getMother().toString().equals(nodes.get(j).getMother().toString())) {
                        System.out.println(
                                persons.get(i).toString() + " Kisisinin Uvey Kardesi " + persons.get(j).toString());
                    } else if (nodes.get(i).getMother().toString().equals(nodes.get(j).getMother().toString())
                            && !nodes.get(i).getFather().toString().equals(nodes.get(j).getFather().toString())) {
                        System.out.println(
                                persons.get(i).toString() + " Kisisinin Uvey Kardesi " + persons.get(j).toString());
                    }

                }
            }
        }

    }

    public void sameBlood(ArrayList<Person> persons, String bloodGroup) {
        System.out.print(bloodGroup + " kan grubuna sahip kisiler : ");
        for (int i = 0; i < persons.size(); i++) {
            if (persons.get(i).getBloodGroup().equals(bloodGroup + "(+)")
                    || persons.get(i).getBloodGroup().equals(bloodGroup + "(-)")) {
                System.out.print(persons.get(i).getName() + " " + persons.get(i).getSurname() + "- ");
            }
        }

    }

    public void sameJob() {
        System.out.println();
        for (int i = 0; i < grands.size(); i++) {
            sameJob(grands.get(i));

        }
    }

    private void sameJob(Node grand) {
        boolean flag = false;
        for (int i = 0; i < grand.getChildrens().size(); i++) {
            if (grand.getChildrens().get(i).getPerson().getJob().equals(grand.getPerson().getJob())) {
                System.out.println("Cocuk: " + grand.getChildrens().get(i).getPerson().toString());
                flag = true;
            }
            for (int j = 0; j < grand.getChildrens().get(i).getChildrens().size(); j++) {
                if (grand.getChildrens().get(i).getChildrens().get(j).getPerson().getJob()
                        .equals(grand.getPerson().getJob())) {
                    System.out.println(
                            "Torun: " + grand.getChildrens().get(i).getChildrens().get(j).getPerson().toString());
                    flag = true;
                }
            }
        }
        if (flag) {
            System.out.println("Ata: " + grand.getPerson().toString());
            System.out.println("Meslek: " + grand.getPerson().getJob());
            System.out.println("*******************************");
        }

    }

    public void sameName() {
        for (int i = 0; i < nodes.size(); i++) {
            for (int j = 0; j < nodes.size(); j++) {
                if (nodes.get(i).getPerson().getName().equals(nodes.get(j).getPerson().getName()) && i != j) {
                    System.out
                            .println(nodes.get(i).getPerson().toString()
                                    + " " + ageCalculator(nodes.get(i).getPerson().getBirthdate()));
                }
            }
        }

    }

    public void relation(ArrayList<Person> persons) {
        Scanner input = new Scanner(System.in);
        System.out.print("Akrabalik bagi bulunacak kisinin adi soyadi : ");
        String name = input.next();
        String surname = input.next();
        Person person = null;
        for (int i = 0; i < persons.size(); i++) {
            if (persons.get(i).getName().equals(name) && persons.get(i).getSurname().equals(surname)) {
                person = persons.get(i);
            }
        }
        System.out.print("Akrabalik bagi bulunacak kisinin adi soyadi : ");
        String name2 = input.next();
        String surname2 = input.next();
        Person person2 = null;
        for (int i = 0; i < persons.size(); i++) {
            if (persons.get(i).getName().equals(name2) && persons.get(i).getSurname().equals(surname2)) {
                person2 = persons.get(i);
            }
        }
        int maxDepth = 0;
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).getPerson().toString().equals(person.toString())) {
                maxDepth = maxDepth(nodes.get(i));
            }
        }
        int maxDepth2 = 0;
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).getPerson().toString().equals(person2.toString())) {
                maxDepth2 = maxDepth(nodes.get(i));
            }
        }
        if (maxDepth < maxDepth2) {
            System.out.print(
                    person2.getName() + " " + person.getName() + "'in ");
            for (int i = 0; i < maxDepth2 - maxDepth; i++) {
                System.out.print("buyuk ");
            }
            String gender = "annesi";
            if (person2.getGender().equals("Erkek")) {
                gender = "babasi";
            }
            System.out.println(gender);

        } else if (maxDepth > maxDepth2) {
            System.out.print(
                    person.getName() + " " + person2.getName() + "'in ");
            for (int i = 0; i < maxDepth - maxDepth2; i++) {
                System.out.print("buyuk ");
            }
            String gender1 = "annesi";
            if (person.getGender().equals("Erkek")) {
                gender1 = "babasi";
            }
            System.out.println(gender1);
        }

    }

    public void createTreeForPerson(Person person, ArrayList<Person> family1, ArrayList<Person> family2,
            ArrayList<Person> family3, ArrayList<Person> family4) {
        ArrayList<Person> family = new ArrayList<Person>();
        Person grandFather = new Person();
        if (family1.contains(person)) {
            family = family1;
        } else if (family2.contains(person)) {
            family = family2;
        } else if (family3.contains(person)) {
            family = family3;
        } else if (family4.contains(person)) {
            family = family4;
        }
        ArrayList<Node> nodes = new ArrayList<Node>();
        for (int i = 0; i < family.size(); i++) {
            nodes.add(new Node(family.get(i)));
        }
        for (int i = 0; i < family.size(); i++) {
            if (family.get(i).getFatherName().equals("null") && family.get(i).getMotherName().equals("null")) {
                grandFather = family.get(i);
            }
        }

        printTree(getGrandByName(grandFather.getName()), 0);

    }

    public int maxDepth() {
        int maxDepth = 0;
        for (int i = 0; i < grands.size(); i++) {
            int depth = maxDepth(grands.get(i));
            if (depth > maxDepth) {
                maxDepth = depth;
            }
        }
        return maxDepth;
    }

    private int maxDepth(Node node) {
        if (node.getChildrens().size() == 0) {
            return 1;
        }
        int maxDepth = 0;
        for (int i = 0; i < node.getChildrens().size(); i++) {
            int depth = maxDepth(node.getChildrens().get(i));
            if (depth > maxDepth) {
                maxDepth = depth;
            }
        }
        return maxDepth + 1;
    }

    public int generations(ArrayList<Person> persons) {
        Scanner input = new Scanner(System.in);
        System.out.print("Sonrasinda kac jenerasyon oldugunu gormek istediginiz kisinin ad soyadi: ");
        String name = input.next();
        String surname = input.next();
        Person person = null;
        for (int i = 0; i < persons.size(); i++) {
            if (persons.get(i).getName().equals(name) && persons.get(i).getSurname().equals(surname)) {
                person = persons.get(i);
            }
        }
        int maxDepth = 0;
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).getPerson().toString().equals(person.toString())) {
                maxDepth = maxDepth(nodes.get(i));
            }
        }
        return maxDepth - 1;

    }

    public int ageCalculator(String birthDate) {
        int age = 0;
        String[] birthDateArray = birthDate.split("/");
        int birthDay = Integer.parseInt(birthDateArray[0]);
        int birthMonth = Integer.parseInt(birthDateArray[1]);
        int birthYear = Integer.parseInt(birthDateArray[2]);
        Calendar today = Calendar.getInstance();
        int day = today.get(Calendar.DAY_OF_MONTH);
        int month = today.get(Calendar.MONTH) + 1;
        int year = today.get(Calendar.YEAR);
        age = year - birthYear;
        if (birthMonth > month) {
            age--;
        } else if (birthMonth == month) {
            if (birthDay > day) {
                age--;
            }
        }
        return age;
    }

}
