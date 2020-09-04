package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import com.urise.webapp.sql.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

interface Executable<T> {
    T executeQuery(PreparedStatement ps) throws SQLException;
}

public class SqlStorage implements Storage {
    public final ConnectionFactory connectionFactory;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void update(Resume resume) {
        try {
            new ExecutionQuery<Object>().executeWithExceptionForResultSet(connectionFactory,new NotExistStorageException("this resume doesn't exist"),
                    "update resume set full_name = ? where uuid = ?", ps -> {
                ps.setString(2, resume.getUuid());
                ps.setString(1, resume.getFullName());
                ps.execute();
                return null;
            });
        } catch (Exception e) {
            throw new NotExistStorageException(e.getMessage());
        }
    }

    @Override
    public void clear() {
        try {
            new ExecutionQuery<Object>().executeWithExceptionForResultSet(connectionFactory,new SQLException("smth wrong with connection"), "DELETE FROM resume", ps -> {
                ps.execute();
                return null;
            });
        } catch (Exception e) {
            throw new StorageException(e);
        }
    }

    @Override
    public void save(Resume resume) {
        try {
            new ExecutionQuery<Object>().executeWithExceptionForResultSet(connectionFactory, new ExistStorageException("this resume already exists in storage"),
                    "INSERT INTO resume (uuid, full_name) VALUES (?,?)",
                    ps -> {
                        ps.setString(1, resume.getUuid());
                        ps.setString(2, resume.getFullName());
                        ps.execute();
                        return null;
                    });
        } catch (Exception e) {
            throw new ExistStorageException(e.getMessage());
        }
    }

    @Override
    public Resume get(String uuid) {
        try {
            return new ExecutionQuery<Resume>().executeWithExceptionForResultSet(connectionFactory,new NotExistStorageException("this resume doesn't exist in storage"),
                    "SELECT * FROM resume r WHERE r.uuid =?", ps -> {
                        ps.setString(1, uuid);
                        ResultSet rs = ps.executeQuery();
                        if (! rs.next()) {
                            throw new NotExistStorageException(uuid);
                        }
                        return new Resume(uuid, rs.getString("full_name"));
                    });
        } catch (Exception e) {
            throw new NotExistStorageException(e.getMessage());
        }

    }

    @Override
    public void delete(String uuid) {
        try {
            new ExecutionQuery<Object>().executeWithExceptionForResultSet(connectionFactory,new NotExistStorageException("this resume doesn't exist"),
                    "delete from resume where uuid = ?", ps -> {
                ps.setString(1, uuid);
                ps.execute();
                return null;
            });
        } catch (Exception e) {
            throw new NotExistStorageException(e.getMessage());
        }
    }

    @Override
    public List<Resume> getAllSorted() {
        try {
            return new ExecutionQuery<List<Resume>>().executeWithExceptionForResultSet(connectionFactory, new StorageException("storage is empty"),
                    "SELECT * FROM resume ORDER BY full_name",
                    ps -> {
                        ResultSet rs = ps.executeQuery();
                        if (! rs.next()) {
                            new StorageException("storage is empty");
                        }
                        List<Resume> resumesList = new ArrayList<>();
                        do{
                            resumesList.add(new Resume(rs.getString("uuid").trim(), rs.getString( "full_name")));
                        }while (rs.next());
                        return resumesList;
                    });
        } catch (Exception e) {
            throw new StorageException(e);
        }
    }

    @Override
    public int size() {
        try {
            return new ExecutionQuery<Integer>().executeWithExceptionForResultSet(connectionFactory, new StorageException("storage is empty"),
                    "SELECT COUNT(*) FROM resume",
                    ps -> {
                        ResultSet rs = ps.executeQuery();
                        if (! rs.next()) {
                            new StorageException("storage is empty");
                        }
                        return rs.getInt(1);
                    });
        } catch (Exception e) {
            throw new StorageException(e);
        }
    }

    private boolean isExecuted(ResultSet rs, String uuid) throws SQLException {
        return rs.next();
    }
}

class ExecutionQuery<T> {
    public T executeWithExceptionForResultSet(ConnectionFactory connectionFactory, Exception exception, String query, Executable<T> executable) throws Exception {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            return executable.executeQuery(ps);
        } catch (SQLException e) {
            throw exception;
        }
    }
}