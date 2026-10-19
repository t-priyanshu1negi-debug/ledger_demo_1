
package in.graphQL.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "gl_journal_line")
public class GlJournalLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "journal_line_id")
    private Long journalLineId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_id", nullable = false)
    private GlJournal journal;

    @Column(name = "line_number", nullable = false)
    private Integer lineNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private GlAccount account;

    @Column(name = "debit_amount", precision = 19, scale = 4)
    private BigDecimal debitAmount;

    @Column(name = "credit_amount", precision = 19, scale = 4)
    private BigDecimal creditAmount;

    @Column(name = "line_description")
    private String lineDescription;

    public GlJournalLine() {
    }

    public Long getJournalLineId() {
        return journalLineId;
    }

    public GlJournal getJournal() {
        return journal;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public GlAccount getAccount() {
        return account;
    }

    public BigDecimal getDebitAmount() {
        return debitAmount;
    }

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    public String getLineDescription() {
        return lineDescription;
    }
}
