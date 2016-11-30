package ru.mk;

import ru.mk.Statistic.Application.Service.SocketClient;
import ru.mk.Statistic.Application.Service.StatisticSocketService;

import java.io.IOException;

import static java.lang.System.exit;

public class SocketApplication {

    final private int SOCKET_PORT = 9091;

    private StatisticSocketService socketService;

    public SocketApplication() throws IOException {
        this.socketService = new StatisticSocketService(SOCKET_PORT);
    }

    public void run() {
        this.socketService.run();

        try {
            for (int i = 0; i < 10; i++) {
                new Thread(new SocketClient("127.0.0.1", SOCKET_PORT, i));
            }
        } catch (IOException e) {
            System.out.println("Could not start socket client");
            exit(2);
        }
    }

    public static void main(String[] args) {
        try {
            new SocketApplication().run();
        } catch (IOException e) {
            System.out.println("Could not start socket server service");
            exit(1);
        }
    }
}


