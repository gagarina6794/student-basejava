drop table resume;

CREATE TABLE resume
(
    uuid      CHAR(36) PRIMARY KEY NOT NULL,
    full_name TEXT                 NOT NULL
);

CREATE TABLE contact
(
    id          SERIAL,
    resume_uuid CHAR(36) NOT NULL REFERENCES resume (uuid) ON DELETE CASCADE,
    type        TEXT     NOT NULL,
    value       TEXT     NOT NULL
);

CREATE TABLE section
(
    id           SERIAL,
    resume_uuid  CHAR(36) NOT NULL REFERENCES resume (uuid) ON DELETE CASCADE,
    section_type TEXT     NOT NULL,
    information  TEXT     NOT NULL
);

CREATE UNIQUE INDEX contact_uuid_type_index
    ON contact (resume_uuid, type);

DROP TABLE resume;
DROP TABLE contact;
DROP TABLE section;

SELECT *
FROM resume r
         LEFT JOIN contact c ON r.uuid = c.resume_uuid
ORDER BY r.full_name, c.resume_uuid;

SELECT * FROM resume r LEFT JOIN contact c ON r.uuid = c.resume_uuid
    JOIN section s ON r.uuid = s.resume_uuid ORDER BY r.full_name,r.uuid;