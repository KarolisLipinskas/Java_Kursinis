package model;

import javafx.beans.property.SimpleStringProperty;

public class CartCommentsParameters {
    private SimpleStringProperty comment_id = new SimpleStringProperty();
    private SimpleStringProperty replying = new SimpleStringProperty();
    private SimpleStringProperty text = new SimpleStringProperty();

    public CartCommentsParameters() {
    }

    public CartCommentsParameters(SimpleStringProperty comment_id, SimpleStringProperty replying, SimpleStringProperty text) {
        this.comment_id = comment_id;
        this.replying = replying;
        this.text = text;
    }


    public CartCommentsParameters(String comment_id, String replying, String text) {
        this.comment_id = new SimpleStringProperty(comment_id);
        this.replying = new SimpleStringProperty(replying);
        this.text = new SimpleStringProperty(text);
    }

    public String getComment_id() {
        return comment_id.get();
    }

    public SimpleStringProperty comment_idProperty() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id.set(comment_id);
    }

    public String getReplying() {
        return replying.get();
    }

    public SimpleStringProperty replyingProperty() {
        return replying;
    }

    public void setReplying(String replying) {
        this.replying.set(replying);
    }

    public String getText() {
        return text.get();
    }

    public SimpleStringProperty textProperty() {
        return text;
    }

    public void setText(String text) {
        this.text.set(text);
    }
}
