package ru.mk.Statistic.Application.Service;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class StatisticSocketService implements Runnable{

    private final ServerSocket serverSocket;

    private int lauchedWorkers = 0;

    public StatisticSocketService(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
    }

    @Override
    public void run() {
        System.out.println("Server started");
        while (true) {
            try {
                Socket socket = this.serverSocket.accept();

                new Thread(new ServerSocketWorker(++this.lauchedWorkers, socket)).start();

            } catch (IOException e) {
                Thread.interrupted();
                return;
            }
        }
    }

    private void shutdown() throws IOException{
        serverSocket.close();
    }
}
