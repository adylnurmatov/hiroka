package com.example.hiroka.podcast;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.atmosphere.config.service.Get;

@Entity
@Table(name = "podcast")
@Getter
@Setter
public class Podcast {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private Category category;
    private String subtitle;
    private String fileName;
}
