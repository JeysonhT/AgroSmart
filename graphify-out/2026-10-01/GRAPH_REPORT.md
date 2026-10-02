# Graph Report - AgroSmart  (2026-09-30)

## Corpus Check
- 149 files · ~516,580 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 1019 nodes · 1601 edges · 70 communities (49 shown, 21 thin omitted)
- Extraction: 94% EXTRACTED · 6% INFERRED · 0% AMBIGUOUS · INFERRED: 101 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `4914786a`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- [[_COMMUNITY_User Details & Profile Data|User Details & Profile Data]]
- [[_COMMUNITY_Home & Crop Presentation|Home & Crop Presentation]]
- [[_COMMUNITY_Crop Loading & Services|Crop Loading & Services]]
- [[_COMMUNITY_Fertilizer Data & Services|Fertilizer Data & Services]]
- [[_COMMUNITY_Network Connectivity & DTOs|Network Connectivity & DTOs]]
- [[_COMMUNITY_MML Stats Async Services|MML Stats Async Services]]
- [[_COMMUNITY_Architecture, Room DB & Hilt|Architecture, Room DB & Hilt]]
- [[_COMMUNITY_Detection Results Persistence|Detection Results Persistence]]
- [[_COMMUNITY_Google Authentication Service|Google Authentication Service]]
- [[_COMMUNITY_Network Security & History Models|Network Security & History Models]]
- [[_COMMUNITY_Profile & Deficiency UI Screens|Profile & Deficiency UI Screens]]
- [[_COMMUNITY_Navigation & App Destinations|Navigation & App Destinations]]
- [[_COMMUNITY_Detection Service Instrumentation Tests|Detection Service Instrumentation Tests]]
- [[_COMMUNITY_AI Recommendation Service & DTOs|AI Recommendation Service & DTOs]]
- [[_COMMUNITY_User Legacy Domain Models|User Legacy Domain Models]]
- [[_COMMUNITY_Detection ViewModel Interface|Detection ViewModel Interface]]
- [[_COMMUNITY_Personal Data UI Components|Personal Data UI Components]]
- [[_COMMUNITY_Edit Profile UI Components|Edit Profile UI Components]]
- [[_COMMUNITY_Recommendation Async Use Cases|Recommendation Async Use Cases]]
- [[_COMMUNITY_Detection ViewModel & Image Processing|Detection ViewModel & Image Processing]]
- [[_COMMUNITY_Diagnosis History Repository Contract|Diagnosis History Repository Contract]]
- [[_COMMUNITY_Diagnosis History Unit Tests|Diagnosis History Unit Tests]]
- [[_COMMUNITY_Diagnosis History Legacy Callbacks|Diagnosis History Legacy Callbacks]]
- [[_COMMUNITY_Main Activity & Bottom Navigation|Main Activity & Bottom Navigation]]
- [[_COMMUNITY_Diagnosis History Room Repository|Diagnosis History Room Repository]]
- [[_COMMUNITY_Diagnosis Repository Operations|Diagnosis Repository Operations]]
- [[_COMMUNITY_Image Cache Management|Image Cache Management]]
- [[_COMMUNITY_Diagnosis CRUD Use Cases|Diagnosis CRUD Use Cases]]
- [[_COMMUNITY_Diagnosis History Flow & Persistence|Diagnosis History Flow & Persistence]]
- [[_COMMUNITY_Recommendation Update Use Cases|Recommendation Update Use Cases]]
- [[_COMMUNITY_Image Base64 Encoding Utilities|Image Base64 Encoding Utilities]]
- [[_COMMUNITY_Room Date Type Converters|Room Date Type Converters]]
- [[_COMMUNITY_Coroutines & Dispatchers DI Module|Coroutines & Dispatchers DI Module]]
- [[_COMMUNITY_Diagnosis History Domain Queries|Diagnosis History Domain Queries]]
- [[_COMMUNITY_Diagnosis History Legacy Use Cases|Diagnosis History Legacy Use Cases]]
- [[_COMMUNITY_Detection UI State & UDF|Detection UI State & UDF]]
- [[_COMMUNITY_Android Instrumented Context Tests|Android Instrumented Context Tests]]
- [[_COMMUNITY_PDF Generation Utilities|PDF Generation Utilities]]
- [[_COMMUNITY_Nutritional Deficiency ML & News Domain|Nutritional Deficiency ML & News Domain]]
- [[_COMMUNITY_Application Lifecycle Setup|Application Lifecycle Setup]]
- [[_COMMUNITY_Hilt Repository Binding Module|Hilt Repository Binding Module]]
- [[_COMMUNITY_Save Diagnosis Use Cases|Save Diagnosis Use Cases]]
- [[_COMMUNITY_Memory Monitoring Utilities|Memory Monitoring Utilities]]
- [[_COMMUNITY_Diagnosis Deletion Actions|Diagnosis Deletion Actions]]
- [[_COMMUNITY_UI Click Interaction Modes|UI Click Interaction Modes]]
- [[_COMMUNITY_Diagnosis Remote Legacy Implementation|Diagnosis Remote Legacy Implementation]]
- [[_COMMUNITY_Community 51|Community 51]]
- [[_COMMUNITY_Community 52|Community 52]]
- [[_COMMUNITY_Community 53|Community 53]]
- [[_COMMUNITY_Community 54|Community 54]]
- [[_COMMUNITY_Community 55|Community 55]]
- [[_COMMUNITY_Community 56|Community 56]]
- [[_COMMUNITY_Community 57|Community 57]]
- [[_COMMUNITY_Community 58|Community 58]]
- [[_COMMUNITY_Community 59|Community 59]]
- [[_COMMUNITY_Community 60|Community 60]]
- [[_COMMUNITY_Community 61|Community 61]]
- [[_COMMUNITY_Community 62|Community 62]]
- [[_COMMUNITY_Community 63|Community 63]]
- [[_COMMUNITY_Community 64|Community 64]]
- [[_COMMUNITY_Community 65|Community 65]]
- [[_COMMUNITY_Community 66|Community 66]]
- [[_COMMUNITY_Community 67|Community 67]]
- [[_COMMUNITY_Community 68|Community 68]]
- [[_COMMUNITY_Community 69|Community 69]]

