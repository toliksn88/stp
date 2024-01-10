package com.innowise.secret_santa.constants;

import com.innowise.secret_santa.model.RoleEnum;
import com.innowise.secret_santa.model.SettingRolesEnum;
import com.innowise.secret_santa.model.StatusGame;
import com.innowise.secret_santa.model.TypeGame;
import com.innowise.secret_santa.model.TypeMessage;
import com.innowise.secret_santa.model.dto.AccountDto;
import com.innowise.secret_santa.model.dto.AddressDto;
import com.innowise.secret_santa.model.dto.ProfileDto;
import com.innowise.secret_santa.model.dto.RoleDto;
import com.innowise.secret_santa.model.dto.request_dto.AccountChangePassword;
import com.innowise.secret_santa.model.dto.request_dto.GameRegistration;
import com.innowise.secret_santa.model.dto.request_dto.GameRequestDto;
import com.innowise.secret_santa.model.dto.request_dto.PagesDto;
import com.innowise.secret_santa.model.dto.request_dto.PlayerRequestDto;
import com.innowise.secret_santa.model.dto.request_dto.RegistrationLoginAccount;
import com.innowise.secret_santa.model.dto.request_dto.SentMessageDto;
import com.innowise.secret_santa.model.dto.response_dto.AccountAuthenticationResponse;
import com.innowise.secret_santa.model.dto.response_dto.DistributionResponseDto;
import com.innowise.secret_santa.model.dto.response_dto.GameResponseDto;
import com.innowise.secret_santa.model.dto.response_dto.PagesDtoResponse;
import com.innowise.secret_santa.model.dto.response_dto.PlayerResponseDto;
import com.innowise.secret_santa.model.dto.response_dto.ProfileOrganizer;
import com.innowise.secret_santa.model.mongo.SentMessage;
import com.innowise.secret_santa.model.mongo.SystemMessage;
import com.innowise.secret_santa.model.postgres.Account;
import com.innowise.secret_santa.model.postgres.Address;
import com.innowise.secret_santa.model.postgres.Distribution;
import com.innowise.secret_santa.model.postgres.Game;
import com.innowise.secret_santa.model.postgres.Player;
import com.innowise.secret_santa.model.postgres.Profile;
import com.innowise.secret_santa.model.postgres.Role;
import com.innowise.secret_santa.util.CalendarUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.mail.SimpleMailMessage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public final class TestConstants {

    private TestConstants() {
    }

    public static final String EMAIL = "mail@gmail.com";
    public static final String ENCODE_PASSWORD = "encode_password";
    public static final String PASSWORD = "password";
    public static final String OLD_PASSWORD = "old_password";
    public static final Long ID = 1L;
    public static final RoleEnum ROLE_ENUM_USER = RoleEnum.ROLE_USER;
    public static final RoleEnum ROLE_ENUM_ORGANIZER = RoleEnum.ROLE_ORGANIZER;
    public static final RoleEnum ROLE_ENUM_PLAYER = RoleEnum.ROLE_PLAYER;
    public static final LocalDateTime DATE = CalendarUtils.getFormatDate(LocalDateTime.now());
    public static final String COUNTRY = "country";
    public static final String STREET = "street";
    public static final String CITY = "city";
    public static final String NUMBER_HOUSE = "1";
    public static final String NUMBER_APARTMENT = "1";
    public static final String NAME = "name";
    public static final TypeMessage TYPE_MESSAGE_CREATE = TypeMessage.CREATE;
    public static final TypeMessage TYPE_MESSAGE_DISTRIBUTION = TypeMessage.DISTRIBUTION;
    public static final TypeMessage TYPE_MESSAGE_DELETE = TypeMessage.DELETE;
    public static final TypeMessage TYPE_MESSAGE_CHANGE = TypeMessage.CHANGE_PASSWORD;
    public static final String LOGGER_MESSAGE_SENT_MESSAGE = "Save sent message for account with id: {}";
    public static final String LOGGER_MESSAGE_CREATE_ACCOUNT = "Account by email {} successful registration";
    public static final String LOGGER_MESSAGE_DELETE_ACCOUNT = "Account by id {} delete";
    public static final String LOGGER_MESSAGE_CHANGE_PASSWORD_ACCOUNT = "Account {} changed password";
    public static final String LOGGER_MESSAGE_CHANGE_ROLE_ACCOUNT = "Account by id {0} set role - {1}";
    public static final String LOGGER_MESSAGE_ABOUT_DISTRIBUTION = "Distribution for game: {0} finished";
    public static final String LOGGER_MESSAGE_ABOUT_CHANGE_STATUS_GAME = "Game with name: {}, update status successful";
    public static final String LOGGER_MESSAGE_CREATED_PROFILE = "Account by id: {}, created profile";
    public static final String LOGGER_MESSAGE_DELETE_PROFILE = "Profile delete for account by id: ";
    public static final String LOGGER_MESSAGE_CREATED_PLAYER = "Account by id: {}, created player";
    public static final String LOGGER_MESSAGE_GET_ALL_DISTRIBUTIONS_FOR_ORGANIZER = "Account by id: {0}, get all distributions for game: {1}";
    public static final String LOGGER_MESSAGE_DELETE_PLAYER = "Account by id: {0} deleted from game: {1}";
    public static final String LOGGER_MESSAGE_INFO_DISTRIBUTIONS = "Player: {0} have to present player: {1} from game: {2}";
    public static final String LOGGER_MESSAGE_ACCOUNT_EMAIL = "Account by email {}, get email about your player";
    public static final String LOGGER_MESSAGE_LOOK_DISTRIBUTION = "Account by id: {} look your distributions";
    public static final String LOGGER_MESSAGE_LOOK_ACCOUNT_DISTRIBUTION = "Account by id: {0} look your distributions for game: {1}";
    public static final SettingRolesEnum SETTING_ROLES_ENUM_ADD = SettingRolesEnum.ADD;
    public static final SettingRolesEnum SETTING_ROLES_ENUM_DELETE = SettingRolesEnum.DELETE;
    public static final String NAME_GAME = "name_game";

    public static final RoleDto ROLE_DTO =
            RoleDto.builder()
                    .roleName(ROLE_ENUM_USER)
                    .build();

    public static final Role ROLE_USER =
            Role.builder()
                    .id(ID)
                    .roleName(ROLE_ENUM_USER)
                    .build();
    public static final Role ROLE_PLAYER =
            Role.builder()
                    .id(ID)
                    .roleName(ROLE_ENUM_PLAYER)
                    .build();

    public static final Address ADDRESS =
            Address.builder()
                    .id(ID)
                    .country(COUNTRY)
                    .city(CITY)
                    .street(STREET)
                    .numberHouse(NUMBER_HOUSE)
                    .numberApartment(NUMBER_APARTMENT)
                    .build();
    public static final AddressDto ADDRESS_DTO =
            AddressDto.builder()
                    .country(COUNTRY)
                    .city(CITY)
                    .street(STREET)
                    .numberHouse(NUMBER_HOUSE)
                    .numberApartment(NUMBER_APARTMENT)
                    .build();

    public static final Profile PROFILE_WITHOUT_ACCOUNT =
            Profile.builder()
                    .id(ID)
                    .address(ADDRESS)
                    .name(NAME)
                    .build();
    public static final ProfileDto PROFILE_DTO =
            ProfileDto.builder()
                    .address(ADDRESS_DTO)
                    .name(NAME)
                    .build();

    public static final RegistrationLoginAccount REGISTRATION_LOGIN_ACCOUNT =
            RegistrationLoginAccount
                    .builder()
                    .email(EMAIL)
                    .password(PASSWORD)
                    .build();

    public static final AccountAuthenticationResponse ACCOUNT_AUTHENTICATION_RESPONSE =
            AccountAuthenticationResponse.builder()
                    .email(EMAIL)
                    .password(PASSWORD)
                    .id(ID)
                    .role(List.of(ROLE_DTO))
                    .build();

    public static final Account ACCOUNT_WITH_PROFILE =
            Account.builder()
                    .id(ID)
                    .email(EMAIL)
                    .password(PASSWORD)
                    .role(List.of(ROLE_USER, ROLE_PLAYER))
                    .dateCreated(DATE)
                    .profile(PROFILE_WITHOUT_ACCOUNT)
                    .build();

    public static final Account ACCOUNT_WITHOUT_PROFILE =
            Account.builder()
                    .id(ID)
                    .email(EMAIL)
                    .password(PASSWORD)
                    .role(List.of(ROLE_USER))
                    .dateCreated(DATE)
                    .build();

    public static final AccountDto ACCOUNT_DTO =
            AccountDto.builder()
                    .email(EMAIL)
                    .build();
    public static final AccountChangePassword ACCOUNT_CHANGE_PASSWORD =
            AccountChangePassword.builder()
                    .oldPassword(OLD_PASSWORD)
                    .newPassword(PASSWORD)
                    .newPassword2(PASSWORD)
                    .build();
    public static final StatusGame STATUS_GAME_START = StatusGame.START;
    public static final TypeGame TYPE_GAME_OPEN = TypeGame.OPEN;
    public static final String DESCRIPTION = "description";
    public static final String NECESSARY_THINGS = "necessary_things";
    public static final String UNNECESSARY_THINGS = "unnecessary_things";

    public static final Profile PROFILE_WITH_ACCOUNT_WITHOUT_PLAYER =
            Profile.builder()
                    .id(ID)
                    .address(ADDRESS)
                    .name(NAME)
                    .account(ACCOUNT_WITHOUT_PROFILE)
                    .build();

    public static final Player PLAYER =
            Player.builder()
                    .id(ID)
                    .profile(PROFILE_WITH_ACCOUNT_WITHOUT_PLAYER)
                    .timeRegistration(DATE)
                    .necessaryThings(NECESSARY_THINGS)
                    .unnecessaryThings(UNNECESSARY_THINGS)
                    .build();


    public static final PlayerRequestDto PLAYER_REQUEST_DTO =
            PlayerRequestDto.builder()
                    .necessaryThings(NECESSARY_THINGS)
                    .unnecessaryThings(UNNECESSARY_THINGS)
                    .build();

    public static final GameRequestDto GAME_REQUEST_DTO =
            GameRequestDto.builder()
                    .description(DESCRIPTION)
                    .nameGame(NAME_GAME)
                    .password(PASSWORD)
                    .timeEnd(DATE)
                    .timeStart(DATE)
                    .build();

    public static final List<Player> PLAYERS = new ArrayList<>(List.of(PLAYER));

    public static final Game GAME_WITH_PLAYERS =
            Game.builder()
                    .id(ID)
                    .nameGame(NAME_GAME)
                    .timeCreated(DATE)
                    .players(PLAYERS)
                    .statusGame(STATUS_GAME_START)
                    .typeGame(TYPE_GAME_OPEN)
                    .description(DESCRIPTION)
                    .timeEnd(DATE)
                    .timeStart(DATE)
                    .password(PASSWORD)
                    .build();
    public static final Game GAME_WITHOUT_PLAYERS =
            Game.builder()
                    .id(ID)
                    .nameGame(NAME_GAME)
                    .timeCreated(DATE)
                    .statusGame(STATUS_GAME_START)
                    .typeGame(TYPE_GAME_OPEN)
                    .description(DESCRIPTION)
                    .timeEnd(DATE)
                    .timeStart(DATE)
                    .password(PASSWORD)
                    .build();

    public static final Player PLAYER_WITH_GAME =
            Player.builder()
                    .id(ID)
                    .profile(PROFILE_WITHOUT_ACCOUNT)
                    .timeRegistration(DATE)
                    .necessaryThings(NECESSARY_THINGS)
                    .unnecessaryThings(UNNECESSARY_THINGS)
                    .game(GAME_WITH_PLAYERS)
                    .build();

    public static final List<Player> PLAYERS_WITH_GAME = new ArrayList<>(List.of(PLAYER_WITH_GAME));
    public static final ProfileOrganizer PROFILE_ORGANIZER =
            ProfileOrganizer.builder()
                    .name(NAME)
                    .build();

    public static final GameResponseDto GAME_RESPONSE_DTO =
            GameResponseDto.builder()
                    .nameGame(NAME_GAME)
                    .description(DESCRIPTION)
                    .timeEnd(DATE)
                    .timeStart(DATE)
                    .timeCreated(DATE)
                    .organizer(PROFILE_ORGANIZER)
                    .build();
    public static final List<Game> GAMES = List.of(GAME_WITHOUT_PLAYERS);
    public static final Distribution DISTRIBUTION =
            Distribution.builder()
                    .id(ID)
                    .timeCreated(DATE)
                    .build();
    public static final List<Distribution> DISTRIBUTIONS = List.of(DISTRIBUTION);
    public static final Integer NUMBER_PAGE = 1;
    public static final Integer SIZE = 1;
    public static final String SORT = "sort";
    public static final PagesDto PAGES_DTO =
            PagesDto.builder()
                    .page(NUMBER_PAGE)
                    .size(SIZE)
                    .sort(SORT)
                    .build();
    public static final List<AccountDto> ACCOUNTS_DTO = List.of(ACCOUNT_DTO);
    public static final List<AddressDto> ADDRESSES_DTO = List.of(ADDRESS_DTO);
    public static final List<GameResponseDto> GAMES_RESPONSE_DTO = List.of(GAME_RESPONSE_DTO);
    public static final Long TOTAL = 1L;
    public static final PageRequest PAGEABLE =
            PageRequest.of(NUMBER_PAGE, SIZE, Sort.by(SORT));
    public static final Page<Account> PAGE_ACCOUNT =
            new PageImpl<>(List.of(ACCOUNT_WITH_PROFILE), PAGEABLE, TOTAL);
    public static final Page<Player> PAGE_PLAYER =
            new PageImpl<>(List.of(PLAYER), PAGEABLE, TOTAL);
    public static final Page<Game> PAGE_GAME =
            new PageImpl<>(List.of(GAME_WITHOUT_PLAYERS), PAGEABLE, TOTAL);
    public static final Page<Address> PAGE_ADDRESS =
            new PageImpl<>(List.of(ADDRESS), PAGEABLE, TOTAL);
    public static final Profile PROFILE_WITH_ACCOUNT =
            Profile.builder()
                    .id(ID)
                    .address(ADDRESS)
                    .name(NAME)
                    .account(ACCOUNT_WITHOUT_PROFILE)
                    .players(PLAYERS)
                    .build();
    public static final Page<Profile> PAGE_PROFILE =
            new PageImpl<>(List.of(PROFILE_WITH_ACCOUNT), PAGEABLE, TOTAL);

    public static final PagesDtoResponse<Object> PAGES_DTO_RESPONSE_ACCOUNT_DTO =
            PagesDtoResponse.builder()
                    .page(NUMBER_PAGE)
                    .size(SIZE)
                    .sort(SORT)
                    .dto(List.of(ACCOUNT_DTO))
                    .build();

    public static final PagesDtoResponse<Object> PAGES_DTO_RESPONSE_ADDRESS_DTO =
            PagesDtoResponse.builder()
                    .page(NUMBER_PAGE)
                    .size(SIZE)
                    .sort(SORT)
                    .dto(List.of(ADDRESS_DTO))
                    .build();
    public static final PagesDtoResponse<Object> PAGES_DTO_RESPONSE_PROFILE_DTO =
            PagesDtoResponse.builder()
                    .page(NUMBER_PAGE)
                    .size(SIZE)
                    .sort(SORT)
                    .dto(List.of(PROFILE_DTO))
                    .build();
    public static final PagesDtoResponse<Object> PAGES_DTO_RESPONSE_GAMES_RESPONSE_DTO =
            PagesDtoResponse.builder()
                    .page(NUMBER_PAGE)
                    .size(SIZE)
                    .sort(SORT)
                    .dto(List.of(GAME_RESPONSE_DTO))
                    .build();
    public static final GameRegistration GAME_REGISTRATION =
            GameRegistration.builder()
                    .nameGame(NAME_GAME)
                    .password(PASSWORD)
                    .build();
    public static final PlayerResponseDto PLAYER_RESPONSE_DTO =
            PlayerResponseDto.builder()
                    .necessaryThings(NECESSARY_THINGS)
                    .unnecessaryThings(UNNECESSARY_THINGS)
                    .timeRegistration(DATE)
                    .profile(PROFILE_DTO)
                    .game(GAME_RESPONSE_DTO)
                    .build();
    public static final List<PlayerResponseDto> PLAYERS_RESPONSE_DTO =
            new ArrayList<>(List.of(PLAYER_RESPONSE_DTO));

    public static final PagesDtoResponse<Object> PAGES_DTO_RESPONSE_PLAYER_DTO =
            PagesDtoResponse.builder()
                    .page(NUMBER_PAGE)
                    .size(SIZE)
                    .sort(SORT)
                    .dto(List.of(PLAYER_RESPONSE_DTO))
                    .build();
    public static final Profile PROFILE_ORGANIZER_FOR_GAME =
            Profile.builder()
                    .id(ID)
                    .address(ADDRESS)
                    .name(NAME)
                    .account(ACCOUNT_WITHOUT_PROFILE)
                    .build();

    public static final Game GAME_WITH_ORGANIZER =
            Game.builder()
                    .id(ID)
                    .nameGame(NAME_GAME)
                    .timeCreated(DATE)
                    .organizer(PROFILE_ORGANIZER_FOR_GAME)
                    .players(PLAYERS)
                    .statusGame(STATUS_GAME_START)
                    .typeGame(TYPE_GAME_OPEN)
                    .description(DESCRIPTION)
                    .timeEnd(DATE)
                    .timeStart(DATE)
                    .password(PASSWORD)
                    .build();

    public static final Player PLAYER_WITH_ACCOUNT =
            Player.builder()
                    .id(ID)
                    .profile(PROFILE_WITH_ACCOUNT)
                    .necessaryThings(NECESSARY_THINGS)
                    .unnecessaryThings(UNNECESSARY_THINGS)
                    .build();

    public static final Game GAMES_WITH_ACCOUNTS =
            Game.builder()
                    .id(ID)
                    .nameGame(NAME_GAME)
                    .timeCreated(DATE)
                    .statusGame(STATUS_GAME_START)
                    .typeGame(TYPE_GAME_OPEN)
                    .description(DESCRIPTION)
                    .timeEnd(DATE)
                    .timeStart(DATE)
                    .organizer(PROFILE_WITH_ACCOUNT)
                    .players(List.of(PLAYER_WITH_ACCOUNT))
                    .build();
    public static final DistributionResponseDto DISTRIBUTION_RESPONSE_DTO =
            DistributionResponseDto.builder()
                    .game(GAME_RESPONSE_DTO)
                    .senderPlayer(PLAYER_RESPONSE_DTO)
                    .targetPlayer(PLAYER_RESPONSE_DTO)
                    .timeCreated(DATE)
                    .build();
    public static final Distribution DISTRIBUTION_WITH_ACCOUNT =
            Distribution.builder()
                    .id(ID)
                    .timeCreated(DATE)
                    .game(GAMES_WITH_ACCOUNTS)
                    .targetPlayer(PLAYER_WITH_ACCOUNT)
                    .senderPlayer(PLAYER_WITH_ACCOUNT)
                    .build();
    public static final Page<Distribution> PAGE_DISTRIBUTION =
            new PageImpl<>(List.of(DISTRIBUTION_WITH_ACCOUNT), PAGEABLE, TOTAL);
    public static final PagesDtoResponse<Object> PAGES_DTO_RESPONSE_DISTRIBUTION_DTO =
            PagesDtoResponse.builder()
                    .page(NUMBER_PAGE)
                    .size(SIZE)
                    .sort(SORT)
                    .dto(List.of(DISTRIBUTION_RESPONSE_DTO))
                    .build();
    public static final String TEXT_MESSAGE = "text_message";
    public static final String STRING_ID = "string_id";
    public static final String TIME_MESSAGE = "time_message";
    public static final String NAME_COLLECTION = "sent_messages";
    public static final String NAME_COLLECTION_SYSTEM_MESSAGES = "system_messages";
    public static final SentMessage SENT_MESSAGE =
            SentMessage.builder()
                    .textMessage(TEXT_MESSAGE)
                    .id(STRING_ID)
                    .timeMessage(TIME_MESSAGE)
                    .account(ID)
                    .build();
    public static final SentMessageDto SENT_MESSAGE_DTO =
            SentMessageDto.builder()
                    .textMessage(TEXT_MESSAGE)
                    .account(ID)
                    .build();

    public static final Class<SystemMessage> SYSTEM_MESSAGE_CLASS = SystemMessage.class;
    public static final String KEY = "typeMessage";

    public static final Query QUERY =
            new Query((Criteria.where(KEY).is(TYPE_MESSAGE_CREATE.name())));

    public static final SystemMessage SYSTEM_MESSAGE =
            SystemMessage.builder()
                    .textMessage(TEXT_MESSAGE)
                    .typeMessage(TYPE_MESSAGE_CREATE)
                    .id(STRING_ID)
                    .build();
    public static final SimpleMailMessage MAIL_MESSAGE =
            new SimpleMailMessage();

    static {
        MAIL_MESSAGE.setTo(EMAIL);
        MAIL_MESSAGE.setSubject(EMAIL);
        MAIL_MESSAGE.setText(TEXT_MESSAGE);
    }

}
