package com.urise.webapp.storage.serializer;

import com.urise.webapp.model.*;

import java.io.*;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataStreamSerializer implements StorageSerialization {

    @Override
    public void doWrite(Resume resume, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            contactsSerialize(dos, resume);
            sectionsSerialize(dos, resume);
        }
    }

    private void contactsSerialize(DataOutputStream dos, Resume resume) throws IOException {
        dos.writeUTF(resume.getUuid());
        dos.writeUTF(resume.getFullName());
        Map<ContactType, String> contacts = resume.getContacts();
        dos.writeInt(contacts.size());
        for (Map.Entry<ContactType, String> entry : resume.getContacts().entrySet()) {
            dos.writeUTF(entry.getKey().name());
            dos.writeUTF(entry.getValue());
        }
    }

    private void sectionsSerialize(DataOutputStream dos, Resume resume) throws IOException {
        Map<SectionType, Section> sections = resume.getSections();
        dos.writeInt(sections.size());
        Writable writableCollection;
        for (Map.Entry<SectionType, Section> entry : resume.getSections().entrySet()) {
            dos.writeUTF(entry.getKey().name());
            switch (entry.getKey()) {
                case ACHIEVEMENTS:
                case QUALIFICATION:
                    writableCollection = (BulletedListSection) entry.getValue();
                    break;
                case OBJECTIVE:
                case PERSONAL:
                    writableCollection = (SimpleTextSection) entry.getValue();
                    break;
                case EDUCATION:
                case EXPERIENCE:
                    writableCollection = (OrganizationSection) entry.getValue();
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + entry.getKey());
            }
            writableCollection.writeCollection(dos);
        }
    }


    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            int size = dis.readInt();
            Resume resume = new Resume(uuid, fullName);
            for (int i = 0; i < size; i++) {
                resume.addContacts(ContactType.valueOf(dis.readUTF()), dis.readUTF());
            }

            int sectionSize = dis.readInt();
            for (int i = 0; i < sectionSize; i++) {
                SectionType sectionType = SectionType.valueOf(dis.readUTF());
                switch (sectionType) {
                    case ACHIEVEMENTS:
                    case QUALIFICATION:
                        int contentSize = dis.readInt();
                        List<String> contentList = new ArrayList<>();
                        for (int j = 0; j < contentSize; j++) {
                            contentList.add(dis.readUTF());
                        }
                        resume.getSections().put(sectionType, new BulletedListSection(contentList));
                        break;
                    case OBJECTIVE:
                    case PERSONAL:
                        var content = dis.readUTF();
                        resume.getSections().put(sectionType, new SimpleTextSection(content));
                        break;
                    case EDUCATION:
                    case EXPERIENCE:
                        int contentOrganizationSize = dis.readInt();
                        List<Organization> organizationList = new ArrayList<>();
                        for (int k = 0; k < contentOrganizationSize; k++) {
                            String name = dis.readUTF();
                            dis.readUTF();
                            String link = dis.readUTF();
                            int experienceSize = dis.readInt();
                            List<Organization.Experience> experiences = new ArrayList<>();
                            for (int e = 0; e < experienceSize; e++) {
                                YearMonth date1 = YearMonth.parse(dis.readUTF(), DateTimeFormatter.ofPattern("uuuu-M"));
                                YearMonth date2 = YearMonth.parse(dis.readUTF(), DateTimeFormatter.ofPattern("uuuu-M"));
                                experiences.add(new Organization.Experience(date1, date2, dis.readUTF(), dis.readUTF()));
                            }
                            organizationList.add(new Organization(name, new Link(name, link), experiences));
                        }
                        resume.addSection(sectionType, new OrganizationSection(organizationList));
                        break;
                }
            }

            return resume;
        }
    }


}