# Demo LMS General Ledger (GL)

A demo backend General Ledger service for a Loan Management System (LMS), built with **Spring Boot**, **GraphQL** and **MySQL**.

> Status: in development (last updated 16 October 2026). This project is a learning and demo build and is not related to the Airtime PRD/HLD.

---

## Scope

The first version covers two use cases:

1. **Account Ledger**: the posted transaction history for a single GL account.
2. **General Ledger Reports**: summary views across accounts (planned).

---

## Tech stack

| Layer | Technology |
|---|---|
| Language / framework | Java, Spring Boot |
| API | Spring for GraphQL |
| Persistence | Spring Data JPA (Hibernate) |
| Database | MySQL (`lms_gl`) |
| API testing | Postman |

---

## Database

Database name: `lms_gl`

| Table | Purpose |
|---|---|
| `gl_account` | Chart of accounts (for example Loan Principal Receivable or Bank Account) |
| `gl_accounting_period` | Accounting periods and their status (open or closed) |
| `gl_journal` | Journal headers: one row per accounting transaction |
| `gl_journal_line` | Debit and credit lines for each journal |
| `gl_posting_rule` | Mapping from LMS events to GL accounts |
| `gl_account_balance` | Account balances by period and currency |


## Project structure

```
src/main/java/.../
├── entity/
│   ├── GlAccount.java
│   ├── GlJournal.java
│   └── GlJournalLine.java
├── repository/
│   ├── GlAccountRepository.java
│   ├── GlJournalRepository.java
│   └── GlJournalLineRepository.java
├── dto/
│   └── LedgerEntryResponse.java
├── service/
│   └── GlLedgerService.java
└── controller/
    └── GlLedgerController.java

src/main/resources/
├── application.properties
└── graphql/
    └── schema.graphqls
```

### Layers

- **Entities** map tables to Java objects using `@Entity`, `@Table`, `@Id`, `@GeneratedValue`, `@Column`, `@Enumerated`, `@ManyToOne` and `@JoinColumn`. All money amounts use `BigDecimal`.
- **Repositories**: `GlJournalLineRepository` has a JPQL query that fetches the journal lines for one account, joins their journal and account, keeps only journals with status `POSTED`, and sorts by posting date and journal number.
- **Service**: `GlLedgerService` loads the journal lines and maps them to `LedgerEntryResponse` DTOs.
- **Controller**: `GlLedgerController` exposes the `accountLedger` query using `@QueryMapping` and `@Argument`.

---

## Running locally

```bash
# 1. Create the database
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS lms_gl;"

# 2. Start the app
./mvnw spring-boot:run      # or: ./gradlew bootRun
```

The app starts on `http://localhost:8080`.

---

## GraphQL API

**Endpoint:** `POST http://localhost:8080/graphql`

The schema lives in `src/main/resources/graphql/schema.graphqls` and defines the `accountLedger` query and the `LedgerEntry` type.

### Example request (Postman)

Body as raw JSON. Adjust the argument and field names to match `schema.graphqls`:

```json
{
  "query": "query { accountLedger(accountCode: \"1100\") { journalNumber postingDate transactionDate description debit credit } }"
}
```

### Expected response

```json
{
  "data": {
    "accountLedger": [
      {
        "journalNumber": "JRN-0001",
        "postingDate": "2026-10-16",
        "transactionDate": "2026-10-16",
        "description": "Loan disbursement",
        "debit": 1000.00,
        "credit": 0.00
      }
    ]
  }
}
```

The values above are example data only.

---

## Troubleshooting: `200 OK` with an empty list

The `accountLedger` query only returns lines that meet all of these conditions:

1. The account exists in `gl_account` with the code you passed (for example `1100`).
2. Journal lines in `gl_journal_line` reference that account.
3. Those lines belong to a journal in `gl_journal` whose status is `POSTED`.

Check each condition directly in MySQL. Adjust the column names to match your schema:

```sql
-- 1. Does the account exist?
SELECT * FROM gl_account WHERE account_code = '1100';

-- 2. Are there journal lines for it?
SELECT jl.*
FROM gl_journal_line jl
JOIN gl_account a ON a.id = jl.account_id
WHERE a.account_code = '1100';

-- 3. Are their journals POSTED?
SELECT j.journal_number, j.status, j.posting_date
FROM gl_journal_line jl
JOIN gl_journal j ON j.id = jl.journal_id
JOIN gl_account a ON a.id = jl.account_id
WHERE a.account_code = '1100';
```

If step 3 shows `DRAFT` or another status, either post the journal or insert test data with status `POSTED`.

---

## Accounting concepts

- **GL Account**: an accounting category, such as Loan Principal Receivable or Bank Account.
- **Journal**: one accounting transaction.
- **Journal Line**: a single debit or credit against one account.
- **Account Ledger**: the posted transaction history for one GL account.
- **Debit and Credit**: every balanced journal has equal total debits and credits.
- **Posting Date vs Transaction Date**: the business transaction date can differ from the date it is posted in the books.

---

## Progress

- [x] MySQL `lms_gl` configured and connected
- [x] Entities: `GlAccount`, `GlJournal`, `GlJournalLine`
- [x] Repositories, with the JPQL account-ledger query and an equivalent SQL version
- [x] GraphQL schema, DTO, service and controller for `accountLedger`
- [x] Endpoint tested in Postman
- [ ] Fix the empty-list response for account `1100` (verify there are lines linked to `POSTED` journals)
- [ ] Entities and repositories for `gl_accounting_period`, `gl_posting_rule` and `gl_account_balance`
- [ ] General Ledger Reports (trial balance, account balances by period)
