
package in.graphQL.repository;

import in.graphQL.entity.GlJournalLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface GlJournalLineRepository
        extends JpaRepository<GlJournalLine, Long> {

    @Query("""
        SELECT line
        FROM GlJournalLine line
        JOIN FETCH line.journal journal
        JOIN FETCH line.account account
        WHERE account.accountCode = :accountCode
        ORDER BY journal.postingDate, journal.journalNumber, line.lineNumber
        """)
    List<GlJournalLine> findAllLedgerEntries(
            @Param("accountCode") String accountCode
    );

    @Query("""
        SELECT line
        FROM GlJournalLine line
        JOIN line.journal journal
        JOIN line.account account
        WHERE account.accountCode = :accountCode
          AND journal.transactionDate >= :fromDate
          AND journal.transactionDate <= :toDate
        ORDER BY journal.transactionDate ASC
    """)
    List<GlJournalLine> findReportRecords(
            @Param("accountCode") String accountCode,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate
    );
}
