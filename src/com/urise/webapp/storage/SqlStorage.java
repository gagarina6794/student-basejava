package com.urise.webapp.storage;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.ContactType;
import com.urise.webapp.model.Resume;
import com.urise.webapp.sql.SqlHelper;

import java.sql.*;
import java.util.*;


public class SqlStorage implements Storage {
    SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        sqlHelper = new SqlHelper(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void update(Resume resume) {
        sqlHelper.transactionExecute(connection -> {
            try (PreparedStatement ps = connection.prepareStatement("UPDATE resume SET full_name = ? WHERE uuid = ?")) {
                ps.setString(2, resume.getUuid());
                ps.setString(1, resume.getFullName());
                executeUpdateWithCheck(ps,resume.getUuid());
            }
            try (PreparedStatement ps = connection.prepareStatement("DELETE FROMcontact WHERE resume_uuid = ?")){
                ps.setString(1, resume.getUuid());
            }
            insertInContact(resume,connection);
            return null;
        });
    }

    @Override
    public void clear() {
        sqlHelper.executeWithException("DELETE FROM resume");
    }

    @Override
    public void save(Resume resume) {
        sqlHelper.transactionExecute(connection -> {
            sqlHelper.executeWithException("INSERT INTO resume (uuid, full_name) VALUES (?,?)", ps -> {
                ps.setString(1, resume.getUuid());
                ps.setString(2, resume.getFullName());
                ps.execute();
                return null;
            });
            insertInContact(resume, connection);
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.executeWithException("SELECT * FROM resume r LEFT JOIN contact c ON r.uuid = c.resume_uuid WHERE r.uuid =?", ps -> {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            Resume resume = new Resume(uuid, rs.getString("full_name"));
            do {
                String value = rs.getString("value");
                String type = rs.getString("type");
                if (value != null && type != null) {
                    resume.addContacts(ContactType.valueOf(type), value);
                }
            } while (rs.next());

            return resume;
        });
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.transactionExecute(connection -> {
            try(PreparedStatement ps = connection.prepareStatement("DELETE FROM resume WHERE uuid = ?")){
                ps.setString(1, uuid);
                executeUpdateWithCheck(ps,uuid);
                return null;
            }
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.transactionExecute(connection -> {
            List<Resume> resumesList = new ArrayList<>();
            Map<String, Resume> resumesMap = new HashMap<>();
            try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM resume r LEFT JOIN contact c ON r.uuid = c.resume_uuid ORDER BY r.full_name,r.uuid")) {
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    throw new StorageException("storage is empty");
                }
                do {
                    String uuid = rs.getString("uuid");
                    if (!resumesMap.containsKey(uuid)) {
                        resumesMap.put(uuid, new Resume(uuid, rs.getString("full_name")));
                        resumesList.add(resumesMap.get(uuid));
                    }
                    String type = rs.getString("type");
                    String value = rs.getString("value");
                    if (type != null && value != null) {
                        resumesMap.get(uuid).getContacts().put(ContactType.valueOf(type), value);
                    }
                } while (rs.next());

            }
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

    private void insertInContact(Resume resume, Connection connection) throws SQLException {
        if (resume.getContacts().size() != 0) {
            try (PreparedStatement ps = connection.prepareStatement("INSERT INTO contact (resume_uuid,type,value) VALUES (?,?,?)"))
            {
                for (Map.Entry<ContactType, String> e : resume.getContacts().entrySet()) {
                    ps.setString(1, resume.getUuid());
                    ps.setString(2, e.getKey().name());
                    ps.setString(3, e.getValue());
                    ps.addBatch();
                }
                ps.executeBatch();
            }
        }
    }

    private void executeUpdateWithCheck(PreparedStatement ps, String uuid) throws SQLException {
        if (ps.executeUpdate() == 0) {
            throw new NotExistStorageException(uuid);
        }
    }
}

