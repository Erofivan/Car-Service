package com.erofivan;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PostgresIT extends AbstractIntegrationTest {
    @Autowired
    private DataSource dataSource;

    @Test
    void liquibaseMigrationsApplied() throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            ResultSet rs = conn.createStatement().executeQuery(
                "SELECT count(*) FROM brands WHERE removed = FALSE"
            );
            rs.next();
            assertEquals(3, rs.getInt(1));
        }
    }

    @Test
    void seedDataPresent() throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            ResultSet rs = conn.createStatement().executeQuery(
                "SELECT count(*) FROM cars WHERE removed = FALSE"
            );
            rs.next();
            assertTrue(rs.getInt(1) >= 3);
        }
    }
}
