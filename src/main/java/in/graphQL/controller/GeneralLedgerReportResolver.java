package in.graphQL.controller;

import in.graphQL.dto.GeneralLedgerReportResponse;
import in.graphQL.service.GeneralLedgerReportService;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;

@Controller
public class GeneralLedgerReportResolver {

    private final GeneralLedgerReportService reportService;

    public GeneralLedgerReportResolver(
            GeneralLedgerReportService reportService) {
        this.reportService = reportService;
    }

    @QueryMapping
    public GeneralLedgerReportResponse generalLedgerReport(
            @Argument String accountCode,
            @Argument String fromDate,
            @Argument String toDate) {

        return reportService.getReport(
                accountCode,
                LocalDate.parse(fromDate),
                LocalDate.parse(toDate)
        );
    }
}
