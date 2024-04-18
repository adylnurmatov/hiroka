package com.example.hiroka.form;

import com.example.hiroka.podcast.Podcast;
import com.example.hiroka.service.PodcastService;
import com.example.hiroka.service.impl.PodcastServiceImpl;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

import static java.io.File.createTempFile;


@AllArgsConstructor
public class PodcastForm extends FormLayout {

    private RestTemplate restTemplate = new RestTemplate();
    //    private final PodcastServiceImpl podcastService;
    Binder<Podcast> binder = new BeanValidationBinder<>(Podcast.class);
    TextField title = new TextField("title");
    TextField description = new TextField("description");
    TextField author = new TextField("author");
    TextField fileName = new TextField("file name");
    TextField subtitle = new TextField("text");
    private Path tempFile;



    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button cancel = new Button("Cancel");
    Button openUploadButton = new Button("Add attach mp3");
    //        openUploadButton.addClickListener(event -> openUploadDialog());
    private Podcast podcast;

    public PodcastForm() throws IOException {
        addClassName("contact-form");
        binder.bindInstanceFields(this);

        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setAcceptedFileTypes("audio/mpeg", "audio/mp3");
        upload.setMaxFileSize(10 * 1024 * 1024);
//        text.setValue(getSpeechToText(buffer));
        // Кнопка сохранения, которая изначально не видима

        // Добавляем слушатель событий для загрузки файла
        upload.addSucceededListener(event -> {
            InputStream fileContent = buffer.getInputStream();
            // Создаем временный файл
            tempFile = createTempFile(fileContent, event.getFileName());
            try {
                subtitle.setValue(getSpeechToText(buffer));
                fileName.setValue(buffer.getFileName());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // Показываем кнопку сохранения
            Notification.show("File uploaded successfully!");
        });

//         Добавляем компоненты в FormLayout
//        formLayout.add(upload);

        add(
                title,
                description,
//                text,
                author,
//                fileName,
                upload,
                createButtonLayout()
        );
    }

    public void setPodcast(Podcast podcast){
        this.podcast = podcast;
        binder.readBean(podcast);
    }

    private Component createButtonLayout() {

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        openUploadButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickListener(click -> validateAndSave());
        delete.addClickListener(click -> fireEvent(new DeleteEvent(this, podcast)));
        cancel.addClickListener(click -> fireEvent(new CloseEvent(this)));
        openUploadButton.addClickListener(event -> openUploadDialog());



        save.addClickShortcut(Key.ENTER);
        cancel.addClickShortcut(Key.ESCAPE);
        return new HorizontalLayout(save, delete, cancel);
    }

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

    private void openUploadDialog() {
        Dialog uploadDialog = new Dialog();
        uploadDialog.setWidth("auto");
        uploadDialog.setHeight("auto");

        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setAcceptedFileTypes("audio/mpeg", ".mp3");

        // Обработка успешной загрузки файла
        upload.addSucceededListener(event -> {
            System.out.println(event);
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

    private void validateAndSave() {
        try {
            binder.writeBean(podcast);
            fireEvent(new SaveEvent(this, podcast));
        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }
    }

    // Events
    public static abstract class PodcastFormEvent extends ComponentEvent<PodcastForm> {
        private Podcast podcast;

        protected PodcastFormEvent(PodcastForm source, Podcast podcast) {
            super(source, false);
            this.podcast = podcast;
        }

        public Podcast getPodcast() {
            return podcast;
        }
    }

    public static class SaveEvent extends PodcastFormEvent {
        SaveEvent(PodcastForm source, Podcast podcast) {
            super(source, podcast);
        }
    }

    public static class DeleteEvent extends PodcastFormEvent {
        DeleteEvent(PodcastForm source, Podcast podcast) {
            super(source, podcast);
        }

    }

    public static class CloseEvent extends PodcastFormEvent {
        CloseEvent(PodcastForm source) {
            super(source, null);
        }
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(DeleteEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }
    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }

    public String getSpeechToText(MemoryBuffer buffer) throws IOException {
        // Get the InputStream from MemoryBuffer
        InputStream inputStream = buffer.getInputStream();
        String fileName = buffer.getFileName();
        byte[] bytes = inputStream.readAllBytes(); // Convert InputStream to byte[]

        return getSpeechToText(bytes, fileName);
    }

    private String getSpeechToText(byte[] mp3Data, String fileName) throws IOException {
        String url = "https://asr.ulut.kg/api/receive_data";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("Authorization", "Bearer 87f1467af3fb14548d74cc0e806434f338a277350177669156c3ea0e8b0ed11c6e90630c98ec6a978a699f59ae47abaaa9c51b337812ef46473a31e2e4545b82");

        HttpEntity<MultiValueMap<String, Object>> requestEntity = getMultiValueMapHttpEntity(mp3Data, fileName, headers);

        // Send POST request
        ResponseEntity<PodcastServiceImpl.TextResponse> response = restTemplate.postForEntity(url, requestEntity, PodcastServiceImpl.TextResponse.class);

        // Return the obtained text
        return Objects.requireNonNull(response.getBody()).getText();
    }

    private static HttpEntity<MultiValueMap<String, Object>> getMultiValueMapHttpEntity(byte[] mp3Data, String fileName, HttpHeaders headers) {
        Resource resource = new ByteArrayResource(mp3Data) {
            @Override
            public String getFilename() {
                return fileName;
            }
        };

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("audio", resource);

        return new HttpEntity<>(body, headers);
    }
}