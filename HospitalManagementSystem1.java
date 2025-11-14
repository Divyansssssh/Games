import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

class Patient {
    private int id;
    private String name;
    private int age;
    private String diagnosis;

    public Patient(int id, String name, int age, String diagnosis) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.diagnosis = diagnosis;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    @Override
    public String toString() {
        return "Patient [ID=" + id + ", Name=" + name + ", Age=" + age + ", Diagnosis=" + diagnosis + "]";
    }
}

class Doctor {
    private int id;
    private String name;
    private String specialization;

    public Doctor(int id, String name, String specialization) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSpecialization() {
        return specialization;
    }

    @Override
    public String toString() {
        return "Doctor [ID=" + id + ", Name=" + name + ", Specialization=" + specialization + "]";
    }
}

class Appointment {
    private int id;
    private Patient patient;
    private Doctor doctor;
    private String date;

    public Appointment(int id, Patient patient, Doctor doctor, String date) {
        this.id = id;
        this.patient = patient;
        this.doctor = doctor;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public Patient getPatient() {
        return patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Appointment [ID=" + id + ", Date=" + date + "\n  Patient: " + patient.getName() + 
               "\n  Doctor: " + doctor.getName() + " (" + doctor.getSpecialization() + ")]";
    }
}

class HospitalService {

    private List<Patient> patients;
    private List<Doctor> doctors;
    private List<Appointment> appointments;

    private int patientIdCounter;
    private int doctorIdCounter;
    private int appointmentIdCounter;

    public HospitalService() {
        this.patients = new ArrayList<>();
        this.doctors = new ArrayList<>();
        this.appointments = new ArrayList<>();
        this.patientIdCounter = 1;
        this.doctorIdCounter = 1;
        this.appointmentIdCounter = 1;
    }

    public Patient addPatient(String name, int age, String diagnosis) {
        Patient newPatient = new Patient(patientIdCounter++, name, age, diagnosis);
        patients.add(newPatient);
        return newPatient;
    }

    public Patient findPatientById(int id) {
        for (Patient patient : patients) {
            if (patient.getId() == id) {
                return patient;
            }
        }
        return null; 
    }

    public List<Patient> getAllPatients() {
        return new ArrayList<>(patients); 
    }

    public Doctor addDoctor(String name, String specialization) {
        Doctor newDoctor = new Doctor(doctorIdCounter++, name, specialization);
        doctors.add(newDoctor);
        return newDoctor;
    }

    public Doctor findDoctorById(int id) {
        for (Doctor doctor : doctors) {
            if (doctor.getId() == id) {
                return doctor;
            }
        }
        return null;
    }

    public List<Doctor> getAllDoctors() {
        return new ArrayList<>(doctors);
    }

    public Appointment scheduleAppointment(int patientId, int doctorId, String date) {
        Patient patient = findPatientById(patientId);
        Doctor doctor = findDoctorById(doctorId);

        if (patient == null) {
            System.out.println("Error: Patient with ID " + patientId + " not found.");
            return null;
        }
        if (doctor == null) {
            System.out.println("Error: Doctor with ID " + doctorId + " not found.");
            return null;
        }

        Appointment newAppointment = new Appointment(appointmentIdCounter++, patient, doctor, date);
        appointments.add(newAppointment);
        return newAppointment;
    }

    public List<Appointment> getAllAppointments() {
        return new ArrayList<>(appointments);
    }
}

public class HospitalManagementSystem {

    private static final HospitalService service = new HospitalService();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        service.addDoctor("Dr. Smith", "Cardiology");
        service.addDoctor("Dr. Jones", "Neurology");
        service.addPatient("Alice", 30, "Heart Palpitations");
        service.addPatient("Bob", 45, "Migraines");

        run();
    }

    public static void run() {
        boolean running = true;
        while (running) {
            displayMenu();
            int choice = getIntInput();

            switch (choice) {
                case 1:
                    handleAddPatient();
                    break;
                case 2:
                    handleAddDoctor();
                    break;
                case 3:
                    handleScheduleAppointment();
                    break;
                case 4:
                    handleViewPatients();
                    break;
                case 5:
                    handleViewDoctors();
                    break;
                case 6:
                    handleViewAppointments();
                    break;
                case 7:
                    running = false;
                    System.out.println("Exiting system. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again (1-7).");
            }
            if (running) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine(); 
            }
        }
        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("\n--- Hospital Management System ---");
        System.out.println("1. Add Patient");
        System.out.println("2. Add Doctor");
        System.out.println("3. Schedule Appointment");
        System.out.println("4. View Patients");
        System.out.println("5. View Doctors");
        System.out.println("6. View Appointments");
        System.out.println("7. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void handleAddPatient() {
        System.out.print("Enter Patient Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Patient Age: ");
        int age = getIntInput();
        System.out.print("Enter Patient Diagnosis: ");
        String diagnosis = scanner.nextLine();

        Patient p = service.addPatient(name, age, diagnosis);
        System.out.println("Patient added successfully: " + p);
    }

    private static void handleAddDoctor() {
        System.out.print("Enter Doctor Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Doctor Specialization: ");
        String specialization = scanner.nextLine();

        Doctor d = service.addDoctor(name, specialization);
        System.out.println("Doctor added successfully: " + d);
    }

    private static void handleScheduleAppointment() {
        System.out.println("\n--- Available Doctors ---");
        handleViewDoctors();
        System.out.print("Enter Doctor ID: ");
        int docId = getIntInput();

        System.out.println("\n--- Registered Patients ---");
        handleViewPatients();
        System.out.print("Enter Patient ID: ");
        int patId = getIntInput();

        System.out.print("Enter Appointment Date (YYYY-MM-DD): ");
        String date = scanner.nextLine();

        Appointment app = service.scheduleAppointment(patId, docId, date);
        if (app != null) {
            System.out.println("Appointment scheduled successfully!");
            System.out.println(app);
        } else {
            System.out.println("Failed to schedule appointment. Check IDs.");
        }
    }

    private static void handleViewPatients() {
        List<Patient> patients = service.getAllPatients();
        if (patients.isEmpty()) {
            System.out.println("No patients found.");
            return;
        }
        System.out.println("\n--- Patient List ---");
        for (Patient p : patients) {
            System.out.println(p);
        }
    }

    private static void handleViewDoctors() {
        List<Doctor> doctors = service.getAllDoctors();
        if (doctors.isEmpty()) {
            System.out.println("No doctors found.");
            return;
        }
        System.out.println("\n--- Doctor List ---");
        for (Doctor d : doctors) {
            System.out.println(d);
        }
    }

    private static void handleViewAppointments() {
        List<Appointment> appointments = service.getAllAppointments();
        if (appointments.isEmpty()) {
            System.out.println("No appointments found.");
            return;
        }
        System.out.println("\n--- Appointment List ---");
        for (Appointment a : appointments) {
            System.out.println("--------------------");
            System.out.println(a);
        }
        System.out.println("--------------------");
    }

    private static int getIntInput() {
        while (true) {
            try {
                int value = scanner.nextInt();
                scanner.nextLine(); 
                return value;
            } catch (InputMismatchException e) {
                System.out.print("Invalid input. Please enter a number: ");
                scanner.nextLine(); 
            }
        }
    }
}