package in.graphQL.controller;

import in.graphQL.dto.LedgerEntryResponse;
import in.graphQL.service.GlLedgerService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class GlLedgerController {

    private final GlLedgerService glLedgerService;

    public GlLedgerController(GlLedgerService glLedgerService) {
        this.glLedgerService = glLedgerService;
    }

    @QueryMapping
    public List<LedgerEntryResponse> accountLedger(
            @Argument String accountCode) {

        return glLedgerService.getAccountLedger(accountCode);
    }
}
