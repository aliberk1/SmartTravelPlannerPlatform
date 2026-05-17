package com.planner.gui;

// Model sınıflarını içe aktarıyoruz: Şehir ve hava durumu bilgileri
import com.planner.model.City;
import com.planner.model.WeatherState;

// Command (Komut) tasarım deseni: Geri alma / yeniden yapma işlemleri için
import com.planner.pattern.command.*;

// Composite (Bileşik) tasarım deseni: Aktivite planlarını ağaç yapısında tutmak için
import com.planner.pattern.composite.ActivityLeaf;
import com.planner.pattern.composite.ActivityPlan;
import com.planner.pattern.composite.PlanComponent;

// Decorator (Dekoratör) tasarım deseni: Ziyaret türlerine ek özellikler (müze, park vb.) eklemek için
import com.planner.pattern.decorator.*;

// Iterator (Yineleyici) tasarım deseni: Şehirleri hava durumuna göre filtrelemek için
import com.planner.pattern.iterator.*;

// Observer (Gözlemci) tasarım deseni: Hava durumu güncellemelerini dinlemek için
import com.planner.pattern.observer.WeatherObserver;
import com.planner.pattern.observer.WeatherProvider;

// Singleton tasarım deseni: Tüm uygulama boyunca tek bir şehir deposu kullanmak için
import com.planner.pattern.singleton.CityRepository;

// Strategy (Strateji) tasarım deseni: Farklı sıralama yöntemlerini seçmek için
import com.planner.pattern.strategy.*;

// JavaFX kullanıcı arayüzü bileşenleri
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MainController: Uygulamanın ana kontrol sınıfı.
 * WeatherObserver arayüzünü uygulayarak Observer desenine katılıyor;
 * hava durumu değiştiğinde updateWeather() metodu otomatik çağrılır.
 */
public class MainController implements WeatherObserver {

    // Ana JavaFX düzen bileşeni: Ekranı üst, orta, alt bölümlere ayırır
    private BorderPane root;

    // Tüm şehirlerin tutulduğu liste
    private List<City> allCities;

    // --- Üst kontrol alanı bileşenleri ---
    // Şehirleri isim / nüfus / alan'a göre sıralamak için açılır menü (Strategy deseni tetikler)
    private ComboBox<String> sortComboBox;
    // Şehirleri hava durumuna göre filtrelemek için açılır menü (Iterator deseni tetikler)
    private ComboBox<String> filterComboBox;

    // --- Sütun 1: Tüm Şehirler ---
    // Sıralanmış tüm şehirlerin gösterildiği liste görünümü
    private ListView<City> allCitiesListView;

    // --- Sütun 2: Filtrelenmiş Şehirler ---
    // Seçili hava durumuna göre filtrelenmiş şehirlerin gösterildiği liste
    private ListView<City> filteredCitiesListView;

    // --- Sütun 3: Planlayıcı ---
    // Seçili şehrin adı, hava durumu ve sıcaklığını gösteren başlık etiketi
    private Label previewHeaderLabel;
    // Önceden tanımlı aktivite seçeneklerinin onay kutuları
    private CheckBox cbMuseum, cbCenter, cbMall, cbPark;
    // Kullanıcının kendi aktivitesini ekleyebileceği metin alanları (ad, maliyet, süre)
    private TextField customLabelTF, customCostTF, customHoursTF;
    // Seçilen aktivitelerin metin önizlemesini gösteren alan
    private TextArea planPreviewArea;
    // Önizlemedeki toplam süre ve maliyet etiketi
    private Label previewTotalLabel;

    // --- Sütun 4: Aktivite Ağacı ---
    // Aktivite ağacının başlığını gösteren etiket
    private Label treeTitleLabel;
    // Aktif şehri gösteren etiket
    private Label treeActiveCityLabel;
    // Yeni bileşik plan düğümü için isim alanı
    private TextField planNameTF;
    // Aktivite planını ağaç yapısında gösteren TreeView (Composite deseni görsel temsili)
    private TreeView<PlanComponent> planTreeView;
    // Ağacın kök düğümü
    private TreeItem<PlanComponent> rootTreeItem;

    // --- Alt kısım grafikleri ---
    // Şehirlerin sıcaklıklarını gösteren çubuk grafik
    private BarChart<String, Number> tempChart;
    // Hava durumu dağılımını gösteren pasta grafik
    private PieChart weatherPieChart;

    // --- Durum çubuğu ---
    // Aktif şehir ağacının toplam süre ve maliyetini gösteren etiket
    private Label currentTreeTotalLabel;
    // Geri alma / yeniden yapma durumunu gösteren etiket
    private Label undoRedoLabel;

    // Pasta grafiğin özel açıklama kutusu
    private VBox customLegendBox;

    // --- İş mantığı bileşenleri ---
    // Command Manager: Geri alma (Undo) ve yeniden yapma (Redo) işlemlerini yönetir
    private CommandManager commandManager;
    // Hava durumu sağlayıcısı: Arka planda çalışarak hava durumunu günceller (Observer)
    private WeatherProvider weatherProvider;
    // Her şehir için ayrı bir aktivite planı saklanır (şehir adı → plan eşlemesi)
    private Map<String, ActivityPlan> cityPlans = new HashMap<>();
    // O an seçili olan aktif şehir
    private City activeCity;
    // Aktif şehrin kök aktivite planı (Composite deseninin kökü)
    private ActivityPlan rootPlan;

    /**
     * Yapıcı metod: Uygulama ilk açıldığında çalışır.
     * Singleton ile şehirleri alır, arayüzü ve iş mantığını başlatır,
     * ardından listeyi ve grafikleri ilk kez günceller.
     */
    public MainController() {
        // Singleton deseni: CityRepository uygulamada tek bir nesne olarak bulunur
        allCities = new ArrayList<>(CityRepository.getInstance().getCities());
        // Command Manager'ı başlatıyoruz (Undo/Redo mekanizması)
        commandManager = new CommandManager();

        initUI();            // Kullanıcı arayüzünü oluştur
        initLogic();         // Hava durumu gözlemcisini başlat
        refreshAllCitiesList(); // Şehir listesini doldur
        updateCharts();      // Grafikleri doldur
    }

