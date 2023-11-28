package gui;

import data.Mereni;
import data.MereniElektrina;
import data.MereniVoda;
import data.TypSenzoru;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import manager.ISpravceMereni;
import manager.Pozice;
import util.Generator;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class MereniDialogController
{
    @FXML
    private TextField tfIdSenzoru;
    @FXML
    private TextField tfSpotrebaNT;
    @FXML
    private TextField tfSpotrebaVT;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ChoiceBox<TypSenzoru> cBTypSenzoru;
    @FXML
    private TextField tfSpotrebaM3;
    @FXML
    private Button btnOk;
    @FXML
    private Button btnCancel;
    @FXML
    private Menu menuItemRandomGen;

    private MereniMainController controller;


    @FXML
    public void initialize() {
        cBTypSenzoru.setItems(FXCollections.observableArrayList(TypSenzoru.values()));
        cBTypSenzoru.getSelectionModel().select(0);
    }

    @FXML
    public void generateRandomData(ActionEvent actionEvent) {
        Generator generator = new Generator(controller.getSpravce().getAbstrDoubleList());
        Mereni mereni = generator.generateMereni();
        if (mereni instanceof MereniElektrina mereniElektrina) {
            tfSpotrebaNT.setDisable(false);
            tfSpotrebaVT.setDisable(false);
            tfIdSenzoru.setText(String.valueOf(mereniElektrina.getIdSenzoru()));
            datePicker.setValue(mereniElektrina.getCasMereni().toLocalDate());
            cBTypSenzoru.setValue(mereniElektrina.getTypSenzoru());
            tfSpotrebaVT.setText(String.valueOf(mereniElektrina.getSpotrebaVT()));
            tfSpotrebaNT.setText(String.valueOf(mereniElektrina.getSpotrebaNT()));
            tfSpotrebaM3.setDisable(true);
            tfSpotrebaM3.setText(null);
        } else if (mereni instanceof MereniVoda mereniVoda) {
            tfSpotrebaM3.setDisable(false);
            tfIdSenzoru.setText(String.valueOf(mereniVoda.getIdSenzoru()));
            datePicker.setValue(mereniVoda.getCasMereni().toLocalDate());
            cBTypSenzoru.setValue(mereniVoda.getTypSenzoru());
            tfSpotrebaM3.setText(String.valueOf(mereniVoda.getSpotrebaM3()));
            tfSpotrebaNT.setDisable(true);
            tfSpotrebaNT.setText(null);
            tfSpotrebaVT.setDisable(true);
            tfSpotrebaVT.setText(null);
        }
    }

    @FXML
    public void submitMereni(ActionEvent actionEvent) {
        ISpravceMereni spravceMereni = controller.getSpravce();
        Pozice pozicePrvku = controller.getcBPozicePrvku().getValue();

        try {
            int id = 0;
            LocalDate date = LocalDate.now();
            boolean allSet = true;
            if (tfIdSenzoru.getText() != null) {
                id = Integer.parseInt(tfIdSenzoru.getText());
            } else {
                System.err.println("ID senzoru nemuze byt null!");
                allSet = false;
            }
            TypSenzoru typSenzoru = cBTypSenzoru.getValue();
            if (datePicker.getValue() != null) {
                date = datePicker.getValue();
            } else {
                System.err.println("Čas měření nemůže byt null!");
                allSet = false;
            }
            if (tfSpotrebaM3.getText() != null && allSet) {
                double spotrebaM3 = Double.parseDouble(tfSpotrebaM3.getText());
                MereniVoda mereniVoda = new MereniVoda(id, typSenzoru, date.atStartOfDay(), spotrebaM3);
                spravceMereni.vlozMereni(mereniVoda, pozicePrvku);
                if (pozicePrvku != Pozice.AKTUALNI) { System.out.println("Měření vody vloženo na pozici " + pozicePrvku); }
                vypniDialog();
            } else if (tfSpotrebaNT.getText() != null && tfSpotrebaVT.getText() != null) {
                double spotrebaNt = Double.parseDouble(tfSpotrebaNT.getText());
                double spotrebaVt = Double.parseDouble(tfSpotrebaVT.getText());
                MereniElektrina mereniElektrina = new MereniElektrina(id, typSenzoru, date.atStartOfDay(), spotrebaVt, spotrebaNt);
                spravceMereni.vlozMereni(mereniElektrina, pozicePrvku);
                if (pozicePrvku != Pozice.AKTUALNI) { System.out.println("Měření elektřiny vloženo na pozici " + pozicePrvku); }
                vypniDialog();
            }
        } catch (NumberFormatException ex) {
            System.err.println("ID senzoru musí být číslo!");
        }
    }

    @FXML
    public void cancelMereni(ActionEvent actionEvent) {
        vypniDialog();
    }

    private void vypniDialog() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    public TextField getTfIdSenzoru() {
        return tfIdSenzoru;
    }
    public TextField getTfSpotrebaNT() {
        return tfSpotrebaNT;
    }
    public TextField getTfSpotrebaVT() {
        return tfSpotrebaVT;
    }
    public DatePicker getDatePicker() {
        return datePicker;
    }
    public ChoiceBox<TypSenzoru> getcBTypSenzoru() {
        return cBTypSenzoru;
    }
    public TextField getTfSpotrebaM3() {
        return tfSpotrebaM3;
    }
    public Button getBtnOk() {
        return btnOk;
    }
    public Menu getMenuItemRandomGen() {
        return menuItemRandomGen;
    }
    public void setTfIdSenzoru(String tfIdSenzoru) {
        this.tfIdSenzoru.setText(tfIdSenzoru);
    }
    public void setTfSpotrebaNT(String tfSpotrebaNT) {
        this.tfSpotrebaNT.setText(tfSpotrebaNT);
    }
    public void setTfSpotrebaVT(String tfSpotrebaVT) {
        this.tfSpotrebaVT.setText(tfSpotrebaVT);
    }
    public void setDatePicker(LocalDateTime date) {
        this.datePicker.setValue(date.toLocalDate());
    }
    public void setcBTypSenzoru(TypSenzoru typSenzoru) {
        this.cBTypSenzoru.setValue(typSenzoru);
    }
    public void setTfSpotrebaM3(String tfSpotrebaM3) {
        this.tfSpotrebaM3.setText(tfSpotrebaM3);
    }
    public void setController(MereniMainController controller) {
        this.controller = controller;
    }
}