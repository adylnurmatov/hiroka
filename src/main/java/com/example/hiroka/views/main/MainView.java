package com.example.hiroka.views.main;

import com.example.hiroka.views.MainLayout;
import com.example.hiroka.views.imageGallery.ImageGalleryViewCard;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.OrderedList;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;

import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import com.vaadin.flow.theme.lumo.LumoUtility.TextColor;


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
        podcastContainer.add(new ImageGalleryViewCard("Podcasts",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQoqa5NvX7RnEgxYKNtAp4ItZPzAYGxtcm7sg&usqp=CAU"));
        podcastContainer.add(new ImageGalleryViewCard("Snow covered mountain",
                "data:image/jpeg;base64,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"));
        podcastContainer.add(new ImageGalleryViewCard("River between mountains",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRmkrzC3obMwh2hRRwGXK7Nz_R9q_p51ag6KQ&usqp=CAU"));

    }
    private void constructUI() {
        addClassNames("image-gallery-view");
        addClassNames(LumoUtility.MaxWidth.SCREEN_LARGE, LumoUtility.Margin.Horizontal.AUTO, Padding.Bottom.LARGE, Padding.Horizontal.LARGE);

        HorizontalLayout container = new HorizontalLayout();
        container.addClassNames(LumoUtility.AlignItems.CENTER, LumoUtility.JustifyContent.BETWEEN);

        VerticalLayout headerContainer = new VerticalLayout();

        headerContainer.add();

        podcastContainer = new OrderedList();
        podcastContainer.addClassNames(Gap.MEDIUM, LumoUtility.Display.FLEX, LumoUtility.FlexWrap.WRAP, LumoUtility.ListStyleType.NONE, LumoUtility.Margin.NONE, Padding.NONE);
        container.add(headerContainer);
        add(container, podcastContainer);
    }
}