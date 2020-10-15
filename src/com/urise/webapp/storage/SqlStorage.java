package com.urise.webapp.storage;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.*;
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
        return sqlHelper.executeWithException("SELECT * FROM resume r LEFT JOIN contact c ON r.uuid = c.resume_uuid " +
                "LEFT JOIN section s ON r.uuid = s.resume_uuid WHERE r.uuid =?", ps -> {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            Resume resume = new Resume(uuid, rs.getString("full_name"));
            do {
                setContactsInResume(rs, resume);
                setSectionsInResume(rs, resume);
            } while (rs.next());
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
                if (!rs.next()) {
                    throw new StorageException("storage is empty");

                }
                do {
                    setContactsInResume(rs, resumesMap.get(rs.getString("resume_uuid")));
                }
                while (rs.next());
            }
            try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM section")) {
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    throw new StorageException("storage is empty");
                }
                do {
                    setSectionsInResume(rs, resumesMap.get(rs.getString("resume_uuid")));
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
                switch (entry.getKey()) {
                    case ACHIEVEMENTS:
                    case QUALIFICATION:
                        String information = String.join("\n", ((BulletedListSection) entry.getValue()).getContent());
                        ps.setString(1, resume.getUuid());
                        ps.setString(2, entry.getKey().toString());
                        ps.setString(3, information);
                        ps.addBatch();
                        break;
                    case OBJECTIVE:
                    case PERSONAL:
                        ps.setString(1, resume.getUuid());
                        ps.setString(2, entry.getKey().toString());
                        ps.setString(3, ((SimpleTextSection) entry.getValue()).getContent());
                        ps.addBatch();
                        break;
                    case EDUCATION:
                    case EXPERIENCE:
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + entry.getKey());
                }
            }
            ps.executeBatch();
        }
    }

    private void executeUpdateWithCheck(PreparedStatement ps, String uuid) throws SQLException {
        if (ps.executeUpdate() == 0) {
            throw new NotExistStorageException(uuid);
        }
    }

    private void setSectionsInResume(ResultSet rs, Resume resume) throws SQLException {
        String sectionType = rs.getString("section_type");
        if (sectionType != null) {
            SectionType section = SectionType.valueOf(sectionType);
            switch (section) {
                case ACHIEVEMENTS:
                case QUALIFICATION:
                    List<String> contentList = Arrays.asList(rs.getString("information").split("\n"));
                    resume.getSections().put(section, new BulletedListSection(contentList));
                    break;
                case OBJECTIVE:
                case PERSONAL:
                    resume.getSections().put(section, new SimpleTextSection(rs.getString("information")));
                    break;
                case EDUCATION:
                case EXPERIENCE:
                    break;
            }
        }
    }

    private void setContactsInResume(ResultSet rs, Resume resume) throws SQLException {
        String value = rs.getString("value");
        String type = rs.getString("type");
        if (value != null && type != null) {
            resume.addContacts(ContactType.valueOf(type), value);
        }
    }
}

