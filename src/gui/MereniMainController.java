package gui;

import data.Mereni;
import data.MereniElektrina;
import data.MereniVoda;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import manager.Pozice;
import manager.SpravceMereni;
import structure.IAbstrDoubleList;

import java.io.IOException;
import java.time.LocalDate;

public class MereniMainController {
    @FXML
    private ListView<String> lvItems;
    @FXML
    private MenuItem menuItemSave;
    @FXML
    private Menu menuClear;
    @FXML
    private Button btnZpristupniMereni;
    @FXML
    private Label lblPrumernaSpotreba;
    @FXML
    private Button btnVlozMereni;
    @FXML
    private Label lblMaxSpotreba;
    @FXML
    private Button btnOdeberMereni;
    @FXML
    private Button btnMaxSpotreba;
    @FXML
    private Button btnPrumernaSpotreba;
    @FXML
    private TextField tfDen;
    @FXML
    private Button btnNajdiMereni;
    @FXML
    private MenuItem menuItemLoad;
    @FXML
    private Label lblPocetPolozek;
    @FXML
    private ChoiceBox<Pozice> cBPozicePrvku;
    @FXML
    private MenuItem menuItemClear;
    @FXML
    private MenuItem menuItemGenerate;
    @FXML
    private DatePicker prumSpotrebaOd;
    @FXML
    private DatePicker prumSpotrebaDo;
    @FXML
    private DatePicker maxSpotrebaOd;
    @FXML
    private DatePicker maxSpotrebaDo;
    @FXML
    private TextField tfSenzorPrumer;
    @FXML
    private TextField tfSenzorMax;
    @FXML
    private TextField tfSenzorDen;
    @FXML
    private Button btnRefresh;

    private SpravceMereni spravce;

    @FXML
    public void initialize() {
        this.spravce = new SpravceMereni();
        StringProperty itemsCount = new SimpleStringProperty();
        itemsCount.bind(Bindings.concat("Počet položek: ").concat(Bindings.size(lvItems.getItems()).asString()));
        lblPocetPolozek.textProperty().bind(itemsCount);
        cBPozicePrvku.setItems(FXCollections.observableArrayList(Pozice.values()));
        cBPozicePrvku.getSelectionModel().select(0);
    }

    @FXML
    public void saveDataFromList(ActionEvent actionEvent) {
        spravce.ulozData();
    }

    @FXML
    public void loadDataFromFile(ActionEvent actionEvent) {
        spravce.nactiData();
        refreshListView();
    }

