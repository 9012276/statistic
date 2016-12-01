package ru.mk;

import ru.mk.Statistic.Application.Service.ServerSocket.ShutdownListener;
import ru.mk.Statistic.Application.Service.SocketClient;
import ru.mk.Statistic.Application.Service.StatisticSocketService;

import java.io.IOException;
import java.net.ConnectException;

import static java.lang.System.exit;

public class SocketApplication {

    final private int SOCKET_PORT = 9091;

    private StatisticSocketService socketService;

    public static void main(String[] args) {
        new SocketApplication().run();
    }

    public void run() {
        try {
            this.socketService = new StatisticSocketService(SOCKET_PORT, (short) 2);
            this.socketService.start();

            Runtime.getRuntime().addShutdownHook(new ShutdownListener(this));

            for (int i = 0; i < 2; i++) {
                try{
                    new Thread(new SocketClient("127.0.0.1", SOCKET_PORT, i)).start();
                } catch (ConnectException e) {
                    System.out.println("Server has gone away");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not start socket client");
            exit(2);
        }
    }

    public void shutdown() {
        try {
            this.socketService.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}


