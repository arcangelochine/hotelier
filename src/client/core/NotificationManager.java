package client.core;

import client.gui.Notification;
import client.util.Logger;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class NotificationManager implements Runnable {
    private static final int BUF_LENGTH = 1024;

    private static final Logger logger = Logger.getInstance();

    private static byte[] buffer = new byte[BUF_LENGTH];
    private static InetAddress address;
    private static final int port = 4000;
    private static MulticastSocket socket;

    public static void setup() {
        try {
            address = InetAddress.getByName("224.0.0.0");
            socket = new MulticastSocket(port);
            socket.joinGroup(address);
            logger.out("NotificationManager ready to go!");
        } catch (Exception e) {
            logger.err("Could not setup NotificationManager: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        while (true) {
            listen();
        }
    }

    private void listen() {
        try {
            DatagramPacket packet = new DatagramPacket(buffer, BUF_LENGTH);
            socket.receive(packet);

            ByteArrayInputStream byteStream = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
            DataInputStream dataStream = new DataInputStream(byteStream);

            String message = dataStream.readUTF();

            Notification notification = new Notification(message);
            notification.setVisible(true);
        } catch (Exception e) {
            logger.err("Error on receiving datagram: " + e.getClass());
        }
    }
}
