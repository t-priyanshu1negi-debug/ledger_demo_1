package in.graphQL.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "gl_journal")
public class GlJournal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "journal_id")
    private Long journalId;

    @Column(name = "journal_number", nullable = false, unique = true)
    private String journalNumber;

    @Column(name = "source_module")
    private String sourceModule;

    @Column(name = "source_event")
    private String sourceEvent;

    @Column(name = "source_reference")
    private String sourceReference;

    @Column(name = "loan_id")
    private Long loanId;

    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    @Column(name = "posting_date", nullable = false)
    private LocalDate postingDate;

    @Column(name = "currency_code", nullable = false)
    private String currencyCode;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "journal_status", nullable = false)
    private JournalStatus journalStatus;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "posted_at")
    private LocalDateTime postedAt;

    public GlJournal() {
    }

    public Long getJournalId() {
        return journalId;
    }

    public String getJournalNumber() {
        return journalNumber;
    }

    public String getSourceModule() {
        return sourceModule;
    }

    public String getSourceEvent() {
        return sourceEvent;
    }

    public String getSourceReference() {
        return sourceReference;
    }

    public Long getLoanId() {
        return loanId;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public LocalDate getPostingDate() {
        return postingDate;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getDescription() {
        return description;
    }

    public JournalStatus getJournalStatus() {
        return journalStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getPostedAt() {
        return postedAt;
    }

    public enum JournalStatus {
        DRAFT, POSTED, REVERSED
    }
}
