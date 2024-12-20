import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class TaskSorter extends JFrame {
    private DefaultListModel<Task> taskListModel;
    private JList<Task> taskList;
    private JTextField taskNameField;
    private JTextField priorityField;
    private JComboBox<String> sortMethodComboBox;

    private ArrayList<Task> tasks;

    public TaskSorter() {
        super("Aplikasi Pengurutan Tugas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        tasks = new ArrayList<>();
        taskListModel = new DefaultListModel<>();
        
        initializeComponents();
        layoutComponents();
        
        setSize(500, 600);
        setLocationRelativeTo(null);
    }

    private void initializeComponents() {
        // Initialize input components
        taskNameField = new JTextField(20);
        priorityField = new JTextField(5);
        
        String[] sortMethods = {"Bubble Sort", "Selection Sort"};
        sortMethodComboBox = new JComboBox<>(sortMethods);
        
        // Initialize task list
        taskList = new JList<>(taskListModel);
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void layoutComponents() {
        // Input panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Nama Tugas:"), gbc);
        
        gbc.gridx = 1;
        inputPanel.add(taskNameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Prioritas (1-10):"), gbc);
        
        gbc.gridx = 1;
        inputPanel.add(priorityField, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Tambah Tugas");
        JButton sortButton = new JButton("Urutkan");
        JButton clearButton = new JButton("Hapus Semua");
        
        buttonPanel.add(addButton);
        buttonPanel.add(sortButton);
        buttonPanel.add(clearButton);
        
        // Main layout
        setLayout(new BorderLayout(10, 10));
        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(taskList), BorderLayout.CENTER);
        
        JPanel southPanel = new JPanel(new GridLayout(2, 1));
        southPanel.add(sortMethodComboBox);
        southPanel.add(buttonPanel);
        add(southPanel, BorderLayout.SOUTH);
        
        // Add button listener
        addButton.addActionListener(e -> addTask());
        
        // Sort button listener
        sortButton.addActionListener(e -> sortTasks());
        
        // Clear button listener
        clearButton.addActionListener(e -> clearTasks());
    }

    private void addTask() {
        try {
            String name = taskNameField.getText().trim();
            int priority = Integer.parseInt(priorityField.getText().trim());
            
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nama tugas tidak boleh kosong!");
                return;
            }
            
            if (priority < 1 || priority > 10) {
                JOptionPane.showMessageDialog(this, "Prioritas harus antara 1-10!");
                return;
            }
            
            Task task = new Task(name, priority);
            tasks.add(task);
            updateTaskList();
            
            // Clear input fields
            taskNameField.setText("");
            priorityField.setText("");
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Prioritas harus berupa angka!");
        }
    }

    private void sortTasks() {
        if (tasks.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tidak ada tugas untuk diurutkan!");
            return;
        }

        String selectedMethod = (String) sortMethodComboBox.getSelectedItem();
        
        if (selectedMethod.equals("Bubble Sort")) {
            bubbleSort();
        } else {
            selectionSort();
        }
        
        updateTaskList();
    }

    private void bubbleSort() {
        int n = tasks.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (tasks.get(j).getPriority() > tasks.get(j + 1).getPriority()) {
                    // Swap tasks
                    Task temp = tasks.get(j);
                    tasks.set(j, tasks.get(j + 1));
                    tasks.set(j + 1, temp);
                }
            }
        }
    }

    private void selectionSort() {
        int n = tasks.size();
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (tasks.get(j).getPriority() < tasks.get(minIdx).getPriority()) {
                    minIdx = j;
                }
            }
            // Swap tasks
            Task temp = tasks.get(minIdx);
            tasks.set(minIdx, tasks.get(i));
            tasks.set(i, temp);
        }
    }

    private void clearTasks() {
        tasks.clear();
        updateTaskList();
    }

    private void updateTaskList() {
        taskListModel.clear();
        for (Task task : tasks) {
            taskListModel.addElement(task);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TaskSorter().setVisible(true);
        });
    }
}

class Task {
    private String name;
    private int priority;
    
    public Task(String name, int priority) {
        this.name = name;
        this.priority = priority;
    }
    
    public String getName() {
        return name;
    }
    
    public int getPriority() {
        return priority;
    }
    
    @Override
    public String toString() {
        return String.format("Tugas: %s (Prioritas: %d)", name, priority);
    }
}
