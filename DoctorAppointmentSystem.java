import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

// Main class to run the application
public class DoctorAppointmentSystem {
    public static void main(String[] args) {
        // Run the GUI on the Event Dispatch Thread for thread safety
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }
}

// ---------------------------------------------------
// MODEL CLASSES (Data Representation)
// ---------------------------------------------------

class Doctor {
    private static int idCounter = 1;
    private int id;
    private String name;
    private String specialization;

    public Doctor(String name, String specialization) {
        this.id = idCounter++;
        this.name = name;
        this.specialization = specialization;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getSpecialization() { return specialization; }

    @Override
    public String toString() {
        return name + " (" + specialization + ")";
    }
}

class Patient {
    private static int idCounter = 1;
    private int id;
    private String name;
    private String contactInfo;

    public Patient(String name, String contactInfo) {
        this.id = idCounter++;
        this.name = name;
        this.contactInfo = contactInfo;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getContactInfo() { return contactInfo; }

    @Override
    public String toString() {
        return name;
    }
}

class Appointment {
    private static int idCounter = 1;
    private int id;
    private Doctor doctor;
    private Patient patient;
    private LocalDateTime appointmentDateTime;

    public Appointment(Doctor doctor, Patient patient, LocalDateTime appointmentDateTime) {
        this.id = idCounter++;
        this.doctor = doctor;
        this.patient = patient;
        this.appointmentDateTime = appointmentDateTime;
    }

    public int getId() { return id; }
    public Doctor getDoctor() { return doctor; }
    public Patient getPatient() { return patient; }
    
    public String getFormattedDateTime() {
        return appointmentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}

// ---------------------------------------------------
// SERVICE CLASS (Data Management)
// ---------------------------------------------------

class DataManager {
    private static DataManager instance;
    private final List<Doctor> doctors;
    private final List<Patient> patients;
    private final List<Appointment> appointments;

    private DataManager() {
        doctors = new ArrayList<>();
        patients = new ArrayList<>();
        appointments = new ArrayList<>();
        addInitialData();
    }

    public static synchronized DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    private void addInitialData() {
        doctors.add(new Doctor("Dr. Smith", "Cardiology"));
        doctors.add(new Doctor("Dr. Jones", "Neurology"));
        
        patients.add(new Patient("John Doe", "555-1234"));
        patients.add(new Patient("Jane Roe", "555-5678"));

        appointments.add(new Appointment(doctors.get(0), patients.get(0), LocalDateTime.now().plusDays(1)));
    }

    public void addDoctor(Doctor doctor) { doctors.add(doctor); }
    public List<Doctor> getDoctors() { return doctors; }

    public void addPatient(Patient patient) { patients.add(patient); }
    public List<Patient> getPatients() { return patients; }

    public void addAppointment(Appointment appointment) { appointments.add(appointment); }
    public List<Appointment> getAppointments() { return appointments; }
}

// ---------------------------------------------------
// UI CLASSES (GUI Components)
// ---------------------------------------------------

class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("Doctor Appointment Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        DoctorPanel doctorPanel = new DoctorPanel();
        PatientPanel patientPanel = new PatientPanel();
        AppointmentPanel appointmentPanel = new AppointmentPanel();

        tabbedPane.addTab("Appointments", null, appointmentPanel, "Manage Appointments");
        tabbedPane.addTab("Doctors", null, doctorPanel, "Manage Doctors");
        tabbedPane.addTab("Patients", null, patientPanel, "Manage Patients");
        
        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedComponent() == appointmentPanel) {
                appointmentPanel.refreshData();
            }
        });

        add(tabbedPane, BorderLayout.CENTER);
    }
}

class DoctorPanel extends JPanel {
    private final DataManager dataManager;
    private final DefaultTableModel tableModel;
    private final JTextField nameField;
    private final JTextField specializationField;

