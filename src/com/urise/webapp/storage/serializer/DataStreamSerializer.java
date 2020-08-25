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
        for (Map.Entry<SectionType, Section> entry : resume.getSections().entrySet()) {
            dos.writeUTF(entry.getKey().name());
            switch (entry.getKey()) {
                case ACHIEVEMENTS:
                case QUALIFICATION:
                    var bulletedSectionList = (BulletedListSection) entry.getValue();
                    dos.writeInt(bulletedSectionList.getContent().size());
                    for (String item : bulletedSectionList.getContent()) {
                        dos.writeUTF(item);
                    }
                    break;
                case OBJECTIVE:
                case PERSONAL:
                    var simpleTextList = (SimpleTextSection) entry.getValue();
                    dos.writeUTF(simpleTextList.getContent());
                    break;
                case EDUCATION:
                case EXPERIENCE:
                    var organizationList = (OrganizationSection) entry.getValue();
                    dos.writeInt(organizationList.getContent().size());
                    for (var organization : organizationList.getContent()) {
                        dos.writeUTF(organization.getOrganizationName());
                        dos.writeUTF(organization.getLink());
                        dos.writeInt(organization.getExperiences().size());
                        for (var experience : organization.getExperiences()) {
                            dos.writeUTF(experience.getYearFrom().toString());
                            dos.writeUTF(experience.getYearTo().toString());
                            dos.writeUTF(experience.getInfo());
                        }
                    }
            }
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
                            String link = dis.readUTF();
                            int experienceSize = dis.readInt();
                            List<Experience> experiences = new ArrayList<>();
                            for (int e = 0; e < experienceSize; e++) {
                               // String data1 = dis.readUTF();
                                YearMonth date1 = YearMonth.parse(dis.readUTF(), DateTimeFormatter.ofPattern("uuuu-M"));
                               // String data2 = dis.readUTF();
                                YearMonth date2 = YearMonth.parse(dis.readUTF(), DateTimeFormatter.ofPattern("uuuu-M"));
                                experiences.add(new Experience(date1,date2,dis.readUTF()));
                            }
                            organizationList.add(new Organization(name,link,experiences));
                        }
                        resume.addSection(sectionType, new OrganizationSection(organizationList));
                        break;
                }
            }

            return resume;
        }
    }


}
