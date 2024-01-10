create table if not exists application.addresses
(
    id bigserial primary key,
    country varchar(255),
    city varchar(255),
    street varchar(255),
    number_house varchar(255),
    number_apartment varchar(255)
)