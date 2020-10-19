package com.urise.webapp.storage;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.*;
import com.urise.webapp.sql.SqlHelper;
import com.urise.webapp.util.JsonParser;

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
                executeUpdateWithCheck(ps, resume.getUuid());
            }
            try (PreparedStatement ps = connection.prepareStatement("DELETE FROM contact WHERE resume_uuid = ?")) {
                ps.setString(1, resume.getUuid());
            }
            try (PreparedStatement ps = connection.prepareStatement("DELETE FROM section WHERE resume_uuid = ?")) {
                ps.setString(1, resume.getUuid());
            }

            insertInContact(resume, connection);
            insertInSection(resume, connection);
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
            insertInSection(resume, connection);
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.transactionExecute(connection -> {
            Resume resume;
            try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM resume WHERE uuid =?")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    throw new NotExistStorageException(uuid);
                }
                resume = new Resume(uuid, rs.getString("full_name"));
            }

            try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM contact WHERE resume_uuid =?")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    setContacts(rs, resume);
                }
            }

            try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM section WHERE resume_uuid =?")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    setSections(rs, resume);
                }
            }
            return resume;
        });
    }


    @Override
    public void delete(String uuid) {
        sqlHelper.transactionExecute(connection -> {
            try (PreparedStatement ps = connection.prepareStatement("DELETE FROM resume WHERE uuid = ?")) {
                ps.setString(1, uuid);
                executeUpdateWithCheck(ps, uuid);
                return null;
            }
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.transactionExecute(connection -> {
            Map<String, Resume> resumesMap = new LinkedHashMap<>();
            try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM resume r ORDER BY r.full_name,r.uuid")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String uuid = rs.getString("uuid");
                    if (!resumesMap.containsKey(uuid)) {
                        resumesMap.put(uuid, new Resume(uuid, rs.getString("full_name")));
                    }
                }
            }

            try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM contact ")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    setContacts(rs, resumesMap.get(rs.getString("resume_uuid")));
                }
            }

            try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM section")) {
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    throw new StorageException("storage is empty");
                }
                do {
                    setSections(rs, resumesMap.get(rs.getString("resume_uuid")));
                } while (rs.next());
            }
            return new ArrayList<>(resumesMap.values());
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
            try (PreparedStatement ps = connection.prepareStatement("INSERT INTO contact (resume_uuid,type,value) VALUES (?,?,?)")) {
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

    private void insertInSection(Resume resume, Connection connection) throws SQLException {
        if (resume.getSections() == null)
            return;
        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO section (resume_uuid,section_type,information) VALUES (?,?,?)")) {
            for (Map.Entry<SectionType, Section> entry : resume.getSections().entrySet()) {
                ps.setString(1, resume.getUuid());
                ps.setString(2, entry.getKey().name());
                Section section = entry.getValue();
                ps.setString(3, JsonParser.write(section, Section.class));
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void executeUpdateWithCheck(PreparedStatement ps, String uuid) throws SQLException {
        if (ps.executeUpdate() == 0) {
            throw new NotExistStorageException(uuid);
        }
    }

    private void setSections(ResultSet rs, Resume resume) throws SQLException {
        String content = rs.getString("information");
        if (content != null) {
            SectionType sectionType = SectionType.valueOf(rs.getString("section_type"));
            resume.addSection(sectionType, JsonParser.read(content, Section.class));
        }
    }

    private void setContacts(ResultSet rs, Resume resume) throws SQLException {
        String value = rs.getString("value");
        String type = rs.getString("type");
        if (value != null && type != null) {
            resume.addContacts(ContactType.valueOf(type), value);
        }
    }
}

