package com.clothz.aistyling.domain.styling;

import com.clothz.aistyling.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Table(name = "styling")
@Entity
public class Styling extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image")
    private String image;

    @Column(name = "prompt")
    private String prompt;

    @Builder
    public Styling(final String image, final String prompt) {
        this.image = image;
        this.prompt = prompt;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Styling styling)) return false;
        return Objects.equals(id, styling.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
