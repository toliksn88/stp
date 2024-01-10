create table if not exists application.accounts_roles
(
    id bigserial primary key,
    id_account bigint references application.accounts(id),
    id_role bigint references application.roles(id)
)