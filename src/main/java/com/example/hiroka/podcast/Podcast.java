package com.example.hiroka.podcast;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "podcast")
@Getter
@Setter
public class Podcast {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String author;
    private String description;
    @Enumerated(EnumType.STRING)
    private Category category;
    private String subtitle;//текст
    private String fileName;
}
