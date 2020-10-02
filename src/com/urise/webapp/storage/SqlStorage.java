package com.urise.webapp.storage;

import com.urise.webapp.ResumeTestData;
import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.ContactType;
import com.urise.webapp.model.Resume;
import com.urise.webapp.sql.ConnectionFactory;
import com.urise.webapp.sql.SqlHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;


public class SqlStorage implements Storage {
    SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        sqlHelper = new SqlHelper(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void update(Resume resume) {
        sqlHelper.transactionExecute(connection -> {
            try (PreparedStatement ps = connection.prepareStatement("update resume set full_name = ? where uuid = ?")) {
                ps.setString(2, resume.getUuid());
                ps.setString(1, resume.getFullName());
                if (ps.executeUpdate() == 0) {
                    throw new NotExistStorageException(resume.getUuid() + " resume doesn't exist");
                }
            }

            sqlHelper.executeWithException("delete from contact where resume_uuid = ?", ps -> {
                ps.setString(1, resume.getUuid());
                return null;
            });
            if (resume.getContacts().size() != 0) {
                sqlHelper.executeWithException("INSERT INTO contact (resume_uuid,type,value) VALUES (?,?,?)", ps -> {// не могу сюда зайти
                    for (Map.Entry<ContactType, String> e : resume.getContacts().entrySet()) {
                        ps.setString(1, resume.getUuid());
                        ps.setString(2, e.getKey().name());
                        ps.setString(3, e.getValue());
                        ps.addBatch();
                    }
                    ps.executeBatch();
                    return null;
                });
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
        sqlHelper.transactionExecute(connection -> {
            sqlHelper.executeWithException("INSERT INTO resume (uuid, full_name) VALUES (?,?)", ps -> {
                ps.setString(1, resume.getUuid());
                ps.setString(2, resume.getFullName());
                ps.execute();
                return null;
            });
            if (resume.getContacts().size() != 0) {
                sqlHelper.executeWithException("INSERT INTO contact (resume_uuid,type,value) VALUES (?,?,?)", ps -> {
                    for (Map.Entry<ContactType, String> e : resume.getContacts().entrySet()) {
                        ps.setString(1, resume.getUuid());
                        ps.setString(2, e.getKey().name());
                        ps.setString(3, e.getValue());
                        ps.addBatch();
                    }
                    ps.executeBatch();
                    return null;
                });
            }
            /*try (PreparedStatement ps = connection.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?,?)")) {
                ps.setString(1, resume.getUuid());
                ps.setString(2, resume.getFullName());
                ps.execute();
            }
            try (PreparedStatement ps = connection.prepareStatement("INSERT INTO contact (resume_uuid,type,value) VALUES (?,?,?)")) {
                for (Map.Entry<ContactType, String> e : resume.getContacts().entrySet()) {
                    ps.setString(1, resume.getUuid());
                    ps.setString(2, e.getKey().name());
                    ps.setString(3, e.getValue());
                    ps.addBatch();
                }
                ps.executeBatch();
            }*/
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
            if (resume.getContacts().size() != 0) {
                do {
                    String value = rs.getString("value");
                    ContactType type = ContactType.valueOf(rs.getString("type"));
                    resume.addContacts(type, value);
                } while (rs.next());
            }
            return resume;
        });
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.transactionExecute(connection -> {
            sqlHelper.executeWithException("delete from resume where uuid = ?", ps -> {
                deleteQueryExecute(ps, uuid);
                return null;
            });
            return null;
        });
        /*sqlHelper.executeWithException("delete from resume where uuid = ?", ps -> {
            ps.setString(1, uuid);
            if (ps.executeUpdate() == 0){
                throw new NotExistStorageException("this resume doesn't exist");
            }
            return null;
        });*/
    }

    private void deleteQueryExecute(PreparedStatement ps, String uuid) throws SQLException {
        ps.setString(1, uuid);
        if (ps.executeUpdate() == 0) {
            throw new NotExistStorageException(uuid + " resume doesn't exist");
        }
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.executeWithException("SELECT * FROM resume r ORDER BY full_name,uuid ",
                ps -> {
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) {
                        throw new StorageException("storage is empty");
                    }
                    List<Resume> resumesList = new ArrayList<>();
                    do {
                        PreparedStatement sp = sqlHelper.connectionFactory.getConnection().prepareStatement("SELECT * FROM resume r LEFT JOIN contact c ON r.uuid = c.resume_uuid WHERE c.resume_uuid =?");
                        String uuid = rs.getString("uuid");
                        sp.setString(1, uuid);
                        ResultSet ss = sp.executeQuery();
                        Map<ContactType, String> contacts = new EnumMap<>(ContactType.class);
                        if (ss.next()) {
                            do {
                                contacts.put(ContactType.valueOf(ss.getString("type")), ss.getString("value"));
                            } while (ss.next());
                        }
                        Resume resume = new Resume(uuid, rs.getString("full_name"));
                        resume.setContacts(contacts);
                        resumesList.add(resume);
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