## God Nodes (most connected - your core abstractions)
1. `DiagnosisHistory` - 43 edges
2. `UserDetails` - 40 edges
3. `Deficiency` - 29 edges
4. `MMLStats` - 26 edges
5. `DetectionViewModel` - 25 edges
6. `AgroSmartNavHost()` - 23 edges
7. `User` - 21 edges
8. `DetectionViewModelTest` - 19 edges
9. `AgroSmartTheme()` - 18 edges
10. `IDetectionViewModel` - 17 edges

## Surprising Connections (you probably didn't know these)
- `toDomain()` --references--> `DiagnosisHistory`  [EXTRACTED]
  app/src/main/java/com/example/agrosmart/data/mapper/DiagnosisHistoryMapper.kt → app/src/main/java/com/example/agrosmart/domain/models/DiagnosisHistory.kt
- `ProfileRouteScreen()` --calls--> `AuthRepositoryImpl`  [INFERRED]
  app/src/main/java/com/example/agrosmart/presentation/ui/components/profile/ProfileRouteScreen.kt → app/src/main/java/com/example/agrosmart/data/repository/impl/AuthRepositoryImpl.java
- `AgroSmartNavHost()` --calls--> `CropCarouselData`  [INFERRED]
  app/src/main/java/com/example/agrosmart/presentation/navigation/AgroSmartNavHost.kt → app/src/main/java/com/example/agrosmart/domain/designModels/CropCarouselData.kt
- `AgroSmartNavHost()` --calls--> `CropInfoRoute`  [INFERRED]
  app/src/main/java/com/example/agrosmart/presentation/navigation/AgroSmartNavHost.kt → app/src/main/java/com/example/agrosmart/presentation/navigation/AgroSmartDestinations.kt
- `AgroSmartNavHost()` --calls--> `DeficiencyInfoRoute`  [INFERRED]
  app/src/main/java/com/example/agrosmart/presentation/navigation/AgroSmartNavHost.kt → app/src/main/java/com/example/agrosmart/presentation/navigation/AgroSmartDestinations.kt

