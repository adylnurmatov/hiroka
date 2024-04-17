package com.example.hiroka.views.admin;

import com.example.hiroka.form.PodcastForm;
import com.example.hiroka.podcast.Podcast;
import com.example.hiroka.service.impl.PodcastService;
import com.example.hiroka.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;

import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Admin/podcasts")
@Route(value = "podcast", layout = MainLayout.class)
@PermitAll
public class AdminpodcastsView extends VerticalLayout {
    private final PodcastService podcastService;
    Grid<Podcast> grid = new Grid<>(Podcast.class);
    TextField filterText =new TextField();

    PodcastForm podcastForm;


    public AdminpodcastsView(PodcastService podcastService) {
        this.podcastService = podcastService;
        addClassName("list-view");
        setSizeFull();

        configurePodcastForm();
        configureGrid();
        add(
                getToolBar(),
                getContent()
        );
        updateList();
        closeEditor();

    }

    private void closeEditor() {
        podcastForm.setPodcast(null);
        podcastForm.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(podcastService.findAllPodcast(filterText.getValue()));
    }

    private Component getContent() {
        HorizontalLayout horizontalLayout = new HorizontalLayout(grid, podcastForm);
        horizontalLayout.setFlexGrow(2, grid);
        horizontalLayout.setFlexGrow(1, podcastForm);
        horizontalLayout.addClassName("content");
        horizontalLayout.setSizeFull();
        return horizontalLayout;
    }

    private void configurePodcastForm() {
        podcastForm = new PodcastForm();
        podcastForm.setWidth("25em");

        podcastForm.addSaveListener(this::savePodcast);
        podcastForm.addDeleteListener(this::deletePodcast);
        podcastForm.addCloseListener(e -> closeEditor());
    }

    private void deletePodcast(PodcastForm.DeleteEvent deleteEvent) {
        podcastService.deletePodcast(deleteEvent.getPodcast());
        updateList();
        closeEditor();
    }

    private void savePodcast(PodcastForm.SaveEvent event){
        podcastService.savePodcast(event.getPodcast());
        updateList();
        closeEditor();
    }

    private Component getToolBar() {
        filterText.setPlaceholder("Filter by name");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());
        Button contactBtn = new Button("Add podcast");
        contactBtn.addClickListener(e -> addContact());
        HorizontalLayout toolbar = new HorizontalLayout(filterText, contactBtn);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addContact() {
        grid.asSingleSelect().clear();
        editPodcast(new Podcast());
    }

    private void configureGrid() {
        grid.addClassName("contact-grid");
        grid.setSizeFull();
        grid.setColumns("title", "description", "subtitle");
        grid.getColumns().forEach(contactColumn -> contactColumn.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(e -> editPodcast(e.getValue()));
    }

    private void editPodcast(Podcast value) {
        if(value == null){
            closeEditor();
        }else {
            podcastForm.setPodcast(value);
            podcastForm.setVisible(true);
            addClassName("editing");
        }
    }

}
