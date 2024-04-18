package com.example.hiroka.form;

import com.example.hiroka.podcast.Category;
import com.example.hiroka.podcast.Podcast;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;



public class PodcastForm extends FormLayout {
    Binder<Podcast> binder = new BeanValidationBinder<>(Podcast.class);
    TextField title = new TextField("username");
    TextField description = new TextField("description");
    TextField author = new TextField("author");
    TextField fileName = new TextField("file name");
    TextField text = new TextField("text");



    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button cancel = new Button("Cancel");
    private Podcast podcast;

    public PodcastForm() {
        addClassName("contact-form");
        binder.bindInstanceFields(this);

        add(
                title,
                description,
                text,
                author,
                fileName,

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

        save.addClickListener(click -> validateAndSave());
        delete.addClickListener(click -> fireEvent(new DeleteEvent(this, podcast)));
        cancel.addClickListener(click -> fireEvent(new CloseEvent(this)));


        save.addClickShortcut(Key.ENTER);
        cancel.addClickShortcut(Key.ESCAPE);
        return new HorizontalLayout(save, delete, cancel);
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
}