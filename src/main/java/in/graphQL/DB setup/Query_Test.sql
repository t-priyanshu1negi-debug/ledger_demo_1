use lms_gl;

SELECT * FROM gl_account;

# Check your journals and their debit/credit totals:

SELECT
    j.journal_number,
    j.journal_status,
    SUM(l.debit_amount) AS total_debit,
    SUM(l.credit_amount) AS total_credit
FROM gl_journal j
JOIN gl_journal_line l ON l.journal_id = j.journal_id
GROUP BY j.journal_id, j.journal_number, j.journal_status;

# check account balance
SELECT
    a.account_code,
    a.account_name,
    b.opening_balance,
    b.total_debit,
    b.total_credit,
    b.closing_balance
FROM gl_account_balance b
JOIN gl_account a ON a.account_id = b.account_id;



#-------POSTMAN BEFORE-------------


SELECT *
FROM gl_account
WHERE account_code = '1100';

-- Lines per account and status
SELECT a.account_code, j.journal_status, COUNT(*) AS cnt
FROM gl_journal_line l
JOIN gl_account a ON l.account_id = a.account_id
JOIN gl_journal j ON l.journal_id = j.journal_id
GROUP BY a.account_code, j.journal_status;

-- What POSTED data exists (should be non-empty after UPDATE)
SELECT a.account_code, j.journal_number, j.journal_status
FROM gl_journal_line l
JOIN gl_account a ON l.account_id = a.account_id
JOIN gl_journal j ON l.journal_id = j.journal_id
WHERE j.journal_status = 'POSTED';

SELECT
    jl.*,
    j.journal_number,
    j.transaction_date,
    j.posting_date,
    j.journal_status,
    a.account_code,
    a.account_name
FROM gl_journal_line jl
JOIN gl_journal j
    ON jl.journal_id = j.journal_id
JOIN gl_account a
    ON jl.account_id = a.account_id
WHERE a.account_code = '1100'
  AND j.journal_status = 'POSTED'
ORDER BY
    j.posting_date,
    j.journal_number,
    jl.line_number;