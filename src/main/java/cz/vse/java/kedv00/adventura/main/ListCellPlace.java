package cz.vse.java.kedv00.adventura.main;

import cz.vse.java.kedv00.adventura.api.IPlace;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;

public class ListCellPlace extends ListCell<IPlace>
{
    @Override
    protected void updateItem(IPlace place, boolean b)
    {
        super.updateItem(place, b);

        if(b)
        {
            setText(null);
            setGraphic(null);
        }
        else {
            setText(place.name());
            ImageView iw = new ImageView(getClass().getResource("place_images/" + place.name().toLowerCase() + ".jpg").toExternalForm());
            iw.setFitHeight(40.0);
            iw.setFitWidth(60.0);
            setGraphic(iw);
        }
    }
}
