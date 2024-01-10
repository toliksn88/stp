create table if not exists application.games
(
    id bigserial primary key,
    description varchar(500),
    name_game varchar(255) not null unique,
    time_created timestamp with time zone not null,
    time_start timestamp with time zone,
    status_game varchar(20) not null,
    type_game varchar(20) not null,
    password varchar(255),
    time_end timestamp with time zone,
    organizer bigint
        references application.profiles(id) not null
)