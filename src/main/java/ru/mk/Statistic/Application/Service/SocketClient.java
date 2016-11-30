package ru.mk.Statistic.Application.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

public class SocketClient implements Runnable{

    private final Socket socket;

    private final BufferedReader input;

    private final PrintWriter output;

    private final int clientId;

    final static private int targetNumber = 100;

    private short transfered = 0;

    final static private short CHUNK_SIZE = 10;

    public SocketClient(String address, int port, int clientId) throws IOException {
        this.socket = new Socket(address, port);
        this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.output = new PrintWriter(socket.getOutputStream(), true);
        this.clientId = clientId;
    }

    @Override
    public void run() {
        try {
            String line = "";

            while (line.toLowerCase().indexOf("complete") == -1) {
                short transfered = (short)(this.transfered + SocketClient.CHUNK_SIZE);
                output.println(transfered);
                this.transfered = transfered;

                if (input.ready()) {
                    line = input.readLine();
                }
            }
        } catch(IOException e) {
            Thread.interrupted();
        }
    }

}
