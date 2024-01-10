create table if not exists application.accounts
(
    id bigserial primary key,
    date_created timestamp with time zone not null,
    email varchar(255) not null unique,
    password varchar(255) not null
)
