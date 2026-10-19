
package in.graphQL.service;

import in.graphQL.dto.LedgerEntryResponse;
import in.graphQL.entity.GlJournalLine;
import in.graphQL.repository.GlJournalLineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class GlLedgerService {

    private final GlJournalLineRepository journalLineRepository;

    public GlLedgerService(
            GlJournalLineRepository journalLineRepository) {
        this.journalLineRepository = journalLineRepository;
    }

    @Transactional(readOnly = true)
    public List<LedgerEntryResponse> getAccountLedger(
            String accountCode) {

        return journalLineRepository
                .findAllLedgerEntries(accountCode)
                .stream()
                .map(line -> mapToResponse(line, accountCode))
                .toList();
    }

    private LedgerEntryResponse mapToResponse(
            GlJournalLine line,
            String accountCode) {

        var journal = line.getJournal();
        var account = line.getAccount();

        return new LedgerEntryResponse(
                journal.getJournalNumber(),
                journal.getTransactionDate(),
                journal.getPostingDate(),
                account.getAccountCode(),
                account.getAccountName(),
                line.getLineDescription() != null
                        ? line.getLineDescription()
                        : journal.getDescription(),
                line.getDebitAmount() != null
                        ? line.getDebitAmount()
                        : BigDecimal.ZERO,
                line.getCreditAmount() != null
                        ? line.getCreditAmount()
                        : BigDecimal.ZERO
        );
    }
}

