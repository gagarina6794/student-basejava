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
            int size = dis.readInt();
            Resume resume = new Resume(uuid, fullName);
            var contactsList = new EnumMap<ContactType, String>(ContactType.class);
            readWithException(contactsList.entrySet(),dis, size, ()->{
                contactsList.put(ContactType.valueOf(dis.readUTF()),dis.readUTF() );
            });
            resume.setContacts(contactsList);
            int sectionSize = dis.readInt();
            var sectionsList = new EnumMap<SectionType, Section>(SectionType.class);
            readWithException(sectionsList.entrySet(),dis,sectionSize, ()->{
                SectionType sectionType = SectionType.valueOf(dis.readUTF());
                switch (sectionType) {
                    case ACHIEVEMENTS:
                    case QUALIFICATION:
                        int contentSize = dis.readInt();
                        List<String> contentList = new ArrayList<>();
                        readWithException(contentList, dis, contentSize, () -> contentList.add(dis.readUTF()));
                        sectionsList.put(sectionType, new BulletedListSection(contentList));
                        break;
                    case OBJECTIVE:
                    case PERSONAL:
                        var content = dis.readUTF();
                        sectionsList.put(sectionType, new SimpleTextSection(content));
                        break;
                    case EDUCATION:
                    case EXPERIENCE:
                        int contentOrganizationSize = dis.readInt();
                        List<Organization> organizationList = new ArrayList<>();
                        readWithException(organizationList, dis, contentOrganizationSize, () -> {
                            String name = dis.readUTF();
                            dis.readUTF();
                            String link = dis.readUTF();
                            int experienceSize = dis.readInt();
                            List<Organization.Experience> experiences = new ArrayList<>();
                            readWithException(experiences, dis, experienceSize, () ->
                            {
                                YearMonth date1 = YearMonth.parse(dis.readUTF(), DateTimeFormatter.ofPattern("uuuu-M"));
                                YearMonth date2 = YearMonth.parse(dis.readUTF(), DateTimeFormatter.ofPattern("uuuu-M"));
                                experiences.add(new Organization.Experience(date1, date2, dis.readUTF(), dis.readUTF()));
                            });
                            organizationList.add(new Organization(name, new Link(name, link), experiences));
                        });
                        sectionsList.put(sectionType, new OrganizationSection(organizationList));
                        break;
                }
            });
            resume.setSections(sectionsList);
            return resume;
        }
    }

    private void contactsSerialize(DataOutputStream dos, Resume resume) throws IOException {
        dos.writeUTF(resume.getUuid());
        dos.writeUTF(resume.getFullName());
        Map<ContactType, String> contacts = resume.getContacts();
        dos.writeInt(contacts.size());
        writeWithException(contacts.entrySet(), dos, entry->{
            dos.writeUTF(entry.getKey().name());
            dos.writeUTF(entry.getValue());
        });
    }

    private void sectionsSerialize(DataOutputStream dos, Resume resume) throws IOException {
        Map<SectionType, Section> sections = resume.getSections();
        dos.writeInt(sections.size());
        writeWithException(sections.entrySet(), dos, entry -> {
            dos.writeUTF(entry.getKey().name());
            switch (entry.getKey()) {
                case ACHIEVEMENTS:
                case QUALIFICATION:
                    dos.writeInt(((BulletedListSection) entry.getValue()).getContent().size());
                    writeWithException(((BulletedListSection) entry.getValue()).getContent(), dos, dos::writeUTF);
                    break;
                case OBJECTIVE:
                case PERSONAL:
                    dos.writeUTF(((SimpleTextSection) entry.getValue()).getContent());
                    break;
                case EDUCATION:
                case EXPERIENCE:
                    dos.writeInt(((OrganizationSection) entry.getValue()).getContent().size());
                    writeWithException(((OrganizationSection) entry.getValue()).getContent(), dos, item -> {
                        dos.writeUTF(item.getOrganizationName());
                        dos.writeUTF(item.getLink().getName());
                        dos.writeUTF(item.getLink().getUrl());
                        dos.writeInt(item.getExperiences().size());
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
        for (var item : collection) {
            writable.writeCollection(item);
        }
    }

    private <T> void readWithException(Collection<T> collection, DataInputStream dis, int size, Readable readable) throws IOException {
        for (int i = 0; i < size; i++) {
            readable.readCollection();
        }
    }
}
