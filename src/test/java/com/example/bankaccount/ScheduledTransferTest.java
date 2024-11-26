package com.example.bankaccount;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.time.LocalDate;
import java.util.List;

class ScheduledTransferTest {
    private ScheduledTransfer scheduler;
    private LocalDate today;

    private ScheduledTransfer scheduledTransfer;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        scheduler = new ScheduledTransfer();
        today = LocalDate.now();

        scheduledTransfer = new ScheduledTransfer();
        // Capture system output to test println
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    void testScheduleTransfer() {
        scheduler.scheduleTransfer("A001", "A002", 500, LocalDate.now().plusDays(1));
        List<ScheduledTransfer.Transfer> transfers = scheduler.getScheduledTransfers();
        assertEquals(1, transfers.size());
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


    @Test
    void testExecuteDueTransfersRemovesTransfersDueToday() {
        // Schedule a transfer for today
        LocalDate today = LocalDate.now();
        scheduledTransfer.scheduleTransfer("Account1", "Account2", 100.0, today);

        // Execute due transfers
        scheduledTransfer.executeDueTransfers();

        // Verify the transfer was removed
        assertEquals(0, scheduledTransfer.getScheduledTransfers().size(),
                "Transfers due today should be removed");
    }

    @Test
    void testExecuteDueTransfersPrintsExecutedTransfer() {
        // Schedule a transfer for today
        LocalDate today = LocalDate.now();
        scheduledTransfer.scheduleTransfer("Account1", "Account2", 100.0, today);

        // Execute due transfers
        scheduledTransfer.executeDueTransfers();

        // Check the output
        String output = outputStream.toString().trim();
        assertTrue(output.contains("Executed transfer: Transfer from Account1 to Account2 of 100.0 on " + today),
                "Should print details of executed transfer");
    }

    @Test
    void testExecuteDueTransfersDoesNotRemoveTransfersNotDueToday() {
        // Schedule transfers for different dates
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        LocalDate dayAfterTomorrow = today.plusDays(2);

        scheduledTransfer.scheduleTransfer("Account1", "Account2", 100.0, today);
        scheduledTransfer.scheduleTransfer("Account3", "Account4", 200.0, tomorrow);
        scheduledTransfer.scheduleTransfer("Account5", "Account6", 300.0, dayAfterTomorrow);

        // Execute due transfers
        scheduledTransfer.executeDueTransfers();

        // Verify only today's transfer was removed
        assertEquals(2, scheduledTransfer.getScheduledTransfers().size(),
                "Only transfers due today should be removed");
    }

    @Test
    void testExecuteDueTransfersWithEmptyList() {
        // Execute due transfers on an empty list
        scheduledTransfer.executeDueTransfers();

        // Verify no exceptions and list remains empty
        assertEquals(0, scheduledTransfer.getScheduledTransfers().size(),
                "List should remain empty");
    }

    @AfterEach
    void tearDown() {
        // Restore original system output
        System.setOut(originalOut);
    }

    // Test case to kill mutant: removed call to LocalDate::isBefore
    @Test
    public void testCannotScheduleTransferInPast() {
        LocalDate pastDate = LocalDate.now().minusDays(1);

        // Ensure IllegalArgumentException is thrown for past dates
        assertThrows(IllegalArgumentException.class, () -> {
            scheduledTransfer.scheduleTransfer("account1", "account2", 100.0, pastDate);
        });
    }

    // Test case to kill mutant: removed equality check in scheduleTransfer
    @Test
    public void testScheduleTransferWithFutureDate() {
        LocalDate futureDate = LocalDate.now().plusDays(1);

        // Ensure transfer can be scheduled for a future date
        scheduledTransfer.scheduleTransfer("account1", "account2", 100.0, futureDate);

        List<ScheduledTransfer.Transfer> scheduledTransfers = scheduledTransfer.getScheduledTransfers();
        assertEquals(1, scheduledTransfers.size());
        assertEquals(futureDate, scheduledTransfers.get(0).date);
    }

    // Test case to kill mutant: removed conditional in executeDueTransfers
    @Test
    public void testExecuteDueTransfers() {
        LocalDate today = LocalDate.now();

        // Schedule multiple transfers
        scheduledTransfer.scheduleTransfer("account1", "account2", 100.0, today);
        scheduledTransfer.scheduleTransfer("account3", "account4", 200.0, today);
        scheduledTransfer.scheduleTransfer("account5", "account6", 300.0, today.plusDays(1));

        // Capture system output to verify execution
        java.io.ByteArrayOutputStream outContent = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(outContent));

        // Execute due transfers
        scheduledTransfer.executeDueTransfers();

        // Verify transfers for today are removed
        List<ScheduledTransfer.Transfer> remainingTransfers = scheduledTransfer.getScheduledTransfers();
        assertEquals(1, remainingTransfers.size());
        assertEquals(today.plusDays(1), remainingTransfers.get(0).date);

        // Verify output contains the executed transfers
        assertTrue(outContent.toString().contains("Executed transfer: Transfer from account1 to account2 of 100.0 on " + today));
        assertTrue(outContent.toString().contains("Executed transfer: Transfer from account3 to account4 of 200.0 on " + today));

        // Reset System.out
        System.setOut(System.out);
    }

