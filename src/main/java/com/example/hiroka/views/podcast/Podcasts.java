package com.example.hiroka.views.podcast;
import com.example.hiroka.podcast.Podcast;
import com.example.hiroka.repo.PodcastRepository;
import com.example.hiroka.service.impl.PodcastServiceImpl;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import lombok.RequiredArgsConstructor;

import java.io.InputStream;
import java.util.List;

@PageTitle("Podcasts")
@Route(value = "podcasts")
@AnonymousAllowed
public class Podcasts extends VerticalLayout {
    ComboBox<String> lan = new ComboBox<>();

    List<UserPodcast> podcasts;

    private final PodcastServiceImpl podcastService;
    
    public Podcasts(PodcastServiceImpl podcastService) {
        this.podcastService = podcastService;
        setAlignItems(Alignment.CENTER);
        setSizeFull();
        Button enBu = new Button("en");
        Button frBu = new Button("fr");
        Button koBu = new Button("ko");
        Button deBu = new Button("ge");
        Button ruBu = new Button("ru");

        podcasts = podcastService.allNamesAndPaths();

        for (UserPodcast podcast : podcasts) {
            HorizontalLayout podcastLayout = createPodcastLayout(podcast);
            add(podcastLayout);
        }


        enBu.addClickListener(e -> {
            text2Speech("en");
            podcasts = podcastService.allNamesAndPaths();
            System.out.println("en");

            for (UserPodcast podcast : podcasts) {
                HorizontalLayout podcastLayout = createPodcastLayout(podcast);
                add(podcastLayout);
            }
        });
        koBu.addClickListener(e -> {
            text2Speech("ko");
            podcasts = podcastService.allNamesAndPaths();
            System.out.println("ko");

            for (UserPodcast podcast : podcasts) {
                HorizontalLayout podcastLayout = createPodcastLayout(podcast);
                add(podcastLayout);
            }
        });
        deBu.addClickListener(e -> {
            text2Speech("ge");
            podcasts = podcastService.allNamesAndPaths();
            System.out.println("ko");

            for (UserPodcast podcast : podcasts) {
                HorizontalLayout podcastLayout = createPodcastLayout(podcast);
                add(podcastLayout);
            }
        });
        ruBu.addClickListener(e -> {
            text2Speech("ru");
            podcasts = podcastService.allNamesAndPaths();
            System.out.println("ko");

            for (UserPodcast podcast : podcasts) {
                HorizontalLayout podcastLayout = createPodcastLayout(podcast);
                add(podcastLayout);
            }
        });
        frBu.addClickListener(e -> {
            text2Speech("fr");

            podcasts = podcastService.allNamesAndPaths();
            System.out.println("fr");

            for (UserPodcast podcast : podcasts) {
                HorizontalLayout podcastLayout = createPodcastLayout(podcast);
                add(podcastLayout);
            }
        });





        // Your other UI initialization...

//        UserPodcast[] podcasts = {
//                new UserPodcast("Подкаст о технологиях", "/home/marlen/Desktop/hiroka/src/main/resources/audio/telegram_audio.mp3"),
//                new UserPodcast("BROOOO" , "audio/uuu.mp3")
//        };

        add(enBu, frBu, ruBu, koBu, deBu);
    }

    private HorizontalLayout createPodcastLayout(UserPodcast podcast) {

        HorizontalLayout podcastLayout = new HorizontalLayout();
        podcastLayout.setWidthFull();
        podcastLayout.setAlignItems(Alignment.CENTER);

        // Paragraph for the podcast title
        Paragraph podcastName = new Paragraph(podcast.title);

        // Button to play the podcast
        Button playButton = new Button("Play");




        StreamResource streamResource = new StreamResource(podcast.title, () -> {
            InputStream resource = getClass().getClassLoader().getResourceAsStream(podcast.audioSource);
            if (resource == null) {
                throw new RuntimeException("Resource not found: " + podcast.audioSource);
            }
            return resource;
        });




        // Div that will contain the audio element
        Div audioDiv = new Div();

        audioDiv.getElement().setAttribute("id", "audio-" + podcast.audioSource);


        // Add a listener to the play button to play the audio
        playButton.addClickListener(buttonClickEvent -> {
            // Use JavaScript to control the audio playback
            getElement().executeJs(
                    "var audio = document.getElementById($0);" +
                            "if (audio.paused) {" +
                            "  audio.play();" +
                            "  $1.textContent = 'Pause';" +
                            "} else {" +
                            "  audio.pause();" +
                            "  $1.textContent = 'Play';" +
                            "}", audioDiv.getElement().getAttribute("id"), playButton.getElement()
            );
        });


        // Append the source element to the audio element
        audioDiv.getElement().appendChild(new Element("audio")
                .setAttribute("controls", true)
                .appendChild(new Element("source")
                        .setAttribute("type", "audio/mp3")
                        .setAttribute("src", streamResource)));

        podcastLayout.add(podcastName, playButton, audioDiv);

        return podcastLayout;
    }

    public static class UserPodcast {
        String title;
        String audioSource;

        public UserPodcast(String title, String audioSource) {
            this.title = title;
            this.audioSource = audioSource; // Relative path within the 'src/main/resources/META-INF/resources/' directory
        }
    }
//  us En
    private void text2Speech(String language){
        podcastService.text2Speech(language);
    }
}