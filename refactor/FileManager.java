package refactor;

import java.io.*;
import java.util.Scanner;

public class FileManager {

    public static Hotel loadHotelFromFile(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);

        String hotelName = scanner.nextLine().trim();
        String hotelAddress = scanner.nextLine().trim();
        String hotelPhoneNumber = scanner.nextLine().trim();

        Hotel hotel = new Hotel(hotelName, hotelAddress, hotelPhoneNumber);

        int numAdmins = Integer.parseInt(scanner.nextLine().trim());
        for (int i = 0; i < numAdmins; i++) {
            String adminName = scanner.nextLine().trim();
            hotel.addAdministrator(new Administrator(adminName));
        }

        int numRooms = Integer.parseInt(scanner.nextLine().trim());
        for (int i = 0; i < numRooms; i++) {
            String roomType = scanner.nextLine().trim();
            int roomId = Integer.parseInt(scanner.nextLine().trim());
            hotel.addRoom(new Room(roomType, roomId));
        }

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
        return hotel;
    }

    public static void saveHotelToFile(Hotel hotel, String fileName) throws IOException {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(hotel.getName() + "\n");
            writer.write(hotel.getAddress() + "\n");
            writer.write(hotel.getPhoneNumber() + "\n");

            writer.write(hotel.getAdministrators().size() + "\n");
            for (Administrator admin : hotel.getAdministrators()) {
                writer.write(admin.getName() + "\n");
            }

            writer.write(hotel.getRooms().size() + "\n");
            for (Room room : hotel.getRooms()) {
                writer.write(room.getType() + "\n");
                writer.write(room.getNumber() + "\n");
            }

            writer.write(hotel.getResidents().size() + "\n");
            for (Resident resident : hotel.getResidents()) {
                writer.write(resident.getName() + "\n");
                writer.write(resident.getRoom().getNumber() + "\n");
                writer.write(resident.getStayDuration() + "\n");
            }
        }
    }
}
