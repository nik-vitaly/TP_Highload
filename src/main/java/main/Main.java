package main;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.cli.*;
import server.Server;

public class Main {

    private static Options options = new Options();

    static {
        options.addOption(new Option("r", true, "Root directory"));
        options.addOption(new Option("p", true, "Port"));
        options.addOption(new Option("c", true, "CPU"));
    }

    public static void main(String[] args) throws ParseException, IOException {

        //ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();


        CommandLineParser parser = new DefaultParser();

        CommandLine commandLine = parser.parse(options, args);

        String directory = commandLine.getOptionValue("r", "/Users/vitaly/Documents/technopark/3/TP_Highload/www");
        int port = Integer.parseInt(commandLine.getOptionValue("p", "8080"));
        int poolSize = Integer.parseInt(commandLine.getOptionValue("c", "5"));

        ExecutorService executor = Executors.newFixedThreadPool(poolSize);
        Settings.setDirectory(directory);
        System.out.print(Settings.getDirectory());
        Settings.threadCount = new AtomicInteger(0);

        ServerSocket ss = new ServerSocket(port);

            while (Settings.isServerOnWork()) {
                Socket s = ss.accept();
                Server req = new Server(s);
                executor.execute(req);
                //new Thread(new Server(s)).start();
            }

            executor.shutdown();
    }
}
