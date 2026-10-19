package in.graphQL.controller;

import in.graphQL.dto.GlAccountResponse;
import in.graphQL.service.GlAccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/gl-accounts")
public class GlAccountController {

    private final GlAccountService glAccountService;

    public GlAccountController(GlAccountService glAccountService) {
        this.glAccountService = glAccountService;
    }

    @GetMapping
    public List<GlAccountResponse> getAllAccounts() {
        return glAccountService.findAll();
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<GlAccountResponse> getAccountById(
            @PathVariable Long accountId) {
        return glAccountService.findById(accountId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/code/{accountCode}")
    public ResponseEntity<GlAccountResponse> getAccountByCode(
            @PathVariable String accountCode) {
        return glAccountService.findByAccountCode(accountCode)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