## Import Cycles
- None detected.

## Hyperedges (group relationships)
- **Clean Architecture Trio (Domain, Data, Presentation)** — agents_clean_architecture, app_src_main_java_com_example_agrosmart_domain_models_crop_crop, app_src_main_java_com_example_agrosmart_data_local_room_entity_diagnosishistoryentity_diagnosishistoryentity [INFERRED 0.95]

## Communities (70 total, 21 thin omitted)

### Community 0 - "User Details & Profile Data"
Cohesion: 0.05
Nodes (34): Any, String, OnUserDetailsLoaded, CompletableFuture, String, UserDetailsLocalService, UserDetailsDto, CompletableFuture (+26 more)

### Community 1 - "Home & Crop Presentation"
Cohesion: 0.06
Nodes (38): IHomeViewModel, List, LiveData, CropCarouselData, AgroSmartTheme(), Boolean, CropCard(), CropCardPreview() (+30 more)

### Community 2 - "Crop Loading & Services"
Cohesion: 0.08
Nodes (16): CropDTO, String, CropRepositoryImpl, DocumentSnapshot, List, String, T, Crop (+8 more)

### Community 3 - "Fertilizer Data & Services"
Cohesion: 0.06
Nodes (24): FertilizerDTO, FertilizerMapper, DocumentSnapshot, String, FertilizerRepositoryImpl, List, T, Fertilizer (+16 more)

### Community 4 - "Network Connectivity & DTOs"
Cohesion: 0.17
Nodes (11): Boolean, Context, NetworkChecker, DeficienciesService, CompletableFuture, MutableList, DeficiencyUseCase, CompletableFuture (+3 more)

### Community 5 - "MML Stats Async Services"
Cohesion: 0.07
Nodes (26): CompletableFuture, Void, MMLStatsService, CompletableFuture, FirebaseFirestore, Override, String, Void (+18 more)

### Community 6 - "Architecture, Room DB & Hilt"
Cohesion: 0.08
Nodes (16): AgroSmartDatabase, DiagnosisHistoryDao, Flow, Int, List, Long, String, DiagnosisHistoryEntity (+8 more)

### Community 7 - "Detection Results Persistence"
Cohesion: 0.08
Nodes (24): DetectionResultService, Boolean, CompletableFuture, DetectionResultRepositoryImpl, Boolean, CompletableFuture, FirebaseFirestore, Override (+16 more)

### Community 8 - "Google Authentication Service"
Cohesion: 0.08
Nodes (20): AuthResultListener, Exception, GoogleAuthService, Context, Intent, AuthRepositoryImpl, Context, Intent (+12 more)

### Community 9 - "Network Security & History Models"
Cohesion: 0.08
Nodes (26): OkHttpClient, UnsafeOkhttpClient, DiagnosisHistoryListView, Any, Boolean, Int, String, Builder (+18 more)

### Community 10 - "Profile & Deficiency UI Screens"
Cohesion: 0.07
Nodes (26): Modifier, ProfileRouteScreen(), DeficiencyViewModel, Context, StateFlow, FertilizerViewModel, StateFlow, StateFlow (+18 more)

### Community 11 - "Navigation & App Destinations"
Cohesion: 0.06
Nodes (37): CameraRoute, ConfigRoute, CropInfoRoute, DeficienciesRoute, DeficiencyInfoRoute, DetectionRoute, DiagnosisInfoRoute, EditProfileRoute (+29 more)

### Community 12 - "Detection Service Instrumentation Tests"
Cohesion: 0.29
Nodes (7): DetectionServiceInstrumentedTest, Before, Context, List, RunWith, String, Test

### Community 13 - "AI Recommendation Service & DTOs"
Cohesion: 0.07
Nodes (14): PreguntaRequest, RespuestaResponse, RecommendationService, String, RecommendationServiceImpl, OkHttpClient, NetworkModule, Respuesta (+6 more)

