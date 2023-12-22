package com.digsol.main.comment;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CommentDto {

    private Long id;

    @NotBlank
    private String text;

    @PastOrPresent
    private LocalDateTime created;

    public CommentDto(Long id, String text, LocalDateTime created) {
        this.id = id;
        this.text = text;
        this.created = created;
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getCreated() {
        return created;
    }
}
