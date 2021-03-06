package com.urise.webapp.sql;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.StorageException;
import org.postgresql.util.PSQLException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper {
    public final ConnectionFactory connectionFactory;

    public SqlHelper(String dbUrl, String dbUser, String dbPassword) {
        connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    public <T> T transactionExecute(SqlTransaction<T> executor){
        try(Connection connection = connectionFactory.getConnection()) {
            try{
                connection.setAutoCommit(false);
                T result = executor.execute(connection);
                connection.commit();
                return result;
            }catch (SQLException e){
                connection.rollback();
                initException(e);
            }
        }catch (SQLException e){
            throw new StorageException(e);
        }
        return null;
    }

    public void executeWithException(String query) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.execute();
        } catch (SQLException e) {
            initException(e);
        }
    }

    public <T> T executeWithException(String query, QueryExecutable<T> executable) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            return executable.executeQuery(ps);
        } catch (SQLException e) {
            initException(e);
        }
        return null;
    }

    void initException(SQLException e) {
        if (e instanceof PSQLException) {
            if (e.getSQLState().equals("23505")) {
                throw new ExistStorageException(null);
            }
        }
        throw new StorageException(e);
    }
}
