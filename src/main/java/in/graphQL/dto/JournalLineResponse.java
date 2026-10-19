package in.graphQL.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import in.graphQL.entity.GlJournalLine;

public record JournalLineResponse(
        Long id,
        LocalDate transactionDate,
        BigDecimal debit,
        BigDecimal credit
) {
    public static JournalLineResponse from(GlJournalLine line) {
        return new JournalLineResponse(
                line.getJournalLineId(),
                line.getJournal().getTransactionDate(),
                line.getDebitAmount(),
                line.getCreditAmount()
        );
    }
}