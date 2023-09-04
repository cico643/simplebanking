create table if not exists bank_account
(
    account_number varchar(255) not null primary key,
    balance        numeric(38, 2),
    create_date    timestamp(6),
    owner          varchar(255) not null
);

create table if not exists transaction
(
    type           varchar(31)  not null,
    approval_code  varchar(255) not null primary key,
    amount         numeric(38, 2),
    date           timestamp(6),
    payee          varchar(255),
    phone_number   varchar(10),
    account_number varchar(255) not null
        constraint fkm56ljtl5gvksqa5hosde27j6q
            references bank_account
);