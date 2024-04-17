package com.example.hiroka.views.podcast;


import com.example.hiroka.podcast.Podcast;
import com.example.hiroka.views.MainLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@PageTitle("Podcasts")
@Route(value = "podcasts", layout = MainLayout.class)
@AnonymousAllowed
public class Podcasts extends VerticalLayout{
    public Podcasts() {
        setAlignItems(FlexComponent.Alignment.CENTER);
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        setSizeFull();
        Podcast[] podcasts = {
                new Podcast("Подкаст о технологиях", "audios/podcast1.mp3"),
                new Podcast("Интервью с разработчиками", "podcast2.mp3"),
                new Podcast("Истории успеха в IT", "podcast3.mp3")
        };

        Image image = new Image("https://example.com/image-url.jpg", "Podcasts");
        image.setWidthFull();


        openPodcastsDialog(podcasts);

    }

    private void openPodcastsDialog(Podcast[] podcasts) {


        for (Podcast podcast : podcasts) {
            HorizontalLayout podcastLayout = new HorizontalLayout();
            podcastLayout.setWidthFull();
            podcastLayout.setAlignItems(FlexComponent.Alignment.CENTER);

            Div audioDiv = new Div();
            audioDiv.getElement().setProperty("innerHTML",
                    "<audio controls>" +
                            "<source src='" + podcast.audioSource + "' type='audio/mpeg'>" +
                            "Your browser does not support the audio element." +
                            "</audio>");

            Paragraph podcastName = new Paragraph(podcast.title);
            podcastLayout.add(podcastName, audioDiv);

            add(podcastLayout);
        }
    }

    class Podcast {
        String title;
        String audioSource;

        Podcast(String title, String audioSource) {
            this.title = title;
            this.audioSource = audioSource;
        }
    }
}
