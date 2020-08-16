create sequence hibernate_sequence;

create table users
(
    id       bigint primary key,
    password varchar(255) not null,
    email    varchar(255)
    constraint uk_users_email
            unique
);

create table accounts
(
    id      bigint primary key,
    balance numeric(19, 2),
    user_id bigint
        constraint fk_account_user references users (id)
);

create index index_acounts_user_id
    on accounts(user_id);

create table bank_transactions
(
    id               bigint primary key,
    amount           numeric(19, 2),
    transaction_type varchar(10),
    created          timestamp default current_timestamp,
    account_id       bigint
        constraint fk_transaction_account references accounts (id)
);

create index index_bank_transactions_acount_id
    on bank_transactions(account_id);
