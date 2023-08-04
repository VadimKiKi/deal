create table credit
(
    credit_id        bigint primary key generated BY DEFAULT AS IDENTITY,
    amount           decimal not null check ( amount > 0),
    term             int     not null check ( term > 0 ),
    monthly_payment  decimal not null check ( monthly_payment > 0),
    rate             decimal not null check ( rate > 0 ),
    psk              decimal not null check ( psk > 0 ),
    payment_schedule jsonb   not null,
    insurance_enable boolean not null,
    salary_client    boolean not null,
    credit_status    varchar not null
)