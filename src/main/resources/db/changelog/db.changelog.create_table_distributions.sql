create table if not exists application.distributions
(
    id bigserial primary key,
    time_created timestamp with time zone not null,
    id_game bigint
        references application.games(id) not null,
    player_sender bigint
        references application.players(id) not null unique,
    player_target bigint
        references application.players(id) not null unique
)