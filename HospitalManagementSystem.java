import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

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

        addDoctor("Dr. Smith", "Cardiology");
        addDoctor("Dr. Jones", "Neurology");
        addPatient("Alice", 30, "Heart Palpitations");
        addPatient("Bob", 45, "Migraines");
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

    public Appointment scheduleAppointment(int patientId, int doctorId, String date) 
            throws IllegalArgumentException {
        Patient patient = findPatientById(patientId);
        Doctor doctor = findDoctorById(doctorId);

        if (patient == null) {
            throw new IllegalArgumentException("Patient with ID " + patientId + " not found.");
        }
        if (doctor == null) {
            throw new IllegalArgumentException("Doctor with ID " + doctorId + " not found.");
        }

        Appointment newAppointment = new Appointment(appointmentIdCounter++, patient, doctor, date);
        appointments.add(newAppointment);
        return newAppointment;
    }

    public List<Appointment> getAllAppointments() {
        return new ArrayList<>(appointments);
    }
}

public class HospitalManagementSystem extends JFrame {

    private final HospitalService service;
    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private final JTextArea viewTextArea;
    
    private final JTextField patientNameField;
    private final JTextField patientAgeField;
    private final JTextField patientDiagnosisField;

    private final JTextField doctorNameField;
    private final JTextField doctorSpecField;
    
    private final JTextField appPatientIdField;
    private final JTextField appDoctorIdField;
    private final JTextField appDateField;

    public HospitalManagementSystem() {
        service = new HospitalService();
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        viewTextArea = new JTextArea(20, 40);
        viewTextArea.setEditable(false);

        patientNameField = new JTextField(20);
        patientAgeField = new JTextField(5);
        patientDiagnosisField = new JTextField(20);

        doctorNameField = new JTextField(20);
        doctorSpecField = new JTextField(20);

        appPatientIdField = new JTextField(5);
        appDoctorIdField = new JTextField(5);
        appDateField = new JTextField(10);

        mainPanel.add(createMainMenuPanel(), "MENU");
        mainPanel.add(createAddPatientPanel(), "ADD_PATIENT");
        mainPanel.add(createAddDoctorPanel(), "ADD_DOCTOR");
        mainPanel.add(createScheduleAppointmentPanel(), "ADD_APPOINTMENT");
        mainPanel.add(createViewPanel(), "VIEW");

        add(mainPanel);
        setTitle("Hospital Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createMainMenuPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton addPatientBtn = new JButton("Add Patient");
        addPatientBtn.addActionListener(e -> cardLayout.show(mainPanel, "ADD_PATIENT"));

        JButton addDoctorBtn = new JButton("Add Doctor");
        addDoctorBtn.addActionListener(e -> cardLayout.show(mainPanel, "ADD_DOCTOR"));

        JButton scheduleAppBtn = new JButton("Schedule Appointment");
        scheduleAppBtn.addActionListener(e -> cardLayout.show(mainPanel, "ADD_APPOINTMENT"));

        JButton viewPatientsBtn = new JButton("View Patients");
        viewPatientsBtn.addActionListener(e -> {
            updateViewArea(service.getAllPatients());
            cardLayout.show(mainPanel, "VIEW");
        });

        JButton viewDoctorsBtn = new JButton("View Doctors");
        viewDoctorsBtn.addActionListener(e -> {
            updateViewArea(service.getAllDoctors());
            cardLayout.show(mainPanel, "VIEW");
        });

        JButton viewAppsBtn = new JButton("View Appointments");
        viewAppsBtn.addActionListener(e -> {
            updateViewArea(service.getAllAppointments());
            cardLayout.show(mainPanel, "VIEW");
        });

        panel.add(addPatientBtn);
        panel.add(addDoctorBtn);
        panel.add(scheduleAppBtn);
        panel.add(viewPatientsBtn);
        panel.add(viewDoctorsBtn);
        panel.add(viewAppsBtn);

        return panel;
    }

    private JPanel createFormPanel(String title, JTextField[] fields, JLabel[] labels, ActionListener submitAction) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel formGrid = new JPanel(new GridLayout(fields.length, 2, 10, 10));
        for (int i = 0; i < fields.length; i++) {
            formGrid.add(labels[i]);
            formGrid.add(fields[i]);
        }
        panel.add(formGrid, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(submitAction);
        
        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "MENU"));

        buttonPanel.add(submitButton);
        buttonPanel.add(backButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createAddPatientPanel() {
        JTextField[] fields = {patientNameField, patientAgeField, patientDiagnosisField};
        JLabel[] labels = {new JLabel("Name:"), new JLabel("Age:"), new JLabel("Diagnosis:")};
        
        ActionListener action = e -> {
            try {
                String name = patientNameField.getText();
                int age = Integer.parseInt(patientAgeField.getText());
                String diagnosis = patientDiagnosisField.getText();
                
                if (name.isEmpty() || diagnosis.isEmpty()) {
                    throw new IllegalArgumentException("Fields cannot be empty.");
                }

                service.addPatient(name, age, diagnosis);
                JOptionPane.showMessageDialog(this, "Patient added successfully!");
                patientNameField.setText("");
                patientAgeField.setText("");
                patientDiagnosisField.setText("");
                cardLayout.show(mainPanel, "MENU");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid age. Please enter a number.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        };
        
        return createFormPanel("Add New Patient", fields, labels, action);
    }

    private JPanel createAddDoctorPanel() {
        JTextField[] fields = {doctorNameField, doctorSpecField};
        JLabel[] labels = {new JLabel("Name:"), new JLabel("Specialization:")};
        
        ActionListener action = e -> {
            try {
                String name = doctorNameField.getText();
                String specialization = doctorSpecField.getText();

                if (name.isEmpty() || specialization.isEmpty()) {
                    throw new IllegalArgumentException("Fields cannot be empty.");
                }

                service.addDoctor(name, specialization);
                JOptionPane.showMessageDialog(this, "Doctor added successfully!");
                doctorNameField.setText("");
                doctorSpecField.setText("");
                cardLayout.show(mainPanel, "MENU");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        };
        
        return createFormPanel("Add New Doctor", fields, labels, action);
    }

    private JPanel createScheduleAppointmentPanel() {
        JTextField[] fields = {appPatientIdField, appDoctorIdField, appDateField};
        JLabel[] labels = {new JLabel("Patient ID:"), new JLabel("Doctor ID:"), new JLabel("Date (YYYY-MM-DD):")};
        
        ActionListener action = e -> {
            try {
                int patientId = Integer.parseInt(appPatientIdField.getText());
                int doctorId = Integer.parseInt(appDoctorIdField.getText());
                String date = appDateField.getText();

                if (date.isEmpty()) {
                    throw new IllegalArgumentException("Date cannot be empty.");
                }

                service.scheduleAppointment(patientId, doctorId, date);
                JOptionPane.showMessageDialog(this, "Appointment scheduled successfully!");
                appPatientIdField.setText("");
                appDoctorIdField.setText("");
                appDateField.setText("");
                cardLayout.show(mainPanel, "MENU");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid ID. Please enter a number.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        };
        
        return createFormPanel("Schedule Appointment", fields, labels, action);
    }

    private JPanel createViewPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(viewTextArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "MENU"));
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(backButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private <T> void updateViewArea(List<T> items) {
        viewTextArea.setText("");
        if (items.isEmpty()) {
            viewTextArea.setText("No items found.");
        } else {
            for (T item : items) {
                viewTextArea.append(item.toString() + "\n\n");
            }
        }
        viewTextArea.setCaretPosition(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HospitalManagementSystem());
    }
}