    @FXML
    public void zpristupniMereni(ActionEvent actionEvent) {
        if (!lvItems.getItems().isEmpty()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("mereniDialog.fxml"));
                Parent root = loader.load();

                MereniDialogController controller = loader.getController();
                controller.getBtnOk().setDisable(true);
                controller.getMenuItemRandomGen().setDisable(true);

                Mereni mereni = spravce.zpristupniMereni(cBPozicePrvku.getValue());
                if (mereni != null) {   // predchudce prvniho nebo naslednik posledniho
                    controller.setTfIdSenzoru(String.valueOf(mereni.getIdSenzoru()));
                    controller.setDatePicker(mereni.getCasMereni());
                    controller.setcBTypSenzoru(mereni.getTypSenzoru());
                    if (mereni instanceof MereniVoda mereniVoda) {
                        controller.setTfSpotrebaM3(String.valueOf(mereniVoda.getSpotrebaM3()));
                        controller.getTfSpotrebaVT().setDisable(true);
                        controller.getTfSpotrebaNT().setDisable(true);
                    } else if (mereni instanceof MereniElektrina mereniElektrina) {
                        controller.setTfSpotrebaVT(String.valueOf(mereniElektrina.getSpotrebaVT()));
                        controller.setTfSpotrebaNT(String.valueOf(mereniElektrina.getSpotrebaNT()));
                    }
                }

                Stage dialogStage = new Stage();
                dialogStage.initModality(Modality.APPLICATION_MODAL);
                dialogStage.setTitle("ZpristupniMereni");
                dialogStage.setScene(new Scene(root));
                dialogStage.showAndWait();
            } catch (IOException e) {
                System.err.println("zpristupniMereni(): Chyba při spouštění dialogu!");
            }
        }
    }

    @FXML
    public void vlozMereni(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("mereniDialog.fxml"));
            Parent root = loader.load();

            MereniDialogController controller = loader.getController();
            controller.setController(this);
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setTitle("VlozMereni");
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();
            refreshListView();
        } catch (IOException e) {
            System.err.println("vlozMereni(): Chyba při spouštění dialogu!");
        }

    }

    @FXML
    public void odeberMereni(ActionEvent actionEvent) {
        Pozice pozice = cBPozicePrvku.getValue();
        Mereni mereni = spravce.odeberMereni(pozice);
        if (mereni != null) {
            System.out.println("Odebráno " + pozice + ": " + mereni);
        }
        refreshListView();
    }

    @FXML
    public void spocitejMaxSpotrebu(ActionEvent actionEvent) {
        try {
            int id = Integer.parseInt(tfSenzorMax.getText());
            LocalDate dateOd = maxSpotrebaOd.getValue();
            LocalDate dateDo = maxSpotrebaDo.getValue();
            if (dateOd == null) { System.err.println("Max spotřeba: datum od = NULL"); }
            if (dateDo == null) { System.err.println("Max spotřeba: datum do = NULL"); }
            if (dateOd != null && dateDo != null) {
                double maxSpotreba = spravce.maxSpotreba(id, dateOd.atStartOfDay(), dateDo.atStartOfDay());
                lblMaxSpotreba.setText(String.valueOf(maxSpotreba));
            }
        } catch (NumberFormatException ex) {
            System.err.println("Neplatné id senzoru pro maximální spotřebu!");
        }
    }

    @FXML
    public void spocitejPrumernouSpotrebu(ActionEvent actionEvent) {
        try {
            int id = Integer.parseInt(tfSenzorPrumer.getText());
            LocalDate dateOd = prumSpotrebaOd.getValue();
            LocalDate dateDo = prumSpotrebaDo.getValue();
            if (dateOd == null) { System.err.println("Průměrná spotřeba: datum od = NULL"); }
            if (dateDo == null) { System.err.println("Průměrná spotřeba: datum do = NULL"); }
            if (dateOd != null && dateDo != null) {
                double maxSpotreba = spravce.prumerSpotreba(id, dateOd.atStartOfDay(), dateDo.atStartOfDay());
                 lblPrumernaSpotreba.setText(String.valueOf(maxSpotreba));
            }
        } catch (NumberFormatException ex) {
            System.err.println("Neplatné id senzoru pro průměrnou spotřebu!");
        }
    }

    @FXML
    public void najdiMereni(ActionEvent actionEvent) {
        try {
            int den = Integer.parseInt(tfDen.getText());
            int id = Integer.parseInt(tfSenzorDen.getText());
            if (den < 1 || den > 31) {
                System.err.println("Toto není validní hodnota pro den!");
                return;
            }
            LocalDate date = LocalDate.of(2023, 10, den);
            IAbstrDoubleList<Mereni> list = spravce.mereniDen(id, date);
            if (list.jePrazdny()) {
                System.out.println("Nebyla nenalezena žádná měření odpovídající těmto parametrům...");
            }
            refreshListView(list);
        } catch (NumberFormatException ex) {
            System.err.println("Hodnoty pro najití měření musí být číselné!");
        }

    }

    @FXML
    public void clearAll(ActionEvent actionEvent) {
        lvItems.getItems().clear();
        lblMaxSpotreba.setText("00.00");
        lblPrumernaSpotreba.setText("00.00");
        maxSpotrebaDo.getEditor().clear();
        maxSpotrebaOd.getEditor().clear();
        prumSpotrebaDo.getEditor().clear();
        prumSpotrebaOd.getEditor().clear();
        tfSenzorMax.clear();
        tfSenzorPrumer.clear();
        tfSenzorDen.clear();
        tfDen.clear();
        spravce.zrus();
    }

    @FXML
    public void generateData(ActionEvent actionEvent) {
        spravce.generujData();
        refreshListView();
    }

    @FXML
    public void reloadData(ActionEvent actionEvent) {
        refreshListView();
    }

    private void refreshListView() {
        lvItems.getItems().clear();
        for (Mereni item: spravce.getAbstrDoubleList()) {
            lvItems.getItems().add(item.toString());
        }
    }

    private void refreshListView(IAbstrDoubleList<Mereni> list) {
        lvItems.getItems().clear();
        for (Mereni item: list) {
            lvItems.getItems().add(item.toString());
        }
    }

    public SpravceMereni getSpravce() {
        return spravce;
    }
    public ChoiceBox<Pozice> getcBPozicePrvku() {
        return cBPozicePrvku;
    }
}