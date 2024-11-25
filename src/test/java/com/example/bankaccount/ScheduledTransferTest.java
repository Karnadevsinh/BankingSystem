package com.example.bankaccount;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

class ScheduledTransferTest {
    private ScheduledTransfer scheduler;

    @BeforeEach
    void setUp() {
        scheduler = new ScheduledTransfer();
    }

    @Test
    void testScheduleTransfer() {
        scheduler.scheduleTransfer("A001", "A002", 500, LocalDate.now().plusDays(1));
        List<ScheduledTransfer.Transfer> transfers = scheduler.getScheduledTransfers();
        assertEquals(1, transfers.size());
    }

    @Test
    void testExecuteDueTransfers() {
        scheduler.scheduleTransfer("A001", "A002", 500, LocalDate.now());
        scheduler.executeDueTransfers();
        assertTrue(scheduler.getScheduledTransfers().isEmpty());
    }
}
