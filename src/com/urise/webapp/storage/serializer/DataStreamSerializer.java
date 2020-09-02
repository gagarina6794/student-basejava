package com.urise.webapp.storage.serializer;

import com.urise.webapp.model.*;

import java.io.*;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

interface Writable<T> {
    void writeCollection(T item) throws IOException;
}

interface Readable {
    void readCollection() throws IOException;
}

public class DataStreamSerializer implements StorageSerialization {

    @Override
    public void doWrite(Resume resume, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            contactsSerialize(dos, resume);
            sectionsSerialize(dos, resume);
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);
            readWithException(dis, () -> {
                resume.getContacts().put(ContactType.valueOf(dis.readUTF()), dis.readUTF());
            });
            readWithException(dis, () -> {
                SectionType sectionType = SectionType.valueOf(dis.readUTF());
                switch (sectionType) {
                    case ACHIEVEMENTS:
                    case QUALIFICATION:
                        List<String> contentList = new ArrayList<>();
                        readWithException(dis, () -> contentList.add(dis.readUTF()));
                        resume.getSections().put(sectionType, new BulletedListSection(contentList));
                        break;
                    case OBJECTIVE:
                    case PERSONAL:
                        var content = dis.readUTF();
                        resume.getSections().put(sectionType, new SimpleTextSection(content));
                        break;
                    case EDUCATION:
                    case EXPERIENCE:
                        List<Organization> organizationList = new ArrayList<>();
                        readWithException(dis, () -> {
                            String name = dis.readUTF();
                            dis.readUTF();
                            String link = dis.readUTF();
                            List<Organization.Experience> experiences = new ArrayList<>();
                            readWithException(dis, () ->
                            {
                                experiences.add(new Organization.Experience(readYearMonth(dis), readYearMonth(dis), dis.readUTF(), dis.readUTF()));
                            });
                            organizationList.add(new Organization(name, new Link(name, link), experiences));
                        });
                        resume.getSections().put(sectionType, new OrganizationSection(organizationList));
                        break;
                }
            });
            return resume;
        }
    }

    private YearMonth readYearMonth(DataInputStream dis) throws IOException {
        return YearMonth.parse(dis.readUTF(), DateTimeFormatter.ofPattern("uuuu-M"));
    }

    private void contactsSerialize(DataOutputStream dos, Resume resume) throws IOException {
        dos.writeUTF(resume.getUuid());
        dos.writeUTF(resume.getFullName());
        Map<ContactType, String> contacts = resume.getContacts();
        writeWithException(contacts.entrySet(), dos, entry -> {
            dos.writeUTF(entry.getKey().name());
            dos.writeUTF(entry.getValue());
        });
    }

    private void sectionsSerialize(DataOutputStream dos, Resume resume) throws IOException {
        Map<SectionType, Section> sections = resume.getSections();
        writeWithException(sections.entrySet(), dos, entry -> {
            dos.writeUTF(entry.getKey().name());
            switch (entry.getKey()) {
                case ACHIEVEMENTS:
                case QUALIFICATION:
                    writeWithException(((BulletedListSection) entry.getValue()).getContent(), dos, dos::writeUTF);
                    break;
                case OBJECTIVE:
                case PERSONAL:
                    dos.writeUTF(((SimpleTextSection) entry.getValue()).getContent());
                    break;
                case EDUCATION:
                case EXPERIENCE:
                    writeWithException(((OrganizationSection) entry.getValue()).getContent(), dos, item -> {
                        dos.writeUTF(item.getOrganizationName());
                        dos.writeUTF(item.getLink().getName());
                        dos.writeUTF(item.getLink().getUrl());
                        writeWithException(item.getExperiences(), dos, experience -> {
                            dos.writeUTF(experience.getYearFrom().toString());
                            dos.writeUTF(experience.getYearTo().toString());
                            dos.writeUTF(experience.getTitle());
                            dos.writeUTF(experience.getInfo());
                        });
                    });
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + entry.getKey());
            }
        });
    }


    private <T> void writeWithException(Collection<T> collection, DataOutputStream dos, Writable<T> writable) throws IOException {
        dos.writeInt(collection.size());
        for (var item : collection) {
            writable.writeCollection(item);
        }
    }

    private void readWithException(DataInputStream dis, Readable readable) throws IOException {
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            readable.readCollection();
        }
    }
}
