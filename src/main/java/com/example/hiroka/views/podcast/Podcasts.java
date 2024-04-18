package com.example.hiroka.views.podcast;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import java.io.InputStream;

@PageTitle("Podcasts")
@Route(value = "podcasts")
@AnonymousAllowed
public class Podcasts extends VerticalLayout {

    public Podcasts() {
        setAlignItems(Alignment.CENTER);
        setSizeFull();

        // Your other UI initialization...

        Podcast[] podcasts = {
                new Podcast("Подкаст о технологиях", "audio/marlen.mp3"),
                new Podcast("BROOOO" , "audio/uuu.mp3")
        };
        for (Podcast podcast : podcasts) {
            HorizontalLayout podcastLayout = createPodcastLayout(podcast);
            add(podcastLayout);
        }
    }

    private HorizontalLayout createPodcastLayout(Podcast podcast) {

        HorizontalLayout podcastLayout = new HorizontalLayout();
        podcastLayout.setWidthFull();
        podcastLayout.setAlignItems(Alignment.CENTER);

        // Paragraph for the podcast title
        Paragraph podcastName = new Paragraph(podcast.title);

        // Button to play the podcast
        Button playButton = new Button("Play");




        StreamResource streamResource = new StreamResource("marlen.mp3", () -> {
            InputStream resource = getClass().getClassLoader().getResourceAsStream("audio/marlen.mp3");
            if (resource == null) {
                throw new RuntimeException("Resource not found: audio/marlen.mp3");
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

    class Podcast {
        String title;
        String audioSource;

        Podcast(String title, String audioSource) {
            this.title = title;
            this.audioSource = audioSource; // Relative path within the 'src/main/resources/META-INF/resources/' directory
        }
    }
}