    // Additional edge case test
    @Test
    public void testScheduleTransferWithZeroAmount() {
        LocalDate futureDate = LocalDate.now().plusDays(1);

        // Ensure zero amount transfer can be scheduled
        scheduledTransfer.scheduleTransfer("account1", "account2", 0.0, futureDate);

        List<ScheduledTransfer.Transfer> scheduledTransfers = scheduledTransfer.getScheduledTransfers();
        assertEquals(1, scheduledTransfers.size());
        assertEquals(0.0, scheduledTransfers.get(0).amount);
    }

    @Test
    public void testExecuteDueTransfersWithConditionalCoverage() {
        LocalDate today = LocalDate.now();

        // Reflection-based approach to bypass validation for testing
        try {
            // Create a new instance using reflection to bypass constructor validation
            ScheduledTransfer scheduledTransfer = ScheduledTransfer.class.getDeclaredConstructor().newInstance();

            // Use reflection to directly add transfers to the list
            java.lang.reflect.Field transfersField = ScheduledTransfer.class.getDeclaredField("transfers");
            transfersField.setAccessible(true);

            // Get the transfers list
            List<ScheduledTransfer.Transfer> transfersList =
                    (List<ScheduledTransfer.Transfer>) transfersField.get(scheduledTransfer);

            // Create Transfer objects directly
            ScheduledTransfer.Transfer todayTransfer1 = createTransfer("account1", "account2", 100.0, today);
            ScheduledTransfer.Transfer todayTransfer2 = createTransfer("account3", "account4", 200.0, today);
            ScheduledTransfer.Transfer futureTransfer = createTransfer("account5", "account6", 300.0, today.plusDays(1));
            ScheduledTransfer.Transfer pastTransfer = createTransfer("account7", "account8", 400.0, today.minusDays(1));

            // Add transfers directly to the list
            transfersList.add(todayTransfer1);
            transfersList.add(todayTransfer2);
            transfersList.add(futureTransfer);
            transfersList.add(pastTransfer);

            // Capture system output to verify exact execution
            java.io.ByteArrayOutputStream outContent = new java.io.ByteArrayOutputStream();
            System.setOut(new java.io.PrintStream(outContent));

            // Execute due transfers
            scheduledTransfer.executeDueTransfers();

            // Reset System.out
            System.setOut(System.out);

            // Verify the output contains exactly the due transfers
            String output = outContent.toString().trim();
            String[] executedTransfers = output.split(System.lineSeparator());

            // Verify only the two transfers scheduled for today are reported
            assertEquals(2, executedTransfers.length, "Should have executed exactly 2 transfers");
            assertTrue(output.contains("Executed transfer: Transfer from account1 to account2 of 100.0 on " + today),
                    "First transfer should be executed");
            assertTrue(output.contains("Executed transfer: Transfer from account3 to account4 of 200.0 on " + today),
                    "Second transfer should be executed");

            // Verify remaining transfers
            List<ScheduledTransfer.Transfer> remainingTransfers = scheduledTransfer.getScheduledTransfers();
            assertEquals(2, remainingTransfers.size(), "Should have 2 transfers remaining");

            // Verify remaining transfers are for future and past dates
            assertTrue(remainingTransfers.stream()
                            .anyMatch(t -> t.date.equals(today.plusDays(1))),
                    "Future transfer should remain");
            assertTrue(remainingTransfers.stream()
                            .anyMatch(t -> t.date.equals(today.minusDays(1))),
                    "Past transfer should remain");

        } catch (Exception e) {
            fail("Test setup failed: " + e.getMessage());
        }
    }

    // Helper method to create Transfer using reflection to bypass validation
    private ScheduledTransfer.Transfer createTransfer(String fromAccount, String toAccount, double amount, LocalDate date) throws Exception {
        Constructor<ScheduledTransfer.Transfer> constructor =
                (Constructor<ScheduledTransfer.Transfer>) ScheduledTransfer.class.getDeclaredClasses()[0].getDeclaredConstructor(
                        String.class, String.class, double.class, LocalDate.class
                );
        constructor.setAccessible(true);
        return constructor.newInstance(fromAccount, toAccount, amount, date);
    }
}
