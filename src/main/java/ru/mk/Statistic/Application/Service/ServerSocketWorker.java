package ru.mk.Statistic.Application.Service;

import java.io.*;
import java.net.Socket;

public class ServerSocketWorker implements Runnable{

    private final Socket socket;

    private final BufferedReader input;

    private final PrintWriter output;

    private final int workerId;

    public ServerSocketWorker(int workerId, Socket socket) throws IOException{
        this.socket = socket;
        this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.output = new PrintWriter(socket.getOutputStream());
        this.workerId = workerId;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String line = input.readLine().trim();
                short transfered = Short.valueOf(line);

                if (transfered == 100) {
                    output.write("Complete");
                    closeConnect();
                }
            } catch (IOException e) {
                output.write(String.format("Worker with id %s could not read from server. Thread stoped", workerId));
                Thread.interrupted();
                return;
            }

        }
    }

    private void closeConnect() throws IOException {
        input.close();
        output.close();
        socket.close();
    }
}
