package com.example.agrosmart.presentation.viewmodels

import com.example.agrosmart.data.network.MMLStatsService
import com.example.agrosmart.data.network.RetrofitClient
import com.example.agrosmart.data.repository.impl.DiagnosisHistoryLocalRepositoryImpl
import com.example.agrosmart.data.repository.impl.MMLStatsRepositoryImpl
import com.example.agrosmart.data.repository.impl.RecommendationServiceImpl
import com.example.agrosmart.domain.usecase.CropsUseCase
import com.example.agrosmart.domain.usecase.DeleteDiagnosisUseCase
import com.example.agrosmart.domain.usecase.DetectionResultUseCase
import com.example.agrosmart.domain.usecase.DetectionUseCase
import com.example.agrosmart.domain.usecase.DiagnosisHistoryUseCase
import com.example.agrosmart.domain.usecase.GetDiagnosisHistoryUseCase
import com.example.agrosmart.domain.usecase.GetRecommendationUseCase
import com.example.agrosmart.domain.usecase.MMLStatsUseCase
import com.example.agrosmart.domain.usecase.SaveDiagnosisUseCase
import com.example.agrosmart.domain.usecase.UpdateDiagnosisRecommendationUseCase
import com.example.agrosmart.domain.usecase.UserDtlUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetectionFragmentViewModel @Inject constructor(
    getDiagnosisHistoryUseCase: GetDiagnosisHistoryUseCase,
    saveDiagnosisUseCase: SaveDiagnosisUseCase,
    deleteDiagnosisUseCase: DeleteDiagnosisUseCase,
    updateDiagnosisRecommendationUseCase: UpdateDiagnosisRecommendationUseCase,
    getRecommendationUseCase: GetRecommendationUseCase,
    cropsUseCase: CropsUseCase,
    detectionUseCase: DetectionUseCase,
    mmlUseCase: MMLStatsUseCase,
    drUseCase: DetectionResultUseCase,
    userDtlUseCase: UserDtlUseCase? = null,
    firebaseAuth: FirebaseAuth? = null
) : DetectionViewModel(
    getDiagnosisHistoryUseCase,
    saveDiagnosisUseCase,
    deleteDiagnosisUseCase,
    updateDiagnosisRecommendationUseCase,
    getRecommendationUseCase,
    cropsUseCase,
    detectionUseCase,
    mmlUseCase,
    drUseCase,
    userDtlUseCase,
    firebaseAuth
) {
    // Default constructor for legacy instantiations
    constructor() : this(
        getDiagnosisHistoryUseCase = GetDiagnosisHistoryUseCase(DiagnosisHistoryLocalRepositoryImpl()),
        saveDiagnosisUseCase = SaveDiagnosisUseCase(DiagnosisHistoryLocalRepositoryImpl()),
        deleteDiagnosisUseCase = DeleteDiagnosisUseCase(DiagnosisHistoryLocalRepositoryImpl()),
        updateDiagnosisRecommendationUseCase = UpdateDiagnosisRecommendationUseCase(DiagnosisHistoryLocalRepositoryImpl()),
        getRecommendationUseCase = GetRecommendationUseCase(RecommendationServiceImpl(RetrofitClient.recomendationService())),
        cropsUseCase = CropsUseCase(),
        detectionUseCase = DetectionUseCase(),
        mmlUseCase = MMLStatsUseCase(MMLStatsService(MMLStatsRepositoryImpl())),
        drUseCase = DetectionResultUseCase()
    )

    // Secondary constructor for RecommendationUnitTest
    constructor(getRecommendationUseCase: GetRecommendationUseCase) : this(
        getDiagnosisHistoryUseCase = GetDiagnosisHistoryUseCase(DiagnosisHistoryLocalRepositoryImpl()),
        saveDiagnosisUseCase = SaveDiagnosisUseCase(DiagnosisHistoryLocalRepositoryImpl()),
        deleteDiagnosisUseCase = DeleteDiagnosisUseCase(DiagnosisHistoryLocalRepositoryImpl()),
        updateDiagnosisRecommendationUseCase = UpdateDiagnosisRecommendationUseCase(DiagnosisHistoryLocalRepositoryImpl()),
        getRecommendationUseCase = getRecommendationUseCase,
        cropsUseCase = CropsUseCase(),
        detectionUseCase = DetectionUseCase(),
        mmlUseCase = MMLStatsUseCase(),
        drUseCase = DetectionResultUseCase()
    ) {
        this.legacyRecommendationUseCase = getRecommendationUseCase
    }

    // Secondary constructor for DiagnosisUnitTest & DetectionFragmentViewModelTest
    constructor(
        getRecommendationUseCase: GetRecommendationUseCase,
        diagnosisHistoryUseCase: DiagnosisHistoryUseCase?,
        cropsUseCase: CropsUseCase?
    ) : this(
        getDiagnosisHistoryUseCase = GetDiagnosisHistoryUseCase(DiagnosisHistoryLocalRepositoryImpl()),
        saveDiagnosisUseCase = SaveDiagnosisUseCase(DiagnosisHistoryLocalRepositoryImpl()),
        deleteDiagnosisUseCase = DeleteDiagnosisUseCase(DiagnosisHistoryLocalRepositoryImpl()),
        updateDiagnosisRecommendationUseCase = UpdateDiagnosisRecommendationUseCase(DiagnosisHistoryLocalRepositoryImpl()),
        getRecommendationUseCase = getRecommendationUseCase,
        cropsUseCase = cropsUseCase ?: CropsUseCase(),
        detectionUseCase = DetectionUseCase(),
        mmlUseCase = MMLStatsUseCase(),
        drUseCase = DetectionResultUseCase()
    ) {
        this.legacyRecommendationUseCase = getRecommendationUseCase
        this.legacyDiagnosisHistoryUseCase = diagnosisHistoryUseCase
        if (cropsUseCase != null) {
            this.cropsUseCase = cropsUseCase
        }
    }
}