### Community 14 - "User Legacy Domain Models"
Cohesion: 0.13
Nodes (17): AllArgsConstructor, Getter, Long, Setter, String, Uri, User, AccountMenuSection() (+9 more)

### Community 15 - "Detection ViewModel Interface"
Cohesion: 0.16
Nodes (4): IDetectionViewModel, ByteArray, Consumer, String

### Community 16 - "Personal Data UI Components"
Cohesion: 0.10
Nodes (19): Modifier, PersonalDataRouteScreen(), Boolean, Int, List, Modifier, String, PersonalDataScreen() (+11 more)

### Community 17 - "Edit Profile UI Components"
Cohesion: 0.11
Nodes (18): EditProfileRouteScreen(), Modifier, String, EditProfileScreen(), EditProfileScreenPreview(), EditProfileTopBar(), List, Modifier (+10 more)

### Community 19 - "Detection ViewModel & Image Processing"
Cohesion: 0.06
Nodes (21): CameraRouteScreen(), Modifier, CameraScreen(), Boolean, Modifier, String, DetectionViewModel, Bitmap (+13 more)

### Community 20 - "Diagnosis History Repository Contract"
Cohesion: 0.19
Nodes (7): DiagnosisHistoryRepository, Flow, Int, List, Result, String, Unit

### Community 21 - "Diagnosis History Unit Tests"
Cohesion: 0.16
Nodes (9): DiagnosisHistory, Int, FakeDiagnosisHistoryRepository, Flow, Int, List, Result, String (+1 more)

### Community 23 - "Main Activity & Bottom Navigation"
Cohesion: 0.17
Nodes (11): MainActivity, AgroSmartBottomBar(), BottomNavItem, Detection, Home, Modifier, Profile, Bundle (+3 more)

### Community 24 - "Diagnosis History Room Repository"
Cohesion: 0.16
Nodes (5): DeficiencyDTO, DeficiencyMapper, DocumentSnapshot, String, DeficiencyMapperTest

### Community 25 - "Diagnosis Repository Operations"
Cohesion: 0.20
Nodes (7): DiagnosisHistoryLocalRepositoryImpl, Flow, Int, List, Result, String, Unit

### Community 26 - "Image Cache Management"
Cohesion: 0.24
Nodes (6): ImageCacheManager, Bitmap, Boolean, ByteArray, Context, String

### Community 27 - "Diagnosis CRUD Use Cases"
Cohesion: 0.17
Nodes (6): DeleteDiagnosisUseCase, GetDiagnosisHistoryUseCase, SaveDiagnosisUseCase, UpdateDiagnosisRecommendationUseCase, DiagnosisHistoryUseCaseKotlinTest, DetectionViewModelTest

### Community 28 - "Diagnosis History Flow & Persistence"
Cohesion: 0.29
Nodes (3): DiagnosisHistoryCallback, Exception, List

### Community 29 - "Recommendation Update Use Cases"
Cohesion: 0.50
Nodes (3): Result, String, Unit

### Community 30 - "Image Base64 Encoding Utilities"
Cohesion: 0.47
Nodes (3): ImageEncoder, ByteArray, String

### Community 31 - "Room Date Type Converters"
Cohesion: 0.47
Nodes (3): DateConverters, Date, Long

### Community 33 - "Diagnosis History Domain Queries"
Cohesion: 0.33
Nodes (4): Flow, Int, List, Result

### Community 34 - "Diagnosis History Legacy Use Cases"
Cohesion: 0.20
Nodes (7): Bitmap, Context, TensorImage, Long, String, MMLResultDTO, FloatArray

### Community 35 - "Detection UI State & UDF"
Cohesion: 0.31
Nodes (5): DeficiencyRepositoryImpl, CompletableFuture, List, MutableList, T

### Community 36 - "Android Instrumented Context Tests"
Cohesion: 0.60
Nodes (3): ExampleInstrumentedTest, RunWith, Test

### Community 37 - "PDF Generation Utilities"
Cohesion: 0.40
Nodes (3): Context, List, PdfGenerator

### Community 40 - "Hilt Repository Binding Module"
Cohesion: 0.29
Nodes (5): Context, List, String, MlModule, ModelAgrosmart

