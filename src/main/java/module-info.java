module cz.vse.java.kedv00.adventura {
    requires javafx.controls;
    requires javafx.fxml;

    opens cz.vse.java.kedv00.adventura.main to javafx.fxml;
    exports cz.vse.java.kedv00.adventura.main;
}