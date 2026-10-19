
package in.graphQL.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LedgerEntryResponse(
        String journalNumber,
        LocalDate transactionDate,
        LocalDate postingDate,
        String accountCode,
        String accountName,
        String description,
        BigDecimal debitAmount,
        BigDecimal creditAmount
) {}
