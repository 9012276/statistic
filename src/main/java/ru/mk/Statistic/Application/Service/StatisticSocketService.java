package ru.mk.Statistic.Application.Service;

import ru.mk.Statistic.Application.Service.ServerSocket.ServerSocketWorker;
import ru.mk.Statistic.Application.Service.ServerSocket.ServerSocketWorkerJob;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class StatisticSocketService extends Thread{

    private final ServerSocket serverSocket;

    private final ServerSocketWorker[] socketWorkers;

    private final Queue<ServerSocketWorkerJob> jobsQueue;

    private int completeJobs = 0;

    public StatisticSocketService(int port, short workersPoolSize) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.socketWorkers = new ServerSocketWorker[workersPoolSize];
        this.jobsQueue = new ConcurrentLinkedQueue<>();

        for (short i = 0; i < workersPoolSize; i++) {
            this.socketWorkers[i] = new ServerSocketWorker(i, this.jobsQueue);
            this.socketWorkers[i].start();
        }
    }

    @Override
    public void run() {
        System.out.println("Server started");

        while (!Thread.interrupted()) {
            try {
                Socket socket = this.serverSocket.accept();
                this.jobsQueue.add(new ServerSocketWorkerJob(socket));
            } catch (IOException e) {
                Thread.interrupted();
                return;
            }
        }
    }

    public void shutdown() throws IOException{
        System.out.println("Jobs accepting interrupted");
        short runningThreads = (short)this.socketWorkers.length;

        while (runningThreads > 0) {
            if (!this.jobsQueue.isEmpty()) {
                try {
                    Thread.sleep(100);
                    continue;
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());;
                    e.printStackTrace();
                }
            }

            runningThreads = 0;
            for(short i = 0; i < this.socketWorkers.length; i++) {
                System.out.println("State is:" + this.socketWorkers[i].getState());
                if (
                    this.socketWorkers[i] != null
                    && (
                        this.socketWorkers[i].getState() == Thread.State.TIMED_WAITING
                        || this.socketWorkers[i].getState() == State.TERMINATED
                    )
                ) {
                    this.socketWorkers[i].interrupt();
                } else {
                    runningThreads++;
                }
            }
        }

        System.out.println("Complete!");
        serverSocket.close();
    }
}
