package com.urise.webapp.sql;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper {
    public final ConnectionFactory connectionFactory;

    public SqlHelper(String dbUrl, String dbUser, String dbPassword) {
        connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    public void executeWithException(String query) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.execute();
        } catch (Exception e) {
            initException(e);
        }
    }

    public <T> T executeWithException(String query, QueryExecutable<T> executable) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            return executable.executeQuery(ps);
        } catch (Exception e) {
            initException(e);
        }
        return null;
    }

    void initException(Exception e) {
        if (e instanceof SQLException) {
            if (((SQLException) e).getSQLState().equals("23505")) {
                throw new ExistStorageException(null);
            }
        }
        if (e instanceof NotExistStorageException)//костыль
            throw new NotExistStorageException(null);
        throw new StorageException(e);
    }
}
