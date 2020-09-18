package com.urise.webapp.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface QueryExecutable<T> {
    T executeQuery(PreparedStatement ps) throws SQLException;
}
