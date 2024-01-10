package com.innowise.secret_santa.model.postgres;

import com.innowise.secret_santa.model.StatusGame;
import com.innowise.secret_santa.model.TypeGame;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(schema = "application", name = "games")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizer")
    private Profile organizer;

    @Column
    private LocalDateTime timeCreated;

    @Column
    private LocalDateTime timeStart;

    @Column
    private LocalDateTime timeEnd;

    @Column
    private String description;

    @Column
    private String nameGame;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "game", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private List<Player> players;

    @Column
    @Enumerated(EnumType.STRING)
    private StatusGame statusGame;

    @Column
    @Enumerated(EnumType.STRING)
    private TypeGame typeGame;

    @Column
    private String password;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Game game = (Game) o;
        return id != null && Objects.equals(id, game.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}