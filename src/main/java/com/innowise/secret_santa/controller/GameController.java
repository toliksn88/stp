package com.innowise.secret_santa.controller;

import com.innowise.secret_santa.constants_message.Constants;
import com.innowise.secret_santa.model.dto.request_dto.GameRequestDto;
import com.innowise.secret_santa.model.dto.request_dto.PagesDto;
import com.innowise.secret_santa.model.dto.response_dto.GameResponseDto;
import com.innowise.secret_santa.model.dto.response_dto.PagesDtoResponse;
import com.innowise.secret_santa.service.game_service.GameService;
import com.innowise.secret_santa.util.HandleAuthorities;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("api/accounts/profiles/games")
@Api("Controller about game")
@Validated
public class GameController {

    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping
    @ApiOperation("Create new game")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<GameResponseDto> createGame(@RequestBody @Valid GameRequestDto game) {
        GameResponseDto newGame = gameService.createGame(game, HandleAuthorities.getIdAuthenticationAccount());

        return new ResponseEntity<>(newGame, HttpStatus.CREATED);
    }

    @DeleteMapping
    @ApiOperation("Delete game for current account")
    @PreAuthorize("hasPermission('ROLE_ADMIN', authentication.principal.authorities) or " +
            "hasPermission('ROLE_ORGANIZER', authentication.principal.authorities)")
    public ResponseEntity<HttpStatus> deleteGame(@RequestParam
                                                 @NotBlank(message = Constants.NOT_NULL_FIELD)
                                                 String nameGame) {
        gameService.deleteGame(HandleAuthorities.getIdAuthenticationAccount(), nameGame);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    @ApiOperation("Get game by id")
    @PreAuthorize("hasPermission('ROLE_ADMIN', authentication.principal.authorities)")
    public ResponseEntity<GameResponseDto> getGameByID(@PathVariable("id")
                                                       @Positive(message = Constants.NOT_NEGATIVE_ID)
                                                       Long id) {
        GameResponseDto gameById = gameService.getGameById(id);
        return new ResponseEntity<>(gameById, HttpStatus.OK);
    }

    @PatchMapping
    @ApiOperation("Change game for current account")
    @PreAuthorize("hasPermission('ROLE_ORGANIZER', authentication.principal.authorities)")
    public ResponseEntity<GameResponseDto> changeGame(@RequestBody @Valid GameRequestDto game,
                                                      @RequestParam
                                                      @NotBlank(message = Constants.NOT_NULL_FIELD)
                                                      String nameGame) {
        GameResponseDto gameResponseDto =
                gameService.changeGame(game, HandleAuthorities.getIdAuthenticationAccount(), nameGame);
        return new ResponseEntity<>(gameResponseDto, HttpStatus.OK);
    }

    @GetMapping
    @ApiOperation("Get all games current account")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<GameResponseDto>> getAllGamesCurrentAccount() {
        List<GameResponseDto> listGames =
                gameService.getAllGamesAccounts(HandleAuthorities.getIdAuthenticationAccount());

        return new ResponseEntity<>(listGames, HttpStatus.OK);
    }

    @GetMapping("/all")
    @ApiOperation("Get all games")
    @PreAuthorize("hasPermission('ROLE_ADMIN', authentication.principal.authorities)")
    public ResponseEntity<PagesDtoResponse<Object>> getAllAccounts
            (@RequestParam(defaultValue = "5") int size,
             @RequestParam(defaultValue = "0") int page,
             @RequestParam(required = false, defaultValue = "email") String sort) {
        PagesDtoResponse<Object> allGames = gameService.getAllGames(PagesDto
                .builder()
                .sort(sort)
                .size(size)
                .page(page)
                .build());
        return new ResponseEntity<>(allGames, HttpStatus.OK);
    }
}