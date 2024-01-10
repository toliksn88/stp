create table if not exists application.players
(
    id bigserial primary key,
    necessary_things varchar(1000),
    unnecessary_things varchar(1000),
    time_registration timestamp with time zone not null,
    id_game bigint
        references application.games(id) not null,
    id_profile bigint
        references application.profiles(id) not null
)