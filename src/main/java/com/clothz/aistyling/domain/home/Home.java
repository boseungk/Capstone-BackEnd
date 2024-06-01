package com.clothz.aistyling.domain.home;

import com.clothz.aistyling.domain.BaseEntity;
import com.clothz.aistyling.domain.styling.Styling;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Table(name = "home")
@Entity
public class Home extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "home_id")
    private Long id;

    @Column(name = "image")
    private String image;

    @Column(name = "sentence")
    private String sentence;

    @Builder
    public Home(final String image, final String sentence) {
        this.image = image;
        this.sentence = sentence;
    }
}
