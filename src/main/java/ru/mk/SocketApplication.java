package ru.mk;

import ru.mk.Statistic.Application.Service.SocketClient;
import ru.mk.Statistic.Application.Service.StatisticSocketService;

import java.io.IOException;

import static java.lang.System.exit;

public class SocketApplication {

    final private int SOCKET_PORT = 9091;

    private StatisticSocketService socketService;

    public SocketApplication() throws IOException {
        this.socketService = new StatisticSocketService(SOCKET_PORT, (short) 2);
    }

    public void run() {
        new Thread(this.socketService).start();

        try {
            for (int i = 0; i < 20; i++) {
                new Thread(new SocketClient("127.0.0.1", SOCKET_PORT, i)).start();

                if (i == 10) {
                    System.out.println("Wating for second part");
                    Thread.sleep(2000);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not start socket client");
            exit(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            new SocketApplication().run();
        } catch (IOException e) {
            System.out.println("Could not start socket server service");
            e.printStackTrace();
            exit(1);
        }
    }
}


