package com.example.hiroka.views.admin;

import com.example.hiroka.dto.AudioDto;
import com.example.hiroka.form.PodcastForm;
import com.example.hiroka.podcast.Podcast;
import com.example.hiroka.service.impl.PodcastServiceImpl;
import com.example.hiroka.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;


import com.vaadin.flow.component.dialog.Dialog;
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

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@PageTitle("Admin/podcasts")
@Route(value = "/admin/podcast", layout = MainLayout.class)
@PermitAll
public class AdminPodcastsView extends VerticalLayout {
    private PodcastServiceImpl podcastServiceImpl;
    Grid<Podcast> grid = new Grid<>(Podcast.class);
    TextField filterText =new TextField();

    PodcastForm podcastForm;

    private Path tempFile;


    public  AdminPodcastsView(PodcastServiceImpl podcastServiceImpl) throws IOException {
        Button openUploadButton = new Button("Add attach mp3");
        openUploadButton.addClickListener(event -> openUploadDialog());

        this.podcastServiceImpl = podcastServiceImpl;
        addClassName("list-view");
        setSizeFull();

        FormLayout formLayout = new FormLayout();

        // Создаем MemoryBuffer для хранения загруженного файла
//        MemoryBuffer buffer = new MemoryBuffer();
//        Upload upload = new Upload(buffer);
//        upload.setAcceptedFileTypes("audio/mpeg", "audio/mp3");
//        upload.setMaxFileSize(10 * 1024 * 1024);
//
//        // Кнопка сохранения, которая изначально не видима
//
//        // Добавляем слушатель событий для загрузки файла
//        upload.addSucceededListener(event -> {
//            InputStream fileContent = buffer.getInputStream();
//            // Создаем временный файл
//            tempFile = createTempFile(fileContent, event.getFileName());
//            // Показываем кнопку сохранения
//            Notification.show("File uploaded successfully!");
//        });

        // Добавляем компоненты в FormLayout
//        formLayout.add(upload);

        configurePodcastForm();
        configureGrid();
        add(
                getToolBar(),
                getContent(),
                formLayout,
                openUploadButton
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
        grid.setItems(podcastServiceImpl.findAllPodcast(filterText.getValue()));
    }

    private Component getContent() {
        HorizontalLayout horizontalLayout = new HorizontalLayout(grid, podcastForm);
        horizontalLayout.setFlexGrow(2, grid);
        horizontalLayout.setFlexGrow(1, podcastForm);
        horizontalLayout.addClassName("content");
        horizontalLayout.setSizeFull();
        return horizontalLayout;
    }

    private void configurePodcastForm() throws IOException {
        podcastForm = new PodcastForm();
        podcastForm.setWidth("25em");

        podcastForm.addSaveListener(this::savePodcast);
        podcastForm.addDeleteListener(this::deletePodcast);
        podcastForm.addCloseListener(e -> closeEditor());
    }

    private void deletePodcast(PodcastForm.DeleteEvent deleteEvent) {
        podcastServiceImpl.deletePodcast(deleteEvent.getPodcast());
        updateList();
        closeEditor();
    }

    private void savePodcast(PodcastForm.SaveEvent event){
        podcastServiceImpl.savePodcast(event.getPodcast());
        updateList();
        closeEditor();
    }
    private void openUploadDialog() {
        Dialog uploadDialog = new Dialog();
        uploadDialog.setWidth("auto");
        uploadDialog.setHeight("auto");

        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setAcceptedFileTypes("audio/mpeg", ".mp3");

        // Обработка успешной загрузки файла
        upload.addSucceededListener(event -> {
            // Здесь можно добавить логику для обработки файла, например сохранение его на сервере
            // или в базе данных
            System.out.println("Upload succeeded");
        });

        // Обработка ошибки загрузки
        upload.addFileRejectedListener(event -> {
            System.out.println("Error uploading: " + event.getErrorMessage());
        });

        uploadDialog.add(upload);

        // Кнопка для закрытия диалога
        Button closeButton = new Button("Close", e -> uploadDialog.close());
        uploadDialog.add(closeButton);

        uploadDialog.open();
    }

    private Component getToolBar() {
        filterText.setPlaceholder("Filter by name");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());
        Button contactBtn = new Button("Add podcast");
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
        grid.setColumns("title", "description", "subtitle", "author", "fileName");
        grid.getColumns().forEach(contactColumn -> contactColumn.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(e -> editPodcast(e.getValue()));
    }

    private void editPodcast(Podcast value) {
        podcastForm.setPodcast(value);
        podcastForm.setVisible(true);
        addClassName("editing");
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