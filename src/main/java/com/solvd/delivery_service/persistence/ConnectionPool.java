package com.solvd.delivery_service.persistence;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ConnectionPool {
    private static volatile ConnectionPool instance;
    private final Queue<Connection> connections = new ConcurrentLinkedQueue<>();
    private final int poolSize = 1;

    private ConnectionPool() {
        Properties property = new Properties();
        Connection connection;
        try (FileInputStream fis = new FileInputStream("src/main/resources/config.properties")) {
            property.load(fis);
            Class.forName(property.getProperty("db.driver"));
            connection = DriverManager.getConnection(
                    property.getProperty("db.url"),
                    property.getProperty("db.user"),
                    property.getProperty("db.password"));
        } catch (IOException e) {
            throw new RuntimeException("You have been problem with reading from property file!", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("You have been problem with JDBC driver!", e);
        } catch (SQLException e) {
            throw new RuntimeException("You have been problem with connecting to your database!", e);
        }
        for (int i = 0; i <= poolSize; i++) {
            connections.add(connection);
        }
    }

    public static synchronized ConnectionPool getInstance() {
        if (instance == null) {
            instance = new ConnectionPool();
        }
        return instance;
    }

    public synchronized Connection getConnection() {
        while (connections.size() == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return connections.remove();
    }

    public synchronized void releaseConnection(Connection connection) {
        if (connections.size() <= poolSize) {
            connections.add(connection);
        }
        notify();
    }
}