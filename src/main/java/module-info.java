module com.example.team4week4 {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires javafx.graphics;
    requires com.google.gson;
    requires jdk.compiler;
    requires transitive log4j;
    requires javafx.base;
    requires java.xml;
    requires javafx.media;
    //requires junit;

    opens gamemaker to javafx.fxml;
    exports gamemaker;
    exports gamemaker.view;
    opens gamemaker.view to javafx.fxml;
    exports gamemaker.view.components;
    opens gamemaker.view.components to javafx.fxml;
}