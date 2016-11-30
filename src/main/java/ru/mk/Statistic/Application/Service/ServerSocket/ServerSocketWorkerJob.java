package ru.mk.Statistic.Application.Service.ServerSocket;

import java.net.Socket;

public class ServerSocketWorkerJob {

    private final Socket socket;

    public ServerSocketWorkerJob(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }
}
