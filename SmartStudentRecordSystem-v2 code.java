import java.io.*;
import java.util.Scanner;

class Student {
    String id;
    String name;
    int marks;

    public Student(String id, String name, int marks) {
        this.id = id;
        this.name = name;
        this.marks = marks;
    }

    @Override
    public String toString() {
        return id + " | " + name + " | " + marks;
    }
}

class StudentLinkedList {
    private static class Node {
        Student data;
        Node next;

        Node(Student data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node head;
    private int count;

    public StudentLinkedList() {
        this.head = null;
        this.count = 0;
    }

    public void add(Student student) {
        Node newNode = new Node(student);
        if (head == null) {
            head = newNode;
        } else {
            Node temp = head;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = newNode;
        }
        count++;
    }

    public void displayAll() {
        if (head == null) {
            System.out.println("No records found.");
            return;
        }
        Node temp = head;
        while (temp != null) {
            System.out.println(temp.data);
            temp = temp.next;
        }
    }

    public Student searchById(String id) {
        Node temp = head;
        while (temp != null) {
            if (temp.data.id.equalsIgnoreCase(id)) {
                return temp.data;
            }
            temp = temp.next;
        }
        return null;
    }

    public boolean deleteById(String id) {
        if (head == null) {
            return false;
        }
        if (head.data.id.equalsIgnoreCase(id)) {
            head = head.next;
            count--;
            return true;
        }
        Node current = head;
        while (current.next != null) {
            if (current.next.data.id.equalsIgnoreCase(id)) {
                current.next = current.next.next;
                count--;
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public Student[] toArray() {
        Student[] arr = new Student[count];
        Node temp = head;
        int i = 0;
        while (temp != null) {
            arr[i++] = temp.data;
            temp = temp.next;
        }
        return arr;
    }
}

class StudentBST {
    private static class BSTNode {
        Student data;
        BSTNode left, right;

        BSTNode(Student data) {
            this.data = data;
            this.left = null;
            this.right = null;
        }
    }

    private BSTNode root;

    public StudentBST() {
        this.root = null;
    }

    public void insert(Student student) {
        root = insertRec(root, student);
    }

    private BSTNode insertRec(BSTNode root, Student student) {
        if (root == null) {
            return new BSTNode(student);
        }
        if (student.name.compareToIgnoreCase(root.data.name) < 0) {
            root.left = insertRec(root.left, student);
        } else {
            root.right = insertRec(root.right, student);
        }
        return root;
    }

    public Student searchByName(String name) {
        return searchRec(root, name);
    }

    private Student searchRec(BSTNode root, String name) {
        if (root == null) {
            return null;
        }
        int cmp = name.compareToIgnoreCase(root.data.name);
        if (cmp == 0) {
            return root.data;
        } else if (cmp < 0) {
            return searchRec(root.left, name);
        } else {
            return searchRec(root.right, name);
        }
    }

    public void rebuild(Student[] students) {
        root = null;
        for (Student s : students) {
            insert(s);
        }
    }
}

public class SmartStudentRecordSystem {
    private static final String FILE_NAME = "student_records.txt";
    private static StudentLinkedList studentList = new StudentLinkedList();
    private static StudentBST studentBST = new StudentBST();
    private static int idCounter = 101;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        loadFromFile();

        while (true) {
            System.out.println("\n1. Add Student");
            System.out.println("2. Display All");
            System.out.println("3. Search Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Rank List (Merge Sort)");
            System.out.println("6. Save & Exit");
            System.out.print("Enter choice (1-6): ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid choice.");
                continue;
            }

            switch (choice) {
                case 1:
                    addStudent();
                    break;
                case 2:
                    studentList.displayAll();
                    break;
                case 3:
                    searchStudent();
                    break;
                case 4:
                    deleteStudent();
                    break;
                case 5:
                    displayRankList();
                    break;
                case 6:
                    saveToFile();
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void addStudent() {
        System.out.print("Enter Name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter Marks (0-100): ");
        int marks;
        try {
            marks = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid marks. Must be between 0 and 100.");
            return;
        }

        if (marks < 0 || marks > 100) {
            System.out.println("Invalid marks. Must be between 0 and 100.");
            return;
        }

        String id = "S" + (idCounter++);
        Student student = new Student(id, name, marks);
        studentList.add(student);
        studentBST.insert(student);

        System.out.println("Student added. ID: " + student);
    }

    private static void searchStudent() {
        System.out.println("1. Search by ID");
        System.out.println("2. Search by Name");
        System.out.print("Enter option: ");
        String option = scanner.nextLine().trim();

        if (option.equals("1")) {
            System.out.print("Enter Student ID: ");
            String id = scanner.nextLine().trim();
            Student found = studentList.searchById(id);
            if (found != null) {
                System.out.println(found);
            } else {
                System.out.println("Student " + id + " not found.");
            }
        } else if (option.equals("2")) {
            System.out.print("Enter Student Name: ");
            String name = scanner.nextLine().trim();
            Student found = studentBST.searchByName(name);
            if (found != null) {
                System.out.println(found);
            } else {
                System.out.println("Student " + name + " not found.");
            }
        } else {
            System.out.println("Invalid option.");
        }
    }

    private static void deleteStudent() {
        System.out.print("Enter Student ID: ");
        String id = scanner.nextLine().trim();

        if (studentList.deleteById(id)) {
            studentBST.rebuild(studentList.toArray());
            System.out.println("Student " + id + " deleted.");
        } else {
            System.out.println("Student " + id + " not found.");
        }
    }

    private static void displayRankList() {
        Student[] arr = studentList.toArray();
        if (arr.length == 0) {
            System.out.println("No records found.");
            return;
        }

        mergeSort(arr, 0, arr.length - 1);

        System.out.println("Rank list:");
        for (int i = 0; i < arr.length; i++) {
            System.out.println((i + 1) + ". " + arr[i]);
        }
    }

    private static void mergeSort(Student[] arr, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSort(arr, left, mid);
            mergeSort(arr, mid + 1, right);
            merge(arr, left, mid, right);
        }
    }

    private static void merge(Student[] arr, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        Student[] L = new Student[n1];
        Student[] R = new Student[n2];

        for (int i = 0; i < n1; i++) {
            L[i] = arr[left + i];
        }
        for (int j = 0; j < n2; j++) {
            R[j] = arr[mid + 1 + j];
        }

        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (L[i].marks >= R[j].marks) {
                arr[k++] = L[i++];
            } else {
                arr[k++] = R[j++];
            }
        }

        while (i < n1) {
            arr[k++] = L[i++];
        }
        while (j < n2) {
            arr[k++] = R[j++];
        }
    }

    private static void loadFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 3) {
                    String id = parts[0].trim();
                    String name = parts[1].trim();
                    int marks = Integer.parseInt(parts[2].trim());

                    Student s = new Student(id, name, marks);
                    studentList.add(s);
                    studentBST.insert(s);

                    if (id.startsWith("S")) {
                        int num = Integer.parseInt(id.substring(1));
                        if (num >= idCounter) {
                            idCounter = num + 1;
                        }
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error loading records.");
        }
    }

    private static void saveToFile() {
        Student[] records = studentList.toArray();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Student s : records) {
                bw.write(s.id + " | " + s.name + " | " + s.marks);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving records.");
        }
    }
}
