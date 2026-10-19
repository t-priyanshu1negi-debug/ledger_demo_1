
USE lms_gl;

INSERT INTO gl_accounting_period
    (period_name, start_date, end_date, period_status)
VALUES
    ('OCT-2026', '2026-10-01', '2026-10-31', 'OPEN'),
    ('NOV-2026', '2026-11-01', '2026-11-30', 'OPEN');



INSERT INTO gl_account
    (account_code, account_name, account_type, normal_balance, currency_code)
VALUES
    ('1100', 'Loan Principal Receivable', 'ASSET', 'DEBIT', 'INR'),
    ('1110', 'Interest Receivable',        'ASSET', 'DEBIT', 'INR'),
    ('1200', 'Bank Account',               'ASSET', 'DEBIT', 'INR'),
    ('2100', 'Interest Payable',           'LIABILITY', 'CREDIT', 'INR'),
    ('4100', 'Interest Income',            'INCOME', 'CREDIT', 'INR'),
    ('4200', 'Processing Fee Income',      'INCOME', 'CREDIT', 'INR'),
    ('5100', 'Loan Loss Expense',          'EXPENSE', 'DEBIT', 'INR'),
    ('5200', 'Penalty Income',             'INCOME', 'CREDIT', 'INR');



INSERT INTO gl_posting_rule
    (product_code, event_type, component_type,debit_account_id, credit_account_id, is_active)
VALUES
    ('PERSONAL_LOAN', 'LOAN_DISBURSEMENT', 'PRINCIPAL',
     (SELECT account_id FROM gl_account WHERE account_code = '1100'),
     (SELECT account_id FROM gl_account WHERE account_code = '1200'), TRUE),

    ('PERSONAL_LOAN', 'LOAN_REPAYMENT', 'PRINCIPAL',
     (SELECT account_id FROM gl_account WHERE account_code = '1200'),
     (SELECT account_id FROM gl_account WHERE account_code = '1100'), TRUE),

    ('PERSONAL_LOAN', 'INTEREST_COLLECTION', 'INTEREST',
     (SELECT account_id FROM gl_account WHERE account_code = '1200'),
     (SELECT account_id FROM gl_account WHERE account_code = '4100'), TRUE),

    ('PERSONAL_LOAN', 'FEE_COLLECTION', 'PROCESSING_FEE',
     (SELECT account_id FROM gl_account WHERE account_code = '1200'),
     (SELECT account_id FROM gl_account WHERE account_code = '4200'), TRUE);
    
    
    
INSERT INTO gl_journal
    (journal_number, source_module, source_event,source_reference, loan_id, transaction_date,posting_date, currency_code, description, journal_status)
VALUES
    ('JV-2026-00001', 'LMS', 'LOAN_DISBURSEMENT',
     'DISB-1001', 1001, '2026-10-16',
     '2026-10-16', 'INR',
     'Loan principal disbursement', 'DRAFT');



SET @journal_id = (
    SELECT journal_id
    FROM gl_journal
    WHERE journal_number = 'JV-2026-00001'
);

INSERT INTO gl_journal_line
    (journal_id, line_number, account_id,
     debit_amount, credit_amount, line_description)
VALUES
    (@journal_id, 1,
     (SELECT account_id FROM gl_account WHERE account_code = '1100'),
     100000, 0, 'Loan principal receivable'),

    (@journal_id, 2,
     (SELECT account_id FROM gl_account WHERE account_code = '1200'),
     0, 100000, 'Loan disbursement from bank');
    
    
INSERT INTO gl_account_balance
    (account_id, period_id, currency_code,
     opening_balance, total_debit, total_credit, closing_balance)
VALUES
    (
        (SELECT account_id FROM gl_account WHERE account_code = '1200'),
        (SELECT period_id FROM gl_accounting_period
         WHERE period_name = 'OCT-2026'),
        'INR', 500000, 0, 100000, 400000
    ),
    (
        (SELECT account_id FROM gl_account WHERE account_code = '1100'),
        (SELECT period_id FROM gl_accounting_period
         WHERE period_name = 'OCT-2026'),
        'INR', 0, 100000, 0, 100000
    );