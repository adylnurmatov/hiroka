package com.example.hiroka.views.admin;

import com.example.hiroka.dto.AudioDto;
import com.example.hiroka.form.PodcastForm;
import com.example.hiroka.podcast.Podcast;
import com.example.hiroka.service.impl.PodcastService;
import com.example.hiroka.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@PageTitle("Admin/podcasts")
@Route(value = "admin/podcast", layout = MainLayout.class)
@PermitAll
public class AdminPodcastsView extends VerticalLayout {
    private final PodcastService podcastService;
    Grid<Podcast> grid = new Grid<>(Podcast.class);
    TextField filterText =new TextField();

    PodcastForm podcastForm;
    private Button saveButton;
    private Path tempFile;


    public AdminPodcastsView(PodcastService podcastService) {
        this.podcastService = podcastService;
        addClassName("list-view");
        setSizeFull();

        FormLayout formLayout = new FormLayout();

        // Создаем MemoryBuffer для хранения загруженного файла
        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setAcceptedFileTypes("audio/mpeg", "audio/mp3");
        upload.setMaxFileSize(10 * 1024 * 1024);

        // Кнопка сохранения, которая изначально не видима
        saveButton = new Button("Save File", event -> saveFile());
        saveButton.setVisible(true);

        // Добавляем слушатель событий для загрузки файла
        upload.addSucceededListener(event -> {
            InputStream fileContent = buffer.getInputStream();
            // Создаем временный файл
            tempFile = createTempFile(fileContent, event.getFileName());
            // Показываем кнопку сохранения
            saveButton.setVisible(true);
            Notification.show("File uploaded successfully!");
        });

        // Добавляем компоненты в FormLayout
        formLayout.add(upload, saveButton);

        configurePodcastForm();
        configureGrid();
        add(
                getToolBar(),
                getContent(),
                formLayout
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
        Button uploadAudio = new Button("upload audio");
        contactBtn.addClickListener(e -> addPodcast());
        HorizontalLayout toolbar = new HorizontalLayout(filterText, contactBtn);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addPodcast() {
        grid.asSingleSelect().clear();
        editPodcast(new Podcast());
    }

    private void attachText2Podcast(AudioDto audioDto){

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
    //Marlen's work
    private Path createTempFile(InputStream fileContent, String fileName) {
        try {
            Path tempFile = Files.createTempFile("upload_", fileName);
            Files.copy(fileContent, tempFile, StandardCopyOption.REPLACE_EXISTING);
            return tempFile;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveFile() {
        if (tempFile != null) {
            try {
                // Здесь логика по сохранению файла в постоянное местоположение
                Path permanentFile = Files.move(tempFile, Path.of("permanent/path/" + tempFile.getFileName()), StandardCopyOption.REPLACE_EXISTING);
                Notification.show("File saved as " + permanentFile);
            } catch (Exception e) {
                e.printStackTrace();
                Notification.show("Error saving file", 3000, Notification.Position.MIDDLE);
            }
        }
    }

}
