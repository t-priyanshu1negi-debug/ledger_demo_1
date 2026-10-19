show databases;

CREATE DATABASE lms_gl;
USE lms_gl;

# Configuration: gl_account, gl_accounting_period, gl_posting_rule
#Transaction recording: gl_journal, gl_journal_line
#Reporting: gl_account_balance


# ---Stores the Chart of Accounts, including account codes, names, types, and normal debit/credit balances.---------------------------
CREATE TABLE gl_account (
    account_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_code VARCHAR(30) NOT NULL UNIQUE,
    account_name VARCHAR(150) NOT NULL,
    account_type ENUM(
        'ASSET',
        'LIABILITY',
        'EQUITY',
        'INCOME',
        'EXPENSE'
    ) NOT NULL,
    parent_account_id BIGINT NULL,
    normal_balance ENUM('DEBIT', 'CREDIT') NOT NULL,
    currency_code CHAR(3) NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_gl_account_parent
        FOREIGN KEY (parent_account_id)
        REFERENCES gl_account(account_id)
);



# ------Defines accounting periods, their start and end dates, and whether posting is open or closed.-------------------------------------

CREATE TABLE gl_accounting_period (
    period_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    period_name VARCHAR(20) NOT NULL UNIQUE,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    period_status ENUM('OPEN', 'CLOSED')
        NOT NULL DEFAULT 'OPEN',

    CONSTRAINT chk_gl_period_dates
        CHECK (end_date >= start_date)
);

# ----Stores the journal header for each LMS financial event, such as loan disbursement or repayment.-------
CREATE TABLE gl_journal (
    journal_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    journal_number VARCHAR(40) NOT NULL UNIQUE,
    source_module VARCHAR(30) NOT NULL,
    source_event VARCHAR(40) NOT NULL,
    source_reference VARCHAR(100) NOT NULL,
    loan_id BIGINT NULL,
    transaction_date DATE NOT NULL,
    posting_date DATE NOT NULL,
    currency_code CHAR(3) NOT NULL,
    description VARCHAR(500),
    journal_status ENUM('DRAFT', 'POSTED', 'REVERSED')
        NOT NULL DEFAULT 'DRAFT',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    posted_at TIMESTAMP NULL,

    UNIQUE KEY uq_gl_source_event (
        source_module,
        source_event,
        source_reference
    ),
    INDEX idx_gl_journal_loan (loan_id),
    INDEX idx_gl_journal_posting_date (posting_date)
);

#---Stores the individual debit and credit entries associated with each journal.-------
CREATE TABLE gl_journal_line (
    journal_line_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    journal_id BIGINT NOT NULL,
    line_number INT NOT NULL,
    account_id BIGINT NOT NULL,
    debit_amount DECIMAL(19,4) NOT NULL DEFAULT 0,
    credit_amount DECIMAL(19,4) NOT NULL DEFAULT 0,
    line_description VARCHAR(300),

    UNIQUE KEY uq_gl_journal_line (
        journal_id,
        line_number
    ),

    CONSTRAINT fk_gl_line_journal
        FOREIGN KEY (journal_id)
        REFERENCES gl_journal(journal_id),

    CONSTRAINT fk_gl_line_account
        FOREIGN KEY (account_id)
        REFERENCES gl_account(account_id),

    CONSTRAINT chk_gl_line_amounts
        CHECK (
            debit_amount >= 0
            AND credit_amount >= 0
            AND (
                (debit_amount > 0 AND credit_amount = 0)
                OR
                (credit_amount > 0 AND debit_amount = 0)
            )
        )
);

# ----Defines which GL accounts should be debited and credited for each loan product, event, and component.-----
CREATE TABLE gl_posting_rule (
    rule_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_code VARCHAR(30) NOT NULL,
    event_type VARCHAR(40) NOT NULL,
    component_type VARCHAR(30) NOT NULL,
    debit_account_id BIGINT NOT NULL,
    credit_account_id BIGINT NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_gl_rule_debit
        FOREIGN KEY (debit_account_id)
        REFERENCES gl_account(account_id),

    CONSTRAINT fk_gl_rule_credit
        FOREIGN KEY (credit_account_id)
        REFERENCES gl_account(account_id),

    UNIQUE KEY uq_gl_posting_rule (
        product_code,
        event_type,
        component_type
    )
);


#--Stores account balances by accounting period and currency for faster financial reporting.-----
CREATE TABLE gl_account_balance (
    balance_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_id BIGINT NOT NULL,
    period_id BIGINT NOT NULL,
    currency_code CHAR(3) NOT NULL,
    opening_balance DECIMAL(19,4) NOT NULL DEFAULT 0,
    total_debit DECIMAL(19,4) NOT NULL DEFAULT 0,
    total_credit DECIMAL(19,4) NOT NULL DEFAULT 0,
    closing_balance DECIMAL(19,4) NOT NULL DEFAULT 0,

    CONSTRAINT fk_gl_balance_account
        FOREIGN KEY (account_id)
        REFERENCES gl_account(account_id),

    CONSTRAINT fk_gl_balance_period
        FOREIGN KEY (period_id)
        REFERENCES gl_accounting_period(period_id),

    UNIQUE KEY uq_gl_account_period (
        account_id,
        period_id,
        currency_code
    )
);