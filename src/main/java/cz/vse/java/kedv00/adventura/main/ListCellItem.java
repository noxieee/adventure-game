package cz.vse.java.kedv00.adventura.main;

import cz.vse.java.kedv00.adventura.api.IItem;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;

/*******************************************************************************
 * Třída, která do listview itemů přidá obrázky na základě jejich jména.
 */
public class ListCellItem extends ListCell<IItem>
{
    @Override
    protected void updateItem(IItem item, boolean b)
    {
        super.updateItem(item, b);

        if(b)
        {
            setText(null);
            setGraphic(null);
        }
        else {
            setText(item.name());
            String path = getClass().getResource("item_images/" + item.name().toLowerCase() + ".jpg").toExternalForm();
            ImageView iw = new ImageView(path);
            iw.setFitHeight(55.0);
            iw.setFitWidth(80.0);
            setGraphic(iw);
        }
    }
}
