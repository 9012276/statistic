package ru.mk.Statistic.Application.Service.ServerSocket;

import java.io.*;
import java.net.Socket;
import java.util.Queue;

public class ServerSocketWorker
        extends Thread
        implements Runnable
{

    private final int workerId;

    private Queue<ServerSocketWorkerJob> queue;

    public ServerSocketWorker(int workerId, Queue<ServerSocketWorkerJob> queue) throws IOException{
        this.queue = queue;
        this.workerId = workerId;
    }

    @Override
    public synchronized void run() {
        while(!Thread.interrupted()) {
            ServerSocketWorkerJob job = this.queue.poll();

            if (job == null) {
                try {
                    System.out.println("Waiting");
                    this.wait(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
                continue;
            }

            doJob(job);
        }

        System.out.println("Thread interrupted");
    }

    private void doJob(ServerSocketWorkerJob job) {
        System.out.println(String.format("Worker %d is starting new job", workerId));
        short transferred = 0;
        Socket socket = job.getSocket();
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            while (transferred < 100) {
                if (input.ready()) {
                    String line = input.readLine().trim();
                    transferred = Short.valueOf(line);
                    System.out.println(String.format("Worker %d received: %d", workerId, transferred));
                    if (transferred == 100) {
                        output.println("Complete");
                    }
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            input.close();
            output.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            Thread.interrupted();
        }
    }
}
