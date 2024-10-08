import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class HotelManagementApp {

    private Hotel hotel;

    public HotelManagementApp() {
        // Зчитування даних з файлу
        readDataFromFile("hotel_data.txt");

        // Створення графічного інтерфейсу
        JFrame frame = new JFrame("Hotel Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JButton adminsButton = new JButton("Кількість адміністраторів");
        JButton residentsButton = new JButton("Кількість мешканців");
        JButton occupiedRoomTypesButton = new JButton("Кількість типів зайнятих номерів");
        JButton residentsByTypeButton = new JButton("Список мешканців обраного типу номеру");
        JButton longestStayButton = new JButton("Найбільший термін проживання");
        JButton mostDemandedTypeButton = new JButton("Тип номеру з найбільшим попитом");
        JButton saveDataButton = new JButton("Зберегти дані у файл");
        JButton addEntryButton = new JButton("Додати запис про готель");
        JButton addResidentButton = new JButton("Додати клієнта");

        adminsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int countAdmins = hotel.countAdministrators();
                JOptionPane.showMessageDialog(frame, "Кількість адміністраторів: " + countAdmins);
            }
        });

        residentsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int countResidents = hotel.countResidents();
                JOptionPane.showMessageDialog(frame, "Кількість мешканців: " + countResidents);
            }
        });

        occupiedRoomTypesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int countOccupiedRoomTypes = hotel.countOccupiedRoomTypes();
                JOptionPane.showMessageDialog(frame, "Кількість типів зайнятих номерів: " + countOccupiedRoomTypes);
            }
        });

        residentsByTypeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String roomType = JOptionPane.showInputDialog(frame, "Введіть тип номеру:");
                if (roomType != null) {
                    java.util.List<String> residentsByType = hotel.getResidentsByRoomType(roomType);
                    String result = String.join(", ", residentsByType);
                    JOptionPane.showMessageDialog(frame, "Мешканці номерів типу " + roomType + ": " + result);
                }
            }
        });

        longestStayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Resident longestStayResident = hotel.getLongestStayResident();
                if (longestStayResident != null) {
                    JOptionPane.showMessageDialog(frame, "Найбільший термін проживання має мешканець " +
                            longestStayResident.getName() + " (" + longestStayResident.getStayDuration() + " днів)");
                } else {
                    JOptionPane.showMessageDialog(frame, "Немає мешканців");
                }
            }
        });

        mostDemandedTypeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String mostDemandedType = hotel.getMostDemandedRoomType();
                if (mostDemandedType != null) {
                    JOptionPane.showMessageDialog(frame, "Тип номеру з найбільшим попитом: " + mostDemandedType);
                } else {
                    JOptionPane.showMessageDialog(frame, "Немає мешканців");
                }
            }
        });

        saveDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveDataToFile("hotel_data.txt");
                JOptionPane.showMessageDialog(frame, "Дані збережено у файл");
            }
        });

        addEntryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addHotelEntry();
            }
        });

        addResidentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addResidentEntry();
            }
        });

        JPanel panel = new JPanel();
        panel.add(adminsButton);
        panel.add(residentsButton);
        panel.add(occupiedRoomTypesButton);
        panel.add(residentsByTypeButton);
        panel.add(longestStayButton);
        panel.add(mostDemandedTypeButton);
        panel.add(addEntryButton);
        panel.add(saveDataButton);
        panel.add(addResidentButton);

        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }

    private void readDataFromFile(String fileName) {
        try {
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);

            // Отримання назви, адреси та номеру телефону готелю
            String hotelName = scanner.nextLine().trim();
            String hotelAddress = scanner.nextLine().trim();
            String hotelPhoneNumber = scanner.nextLine().trim();

            // Створення об'єкта готелю
            hotel = new Hotel(hotelName, hotelAddress, hotelPhoneNumber);

            // Додавання адміністраторів
            int numAdmins = Integer.parseInt(scanner.nextLine().trim());
            for (int i = 0; i < numAdmins; i++) {
                String adminName = scanner.nextLine().trim();
                Administrator admin = new Administrator(adminName);
                hotel.addAdministrator(admin);
            }

            // Додавання номерів
            int numRooms = Integer.parseInt(scanner.nextLine().trim());
            for (int i = 0; i < numRooms; i++) {
                String roomType = scanner.nextLine().trim();
                int roomId = Integer.parseInt(scanner.nextLine().trim());
                Room room = new Room(roomType, roomId);
                hotel.addRoom(room);
            }

            // Додавання мешканців
            int numResidents = Integer.parseInt(scanner.nextLine().trim());
            for (int i = 0; i < numResidents; i++) {
                String residentName = scanner.nextLine().trim();
                int roomNumber = Integer.parseInt(scanner.nextLine().trim());
                int stayDuration = Integer.parseInt(scanner.nextLine().trim());

                Room residentRoom = hotel.getRoomByNumber(roomNumber);
                Resident resident = new Resident(residentName, residentRoom, stayDuration);
                hotel.addResident(resident);
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Файл не знайдено: " + fileName);
        }
    }

    private void saveDataToFile(String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            // Записуємо дані готелю
            writer.write(hotel.getName() + "\n");
            writer.write(hotel.getAddress() + "\n");
            writer.write(hotel.getPhoneNumber() + "\n");

            // Записуємо кількість адміністраторів та їхні імена
            writer.write(hotel.getAdministrators().size() + "\n");
            for (Administrator admin : hotel.getAdministrators()) {
                writer.write(admin.getName() + "\n");
            }

            // Записуємо кількість номерів та їхні дані
            writer.write(hotel.getRooms().size() + "\n");
            for (Room room : hotel.getRooms()) {
                writer.write(room.getType() + "\n");
                writer.write(room.getNumber() + "\n");
            }

            // Записуємо кількість мешканців та їхні дані
            writer.write(hotel.getResidents().size() + "\n");
            for (Resident resident : hotel.getResidents()) {
                writer.write(resident.getName() + "\n");
                writer.write(resident.getRoom().getNumber() + "\n");
                writer.write(resident.getStayDuration() + "\n");
            }

        } catch (IOException e) {
            System.err.println("Помилка при збереженні даних у файл: " + e.getMessage());
        }
    }


    private void addHotelEntry() {
        String hotelName = JOptionPane.showInputDialog("Введіть назву готелю:");
        String hotelAddress = JOptionPane.showInputDialog("Введіть адресу готелю:");
        String hotelPhoneNumber = JOptionPane.showInputDialog("Введіть телефон готелю:");
    
        // Логіка для отримання кількості адміністраторів, кімнат та мешканців
        int numAdmins = Integer.parseInt(JOptionPane.showInputDialog("Введіть кількість адміністраторів:"));
        int numRooms = Integer.parseInt(JOptionPane.showInputDialog("Введіть кількість кімнат:"));
        int numResidents = Integer.parseInt(JOptionPane.showInputDialog("Введіть кількість мешканців:"));
    
        // Створення нового об'єкта готелю
        hotel = new Hotel(hotelName, hotelAddress, hotelPhoneNumber);
    
        // Логіка для додавання адміністраторів
        for (int i = 0; i < numAdmins; i++) {
            String adminName = JOptionPane.showInputDialog("Введіть ім'я адміністратора " + (i + 1) + ":");
            Administrator admin = new Administrator(adminName);
            hotel.addAdministrator(admin);
        }
    
        // Логіка для додавання кімнат
        for (int i = 0; i < numRooms; i++) {
            String roomType = JOptionPane.showInputDialog("Введіть тип кімнати " + (i + 1) + ":");
            int roomId = Integer.parseInt(JOptionPane.showInputDialog("Введіть номер кімнати " + (i + 1) + ":"));
            Room room = new Room(roomType, roomId);
            hotel.addRoom(room);
        }
    
        // Логіка для додавання мешканців
        for (int i = 0; i < numResidents; i++) {
            String residentName = JOptionPane.showInputDialog("Введіть ім'я мешканця " + (i + 1) + ":");
            int roomNumber = Integer.parseInt(JOptionPane.showInputDialog("Введіть номер кімнати для мешканця " + (i + 1) + ":"));
            int stayDuration = Integer.parseInt(JOptionPane.showInputDialog("Введіть термін проживання для мешканця " + (i + 1) + ":"));
    
            Room residentRoom = hotel.getRoomByNumber(roomNumber);
            Resident resident = new Resident(residentName, residentRoom, stayDuration);
            hotel.addResident(resident);
        }
    
        // Збереження даних у файл
        saveDataToFile("hotel_data.txt");
    
        JOptionPane.showMessageDialog(null, "Запис про готель додано та збережено у файл успішно!");
    }
    
    private void addResidentEntry() {
        String residentName = JOptionPane.showInputDialog("Введіть ім'я мешканця:");
        String roomNumberInput = JOptionPane.showInputDialog("Введіть номер кімнати:");

        try {
            int roomNumber = Integer.parseInt(roomNumberInput);

            Room residentRoom = hotel.getRoomByNumber(roomNumber);
            if (residentRoom != null) {
                int stayDuration = Integer.parseInt(JOptionPane.showInputDialog("Введіть термін проживання (в днях):"));
                Resident resident = new Resident(residentName, residentRoom, stayDuration);
                hotel.addResident(resident);
                JOptionPane.showMessageDialog(null, "Інформацію про мешканця додано успішно!");
            } else {
                JOptionPane.showMessageDialog(null, "Кімнату з номером " + roomNumber + " не знайдено.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Неправильний формат номеру кімнати або терміну проживання.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new HotelManagementApp();
            }
        });
    }
}
