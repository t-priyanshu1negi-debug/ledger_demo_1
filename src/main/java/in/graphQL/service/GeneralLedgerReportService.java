package in.graphQL.service;

import in.graphQL.dto.GeneralLedgerReportResponse;
import in.graphQL.dto.JournalLineResponse;
import in.graphQL.entity.GlJournalLine;
import in.graphQL.repository.GlJournalLineRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class GeneralLedgerReportService {

    private final GlJournalLineRepository repository;

    public GeneralLedgerReportService(
            GlJournalLineRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public GeneralLedgerReportResponse getReport(
            String accountCode,
            LocalDate fromDate,
            LocalDate toDate) {

        if (fromDate.isAfter(toDate)) {
            throw new IllegalArgumentException(
                    "fromDate cannot be after toDate");
        }

        List<GlJournalLine> lines =
                repository.findReportRecords(
                        accountCode, fromDate, toDate);

        BigDecimal totalDebit = BigDecimal.ZERO;
        BigDecimal totalCredit = BigDecimal.ZERO;

        List<JournalLineResponse> records = lines.stream()
                .map(line -> new JournalLineResponse(
                        line.getJournalLineId(),
                        line.getJournal().getTransactionDate(),
                        line.getDebitAmount(),
                        line.getCreditAmount()
                ))
                .toList();

        for (JournalLineResponse record : records) {
            totalDebit = totalDebit.add(record.debit());
            totalCredit = totalCredit.add(record.credit());
        }

        // Temporary: this calculates net movement, not the
        // actual closing balance including the opening balance.
        BigDecimal closingBalance =
                totalDebit.subtract(totalCredit);

        return new GeneralLedgerReportResponse(
                accountCode,
                totalDebit,
                totalCredit,
                closingBalance,
                records
        );
    }
}