    /**
     * JavaFX düzen kökünü (BorderPane) döndürür.
     * MainApp bu metodu çağırarak sahneye ekler.
     */
    public BorderPane getRootView() {
        return root;
    }

    /**
     * initUI: Tüm görsel bileşenleri programatik olarak oluşturur ve ekrana yerleştirir.
     * FXML kullanılmıyor; düzen tamamen Java kodu ile oluşturuluyor.
     */
    private void initUI() {
        root = new BorderPane();

        // --- Üst kontrol paneli ---
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(5);
        grid.setPadding(new Insets(5));

        // Izgara sütunlarını eşit genişlikte böl
        ColumnConstraints cc1 = new ColumnConstraints();
        cc1.setPercentWidth(50);
        ColumnConstraints cc2 = new ColumnConstraints();
        cc2.setPercentWidth(50);
        grid.getColumnConstraints().addAll(cc1, cc2);

        // Sıralama açılır menüsü: Strategy desenini tetikler
        Label sortLbl = new Label("Sort Cities:");
        sortComboBox = new ComboBox<>(FXCollections.observableArrayList("Sort by Name", "Sort by Population", "Sort by Area"));
        sortComboBox.getSelectionModel().selectFirst();
        sortComboBox.setOnAction(e -> applySortStrategy()); // Seçim değişince strateji uygula
        sortComboBox.setMaxWidth(Double.MAX_VALUE);
        grid.add(sortLbl, 0, 0);
        grid.add(sortComboBox, 1, 0);
        GridPane.setHgrow(sortComboBox, Priority.ALWAYS);

        // Filtreleme açılır menüsü: Iterator desenini tetikler
        Label filterLbl = new Label("Filter by Weather:");
        filterComboBox = new ComboBox<>(FXCollections.observableArrayList("ALL", "SUNNY", "CLOUDY", "RAINY", "SNOWY"));
        filterComboBox.getSelectionModel().selectFirst();
        filterComboBox.setOnAction(e -> applyIteratorFilter()); // Seçim değişince iterator uygula
        filterComboBox.setMaxWidth(Double.MAX_VALUE);
        grid.add(filterLbl, 0, 1);
        grid.add(filterComboBox, 1, 1);
        GridPane.setHgrow(filterComboBox, Priority.ALWAYS);

        // "Controls" başlık etiketi ve yatay ayırıcı çizgi
        HBox headerBox = new HBox(5);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        Label controlsLbl = new Label("Controls");
        controlsLbl.setFont(Font.font("System", FontWeight.BOLD, 12));
        Separator hLine = new Separator();
        HBox.setHgrow(hLine, Priority.ALWAYS);
        headerBox.getChildren().addAll(controlsLbl, hLine);

        VBox topControls = new VBox(5, headerBox, grid);
        topControls.setPadding(new Insets(5, 10, 5, 10));

        // --- 4 sütunlu bölünmüş panel ---
        SplitPane splitPane4Cols = new SplitPane();

        // Sütun 1: Tüm şehirlerin listesi
        VBox col1Box = new VBox(5);
        col1Box.setPadding(new Insets(5));
        Label col1Title = new Label("All Cities (resorted only when sort type changes)");
        allCitiesListView = new ListView<>();
        setupCityListView(allCitiesListView); // Özel hücre biçimlendirmesi ayarla
        VBox.setVgrow(allCitiesListView, Priority.ALWAYS);
        col1Box.getChildren().addAll(col1Title, allCitiesListView);

        // Sütun 2: Hava durumuna göre filtrelenmiş şehirler
        VBox col2Box = new VBox(5);
        col2Box.setPadding(new Insets(5));
        Label col2Title = new Label("Cities by Weather");
        filteredCitiesListView = new ListView<>();
        setupCityListView(filteredCitiesListView);
        VBox.setVgrow(filteredCitiesListView, Priority.ALWAYS);
        col2Box.getChildren().addAll(col2Title, filteredCitiesListView);

        // Sütun 3: Planlayıcı paneli
        VBox col3Box = new VBox(5);
        col3Box.setPadding(new Insets(5));
        Label col3Title = new Label("Planner");
        col3Title.setFont(Font.font("System", FontWeight.BOLD, 12));
        // Seçili şehrin özet bilgisini gösterir
        previewHeaderLabel = new Label("Preview for - | Weather: - | Temp: -");

        Label availActivitiesLbl = new Label("Available Activities");
        availActivitiesLbl.setFont(Font.font("System", FontWeight.BOLD, 12));

        // Onay kutuları: Decorator desenindeki hazır ziyaret türlerini temsil eder
        VBox cbBox = new VBox(5);
        cbBox.setStyle("-fx-border-color: lightgray; -fx-padding: 5;");
        cbMuseum = new CheckBox("Visit Museum (2.0 h, $18.0)");
        cbCenter = new CheckBox("Visit City Center (1.5 h, $0.0)");
        cbMall = new CheckBox("Visit Shopping Mall (2.0 h, $25.0)");
        cbPark = new CheckBox("Walk in the Park (1.0 h, $7.0)");
        cbBox.getChildren().addAll(cbMuseum, cbCenter, cbMall, cbPark);

        // Herhangi bir onay kutusu değiştiğinde önizlemeyi güncelle
        cbMuseum.setOnAction(e -> updatePlanPreview());
        cbCenter.setOnAction(e -> updatePlanPreview());
        cbMall.setOnAction(e -> updatePlanPreview());
        cbPark.setOnAction(e -> updatePlanPreview());

        // Kullanıcı tanımlı (özel) aktivite ekleme alanı
        Label addCustomLbl = new Label("Add New Activity Option");
        addCustomLbl.setFont(Font.font("System", FontWeight.BOLD, 12));
        HBox customBox = new HBox(5);
        customBox.setAlignment(Pos.CENTER_LEFT);
        customLabelTF = new TextField(); customLabelTF.setPrefWidth(60);
        customCostTF = new TextField(); customCostTF.setPrefWidth(40);
        customHoursTF = new TextField(); customHoursTF.setPrefWidth(40);
        Button addCustomBtn = new Button("Add Custom Activity Option");
        customBox.getChildren().addAll(new Label("Label:"), customLabelTF, new Label("Cost:"), customCostTF, new Label("Hours:"), customHoursTF, addCustomBtn);

        // "Add Custom Activity Option" butonu tıklandığında özel aktivite ekle
        addCustomBtn.setOnAction(e -> addCustomActivity());

        // Seçili aktivitelerin metin önizlemesi
        Label planPreviewLbl = new Label("Plan Preview");
        planPreviewLbl.setFont(Font.font("System", FontWeight.BOLD, 12));
        planPreviewArea = new TextArea();
        planPreviewArea.setEditable(false); // Kullanıcı doğrudan düzenleyemez
        VBox.setVgrow(planPreviewArea, Priority.ALWAYS);

        // Önizleme toplamı etiketi ve "Add Selected Activities" butonu
        HBox previewBottomBox = new HBox(10);
        previewBottomBox.setAlignment(Pos.CENTER_RIGHT);
        previewTotalLabel = new Label("Preview total: 0.0 hours / $0.0");
        Button addSelectedBtn = new Button("Add Selected Activities");
        // Seçili aktiviteleri plan ağacına Command deseni ile ekle
        addSelectedBtn.setOnAction(e -> addSelectedActivities());
        previewBottomBox.getChildren().addAll(previewTotalLabel, addSelectedBtn);

        col3Box.getChildren().addAll(col3Title, previewHeaderLabel, availActivitiesLbl, cbBox, addCustomLbl, customBox, planPreviewLbl, planPreviewArea, previewBottomBox);

        // Sütun 4: Aktivite ağacı (Composite deseninin görsel temsili)
        VBox col4Box = new VBox(5);
        col4Box.setPadding(new Insets(5));
        treeTitleLabel = new Label("Activity Tree for -");
        treeTitleLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        treeActiveCityLabel = new Label("Active city: -");

        // Yeni bir bileşik plan düğümü ekleme alanı
        Label addNodeLbl = new Label("Add Composite Plan Node");
        addNodeLbl.setFont(Font.font("System", FontWeight.BOLD, 12));
        HBox planNameBox = new HBox(5);
        planNameBox.setAlignment(Pos.CENTER_LEFT);
        planNameTF = new TextField();
        HBox.setHgrow(planNameTF, Priority.ALWAYS);
        Button addPlanNodeBtn = new Button("Add Plan Node");
        addPlanNodeBtn.setOnAction(e -> addPlanNode()); // Composite düğüm ekle
        planNameBox.getChildren().addAll(new Label("Plan name:"), planNameTF, addPlanNodeBtn);

        // Aktivite planını ağaç olarak gösteren TreeView
        // Her düğüm bir PlanComponent'tır: ya ActivityPlan (composite) ya da ActivityLeaf (yaprak)
        planTreeView = new TreeView<>();
        planTreeView.setCellFactory(tv -> new TreeCell<PlanComponent>() {
            @Override
            protected void updateItem(PlanComponent item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    // Düğümün adını, toplam süresini ve toplam maliyetini göster
                    setText(String.format("%s [%.1f h, $%.1f]", item.getName(), item.getTotalTime(), item.getTotalCost()));
                }
            }
        });
        // Ağaçta seçim değiştiğinde önizlemeyi güncelle
        planTreeView.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> updatePlanPreview());
        VBox.setVgrow(planTreeView, Priority.ALWAYS);

        // Düğüm işlemleri için 3 sütunlu buton ızgarası
        GridPane btnGrid = new GridPane();
        btnGrid.setHgap(5); btnGrid.setVgap(5);
        ColumnConstraints cc = new ColumnConstraints();
        cc.setPercentWidth(33.33);
        btnGrid.getColumnConstraints().addAll(cc, cc, cc);

        // Seçili düğümü kaldır (Command deseni: RemoveComponentCommand)
        Button removeNodeBtn = new Button("Remove Selected Node");
        removeNodeBtn.setMaxWidth(Double.MAX_VALUE);
        removeNodeBtn.setOnAction(e -> removeSelectedNode());

        // Seçili düğümü yukarı taşı (Command deseni: MoveComponentCommand)
        Button moveUpBtn = new Button("Move Up");
        moveUpBtn.setMaxWidth(Double.MAX_VALUE);
        moveUpBtn.setOnAction(e -> moveNode(-1));

        // Seçili düğümü aşağı taşı
        Button moveDownBtn = new Button("Move Down");
        moveDownBtn.setMaxWidth(Double.MAX_VALUE);
        moveDownBtn.setOnAction(e -> moveNode(1));

        // Aktif şehrin tüm aktivite ağacını temizle (Command deseni: ClearPlanCommand)
        Button clearTreeBtn = new Button("Clear Active City Tree");
        clearTreeBtn.setMaxWidth(Double.MAX_VALUE);
        clearTreeBtn.setOnAction(e -> {
            if(rootPlan != null) {
                commandManager.executeCommand(new ClearPlanCommand(rootPlan));
                refreshPlanView();
            }
        });

        // Geri al (Undo): Son komutu geri alır
        Button undoBtn = new Button("Undo");
        undoBtn.setMaxWidth(Double.MAX_VALUE);
        undoBtn.setOnAction(e -> { commandManager.undo(); refreshPlanView(); });

        // Yeniden yap (Redo): Geri alınan komutu tekrar uygular
        Button redoBtn = new Button("Redo");
        redoBtn.setMaxWidth(Double.MAX_VALUE);
        redoBtn.setOnAction(e -> { commandManager.redo(); refreshPlanView(); });

        btnGrid.add(removeNodeBtn, 0, 0);
        btnGrid.add(moveUpBtn, 1, 0);
        btnGrid.add(moveDownBtn, 2, 0);
        btnGrid.add(clearTreeBtn, 0, 1);
        btnGrid.add(undoBtn, 1, 1);
        btnGrid.add(redoBtn, 2, 1);

        col4Box.getChildren().addAll(treeTitleLabel, treeActiveCityLabel, addNodeLbl, planNameBox, planTreeView, btnGrid);

        // 4 sütunu bölünmüş panele ekle; divider konumları yüzde olarak belirlenir
        splitPane4Cols.getItems().addAll(col1Box, col2Box, col3Box, col4Box);
        splitPane4Cols.setDividerPositions(0.20, 0.40, 0.70);
        VBox.setVgrow(splitPane4Cols, Priority.ALWAYS);

        VBox topMainBox = new VBox(topControls, splitPane4Cols);

        // --- Alt kısım: Grafikler ---
        SplitPane bottomSplitPane = new SplitPane();
        bottomSplitPane.setPadding(new Insets(10));

        // Çubuk grafik eksenleri
        CategoryAxis xAxis = new CategoryAxis(); // X ekseni: şehir adları
        NumberAxis yAxis = new NumberAxis();
        // Y ekseni etiketlerini ve işaretlerini gizle; sadece çubuklar görünsün
        yAxis.setTickLabelsVisible(false);
        yAxis.setOpacity(0);
        yAxis.setTickMarkVisible(false);

        // Şehir sıcaklıklarını gösteren çubuk grafik
        tempChart = new BarChart<>(xAxis, yAxis);
        tempChart.setTitle("City Temperatures");
        tempChart.setLegendVisible(false);

        // Hava durumu dağılımını gösteren pasta grafik
        weatherPieChart = new PieChart();
        weatherPieChart.setTitle("Weather Distribution");
        weatherPieChart.setLegendVisible(false);
        weatherPieChart.setLabelsVisible(false);

        // Pasta grafiğin yanına özel renk açıklamaları kutusu
        customLegendBox = new VBox(8);
        customLegendBox.setPadding(new Insets(0));
        customLegendBox.setAlignment(Pos.CENTER_LEFT);
        customLegendBox.setTranslateX(-20); // Pasta grafiğe biraz yaklaştır

        HBox pieContainer = new HBox(0, weatherPieChart, customLegendBox);
        pieContainer.setAlignment(Pos.CENTER);

        bottomSplitPane.getItems().addAll(tempChart, pieContainer);
        bottomSplitPane.setDividerPositions(0.5); // Eşit böl

        // Dikey ana bölünmüş panel: üstte kontroller + listeler, altta grafikler
        SplitPane mainSplit = new SplitPane();
        mainSplit.setOrientation(javafx.geometry.Orientation.VERTICAL);
        mainSplit.getItems().addAll(topMainBox, bottomSplitPane);
        mainSplit.setDividerPositions(0.7); // Üst kısım %70, alt kısım %30

        // --- Durum çubuğu: Ekranın en altında yer alır ---
        HBox statusBar = new HBox();
        statusBar.setPadding(new Insets(5));
        statusBar.setStyle("-fx-background-color: #f0f0f0;");
        // Sol taraf: aktif ağacın toplam süre ve maliyeti
        currentTreeTotalLabel = new Label("Current city tree total: 0.0 hours / $0.0");
        currentTreeTotalLabel.setFont(Font.font("System", FontWeight.BOLD, 10));

        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Sağ taraf: Undo ve Redo için mevcut komutların açıklamaları
        undoRedoLabel = new Label("Undo: Nothing | Redo: Nothing");
        undoRedoLabel.setFont(Font.font("System", FontWeight.BOLD, 10));
        statusBar.getChildren().addAll(currentTreeTotalLabel, spacer, undoRedoLabel);

        root.setCenter(mainSplit);
        root.setBottom(statusBar);

        // Şehir listelerinde seçim değiştiğinde o şehri aktif yap
        allCitiesListView.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if(newV != null) selectCity(newV);
        });
        filteredCitiesListView.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if(newV != null) selectCity(newV);
        });
    }

    /**
     * ListView'larda her şehir satırının nasıl görüneceğini belirler.
     * Ad, nüfus, alan, sıcaklık ve hava durumu bilgilerini gösterir.
     */
    private void setupCityListView(ListView<City> listView) {
        listView.setCellFactory(lv -> new ListCell<City>() {
            @Override
            protected void updateItem(City city, boolean empty) {
                super.updateItem(city, empty);
                if (empty || city == null) {
                    setText(null);
                } else {
                    // Biçimlendirilmiş şehir bilgisi
                    setText(String.format("%s (Pop: %d, Area: %.1f km², Temp: %.1f'C, %s)",
                        city.getName(), city.getPopulation(), city.getArea(), city.getCurrentTemperature(), city.getCurrentWeatherState()));
                }
            }
        });
    }

    /**
     * initLogic: Hava durumu sağlayıcısını (WeatherProvider) başlatır.
     * WeatherProvider bir Thread olarak çalışır; periyodik olarak şehirlerin
     * hava durumunu günceller ve kayıtlı gözlemcilere (Observer) haber verir.
     * setDaemon(true) ile ana uygulama kapandığında bu iş parçacığı da sonlanır.
     */
    private void initLogic() {
        weatherProvider = new WeatherProvider();
        // Bu controller'ı Observer olarak kaydet; updateWeather() çağrılacak
        weatherProvider.addObserver(this);
        Thread thread = new Thread(weatherProvider);
        thread.setDaemon(true); // Arka plan iş parçacığı: uygulama kapanınca durur
        thread.start();
    }

    /**
     * Uygulamayı kapatırken WeatherProvider'ı durdurur.
     * MainApp onStop() veya benzeri bir yerden çağırır.
     */
    public void stopWeatherProvider() {
        if (weatherProvider != null) {
            weatherProvider.stopProvider();
        }
    }

    /**
     * Strategy deseni: Kullanıcının seçtiği sıralama kriterine göre
     * doğru SortStrategy nesnesini oluşturur ve allCities listesini sıralar.
     * - SortByName: Alfabetik sıralama
     * - SortByPopulation: Nüfusa göre sıralama
     * - SortByArea: Alana göre sıralama
     */
    private void applySortStrategy() {
        String selection = sortComboBox.getValue();
        SortStrategy strategy;
        if ("Sort by Population".equals(selection)) {
            strategy = new SortByPopulation();
        } else if ("Sort by Area".equals(selection)) {
            strategy = new SortByArea();
        } else {
            strategy = new SortByName();
        }

        // Seçilen stratejiyi uygula ve liste görünümünü yenile
        strategy.sort(allCities);
        refreshAllCitiesList();
    }

    /**
     * Iterator deseni: Kullanıcının seçtiği hava durumuna göre
     * doğru CityIterator nesnesini oluşturur ve şehirleri filtreler.
     * - SunnyIterator: Yalnızca güneşli şehirleri döndürür
     * - CloudyIterator: Yalnızca bulutlu şehirleri döndürür
     * - RainyIterator: Yalnızca yağmurlu şehirleri döndürür
     * - SnowyIterator: Yalnızca karlı şehirleri döndürür
     * "ALL" seçiliyse tüm şehirler gösterilir.
     */
    private void applyIteratorFilter() {
        String selection = filterComboBox.getValue();
        List<City> filtered = new ArrayList<>();
        CityIterator iterator = null;

        if ("SUNNY".equals(selection)) {
            iterator = new SunnyIterator(allCities);
        } else if ("CLOUDY".equals(selection)) {
            iterator = new CloudyIterator(allCities);
        } else if ("RAINY".equals(selection)) {
            iterator = new RainyIterator(allCities);
        } else if ("SNOWY".equals(selection)) {
            iterator = new SnowyIterator(allCities);
        }

        if (iterator != null) {
            // hasNext() / next() ile şehirleri teker teker işle (Iterator kalıbı)
            while (iterator.hasNext()) {
                filtered.add(iterator.next());
            }
        } else {
            // "ALL" seçiliyse filtreleme yapma, hepsini ekle
            filtered.addAll(allCities);
        }

        // Filtrelenmiş listeyi ikinci sütuna yansıt
        filteredCitiesListView.setItems(FXCollections.observableArrayList(filtered));
    }

    /**
     * Birinci sütundaki "Tüm Şehirler" listesini günceller.
     * Sıralama değiştikten sonra mevcut seçimi koruyarak listeyi yeniler.
     */
    private void refreshAllCitiesList() {
        City selectedAll = allCitiesListView.getSelectionModel().getSelectedItem();
        allCitiesListView.setItems(FXCollections.observableArrayList(allCities));
        if (selectedAll != null) allCitiesListView.getSelectionModel().select(selectedAll);
    }

    /**
     * Observer deseni: WeatherProvider hava durumunu değiştirdiğinde bu metod çağrılır.
     * Platform.runLater() ile güncelleme JavaFX UI iş parçacığında yapılır
     * (JavaFX'te UI değişiklikleri yalnızca bu iş parçacığından yapılabilir).
     */
    @Override
    public void updateWeather() {
        Platform.runLater(() -> {
            refreshAllCitiesList();    // Şehir listesini güncelle
            applyIteratorFilter();     // Filtrelenmiş listeyi güncelle
            updateCharts();            // Grafikleri güncelle
            if (activeCity != null) {
                // Aktif şehrin başlık bilgisini güncelle (yeni hava durumu ve sıcaklık)
                previewHeaderLabel.setText(String.format("Preview for %s | Weather: %s | Temp: %.1f'C",
                    activeCity.getName(), activeCity.getCurrentWeatherState(), activeCity.getCurrentTemperature()));
                updatePlanPreview();   // Önizleme metnini de güncelle
            }
            // Hücreleri görsel olarak zorla yenile (Observer güncellemesi sonrası)
            allCitiesListView.refresh();
            filteredCitiesListView.refresh();
        });
    }

    /**
     * Çubuk grafik ve pasta grafik verilerini sıfırdan oluşturur.
     * Çubuk grafik: Her şehrin mevcut sıcaklığını gösterir.
     * Pasta grafik: Hava durumu türlerinin (SUNNY/CLOUDY/RAINY/SNOWY) dağılımını gösterir.
     */
    private void updateCharts() {
        tempChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Temperature");
        int sunnyCount = 0, cloudyCount = 0, rainyCount = 0, snowyCount = 0;

        for (City city : CityRepository.getInstance().getCities()) {
            String name = city.getName();
            // Uzun şehir adlarını kısalt (grafik ekseninde yer sıkıntısı olmasın)
            String shortName = name.length() > 5 ? name.substring(0, 5) + ".." : name;
            double temp = city.getCurrentTemperature();
            XYChart.Data<String, Number> data = new XYChart.Data<>(shortName, temp);

            // Çubuk oluşturulduğunda üzerine sıcaklık değerini yaz ve renklendirme uygula
            data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                if (newNode instanceof StackPane) {
                    StackPane bar = (StackPane) newNode;
                    javafx.scene.text.Text text = new javafx.scene.text.Text(String.format("%.1f", temp));
                    bar.getChildren().add(text);
                    // Pozitif sıcaklıkta etiket çubuğun üstünde, negatif ise altında görünsün
                    if (temp >= 0) {
                        text.setTranslateY(-15);
                    } else {
                        text.setTranslateY(15);
                    }
                    bar.setStyle("-fx-bar-fill: #ff6666;"); // Çubuk rengi kırmızı tonu
                }
            });
            series.getData().add(data);

            // Hava durumu türlerini say (pasta grafik için)
            switch (city.getCurrentWeatherState()) {
                case SUNNY: sunnyCount++; break;
                case CLOUDY: cloudyCount++; break;
                case RAINY: rainyCount++; break;
                case SNOWY: snowyCount++; break;
            }
        }
        tempChart.getData().add(series);

        // Pasta grafiği doldur; her dilime özel renk ata
        weatherPieChart.getData().clear();
        customLegendBox.getChildren().clear();
        addPieSlice("SUNNY (" + sunnyCount + ")", sunnyCount, "#E35D36");
        addPieSlice("CLOUDY (" + cloudyCount + ")", cloudyCount, "#ECAE24");
        addPieSlice("RAINY (" + rainyCount + ")", rainyCount, "#5CB85C");
        addPieSlice("SNOWY (" + snowyCount + ")", snowyCount, "#45A9C5");
    }

    /**
     * Pasta grafiğe bir dilim ekler; aynı zamanda sağ taraftaki özel
     * renk açıklamasına (legend) ilgili renkli daire + etiket ekler.
     * count == 0 ise dilim eklenmez (sıfır değeri grafiği bozar).
     */
    private void addPieSlice(String name, int count, String color) {
        if (count > 0) {
            PieChart.Data d = new PieChart.Data(name, count);
            weatherPieChart.getData().add(d);
            // Dilim oluşturulduğunda CSS rengi uygula
            d.nodeProperty().addListener((obs, oldNode, newNode) -> {
                if (newNode != null) {
                    newNode.setStyle("-fx-pie-color: " + color + ";");
                }
            });

            // Özel renk açıklaması: renkli daire + metin etiketi
            javafx.scene.shape.Circle circle = new javafx.scene.shape.Circle(6);
            circle.setFill(javafx.scene.paint.Color.web(color));
            Label label = new Label(name);
            label.setFont(Font.font("System", 12));
            label.setTextFill(javafx.scene.paint.Color.web("#333333"));
            HBox legendItem = new HBox(8, circle, label);
            legendItem.setAlignment(Pos.CENTER_LEFT);
            customLegendBox.getChildren().add(legendItem);
        }
    }

    /**
     * Kullanıcı bir şehre tıkladığında çalışır.
     * - Aktif şehri günceller.
     * - O şehre ait plan yoksa yeni boş bir ActivityPlan oluşturur (Composite kök).
     * - Başlık etiketlerini günceller.
     * - Ağaç ve önizleme görünümlerini yeniler.
     */
    private void selectCity(City city) {
        if (city == null) return;
        activeCity = city;

        // Her şehrin kendi aktivite planı Map'te saklanır; yoksa yeni oluştur
        if (!cityPlans.containsKey(city.getName())) {
            cityPlans.put(city.getName(), new ActivityPlan(city.getName() + " Root Plan"));
        }
        rootPlan = cityPlans.get(city.getName());

        // Seçili şehrin bilgilerini üst başlığa yaz
        previewHeaderLabel.setText(String.format("Preview for %s | Weather: %s | Temp: %.1f'C",
            city.getName(), city.getCurrentWeatherState(), city.getCurrentTemperature()));

        treeTitleLabel.setText("Activity Tree for " + city.getName());
        treeActiveCityLabel.setText("Active city: " + city.getName());

        refreshPlanView();    // Ağaç görünümünü yenile
        updatePlanPreview();  // Önizleme metnini yenile
    }

    /**
     * Sütun 3'teki metin önizlemesini günceller.
     * Aktif şehrin bilgilerini, seçili aktiviteleri ve tahmini toplam
     * süre ile maliyeti gösterir. Decorator deseninin sağladığı maliyet/süre
     * değerleri burada önizleme olarak kullanıcıya sunulur.
     */
    private void updatePlanPreview() {
        if (activeCity == null) {
            planPreviewArea.setText("");
            previewTotalLabel.setText("Preview total: 0.0 hours / $0.0");
            return;
        }

        StringBuilder sb = new StringBuilder();
        // Şehir özet bilgileri
        sb.append("Active city: ").append(activeCity.getName()).append("\n");
        sb.append("Population: ").append(activeCity.getPopulation()).append("\n");
        sb.append("Area: ").append(activeCity.getArea()).append(" km²\n");
        sb.append("Current weather: ").append(activeCity.getCurrentWeatherState())
          .append(", ").append(activeCity.getCurrentTemperature()).append("'C\n\n");

        sb.append("Selected activities to be added under the active composite node:\n");

        double previewCost = 0;
        double previewTime = 0;
        List<String> selectedNames = new ArrayList<>();

        // Seçili her onay kutusu için süre ve maliyeti topla
        if (cbMuseum.isSelected()) {
            sb.append("- Visit Museum (2.0 h, $18.0)\n");
            previewTime += 2.0; previewCost += 18.0;
            selectedNames.add("Visit Museum");
        }
        if (cbCenter.isSelected()) {
            sb.append("- Visit City Center (1.5 h, $0.0)\n");
            previewTime += 1.5; previewCost += 0.0;
            selectedNames.add("Visit City Center");
        }
        if (cbMall.isSelected()) {
            sb.append("- Visit Shopping Mall (2.0 h, $25.0)\n");
            previewTime += 2.0; previewCost += 25.0;
            selectedNames.add("Visit Shopping Mall");
        }
        if (cbPark.isSelected()) {
            sb.append("- Walk in the Park (1.0 h, $7.0)\n");
            previewTime += 1.0; previewCost += 7.0;
            selectedNames.add("Walk in the Park");
        }

        sb.append("\n");

        // Aktivitelerin ekleneceği hedef composite düğümü belirle
        TreeItem<PlanComponent> selectedNode = planTreeView.getSelectionModel().getSelectedItem();
        PlanComponent targetComposite = null;
        if (selectedNode != null && selectedNode.getValue() instanceof ActivityPlan) {
            // Seçili düğüm bir composite plan ise doğrudan hedef
            targetComposite = selectedNode.getValue();
        } else if (selectedNode != null && selectedNode.getParent() != null && selectedNode.getParent().getValue() instanceof ActivityPlan) {
            // Seçili düğüm bir yaprak ise, üst (ebeveyn) composite düğümü hedef al
            targetComposite = selectedNode.getParent().getValue();
        } else {
            // Hiçbir şey seçili değilse kök plan hedef
            targetComposite = rootPlan;
        }

        String targetName = targetComposite != null ? targetComposite.getName() : "None";
        sb.append("Active composite target: ").append(targetName).append("\n\n");

        // Decorator deseni özeti: Temel ziyaret + seçili eklentiler
        sb.append("Decorator-based preview summary:\n");
        sb.append("Base plan for visiting ").append(activeCity.getName());
        for (String name : selectedNames) {
            sb.append(", ").append(name);
        }
        sb.append("\n");
        sb.append("Total time: ").append(previewTime).append(" hours\n");
        sb.append("Total cost: $").append(previewCost).append("\n");

        planPreviewArea.setText(sb.toString());
        previewTotalLabel.setText(String.format("Preview total: %.1f hours / $%.1f", previewTime, previewCost));
    }

    /**
     * "Add Selected Activities" butonuna basıldığında çalışır.
     * Seçili her onay kutusu için:
     *  1. Decorator deseni: BaseCityVisit üzerine MuseumVisit / CityCenterVisit vb. sarılır.
     *  2. Composite deseni: ActivityLeaf olarak ağaca eklenir.
     *  3. Command deseni: AddComponentCommand ile geri alınabilir şekilde eklenir.
     * İşlem sonunda onay kutuları temizlenir ve ağaç yenilenir.
     */
    private void addSelectedActivities() {
        if (activeCity == null || rootPlan == null) return;

        // Hedef composite düğümü belirle (önizleme ile aynı mantık)
        TreeItem<PlanComponent> selectedNode = planTreeView.getSelectionModel().getSelectedItem();
        PlanComponent targetComposite = null;
        if (selectedNode != null && selectedNode.getValue() instanceof ActivityPlan) {
            targetComposite = selectedNode.getValue();
        } else if (selectedNode != null && selectedNode.getParent() != null && selectedNode.getParent().getValue() instanceof ActivityPlan) {
            targetComposite = selectedNode.getParent().getValue();
        } else {
            targetComposite = rootPlan;
        }

        if (targetComposite == null) return;

        // Müze: BaseCityVisit + MuseumVisit dekoratörü → ActivityLeaf → Command ile ekle
        if (cbMuseum.isSelected()) {
            CityVisit base = new BaseCityVisit(activeCity);
            CityVisit decorated = new MuseumVisit(base);
            ActivityLeaf leaf = new ActivityLeaf("Visit Museum", decorated.getCost(), decorated.getTimeInHours());
            commandManager.executeCommand(new AddComponentCommand(targetComposite, leaf));
        }
        // Şehir Merkezi: BaseCityVisit + CityCenterVisit dekoratörü
        if (cbCenter.isSelected()) {
            CityVisit base = new BaseCityVisit(activeCity);
            CityVisit decorated = new CityCenterVisit(base);
            ActivityLeaf leaf = new ActivityLeaf("Visit City Center", decorated.getCost(), decorated.getTimeInHours());
            commandManager.executeCommand(new AddComponentCommand(targetComposite, leaf));
        }
        // AVM: BaseCityVisit + ShoppingMallVisit dekoratörü
        if (cbMall.isSelected()) {
            CityVisit base = new BaseCityVisit(activeCity);
            CityVisit decorated = new ShoppingMallVisit(base);
            ActivityLeaf leaf = new ActivityLeaf("Visit Shopping Mall", decorated.getCost(), decorated.getTimeInHours());
            commandManager.executeCommand(new AddComponentCommand(targetComposite, leaf));
        }
        // Park: BaseCityVisit + ParkVisit dekoratörü
        if (cbPark.isSelected()) {
            CityVisit base = new BaseCityVisit(activeCity);
            CityVisit decorated = new ParkVisit(base);
            ActivityLeaf leaf = new ActivityLeaf("Walk in the Park", decorated.getCost(), decorated.getTimeInHours());
            commandManager.executeCommand(new AddComponentCommand(targetComposite, leaf));
        }

        // Tüm onay kutularını temizle
        cbMuseum.setSelected(false);
        cbCenter.setSelected(false);
        cbMall.setSelected(false);
        cbPark.setSelected(false);

        refreshPlanView(); // Ağaç ve durum çubuğunu güncelle
    }

    /**
     * Kullanıcının metin alanlarına girdiği özel aktiviteyi ağaca ekler.
     * Girilen ad, maliyet ve süre değerleri okunur; doğrudan ActivityLeaf
     * olarak oluşturulur ve Command deseni ile hedef düğüme eklenir.
     * Sayı formatı hatalıysa sessizce görmezden gelir (NumberFormatException).
     */
    private void addCustomActivity() {
        if (activeCity == null || rootPlan == null) return;
        try {
            String label = customLabelTF.getText();
            double cost = Double.parseDouble(customCostTF.getText());
            double hours = Double.parseDouble(customHoursTF.getText());

            if (label.isEmpty()) return; // Ad boşsa işlem yapma

            // Hedef composite düğümü belirle
            TreeItem<PlanComponent> selectedNode = planTreeView.getSelectionModel().getSelectedItem();
            PlanComponent targetComposite = null;
            if (selectedNode != null && selectedNode.getValue() instanceof ActivityPlan) {
                targetComposite = selectedNode.getValue();
            } else if (selectedNode != null && selectedNode.getParent() != null && selectedNode.getParent().getValue() instanceof ActivityPlan) {
                targetComposite = selectedNode.getParent().getValue();
            } else {
                targetComposite = rootPlan;
            }

            // Özel aktiviteyi yaprak düğüm olarak oluştur ve Command ile ekle
            ActivityLeaf leaf = new ActivityLeaf(label, cost, hours);
            commandManager.executeCommand(new AddComponentCommand(targetComposite, leaf));

            // Metin alanlarını temizle
            customLabelTF.clear();
            customCostTF.clear();
            customHoursTF.clear();

            refreshPlanView();

        } catch (NumberFormatException ignored) {
            // Geçersiz sayı formatı durumunda sessiz kal; kullanıcıya alert gösterilmez
        }
    }

    /**
     * "Add Plan Node" butonuna basıldığında yeni bir bileşik plan düğümü ekler.
     * ActivityPlan bir Composite nesnesidir: içine başka ActivityPlan veya
     * ActivityLeaf eklenebilir. Bu sayede iç içe geçmiş plan grupları oluşturulur.
     */
    private void addPlanNode() {
        if (activeCity == null || rootPlan == null) return;
        String name = planNameTF.getText();
        if (name == null || name.trim().isEmpty()) return;

        // Hedef composite düğümü belirle
        TreeItem<PlanComponent> selectedNode = planTreeView.getSelectionModel().getSelectedItem();
        PlanComponent targetComposite = null;
        if (selectedNode != null && selectedNode.getValue() instanceof ActivityPlan) {
            targetComposite = selectedNode.getValue();
        } else if (selectedNode != null && selectedNode.getParent() != null && selectedNode.getParent().getValue() instanceof ActivityPlan) {
            targetComposite = selectedNode.getParent().getValue();
        } else {
            targetComposite = rootPlan;
        }

        // Yeni composite plan düğümü oluştur ve Command ile ekle
        ActivityPlan newPlan = new ActivityPlan(name);
        commandManager.executeCommand(new AddComponentCommand(targetComposite, newPlan));
        planNameTF.clear();
        refreshPlanView();
    }

    /**
     * Ağaçta seçili düğümü kaldırır.
     * Kök düğüm (rootTreeItem) silinemez; koruma mevcuttur.
     * RemoveComponentCommand ile geri alınabilir şekilde silinir.
     */
    private void removeSelectedNode() {
        TreeItem<PlanComponent> selectedItem = planTreeView.getSelectionModel().getSelectedItem();
        if (selectedItem == null || selectedItem == rootTreeItem) return; // Kök silinemez

        PlanComponent targetComp = selectedItem.getValue();
        TreeItem<PlanComponent> parentItem = selectedItem.getParent();

        if (parentItem != null) {
            PlanComponent parentPlan = parentItem.getValue();
            if (parentPlan != null && targetComp != null) {
                // Üst düğümden seçili bileşeni kaldır (Command ile)
                commandManager.executeCommand(new RemoveComponentCommand(parentPlan, targetComp));
                refreshPlanView();
            }
        }
    }

    /**
     * Seçili düğümü ağaçta yukarı (-1) veya aşağı (+1) taşır.
     * MoveComponentCommand ile geri alınabilir şekilde taşınır.
     * İşlem sonunda taşınan düğüm tekrar seçili hale getirilir.
     */
    private void moveNode(int direction) {
        TreeItem<PlanComponent> selectedItem = planTreeView.getSelectionModel().getSelectedItem();
        if (selectedItem == null || selectedItem == rootTreeItem) return;

        PlanComponent targetComp = selectedItem.getValue();
        TreeItem<PlanComponent> parentItem = selectedItem.getParent();

        if (parentItem != null) {
            PlanComponent parentPlan = parentItem.getValue();
            if (parentPlan != null && targetComp != null) {
                // Taşıma komutunu çalıştır
                commandManager.executeCommand(new MoveComponentCommand(parentPlan, targetComp, direction));
                refreshPlanView();
                // Taşınan öğeyi yeni konumunda seçili tut
                selectItemByComponent(rootTreeItem, targetComp);
            }
        }
    }

    /**
     * Ağaçta belirli bir PlanComponent nesnesine karşılık gelen TreeItem'ı bulur
     * ve onu seçili hale getirir. Özyinelemeli (recursive) olarak tüm ağacı tarar.
     */
    private void selectItemByComponent(TreeItem<PlanComponent> root, PlanComponent comp) {
        if (root == null) return;
        if (root.getValue() == comp) {
            planTreeView.getSelectionModel().select(root); // Bulundu, seç
            return;
        }
        // Alt düğümleri özyinelemeli olarak ara
        for (TreeItem<PlanComponent> child : root.getChildren()) {
            selectItemByComponent(child, comp);
        }
    }

    /**
     * Plan ağacı görünümünü sıfırdan yeniden oluşturur.
     * rootPlan (Composite kök) yoksa ağacı boşaltır.
     * buildTreeView() ile model verisi, TreeItem hiyerarşisine dönüştürülür.
     */
    private void refreshPlanView() {
        if (rootPlan == null) {
            planTreeView.setRoot(null);
            updateStatus();
            return;
        }

        // Kök TreeItem oluştur ve model ağacından görsel ağacı inşa et
        rootTreeItem = new TreeItem<>(rootPlan);
        buildTreeView(rootTreeItem, rootPlan);
        rootTreeItem.setExpanded(true); // Kök varsayılan olarak açık
        planTreeView.setRoot(rootTreeItem);
        planTreeView.refresh();
        updatePlanPreview(); // Önizlemeyi de güncelle
        updateStatus();      // Durum çubuğunu güncelle
    }

    /**
     * Durum çubuğundaki etiketleri günceller:
     * - Sol etiket: Aktif ağacın toplam süre ve maliyeti (Composite.getTotalTime/Cost)
     * - Sağ etiket: Undo ve Redo için mevcut komut açıklamaları (Command deseni)
     */
    private void updateStatus() {
        if (rootPlan != null && currentTreeTotalLabel != null) {
            currentTreeTotalLabel.setText(String.format("Current city tree total: %.1f hours / $%.1f", rootPlan.getTotalTime(), rootPlan.getTotalCost()));
        } else if (currentTreeTotalLabel != null) {
            currentTreeTotalLabel.setText("Current city tree total: 0.0 hours / $0.0");
        }
        if (undoRedoLabel != null) {
            // CommandManager'dan geri alma ve yeniden yapma açıklamalarını al
            undoRedoLabel.setText(String.format("Undo: %s | Redo: %s", commandManager.getUndoDescription(), commandManager.getRedoDescription()));
        }
    }

    /**
     * Model ağacını (PlanComponent hiyerarşisi) görsel TreeItem ağacına dönüştürür.
     * Composite deseninde PlanComponent.getChildren() yöntemi; hem ActivityPlan
     * hem de ActivityLeaf için tanımlıdır (yaprak düğümlerde boş liste döner).
     * Özyinelemeli çağrı ile tüm ağaç derinliklerine inilir.
     */
    private void buildTreeView(TreeItem<PlanComponent> treeNode, PlanComponent component) {
        if (component.getChildren() != null) {
            for (PlanComponent child : component.getChildren()) {
                TreeItem<PlanComponent> item = new TreeItem<>(child);
                treeNode.getChildren().add(item);
                item.setExpanded(true); // Tüm düğümler varsayılan açık
                buildTreeView(item, child); // Alt düğümleri özyinelemeli ekle
            }
        }
    }
}
