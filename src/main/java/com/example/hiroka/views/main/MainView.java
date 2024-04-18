package com.example.hiroka.views.main;

import com.example.hiroka.views.MainLayout;
import com.example.hiroka.views.imageGallery.ImageGalleryViewCard;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.charts.model.style.Color;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.OrderedList;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;

import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;



@PageTitle("Main")
@Route(value = "main", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@AnonymousAllowed
@Uses(Icon.class)
public class MainView extends Composite<VerticalLayout> implements HasComponents {
    private OrderedList podcastContainer;



    public MainView() {
        VerticalLayout layoutColumn2 = new VerticalLayout();
        HorizontalLayout layoutRow = new HorizontalLayout();
        TextField textField = new TextField();
        HorizontalLayout layoutRow2 = new HorizontalLayout();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        layoutColumn2.setWidthFull();
        textField.setPlaceholder("Search");
        textField.setWidth("60%");
        getContent().setFlexGrow(1.0, layoutColumn2);
        layoutColumn2.setWidth("100%");
        layoutColumn2.getStyle().set("flex-grow", "1");
        layoutRow.setWidthFull();
        layoutColumn2.setFlexGrow(1.0, layoutRow);
        layoutRow.setMargin(true);
        layoutRow2.setMargin(true);
        layoutRow.setWidth("100%");
        layoutColumn2.setPadding(false);
        layoutColumn2.setMargin(false);

        layoutRow.setHeight("min-content");
        layoutRow.setAlignItems(FlexComponent.Alignment.START);
        layoutRow.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        layoutRow2.setWidthFull();
        layoutColumn2.setFlexGrow(1.0, layoutRow2);
        layoutRow2.addClassName(Gap.MEDIUM);
        layoutRow2.setWidth("100%");
        layoutRow2.getStyle().set("flex-grow", "1");
        getContent().add(layoutColumn2);
        layoutColumn2.add(layoutRow);
        layoutRow.add(textField);
        layoutColumn2.add(layoutRow2);
        constructUI();
        podcastContainer.add(new ImageGalleryViewCard("Kyrgyz Podcasts",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQoqa5NvX7RnEgxYKNtAp4ItZPzAYGxtcm7sg&usqp=CAU" , "This is a podcast about Kyrgyzstan"));
        podcastContainer.add(new ImageGalleryViewCard("Manas","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRmkrzC3obMwh2hRRwGXK7Nz_R9q_p51ag6KQ&usqp=CAU" , "That is a podcast about Manas who was a hero of Kyrgyzstan"));
        podcastContainer.add(new ImageGalleryViewCard("NOORUZ","https://files.oaiusercontent.com/file-E6Y6SeDmlA5pxx4Kzq0bDGJy?se=2024-04-18T02%3A02%3A08Z&sp=r&sv=2021-08-06&sr=b&rscc=max-age%3D31536000%2C%20immutable&rscd=attachment%3B%20filename%3D02274ef8-afd3-4aac-abb4-9a32a3f97145.webp&sig=2rCv32nyOVnMzcfYLejdtZQqnxmC2v0O0pSZN7tlYu8%3D" ,"This playlist contains information about Nooruz , the most popular holiday in Kyrgyzstan"));

        podcastContainer.add(new ImageGalleryViewCard("Kurmanjan Datka","https://files.oaiusercontent.com/file-aVpd8Okikm5SSd6gc7Unnwmj?se=2024-04-18T02%3A02%3A08Z&sp=r&sv=2021-08-06&sr=b&rscc=max-age%3D31536000%2C%20immutable&rscd=attachment%3B%20filename%3D6739b0e6-1981-4a88-9949-6d62ad360a9b.webp&sig=5JN2GslleUr6HNg09H7umqHFb2cHNwUBtUaOMxdHyKI%3D" , "Podcast contains info about Kurmanjan Datka"));


    }
    private void constructUI() {
        addClassNames("image-gallery-view");
        addClassNames(LumoUtility.MaxWidth.SCREEN_LARGE, LumoUtility.Margin.Horizontal.AUTO, Padding.Bottom.LARGE, Padding.Horizontal.LARGE);

        HorizontalLayout container = new HorizontalLayout();
        container.addClassNames(LumoUtility.AlignItems.CENTER, LumoUtility.JustifyContent.BETWEEN);

        VerticalLayout headerContainer = new VerticalLayout();

        headerContainer.add();

        podcastContainer = new OrderedList();
        podcastContainer.addClassNames(Gap.MEDIUM, LumoUtility.Display.FLEX, LumoUtility.FlexWrap.WRAP, LumoUtility.ListStyleType.NONE, LumoUtility.Margin.NONE, Padding.NONE );
        container.add(headerContainer);
        add(container, podcastContainer);
    }
}