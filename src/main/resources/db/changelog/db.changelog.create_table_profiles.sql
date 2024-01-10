create table if not exists application.profiles
(
    id bigserial primary key,
    name varchar(255),
    id_account bigint
        references application.accounts(id) not null unique,
    id_address bigint
        references application.addresses(id)
)