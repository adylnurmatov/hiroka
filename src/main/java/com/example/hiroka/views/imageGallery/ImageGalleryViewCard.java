package com.example.hiroka.views.imageGallery;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.theme.lumo.LumoUtility.AlignItems;
import com.vaadin.flow.theme.lumo.LumoUtility.Background;
import com.vaadin.flow.theme.lumo.LumoUtility.BorderRadius;
import com.vaadin.flow.theme.lumo.LumoUtility.Display;
import com.vaadin.flow.theme.lumo.LumoUtility.FlexDirection;
import com.vaadin.flow.theme.lumo.LumoUtility.FontSize;
import com.vaadin.flow.theme.lumo.LumoUtility.FontWeight;
import com.vaadin.flow.theme.lumo.LumoUtility.JustifyContent;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import com.vaadin.flow.theme.lumo.LumoUtility.Overflow;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import com.vaadin.flow.theme.lumo.LumoUtility.TextColor;
import com.vaadin.flow.theme.lumo.LumoUtility.Width;

public class ImageGalleryViewCard extends ListItem {

        public ImageGalleryViewCard(String text, String url , String t ) {
            addClassNames(Background.CONTRAST_5, Display.FLEX, FlexDirection.COLUMN, AlignItems.START, Padding.MEDIUM,
                    BorderRadius.LARGE);
            setWidth("20%");

            Div div = new Div();
            div.addClassNames(Background.CONTRAST, Display.FLEX, AlignItems.CENTER, JustifyContent.CENTER,
                    Margin.Bottom.MEDIUM, Overflow.HIDDEN, BorderRadius.MEDIUM, Width.FULL);
            div.setHeight("26%");
            div.addClickListener(e -> UI.getCurrent().navigate("podcasts"));

            Image image = new Image();

            image.setWidth("100%");
            image.setSrc(url);
            image.setAlt(text);

            div.add(image);

            Span header = new Span();
            header.addClassNames(FontSize.XLARGE, FontWeight.SEMIBOLD);
            header.setText(text);



            Paragraph description = new Paragraph(t);
            description.addClassName(Margin.Vertical.MEDIUM);

            Span badge = new Span();
            badge.getElement().setAttribute("theme", "badge");
            badge.setText("Podcast");

            add(div, header, description, badge);
        }
}
