package in.graphQL.service;

import in.graphQL.dto.GlAccountResponse;
import in.graphQL.repository.GlAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class GlAccountService {

    private final GlAccountRepository glAccountRepository;

    public GlAccountService(GlAccountRepository glAccountRepository) {
        this.glAccountRepository = glAccountRepository;
    }

    @Transactional(readOnly = true)
    public List<GlAccountResponse> findAll() {
        return glAccountRepository.findAll()
                .stream()
                .map(GlAccountResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<GlAccountResponse> findById(Long accountId) {
        return glAccountRepository.findById(accountId)
                .map(GlAccountResponse::from);
    }

    @Transactional(readOnly = true)
    public Optional<GlAccountResponse> findByAccountCode(String accountCode) {
        return glAccountRepository.findByAccountCode(accountCode)
                .map(GlAccountResponse::from);
    }
}
