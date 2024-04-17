package com.example.hiroka.views.settings;

import com.example.hiroka.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;

import java.util.ArrayList;
import java.util.List;

@PageTitle("Settings")
@Route(value = "settings", layout = MainLayout.class)
@PermitAll
@Uses(Icon.class)
public class SettingsView extends Composite<VerticalLayout> {
    public SettingsView() {
        Select select = new Select();
        VerticalLayout layoutColumn2 = new VerticalLayout();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        select.setLabel("Language");
        select.addValueChangeListener(event -> {
            SampleItem selectedItem = (SampleItem) event.getValue();
            if (selectedItem != null) {
                select.setPlaceholder(selectedItem.label());
            } else {
                select.setPlaceholder("Select a language");
            }
        });
        select.setWidth("min-content");
        setSelectSampleData(select);
        layoutColumn2.setWidthFull();
        getContent().setFlexGrow(1.0, layoutColumn2);
        layoutColumn2.setWidth("100%");
        layoutColumn2.getStyle().set("flex-grow", "1");
        getContent().add(select);
        getContent().add(layoutColumn2);
    }

    record SampleItem(String value, String label, Boolean disabled) {
    }

    private void setSelectSampleData(Select select) {
        List<SampleItem> sampleItems = new ArrayList<>();
        sampleItems.add(new SampleItem("en", "English", null));
        sampleItems.add(new SampleItem("ru", "Russian", null));
        sampleItems.add(new SampleItem("kg", "Kyrgyz", null));
        sampleItems.add(new SampleItem("fr", "French", null));
        select.setItems(sampleItems);
        select.setItemLabelGenerator(item -> ((SampleItem) item).label());
        select.setItemEnabledProvider(item -> !Boolean.TRUE.equals(((SampleItem) item).disabled()));
    }
}
