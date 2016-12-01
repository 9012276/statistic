package ru.mk.Statistic.Application.Service.ServerSocket;


import ru.mk.SocketApplication;

public class ShutdownListener extends Thread{

    private SocketApplication app;

    public ShutdownListener(SocketApplication app) {
        this.app = app;
    }

    @Override
    public void run() {
        System.out.println("INTERRUPT");
        this.app.shutdown();
    }
}
