package ru.mk.Statistic.Application.Service;

import ru.mk.Statistic.Application.Service.ServerSocket.ServerSocketWorker;
import ru.mk.Statistic.Application.Service.ServerSocket.ServerSocketWorkerJob;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.SynchronousQueue;

public class StatisticSocketService implements Runnable{

    private final ServerSocket serverSocket;

    private final ServerSocketWorker[] socketWorkers;

    private final Queue<ServerSocketWorkerJob> jobsQueue;

    private int completeJobs = 0;

    public StatisticSocketService(int port, short workersPoolSize) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.socketWorkers = new ServerSocketWorker[workersPoolSize];
        this.jobsQueue = new ConcurrentLinkedQueue<>();

        for (short i = 0; i< workersPoolSize; i++) {
            this.socketWorkers[i] = new ServerSocketWorker(i, this.jobsQueue);
            this.socketWorkers[i].start();
        }
    }

    @Override
    public void run() {
        System.out.println("Server started");
        while (true) {
            try {
                Socket socket = this.serverSocket.accept();
                this.jobsQueue.add(new ServerSocketWorkerJob(socket));
                completeJobs++;

                if (completeJobs > 3) {
                    break;
                }

            } catch (IOException e) {
                Thread.interrupted();
                return;
            }
        }

        System.out.println("Jobs accepting interrupted");
        short runningThreads = (short)this.socketWorkers.length;

        while (!this.jobsQueue.isEmpty()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("All tasks done");

        while (runningThreads > 0) {
            runningThreads = 0;
            for(short i = 0; i < this.socketWorkers.length; i++) {
                System.out.println("State is:" + this.socketWorkers[i].getState());
                if (this.socketWorkers[i] != null && this.socketWorkers[i].getState() == Thread.State.TIMED_WAITING) {
                    System.out.println("State2 is:" + this.socketWorkers[i].getState());
                    this.socketWorkers[i].interrupt();
                    this.socketWorkers[i] = null;
                } else {
                    runningThreads++;
                }
            }

            return;
        }

        try {
            System.out.println("Complete!");
            shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void shutdown() throws IOException{
        serverSocket.close();
    }
}
