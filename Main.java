import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame parentFrame = new JFrame("AirBus Pro");
        JPanel bookingPanel = new JPanel();
        Booking bookingBoard = new Booking();

        bookingPanel.setLayout(new GridLayout(1, 2));

        bookingPanel.add(new SeatMap(bookingBoard));
        bookingPanel.add(bookingBoard);

        parentFrame.setLayout(new BorderLayout());
        parentFrame.add(renderTitleSection(), BorderLayout.NORTH);
        parentFrame.add(bookingPanel, BorderLayout.CENTER);

        parentFrame.setSize(1280, 720);
        parentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        parentFrame.setVisible(true);
    }

    public static JPanel renderTitleSection() {
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 0));
        JLabel titleLabel = new JLabel("AirBus Pro");
        titleLabel.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 24));
        JLabel subtitleLabel = new JLabel("Book your Bus Seats with Ease");
        subtitleLabel.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));
        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);
        return titlePanel;
    }
}

class SeatMap extends JPanel {

    public SeatMap(Booking booking) {
        this.setLayout(new GridLayout(5, 2, 30, 30));
        this.setBorder(BorderFactory.createEmptyBorder(0, 30, 30, 30));

        Seat s1 = new Seat("V1", 0, 0, Seat.Category.VIP);
        Seat s2 = new Seat("V2", 0, 1, Seat.Category.VIP);
        Seat s3 = new Seat("V3", 1, 0, Seat.Category.VIP);
        Seat s4 = new Seat("V4", 1, 1, Seat.Category.VIP);
        Seat s5 = new Seat("E1", 2, 0, Seat.Category.ECONOMY);
        Seat s6 = new Seat("E2", 2, 1, Seat.Category.ECONOMY);
        Seat s7 = new Seat("E3", 3, 0, Seat.Category.ECONOMY);
        Seat s8 = new Seat("E4", 3, 1, Seat.Category.ECONOMY);
        Seat s9 = new Seat("E5", 4, 0, Seat.Category.ECONOMY);
        Seat s10 = new Seat("E6", 4, 1, Seat.Category.ECONOMY);

        s1.addActionListener(e -> booking.setSeat(s1));
        s2.addActionListener(e -> booking.setSeat(s2));
        s3.addActionListener(e -> booking.setSeat(s3));
        s4.addActionListener(e -> booking.setSeat(s4));
        s5.addActionListener(e -> booking.setSeat(s5));
        s6.addActionListener(e -> booking.setSeat(s6));
        s7.addActionListener(e -> booking.setSeat(s7));
        s8.addActionListener(e -> booking.setSeat(s8));
        s9.addActionListener(e -> booking.setSeat(s9));
        s10.addActionListener(e -> booking.setSeat(s10));

        this.add(s1);
        this.add(s2);
        this.add(s3);
        this.add(s4);
        this.add(s5);
        this.add(s6);
        this.add(s7);
        this.add(s8);
        this.add(s9);
        this.add(s10);
    }
}

class Seat extends JButton {
    private int row;
    private int column;
    private String seatNumber;
    private boolean isBooked;
    private Category category;
    private int price;

    enum Category {
        ECONOMY, VIP,
    }

    public Seat(String seatNumber, int row, int column, Category category) {
        this.row = row;
        this.column = column;
        this.seatNumber = seatNumber;
        this.category = category;
        this.isBooked = false;
        this.setText("Seat " + seatNumber);
    }

    public void markAsBooked() {
        if (!isBooked) {
            isBooked = true;
            this.setText("Booked");
            this.setEnabled(false);
        }
    }

    public String getSeatNumber() {
        return seatNumber;
    }

}

class Booking extends JPanel {
    private Seat seat;
    private String name;
    private String phoneNumber;
    private JLabel bookingLabel;
    private JPanel nameRegistryPanel;
    private JLabel nameLabel;
    private JTextField nameField;
    private JLabel phoneNumberLabel;
    private JTextField phoneNumberField;
    private JLabel seatLabel;
    private JLabel seatField;
    private JButton confirmButton;
    private JButton viewBookingButton;
    private JPanel buttonPanel;

    public Booking() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(0, 30, 30, 30));

        bookingLabel = new JLabel("Booking Details: ");
        nameRegistryPanel = new JPanel();
        nameLabel = new JLabel("Passenger Name: ");
        nameField = new JTextField(10);
        phoneNumberLabel = new JLabel("Phone Number: ");
        phoneNumberField = new JTextField(10);
        seatLabel = new JLabel("Seat Number: ");
        seatField = new JLabel("Not Selected");
        confirmButton = new JButton("Confirm");
        viewBookingButton = new JButton("View Booking");
        buttonPanel = new JPanel();

        bookingLabel.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 18));
        nameRegistryPanel.setLayout(new GridLayout(4, 2, 30, 30));

        confirmButton.addActionListener(e -> {
            if (seat == null) {
                JOptionPane.showMessageDialog(this, "Please select a seat first.");
                return;
            }
            if (nameField.getText().isEmpty() || phoneNumberField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.");
                return;
            }
            int result = JOptionPane.showConfirmDialog(this, "Confirm booking for seat " + seat.getSeatNumber() + "?");
            if (result == JOptionPane.YES_OPTION) {
                this.name = nameField.getText();
                this.phoneNumber = phoneNumberField.getText();
                BookingInformation.addBooking(this.name, this.phoneNumber, this.seat);
                seat.markAsBooked();
                seatField.setText("Not Selected");
                seat = null;
                JOptionPane.showMessageDialog(this, "Booking Confirmed!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        viewBookingButton.addActionListener(e -> {
            JDialog bookingDialog = new JDialog();

            bookingDialog.setTitle("Current Bookings");
            bookingDialog.setSize(600, 600);
            bookingDialog.setLocationRelativeTo(null);
            bookingDialog.setModal(true);
            bookingDialog.setVisible(true);

            String[] columns = {"#", "Name", "Phone", "Seat"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);

            this.add(bookingDialog);
        });

        nameRegistryPanel.add(nameLabel);
        nameRegistryPanel.add(nameField);
        nameRegistryPanel.add(phoneNumberLabel);
        nameRegistryPanel.add(phoneNumberField);
        nameRegistryPanel.add(seatLabel);
        nameRegistryPanel.add(seatField);
        buttonPanel.add(confirmButton);
        buttonPanel.add(viewBookingButton);
        this.add(bookingLabel);
        this.add(nameRegistryPanel);
        this.add(buttonPanel);

    }

    public void setSeat(Seat seat) {
        this.seat = seat;
        seatField.setText(seat.getSeatNumber());
    }
}

class BookingRecord {
    public String name;
    public String phoneNumber;
    public Seat seat;

    public BookingRecord(String name, String phoneNumber, Seat seat) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.seat = seat;
    }
}

class BookingInformation {
    static public ArrayList<BookingRecord> bookings = new ArrayList<>();

    public static void addBooking(String name, String phoneNumber, Seat seat) {
        bookings.add(new BookingRecord(name, phoneNumber, seat));
    }
}