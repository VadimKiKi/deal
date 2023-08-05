create table application
(
    application_id bigint primary key generated by default as identity,
    client_id      bigint references client (client_id) on delete cascade,
    credit_id      bigint references credit (credit_id) on delete cascade,
    status         varchar,
    creation_date  date,
    applied_offer  jsonb,
    sign_date      date,
    ses_code       int,
    status_history jsonb
)