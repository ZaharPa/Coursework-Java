package refactor;

import java.util.*;
import java.util.stream.Collectors;

public class Hotel {
    private String name;
    private String address;
    private String phoneNumber;
    private List<Administrator> administrators = new ArrayList<>();
    private List<Room> rooms = new ArrayList<>();
    private List<Resident> residents = new ArrayList<>();

    public Hotel(String name, String address, String phoneNumber) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public List<Administrator> getAdministrators() {
        return Collections.unmodifiableList(administrators);
    }

    public List<Room> getRooms() {
        return Collections.unmodifiableList(rooms);
    }

    public List<Resident> getResidents() {
        return Collections.unmodifiableList(residents);
    }

    public void addAdministrator(Administrator administrator) {
        administrators.add(administrator);
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public void addResident(Resident resident) {
        residents.add(resident);
    }

    public int countAdministrators() {
        return administrators.size();
    }

    public int countResidents() {
        return residents.size();
    }

    public int countOccupiedRoomTypes() {
        return (int) residents.stream()
                .map(resident -> resident.getRoom().getType())
                .distinct()
                .count();
    }

    public List<String> getResidentsByRoomType(String roomType) {
        return residents.stream()
                .filter(resident -> resident.getRoom().getType().equals(roomType))
                .map(Resident::getName)
                .collect(Collectors.toList());
    }

    public Resident getLongestStayResident() {
        return residents.stream()
                .max(Comparator.comparingInt(Resident::getStayDuration))
                .orElse(null);
    }

    public String getMostDemandedRoomType() {
        return residents.stream()
                .collect(Collectors.groupingBy(resident -> resident.getRoom().getType(), Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    public Room getRoomByNumber(int roomNumber) {
        return rooms.stream()
                .filter(room -> room.getNumber() == roomNumber)
                .findFirst()
                .orElse(null);
    }
}
