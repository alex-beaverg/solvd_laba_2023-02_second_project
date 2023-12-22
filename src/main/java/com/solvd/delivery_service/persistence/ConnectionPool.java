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
    private final int poolSize;
    private final Queue<Connection> connections = new ConcurrentLinkedQueue<>();

    private ConnectionPool(int poolSize) {
        this.poolSize = poolSize;
        Properties property = new Properties();
        for (int i = 0; i < this.poolSize; i++) {
            Connection connection;
            try (FileInputStream fis = new FileInputStream("src/main/resources/config.properties")) {
                property.load(fis);
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(
                        property.getProperty("db.url"),
                        property.getProperty("db.user"),
                        property.getProperty("db.password")
                );
            } catch (IOException e) {
                throw new RuntimeException("You have been problem with reading from property file!", e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("You have been problem with JDBC driver!", e);
            } catch (SQLException e) {
                throw new RuntimeException("You have been problem with connecting to your database!", e);
            }
            connections.add(connection);
        }
    }

    public static synchronized ConnectionPool getInstance(int poolSize) {
        if (instance == null) {
            instance = new ConnectionPool(poolSize);
        }
        return instance;
    }

    public static synchronized ConnectionPool getInstance() {
        if (instance == null) {
            instance = new ConnectionPool(1);
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
        if (connections.size() < this.poolSize) {
            connections.add(connection);
        }
        notify();
    }

    public synchronized int getNumberOfAvailableConnections() {
        return connections.size();
    }

    public synchronized int getPoolSize() {
        return this.poolSize;
    }
}
