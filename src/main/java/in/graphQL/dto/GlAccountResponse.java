package in.graphQL.dto;

import in.graphQL.entity.GlAccount;

import java.time.LocalDateTime;

public record GlAccountResponse(
        Long accountId,
        String accountCode,
        String accountName,
        GlAccount.AccountType accountType,
        GlAccount.NormalBalance normalBalance,
        String currencyCode,
        Boolean active,
        LocalDateTime createdAt
) {
    public static GlAccountResponse from(GlAccount account) {
        return new GlAccountResponse(
                account.getAccountId(),
                account.getAccountCode(),
                account.getAccountName(),
                account.getAccountType(),
                account.getNormalBalance(),
                account.getCurrencyCode(),
                account.getActive(),
                account.getCreatedAt()
        );
    }
}
