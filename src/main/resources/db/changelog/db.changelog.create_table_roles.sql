create table if not exists application.roles
(
    id bigserial primary key,
    role_name varchar(255) unique not null
)