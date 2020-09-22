package com.urise.webapp.storage;

import com.urise.webapp.ResumeTestData;
import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import com.urise.webapp.sql.ConnectionFactory;
import com.urise.webapp.sql.SqlHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class SqlStorage implements Storage {
    SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        sqlHelper = new SqlHelper(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void update(Resume resume) {
        sqlHelper.executeWithException(
                "update resume set full_name = ? where uuid = ?", ps -> {
                    ps.setString(2, resume.getUuid());
                    ps.setString(1, resume.getFullName());
                    if (ps.executeUpdate() == 0) {
                        throw new NotExistStorageException("this resume doesn't exist");
                    }
                    return null;
                });
    }

    @Override
    public void clear() {
        sqlHelper.executeWithException("DELETE FROM resume");
    }

    @Override
    public void save(Resume resume) {
        sqlHelper.executeWithException("INSERT INTO resume (uuid, full_name) VALUES (?,?)",
                ps -> {
                    ps.setString(1, resume.getUuid());
                    ps.setString(2, resume.getFullName());
                    ps.execute();
                    return null;
                });
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.executeWithException("SELECT * FROM resume r WHERE r.uuid =?", ps -> {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            return new Resume(uuid, rs.getString("full_name"));
        });
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.executeWithException("delete from resume where uuid = ?", ps -> {
            ps.setString(1, uuid);
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException("this resume doesn't exist");
            }
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.executeWithException("SELECT * FROM resume ORDER BY full_name,uuid ",
                ps -> {
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) {
                        throw new StorageException("storage is empty");
                    }
                    List<Resume> resumesList = new ArrayList<>();
                    do {
                        resumesList.add(ResumeTestData.fillResume(rs.getString("uuid"), rs.getString("full_name")));
                    } while (rs.next());
                    return resumesList;
                });
    }

    @Override
    public int size() {
        return sqlHelper.executeWithException("SELECT COUNT(*) FROM resume",
                ps -> {
                    ResultSet rs = ps.executeQuery();
                    rs.next();
                    return rs.getInt(1);
                });
    }
}