### Community 41 - "Save Diagnosis Use Cases"
Cohesion: 0.50
Nodes (3): Result, String, Unit

### Community 51 - "Community 51"
Cohesion: 0.20
Nodes (9): 1. Visión y Objetivos de la Migración, 2.1. Capa de Dominio (`domain`) — El Núcleo Puro, 2.2. Capa de Datos (`data`) — Fuentes de Información y Mapeos, 2.3. Capa de Presentación (`presentation`) — MVVM y Estado Unidireccional (UDF), 2. Estructura de Capas (Clean Architecture), 3. Guía de Reemplazo Tecnológico en AgroSmart, 4. Estrategia de Migración Paso a Paso, 5. Convenciones de Código y Nomenclatura (+1 more)

### Community 52 - "Community 52"
Cohesion: 0.33
Nodes (5): Agrosmart App Móvil  🌽🫘🌱, Arquitectura de la aplicación, Funcionamiento básico de la app., Requerimientos técnicos mínimos, Tecnologías de la aplicación

### Community 63 - "Community 63"
Cohesion: 0.39
Nodes (7): DeficienciesScreen(), DeficienciesScreenPreview(), DeficienciesTopBar(), DeficiencyCard(), Boolean, List, Modifier

### Community 64 - "Community 64"
Cohesion: 0.38
Nodes (4): DeficiencyRepository, CompletableFuture, List, MutableList

### Community 65 - "Community 65"
Cohesion: 0.40
Nodes (3): DetectionService, Bitmap, TensorImage

### Community 67 - "Community 67"
Cohesion: 0.40
Nodes (3): IMLDetectionService, Bitmap, TensorImage

### Community 68 - "Community 68"
Cohesion: 0.40
Nodes (3): DetectionUseCase, Bitmap, TensorImage

## Knowledge Gaps
- **32 isolated node(s):** `ClickMode`, `DiagnosisHistoryRemoteRepositoryImp`, `HomeRoute`, `DeficienciesRoute`, `FertilizersRoute` (+27 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **21 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `DetectionViewModel` connect `Detection ViewModel & Image Processing` to `Diagnosis CRUD Use Cases`, `Profile & Deficiency UI Screens`, `Navigation & App Destinations`, `Detection ViewModel Interface`?**
  _High betweenness centrality (0.314) - this node is a cross-community bridge._
- **Why does `AgroSmartNavHost()` connect `Navigation & App Destinations` to `Home & Crop Presentation`, `Fertilizer Data & Services`, `Network Security & History Models`, `Profile & Deficiency UI Screens`, `Personal Data UI Components`, `Edit Profile UI Components`, `Detection ViewModel & Image Processing`, `Main Activity & Bottom Navigation`, `Community 63`?**
  _High betweenness centrality (0.263) - this node is a cross-community bridge._
- **Why does `DiagnosisHistory` connect `Diagnosis History Unit Tests` to `Diagnosis History Domain Queries`, `Community 69`, `PDF Generation Utilities`, `Architecture, Room DB & Hilt`, `Network Security & History Models`, `Diagnosis Deletion Actions`, `Detection ViewModel Interface`, `Detection ViewModel & Image Processing`, `Diagnosis History Repository Contract`, `Diagnosis History Legacy Callbacks`, `Diagnosis Repository Operations`, `Diagnosis CRUD Use Cases`, `Diagnosis History Flow & Persistence`?**
  _High betweenness centrality (0.154) - this node is a cross-community bridge._
- **What connects `ClickMode`, `DiagnosisHistoryRemoteRepositoryImp`, `HomeRoute` to the rest of the system?**
  _32 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `User Details & Profile Data` be split into smaller, more focused modules?**
  _Cohesion score 0.052614052614052616 - nodes in this community are weakly interconnected._
- **Should `Home & Crop Presentation` be split into smaller, more focused modules?**
  _Cohesion score 0.05584415584415584 - nodes in this community are weakly interconnected._
- **Should `Crop Loading & Services` be split into smaller, more focused modules?**
  _Cohesion score 0.07692307692307693 - nodes in this community are weakly interconnected._