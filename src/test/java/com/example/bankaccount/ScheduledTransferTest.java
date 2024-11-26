package com.example.bankaccount;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

class ScheduledTransferTest {
    private ScheduledTransfer scheduler;
    private LocalDate today;

    @BeforeEach
    void setUp() {
        scheduler = new ScheduledTransfer();
        today = LocalDate.now();
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

    @Test
    void executeDueTransfers_RemovesOnlyTransfersDueToday() {
        // Schedule a transfer for today
        scheduler.scheduleTransfer("ACC1", "ACC2", 100.0, today);

        // Schedule a transfer for tomorrow
        scheduler.scheduleTransfer("ACC3", "ACC4", 200.0, today.plusDays(1));

        // Execute transfers
        scheduler.executeDueTransfers();

        // Verify only tomorrow's transfer remains
        List<ScheduledTransfer.Transfer> remainingTransfers = scheduler.getScheduledTransfers();
        assertEquals(1, remainingTransfers.size());
        ScheduledTransfer.Transfer remaining = remainingTransfers.get(0);
        assertEquals(today.plusDays(1), remaining.date);
        assertEquals("ACC3", remaining.fromAccount);
        assertEquals("ACC4", remaining.toAccount);
        assertEquals(200.0, remaining.amount);
    }

    @Test
    void executeDueTransfers_RemovesAllTransfersDueToday() {
        // Schedule multiple transfers for today
        scheduler.scheduleTransfer("ACC1", "ACC2", 100.0, today);
        scheduler.scheduleTransfer("ACC3", "ACC4", 200.0, today);
        scheduler.scheduleTransfer("ACC5", "ACC6", 300.0, today);

        // Execute transfers
        scheduler.executeDueTransfers();

        // Verify all transfers were removed
        assertTrue(scheduler.getScheduledTransfers().isEmpty());
    }

    @Test
    void executeDueTransfers_KeepsAllFutureTransfers() {
        // Schedule only future transfers
        scheduler.scheduleTransfer("ACC1", "ACC2", 100.0, today.plusDays(1));
        scheduler.scheduleTransfer("ACC3", "ACC4", 200.0, today.plusDays(2));

        // Get initial size
        int initialSize = scheduler.getScheduledTransfers().size();

        // Execute transfers
        scheduler.executeDueTransfers();

        // Verify no transfers were removed
        assertEquals(initialSize, scheduler.getScheduledTransfers().size());
    }

    @Test
    void executeDueTransfers_HandlesEmptyTransfersList() {
        // Execute transfers on empty list
        scheduler.executeDueTransfers();

        // Verify still empty
        assertTrue(scheduler.getScheduledTransfers().isEmpty());
    }

    @Test
    void executeDueTransfers_MixedTransfers() {
        // Schedule transfers for various dates
        scheduler.scheduleTransfer("ACC1", "ACC2", 100.0, today); // Should be removed
        scheduler.scheduleTransfer("ACC3", "ACC4", 200.0, today); // Should be removed
        scheduler.scheduleTransfer("ACC5", "ACC6", 300.0, today.plusDays(1)); // Should remain
        scheduler.scheduleTransfer("ACC7", "ACC8", 400.0, today.plusDays(2)); // Should remain

        // Execute transfers
        scheduler.executeDueTransfers();

        // Verify results
        List<ScheduledTransfer.Transfer> remainingTransfers = scheduler.getScheduledTransfers();
        assertEquals(2, remainingTransfers.size());

        // Verify all remaining transfers are future transfers
        for (ScheduledTransfer.Transfer transfer : remainingTransfers) {
            assertTrue(transfer.date.isAfter(today));
        }
    }

    @Test
    void transfer_toString_ContainsAllFields() {
        // Arrange
        String fromAccount = "123456";
        String toAccount = "789012";
        double amount = 1000.50;
        LocalDate date = LocalDate.of(2024, 1, 1);

        ScheduledTransfer.Transfer transfer = new ScheduledTransfer.Transfer(fromAccount, toAccount, amount, date);

        // Act
        String result = transfer.toString();

        // Assert
        assertAll(
                () -> assertTrue(result.contains(fromAccount), "Should contain from account"),
                () -> assertTrue(result.contains(toAccount), "Should contain to account"),
                () -> assertTrue(result.contains(String.valueOf(amount)), "Should contain amount"),
                () -> assertTrue(result.contains(date.toString()), "Should contain date"),
                () -> assertFalse(result.isEmpty(), "Should not be empty"),
                () -> assertTrue(result.contains("Transfer from"), "Should contain 'Transfer from' text"),
                () -> assertTrue(result.contains("to"), "Should contain 'to' text"),
                () -> assertTrue(result.contains("of"), "Should contain 'of' text"),
                () -> assertTrue(result.contains("on"), "Should contain 'on' text")
        );
    }

    @Test
    void transfer_toString_DifferentValues_ProduceDifferentStrings() {
        // Arrange
        ScheduledTransfer.Transfer transfer1 = new ScheduledTransfer.Transfer("ACC1", "ACC2", 100.0, LocalDate.of(2024, 1, 1));
        ScheduledTransfer.Transfer transfer2 = new ScheduledTransfer.Transfer("ACC3", "ACC4", 200.0, LocalDate.of(2024, 1, 2));

        // Act
        String result1 = transfer1.toString();
        String result2 = transfer2.toString();

        // Assert
        assertNotEquals(result1, result2, "Different transfers should have different string representations");
        assertFalse(result1.isEmpty(), "First transfer string should not be empty");
        assertFalse(result2.isEmpty(), "Second transfer string should not be empty");
    }
}
