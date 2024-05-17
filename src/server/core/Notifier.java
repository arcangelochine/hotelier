package server.core;

import server.database.Database;
import server.database.Hotel;
import server.util.Logger;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Notifier implements Runnable {
    private static final int BUF_LENGTH = 1024;

    private static final Logger logger = Logger.getInstance();
    private static final RankManager rankManager = RankManager.getInstance();
    private static final Database database = Database.getInstance();
    private static final HashMap<String, Hotel> topHotels = new HashMap<>();

    private static byte[] buffer = new byte[BUF_LENGTH];
    private static InetAddress address;
    private static final int port = 4000;
    private static MulticastSocket socket;

    public static void setup() {
        try {
            address = InetAddress.getByName("224.0.0.0");
            socket = new MulticastSocket(port);
            socket.joinGroup(address);
            logger.out("Notifier ready to go!");
        } catch (Exception e) {
            logger.err("Could not setup Notifier: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        topHotels.putAll(getTopHotels());
        rankManager.update();

        for (Map.Entry<String, Hotel> entry : getTopHotels().entrySet()) {
            String city = entry.getKey();
            Hotel hotel = entry.getValue();

            if (!hotel.getId().equals(topHotels.get(city).getId()))
                sendNotify(city, hotel.getName());
        }
    }

    private HashMap<String, Hotel> getTopHotels() {
        return (HashMap<String, Hotel>) database.getHotels().entrySet().stream()
                .filter(entry -> entry.getValue() != null && !entry.getValue().isEmpty())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().get(0)
                ));
    }

    private void sendNotify(String city, String hotelName) {
        if (socket == null)
            return;

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            DataOutputStream dataStream = new DataOutputStream(outputStream);
            DatagramPacket packet = new DatagramPacket(buffer, BUF_LENGTH, address, port);

            dataStream.writeUTF(hotelName + " is now the best hotel in " + city + "!");

            byte[] bytes = outputStream.toByteArray();
            packet.setData(bytes);
            packet.setLength(bytes.length);

            socket.send(packet);
            outputStream.reset();
            logger.out("Notify sent");
        } catch (Exception e) {
            logger.err("Could not send notify: " + e.getMessage());
        }
    }
}
