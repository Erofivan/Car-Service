package com.erofivan;

import com.erofivan.infrastructure.http.StorageServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@EmbeddedKafka(partitions = 1, topics = {
    "orders.inventory.placed",
    "orders.inventory.cancelled",
    "orders.custom.warehouse-approved"
})
@Sql(scripts = "/db/seed-test-users.sql",
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/db/cleanup-test-users.sql",
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
abstract class AbstractOrderServiceIT {

    @Autowired
    protected MockMvc mockMvc;

    @MockitoBean
    protected StorageServiceClient storageServiceClient;
}
