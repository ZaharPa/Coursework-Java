package refactor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class HotelManagementApp {

    private Hotel hotel;

    public HotelManagementApp() {
        // Зчитування даних з файлу
        loadHotelData();

        // Створення графічного інтерфейсу
        createAndShowGUI();
    }

    private void loadHotelData() {
        try {
            hotel = FileManager.loadHotelFromFile("hotel_data.txt");
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Файл не знайдено: hotel_data.txt");
            hotel = new Hotel("Default Hotel", "Default Address", "000-000-0000"); // Значення за замовчуванням
        }
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Hotel Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 1)); // Змінено для додаткових кнопок

        panel.add(createButton("Кількість адміністраторів", e -> showCountAdministrators(frame)));
        panel.add(createButton("Кількість мешканців", e -> showCountResidents(frame)));
        panel.add(createButton("Кількість типів зайнятих номерів", e -> showCountOccupiedRoomTypes(frame)));
        panel.add(createButton("Список мешканців обраного типу номеру", e -> showResidentsByRoomType(frame)));
        panel.add(createButton("Найбільший термін проживання", e -> showLongestStay(frame)));
        panel.add(createButton("Тип номеру з найбільшим попитом", e -> showMostDemandedType(frame)));
        panel.add(createButton("Зберегти дані у файл", e -> saveHotelData(frame)));
        panel.add(createButton("Додати запис про готель", e -> addHotelEntry(frame)));

        frame.add(panel);
        frame.setVisible(true);
    }

    private JButton createButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.addActionListener(actionListener);
        return button;
    }

    private void showCountAdministrators(JFrame frame) {
        int count = hotel.countAdministrators();
        JOptionPane.showMessageDialog(frame, "Кількість адміністраторів: " + count);
    }

    private void showCountResidents(JFrame frame) {
        int count = hotel.countResidents();
        JOptionPane.showMessageDialog(frame, "Кількість мешканців: " + count);
    }

    private void showCountOccupiedRoomTypes(JFrame frame) {
        int count = hotel.countOccupiedRoomTypes();
        JOptionPane.showMessageDialog(frame, "Кількість типів зайнятих номерів: " + count);
    }

    private void showResidentsByRoomType(JFrame frame) {
        String roomType = JOptionPane.showInputDialog(frame, "Введіть тип номеру:");
        if (roomType != null) {
            List<String> residents = hotel.getResidentsByRoomType(roomType);
            String message = "Мешканці номерів типу " + roomType + ":\n" + String.join("\n", residents);
            JOptionPane.showMessageDialog(frame, message);
        }
    }

    private void showLongestStay(JFrame frame) {
        Resident resident = hotel.getLongestStayResident();
        if (resident != null) {
            JOptionPane.showMessageDialog(frame, "Найбільший термін проживання: " + resident.getName() + " (" + resident.getStayDuration() + " днів)");
        } else {
            JOptionPane.showMessageDialog(frame, "Немає мешканців");
        }
    }

    private void showMostDemandedType(JFrame frame) {
        String roomType = hotel.getMostDemandedRoomType();
        if (roomType != null) {
            JOptionPane.showMessageDialog(frame, "Тип номеру з найбільшим попитом: " + roomType);
        } else {
            JOptionPane.showMessageDialog(frame, "Немає мешканців");
        }
    }

    private void saveHotelData(JFrame frame) {
        try {
            FileManager.saveHotelToFile(hotel, "hotel_data.txt");
            JOptionPane.showMessageDialog(frame, "Дані збережено у файл");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Помилка при збереженні даних у файл: " + e.getMessage());
        }
    }

    private void addHotelEntry(JFrame frame) {
        String hotelName = JOptionPane.showInputDialog("Введіть назву готелю:");
        String hotelAddress = JOptionPane.showInputDialog("Введіть адресу готелю:");
        String hotelPhoneNumber = JOptionPane.showInputDialog("Введіть телефон готелю:");

        hotel = new Hotel(hotelName, hotelAddress, hotelPhoneNumber);
        JOptionPane.showMessageDialog(frame, "Запис про готель додано!");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(HotelManagementApp::new);
    }
}
