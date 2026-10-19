package in.graphQL.dto;

import java.math.BigDecimal;
import java.util.List;

public record GeneralLedgerReportResponse(
        String accountCode,
        BigDecimal totalDebit,
        BigDecimal totalCredit,
        BigDecimal closingBalance,
        List<JournalLineResponse> records
) {}