    public DoctorPanel() {
        dataManager = DataManager.getInstance();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Add New Doctor"));
        
        nameField = new JTextField();
        specializationField = new JTextField();
        JButton addButton = new JButton("Add Doctor");

        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Specialization:"));
        formPanel.add(specializationField);
        formPanel.add(new JLabel());
        formPanel.add(addButton);

        String[] columnNames = {"ID", "Name", "Specialization"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable doctorTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(doctorTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Existing Doctors"));

        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        addButton.addActionListener(e -> addDoctor());
        refreshDoctorTable();
    }

    private void addDoctor() {
        String name = nameField.getText();
        String spec = specializationField.getText();

        if (name.isEmpty() || spec.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        dataManager.addDoctor(new Doctor(name, spec));
        nameField.setText("");
        specializationField.setText("");
        refreshDoctorTable();
    }

    public void refreshDoctorTable() {
        tableModel.setRowCount(0);
        List<Doctor> doctors = dataManager.getDoctors();
        for (Doctor doc : doctors) {
            tableModel.addRow(new Object[]{doc.getId(), doc.getName(), doc.getSpecialization()});
        }
    }
}

class PatientPanel extends JPanel {
    private final DataManager dataManager;
    private final DefaultTableModel tableModel;
    private final JTextField nameField;
    private final JTextField contactField;

    public PatientPanel() {
        dataManager = DataManager.getInstance();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Add New Patient"));

        nameField = new JTextField();
        contactField = new JTextField();
        JButton addButton = new JButton("Add Patient");

        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Contact Info:"));
        formPanel.add(contactField);
        formPanel.add(new JLabel());
        formPanel.add(addButton);

        String[] columnNames = {"ID", "Name", "Contact Info"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable patientTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(patientTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Existing Patients"));

        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        addButton.addActionListener(e -> addPatient());
        refreshPatientTable();
    }

    private void addPatient() {
        String name = nameField.getText();
        String contact = contactField.getText();

        if (name.isEmpty() || contact.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        dataManager.addPatient(new Patient(name, contact));
        nameField.setText("");
        contactField.setText("");
        refreshPatientTable();
    }

    public void refreshPatientTable() {
        tableModel.setRowCount(0);
        List<Patient> patients = dataManager.getPatients();
        for (Patient p : patients) {
            tableModel.addRow(new Object[]{p.getId(), p.getName(), p.getContactInfo()});
        }
    }
}

class AppointmentPanel extends JPanel {
    private final DataManager dataManager;
    private final DefaultTableModel tableModel;
    private final JComboBox<Doctor> doctorComboBox;
    private final JComboBox<Patient> patientComboBox;
    private final JTextField dateTimeField;

    public AppointmentPanel() {
        dataManager = DataManager.getInstance();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Book New Appointment"));

        doctorComboBox = new JComboBox<>();
        patientComboBox = new JComboBox<>();
        dateTimeField = new JTextField();
        JButton bookButton = new JButton("Book Appointment");
        
        formPanel.add(new JLabel("Select Doctor:"));
        formPanel.add(doctorComboBox);
        formPanel.add(new JLabel("Select Patient:"));
        formPanel.add(patientComboBox);
        formPanel.add(new JLabel("Date & Time (yyyy-MM-dd HH:mm):"));
        formPanel.add(dateTimeField);
        formPanel.add(new JLabel());
        formPanel.add(bookButton);

        String[] columnNames = {"ID", "Doctor", "Patient", "Date & Time"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable appointmentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(appointmentTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Scheduled Appointments"));

        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        bookButton.addActionListener(e -> bookAppointment());
        refreshData();
    }

    private void bookAppointment() {
        Doctor selectedDoctor = (Doctor) doctorComboBox.getSelectedItem();
        Patient selectedPatient = (Patient) patientComboBox.getSelectedItem();
        String dateTimeStr = dateTimeField.getText();

        if (selectedDoctor == null || selectedPatient == null || dateTimeStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            Appointment newAppointment = new Appointment(selectedDoctor, selectedPatient, dateTime);
            dataManager.addAppointment(newAppointment);
            dateTimeField.setText("");
            refreshAppointmentTable();
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid date/time format. Please use 'yyyy-MM-dd HH:mm'.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void refreshData() {
        refreshDoctorComboBox();
        refreshPatientComboBox();
        refreshAppointmentTable();
    }

    private void refreshDoctorComboBox() {
        doctorComboBox.removeAllItems();
        List<Doctor> doctors = dataManager.getDoctors();
        for (Doctor doc : doctors) {
            doctorComboBox.addItem(doc);
        }
    }

    private void refreshPatientComboBox() {
        patientComboBox.removeAllItems();
        List<Patient> patients = dataManager.getPatients();
        for (Patient p : patients) {
            patientComboBox.addItem(p);
        }
    }

    private void refreshAppointmentTable() {
        tableModel.setRowCount(0);
        List<Appointment> appointments = dataManager.getAppointments();
        for (Appointment app : appointments) {
            tableModel.addRow(new Object[]{
                app.getId(),
                app.getDoctor().getName(),
                app.getPatient().getName(),
                app.getFormattedDateTime()
            });
        }
    }
}